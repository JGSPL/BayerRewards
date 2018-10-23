package com.procialize.vivo_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.procialize.vivo_app.Adapter.LikeAdapter;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.GetterSetter.AttendeeList;
import com.procialize.vivo_app.GetterSetter.LikeListing;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LikeDetailActivity extends AppCompatActivity {
    String newsfeedid;
    private APIService mAPIService;
    String token;
    HashMap<String, String> user;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    LikeAdapter likeAdapter;
    List<AttendeeList> attendeeLists;
    RecyclerView like_list;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        like_list=findViewById(R.id.like_list);

        mAPIService = ApiUtils.getAPIService();
        Intent intent = getIntent();
        newsfeedid = intent.getStringExtra("noificationid");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        SessionManager sessionManager = new SessionManager(LikeDetailActivity.this);

        user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        mAPIService.postLikeUserList(token, newsfeedid, eventid).enqueue(new Callback<LikeListing>() {
            @Override
            public void onResponse(Call<LikeListing> call, Response<LikeListing> response) {
//                Log.d("","LikeListResp"+response.body());
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    showPostLikeListresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(LikeDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeListing> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//                dismissProgress();
                Toast.makeText(LikeDetailActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPostLikeListresponse(Response<LikeListing> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {

            Log.e("post", "success");
            attendeeLists = new ArrayList<>();

            attendeeLists = response.body().getAttendeeList();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            likeAdapter = new LikeAdapter(LikeDetailActivity.this, attendeeLists);
            like_list.setLayoutManager(layoutManager);
            likeAdapter.notifyDataSetChanged();
            like_list.setAdapter(likeAdapter);
//            like_list.scheduleLayoutAnimation();




        } else {
            Log.e("post", "fail");
            Log.e("list", response.body().getAttendeeList().size() + "");
//            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onContactSelected(AttendeeList attendeeList) {
//
//    }
}