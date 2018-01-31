package com.example.android.hackvsit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.hackvsit.R;
import com.example.android.hackvsit.adapter.ProductAdapter;
import com.example.android.hackvsit.model.Machine;
import com.example.android.hackvsit.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MachineActivity extends AppCompatActivity implements ProductAdapter.OnClickHandler {

    private Machine mMachine;
    private String MACHINE = "machine";
    private String PRODUCT = "product";

    private static final String EXTRA_IMAGE_TRANSITION = "image_transition";
    private ProductAdapter mAdapter;

    @BindView(R.id.products_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        mMachine = startingIntent.getParcelableExtra(MACHINE);

        mAdapter = new ProductAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        for(Product product:mMachine.getmProducts()){
            if (mMachine.getmRecommendations().contains(product.getmName())){
                product.setmIsRecommended(true);
            }
            else{
                product.setmIsRecommended(false);
            }
        }

        mAdapter.setListData(mMachine.getmProducts());

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false));

        mRecyclerView.setHasFixedSize(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_machine,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_action_cart:
                Intent intent = new Intent(MachineActivity.this,CartActivity.class);
                intent.putExtra(MACHINE,mMachine);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(Product selectedProduct, ImageView imageView) {

        Intent intent = new Intent(MachineActivity.this,ProductActivity.class);
        String imageTransitionName = ViewCompat.getTransitionName(imageView);
        intent.putExtra(PRODUCT,selectedProduct);
        intent.putExtra(EXTRA_IMAGE_TRANSITION, imageTransitionName);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView,
                imageTransitionName);

        startActivity(intent, options.toBundle());



    }
}
