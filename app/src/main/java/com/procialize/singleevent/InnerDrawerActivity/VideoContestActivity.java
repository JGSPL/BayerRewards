package com.procialize.singleevent.InnerDrawerActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Adapter.VideoContestAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.ReportVideoContest;
import com.procialize.singleevent.GetterSetter.ReportVideoContestHide;
import com.procialize.singleevent.GetterSetter.VideoContest;
import com.procialize.singleevent.GetterSetter.VideoContestLikes;
import com.procialize.singleevent.GetterSetter.VideoContestListFetch;

import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoContestActivity extends AppCompatActivity implements VideoContestAdapter.VideoContestAdapterListner {


    Button uploadbtn;
    SwipeRefreshLayout videofeedrefresh;
    ProgressBar progressBar;
    private APIService mAPIService;
    RecyclerView videorecycler;
    String token;
    VideoContestAdapter videoAdapter;

    BottomSheetDialog dialog;
    Dialog myDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    String user_id,colorActive;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_contest);

       // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
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
        Util.logomethod(this,headerlogoIv);

        uploadbtn = findViewById(R.id.uploadbtn);
        videofeedrefresh = findViewById(R.id.videofeedrefresh);
        videorecycler = findViewById(R.id.videorecycler);
        TextView header = (TextView)findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));


        int columns = 2;
        videorecycler.setLayoutManager(new GridLayoutManager(this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        videorecycler.setLayoutAnimation(animation);


        progressBar = findViewById(R.id.progressBar);

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();


        user_id = user.get(SessionManager.KEY_ID);
        // token
        // token
        token = user.get(SessionManager.KEY_TOKEN);
        uploadbtn.setBackgroundColor(Color.parseColor(colorActive));

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videoupload = new Intent(VideoContestActivity.this, VideoContestUploadActivity.class);
                startActivity(videoupload);
                finish();
            }
        });

        videofeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SelfieListFetch(token, eventid);
            }
        });

        SelfieListFetch(token, eventid);


    }

    public void SelfieListFetch(String token, String eventid) {
        showProgress();
        mAPIService.VideoContestListFetch(token, eventid).enqueue(new Callback<VideoContestListFetch>() {
            @Override
            public void onResponse(Call<VideoContestListFetch> call, Response<VideoContestListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (videofeedrefresh.isRefreshing()) {
                        videofeedrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (videofeedrefresh.isRefreshing()) {
                        videofeedrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoContestListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (videofeedrefresh.isRefreshing()) {
                    videofeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<VideoContestListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {


            videoAdapter = new VideoContestAdapter(this, response.body().getVideoContest(), this);
            videoAdapter.notifyDataSetChanged();
            videorecycler.setAdapter(videoAdapter);
            videorecycler.scheduleLayoutAnimation();

        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();

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
    public void onContactSelected(VideoContest videoContest) {

        Intent intent = new Intent(VideoContestActivity.this, ExoVideoActivity.class);
        intent.putExtra("videoUrl", videoContest.getFileName());
        intent.putExtra("title", videoContest.getTitle());
        intent.putExtra("page", "contest");
        startActivity(intent);
    }

    @Override
    public void onLikeListener(View v, VideoContest videoContest, int position, TextView countTv, ImageView likeIv) {


        if (videoContest.getLikeFlag().equals("1")) {
            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            VideoContestLike(token, eventid, videoContest.getId());

            try {

                int count = Integer.parseInt(videoContest.getTotalLikes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + "");

                } else {
                    countTv.setText("0");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_afterlike));

            VideoContestLike(token, eventid, videoContest.getId());


            try {

                int count = Integer.parseInt(videoContest.getTotalLikes());


                count = count + 1;
                countTv.setText(count + "");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMoreListner(View v, final VideoContest videoContest, final int position) {
        dialog = new BottomSheetDialog(this);

        dialog.setContentView(R.layout.botomcontestdialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);

        deleteTv.setVisibility(View.GONE);

        reportTv.setText("Report Video");
        hideTv.setText("Hide Video");
        deleteTv.setText("Detete this Video");

        if (videoContest.getAttendeeId().equalsIgnoreCase(user_id)) {
            deleteTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.GONE);
            reportTv.setVisibility(View.GONE);
        } else {
            deleteTv.setVisibility(View.GONE);
            hideTv.setVisibility(View.VISIBLE);
            reportTv.setVisibility(View.VISIBLE);
        }
        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportVideoContestHide(eventid, videoContest.getId(), token, position);
            }
        });

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteVideo(eventid, videoContest.getId(), token, position);
            }
        });


        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge(videoContest.getId(),videoContest.getFirstName());
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    @Override
    public void onShareListner(View v, VideoContest videoContest, int position) {
        shareTextUrl(videoContest.getTitle(), ApiConstant.selfievideo + videoContest.getFileName());
    }


    public void VideoContestLike(String token, String eventid, String id) {
        mAPIService.VideoContestLikes(token, eventid, id).enqueue(new Callback<VideoContestLikes>() {
            @Override
            public void onResponse(Call<VideoContestLikes> call, Response<VideoContestLikes> response) {

                if (response.body().getStatus().equalsIgnoreCase("Success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

//                    Toast.makeText(VideoContestActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<VideoContestLikes> call, Throwable t) {
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showLikeResponse(Response<VideoContestLikes> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {
            SelfieListFetch(token, eventid);
//            Toast.makeText(VideoContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(VideoContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
      //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        SelfieListFetch(token, eventid);

        super.onResume();
    }

    public void ReportVideoContestHide(String eventid, String selfieid, String token, final int position) {
//        showProgress();
        mAPIService.ReportVideoContestHide(token, eventid, selfieid).enqueue(new Callback<ReportVideoContestHide>() {
            @Override
            public void onResponse(Call<ReportVideoContestHide> call, Response<ReportVideoContestHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(VideoContestActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportVideoContestHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    public void DeleteVideo(String eventid, String selfieid, String token, final int position) {
//        showProgress();
        mAPIService.ReportVideoContestHide(token, eventid, selfieid).enqueue(new Callback<ReportVideoContestHide>() {
            @Override
            public void onResponse(Call<ReportVideoContestHide> call, Response<ReportVideoContestHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(VideoContestActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportVideoContestHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostHideresponse(Response<ReportVideoContestHide> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            videoAdapter.videoContestList.remove(position);
            videoAdapter.notifyItemRemoved(position);
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(VideoContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    public void ReportVideoContest(String eventid, String feedid, String token, String text) {
//        showProgress();
        mAPIService.ReportVideoContest(token, eventid, feedid, text).enqueue(new Callback<ReportVideoContest>() {
            @Override
            public void onResponse(Call<ReportVideoContest> call, Response<ReportVideoContest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(VideoContestActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportVideoContest> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostresponse(Response<ReportVideoContest> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(VideoContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(VideoContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
        }
    }

    private void showratedialouge(final String id, String name) {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);
        ratebtn.setText("Send");

        ratebtn.setBackgroundColor(Color.parseColor(colorActive));

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        final ImageView imgCancel= myDialog.findViewById(R.id.imgCancel);

        title.setText("Report Video");

        nametv.setText("To " + name);
        nametv.setTextColor(Color.parseColor(colorActive));

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });




        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.dismiss();
                    ReportVideoContest(eventid, id, token, msg);
                } else {

                    Toast.makeText(VideoContestActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void shareTextUrl(String data, String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, data);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link!"));
    }


    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

}
