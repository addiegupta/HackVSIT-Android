package com.example.android.hackvsit.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.hackvsit.R;
import com.example.android.hackvsit.model.Machine;
import com.example.android.hackvsit.utils.JSONUtils;
import com.example.android.hackvsit.utils.QueryUtils;
import com.example.android.hackvsit.utils.Tools;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements QueryUtils.QueryUtilsCallback {


    private CameraSource mCameraSource;
    private static final String SHARED_PREFS_KEY = "shared_prefs";
    private boolean requestSent = false;

    @BindView(R.id.pb_main)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.camera_view)
    SurfaceView mCameraView;
    @BindView(R.id.fab_capture_link)
    FloatingActionButton mCaptureFab;
    private String MACHINE = "machine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        mCameraSource = new CameraSource.Builder(this, barcodeDetector).build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (Tools.checkPermission(MainActivity.this)) {
                    startCamera();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                mCameraSource.stop();
            }
        });

        barcodeDetector
                .setProcessor(new Detector.Processor<Barcode>() {
                                  @Override
                                  public void release() {

                                  }

                                  @Override
                                  public void receiveDetections(Detector.Detections<Barcode> detections) {
                                      final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                                      if (barcodes.size() != 0 && !requestSent) {
                                          requestSent = true;
                                          new Handler(Looper.getMainLooper()).post(new Runnable() {
                                              @Override
                                              public void run() {
                                                  mLoadingIndicator.setVisibility(View.VISIBLE);
                                                  Tools.toast(MainActivity.this, "QR Code recognised");
                                              }
                                          });
                                          String id = barcodes.valueAt(0).rawValue;
                                          RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                          QueryUtils.fetchMachineData(queue, id, MainActivity.this);
                                          mCameraView.setVisibility(GONE);
                                          mCameraSource.stop();
                                      }
                                  }
                              }
                );

        mCaptureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CaptureActivity.class));
            }
        });
        mCaptureFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Tools.toast(MainActivity.this,"Identify Products using Smart Vision");
                return true;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Tools.PERMISSION_REQUEST_KEY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Tools.toast(this, "Camera is needed for the app to function");
                    finish();
                }
            }
        }
    }

    private void startCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        else {
            try {
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException ie) {
                Timber.e(ie.getMessage());
            }
        }
    }

    @Override
    public void returnResponse(String response) {
        new JSONTask().execute(response);
    }

    @Override
    public void launchPayPortal(String price, String time) {

    }

    private class JSONTask extends AsyncTask<String ,Void,Machine>{

        @Override
        protected Machine doInBackground(String... strings) {

            return JSONUtils.getMachineDetails(strings[0]);
        }

        @Override
        protected void onPostExecute(Machine machine) {
            super.onPostExecute(machine);

            Intent intent = new Intent(MainActivity.this, MachineActivity.class);
            intent.putExtra(MACHINE, machine);
            startActivity(intent);
            finish();

        }
    }


}
