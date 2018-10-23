package com.procialize.vivo_app.InnerDrawerActivity;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.vivo_app.Activity.CalendeAdapter;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.BuildConfig;
import com.procialize.vivo_app.GetterSetter.EventListing;
import com.procialize.vivo_app.GetterSetter.Login;
import com.procialize.vivo_app.GetterSetter.UserEventList;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;
import com.procialize.vivo_app.gcm.GCMHelper;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeti on 23-10-2018.
 */

public class CalendarActivity extends AppCompatActivity implements CalendeAdapter.EventAdapterListner {

    RecyclerView eventrecycler;
    SwipeRefreshLayout eventrefresh;
    private APIService mAPIService;
    private ProgressBar progressBar;
    CalendeAdapter eventAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String emailid;
    String  password;
    EditText searchEt;
    SessionManager session;
    String platform, device, os_version, app_version;

    ImageView headerlogoIv,img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        emailid = user.get(SessionManager.KEY_EMAIL);
        password = user.get(SessionManager.KEY_PASSWORD);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        platform = "android";
        device = Build.MODEL;
        os_version = Build.VERSION.RELEASE;
        app_version = "Version" + BuildConfig.VERSION_NAME;
        eventrecycler = findViewById(R.id.eventrecycler);
        eventrefresh = findViewById(R.id.eventrefresh);
        progressBar = findViewById(R.id.progressBar);
        searchEt = findViewById(R.id.searchEt);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        eventrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        eventrecycler.setLayoutAnimation(animation);


        mAPIService = ApiUtils.getAPIService();


        sendEventList(emailid, password);

        eventrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendEventList(emailid, password);
            }
        });


        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                eventAdapter.getFilter().filter(s.toString());
            }
        });




    }


    public void sendEventList(String email, String password) {
        mAPIService.EventListPost(email, password).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {

            eventAdapter = new CalendeAdapter(CalendarActivity.this, response.body().getUserEventList(), this);
            eventAdapter.notifyDataSetChanged();
            eventrecycler.setAdapter(eventAdapter);
            eventrecycler.scheduleLayoutAnimation();

            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }
        }
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onContactSelected(UserEventList eventList) {

    }
}
