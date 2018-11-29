package com.procialize.singleevent.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class EngagementActivity extends AppCompatActivity {

    CardView selfiecard_view, videocard_view;
    private List<EventSettingList> eventSettingLists;
    HashMap<String, String> user;
    String engagement_selfie_contest = "0", engagement_video_contest = "0";
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid,colorActive;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engagement);
       // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");


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

        selfiecard_view = findViewById(R.id.selfiecard_view);
        videocard_view = findViewById(R.id.videocard_view);

        TextView header = (TextView)findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        TextView selfieTv = (TextView)findViewById(R.id.selfieTv);
        selfieTv.setBackgroundColor(Color.parseColor(colorActive));
        TextView videoTv = (TextView)findViewById(R.id.videoTv);
        videoTv.setBackgroundColor(Color.parseColor(colorActive));


        SessionManager sessionManager = new SessionManager(this);

        user = sessionManager.getUserDetails();


        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }


        if (engagement_selfie_contest.equalsIgnoreCase("0")) {
            selfiecard_view.setVisibility(View.GONE);
        }

        if (engagement_video_contest.equalsIgnoreCase("0")) {
            videocard_view.setVisibility(View.GONE);
        }

        selfiecard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selfie = new Intent(EngagementActivity.this, SelfieContestActivity.class);
                startActivity(selfie);
            }
        });

        videocard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videocontest = new Intent(EngagementActivity.this, VideoContestActivity.class);
                startActivity(videocontest);
            }
        });
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("engagement_selfie_contest")) {
                engagement_selfie_contest = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("engagement_video_contest")) {
                engagement_video_contest = eventSettingLists.get(i).getFieldValue();
            }

        }
    }

    @Override
    protected void onResume() {
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

}
