package com.bayer.bayerreward.InnerDrawerActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bayer.bayerreward.Fragments.AgendaFolderFragment;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;


public class AgendaVacationActivity extends AppCompatActivity {
    LinearLayout linear_header;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_vacation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        linear_header = findViewById(R.id.linear_header);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Fragment fragment = new AgendaFolderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.myFrame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        CommonFunction.crashlytics("AgendaVacationActivity","");
        firbaseAnalytics(this,"AgendaVacationActivity","");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        linear_header.setBackgroundResource(R.color.transp);
        linear_header.setAlpha(00);
    }
}