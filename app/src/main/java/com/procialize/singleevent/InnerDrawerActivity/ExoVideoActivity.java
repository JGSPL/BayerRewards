package com.procialize.singleevent.InnerDrawerActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.R;


/**
 * Created by HP-PC on 11-08-2016.
 */
public class ExoVideoActivity extends Activity  implements OnPreparedListener {

    EMVideoView emVideoView;

    String videoUrl = "";
    String tripId = "";


    RelativeLayout llTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        videoUrl = getIntent().getExtras().getString("videoUrl");


        TextView txtIcon = (TextView) findViewById(R.id.txtIcon);
        llTop = (RelativeLayout) findViewById(R.id.rlData);


        txtIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        setupVideoView(ApiConstant.selfievideo+videoUrl);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }


    private void setupVideoView(String videoUrl) {
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


        emVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                //finish();

            }
        });





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

        llTop.setVisibility(View.GONE);
        emVideoView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        emVideoView.pause();
    }
}

