package com.procialize.singleevent.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.RatingSpeakerPost;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakerDetailsActivity extends AppCompatActivity {

    String speakerid, city, country, company, designation, description, totalrating, name, profile, mobile;
    TextView tvdesc, tvname, tvcompany, tvdesignation, tvcity, speakertitle, tvmobile;
    ImageView profileIV;
    Button ratebtn;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, attendeeid;
    float ratingval;
    Dialog myDialog;
    String speaker_rating, speaker_designation, speaker_company, speaker_location, speaker_mobile;
    List<EventSettingList> eventSettingLists;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    View viewthree, viewtwo, viewone, viewfive,viewfour;
    RelativeLayout ratinglayout, layoutTop;
    RatingBar ratingbar;
    ImageView headerlogoIv;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_details);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


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

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        typeface = Typeface.createFromAsset(getAssets(),
                "DINPro-Regular.ttf");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(SpeakerDetailsActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        attendeeid = user.get(SessionManager.KEY_ID);

        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        try {
            speakerid = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            city = getIntent().getExtras().getString("city");
            country = getIntent().getExtras().getString("country");
            company = getIntent().getExtras().getString("company");
            designation = getIntent().getExtras().getString("designation");
            description = getIntent().getExtras().getString("description");
            totalrating = getIntent().getExtras().getString("totalrating");
            profile = getIntent().getExtras().getString("profile");
            mobile = getIntent().getExtras().getString("mobile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SubmitAnalytics(apikey, eventid, "", "", "speakerDetail");
        tvname = findViewById(R.id.tvname);
        tvdesc = findViewById(R.id.tvdesc);
        tvdesc.setMovementMethod(new ScrollingMovementMethod());
        tvdesc.setTypeface(typeface);
        tvcompany = findViewById(R.id.tvcompany);
        tvdesignation = findViewById(R.id.tvdesignation);
        tvmobile = findViewById(R.id.tvmobile);
        speakertitle = findViewById(R.id.speakertitle);
        tvcity = findViewById(R.id.tvcity);
        profileIV = findViewById(R.id.profileIV);
        viewone = findViewById(R.id.viewone);
        viewtwo = findViewById(R.id.viewtwo);
        viewfive = findViewById(R.id.viewfive);
        viewthree = findViewById(R.id.viewthree);
        viewfour = findViewById(R.id.viewfour);
        ratinglayout = findViewById(R.id.ratinglayout);
        ratingbar = findViewById(R.id.ratingbar);
        layoutTop = findViewById(R.id.layoutTop);

        progressBar = findViewById(R.id.progressBar);

        ratebtn = findViewById(R.id.ratebtn);

        tvname.setTextColor(Color.parseColor(colorActive));
        speakertitle.setTextColor(Color.parseColor(colorActive));
        ratebtn.setBackgroundColor(Color.parseColor(colorActive));
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));
        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor(colorActive),
                PorterDuff.Mode.SRC_ATOP);


        if (name != null) {
            if (name.equalsIgnoreCase("N A")) {
                tvname.setVisibility(View.GONE);
            } else {
                tvname.setText(name);
            }
        } else {
            tvname.setVisibility(View.GONE);
        }

        if (description != null) {
            if (description.equalsIgnoreCase("")) {
                tvdesc.setVisibility(View.GONE);
                viewfive.setVisibility(View.GONE);
            } else if (description.equalsIgnoreCase(" ")) {
                tvdesc.setVisibility(View.GONE);
                viewfive.setVisibility(View.GONE);
            } else {
                tvdesc.setText(description);
            }
        } else {
            tvdesc.setVisibility(View.GONE);
            viewfive.setVisibility(View.GONE);
        }

        if (company != null && speaker_company.equalsIgnoreCase("1")) {
            if (company.equalsIgnoreCase("")) {
                tvcompany.setVisibility(View.GONE);
            } else {
                tvcompany.setText(company);
            }

        } else {
            tvcompany.setVisibility(View.GONE);
        }

        if (designation != null && speaker_designation.equalsIgnoreCase("1")) {
            if (designation.equalsIgnoreCase("")) {
                tvdesignation.setVisibility(View.GONE);
                viewthree.setVisibility(View.GONE);
            } else {
                tvdesignation.setText(designation);
            }

        } else {
            tvdesignation.setVisibility(View.GONE);
            viewthree.setVisibility(View.GONE);
        }

        if (city != null && speaker_location.equalsIgnoreCase("1")) {
            if (city.equalsIgnoreCase("")) {
                tvcity.setVisibility(View.GONE);
                viewfour.setVisibility(View.GONE);
            } else {
                tvcity.setText(city);
            }

        } else {
            tvcity.setVisibility(View.GONE);
            viewfour.setVisibility(View.GONE);
        }

        if (mobile != null && speaker_mobile.equalsIgnoreCase("1")) {
            if (mobile.equalsIgnoreCase("")) {
                tvmobile.setVisibility(View.GONE);
            } else {
                tvmobile.setText(mobile);
            }

        } else {
            tvmobile.setVisibility(View.GONE);
        }
        if (profile != null) {
            Glide.with(this).load(ApiConstant.speaker + profile).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(profileIV).onLoadStarted(getDrawable(R.drawable.profilepic_placeholder));
        } else {
            progressBar.setVisibility(View.GONE);
        }

        if (speakerid.equalsIgnoreCase(attendeeid)) {
            ratebtn.setVisibility(View.GONE);
            ratinglayout.setVisibility(View.GONE);
        } else {
            ratebtn.setVisibility(View.VISIBLE);
            ratinglayout.setVisibility(View.VISIBLE);
        }

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingval = rating;
            }
        });


        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingval > 0) {
                    PostRate(eventid, String.valueOf(ratingval), apikey, speakerid, "");
                } else {
                    Toast.makeText(SpeakerDetailsActivity.this, "Please Select Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {
        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("speaker_rating")) {
                speaker_rating = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_designation")) {
                speaker_designation = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_company")) {
                speaker_company = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_location")) {
                speaker_location = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_mobile")) {
                speaker_mobile = eventSettingLists.get(i).getFieldValue();
            }

        }
    }

    private void showratedialouge() {

        myDialog = new Dialog(SpeakerDetailsActivity.this);
        myDialog.setContentView(R.layout.dialog_rate_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        RatingBar ratingBar = myDialog.findViewById(R.id.ratingbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingval = rating;
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingval > 0) {
                    PostRate(eventid, String.valueOf(ratingval), apikey, speakerid, "");
                } else {
                    Toast.makeText(SpeakerDetailsActivity.this, "Please Select Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void PostRate(String eventid, String rating, String token, String speakerid, String comment) {
        final ProgressDialog progressDialog = new ProgressDialog(SpeakerDetailsActivity.this);
        progressDialog.show();
//        showProgress();
        mAPIService.RatingSpeakerPost(token, eventid, speakerid, rating, comment).enqueue(new Callback<RatingSpeakerPost>() {
            @Override
            public void onResponse(Call<RatingSpeakerPost> call, Response<RatingSpeakerPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressDialog.dismiss();
                    ratingbar.setRating(0F);
//                    dismissProgress();
                    DeletePostresponse(response);
                } else {
//                    dismissProgress();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RatingSpeakerPost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<RatingSpeakerPost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");
            SubmitAnalytics(apikey, eventid, "", "", "rating");
//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();


        } else {
            Log.e("post", "fail");
//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

//                    Toast.makeText(SpeakerDetailsActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
//                Toast.makeText(SpeakerDetailsActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
