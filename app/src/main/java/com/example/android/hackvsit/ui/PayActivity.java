package com.example.android.hackvsit.ui;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.hackvsit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayActivity extends AppCompatActivity {

    @BindView(R.id.payment_web_view)
    WebView mWebView;

    public static final String BASE_PORTAL_URL = "http://192.168.136.210:3000/testtxn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        ButterKnife.bind(this);

        String price = getIntent().getStringExtra("price");
        String time = getIntent().getStringExtra("time");
        String url = BASE_PORTAL_URL + "/?time=" +time+"&price="+price;
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
        }
    }
}
