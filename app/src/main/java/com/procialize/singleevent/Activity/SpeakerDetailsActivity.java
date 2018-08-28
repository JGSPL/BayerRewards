package com.procialize.singleevent.Activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.procialize.singleevent.GetterSetter.DeletePost;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.RatingSpeakerDetail;
import com.procialize.singleevent.GetterSetter.RatingSpeakerPost;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakerDetailsActivity extends AppCompatActivity {

    String speakerid, city, country, company, designation, description, totalrating, name, profile;
    TextView tvname, tvcompany, tvdesignation, tvcity;
    ImageView profileIV;
    Button ratebtn;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey;
    float ratingval;
    Dialog myDialog;
    String speaker_rating, speaker_designation, speaker_company, speaker_location, speaker_mobile;
    List<EventSettingList> eventSettingLists;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_details);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


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


        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(SpeakerDetailsActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

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
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvname = findViewById(R.id.tvname);
        tvcompany = findViewById(R.id.tvcompany);
        tvdesignation = findViewById(R.id.tvdesignation);
        tvcity = findViewById(R.id.tvcity);
        profileIV = findViewById(R.id.profileIV);

        progressBar = findViewById(R.id.progressBar);

        ratebtn = findViewById(R.id.ratebtn);


        if (name != null) {
            if (name.equalsIgnoreCase("N A")) {
                tvname.setVisibility(View.GONE);
            } else {
                tvname.setText(name);
            }
        } else {
            tvname.setVisibility(View.GONE);
        }


        if (company != null && speaker_company.equalsIgnoreCase("1")) {
            if (company.equalsIgnoreCase("N A")) {
                tvcompany.setVisibility(View.GONE);
            } else {
                tvcompany.setText(company);
            }

        } else {
            tvcompany.setVisibility(View.GONE);
        }

        if (designation != null && speaker_designation.equalsIgnoreCase("1")) {
            if (designation.equalsIgnoreCase("N A")) {
                tvdesignation.setVisibility(View.GONE);
            } else {
                tvdesignation.setText(designation);
            }

        } else {
            tvdesignation.setVisibility(View.GONE);
        }

        if (city != null && speaker_location.equalsIgnoreCase("1")) {
            if (city.equalsIgnoreCase("N A")) {
                tvcity.setVisibility(View.GONE);
            } else {
                tvcity.setText(city);
            }

        } else {
            tvcity.setVisibility(View.GONE);
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

        if (speaker_rating.equalsIgnoreCase("1")) {
            ratebtn.setVisibility(View.VISIBLE);
        } else {
            ratebtn.setVisibility(View.GONE);
        }

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showratedialouge();
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
            }

            if (eventSettingLists.get(i).getFieldName().equals("speaker_mobile")) {
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
//        showProgress();
        mAPIService.RatingSpeakerPost(token, eventid, speakerid, rating, comment).enqueue(new Callback<RatingSpeakerPost>() {
            @Override
            public void onResponse(Call<RatingSpeakerPost> call, Response<RatingSpeakerPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    DeletePostresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RatingSpeakerPost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<RatingSpeakerPost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();


        } else {
            Log.e("post", "fail");
            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }
}
