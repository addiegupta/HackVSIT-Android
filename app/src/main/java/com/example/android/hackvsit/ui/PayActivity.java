package com.example.android.hackvsit.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.hackvsit.R;

import butterknife.ButterKnife;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        ButterKnife.bind(this);
    }
}
