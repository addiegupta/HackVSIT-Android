package com.example.android.hackvsit.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.hackvsit.R;
import com.example.android.hackvsit.adapter.ItemAdapter;
import com.example.android.hackvsit.data.ProductColumns;
import com.example.android.hackvsit.model.Machine;
import com.example.android.hackvsit.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.hackvsit.data.ProductProvider.Products.URI_PRODUCTS;

public class CartActivity extends AppCompatActivity{

    private static final String MACHINE = "machine";
    private Machine mMachine;
    private ItemAdapter mAdapter;
    private ArrayList<Product> mProducts;

    @BindView(R.id.cart_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        mMachine = getIntent().getParcelableExtra(MACHINE);

        mAdapter = new ItemAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        prepareCart();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        mRecyclerView.setHasFixedSize(true);

    }
    private void prepareCart(){

        mProducts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(URI_PRODUCTS,null,null,null,null);
        if(cursor!=null&&cursor.getCount()!=0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductColumns.NAME));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ProductColumns.IMAGE));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.QUANTITY));
                int maxQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductColumns.MAX_QUANTITY));

                mProducts.add(new Product(name,imageUrl,quantity,price,maxQuantity));
                cursor.moveToNext();
            }
            cursor.close();
        }
       mAdapter.setListData(mProducts);
    }
}
