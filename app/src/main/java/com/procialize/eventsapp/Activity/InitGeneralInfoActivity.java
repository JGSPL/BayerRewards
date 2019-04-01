package com.procialize.eventsapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Utility.Util;

public class InitGeneralInfoActivity extends AppCompatActivity {
    String name, description;
    TextView txt_title, text_maininfo;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    WebView mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        colorActive = prefs.getString("colorActive", "");

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");

//        if (name.equalsIgnoreCase("about location")) {
//            setContentView(R.layout.about_city);
//
//            TextView attendeetitle = findViewById(R.id.attendeetitle);
//            attendeetitle.setTextColor(Color.parseColor(colorActive));
//
//            RelativeLayout layoutTop = findViewById(R.id.layoutTop);
//            layoutTop.setBackgroundColor(Color.parseColor(colorActive));
//
//            TextView tvname = findViewById(R.id.tvname);
//            tvname.setTextColor(Color.parseColor("#FFFFFF"));
//
//
//        } else if (name.equalsIgnoreCase("about hotel")) {
//            setContentView(R.layout.about_hotel);
//
//            TextView attendeetitle = findViewById(R.id.attendeetitle);
//            attendeetitle.setTextColor(Color.parseColor(colorActive));
//
//            RelativeLayout layoutTop = findViewById(R.id.layoutTop);
//            layoutTop.setBackgroundColor(Color.parseColor(colorActive));
//
//            TextView tvname = findViewById(R.id.tvname);
//            tvname.setTextColor(Color.parseColor("#FFFFFF"));
//
//
//        } else {
        setContentView(R.layout.activity_init_general_info);
        mywebview = findViewById(R.id.webView);


        WebSettings settings = mywebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mywebview.clearCache(true);
        mywebview.loadData(description, "text/html", "UTF-8");

        TextView txt_title = findViewById(R.id.txt_title);
        txt_title.setTextColor(Color.parseColor(colorActive));
//        }

//        txt_title = (TextView) findViewById(R.id.txt_title);
//        text_maininfo = (TextView) findViewById(R.id.text_maininfo);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_title.setText(name);
//        txt_title.setAllCaps(true);
//        text_maininfo.setText(description);


    }
}
