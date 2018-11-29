package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ProgressBar;
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
    TextView weather_tv, abtcurency_tv, about_hotel, pullrefresh,genHeader;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    List<InfoList> generalinfoLists;
    LinearLayout linearlayout,general_info_cur,general_info_wea;
    SwipeRefreshLayout generalInforefresh;
    LinearLayout.LayoutParams params;
    TextView textView;
    ImageView back;
    GeneralInfoListAdapter generalInfoListAdapter;
    RecyclerView general_item_list;
    ProgressBar progressBar;
    String api_token,colorActive;
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
        genHeader=findViewById(R.id.header);

        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");


        params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Create TextView programmatically.
        textView = new TextView(GeneralInfoActivity.this);

        weather_tv.setTextColor(Color.parseColor(colorActive));
        abtcurency_tv.setTextColor(Color.parseColor(colorActive));
        genHeader.setTextColor(Color.parseColor(colorActive));


        general_info_wea = (LinearLayout) findViewById(R.id.general_info_wea);
        general_info_cur = (LinearLayout) findViewById(R.id.general_info_cur);

        ImageView ic_rightarrow = (ImageView) findViewById(R.id.ic_rightarrow);
        ImageView ic_rightarrow1 = (ImageView) findViewById(R.id.ic_rightarrow1);
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        ic_rightarrow.setImageDrawable(drawable);
        int colorInt2 = Color.parseColor(colorActive);

        ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

        Drawable drawable1 = DrawableCompat.wrap(ic_rightarrow1.getDrawable());
        DrawableCompat.setTintList(drawable1, csl2);
        ic_rightarrow1.setImageDrawable(drawable1);





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
                    general_info_wea.setVisibility(View.VISIBLE);

                }

            }

            if (eventName.equalsIgnoreCase("gen_info_currency_converter")) {
                if (eventValue.equalsIgnoreCase("1")) {
                    abtcurency_tv.setVisibility(View.VISIBLE);
                    general_info_cur.setVisibility(View.VISIBLE);

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

        showProgress();
        mAPIService.FetchGeneralInfo(eventid).enqueue(new Callback<GeneralInfoList>() {
            @Override
            public void onResponse(Call<GeneralInfoList> call, Response<GeneralInfoList> response) {

                if (response.body().getStatus().equals("success")) {
                   dismissProgress();
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
                    dismissProgress();
                    Toast.makeText(GeneralInfoActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralInfoList> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(GeneralInfoActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
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

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

}
