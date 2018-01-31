package com.example.android.hackvsit.adapter;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.hackvsit.R;
import com.example.android.hackvsit.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> mProductsList;
    private Context mContext;
    private OnClickHandler mClickHandler;

    public ProductAdapter(Context context, OnClickHandler handler) {
        mContext = context;
        mClickHandler = handler;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View createdView = inflater.inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(createdView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        String name,imageUrl,price,quantity;
        try {
            Product currentProduct = mProductsList.get(position);
            name = currentProduct.getmName();
            price = "\u20B9 " + String.valueOf(currentProduct.getmPrice());
            imageUrl = currentProduct.getmImageUrl();

            holder.mNameTextView.setText(name);
            holder.mPriceTextView.setText(price);
            if (!imageUrl.isEmpty()) {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_cart)
                        .error(R.drawable.placeholder_cart);

                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(options)
                        .thumbnail(0.5f)
                        .into(holder.mProductImageView);

            }

            //TODO Check requirement of this line
            //else holder.mProductImageView.setImageResource(R.drawable.R.drawable.placeholder_cart);

            ViewCompat.setTransitionName(holder.mProductImageView, name);
        } catch (Exception e) {

            e.printStackTrace();
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mProductsList == null ? 0 : mProductsList.size();
    }

    public interface OnClickHandler {
        void onClick(Product selectedProduct, ImageView imageView);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.tv_product_name)
        TextView mNameTextView;
        @BindView(R.id.tv_product_price)
        TextView mPriceTextView;
        @BindView(R.id.iv_product_image)
        ImageView mProductImageView;


        ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Product product = mProductsList.get(adapterPosition);
            Timber.d("Selected item :" + product.getmName());
            mClickHandler.onClick(product, mProductImageView);
        }

    }

    public void setListData(ArrayList<Product> list) {
        mProductsList = list;
        notifyDataSetChanged();
    }
}
