package com.procialize.eventsapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.DbHelper.ConnectionDetector;
import com.procialize.eventsapp.GetterSetter.Contact;
import com.procialize.eventsapp.GetterSetter.ContactListFetch;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;
    ProgressBar progressBar;
    List<Contact> contactList;
    WebView mywebview;
    private APIService mAPIService;
    SwipeRefreshLayout webrefresher;
    ConnectionDetector cd;
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


        cd = new ConnectionDetector(this);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);


        mywebview = findViewById(R.id.webView);
        webrefresher = findViewById(R.id.webrefresher);
        mywebview.setBackgroundColor(Color.TRANSPARENT);

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

        // mywebview.loadUrl("https://www.procialize.info/contact_us.html");

        if (cd.isConnectingToInternet()) {
            fetchContact(eventid, token);
        } else {
            Toast.makeText(this, "Device not connected to internet", Toast.LENGTH_SHORT).show();
        }

        webrefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (cd.isConnectingToInternet()) {
                    fetchContact(eventid, token);
                } else {
                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    Toast.makeText(WebViewActivity.this, "Device not connected to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void fetchContact(String eventid, String token) {
        showProgress();
        mAPIService.ContactListFetch(eventid, token).enqueue(new Callback<ContactListFetch>() {
            @Override
            public void onResponse(Call<ContactListFetch> call, Response<ContactListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContactListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                if (webrefresher.isRefreshing()) {
                    webrefresher.setRefreshing(false);
                }
                dismissProgress();

            }
        });
    }

    public void showResponse(Response<ContactListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getContactList().isEmpty())) {
                contactList = response.body().getContactList();
                String contactstr = contactList.get(0).getContent();
                String cssEditor = response.body().getCssEditor();


                mywebview.clearCache(true);
                mywebview.loadData(cssEditor + contactstr, "text/html", "UTF-8");
                mywebview.setWebViewClient(new CustomWebViewClient());

            }
            /* else {

                setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                text_empty.setText("Contacts not available");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


            }*/

        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
    private class CustomWebViewClient extends WebViewClient {

        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.clearCache(true);
                view.reload();
                return true;
            }else if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                startActivity(intent);
                view.clearCache(true);
                view.reload();
                return true;
            }

            view.clearCache(true);
            view.loadUrl(url);
            return true;
        }
    }

    public void showProgress() {
       /* if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }*/
    }

    public void dismissProgress() {
       /* if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }*/
    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
