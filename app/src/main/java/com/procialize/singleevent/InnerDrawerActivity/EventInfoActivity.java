package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.procialize.singleevent.Adapter.AgendaAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.EventInfoFetch;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.FetchAgenda;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoActivity extends FragmentActivity implements OnMapReadyCallback {


    ImageView logoIv;
    TextView nameTv, dateTv, cityTv, eventvenu, event_desc;
    private APIService mAPIService;
    private GoogleMap map;
    LatLng position;
    MarkerOptions options;
    private Date d2, d1;
    SessionManager sessionManager;
    String token;
    ProgressBar progressbar;
    String event_info_display_map, event_info_description;
    List<EventSettingList> eventSettingLists;
    SupportMapFragment fm;
    ImageView back;
    LinearLayout linMap;

    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo2);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


        Toolbar toolbar = findViewById(R.id.toolbar);


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);
        sessionManager = new SessionManager(this);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();


        token = user.get(SessionManager.KEY_TOKEN);

        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        mAPIService = ApiUtils.getAPIService();

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);

        logoIv = findViewById(R.id.logoIv);
        nameTv = findViewById(R.id.nameTv);
        dateTv = findViewById(R.id.dateTv);
        cityTv = findViewById(R.id.cityTv);
        eventvenu = findViewById(R.id.eventvenu);
        event_desc = findViewById(R.id.event_desc);
        progressbar = findViewById(R.id.progressbar);
        back = findViewById(R.id.back);
        linMap = findViewById(R.id.linMap);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("event_info_display_map")) {
                event_info_display_map = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("event_info_description")) {
                event_info_description = eventSettingLists.get(i).getFieldValue();
            }

        }
    }


    public void fetchEventInfo(String token, String eventid) {
        showProgress();
        mAPIService.EventInfoFetch(token, eventid).enqueue(new Callback<EventInfoFetch>() {
            @Override
            public void onResponse(Call<EventInfoFetch> call, Response<EventInfoFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventInfoFetch> call, Throwable t) {
                dismissProgress();
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponse(final Response<EventInfoFetch> response) {

        // specify an adapter (see also next example)

        if (response.body().getStatus().equalsIgnoreCase("success")) {
            String startTime = "", endTime = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDateandTime = sdf.format(new Date());
            try {
                if (response.body().getEventList().get(0).getEventStart().equals("null") && response.body().getEventList().get(0).getEventStart() != null && !response.body().getEventList().get(0).getEventStart().isEmpty()) {
                    startTime = currentDateandTime;
                } else {
                    startTime = response.body().getEventList().get(0).getEventStart();
                }

                if (response.body().getEventList().get(0).getEventEnd().equals("null") && response.body().getEventList().get(0).getEventEnd() != null && response.body().getEventList().get(0).getEventEnd().isEmpty()) {
                    endTime = currentDateandTime;
                } else {
                    endTime = response.body().getEventList().get(0).getEventEnd();
                }
            } catch (Exception e) {
                startTime = currentDateandTime;
                endTime = currentDateandTime;
            }


            // SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

            try {
                d1 = f.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long millisecondsStart = d1.getTime();

            try {
                d2 = f.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long millisecondsEnd = d2.getTime();

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

            String finalStartTime = formatter.format(new Date(millisecondsStart));
            String finalEndTime = formatter.format(new Date(millisecondsEnd));

            try {
                nameTv.setText(response.body().getEventList().get(0).getEventName());
                if(finalStartTime.equalsIgnoreCase(finalEndTime)){
                    dateTv.setText(finalStartTime);

                }else {
                    dateTv.setText(finalStartTime + " - " + finalEndTime);
                }
                cityTv.setText(response.body().getEventList().get(0).getEventCity());
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (event_info_description.equalsIgnoreCase("1") && response.body().getEventList().get(0).getEventDescription() != null) {

                    event_desc.setVisibility(View.VISIBLE);
                    eventvenu.setVisibility(View.VISIBLE);
                } else {
                    event_desc.setVisibility(View.GONE);
                    eventvenu.setVisibility(View.GONE);
                }
                eventvenu.setText("Venue:- " + response.body().getEventList().get(0).getEventLocation());
                event_desc.setText(response.body().getEventList().get(0).getEventDescription());
                String image_final_url = /*ApiConstant.baseUrl*/"https://www.procialize.info/uploads/app_logo/"+ response.body().getEventList().get(0).getLogo();

//                Glide.with(getApplicationContext()).load(image_final_url).into(logoIv).onLoadStarted(getDrawable(R.drawable.logo));
                Glide.with(getApplicationContext()).load(image_final_url)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        logoIv.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).into(logoIv).onLoadStarted(this.getDrawable(R.drawable.profilepic_placeholder));
            } catch (Exception e) {
                e.printStackTrace();
            }

            linMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String label = "ABC Label";
                    String uriBegin = "geo:" + response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLongitude();
                    String query = response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLatitude() + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(intent);


                }
            });



            try {
                if (map != null && event_info_display_map.equalsIgnoreCase("1")) {

                    fm.getView().setVisibility(View.VISIBLE);
                    position = new LatLng(Double.parseDouble(response.body().getEventList().get(0).getEventLatitude()), Double.parseDouble(response.body().getEventList().get(0).getEventLongitude()));

                    CameraUpdate updatePosition1 = CameraUpdateFactory.newLatLng(position);

                    map.moveCamera(updatePosition1);

                    // Instantiating MarkerOptions class
                    options = new MarkerOptions();

                    // Setting position for the MarkerOptions
                    options.position(position);

                    // Setting title for the MarkerOptions
                    options.title("Venue");

                    // Setting snippet for the MarkerOptions
                    options.snippet("Venue:- " + response.body().getEventList().get(0).getEventLocation());


                    // Adding Marker on the Google Map
                    map.addMarker(options);


                    moveToCurrentLocation(position);
                } else {
                    fm.getView().setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }


    }


    private void moveToCurrentLocation(LatLng currentLocation) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        fetchEventInfo(token, eventid);

    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
