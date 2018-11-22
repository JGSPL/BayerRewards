package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Activity.PdfViewerActivity;
import com.procialize.singleevent.Adapter.DocumentsAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.EmptyViewActivity;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.DocumentList;
import com.procialize.singleevent.GetterSetter.DocumentsListFetch;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentsActivity extends AppCompatActivity implements DocumentsAdapter.DocumentsAdapterListner {


    private APIService mAPIService;
    SwipeRefreshLayout docRvrefresh;
    RecyclerView docRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg;
    ImageView headerlogoIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg","");


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
       // Util.logomethod(this,headerlogoIv);
        Glide.with(this).load("http://www.procialize.info/uploads/app_logo/" + logoImg).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                headerlogoIv.setImageResource(R.drawable.splashlogo);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(headerlogoIv);

        docRv = findViewById(R.id.docRv);
        docRvrefresh = findViewById(R.id.docRvrefresh);
        progressBar = findViewById(R.id.progressBar);


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
                final ImageView headerlogoIv1 = findViewById(R.id.headerlogoIv);
                Util.logomethod(this,headerlogoIv1);

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
        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + "https://www.procialize.info/uploads/documents/" + document.getFileName());
        startActivity(pdfview);
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }


}
