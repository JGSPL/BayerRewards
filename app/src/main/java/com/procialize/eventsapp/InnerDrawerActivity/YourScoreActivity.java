package com.procialize.eventsapp.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.procialize.eventsapp.R;

public class YourScoreActivity extends AppCompatActivity {
    TextView txt_count, questionTv;
    Button btn_ok;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        txt_count = findViewById(R.id.txt_count);
        questionTv = findViewById(R.id.questionTv);
        btn_ok = findViewById(R.id.btn_ok);

        btn_ok.setBackgroundColor(Color.parseColor(colorActive));
        txt_count.setTextColor(Color.parseColor(colorActive));

//        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
//        txt_count.setTypeface(typeface);

        final Intent intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        int correnctcount = intent.getIntExtra("Answers", 0);
        int totalcount = intent.getIntExtra("TotalQue", 0);

        questionTv.setText(folderName);
        txt_count.setText(correnctcount + "/" + totalcount);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, FolderQuizActivity.class);
                startActivity(intent1);
                finish();
            }
        });


    }
}
