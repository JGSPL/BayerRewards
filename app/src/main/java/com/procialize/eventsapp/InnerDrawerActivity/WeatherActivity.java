package com.procialize.eventsapp.InnerDrawerActivity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.procialize.eventsapp.Adapter.WeatherAdapter;
import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.GetterSetter.Forecast;
import com.procialize.eventsapp.GetterSetter.Weather;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements WeatherAdapter.WeatherAdapterListner {

    RecyclerView weatherRV;
    ImageView infoIv, headerlogoIv;
    TextView cityTv, countryTv, dateTv, timeTv, infoTv, feelTv, humidityTv, visibilityTv, indexTv, tempTv, maxTv, minTv, dayTv, twTitle, cTv, fTv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    LinearLayout twLin;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "95");
        colorActive = prefs.getString("colorActive", "");


        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
        mAPIService = ApiUtils.getAPIService();

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        headerlogoIv = findViewById(R.id.headerlogoIv);
//        Util.logomethod(this, headerlogoIv);


        cityTv = findViewById(R.id.cityTv);
        countryTv = findViewById(R.id.countryTv);
        dateTv = findViewById(R.id.dateTv);
        timeTv = findViewById(R.id.timeTv);
        infoTv = findViewById(R.id.infoTv);
        feelTv = findViewById(R.id.feelTv);

        cTv = findViewById(R.id.cTv);
        fTv = findViewById(R.id.fTv);

        humidityTv = findViewById(R.id.humidityTv);
        visibilityTv = findViewById(R.id.visibilityTv);
        indexTv = findViewById(R.id.indexTv);

        tempTv = findViewById(R.id.tempTv);
        maxTv = findViewById(R.id.maxTv);
        minTv = findViewById(R.id.minTv);
        dayTv = findViewById(R.id.dayTv);
        twLin = findViewById(R.id.twLin);
        twTitle = findViewById(R.id.twTitle);


        infoIv = findViewById(R.id.infoIv);
        weatherRV = findViewById(R.id.weatherRV);

        twTitle.setTextColor(Color.parseColor(colorActive));
        twLin.setBackgroundColor(Color.parseColor(colorActive));
        dayTv.setTextColor(Color.parseColor(colorActive));
        tempTv.setTextColor(Color.parseColor(colorActive));
        cTv.setTextColor(Color.parseColor(colorActive));
        fTv.setTextColor(Color.parseColor(colorActive));
        maxTv.setTextColor(Color.parseColor(colorActive));
        minTv.setTextColor(Color.parseColor(colorActive));
        infoTv.setTextColor(Color.parseColor(colorActive));
        maxTv.setTextColor(Color.parseColor(colorActive));

        ImageView ic_rightarrow = findViewById(R.id.highiv);
        ImageView ic_rightarrow1 = findViewById(R.id.lowiv);
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        ic_rightarrow.setImageDrawable(drawable);


        int colorInt2 = Color.parseColor(colorActive);

        ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

        Drawable drawable1 = DrawableCompat.wrap(ic_rightarrow1.getDrawable());
        DrawableCompat.setTintList(drawable1, csl2);
        ic_rightarrow1.setImageDrawable(drawable1);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        weatherRV.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //  weatherRV.setLayoutAnimation(animation);


        fetchWeather(token, eventid);

    }


    public void fetchWeather(String token, String eventid) {
        mAPIService.WeatherListFetch(token, eventid).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<Weather> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            cityTv.setText(response.body().getCityname());
            countryTv.setText(response.body().getCountryname());

            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");


            Date date = new Date();
            try {
                date = formatter.parse(response.body().getDateTime());
                System.out.println("Date is: " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String dmy = mdyFormat.format(date);
            String time = timeFormat.format(date);
            String day = dayFormat.format(date);


            dateTv.setText(dmy);
            timeTv.setText(time);

            if (response.body().getCurrentTempText().equalsIgnoreCase("Thunderstorms")) {
                infoIv.setImageResource(R.drawable.thunder);
            } else if (response.body().getCurrentTempText().equalsIgnoreCase("Mostly Cloudy")) {
                infoIv.setImageResource(R.drawable.clean);
            } else if (response.body().getCurrentTempText().equalsIgnoreCase("Cloudy")) {
                infoIv.setImageResource(R.drawable.cloudy);
            } else if (response.body().getCurrentTempText().equalsIgnoreCase("Partly Cloudy")) {
                infoIv.setImageResource(R.drawable.clean);
            } else {
                infoIv.setImageResource(R.drawable.clean);
            }

            char tmp = 0x00B0;

            dayTv.setText(day);
            infoTv.setText(response.body().getCurrentTempText());
            tempTv.setText(String.valueOf(response.body().getCurrentTemp()) + tmp + "");
            //  feelTv.setText(String.valueOf(response.body().getCurrentTemp()) + tmp+"");
            humidityTv.setText(response.body().getHumidity());
            visibilityTv.setText(response.body().getVisibility());
            feelTv.setText(response.body().getSunrise());


            WeatherAdapter docAdapter = new WeatherAdapter(WeatherActivity.this, response.body().getForecast(), this);
            docAdapter.notifyDataSetChanged();
            weatherRV.setAdapter(docAdapter);
//            weatherRV.scheduleLayoutAnimation();


            maxTv.setText(String.valueOf(response.body().getMax()) + tmp);
            minTv.setText(String.valueOf(response.body().getMin()) + tmp);

            indexTv.setText(response.body().getSunset());
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onContactSelected(Forecast forecast) {

    }
}
