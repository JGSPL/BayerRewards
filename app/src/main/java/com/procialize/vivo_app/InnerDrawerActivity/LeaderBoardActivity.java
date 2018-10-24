package com.procialize.vivo_app.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.vivo_app.Activity.PdfViewerActivity;
import com.procialize.vivo_app.Adapter.LeaderBoardAdapter;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.GetterSetter.DocumentList;
import com.procialize.vivo_app.GetterSetter.LeaderBoardListFetch;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rahul on 23-10-2018.
 */

public class LeaderBoardActivity  extends AppCompatActivity {


    private APIService mAPIService;
    SwipeRefreshLayout leadRvrefresh;
    RecyclerView LeaderRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


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
        LeaderRv = findViewById(R.id.LeaderRv);
        leadRvrefresh = findViewById(R.id.leadRvrefresh);
        progressBar = findViewById(R.id.progressBar);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        LeaderRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        LeaderRv.setLayoutAnimation(animation);


        fetchLeaderboard(eventid, token);

        leadRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLeaderboard(eventid, token);
            }
        });

    }


    public void fetchLeaderboard(String eventid, String token) {
        showProgress();
        mAPIService.LeaderBoardListFetch(eventid, token).enqueue(new Callback<LeaderBoardListFetch>() {
            @Override
            public void onResponse(Call<LeaderBoardListFetch> call, Response<LeaderBoardListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (leadRvrefresh.isRefreshing()) {
                        leadRvrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (leadRvrefresh.isRefreshing()) {
                        leadRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LeaderBoardListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (leadRvrefresh.isRefreshing()) {
                    leadRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<LeaderBoardListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getLeaderboardList().isEmpty())) {
                LeaderBoardAdapter docAdapter = new LeaderBoardAdapter(LeaderBoardActivity.this, response.body().getLeaderboardList());
                docAdapter.notifyDataSetChanged();
                LeaderRv.setAdapter(docAdapter);
                LeaderRv.scheduleLayoutAnimation();
                LeaderRv.setVisibility(View.VISIBLE);

            } else {

                setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                text_empty.setText("LeaderBoard Data not available");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


//                Intent intent = new Intent(LeaderBoardActivity.this, EmptyViewActivity.class);
//                startActivity(intent);
//                finish();

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
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }


}
