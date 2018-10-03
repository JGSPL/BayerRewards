package com.procialize.singleevent.InnerDrawerActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Activity.VideoViewActivity;
import com.procialize.singleevent.Adapter.VideoFirstLevelAdapter;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.GetterSetter.FirstLevelFilter;
import com.procialize.singleevent.GetterSetter.FolderList;
import com.procialize.singleevent.GetterSetter.GalleryList;
import com.procialize.singleevent.GetterSetter.VideoFolderList;
import com.procialize.singleevent.GetterSetter.VideoList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Utility.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class VideoFirstLevelActivity extends AppCompatActivity implements VideoFirstLevelAdapter.VideoFirstLevelAdapterListner {


    RecyclerView videoRv;
    TextView tvname;
    public static String foldername;
    List<VideoList> videoLists;
    List<VideoFolderList> folderLists;
    List<FirstLevelFilter> filtergallerylists;
    String foldernamenew;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_first_level);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        videoLists = new ArrayList<>();
        folderLists = new ArrayList<>();
        filtergallerylists = new ArrayList<>();

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

        foldername = getIntent().getExtras().getString("foldername");
        videoLists = (List<VideoList>) getIntent().getExtras().getSerializable("videolist");
        folderLists = (List<VideoFolderList>) getIntent().getExtras().getSerializable("folderlist");


        videoRv = findViewById(R.id.videoRv);
        tvname = findViewById(R.id.tvname);


        // use a linear layout manager
        int columns = 2;
        videoRv.setLayoutManager(new GridLayoutManager(this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        videoRv.setLayoutAnimation(animation);


        if (foldername.contains("/")) {
            String[] parts = foldername.split("/");
            String part1 = parts[0];
            String part2 = parts[1];
            tvname.setText(part2);
        } else {
            tvname.setText(foldername);
        }


        if (folderLists.size() != 0 || videoLists.size() != 0) {
            if (folderLists.size() != 0) {
                for (int i = 0; i < folderLists.size(); i++) {
                    if (folderLists.get(i).getFolderName() != null) {
                        if (folderLists.get(i).getFolderName().contains(foldername + "/")) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            firstLevelFilter.setTitle(folderLists.get(i).getFolderName());
                            firstLevelFilter.setFolderName(folderLists.get(i).getFolderName());
                            firstLevelFilter.setFileName(ApiConstant.folderimage + folderLists.get(i).getFolderImage());
                            filtergallerylists.add(firstLevelFilter);
                        }
                    }
                }
            }

            if (videoLists.size() != 0) {

                for (int i = 0; i < videoLists.size(); i++) {
                    if (videoLists.get(i).getFolderName() != null) {
                        if (videoLists.get(i).getFolderName().equals(foldername)) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            firstLevelFilter.setTitle(videoLists.get(i).getTitle());
                            firstLevelFilter.setFolderName(videoLists.get(i).getFolderName());
                            firstLevelFilter.setFileName(videoLists.get(i).getVideoUrl());
                            filtergallerylists.add(firstLevelFilter);
                        }
                    }
                }
            }

            VideoFirstLevelAdapter galleryAdapter = new VideoFirstLevelAdapter(this, filtergallerylists, this);
            galleryAdapter.notifyDataSetChanged();
            videoRv.setAdapter(galleryAdapter);
            videoRv.scheduleLayoutAnimation();

        } else {
            Toast.makeText(getApplicationContext(), "No Video Found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onContactSelected(FirstLevelFilter firstLevelFilter) {
//        foldernamenew =
        if (firstLevelFilter.getFolderName() == null || firstLevelFilter.getFolderName().equalsIgnoreCase("null")
                || firstLevelFilter.getFolderName().equalsIgnoreCase(foldername)) {

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

                if (foldername.contains("/")) {
                    Intent intent = new Intent(getApplicationContext(), VideoFirstLevelActivity.class);
                    intent.putExtra("foldername", firstLevelFilter.getFolderName());
                    intent.putExtra("videolist", (Serializable) videoLists);
                    intent.putExtra("folderlist", (Serializable) folderLists);
                    startActivity(intent);

                } else {
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                    videoIntent.setDataAndType(
                            Uri.parse(ApiConstant.folderimage + firstLevelFilter.getFileName()), "video/*");
                    startActivity(videoIntent);
                }
            }

//            Intent view = new Intent(getApplicationContext(), VideoViewActivity.class);
//            view.putExtra("url", firstLevelFilter.getFileName());
//
//            startActivity(view);
        } else {

            if (firstLevelFilter.getFileName().contains("youtu")) {
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


                Intent intent = new Intent(getApplicationContext(), VideoFirstLevelActivity.class);
                intent.putExtra("foldername", firstLevelFilter.getFileName());
                intent.putExtra("videolist", (Serializable) videoLists);
                intent.putExtra("folderlist", (Serializable) folderLists);
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
