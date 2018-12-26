package com.procialize.singleevent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SessionManager sessionManager =new SessionManager(getApplicationContext());

//                sessionManager.checkLogin();

                if (sessionManager.isLoggedIn()==true) {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }else
                {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    protected void onResume() {
       // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }
}
