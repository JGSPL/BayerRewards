package com.procialize.singleevent.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.procialize.singleevent.Adapter.EventAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.EventListing;
import com.procialize.singleevent.GetterSetter.Login;
import com.procialize.singleevent.GetterSetter.UserEventList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventChooserActivity extends AppCompatActivity implements EventAdapter.EventAdapterListner {

    RecyclerView eventrecycler;
    SwipeRefreshLayout eventrefresh;
    private APIService mAPIService;
    private ProgressBar progressBar;
    EventAdapter eventAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String emailid, password;
    String eventnamestr;
    EditText searchEt;
    ImageView img_logout;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_chooser);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        emailid = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        session = new SessionManager(getApplicationContext());
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


        eventrecycler = findViewById(R.id.eventrecycler);
        eventrefresh = findViewById(R.id.eventrefresh);
        progressBar = findViewById(R.id.progressBar);
        searchEt = findViewById(R.id.searchEt);
        img_logout = findViewById(R.id.img_logout);
//        searchEt.setFocusable(false);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent intent = new Intent(EventChooserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        eventrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        eventrecycler.setLayoutAnimation(animation);


        mAPIService = ApiUtils.getAPIService();


        sendEventList(emailid, password);

        eventrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendEventList(emailid, password);
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
                eventAdapter.getFilter().filter(s.toString());
            }
        });

    }


    public void sendEventList(String email, String password) {
        mAPIService.EventListPost(email, password).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {

            eventAdapter = new EventAdapter(EventChooserActivity.this, response.body().getUserEventList(), this);
            eventAdapter.notifyDataSetChanged();
            eventrecycler.setAdapter(eventAdapter);
            eventrecycler.scheduleLayoutAnimation();

            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }
        }
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onContactSelected(UserEventList eventList) {
        String eventid = eventList.getEventId();
        eventnamestr = eventList.getName();
        sendLogin(emailid, password, eventid);
    }


    public void sendLogin(String email, String password, final String eventid) {
        mAPIService.LoginPost(email, password, eventid).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponseLogin(response, eventid);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseLogin(Response<Login> response, String eventid) {

        if (response.body().getStatus().equals("success")) {


            SessionManager sessionManager = new SessionManager(this);

            String firstname = response.body().getUserData().getFirstName();
            String lastname = response.body().getUserData().getLastName();
            String email = response.body().getUserData().getEmail();
            String mobile = response.body().getUserData().getMobile();
            String designation = response.body().getUserData().getDesignation();
            String company = response.body().getUserData().getCompanyName();
            String token = response.body().getUserData().getApiAccessToken();
            String desc = response.body().getUserData().getDescription();
            String city = response.body().getUserData().getCity();
            String country = response.body().getUserData().getCountry();
            String pic = response.body().getUserData().getProfilePic();
            String id = response.body().getUserData().getAttendeeId();

            sessionManager.createLoginSession(firstname, lastname, email, mobile, company, designation, token, desc, city, country, pic, id, emailid, password,"1");
            sessionManager.saveSharedPreferencesEventList(response.body().getEventSettingList());
            sessionManager.saveSharedPreferencesMenuEventList(response.body().getEventMenuSettingList());


            SharedPreferences prefs = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE);
            String login = prefs.getString("loginfirst", "");


            if (login.equalsIgnoreCase("1")) {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("eventid", eventid).commit();
                editor.putString("eventnamestr", eventnamestr).commit();
//                editor.putString("loginfirst","1");
                editor.apply();
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
//                home.putExtra("eventId", eventid);
//                home.putExtra("eventnamestr", eventnamestr);
                startActivity(home);
                finish();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("eventid", eventid).commit();
                editor.putString("eventnamestr", eventnamestr).commit();
//                editor.putString("loginfirst","1");
                editor.apply();

                Intent home = new Intent(getApplicationContext(), ProfileActivity.class);
//                home.putExtra("eventId", eventid);
//                home.putExtra("eventnamestr", eventnamestr);
                startActivity(home);
                finish();
            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

}
