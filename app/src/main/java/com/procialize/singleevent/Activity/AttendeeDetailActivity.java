package com.procialize.singleevent.Activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.GetterSetter.AttendeeList;
import com.procialize.singleevent.GetterSetter.EventSettingList;

import com.procialize.singleevent.GetterSetter.SendMessagePost;
import com.procialize.singleevent.GetterSetter.UserData;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendeeDetailActivity extends AppCompatActivity {


    String attendeeid, city, country, company, designation, description, totalrating, name, profile;
    TextView tvname, tvcompany, tvdesignation, tvcity;
    Button sendbtn;
    Dialog myDialog;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey;
    ImageView profileIV;
    ProgressBar progressBar;
    String attendee_company, attendee_location, attendee_mobile, attendee_design;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    UserData userData;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AttendeeList> attendeeList;
    private List<AttendeeList> attendeesDBList;
    private DBHelper dbHelper;
    String getattendee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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

        dbHelper = new DBHelper(AttendeeDetailActivity.this);
        db = dbHelper.getWritableDatabase();

        db = dbHelper.getReadableDatabase();

        userData = dbHelper.getUserDetails();


        // token


        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(AttendeeDetailActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        getattendee = user.get(SessionManager.KEY_ID);

        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        try {
            attendeeid = getIntent().getExtras().getString("id");
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

        sendbtn = findViewById(R.id.sendbtn);
        if (attendeeid.equalsIgnoreCase(getattendee)) {
            sendbtn.setVisibility(View.GONE);
        } else {
            sendbtn.setVisibility(View.VISIBLE);
        }

        if (name.equalsIgnoreCase("N A")) {
            tvname.setVisibility(View.GONE);
        } else if (name != null) {
            tvname.setText(name);
        } else {
            tvname.setVisibility(View.GONE);
        }

        if (company.equalsIgnoreCase("N A")) {
            tvcompany.setVisibility(View.GONE);
        } else if (company != null && attendee_company.equalsIgnoreCase("1")) {
            tvcompany.setText(company);

        } else {
            tvcompany.setVisibility(View.GONE);
        }

        try {
            if (designation.equalsIgnoreCase("N A")) {
                tvdesignation.setVisibility(View.GONE);
            } else if (designation != null && attendee_design.equalsIgnoreCase("1")) {
                tvdesignation.setText(designation);
            } else {
                tvdesignation.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        if (city.equalsIgnoreCase("N A")) {
            tvcity.setVisibility(View.GONE);
        } else if (city != null && attendee_location.equalsIgnoreCase("1")) {
            tvcity.setText(city);

        } else {
            tvcity.setVisibility(View.GONE);
        }

        if (profile != null) {
            Glide.with(this).load(ApiConstant.profilepic + profile).listener(new RequestListener<Drawable>() {
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
            }).into(profileIV);
        } else {
            progressBar.setVisibility(View.GONE);
        }


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("attendee_company")) {
                attendee_company = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("attendee_location")) {
                attendee_location = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("attendee_mobile")) {
                attendee_mobile = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_designation")) {
                attendee_design = eventSettingLists.get(i).getFieldValue();
            }
        }
    }


    private void showratedialouge() {

        myDialog = new Dialog(AttendeeDetailActivity.this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);

        nametv.setText("To " + name);

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

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

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    PostMesssage(eventid, msg, apikey, attendeeid);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void PostMesssage(String eventid, String msg, String token, String attendeeid) {
//        showProgress();
        mAPIService.SendMessagePost(token, eventid, msg, attendeeid).enqueue(new Callback<SendMessagePost>() {
            @Override
            public void onResponse(Call<SendMessagePost> call, Response<SendMessagePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    DeletePostresponse(response);
                } else {
//                    dismissProgress();

//                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendMessagePost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<SendMessagePost> response) {

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
