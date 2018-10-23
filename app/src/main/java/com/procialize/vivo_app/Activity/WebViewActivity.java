package com.procialize.vivo_app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Utility.Util;

public class WebViewActivity extends AppCompatActivity {

    ImageView headerlogoIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);


        WebView mywebview = (WebView) findViewById(R.id.webView);

        mywebview.loadUrl("https://www.procialize.info/contact_us.html");


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
