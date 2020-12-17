package com.bayer.bayerreward.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.regex.Pattern;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class YourScoreActivity extends AppCompatActivity {
    TextView txt_count, questionTv, txt_title;
    Button btn_ok;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    LinearLayout linear;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.activetab), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(YourScoreActivity.this, FolderQuizActivity.class);
                startActivity(intent);
                finish();


            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        txt_count = findViewById(R.id.txt_count);
        questionTv = findViewById(R.id.questionTv);
        btn_ok = findViewById(R.id.btn_ok);
        linear = findViewById(R.id.linear);
        txt_title = findViewById(R.id.txt_title);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        btn_ok.setBackgroundColor(Color.parseColor(colorActive));
        txt_count.setTextColor(Color.parseColor(colorActive));

//        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
//        txt_count.setTypeface(typeface);
//        try {
////            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
//            //path to /data/data/yourapp/app_data/dirName
////            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
//            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
//            Resources res = getResources();
//            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
//            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
//            linear.setBackgroundDrawable(bd);
//
//            Log.e("PATH", String.valueOf(mypath));
//        } catch (Exception e) {
//            e.printStackTrace();
//            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
//        }
        final Intent intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        String correnctcount = intent.getStringExtra("Answers");
        String totalcount = intent.getStringExtra("TotalQue");

        questionTv.setText(decodeUnicode(folderName));
        txt_count.setText(correnctcount + "/" + totalcount);
        questionTv.setBackgroundColor(Color.parseColor(colorActive));
        txt_title.setTextColor(Color.parseColor(colorActive));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, FolderQuizActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        CommonFunction.crashlytics("YourScore","");
        firbaseAnalytics(this,"YourScore","");

    }

    private static String decodeUnicode(String theString) {
        final String UNICODE_REGEX = "\\\\u([0-9a-f]{4})";
        final Pattern UNICODE_PATTERN = Pattern.compile(UNICODE_REGEX);
        String message = theString;
        Matcher matcher = UNICODE_PATTERN.matcher(message);
        StringBuffer decodedMessage = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(
                    decodedMessage, String.valueOf((char) Integer.parseInt(matcher.group(1), 16)));
        }
        matcher.appendTail(decodedMessage);
        System.out.println(decodedMessage.toString());
        return decodedMessage.toString();
    }
}
