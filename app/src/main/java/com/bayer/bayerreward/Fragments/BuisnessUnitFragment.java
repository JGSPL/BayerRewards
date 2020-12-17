package com.bayer.bayerreward.Fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bayer.bayerreward.Adapter.BuisnessUnitAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.business_unit_data;
import com.bayer.bayerreward.GetterSetter.leaderboard_data;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class BuisnessUnitFragment extends Fragment {

    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, token;
    ConnectionDetector cd;
    RecyclerView recycler_region;
    APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<business_unit_data> buisnessList = new ArrayList<business_unit_data>();
    private List<business_unit_data> buisnessdbList = new ArrayList<business_unit_data>();
    SwipeRefreshLayout pullrefresh;
    String MY_TOTAL_POINTS = "totalPoints";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_PREFS_LEADER = "ProcializeLeader";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_buisness_unit_fragment, container, false);
        recycler_region = view.findViewById(R.id.recycler_buisness);
        progressBar = view.findViewById(R.id.progressBar);
        pullrefresh = view.findViewById(R.id.pullrefresh);

        mAPIService = ApiUtils.getAPIService();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        cd = new ConnectionDetector(getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_region.setLayoutManager(mLayoutManager);
        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getReadableDatabase();

        SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_LEADER, MODE_PRIVATE);
        String login = prefs1.getString("firsttimebui", "");

        if (login.equalsIgnoreCase("1")) {
            progressBar.setVisibility(View.GONE);
            dbHelper = new DBHelper(getActivity());
            buisnessdbList = dbHelper.getBuisnessUnitList();
            BuisnessUnitAdapter territoryAdapter = new BuisnessUnitAdapter(getActivity(), buisnessdbList);
            territoryAdapter.notifyDataSetChanged();
            recycler_region.setAdapter(territoryAdapter);

            SharedPreferences prefs2 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
            String totalpoint = prefs2.getString("totalpoint", "");
            LeaderboardFragment.txt_cnt.setText(totalpoint);
        } else {
            if (cd.isConnectingToInternet()) {
                LeaderBoardReward(token, eventid);
            } else {
                progressBar.setVisibility(View.GONE);
                dbHelper = new DBHelper(getActivity());
                buisnessdbList = dbHelper.getBuisnessUnitList();
                BuisnessUnitAdapter territoryAdapter = new BuisnessUnitAdapter(getActivity(), buisnessdbList);
                territoryAdapter.notifyDataSetChanged();
                recycler_region.setAdapter(territoryAdapter);

                SharedPreferences prefs3 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                String totalpoint = prefs3.getString("totalpoint", "");
                LeaderboardFragment.txt_cnt.setText(totalpoint);

            }
        }

        pullrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    LeaderBoardReward(token, eventid);
                } else {
                    progressBar.setVisibility(View.GONE);
                    dbHelper = new DBHelper(getActivity());
                    buisnessdbList = dbHelper.getBuisnessUnitList();
                    BuisnessUnitAdapter territoryAdapter = new BuisnessUnitAdapter(getActivity(), buisnessdbList);
                    territoryAdapter.notifyDataSetChanged();
                    recycler_region.setAdapter(territoryAdapter);
                }
            }
        });

        CommonFunction.crashlytics("BuisnessUnitFrag","");
        firbaseAnalytics(getActivity(),"BuisnessUnitFrag","");
        return view;
    }

    public void LeaderBoardReward(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.LeaderBoardReward(token, eventid).enqueue(new Callback<leaderboard_data>() {
            @Override
            public void onResponse(Call<leaderboard_data> call, Response<leaderboard_data> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    if (pullrefresh.isRefreshing()) {
                        pullrefresh.setRefreshing(false);
                    }
                    buisnessList.clear();
                    procializeDB.clearBuisnessTable();
                    buisnessList = response.body().getBusiness_unit_data();
                    procializeDB.insertBuisnessunitTable(buisnessList, db);
                    dbHelper = new DBHelper(getActivity());
                    buisnessList = new ArrayList<business_unit_data>();
                    buisnessdbList = dbHelper.getBuisnessUnitList();
                    BuisnessUnitAdapter territoryAdapter = new BuisnessUnitAdapter(getActivity(), buisnessdbList);
                    territoryAdapter.notifyDataSetChanged();
                    recycler_region.setAdapter(territoryAdapter);

                    SharedPreferences.Editor editor1 = getActivity().getSharedPreferences(MY_PREFS_LEADER, MODE_PRIVATE).edit();
                    editor1.putString("firsttimebui", "1");
                    editor1.apply();

                } else {
                    if (pullrefresh.isRefreshing()) {
                        pullrefresh.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    // Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<leaderboard_data> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressBar.setVisibility(View.GONE);
                if (pullrefresh.isRefreshing()) {
                    pullrefresh.setRefreshing(false);
                }
                // Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();

            }
        });
    }


}
