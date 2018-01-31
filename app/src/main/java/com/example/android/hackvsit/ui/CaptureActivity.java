package com.example.android.hackvsit.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.hackvsit.R;
import com.example.android.hackvsit.utils.QueryUtils;
import com.mindorks.paracamera.Camera;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends AppCompatActivity implements QueryUtils.QueryUtilsCallback{

    @BindView(R.id.capture_button)
    FloatingActionButton mCaptureButton;
    @BindView(R.id.tv_capture_status)
    TextView mStatusTextView;
    @BindView(R.id.pb_capture)
    ProgressBar mLoadingBar;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        ButterKnife.bind(this);

        mStatusTextView.setText(R.string.computer_vision);

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("IMG_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);


        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the camera takePicture method to open the existing camera
                try {
                    camera.takePicture();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
        // Get the bitmap and image path onActivityResult of an activity or fragment
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == Camera.REQUEST_TAKE_PHOTO){
                Bitmap bitmap = camera.getCameraBitmap();
                if(bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    RequestQueue queue = Volley.newRequestQueue(CaptureActivity.this);
                    QueryUtils.sendImageData(queue,encodedImage,CaptureActivity.this);
                    mLoadingBar.setVisibility(View.VISIBLE);
                    mStatusTextView.setText(R.string.processing);

                }else{
                    Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
                }
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void returnResponse(String response) {
        mLoadingBar.setVisibility(View.GONE);
        mStatusTextView.setText(response);
    }

    @Override
    public void launchPayPortal(String price, String time) {

    }
}
