package com.procialize.eventsapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventsapp.Adapter.LikeAdapter;
import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiConstant;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.eventsapp.GetterSetter.AttendeeList;
import com.procialize.eventsapp.GetterSetter.LikeListing;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
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
    ImageView headerlogoIv, profileIV, feedimageIv, playicon;
    ProgressBar progressView, feedprogress;
    TextView nameTv, designationTv, dateTv, companyTv, headingTv;
    MyJZVideoPlayerStandard videoplayer;
    String fname, lname, name, company, designation, heading, date, Likes, Likeflag, Comments, profileurl, noti_profileurl, feedurl, flag, type, feedid, apikey, thumbImg, videourl, noti_type;
    float p1 = 0;
    String colorActive;
    LinearLayout relative;
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

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        headerlogoIv = findViewById(R.id.headerlogoIv);
        profileIV = findViewById(R.id.profileIV);
        feedimageIv = findViewById(R.id.feedimageIv);
        playicon = findViewById(R.id.playicon);
        Util.logomethod(this, headerlogoIv);

        progressView = findViewById(R.id.progressView);
        feedprogress = findViewById(R.id.feedprogress);
        videoplayer = findViewById(R.id.videoplayer);
        nameTv = findViewById(R.id.nameTv);
        nameTv.setTextColor(Color.parseColor(colorActive));
        companyTv = findViewById(R.id.companyTv);
        dateTv = findViewById(R.id.dateTv);
        designationTv = findViewById(R.id.designationTv);
        headingTv = findViewById(R.id.headingTv);
        like_list = findViewById(R.id.like_list);
        relative = findViewById(R.id.relative);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        Intent intent = getIntent();
        try {
            if (intent != null) {
                lname = intent.getStringExtra("lname");
                fname = intent.getStringExtra("fname");
                if (fname == null) {
                    fname = "";
                }

                if (lname == null) {
                    lname = "";
                }
                company = intent.getStringExtra("company");
                designation = intent.getStringExtra("designation");
                heading = intent.getStringExtra("heading");
                date = intent.getStringExtra("date");
                if (intent.getStringExtra("Likes") == null) {
                    Likes = "1";
                } else {
                    Likes = intent.getStringExtra("Likes");
                }

                if (intent.getStringExtra("Comments") == null) {
                    Comments = "1";
                } else {
                    Comments = intent.getStringExtra("Comments");
                }
                Likeflag = intent.getStringExtra("Likeflag");
                noti_type = intent.getStringExtra("noti_type");

                if (noti_type.equalsIgnoreCase("Notification")) {
                    noti_profileurl = intent.getStringExtra("profilepic");
                } else if (noti_type.equalsIgnoreCase("Wall_Post")) {
                    profileurl = intent.getStringExtra("profilepic");
                }

                type = intent.getStringExtra("type");


                feedid = intent.getStringExtra("feedid");

                p1 = intent.getFloatExtra("AspectRatio", (float) 0.000);

                if (type.equalsIgnoreCase("Image")) {
                    feedurl = intent.getStringExtra("url");

                    Log.e("feedurl", feedurl);

                } else if (type.equalsIgnoreCase("Video")) {
                    thumbImg = intent.getStringExtra("thumbImg");
                    videourl = intent.getStringExtra("videourl");
                }
                Log.e("p", p1 + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        nameTv.setText(fname + " " + lname);
        companyTv.setText(company);
        designationTv.setText(designation);
        try {
            if (heading.equalsIgnoreCase("")) {
                headingTv.setVisibility(View.GONE);
            } else {
                headingTv.setText(StringEscapeUtils.unescapeJava(heading));
            }
        }catch (Exception e){
            e.printStackTrace();
            headingTv.setVisibility(View.GONE);
        }


        if (noti_type.equalsIgnoreCase("Notification")) {

            if (noti_profileurl != null) {

                Glide.with(this).load(ApiConstant.profilepic + noti_profileurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            } else {
                progressView.setVisibility(View.GONE);
            }

        } else if (noti_type.equalsIgnoreCase("Wall_Post")) {
            if (profileurl != null) {

                Glide.with(this).load(profileurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            } else {
                progressView.setVisibility(View.GONE);
            }
        }

        if (type.equals("Image")) {
            //photo

            if (feedurl != null) {
                feedimageIv.setVisibility(View.VISIBLE);
                videoplayer.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
                Glide.with(this).load(feedurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(feedimageIv).onLoadStarted(getDrawable(R.drawable.gallery_placeholder));
            } else {
                feedprogress.setVisibility(View.GONE);
            }

        } else if (type.equals("Video")) {
            //video

            if (videourl != null) {
                feedimageIv.setVisibility(View.GONE);
                videoplayer.setVisibility(View.VISIBLE);
                playicon.setVisibility(View.GONE);
                videoplayer.setUp(videourl
                        , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

                Glide.with(LikeDetailActivity.this).load(videourl).into(videoplayer.thumbImageView);


                feedprogress.setVisibility(View.GONE);
            }

        } else if (type.equals("Status")) {
            feedimageIv.setVisibility(View.GONE);
            feedprogress.setVisibility(View.GONE);
        }

        mAPIService = ApiUtils.getAPIService();


        SessionManager sessionManager = new SessionManager(LikeDetailActivity.this);

        user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        mAPIService.postLikeUserList(token, feedid, eventid).enqueue(new Callback<LikeListing>() {
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