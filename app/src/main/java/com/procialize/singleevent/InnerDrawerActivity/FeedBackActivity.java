package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.procialize.singleevent.Activity.PdfViewerActivity;
import com.procialize.singleevent.Adapter.FeedBackAdapter;

import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.SurveyList;
import com.procialize.singleevent.GetterSetter.SurveyListFetch;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity implements FeedBackAdapter.FeedBackAdapterListner {


    private APIService mAPIService;
    SwipeRefreshLayout feedbackRvrefresh;
    RecyclerView feedbackRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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


        feedbackRv = findViewById(R.id.feedbackRv);
        progressBar = findViewById(R.id.progressBar);
        feedbackRvrefresh = findViewById(R.id.feedbackRvrefresh);

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        feedbackRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        feedbackRv.setLayoutAnimation(animation);


        fetchFeedback(token, eventid);

        feedbackRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeedback(token, eventid);
            }
        });


    }


    public void fetchFeedback(String token, String eventid) {
        showProgress();
        mAPIService.SurveyListFetch(token, eventid).enqueue(new Callback<SurveyListFetch>() {
            @Override
            public void onResponse(Call<SurveyListFetch> call, Response<SurveyListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (feedbackRvrefresh.isRefreshing()) {
                        feedbackRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {

                    if (feedbackRvrefresh.isRefreshing()) {
                        feedbackRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SurveyListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                if (feedbackRvrefresh.isRefreshing()) {
                    feedbackRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<SurveyListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getSurveyList().isEmpty())) {
                FeedBackAdapter feedbackAdapter = new FeedBackAdapter(this, response.body().getSurveyList(), this);
                feedbackAdapter.notifyDataSetChanged();
                feedbackRv.setAdapter(feedbackAdapter);
                feedbackRv.scheduleLayoutAnimation();
            } else {
                setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                text_empty.setText("Survey not available");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
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
    public void onContactSelected(SurveyList survey) {

        Intent pdfview = new Intent(this, PdfViewerActivity.class);
        pdfview.putExtra("url", survey.getUrl());
        startActivity(pdfview);

    }
}
