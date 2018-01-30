package com.example.android.hackvsit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.hackvsit.R;
import com.example.android.hackvsit.adapter.ProductAdapter;
import com.example.android.hackvsit.model.Machine;
import com.example.android.hackvsit.model.Product;
import com.example.android.hackvsit.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MachineActivity extends AppCompatActivity implements ProductAdapter.OnClickHandler {

    private Machine mMachine;
    private String MACHINE = "machine";
    private ProductAdapter mAdapter;

    @BindView(R.id.products_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_buy_products)
    Button mBuyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        mMachine = startingIntent.getParcelableExtra(MACHINE);
        Timber.d(mMachine.getmVendorId() + mMachine.getmProducts().get(0).getmName());

        mAdapter = new ProductAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListData(mMachine.getmProducts());

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false));

        mRecyclerView.setHasFixedSize(true);
    }


    @Override
    public void onClick(Product selectedProduct, ImageView imageView) {
        Timber.d("Selected product is :" + selectedProduct.getmName());
        Tools.toast(this,"Selected product is : " + selectedProduct.getmName());

    }
}
