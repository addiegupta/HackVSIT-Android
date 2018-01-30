package com.example.android.hackvsit.ui;

import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.android.hackvsit.R;
import com.example.android.hackvsit.model.Nutrition;
import com.example.android.hackvsit.model.Product;
import com.example.android.hackvsit.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.example.android.hackvsit.data.ProductProvider.Products.URI_PRODUCTS;

public class ProductActivity extends AppCompatActivity {

    private Product mProduct;

    private static final String EXTRA_IMAGE_TRANSITION = "image_transition";

    @BindView(R.id.tv_quantity)
    TextView mQuantityTextView;
    @BindView(R.id.tv_stock_quantity)
    TextView mMaxQuantityTextView;
    @BindView(R.id.iv_product_image_toolbar)
    ImageView mImageView;
    @BindView(R.id.product_nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.detail_appbar)
    AppBarLayout mAppBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nutrition_cardview)
    CardView mNutritionCardView;
    @BindView(R.id.tv_nutritional_facts)
    TextView mNutritionTextView;
    @BindView(R.id.quantity_card_view)
    CardView mQuantityCardView;
    @BindView(R.id.tv_price)
    TextView mPriceTextView;
    @BindView(R.id.button_add_to_cart)
    Button mCartButton;
    @BindView(R.id.button_quantity_minus)
    Button mMinusButton;
    @BindView(R.id.button_quantity_plus)
    Button mPlusButton;


    private String PRODUCT = "product";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        mProduct = getIntent().getParcelableExtra(PRODUCT);
        loadImageAndTitle();

        manageToolbarAndAppbar();
        setNutritionalFacts();
        setTextViews();
        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase();
            }
        });
        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMinus();
            }
        });
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlus();
            }
        });
    }

    private void handleMinus(){
        int quantity = Integer.parseInt(mQuantityTextView.getText().toString());
        if(quantity!=1){
            mQuantityTextView.setText(String.valueOf(quantity-1));
        }
    }
    private void handlePlus(){
        int quantity = Integer.parseInt(mQuantityTextView.getText().toString());
        int max = mProduct.getmQuantity();
        if(quantity!=max){
            mQuantityTextView.setText(String.valueOf(quantity+1));
        }
    }

    private void purchase(){
        int quantity = Integer.parseInt(mQuantityTextView.getText().toString());

        ContentValues values = new ContentValues();
        values.put("id",mProduct.getmId());
        values.put("quantity",quantity);
        values.put("max_quantity",mProduct.getmQuantity());
        values.put("name",mProduct.getmName());
        values.put("price",mProduct.getmPrice());
        values.put("image",mProduct.getmImageUrl());

        getContentResolver().insert(URI_PRODUCTS,values);

        Tools.toast(this,"Added to Cart");
        finish();
    }
    private void setTextViews(){
        String price = "Price: \u20B9"+mProduct.getmPrice();
        String stock = "Quantity Available: " + mProduct.getmQuantity();
        mPriceTextView.setText(price);
        mMaxQuantityTextView.setText(stock);
    }
    private void setNutritionalFacts() {
        Nutrition nutrition = mProduct.getmNutrition();
        String nutritionalFacts =
                "\u2022 " + "Calcium: " + nutrition.getmCalcium() + "\n" +
                        "\u2022 " + "Calories: " + nutrition.getmCalories() + "\n" +
                        "\u2022 " + "Cholesterol: " + nutrition.getmCholesterol() + "\n" +
                        "\u2022 " + "Fat: " + nutrition.getmFat() + "\n" +
                        "\u2022 " + "Iron: " + nutrition.getmIron() + "\n" +
                        "\u2022 " + "Protein: " + nutrition.getmProtein();

        mNutritionTextView.setText(nutritionalFacts);
    }

    private void manageToolbarAndAppbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbar.setTitle(mProduct.getmName());

    }

    private void loadImageAndTitle() {
        String imageUrl = mProduct.getmImageUrl();

        if (!imageUrl.isEmpty()) {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_cart)
                    .error(R.drawable.placeholder_cart);

            Glide.with(this)
                    .load(imageUrl)
                    .thumbnail(0.5f)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mImageView.setImageResource(R.drawable.placeholder_cart);
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(mImageView);

        } else {
            mImageView.setImageResource(R.drawable.placeholder_cart);
            supportStartPostponedEnterTransition();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // To prevent "Unable to create layer for CardView" error
        mNutritionCardView.setVisibility(GONE);
        mQuantityCardView.setVisibility(GONE);
    }

}
