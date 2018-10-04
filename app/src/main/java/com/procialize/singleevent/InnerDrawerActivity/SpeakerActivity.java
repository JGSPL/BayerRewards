package com.procialize.singleevent.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.procialize.singleevent.Activity.SpeakerDetailsActivity;
import com.procialize.singleevent.Adapter.SpeakerAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.FetchSpeaker;
import com.procialize.singleevent.GetterSetter.SpeakerList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
SpeakerActivity extends AppCompatActivity implements SpeakerAdapter.SpeakerAdapterListner {


    private APIService mAPIService;
    SwipeRefreshLayout speakerfeedrefresh;
    RecyclerView speakerrecycler;
    EditText searchEt;
    SpeakerAdapter speakerAdapter;

    private ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


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


        speakerrecycler = findViewById(R.id.speakerrecycler);

        speakerfeedrefresh = findViewById(R.id.speakerfeedrefresh);

        searchEt = findViewById(R.id.searchEt);

        progressBar = findViewById(R.id.progressBar);

//        speakerrecycler.setHasFixedSize(true);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        SubmitAnalytics(token, eventid, "", "", "speaker");
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        speakerrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        speakerrecycler.setLayoutAnimation(animation);


        fetchSpeaker(token, eventid);

        speakerfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSpeaker(token, eventid);
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
                speakerAdapter.getFilter().filter(s.toString());
            }
        });
    }


    public void fetchSpeaker(String token, String eventid) {

        showProgress();
        mAPIService.SpeakerFetchPost(token, eventid).enqueue(new Callback<FetchSpeaker>() {
            @Override
            public void onResponse(Call<FetchSpeaker> call, Response<FetchSpeaker> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(SpeakerActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSpeaker> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SpeakerActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

                hideProgress();
                if (speakerfeedrefresh.isRefreshing()) {
                    speakerfeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchSpeaker> response) {

        // specify an adapter (see also next example)
        if (!(response.body().getSpeakerList().isEmpty())) {
            speakerAdapter = new SpeakerAdapter(SpeakerActivity.this, response.body().getSpeakerList(), this);
            speakerAdapter.notifyDataSetChanged();
            speakerrecycler.setAdapter(speakerAdapter);
            speakerrecycler.scheduleLayoutAnimation();
        } else {
            setContentView(R.layout.activity_empty_view);
            ImageView imageView = findViewById(R.id.back);
            TextView text_empty = findViewById(R.id.text_empty);
            text_empty.setText("Speakers not available");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onContactSelected(SpeakerList speaker) {
        Intent speakeretail = new Intent(this, SpeakerDetailsActivity.class);
        speakeretail.putExtra("id", speaker.getAttendeeId());
        speakeretail.putExtra("name", speaker.getFirstName() + " " + speaker.getLastName());
        speakeretail.putExtra("city", speaker.getCity());
        speakeretail.putExtra("country", speaker.getCountry());
        speakeretail.putExtra("company", speaker.getCompany());
        speakeretail.putExtra("designation", speaker.getDesignation());
        speakeretail.putExtra("description", speaker.getDescription());
        speakeretail.putExtra("totalrate", speaker.getTotalRating());
        speakeretail.putExtra("profile", speaker.getProfilePic());
        startActivity(speakeretail);
    }


    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    Toast.makeText(SpeakerActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                Toast.makeText(SpeakerActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
