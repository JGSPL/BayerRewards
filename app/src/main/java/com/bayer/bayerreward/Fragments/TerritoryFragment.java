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

import com.bayer.bayerreward.Adapter.TerritoryAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.leaderboard_data;
import com.bayer.bayerreward.GetterSetter.territory_data;
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


public class TerritoryFragment extends Fragment {

    RecyclerView recycler_territory;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_TOTAL_POINTS = "totalPoints";
    String eventid, token;
    ConnectionDetector cd;
    APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<territory_data> territoryList = new ArrayList<territory_data>();
    private List<territory_data> territorydbList = new ArrayList<territory_data>();
    SwipeRefreshLayout pullrefresh;
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_PREFS_LEADER = "ProcializeLeader";

    public TerritoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_territory, container, false);
        recycler_territory = view.findViewById(R.id.recycler_territory);
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
        recycler_territory.setLayoutManager(mLayoutManager);
        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getReadableDatabase();


        SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_LEADER, MODE_PRIVATE);
        String login = prefs1.getString("firsttime", "");

        if (login.equalsIgnoreCase("1")) {
            progressBar.setVisibility(View.GONE);
            dbHelper = new DBHelper(getActivity());
            territorydbList = dbHelper.getTerritoryList();
            TerritoryAdapter territoryAdapter = new TerritoryAdapter(getActivity(), territorydbList);
            territoryAdapter.notifyDataSetChanged();
            recycler_territory.setAdapter(territoryAdapter);

            SharedPreferences prefs2 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
            String totalpoint = prefs2.getString("totalpoint", "");
            String rank = prefs2.getString("rank", "");
            LeaderboardFragment.txt_cnt.setText(totalpoint);
            LeaderboardFragment.txt_rank.setText(rank);
        } else {
            if (cd.isConnectingToInternet()) {
                LeaderBoardReward(token, eventid);
            } else {
                progressBar.setVisibility(View.GONE);
                dbHelper = new DBHelper(getActivity());
                territorydbList = dbHelper.getTerritoryList();
                TerritoryAdapter territoryAdapter = new TerritoryAdapter(getActivity(), territorydbList);
                territoryAdapter.notifyDataSetChanged();
                recycler_territory.setAdapter(territoryAdapter);

                SharedPreferences prefs3 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                String totalpoint = prefs3.getString("totalpoint", "");
                String rank = prefs3.getString("rank", "");
                LeaderboardFragment.txt_cnt.setText(totalpoint);
                LeaderboardFragment.txt_rank.setText(rank);


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
                    territorydbList = dbHelper.getTerritoryList();
                    TerritoryAdapter territoryAdapter = new TerritoryAdapter(getActivity(), territorydbList);
                    territoryAdapter.notifyDataSetChanged();
                    recycler_territory.setAdapter(territoryAdapter);

                    SharedPreferences prefs = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                    if (prefs.contains("totalpoint")) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("totalpoint");
                        editor.commit();
                    }

                    SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                    String totalpoint = prefs1.getString("totalpoint", "");
                    String date = prefs1.getString("date", "");
                    String rank = prefs1.getString("rank", "");
                    LeaderboardFragment.txt_cnt.setText(totalpoint);
                    LeaderboardFragment.txt_time.setText("As on " + date);
                    LeaderboardFragment.txt_rank.setText(rank);

                }
            }
        });

        CommonFunction.crashlytics("TerritoryFrag","");
        firbaseAnalytics(getActivity(),"TerritoryFrag","");
        return view;
    }


    public void LeaderBoardReward(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.LeaderBoardReward(token, eventid).enqueue(new Callback<leaderboard_data>() {
            @Override
            public void onResponse(Call<leaderboard_data> call, Response<leaderboard_data> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (pullrefresh.isRefreshing()) {
                        pullrefresh.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    territoryList.clear();
                    procializeDB.clearTerritoryTable();
                    territoryList = response.body().getTerritory_data();

                    procializeDB.insertTerritoryTable(territoryList, db);


                    dbHelper = new DBHelper(getActivity());
                    territoryList = new ArrayList<territory_data>();
                    territorydbList = dbHelper.getTerritoryList();

                    TerritoryAdapter territoryAdapter = new TerritoryAdapter(getActivity(), territorydbList);
                    territoryAdapter.notifyDataSetChanged();
                    recycler_territory.setAdapter(territoryAdapter);
                    LeaderboardFragment.txt_cnt.setText(response.body().getTotal_available_point());
                    LeaderboardFragment.txt_rank.setText(response.body().getTerritory_rank());
//                    LeaderboardFragment.txt_time.setText("As On " + response.body().getAs_on_date());
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE).edit();
                    editor.putString("totalpoint", response.body().getTotal_available_point()).commit();
                    editor.putString("date", response.body().getAs_on_date()).commit();
                    editor.putString("rank", response.body().getTerritory_rank()).commit();
                    editor.apply();

                    SharedPreferences.Editor editor1 = getActivity().getSharedPreferences(MY_PREFS_LEADER, MODE_PRIVATE).edit();
                    editor1.putString("firsttimeter", "1");
                    editor1.apply();

                    Log.d("Tearritory point", response.body().getTerritory_rank());
                    Log.d("Region point", response.body().getRegion_rank());
                    Log.d("Date", response.body().getAs_on_date());

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
