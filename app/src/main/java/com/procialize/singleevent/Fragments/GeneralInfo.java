package com.procialize.singleevent.Fragments;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Activity.CurrencyConverter;
import com.procialize.singleevent.Activity.InitGeneralInfoActivity;
import com.procialize.singleevent.Activity.TimeWeatherActivity;
import com.procialize.singleevent.Adapter.GeneralInfoListAdapter;
import com.procialize.singleevent.Adapter.MyAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.GeneralInfoList;
import com.procialize.singleevent.GetterSetter.InfoList;
import com.procialize.singleevent.InnerDrawerActivity.GeneralInfoActivity;

import com.procialize.singleevent.InnerDrawerActivity.WeatherActivity;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GeneralInfo extends Fragment implements GeneralInfoListAdapter.GeneralInfoListener {

    List<EventSettingList> eventSettingLists;
    TextView weather_tv, abtcurency_tv, about_hotel, pullrefresh;
    private APIService mAPIService;
    String eventid;
    List<InfoList> generalinfoLists;
    LinearLayout linearlayout,general_info_cur,general_info_wea;
    SwipeRefreshLayout generalInforefresh;
    LinearLayout.LayoutParams params;
    TextView textView,header;
    ProgressBar progressBar;
    RecyclerView general_item_list;
    GeneralInfoListAdapter generalInfoListAdapter;
    List views = new ArrayList();
    View viewlayout;
    LinearLayout insertPoint;
    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive;




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.general_info, container, false);
        SessionManager sessionManager = new SessionManager(getActivity());
        eventSettingLists = sessionManager.loadEventList();
        SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs1.getString("colorActive","");


        weather_tv = (TextView) view.findViewById(R.id.weather_tv);
        abtcurency_tv = (TextView) view.findViewById(R.id.abtcurency_tv);
        header = (TextView) view.findViewById(R.id.header);

//        about_hotel = (TextView) view.findViewById(R.id.about_hotel);
        linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);

        weather_tv.setTextColor(Color.parseColor(colorActive));
        abtcurency_tv.setTextColor(Color.parseColor(colorActive));
        header.setTextColor(Color.parseColor(colorActive));


        general_info_wea = (LinearLayout) view.findViewById(R.id.general_info_wea);
        general_info_cur = (LinearLayout) view.findViewById(R.id.general_info_cur);

        ImageView ic_rightarrow = (ImageView) view.findViewById(R.id.ic_rightarrow);
        ImageView ic_rightarrow1 = (ImageView) view.findViewById(R.id.ic_rightarrow1);
//        int colorInt = Color.parseColor(colorActive);
//
//        ColorStateList csl = ColorStateList.valueOf(colorInt);
//        Drawable drawable = DrawableCompat.wrap(ic_rightarrow.getDrawable());
//        DrawableCompat.setTintList(drawable, csl);
//        ic_rightarrow.setImageDrawable(drawable);


//        Drawable drawable1 = DrawableCompat.wrap(ic_rightarrow1.getDrawable());
//        DrawableCompat.setTintList(drawable1, csl);
//        ic_rightarrow1.setImageDrawable(drawable1);


        ic_rightarrow.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
        ic_rightarrow1.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

        pullrefresh = (TextView) view.findViewById(R.id.pullrefresh);
        generalInforefresh = view.findViewById(R.id.generalInforefresh);

        general_item_list = view.findViewById(R.id.general_item_list);
        progressBar = view.findViewById(R.id.progressBar);


        HashMap<String, String> user = sessionManager.getUserDetails();


        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        if (generalInforefresh.isRefreshing()) {
            generalInforefresh.setRefreshing(false);
        }

        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
//        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // Create TextView programmatically.
//        textView = new TextView(getActivity());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        general_item_list.setLayoutManager(mLayoutManager);
        getInfoTab();

//        generalInfoAdapter.scheduleLayoutAnimation();
        for (int i = 0; i < eventSettingLists.size(); i++) {
            String eventName = eventSettingLists.get(i).getFieldName();
            String eventValue = eventSettingLists.get(i).getFieldValue();

            if (eventName.equalsIgnoreCase("gen_info_weather") && eventValue.equalsIgnoreCase("1")) {
                weather_tv.setVisibility(View.VISIBLE);
                general_info_wea.setVisibility(View.VISIBLE);

            }

            if (eventName.equalsIgnoreCase("gen_info_currency_converter") && eventValue.equalsIgnoreCase("1")) {
                abtcurency_tv.setVisibility(View.VISIBLE);
                general_info_cur.setVisibility(View.VISIBLE);

            }
        }

        SubmitAnalytics(token, eventid, "", "", "generalInfo");
        weather_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });

        abtcurency_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurrencyConverter.class);
                startActivity(intent);
            }
        });


        generalInforefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getInfoTab();

            }
        });


        return view;
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


                    generalInfoListAdapter = new GeneralInfoListAdapter(getActivity(), generalinfoLists, GeneralInfo.this);
                    generalInfoListAdapter.notifyDataSetChanged();
                    general_item_list.setAdapter(generalInfoListAdapter);


                    if (generalInforefresh.isRefreshing()) {
                        generalInforefresh.setRefreshing(false);
                    }


                } else {
                   dismissProgress();
                  //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralInfoList> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Unable to submit post to API.");
              //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                if (generalInforefresh.isRefreshing()) {
                    generalInforefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    @Override
    public void onContactSelected(InfoList firstLevelFilter) {
        Intent intent = new Intent(getActivity(), InitGeneralInfoActivity.class);
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

                    //Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
              //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();

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
