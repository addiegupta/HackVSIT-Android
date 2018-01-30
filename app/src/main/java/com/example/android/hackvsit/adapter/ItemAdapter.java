package com.example.android.hackvsit.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.hackvsit.R;
import com.example.android.hackvsit.data.ProductColumns;
import com.example.android.hackvsit.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.example.android.hackvsit.data.ProductProvider.Products.URI_PRODUCTS;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Product> mProductsList;
    private Context mContext;

    public ItemAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View createdView = inflater.inflate(R.layout.cart_list_item, parent, false);
        return new ItemViewHolder(createdView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String name, imageUrl, price, quantity;
        try {
            Product currentProduct = mProductsList.get(position);
            name = currentProduct.getmName();
            price = "\u20B9 " + String.valueOf(currentProduct.getmPrice() * currentProduct.getmQuantity());
            quantity = String.valueOf(currentProduct.getmQuantity());
            imageUrl = currentProduct.getmImageUrl();

            holder.mNameTextView.setText(name);
            holder.mPriceTextView.setText(price);
            holder.mQuantityTextView.setText(quantity);
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

        } catch (Exception e) {

            e.printStackTrace();
            holder.itemView.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mProductsList == null ? 0 : mProductsList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_cart_name)
        TextView mNameTextView;
        @BindView(R.id.tv_cart_price)
        TextView mPriceTextView;
        @BindView(R.id.tv_quantity_cart)
        TextView mQuantityTextView;
        @BindView(R.id.iv_cart_image)
        ImageView mProductImageView;
        @BindView(R.id.button_cart_quantity_minus)
        Button mMinusButton;
        @BindView(R.id.button_cart_quantity_plus)
        Button mPlusButton;
        @BindView(R.id.button_cart_delete)
        ImageButton mDeleteButton;


        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    Product product = mProductsList.get(adapterPosition);
                    int quantity = Integer.parseInt(mQuantityTextView.getText().toString());
                    if (quantity != 1) {
                        mQuantityTextView.setText(String.valueOf(quantity - 1));
                        mProductsList.get(adapterPosition).setmQuantity(quantity - 1);
                        int price = product.getmPrice() * (quantity - 1);
                        mPriceTextView.setText("\u20B9 " + String.valueOf(price));
                    }
                }
            });
            mPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    Product product = mProductsList.get(adapterPosition);

                    int quantity = Integer.parseInt(mQuantityTextView.getText().toString());
                    int maxQuantity = product.getmMaxQuantity();
                    if (quantity != maxQuantity) {
                        mQuantityTextView.setText(String.valueOf(quantity + 1));
                        mProductsList.get(adapterPosition).setmQuantity(quantity + 1);
                        int price = product.getmPrice() * (quantity + 1);
                        mPriceTextView.setText("\u20B9 " + String.valueOf(price));
                    }
                }
            });
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    int id = mProductsList.get(adapterPosition).getmId();
                    showDeleteDialog(String.valueOf(id));
                }
            });
        }

        private void showDeleteDialog(final String productId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.delete_confirm)
                    .setCancelable(false)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            URI_PRODUCTS.buildUpon().appendPath(productId).build();
                            String selection = ProductColumns._ID + "=?";
                            String[] selectionArgs = {productId};
                            mContext.getContentResolver().delete(URI_PRODUCTS, selection, selectionArgs);
                            notifyDataSetChanged();
                            itemView.setVisibility(GONE);
                            dialog.dismiss();


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();
        }
    }

    public void setListData(ArrayList<Product> list) {
        mProductsList = list;
        notifyDataSetChanged();
    }
}
