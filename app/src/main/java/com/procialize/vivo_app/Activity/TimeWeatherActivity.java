package com.procialize.vivo_app.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.GetterSetter.TimeWeather;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeWeatherActivity extends AppCompatActivity {

    TextView txtCity, temp, min, max, txtdate;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ProgressDialog progressDialog;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_weather);

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
        Util.logomethod(this,headerlogoIv);

        txtCity = (TextView) findViewById(R.id.txtCity);
        temp = (TextView) findViewById(R.id.temp);
        max = (TextView) findViewById(R.id.max);
        min = (TextView) findViewById(R.id.min);
        txtdate = (TextView) findViewById(R.id.txtdate);

        SessionManager sessionManager = new SessionManager(TimeWeatherActivity.this);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


        getInfoTab();

    }

    public void getInfoTab() {
        progressDialog = new ProgressDialog(TimeWeatherActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAPIService.FetchTimeWeather(eventid).enqueue(new Callback<TimeWeather>() {
            @Override
            public void onResponse(Call<TimeWeather> call, Response<TimeWeather> response) {

                if (response.body().getStatus().equals("success")) {
                    progressDialog.dismiss();
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    String date_time = response.body().getDate_time();
                    String mintemp = response.body().getMin();
                    String maxtemp = response.body().getMax();
                    String cityname = response.body().getCityname();
                    String currenttemp = response.body().getCurrent_temp();

                    txtCity.setText(cityname);
                    temp.setText(currenttemp + "°");
                    min.setText(mintemp);
                    max.setText(maxtemp);
                    txtdate.setText(date_time);


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(TimeWeatherActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TimeWeather> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(TimeWeatherActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
