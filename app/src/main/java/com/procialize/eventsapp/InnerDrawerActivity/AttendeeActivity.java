package com.procialize.eventsapp.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.eventsapp.Activity.AttendeeDetailActivity;
import com.procialize.eventsapp.Adapter.AttendeeAdapter;
import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.DbHelper.ConnectionDetector;
import com.procialize.eventsapp.DbHelper.DBHelper;
import com.procialize.eventsapp.GetterSetter.AttendeeList;
import com.procialize.eventsapp.GetterSetter.FetchAttendee;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendeeActivity extends AppCompatActivity implements AttendeeAdapter.AttendeeAdapterListner {

    RecyclerView attendeerecycler;
    SwipeRefreshLayout attendeefeedrefresh;
    EditText searchEt;
    AttendeeAdapter attendeeAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private ProgressBar progressBar;
    private ConnectionDetector cd;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private List<AttendeeList> attendeeDBList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


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

        cd = new ConnectionDetector(AttendeeActivity.this);
        dbHelper = new DBHelper(AttendeeActivity.this);

        procializeDB = new DBHelper(AttendeeActivity.this);
        db = procializeDB.getWritableDatabase();

        attendeeDBList = new ArrayList<>();

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        attendeerecycler = findViewById(R.id.attendeerecycler);
        searchEt = findViewById(R.id.searchEt);
        attendeefeedrefresh = findViewById(R.id.attendeefeedrefresh);
        progressBar = findViewById(R.id.progressBar);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        attendeerecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //attendeerecycler.setLayoutAnimation(animation);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            attendeeDBList = dbHelper.getAttendeeDetails();

            attendeeAdapter = new AttendeeAdapter(AttendeeActivity.this, attendeeDBList, this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();
        }

        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (cd.isConnectingToInternet()) {
                    fetchFeed(token, eventid);
                } else {
                    db = procializeDB.getReadableDatabase();

                    attendeeDBList = dbHelper.getAttendeeDetails();

                    attendeeAdapter = new AttendeeAdapter(AttendeeActivity.this, attendeeDBList, AttendeeActivity.this);
                    attendeeAdapter.notifyDataSetChanged();
                    attendeerecycler.setAdapter(attendeeAdapter);
                    attendeerecycler.scheduleLayoutAnimation();

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(true);
                    }
                }
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
                attendeeAdapter.getFilter().filter(s.toString());
            }
        });
    }


    public void fetchFeed(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.AttendeeFetchPost(token, eventid).enqueue(new Callback<FetchAttendee>() {
            @Override
            public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {
                    progressBar.setVisibility(View.GONE);

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(AttendeeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAttendee> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(AttendeeActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchAttendee> response) {

        // specify an adapter (see also next example)
        if (!(response.body().getAttendeeList().isEmpty())) {

            dbHelper.clearAttendeesTable();
            dbHelper.insertAttendeesInfo(response.body().getAttendeeList(), db);


            attendeeAdapter = new AttendeeAdapter(AttendeeActivity.this, response.body().getAttendeeList(), this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();
        } else {
            setContentView(R.layout.activity_empty_view);
            ImageView imageView = findViewById(R.id.back);
            TextView text_empty = findViewById(R.id.text_empty);
            text_empty.setText("Attendee not available");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    @Override
    public void onContactSelected(AttendeeList attendee) {
        Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
        attendeetail.putExtra("id", attendee.getAttendeeId());
        attendeetail.putExtra("name", attendee.getFirstName() + " " + attendee.getLastName());
        attendeetail.putExtra("city", attendee.getCity());
        attendeetail.putExtra("country", attendee.getCountry());
        attendeetail.putExtra("company", attendee.getCompanyName());
        attendeetail.putExtra("designation", attendee.getDesignation());
        attendeetail.putExtra("description", attendee.getDescription());
        attendeetail.putExtra("profile", attendee.getProfilePic());
        attendeetail.putExtra("mobile", attendee.getMobile());
//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
        startActivity(attendeetail);
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

}
