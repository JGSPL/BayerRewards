package com.bayer.bayerreward.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Adapter.MPointCalculatorAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.MpointCalculator;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class MpointCalc extends AppCompatActivity {

    RecyclerView recycler_mpointcalc;
    ImageView headerlogoIv;
    ProgressBar progressBar;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    SessionManager sessionManager;
    String eventid, colorActive, apikey;
    public static TextView txt_score;
    MPointCalculatorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpoint_calc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#0092df"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        apikey = user.get(SessionManager.KEY_TOKEN);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        progressBar = findViewById(R.id.progressBar);
        txt_score = findViewById(R.id.txt_score);
        recycler_mpointcalc = findViewById(R.id.recycler_mpointcalc);
        Util.logomethod(this, headerlogoIv);
        mAPIService = ApiUtils.getAPIService();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_mpointcalc.setLayoutManager(mLayoutManager);

        MPointCalcList(apikey, eventid);

        CommonFunction.crashlytics("Mpoint","");
        firbaseAnalytics(this,"Mpoint","");

    }

    public void MPointCalcList(String token, String eventid) {
        showProgress();
        mAPIService.MPointCalcList(token, eventid).enqueue(new Callback<MpointCalculator>() {
            @Override
            public void onResponse(Call<MpointCalculator> call, Response<MpointCalculator> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    adapter = new MPointCalculatorAdapter(MpointCalc.this, response.body().getM_points_list());
                    adapter.notifyDataSetChanged();
                    recycler_mpointcalc.setAdapter(adapter);
//                    showResponse(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MpointCalculator> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();

            }
        });
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
