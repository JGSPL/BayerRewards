package com.procialize.singleevent.Fragments;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class GeneralInfo extends Fragment implements GeneralInfoListAdapter.GeneralInfoListener {

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
    ProgressDialog progressDialog;
    RecyclerView general_item_list;
    GeneralInfoListAdapter generalInfoListAdapter;
    List views = new ArrayList();
    View viewlayout;
    LinearLayout insertPoint;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.general_info, container, false);
        SessionManager sessionManager = new SessionManager(getActivity());
        eventSettingLists = sessionManager.loadEventList();
        weather_tv = (TextView) view.findViewById(R.id.weather_tv);
        abtcurency_tv = (TextView) view.findViewById(R.id.abtcurency_tv);
//        about_hotel = (TextView) view.findViewById(R.id.about_hotel);
        linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);
        pullrefresh = (TextView) view.findViewById(R.id.pullrefresh);
        generalInforefresh = view.findViewById(R.id.generalInforefresh);

        general_item_list = view.findViewById(R.id.general_item_list);


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

            }

            if (eventName.equalsIgnoreCase("gen_info_currency_converter") && eventValue.equalsIgnoreCase("1")) {
                abtcurency_tv.setVisibility(View.VISIBLE);
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAPIService.FetchGeneralInfo(eventid).enqueue(new Callback<GeneralInfoList>() {
            @Override
            public void onResponse(Call<GeneralInfoList> call, Response<GeneralInfoList> response) {

                if (response.body().getStatus().equals("success")) {
                    progressDialog.dismiss();
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    generalinfoLists = response.body().getInfoList();


                    generalInfoListAdapter = new GeneralInfoListAdapter(getActivity(), generalinfoLists, GeneralInfo.this);
                    generalInfoListAdapter.notifyDataSetChanged();
                    general_item_list.setAdapter(generalInfoListAdapter);


                    if (generalInforefresh.isRefreshing()) {
                        generalInforefresh.setRefreshing(false);
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralInfoList> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
