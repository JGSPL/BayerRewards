package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Activity.ViewPDFActivity;
import com.procialize.singleevent.Adapter.MyTravelAdapter;
import com.procialize.singleevent.Adapter.MyTravelAdapterList;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.CustomTools.RecyclerViewEmptySupport;
import com.procialize.singleevent.GetterSetter.TravelList;
import com.procialize.singleevent.GetterSetter.TravelListFetch;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTravelActivity extends AppCompatActivity implements MyTravelAdapter.MyTravelAdapterListner {


    private APIService mAPIService;
        SwipeRefreshLayout travelRvrefresh;
   // ListView travelRv;
     RecyclerView travelRv;

    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    TextView empty;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travel);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");



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
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        travelRv = findViewById(R.id.travelRv);
        travelRvrefresh = findViewById(R.id.travelRvrefresh);
        progressBar = findViewById(R.id.progressBar);

        TextView travelHeader = findViewById(R.id.travelHeader);
        travelHeader.setTextColor(Color.parseColor(colorActive));

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
       travelRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
       // travelRv.setLayoutAnimation(animation);

        fetchTravel(token, eventid);

        travelRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTravel(token, eventid);            }
        });

    }


    public void fetchTravel(String token, String eventid) {
        showProgress();
        mAPIService.TravelListFetch(token, eventid).enqueue(new Callback<TravelListFetch>() {
            @Override
            public void onResponse(Call<TravelListFetch> call, Response<TravelListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (travelRvrefresh.isRefreshing()) {
                        travelRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {

                    if (travelRvrefresh.isRefreshing()) {
                        travelRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TravelListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (travelRvrefresh.isRefreshing()) {
                    travelRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<TravelListFetch> response) {

        // specify an adapter (see also next example)
        RelativeLayout linUpper = (RelativeLayout) findViewById(R.id.linUpper);
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            List<TravelList> travelLists = response.body().getTravelList();
            if(travelLists.size()==0){
                //travelRv.setVisibility(View.GONE);
               // linUpper.setBackground(getResources().getDrawable(R.drawable.noticket));
                 travelRvrefresh.setVisibility(View.VISIBLE);
                MyTravelAdapter travelAdapter = new MyTravelAdapter(this, response.body().getTravelList(), this);
                travelAdapter.notifyDataSetChanged();

                travelRv.setAdapter(travelAdapter);
                //travelRvrefresh.setAdapter(travelAdapter);

                travelRv.scheduleLayoutAnimation();
                TextView txtEmpty = (TextView)findViewById(R.id.txtEmpty);
                txtEmpty.setVisibility(View.VISIBLE);
            }else {
               // linUpper.setBackground(getResources().getDrawable(R.drawable.travel_bg));

                // MyTravelAdapterList travelAdapter = new MyTravelAdapterList(MyTravelActivity.this, response.body().getTravelList(), this);
                MyTravelAdapter travelAdapter = new MyTravelAdapter(this, response.body().getTravelList(), this);
                travelAdapter.notifyDataSetChanged();

                travelRv.setAdapter(travelAdapter);
                //travelRvrefresh.setAdapter(travelAdapter);

            travelRv.scheduleLayoutAnimation();
                // travelRv.setEmptyView(findViewById(android.R.id.empty));
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
    public void onContactSelected(TravelList travel) {
        Intent pdfview = new Intent(this, ViewPDFActivity.class);
        pdfview.putExtra("url", "https://www.procialize.info/uploads/travel_gallery/" + travel.getFileName());
        startActivity(pdfview);
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
