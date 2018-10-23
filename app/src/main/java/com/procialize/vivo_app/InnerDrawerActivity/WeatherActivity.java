package com.procialize.vivo_app.InnerDrawerActivity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.vivo_app.Adapter.WeatherAdapter;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.GetterSetter.Forecast;
import com.procialize.vivo_app.GetterSetter.Weather;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements WeatherAdapter.WeatherAdapterListner{

    RecyclerView weatherRV;
    ImageView infoIv;
    TextView cityTv,countryTv,dateTv,timeTv,infoTv,feelTv,humidityTv,visibilityTv,indexTv,tempTv,maxTv,minTv,dayTv;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
        mAPIService = ApiUtils.getAPIService();


        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        cityTv = findViewById(R.id.cityTv);
        countryTv = findViewById(R.id.countryTv);
        dateTv = findViewById(R.id.dateTv);
        timeTv = findViewById(R.id.timeTv);
        infoTv = findViewById(R.id.infoTv);
        feelTv = findViewById(R.id.feelTv);
        humidityTv = findViewById(R.id.humidityTv);
        visibilityTv = findViewById(R.id.visibilityTv);
        indexTv = findViewById(R.id.indexTv);

        tempTv = findViewById(R.id.tempTv);
        maxTv = findViewById(R.id.maxTv);
        minTv = findViewById(R.id.minTv);
        dayTv = findViewById(R.id.dayTv);


        infoIv = findViewById(R.id.infoIv);
        weatherRV = findViewById(R.id.weatherRV);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        weatherRV.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        weatherRV.setLayoutAnimation(animation);


        fetchWeather(token,eventid);

    }


    public void fetchWeather(String token, String eventid) {
        mAPIService.WeatherListFetch(token,eventid).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                if(response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                }else
                {
                    Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Low network or no network",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<Weather> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success"))
        {

            cityTv.setText(response.body().getCityname());
            countryTv.setText(response.body().getCountryname());

            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
            SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM");
            Date date = new Date();
            try {
                date = formatter.parse(response.body().getDateTime());
                System.out.println("Date is: "+date);
            } catch (ParseException e) {e.printStackTrace();}

            String dmy = mdyFormat.format(date);
            String time = timeFormat.format(date);
            String day = dayFormat.format(date);


            dateTv.setText(dmy);
            timeTv.setText(time);

            if (response.body().getCurrentTempText().equalsIgnoreCase("Thunderstorms"))
            {
                infoIv.setImageResource(R.drawable.thunder);
            }else if (response.body().getCurrentTempText().equalsIgnoreCase("Mostly Cloud"))
            {
                infoIv.setImageResource(R.drawable.clean);
            }else if (response.body().getCurrentTempText().equalsIgnoreCase("Partly Cloudy"))
            {
                infoIv.setImageResource(R.drawable.clean);
            }else if (response.body().getCurrentTempText().equalsIgnoreCase("Cloudy"))
            {
                infoIv.setImageResource(R.drawable.cloudy);
            }else
            {
                infoIv.setImageResource(R.drawable.sunny);
            }

            char tmp = 0x00B0;

            infoTv.setText(response.body().getCurrentTempText());
            tempTv.setText(String.valueOf(response.body().getCurrentTemp()) + tmp+"" );
            feelTv.setText(String.valueOf(response.body().getCurrentTemp()) + tmp+"");
            humidityTv.setText(response.body().getHumidity());
            visibilityTv.setText(response.body().getVisibility());

            WeatherAdapter docAdapter = new WeatherAdapter(WeatherActivity.this, response.body().getForecast(),this);
            docAdapter.notifyDataSetChanged();
            weatherRV.setAdapter(docAdapter);
            weatherRV.scheduleLayoutAnimation();


            maxTv.setText(String.valueOf(response.body().getMax()) + tmp );
            minTv.setText(String.valueOf(response.body().getMin()) + tmp );

        }else
        {
            Toast.makeText(getApplicationContext(),response.body().getMsg(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onContactSelected(Forecast forecast)
    {

    }
}
