package com.bayer.bayerreward.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Adapter.MyPerformanceAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.MyPerformance;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.utils.CommonFunction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;


public class MyPreferenceFragment extends Fragment {

    TextView txt_points, txt_achived_value, txt_achived_qpoints, txt_time;
    RecyclerView recycler_prod;
    APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, token;
    MyPerformanceAdapter myPerformanceAdapter;
    ProgressBar progressbar;
    SwipeRefreshLayout refreash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_preference, container, false);
        MpinScanner.count = 0;
        txt_points = view.findViewById(R.id.txt_points);
        txt_achived_value = view.findViewById(R.id.txt_achived_value);
        txt_achived_qpoints = view.findViewById(R.id.txt_achived_qpoints);
        txt_time = view.findViewById(R.id.txt_time);
        recycler_prod = view.findViewById(R.id.recycler_prod);
        progressbar = view.findViewById(R.id.progressbar);
        refreash = view.findViewById(R.id.refreash);

        mAPIService = ApiUtils.getAPIService();
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get(SessionManager.KEY_TOKEN);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_prod.setLayoutManager(mLayoutManager);

        refreash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyPerformance(token, eventid);
            }
        });

        MyPerformance(token, eventid);
        CommonFunction.crashlytics("MyPrefFrag", "");
        firbaseAnalytics(getActivity(), "MyPrefFrag", "");
        return view;
    }

    public void Refreash() {
        MyPerformance(token, eventid);
    }

    public void MyPerformance(String token, String eventid) {
        progressbar.setVisibility(View.VISIBLE);
        mAPIService.MyPerformance(token, eventid).enqueue(new Callback<MyPerformance>() {
            @Override
            public void onResponse(Call<MyPerformance> call, Response<MyPerformance> response) {
                progressbar.setVisibility(View.GONE);
                refreash.setRefreshing(false);
                try {
                    if (response.isSuccessful()) {
                        txt_achived_qpoints.setText(response.body().getTotal_achieved_qpoint_value() + "%");
                        txt_achived_value.setText(response.body().getTotal_achieved_value() + "%");
                        txt_points.setText(response.body().getTotal_available_point());
                        myPerformanceAdapter = new MyPerformanceAdapter(getActivity(), response.body().getProduct_achived_planned());
                        myPerformanceAdapter.notifyDataSetChanged();
                        recycler_prod.setAdapter(myPerformanceAdapter);

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                        String datetime = dateformat.format(c.getTime());
                        txt_time.setText("As on " + datetime);

                    } else {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    refreash.setRefreshing(false);
                    progressbar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<MyPerformance> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                refreash.setRefreshing(false);
                Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MyPerformance(token, eventid);
    }
}
