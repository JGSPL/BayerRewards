package com.procialize.singleevent.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.RatingSessionPost;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaDetailActivity extends AppCompatActivity {

    String agendaid, date, name, description, starttime, endtime;
    TextView tvname, tvdate, tvtime, tvdscription;
    Button ratebtn;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey;
    float ratingval;
    Dialog myDialog;
    String agenda_save_to_calendar, agenda_text_description;
    List<EventSettingList> eventSettingLists;

    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    LinearLayout linear1, linear2, linear3;
    View viewtwo, viewone;
    RatingBar ratingbar;
    ProgressDialog progressDialog;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_detail);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //Testing purpose

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

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(AgendaDetailActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        SubmitAnalytics(apikey, eventid, "", "", "agendaDetail");

        try {
            agendaid = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            date = getIntent().getExtras().getString("date");
            description = getIntent().getExtras().getString("description");
            starttime = getIntent().getExtras().getString("starttime");
            endtime = getIntent().getExtras().getString("endtime");
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvname = findViewById(R.id.tvname);
        tvdate = findViewById(R.id.tvdate);
        tvtime = findViewById(R.id.tvtime);
        tvdscription = findViewById(R.id.tvdscription);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        viewone = findViewById(R.id.viewone);
        viewtwo = findViewById(R.id.viewtwo);
        ratebtn = findViewById(R.id.ratebtn);
        ratingbar = findViewById(R.id.ratingbar);

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
                    PostRate(eventid, String.valueOf(ratingval), apikey, agendaid);
                } else {
                    Toast.makeText(AgendaDetailActivity.this, "Please Select Something", Toast.LENGTH_SHORT).show();

                }
            }
        });

        if (name != null) {
            tvname.setText(name);
        } else {
            tvname.setVisibility(View.GONE);

        }

        if (date != null) {
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM");

                Date ogdate = originalFormat.parse(date);
                String day = String.valueOf(android.text.format.DateFormat.format("EEEE", ogdate));
                String sessiondate = targetFormat.format(ogdate);
                tvdate.setText(sessiondate+" - "+day);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvdate.setVisibility(View.GONE);
            linear1.setVisibility(View.GONE);
            viewone.setVisibility(View.GONE);
        }


        if (starttime != null && endtime != null) {
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
                SimpleDateFormat targetFormat = new SimpleDateFormat("kk:mm aa");

                Date startdate = originalFormat.parse(starttime);
                Date enddate = originalFormat.parse(endtime);

                String startdatestr = targetFormat.format(startdate);
                String enddatestr = targetFormat.format(enddate);

                tvtime.setText(startdatestr + " - " + enddatestr);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            tvtime.setVisibility(View.GONE);
            linear2.setVisibility(View.GONE);
            viewone.setVisibility(View.GONE);
        }

        if (description != null && agenda_text_description.equalsIgnoreCase("1")) {
            tvdscription.setText(description);
        } else {
            tvdscription.setVisibility(View.GONE);
            linear3.setVisibility(View.GONE);
            viewtwo.setVisibility(View.GONE);
        }


//        ratebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showratedialouge();
//            }
//        });

    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("agenda_save_to_calendar")) {
                agenda_save_to_calendar = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("agenda_text_description")) {
                agenda_text_description = eventSettingLists.get(i).getFieldValue();
            }
        }
    }


    private void showratedialouge() {

        myDialog = new Dialog(AgendaDetailActivity.this);
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
                    PostRate(eventid, String.valueOf(ratingval), apikey, agendaid);
                } else {
                    Toast.makeText(AgendaDetailActivity.this, "Please Select Something", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void PostRate(String eventid, String rating, String token, String speakerid) {
        progressDialog = new ProgressDialog(AgendaDetailActivity.this);
        progressDialog.setMessage("");
        progressDialog.show();
//        showProgress();
        mAPIService.RatingSessionPost(token, eventid, speakerid, rating).enqueue(new Callback<RatingSessionPost>() {
            @Override
            public void onResponse(Call<RatingSessionPost> call, Response<RatingSessionPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressDialog.dismiss();
//                    dismissProgress();
                    DeletePostresponse(response);
                } else {
                    progressDialog.dismiss();
//                    dismissProgress();

//                    Toast.makeText(getApplicationContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RatingSessionPost> call, Throwable t) {
                Log.e("hit", "Low network or no network");
                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),"Unable to process",Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<RatingSessionPost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            SubmitAnalytics(apikey, eventid, "", "", "rating");
        } else {
            Log.e("post", "fail");
//            myDialog.dismiss();
//            Toast.makeText(this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

//                    Toast.makeText(AgendaDetailActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
//                Toast.makeText(AgendaDetailActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
