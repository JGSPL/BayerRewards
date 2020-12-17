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

import com.bayer.bayerreward.Adapter.RegionAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.leaderboard_data;
import com.bayer.bayerreward.GetterSetter.region_data;
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


public class RegionFragment extends Fragment {
    RecyclerView recycler_region;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, token;
    ConnectionDetector cd;
    APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    SwipeRefreshLayout pullrefresh;
    String MY_TOTAL_POINTS = "totalPoints";
    String MY_PREFS_LEADER = "ProcializeLeader";
    String MY_PREFS_RANK = "rank";


    private List<region_data> regionList = new ArrayList<region_data>();
    private List<region_data> regiondbList = new ArrayList<region_data>();

    public RegionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_region, container, false);

        recycler_region = view.findViewById(R.id.recycler_region);
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
        String login = prefs1.getString("firsttimereg", "");
        SharedPreferences prefs3 = getActivity().getSharedPreferences(MY_PREFS_RANK, MODE_PRIVATE);
        String rankregion = prefs3.getString("rankregion", "");



        if (login.equalsIgnoreCase("1")) {
            progressBar.setVisibility(View.GONE);
            dbHelper = new DBHelper(getActivity());
            regiondbList = dbHelper.getRegionList();
            RegionAdapter territoryAdapter = new RegionAdapter(getActivity(), regiondbList);
            territoryAdapter.notifyDataSetChanged();
            recycler_region.setAdapter(territoryAdapter);

            SharedPreferences prefs2 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
            String totalpoint = prefs2.getString("totalpoint", "");


            LeaderboardFragment.txt_cnt.setText(totalpoint);
            LeaderboardFragment.txt_rank.setText(rankregion);
        } else {
            if (cd.isConnectingToInternet()) {
                LeaderBoardReward(token, eventid);
            } else {
                progressBar.setVisibility(View.GONE);
                dbHelper = new DBHelper(getActivity());
                regiondbList = dbHelper.getRegionList();
                RegionAdapter territoryAdapter = new RegionAdapter(getActivity(), regiondbList);
                territoryAdapter.notifyDataSetChanged();
                recycler_region.setAdapter(territoryAdapter);

                SharedPreferences prefs4 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                String totalpoint = prefs4.getString("totalpoint", "");

                LeaderboardFragment.txt_cnt.setText(totalpoint);
                LeaderboardFragment.txt_rank.setText(rankregion);

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
                    regiondbList = dbHelper.getRegionList();
                    RegionAdapter territoryAdapter = new RegionAdapter(getActivity(), regiondbList);
                    territoryAdapter.notifyDataSetChanged();
                    recycler_region.setAdapter(territoryAdapter);

                }
            }
        });

        CommonFunction.crashlytics("RegionFrag","");
        firbaseAnalytics(getActivity(),"RegionFrag","");
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
                    LeaderboardFragment.txt_rank.setText(response.body().getRegion_rank());
                    progressBar.setVisibility(View.GONE);
                    regionList.clear();
                    procializeDB.clearRegionTable();
                    regionList = response.body().getRegion_data();

                    procializeDB.insertRegionTable(regionList, db);


                    dbHelper = new DBHelper(getActivity());
                    regionList = new ArrayList<region_data>();
                    regiondbList = dbHelper.getRegionList();
                    RegionAdapter territoryAdapter = new RegionAdapter(getActivity(), regiondbList);
                    territoryAdapter.notifyDataSetChanged();
                    recycler_region.setAdapter(territoryAdapter);
                    SharedPreferences.Editor editor1 = getActivity().getSharedPreferences(MY_PREFS_LEADER, MODE_PRIVATE).edit();
                    editor1.putString("firsttimereg", "1");
                    editor1.apply();


                    SharedPreferences.Editor editor2 = getActivity().getSharedPreferences(MY_PREFS_RANK, MODE_PRIVATE).edit();
                    editor2.putString("rankregion", response.body().getRegion_rank());
                    editor2.apply();

                    Log.d("Tearritory point",response.body().getTerritory_rank());
                    Log.d("Region point",response.body().getRegion_rank());


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
