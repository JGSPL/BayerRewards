package com.procialize.singleevent.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;

import com.procialize.singleevent.Activity.CurrencyConverter;
import com.procialize.singleevent.Activity.InitGeneralInfoActivity;
import com.procialize.singleevent.Activity.TimeWeatherActivity;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.GeneralInfoList;
import com.procialize.singleevent.GetterSetter.InfoList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralInfo extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.general_info, container, false);
        SessionManager sessionManager = new SessionManager(getActivity());
        eventSettingLists = sessionManager.loadEventList();
        weather_tv = (TextView) view.findViewById(R.id.weather_tv);
        abtcurency_tv = (TextView) view.findViewById(R.id.abtcurency_tv);
        about_hotel = (TextView) view.findViewById(R.id.about_hotel);
        linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);
        pullrefresh = (TextView) view.findViewById(R.id.pullrefresh);
        generalInforefresh = view.findViewById(R.id.generalInforefresh);

        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // Create TextView programmatically.
        textView = new TextView(getActivity());

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

        weather_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimeWeatherActivity.class);
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

                linearlayout.removeView(textView);

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
                    for (int i = 0; i <= generalinfoLists.size() - 1; i++) {
                        final String name = generalinfoLists.get(i).getName();
                        final String description = generalinfoLists.get(i).getDescription();
//                        if (name.equalsIgnoreCase("about hotel")) {


                        params.setMargins(15, 15, 15, 15);
                        textView.setLayoutParams(params);
                        textView.setPadding(0, 25, 20, 25);
                        textView.setBackgroundResource(R.drawable.agendabg);
                        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_rightarrow, 0);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText(name);
                        textView.setTextColor(Color.BLACK);

                        textView.setAllCaps(true);


                        try {
                            // Add TextView to LinearLayout
                            if (linearlayout != null) {
                                linearlayout.addView(textView);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                            about_hotel.setVisibility(View.VISIBLE);
//                        }
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), InitGeneralInfoActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("description", description);
                                startActivity(intent);
                            }
                        });

                    }
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
}
