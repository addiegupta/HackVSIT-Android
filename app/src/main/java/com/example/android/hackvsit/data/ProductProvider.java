package com.example.android.hackvsit.data;

import android.net.Uri;

import com.example.android.hackvsit.BuildConfig;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = ProductProvider.AUTHORITY, database = ProductDatabase.class)
public class ProductProvider {


    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    @TableEndpoint(table = ProductDatabase.PRODUCTS)
    public static class Products {

        @ContentUri(
                path = "products",
                type = "vnd.android.cursor.dir/product",
                defaultSort = ProductColumns.ID + " ASC")
        public static final Uri URI_PRODUCTS = Uri.parse("content://" + AUTHORITY + "/products");

    }
}
