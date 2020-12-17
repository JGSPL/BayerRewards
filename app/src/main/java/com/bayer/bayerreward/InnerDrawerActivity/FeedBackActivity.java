package com.bayer.bayerreward.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.SuveyDetailActivity;
import com.bayer.bayerreward.Adapter.FeedBackAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.SurveyList;
import com.bayer.bayerreward.GetterSetter.SurveyListFetch;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class FeedBackActivity extends AppCompatActivity implements FeedBackAdapter.FeedBackAdapterListner {


    SwipeRefreshLayout feedbackRvrefresh;
    RecyclerView feedbackRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    LinearLayout linear;
    TextView txt_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.activetab), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        TextView header = findViewById(R.id.title);
        txt_msg = findViewById(R.id.txt_msg);
//        header.setTextColor(Color.parseColor(colorActive));

        feedbackRv = findViewById(R.id.feedbackRv);
        progressBar = findViewById(R.id.progressBar);
        feedbackRvrefresh = findViewById(R.id.feedbackRvrefresh);
        linear = findViewById(R.id.linear);

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
        //feedbackRv.setLayoutAnimation(animation);


        fetchFeedback(token, eventid);

        feedbackRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeedback(token, eventid);
            }
        });

//        try {
////            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
//            //path to /data/data/yourapp/app_data/dirName
////            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
//            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
//            Resources res = getResources();
//            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
//            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
//            linear.setBackgroundDrawable(bd);
//
//            Log.e("PATH", String.valueOf(mypath));
//        } catch (Exception e) {
//            e.printStackTrace();
//            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
//        }

        CommonFunction.crashlytics("FeedbackActivity","");
        firbaseAnalytics(this,"FeedbackActivity","");

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
                feedbackRv.setVisibility(View.VISIBLE);
                txt_msg.setVisibility(View.GONE);
                FeedBackAdapter feedbackAdapter = new FeedBackAdapter(this, response.body().getSurveyList(), this);
                feedbackAdapter.notifyDataSetChanged();
                feedbackRv.setAdapter(feedbackAdapter);
//                feedbackRv.scheduleLayoutAnimation();
            } else {
                feedbackRv.setVisibility(View.GONE);
                txt_msg.setVisibility(View.VISIBLE);
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
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onContactSelected(SurveyList survey) {

        Intent pdfview = new Intent(this, SuveyDetailActivity.class);
        pdfview.putExtra("url", survey.getUrl());
        startActivity(pdfview);

    }
}
