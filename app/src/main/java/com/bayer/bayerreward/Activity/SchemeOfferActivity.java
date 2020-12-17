package com.bayer.bayerreward.Activity;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Adapter.SchemeOfferAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.SchemesAndOffers;
import com.bayer.bayerreward.GetterSetter.schemes_and_offers_list;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class SchemeOfferActivity extends AppCompatActivity implements SchemeOfferAdapter.SchemeOfferAdapterListner {

    RecyclerView recycler_scheme;
    TextView txt_title;
    ImageView headerlogoIv;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    SchemeOfferAdapter schemeOfferAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_offer);

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

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        recycler_scheme = findViewById(R.id.recycler_scheme);
        txt_title = findViewById(R.id.txt_title);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(SchemeOfferActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_scheme.setLayoutManager(mLayoutManager);

        SchemesAndOffers(apikey, eventid);
        CommonFunction.crashlytics("SchemeOffer","");
        firbaseAnalytics(this,"SchemeOffer","");

    }

    public void SchemesAndOffers(String token, String eventid) {

        mAPIService.SchemesAndOffers(token, eventid).enqueue(new Callback<SchemesAndOffers>() {
            @Override
            public void onResponse(Call<SchemesAndOffers> call, Response<SchemesAndOffers> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());
                    try {
                        schemeOfferAdapter = new SchemeOfferAdapter(SchemeOfferActivity.this, response.body().getSchemes_and_offers_list(), SchemeOfferActivity.this);
                        schemeOfferAdapter.notifyDataSetChanged();
                        recycler_scheme.setAdapter(schemeOfferAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SchemesAndOffers> call, Throwable t) {


            }
        });
    }

    @Override
    public void onContactSelected(schemes_and_offers_list travel) {
        Intent intent = new Intent(SchemeOfferActivity.this, SchemeDetailActivity.class);
        intent.putExtra("title", travel.getTitle());
        intent.putExtra("desc", travel.getDescription());
        startActivity(intent);
    }
}
