package com.procialize.eventsapp.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.eventsapp.Activity.PollDetailActivity;
import com.procialize.eventsapp.Adapter.PollNewAdapter;
import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.DbHelper.ConnectionDetector;
import com.procialize.eventsapp.GetterSetter.LivePollFetch;
import com.procialize.eventsapp.GetterSetter.LivePollList;
import com.procialize.eventsapp.GetterSetter.LivePollOptionList;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivePollActivity extends AppCompatActivity implements PollNewAdapter.PollAdapterListner {

    SwipeRefreshLayout pollrefresh;
    ListView pollRv;
    ProgressBar progressBar;
    List<LivePollOptionList> optionLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    TextView emptyView;
    private APIService mAPIService;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_poll);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        cd = new ConnectionDetector(getApplicationContext());

        headerlogoIv = findViewById(R.id.headerlogoIv);


        Util.logomethod(this, headerlogoIv);
        pollRv = findViewById(R.id.pollRv);
        pollrefresh = findViewById(R.id.pollrefresh);
        progressBar = findViewById(R.id.progressBar);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        RelativeLayout layoutTop = findViewById(R.id.layoutTop);
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));


        optionLists = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        // use a linear layout manager
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        pollRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //  pollRv.setLayoutAnimation(animation);

        if (cd.isConnectingToInternet()) {
            fetchPoll(token, eventid);
        } else {

            Toast.makeText(LivePollActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }


        pollrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    fetchPoll(token, eventid);
                } else {
                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    Toast.makeText(LivePollActivity.this, "No internet connection",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void fetchPoll(String token, String eventid) {
        showProgress();
        mAPIService.LivePollFetch(token, eventid).enqueue(new Callback<LivePollFetch>() {
            @Override
            public void onResponse(Call<LivePollFetch> call, Response<LivePollFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (pollrefresh.isRefreshing()) {
                    pollrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<LivePollFetch> response) {

        // specify an adapter (see also next example)


        PollNewAdapter pollAdapter = new PollNewAdapter(this, response.body().getLivePollList(), response.body().getLivePollOptionList(), this);
        pollAdapter.notifyDataSetChanged();
        pollRv.setAdapter(pollAdapter);
        pollRv.setEmptyView(findViewById(android.R.id.empty));

        optionLists = response.body().getLivePollOptionList();

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
        super.onResume();
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    @Override
    public void onContactSelected(LivePollList pollList) {

        Intent polldetail = new Intent(getApplicationContext(), PollDetailActivity.class);
        polldetail.putExtra("id", pollList.getId());
        polldetail.putExtra("question", pollList.getQuestion());
        polldetail.putExtra("replied", pollList.getReplied());
        polldetail.putExtra("optionlist", (Serializable) optionLists);
        startActivity(polldetail);
        finish();


    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
