package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Activity.CurrencyConverter;
import com.procialize.singleevent.Activity.InitGeneralInfoActivity;
import com.procialize.singleevent.Activity.ProfileActivity;
import com.procialize.singleevent.Activity.TimeWeatherActivity;
import com.procialize.singleevent.Adapter.GeneralInfoListAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.Fragments.GeneralInfo;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.FetchAgenda;
import com.procialize.singleevent.GetterSetter.GeneralInfoList;
import com.procialize.singleevent.GetterSetter.InfoList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralInfoActivity extends AppCompatActivity implements GeneralInfoListAdapter.GeneralInfoListener {

    List<EventSettingList> eventSettingLists;
    TextView weather_tv, abtcurency_tv, about_hotel, pullrefresh;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    List<InfoList> generalinfoLists;
    LinearLayout linearlayout;
    SwipeRefreshLayout generalInforefresh;
    LinearLayout.LayoutParams params;
    TextView textView;
    ImageView back;
    GeneralInfoListAdapter generalInfoListAdapter;
    RecyclerView general_item_list;
    ProgressDialog progressDialog;
    String api_token;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);

        SessionManager sessionManager = new SessionManager(GeneralInfoActivity.this);
        eventSettingLists = sessionManager.loadEventList();
        weather_tv = (TextView) findViewById(R.id.weather_tv);
        abtcurency_tv = (TextView) findViewById(R.id.abtcurency_tv);
//        about_hotel = (TextView) findViewById(R.id.about_hotel);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        pullrefresh = (TextView) findViewById(R.id.pullrefresh);
        generalInforefresh = findViewById(R.id.generalInforefresh);
        general_item_list = findViewById(R.id.general_item_list);
        back = findViewById(R.id.back);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Create TextView programmatically.
        textView = new TextView(GeneralInfoActivity.this);


        HashMap<String, String> user = sessionManager.getUserDetails();

        api_token = user.get(SessionManager.KEY_TOKEN);
        SubmitAnalytics(api_token, eventid, "", "", "generalInfo");

//                    generalInfoAdapter.scheduleLayoutAnimation();
        for (int i = 0; i < eventSettingLists.size(); i++) {
            String eventName = eventSettingLists.get(i).getFieldName();
            String eventValue = eventSettingLists.get(i).getFieldValue();

            if (eventName.equalsIgnoreCase("gen_info_weather")) {
                if (eventValue.equalsIgnoreCase("1")) {
                    weather_tv.setVisibility(View.VISIBLE);
                }

            }

            if (eventName.equalsIgnoreCase("gen_info_currency_converter")) {
                if (eventValue.equalsIgnoreCase("1")) {
                    abtcurency_tv.setVisibility(View.VISIBLE);
                }
            }
        }

        weather_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneralInfoActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        abtcurency_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneralInfoActivity.this, CurrencyConverter.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(GeneralInfoActivity.this);
        general_item_list.setLayoutManager(mLayoutManager);
        getInfoTab();

        generalInforefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                getInfoTab();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getInfoTab() {
        progressDialog = new ProgressDialog(GeneralInfoActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAPIService.FetchGeneralInfo(eventid).enqueue(new Callback<GeneralInfoList>() {
            @Override
            public void onResponse(Call<GeneralInfoList> call, Response<GeneralInfoList> response) {

                if (response.body().getStatus().equals("success")) {
                    progressDialog.dismiss();
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    generalinfoLists = response.body().getInfoList();

                    showResponse(response);
//                    generalInfoListAdapter = new GeneralInfoListAdapter(GeneralInfoActivity.this ,generalinfoLists, this);
//                    generalInfoListAdapter.notifyDataSetChanged();
//                    general_item_list.setAdapter(generalInfoListAdapter);


                    if (generalInforefresh.isRefreshing()) {
                        generalInforefresh.setRefreshing(false);
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(GeneralInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralInfoList> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(GeneralInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                if (generalInforefresh.isRefreshing()) {
                    generalInforefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<GeneralInfoList> response) {
        try {
            generalInfoListAdapter = new GeneralInfoListAdapter(getApplicationContext(), response.body().getInfoList(), this);
            generalInfoListAdapter.notifyDataSetChanged();
            general_item_list.setAdapter(generalInfoListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    @Override
    public void onContactSelected(InfoList firstLevelFilter) {
        Intent intent = new Intent(GeneralInfoActivity.this, InitGeneralInfoActivity.class);
        intent.putExtra("name", firstLevelFilter.getName());
        intent.putExtra("description", firstLevelFilter.getDescription());
        startActivity(intent);
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    Toast.makeText(GeneralInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                Toast.makeText(GeneralInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
