package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Utility.Util;


/**
 * Created by HP-PC on 11-08-2016.
 */
public class ExoVideoActivity extends AppCompatActivity implements OnPreparedListener {

    EMVideoView emVideoView;

    String videoUrl = "";
    String title = "";
    String tripId = "",page;
    Button btn_share;
    RelativeLayout llTop;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid,colorActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        videoUrl = getIntent().getExtras().getString("videoUrl");
        title = getIntent().getExtras().getString("title");
        page = getIntent().getExtras().getString("page");

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

        TextView txtIcon = (TextView) findViewById(R.id.txtIcon);
        btn_share = (Button) findViewById(R.id.btn_share);
//        llTop = (RelativeLayout) findViewById(R.id.rlData);
//        headerlogoIv = findViewById(R.id.headerlogoIv);
//        headerlogoIv.setText(title);
//        Util.logomethod(this,headerlogoIv);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);
        txtIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        if(title!=null){
            txtTitle.setText(title);
            txtTitle.setTextColor(Color.parseColor(colorActive));

        }

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl(title, ApiConstant.selfievideo + videoUrl);
            }
        });


        if (page.equalsIgnoreCase("videoMain")) {
            setupVideoView(ApiConstant.folderimage + videoUrl);

        } else {
            setupVideoView(ApiConstant.selfievideo + videoUrl);

        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }


    private void setupVideoView(final String videoUrl) {
        emVideoView = (EMVideoView) findViewById(R.id.video_view);
        emVideoView.setOnPreparedListener(ExoVideoActivity.this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        emVideoView.setVideoURI(Uri.parse(videoUrl));

        pDialog = new ProgressDialog(ExoVideoActivity.this);
        // Set progressbar title
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        // Show progressbar
        pDialog.show();
        emVideoView.requestFocus();

//        emVideoView.setVideoURI(Uri.parse(videoUrl));


        emVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                //finish();
                emVideoView.setVideoURI(Uri.parse(videoUrl));


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

   /* @Override
    public void onPrepared() {
        //Starts the video playback as soon as it is ready
        emVideoView.start();

        llTop.setVisibility(View.GONE);
        emVideoView.setVisibility(View.VISIBLE);


    }*/




    /*private void hide() {

        SlideOutUpAnimator slideOutUpAnimator = new SlideOutUpAnimator();
        slideOutUpAnimator.prepare(rlTop);
        slideOutUpAnimator.setDuration(1500);

        slideOutUpAnimator.animate();

    }

    private void show() {
        rlTop.setVisibility(View.VISIBLE);
        SlideInDownAnimator slideInDownAnimator = new SlideInDownAnimator();
        slideInDownAnimator.prepare(rlTop);
        slideInDownAnimator.setDuration(1500);

        slideInDownAnimator.animate();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, Constants.SPLASH_TIME);


    }*/

    ProgressDialog pDialog;


    @Override
    public void onPrepared() {
        pDialog.dismiss();
        emVideoView.start();

//        llTop.setVisibility(View.GONE);
        emVideoView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        emVideoView.pause();
    }
}

