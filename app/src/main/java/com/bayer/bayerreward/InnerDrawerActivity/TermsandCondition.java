package com.bayer.bayerreward.InnerDrawerActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;


public class TermsandCondition extends AppCompatActivity {
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#0092df"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);


        WebView mywebview = findViewById(R.id.webView);

        mywebview.loadUrl(ApiConstant.imgURL + "terms_and_condition.html");
        CommonFunction.crashlytics("TermsandConditions","");
        firbaseAnalytics(this,"TermsandConditions","");
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
