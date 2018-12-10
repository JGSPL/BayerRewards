package com.procialize.vivo_app.InnerDrawerActivity;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.procialize.vivo_app.Fragments.AgendaFolderFragment;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Utility.Util;


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
        Util.logomethod(this,headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Fragment fragment = new AgendaFolderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.myFrame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        linear_header.setBackgroundResource(R.color.transp);
        linear_header.setAlpha(00);
    }
}