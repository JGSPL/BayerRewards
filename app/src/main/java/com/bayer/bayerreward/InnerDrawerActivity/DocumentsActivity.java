package com.bayer.bayerreward.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.PdfViewerActivity;
import com.bayer.bayerreward.Adapter.DocumentsAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.DocumentList;
import com.bayer.bayerreward.GetterSetter.DocumentsListFetch;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import java.io.File;
import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class DocumentsActivity extends AppCompatActivity implements DocumentsAdapter.DocumentsAdapterListner {


    SwipeRefreshLayout docRvrefresh;
    RecyclerView docRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");


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
        Util.logomethod(this, headerlogoIv);

        docRv = findViewById(R.id.docRv);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        docRvrefresh = findViewById(R.id.docRvrefresh);
        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        docRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //   docRv.setLayoutAnimation(animation);


        fetchDocuments(token, eventid);

        docRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDocuments(token, eventid);
            }
        });

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }


        CommonFunction.crashlytics("DocumentActivity","");
        firbaseAnalytics(this,"DocumentActivity","");

    }


    public void fetchDocuments(String token, String eventid) {
        showProgress();
        mAPIService.DocumentsListFetch(token, eventid).enqueue(new Callback<DocumentsListFetch>() {
            @Override
            public void onResponse(Call<DocumentsListFetch> call, Response<DocumentsListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (docRvrefresh.isRefreshing()) {
                        docRvrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (docRvrefresh.isRefreshing()) {
                        docRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DocumentsListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (docRvrefresh.isRefreshing()) {
                    docRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<DocumentsListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getDocumentList().isEmpty())) {
                DocumentsAdapter docAdapter = new DocumentsAdapter(DocumentsActivity.this, response.body().getDocumentList(), this);
                docAdapter.notifyDataSetChanged();
                docRv.setAdapter(docAdapter);
                docRv.scheduleLayoutAnimation();
                docRv.setVisibility(View.VISIBLE);

            } else {

                setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                ImageView headerlogoIv = findViewById(R.id.headerlogoIv);
                Util.logomethod(this, headerlogoIv);

                text_empty.setText("Document not available");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


//                Intent intent = new Intent(DocumentsActivity.this, EmptyViewActivity.class);
//                startActivity(intent);
//                finish();

            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onContactSelected(DocumentList document) {


        Intent pdfview = new Intent(this, PdfViewerActivity.class);
        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + ApiConstant.imgURL + "uploads/documents/" + document.getFileName());
        pdfview.putExtra("url1", ApiConstant.imgURL + "uploads/documents/" + document.getFileName());
        startActivity(pdfview);
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }


}
