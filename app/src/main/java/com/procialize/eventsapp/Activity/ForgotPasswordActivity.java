package com.procialize.eventsapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.procialize.eventsapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        headerlogoIv = findViewById(R.id.headerlogoIv);

        headerlogoIv.setVisibility(View.VISIBLE);
    }
}
