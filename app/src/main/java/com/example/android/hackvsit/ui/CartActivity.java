package com.example.android.hackvsit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aitorvs.android.fingerlock.FingerprintDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.hackvsit.R;
import com.example.android.hackvsit.adapter.ItemAdapter;
import com.example.android.hackvsit.data.ProductColumns;
import com.example.android.hackvsit.model.Machine;
import com.example.android.hackvsit.model.Product;
import com.example.android.hackvsit.utils.QueryUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.hackvsit.data.ProductProvider.Products.URI_PRODUCTS;

public class CartActivity extends AppCompatActivity implements FingerprintDialog.Callback ,QueryUtils.QueryUtilsCallback{

    private static final String MACHINE = "machine";
    private Machine mMachine;

    private String KEY_NAME = "scan_fingerprint";
    private ItemAdapter mAdapter;
    private ArrayList<Product> mProducts;
    private static final int PAY_REQUEST_CODE = 1;

    private static final String SHARED_PREFS_KEY = "shared_prefs";

    private String AUTH_ID = "auth_id";

    @BindView(R.id.cart_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_payment)
    Button mPayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        mMachine = getIntent().getParcelableExtra(MACHINE);

        mAdapter = new ItemAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        prepareCart();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mRecyclerView.setHasFixedSize(true);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

    }

    private void authenticateUser() {
        new FingerprintDialog.Builder()
                .with(CartActivity.this)    // context, must call
                .setKeyName(KEY_NAME)// String key name, must call
                .setCancelable(true)
                .setRequestCode(PAY_REQUEST_CODE)         // request code identifier, must call
                .show();                    // show the dialog


    }

    private void prepareCart() {

        mProducts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(URI_PRODUCTS, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductColumns.NAME));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ProductColumns.IMAGE));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.PRICE));
                int _id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns._ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.QUANTITY));
                int maxQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.MAX_QUANTITY));

                Product product = new Product(name, imageUrl, quantity, price, maxQuantity);
                product.setmId(_id);
                mProducts.add(product);
                cursor.moveToNext();
            }
            cursor.close();
        }
        mAdapter.setListData(mProducts);
    }

    @Override
    public void onFingerprintDialogAuthenticated() {
        Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();

        makePayment();
    }

    @Override
    public void onFingerprintDialogVerifyPassword(FingerprintDialog fingerprintDialog, String s) {

    }

    private void makePayment() {
        JSONObject parent = new JSONObject();
        try {
            SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);

            String userNumber = prefs.getString(AUTH_ID, "0");
            parent.put("user", userNumber);
            JSONArray array = new JSONArray();
            Cursor cursor = getContentResolver().query(URI_PRODUCTS, null, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    JSONObject object = new JSONObject();
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.ID));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.QUANTITY));
                    object.put(String.valueOf(id), String.valueOf(quantity));
                    array.put(object);
                    cursor.moveToNext();
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            parent.put("cart", array);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        QueryUtils.makePayment(queue,mMachine.getmId(),parent,null);

    }

    @Override
    public void onFingerprintDialogStageUpdated(FingerprintDialog fingerprintDialog, FingerprintDialog.Stage stage) {

    }

    @Override
    public void onFingerprintDialogCancelled() {

    }


    @Override
    public void setupMachine(Machine machine) {

    }

    @Override
    public void launchPayPortal() {
        startActivity(new Intent(CartActivity.this,PayActivity.class));
        finish();
    }
}
