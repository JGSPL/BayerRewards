package com.bayer.bayerreward.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.CatlogActivity;
import com.bayer.bayerreward.Adapter.RedeemAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.GetterSetter.Catlog;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;


public class RequestToRedeem extends Fragment {

    Dialog myDialog;
    RecyclerView recycle_redeem;
    ProgressBar progressbar;
    APIService mAPIService;
    ConnectionDetector cd;
    String apikey, eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    SessionManager sessionManager;
    RedeemAdapter redeemAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_to_redeem, container, false);

        recycle_redeem = view.findViewById(R.id.recycle_redeem);
        progressbar = view.findViewById(R.id.progressbar);

        mAPIService = ApiUtils.getAPIService();

        cd = new ConnectionDetector(getActivity());
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycle_redeem.setLayoutManager(mLayoutManager);

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        if (cd.isConnectingToInternet()) {
            CatlogList(eventid, apikey);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        CommonFunction.crashlytics("RequestToRedeemFrag", "");
        firbaseAnalytics(getActivity(), "RequestToRedeemFrag", "");
        return view;
    }

    public void CatlogList(String eventid, String token) {
        showProgress();
//        showProgress();
        mAPIService.CatlogList(token, eventid).enqueue(new Callback<Catlog>() {
            @Override
            public void onResponse(Call<Catlog> call, Response<Catlog> response) {

                if (response.isSuccessful()) {
                    dismissProgress();
                    redeemAdapter = new RedeemAdapter(getActivity(), response.body().getCatalog_list());
                    redeemAdapter.notifyDataSetChanged();
                    recycle_redeem.setAdapter(redeemAdapter);

                    if ((Integer.parseInt(response.body().getTotal_available_point())) < 0) {
                        CatlogActivity.txt_totalpoints.setText("0");
                    } else {
                        CatlogActivity.txt_totalpoints.setText(response.body().getTotal_available_point());
                    }

                } else {
                    dismissProgress();


                }
            }

            @Override
            public void onFailure(Call<Catlog> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Low network or no network");

            }
        });
    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        progressbar.setVisibility(View.GONE);
    }


}
