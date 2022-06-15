package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WebviewActivity extends Activity {

    WebView wv1;
    RelativeLayout backreli;
    TextView heading_cc_list;
    String url = "", strcardname = "", bank_code = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                url = "";
                strcardname = "";
                bank_code = "";
            } else {
                url = extras.getString("url");
                strcardname = extras.getString("strcardname");
                bank_code = extras.getString("bank_code");
            }
        } else {
            url = (String) savedInstanceState.getSerializable("url");
            strcardname = (String) savedInstanceState.getSerializable("strcardname");
            bank_code = (String) savedInstanceState.getSerializable("bank_code");
        }


        wv1 = (WebView) findViewById(R.id.webview);
        backreli = findViewById(R.id.backreli);
        heading_cc_list = findViewById(R.id.heading_cc_list);

        heading_cc_list.setText(strcardname);
        backreli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setDomStorageEnabled(true);

        wv1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String thanksur1 = "https://www.deal4loans.com/axis-credit-card-thanks.php";
                String thanksur2 = "https://www.deal4loans.com/scb-credit-card-thanks.php";
                String thanksur3 = "https://www.deal4loans.com/sbi-credit-card-bpcl-thanks.php";


                if (url.contains(thanksur1) || url.contains(thanksur2) || url.contains(thanksur3)) {

                    Intent intent = new Intent(WebviewActivity.this, Thankyoupage.class);
                    intent.putExtra("cardname", "" + strcardname);
                    intent.putExtra("bank_code", "" + bank_code);
                    startActivity(intent);
                    finish();

                } else {
                    view.loadUrl(url);
                }

                return false;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        wv1.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        if (wv1.canGoBack()) {
            wv1.goBack();
        } else {
            super.onBackPressed();
        }
    }

}