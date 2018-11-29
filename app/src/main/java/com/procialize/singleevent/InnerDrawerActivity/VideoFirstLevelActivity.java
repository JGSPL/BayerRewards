package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
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

import com.procialize.singleevent.Adapter.VideoFirstLevelAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFirstLevelActivity extends AppCompatActivity implements VideoFirstLevelAdapter.VideoFirstLevelAdapterListner {


    RecyclerView videoRv;
    TextView tvname;
    public static String foldername;
    List<VideoList> videoLists;
    List<VideoFolderList> folderLists;
    List<FirstLevelFilter> filtergallerylists;
    String foldernamenew;
    ImageView headerlogoIv;
    String eventid,colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    private APIService mAPIService;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_first_level);

      //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");

        mAPIService = ApiUtils.getAPIService();
        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        videoLists = new ArrayList<>();
        folderLists = new ArrayList<>();
        filtergallerylists = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this,R.style.MyAlertDialogStyle);


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
//        videoLists = (List<VideoList>) getIntent().getExtras().getSerializable("videolist");
//        folderLists = (List<VideoFolderList>) getIntent().getExtras().getSerializable("folderlist");


        videoRv = findViewById(R.id.videoRv);
        tvname = findViewById(R.id.tvname);
        tvname.setTextColor(Color.parseColor(colorActive));


        // use a linear layout manager

        progressDialog.setTitle("Fetching Vides");
        progressDialog.setMessage("Processing.....");
        progressDialog.show();

        fetchVideo(token, eventid);


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
//                    intent.putExtra("videolist", (Serializable) videoLists);
//                    intent.putExtra("folderlist", (Serializable) folderLists);
                    startActivity(intent);

                } else {
                   /* Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                    videoIntent.setDataAndType(
                            Uri.parse(ApiConstant.folderimage + firstLevelFilter.getFileName()), "video*//*");
                    startActivity(videoIntent);*/
                    Intent edit = new Intent(VideoFirstLevelActivity.this, ExoVideoActivity.class);
                    edit.putExtra("videoUrl", firstLevelFilter.getFileName());
                    edit.putExtra("title", "videoMain");
                    startActivity(edit);
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

                if (videoLists.size() == 0) {

                    Toast.makeText(VideoFirstLevelActivity.this, "Folder is Empty", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(getApplicationContext(), VideoFirstLevelActivity.class);
                    intent.putExtra("foldername", firstLevelFilter.getFileName());
//                    intent.putExtra("videolist", (Serializable) videoLists);
//                    intent.putExtra("folderlist", (Serializable) folderLists);
                    startActivity(intent);
                }
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }


    public void fetchVideo(String token, String eventid) {

        mAPIService.VideoFetchListFetch(token, eventid).enqueue(new Callback<VideoFetchListFetch>() {
            @Override
            public void onResponse(Call<VideoFetchListFetch> call, Response<VideoFetchListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                } else {


                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoFetchListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }





        public void showResponse (Response < VideoFetchListFetch > response) {


            // specify an adapter (see also next example)
            if (response.body().getVideoList().size() != 0) {

                videoLists = response.body().getVideoList();
                folderLists = response.body().getVideoFolderList();


                Object[] params = {this,videoLists,folderLists,filtergallerylists};
                new TestAsync().execute(params);



                } else {
                    Toast.makeText(getApplicationContext(), "No Video Available", Toast.LENGTH_SHORT).show();

                }
        }




    class TestAsync extends AsyncTask<Object, Void, List<FirstLevelFilter>>
    {
        String TAG = getClass().getSimpleName();
        Context context;

        protected void onPreExecute (){
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected List<FirstLevelFilter> doInBackground(Object... params) {

            context = (Context) params[0];

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



            } else {
                Toast.makeText(getApplicationContext(), "No Video Found", Toast.LENGTH_SHORT).show();
            }
            return filtergallerylists;
        }


        protected void onPostExecute(List<FirstLevelFilter> filtergallerylistsdemp) {


            filtergallerylists = filtergallerylistsdemp;
            setrv(filtergallerylistsdemp);

            super.onPostExecute(filtergallerylists);
        }
    }

    public void setrv(List<FirstLevelFilter> filtergallerylist)
    {
        int columns = 2;
        videoRv.setLayoutManager(new GridLayoutManager(VideoFirstLevelActivity.this, columns));

//        int resId = R.anim.layout_animation_slide_right;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VideoFirstLevelActivity.this, resId);
//        videoRv.setLayoutAnimation(animation);


        VideoFirstLevelAdapter galleryAdapter = new VideoFirstLevelAdapter(VideoFirstLevelActivity.this, filtergallerylist,this);
        videoRv.setAdapter(galleryAdapter);
        galleryAdapter.notifyDataSetChanged();
//        videoRv.scheduleLayoutAnimation();
        if (progressDialog != null)
            progressDialog.cancel();


    }

}
