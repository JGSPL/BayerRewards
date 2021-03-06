package com.bayer.bayerreward.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.AgendaDetailActivity;
import com.bayer.bayerreward.Adapter.AgendaAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.AgendaList;
import com.bayer.bayerreward.GetterSetter.Analytic;
import com.bayer.bayerreward.GetterSetter.FetchAgenda;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class AgendaActivity extends AppCompatActivity implements AgendaAdapter.AgendaAdapterListner {

    RecyclerView agendarecycler;
    SwipeRefreshLayout agendafeedrefresh;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        agendafeedrefresh = findViewById(R.id.agendafeedrefresh);
        agendarecycler = findViewById(R.id.agendarecycler);
        progressBar = findViewById(R.id.progressBar);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        agendarecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        agendarecycler.setLayoutAnimation(animation);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        fetchAgenda(token, eventid);

        SubmitAnalytics(token, eventid, "", "", "agenda");

        agendafeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchAgenda(token, eventid);
            }
        });

        CommonFunction.crashlytics("AgendaActivity","");
        firbaseAnalytics(this,"AgendaActivity","");
    }


    public void fetchAgenda(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.AgendaFetchPost(token, eventid).enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);

                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {
                    progressBar.setVisibility(View.GONE);
                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(AgendaActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressBar.setVisibility(View.GONE);
                if (agendafeedrefresh.isRefreshing()) {
                    agendafeedrefresh.setRefreshing(false);
                }
                Toast.makeText(AgendaActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<FetchAgenda> response) {
//        String date = "";
//        for(int i=0;i<response.body().getAgendaList().size();i++){
//            if(response.body().getAgendaList().get(i).getSessionDate().equalsIgnoreCase(date)){
//                date = response.body().getAgendaList().get(i).getSessionDate();
//                tempagendaList.add(response.body().getAgendaList().get(i));
//            }else {
//                date = response.body().getAgendaList().get(i).getSessionDate();
//            }
//        }
        // specify an adapter (see also next example)
        try {
            AgendaAdapter agendaAdapter = new AgendaAdapter(AgendaActivity.this, response.body().getAgendaList(), this);
            agendaAdapter.notifyDataSetChanged();
            agendarecycler.setAdapter(agendaAdapter);
            agendarecycler.scheduleLayoutAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onContactSelected(AgendaList agenda) {
        Intent agendadetail = new Intent(this, AgendaDetailActivity.class);

        agendadetail.putExtra("id", agenda.getSessionId());
        agendadetail.putExtra("date", agenda.getSessionDate());
        agendadetail.putExtra("name", agenda.getSessionName());
        agendadetail.putExtra("description", agenda.getSessionDescription());
        agendadetail.putExtra("starttime", agenda.getSessionStartTime());
        agendadetail.putExtra("endtime", agenda.getSessionEndTime());

        startActivity(agendadetail);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

//                    Toast.makeText(AgendaActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
//                Toast.makeText(AgendaActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
