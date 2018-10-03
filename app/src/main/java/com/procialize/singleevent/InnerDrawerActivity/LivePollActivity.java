package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import com.procialize.singleevent.Activity.PollDetailActivity;
import com.procialize.singleevent.Adapter.PollAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.LivePollFetch;
import com.procialize.singleevent.GetterSetter.LivePollList;
import com.procialize.singleevent.GetterSetter.LivePollOptionList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivePollActivity extends AppCompatActivity implements PollAdapter.PollAdapterListner {

    private APIService mAPIService;
    SwipeRefreshLayout pollrefresh;
    RecyclerView pollRv;
    ProgressBar progressBar;
    List<LivePollOptionList> optionLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_poll);

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
                finish();
            }
        });


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);
        pollRv = findViewById(R.id.pollRv);
        pollrefresh = findViewById(R.id.pollrefresh);
        progressBar = findViewById(R.id.progressBar);

        optionLists = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        pollRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        pollRv.setLayoutAnimation(animation);


        fetchPoll(token, eventid);

        pollrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPoll(token, eventid);
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
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (pollrefresh.isRefreshing()) {
                    pollrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<LivePollFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getLivePollList().size() != 0) {
            PollAdapter pollAdapter = new PollAdapter(this, response.body().getLivePollList(), response.body().getLivePollOptionList(), this);
            pollAdapter.notifyDataSetChanged();
            pollRv.setAdapter(pollAdapter);
            pollRv.scheduleLayoutAnimation();

            optionLists = response.body().getLivePollOptionList();
        } else {
            setContentView(R.layout.activity_empty_view);
            ImageView imageView = findViewById(R.id.back);
            TextView text_empty = findViewById(R.id.text_empty);
            text_empty.setText("LivePoll not available");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

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
        super.onResume();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
