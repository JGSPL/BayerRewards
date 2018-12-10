package com.procialize.singleevent.InnerDrawerActivity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Activity.VideoViewActivity;
import com.procialize.singleevent.Adapter.VideoAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.FirstLevelFilter;
import com.procialize.singleevent.GetterSetter.VideoFetchListFetch;
import com.procialize.singleevent.GetterSetter.VideoFolderList;
import com.procialize.singleevent.GetterSetter.VideoList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterListner {

    private APIService mAPIService;
    SwipeRefreshLayout videoRvrefresh;
    RecyclerView videoRv;
    ProgressBar progressBar;

    private static List<VideoList> videoLists;
    private static List<VideoFolderList> folderLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid,colorActive;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");


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

        videoRv = findViewById(R.id.videoRv);
        progressBar = findViewById(R.id.progressBar);
        videoRvrefresh = findViewById(R.id.videoRvrefresh);
        TextView header = (TextView)findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        int columns = 2;
        videoRv.setLayoutManager(new GridLayoutManager(this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
       // videoRv.setLayoutAnimation(animation);


        fetchVideo(token, eventid);

        videoRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchVideo(token, eventid);
            }
        });
    }


    public void fetchVideo(String token, String eventid) {
        showProgress();
        mAPIService.VideoFetchListFetch(token, eventid).enqueue(new Callback<VideoFetchListFetch>() {
            @Override
            public void onResponse(Call<VideoFetchListFetch> call, Response<VideoFetchListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (videoRvrefresh.isRefreshing()) {
                        videoRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();

                    showResponse(response);
                } else {
                    if (videoRvrefresh.isRefreshing()) {
                        videoRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoFetchListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (videoRvrefresh.isRefreshing()) {
                    videoRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<VideoFetchListFetch> response) {


        // specify an adapter (see also next example)
        if (response.body().getVideoList().size() != 0) {

            videoLists = response.body().getVideoList();
            folderLists = response.body().getVideoFolderList();


            List<FirstLevelFilter> filtergallerylists = new ArrayList<>();
            Log.d("Video Response", String.valueOf(response.body().getVideoFolderList()));

            if (response.body().getVideoList().size() != 0 || response.body().getVideoFolderList().size() != 0) {

                if (response.body().getVideoFolderList().size() != 0) {
                    for (int i = 0; i < response.body().getVideoFolderList().size(); i++) {
                        if (response.body().getVideoFolderList().get(i).getFolderName() != null || !response.body().getVideoFolderList().get(i).getFolderName().equalsIgnoreCase("null")) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            if (!response.body().getVideoFolderList().get(i).getFolderName().contains("/")) {
                                firstLevelFilter.setFolderName(response.body().getVideoFolderList().get(i).getFolderName());
                                firstLevelFilter.setTitle(response.body().getVideoFolderList().get(i).getFolderName());
                                firstLevelFilter.setFileName(ApiConstant.folderimage + response.body().getVideoFolderList().get(i).getFolderImage());

                                filtergallerylists.add(firstLevelFilter);
                            }
                        }
                    }
                }

                for (int i = 0; i < response.body().getVideoList().size(); i++) {
                    if (response.body().getVideoList().get(i).getFolderName() == null || response.body().getVideoList().get(i).getFolderName().equalsIgnoreCase("null")) {
                        FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                        firstLevelFilter.setTitle(response.body().getVideoList().get(i).getTitle());
                        firstLevelFilter.setFileName(response.body().getVideoList().get(i).getVideoUrl());
                        firstLevelFilter.setFolderName(response.body().getVideoList().get(i).getFolderName());

                        filtergallerylists.add(firstLevelFilter);
                    }
                }


                VideoAdapter videoAdapter = new VideoAdapter(this, filtergallerylists, this);
                videoAdapter.notifyDataSetChanged();
                videoRv.setAdapter(videoAdapter);
                videoRv.scheduleLayoutAnimation();
            } else {
                Toast.makeText(getApplicationContext(), "No Video Available", Toast.LENGTH_SHORT).show();

            }
        } else {
            setContentView(R.layout.activity_empty_view);
            ImageView imageView = findViewById(R.id.back);
            TextView text_empty = findViewById(R.id.text_empty);
            text_empty.setText("Video's not available");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    @Override
    public void onContactSelected(FirstLevelFilter firstLevelFilter) {


        if (firstLevelFilter.getFolderName() == null || firstLevelFilter.getFolderName().equalsIgnoreCase("null")) {
            Boolean flagUrl = firstLevelFilter.getFileName()
                    .contains("youtu");

            // Check for Internet Connection

            if (flagUrl) {

                String videoUrl = firstLevelFilter.getFileName();

//                    String videoId = videoUrl.substring(videoUrl
//                            .lastIndexOf("=") + 1);

//                    String url =videoUrl.substring(videoUrl
//                            .lastIndexOf("&index") + 0);

                String[] parts = videoUrl.split("=");
                String part1 = parts[0]; // 004
                String videoId = parts[0]; // 034556


                String[] parts1 = videoId.split("&index");

                String url = parts1[0];


                String[] parts2 = videoId.split("&list");


                String url2 = parts2[0];

                Log.e("video", firstLevelFilter.getFileName());

                try {
//                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( videoUrl));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(videoUrl));
                    startActivity(webIntent);
//                    try {
//                        startActivity(appIntent);
//                    } catch (ActivityNotFoundException ex) {
//                        startActivity(webIntent);
//                    }

                } catch (ActivityNotFoundException e) {

                    // youtube is not installed.Will be opened in other
                    // available apps
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                    videoIntent.setDataAndType(
                            Uri.parse(firstLevelFilter.getFileName()),
                            "video/*");
                    startActivity(videoIntent);
                }

            } else {

                /*Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                videoIntent.setDataAndType(
                        Uri.parse(firstLevelFilter.getFileName()), "video*//*");
                startActivity(videoIntent);*/
                Intent edit = new Intent(VideoActivity.this, ExoVideoActivity.class);
                edit.putExtra("videoUrl", firstLevelFilter.getFileName());
                edit.putExtra("title", firstLevelFilter.getTitle());
                edit.putExtra("page", "videoMain");
                startActivity(edit);
            }

//            String CurrentString =firstLevelFilter.getFileName();
//            String[] separated = CurrentString.split("v=");
//            String videoId = separated[1];
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
//            intent.putExtra("VIDEO_ID", videoId);
//            startActivity(intent);

//            Intent view = new Intent(getApplicationContext(), VideoViewActivity.class);
//            view.putExtra("url", firstLevelFilter.getFileName());
//
//            startActivity(view);
        } else {

            if (videoLists.size() == 0) {

                Toast.makeText(VideoActivity.this, "Folder is Empty", Toast.LENGTH_SHORT).show();

            } else {
                String foldername = firstLevelFilter.getFolderName();

                Intent intent = new Intent(getApplicationContext(), VideoFirstLevelActivity.class);
                intent.putExtra("foldername", foldername);
//                intent.putExtra("videolist", (Serializable) videoLists);
//                intent.putExtra("folderlist", (Serializable) folderLists);

                startActivity(intent);
            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
