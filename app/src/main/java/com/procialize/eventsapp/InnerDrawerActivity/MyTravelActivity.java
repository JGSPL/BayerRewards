package com.procialize.eventsapp.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.eventsapp.Activity.ViewPDFActivity;
import com.procialize.eventsapp.Adapter.MyTravelAdapter;
import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiConstant;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.GetterSetter.TravelList;
import com.procialize.eventsapp.GetterSetter.TravelListFetch;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTravelActivity extends AppCompatActivity implements MyTravelAdapter.MyTravelAdapterListner {


    SwipeRefreshLayout travelRvrefresh;
    // ListView travelRv;
    RecyclerView travelRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    TextView empty;
    ImageView headerlogoIv;
    private APIService mAPIService;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travel);

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
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        travelRv = findViewById(R.id.travelRv);
        travelRvrefresh = findViewById(R.id.travelRvrefresh);
        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);

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

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        fetchTravel(token, eventid);

        travelRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTravel(token, eventid);
            }
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
        RelativeLayout linUpper = findViewById(R.id.linUpper);
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            List<TravelList> travelLists = response.body().getTravelList();
            if (travelLists.size() == 0) {
                //travelRv.setVisibility(View.GONE);
                // linUpper.setBackground(getResources().getDrawable(R.drawable.noticket));
                MyTravelAdapter travelAdapter = new MyTravelAdapter(this, response.body().getTravelList(), this);
                travelAdapter.notifyDataSetChanged();

                travelRv.setAdapter(travelAdapter);
                //travelRvrefresh.setAdapter(travelAdapter);

                travelRv.scheduleLayoutAnimation();
                TextView txtEmpty = findViewById(R.id.txtEmpty);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                TextView txtEmpty = findViewById(R.id.txtEmpty);
                txtEmpty.setVisibility(View.GONE);
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
        if (travel.getType().equalsIgnoreCase("video")) {
            Intent intent = new Intent(MyTravelActivity.this, ExoVideoActivity.class);
            intent.putExtra("videoUrl", travel.getFileName());
            intent.putExtra("title", travel.getTitle());
            intent.putExtra("page", "travel");
            startActivity(intent);
        } else {
            Intent pdfview = new Intent(this, ViewPDFActivity.class);
            pdfview.putExtra("url", ApiConstant.imgURL + "uploads/travel_gallery/" + travel.getFileName());
            startActivity(pdfview);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
