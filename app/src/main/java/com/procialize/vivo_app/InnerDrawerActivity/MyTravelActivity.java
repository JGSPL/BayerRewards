package com.procialize.vivo_app.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.vivo_app.Activity.PdfViewerActivity;
import com.procialize.vivo_app.Adapter.MyTravelAdapterList;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.GetterSetter.TravelList;
import com.procialize.vivo_app.GetterSetter.TravelListFetch;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTravelActivity extends AppCompatActivity implements MyTravelAdapterList.MyTravelAdapterListner {


    private APIService mAPIService;
    //    SwipeRefreshLayout travelRvrefresh;
    ListView travelRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    TextView empty;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travel);

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
        Util.logomethod(this, headerlogoIv);

        travelRv = findViewById(R.id.travelRv);
//        travelRvrefresh = findViewById(R.id.travelRvrefresh);
        progressBar = findViewById(R.id.progressBar);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        travelRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        travelRv.setLayoutAnimation(animation);

        fetchTravel(token, eventid);

//        travelRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                fetchTravel(token, eventid);
//            }
//        });

    }


    public void fetchTravel(String token, String eventid) {
        showProgress();
        mAPIService.TravelListFetch(token, eventid).enqueue(new Callback<TravelListFetch>() {
            @Override
            public void onResponse(Call<TravelListFetch> call, Response<TravelListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

//                    if (travelRvrefresh.isRefreshing()) {
//                        travelRvrefresh.setRefreshing(false);
//                    }
                    dismissProgress();
                    showResponse(response);
                } else {

//                    if (travelRvrefresh.isRefreshing()) {
//                        travelRvrefresh.setRefreshing(false);
//                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TravelListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
//                if (travelRvrefresh.isRefreshing()) {
//                    travelRvrefresh.setRefreshing(false);
//                }
            }
        });
    }

    public void showResponse(Response<TravelListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            MyTravelAdapterList travelAdapter = new MyTravelAdapterList(MyTravelActivity.this, response.body().getTravelList(), this);
            travelRv.setAdapter(travelAdapter);
//            travelRv.scheduleLayoutAnimation();
            travelRv.setEmptyView(findViewById(android.R.id.empty));

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
    public void onContactSelected(TravelList travel) {
        Intent pdfview = new Intent(this, PdfViewerActivity.class);
        pdfview.putExtra("url", "https://www.procialize.info/uploads/travel_gallery/" + travel.getFileName());
        startActivity(pdfview);
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
