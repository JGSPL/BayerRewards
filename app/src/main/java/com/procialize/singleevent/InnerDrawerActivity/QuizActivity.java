package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.Toast;
import com.procialize.singleevent.Activity.QuizDetailActivity;
import com.procialize.singleevent.Adapter.QuizAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.QuizFetch;
import com.procialize.singleevent.GetterSetter.QuizList;
import com.procialize.singleevent.GetterSetter.QuizOptionList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements QuizAdapter.QuizAdapterListner{

    private APIService mAPIService;
    SwipeRefreshLayout quizrefresh;
    RecyclerView quizRv;
    ProgressBar progressBar;
    List<QuizOptionList> quizOptionLists;

    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


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


        quizRv=findViewById(R.id.quizRv);
        progressBar=findViewById(R.id.progressBar);
        quizrefresh=findViewById(R.id.quizrefresh);

        quizOptionLists = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);



        // use a linear layout manager
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        quizRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        quizRv.setLayoutAnimation(animation);


        fetchQuiz(token,eventid);

        quizrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchQuiz(token,eventid);
            }
        });
    }


    public void fetchQuiz(String token, String eventid) {
        showProgress();
        mAPIService.QuizFetch(token,eventid).enqueue(new Callback<QuizFetch>() {
            @Override
            public void onResponse(Call<QuizFetch> call, Response<QuizFetch> response) {

                if(response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (quizrefresh.isRefreshing()) {
                        quizrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                }else
                {
                    if (quizrefresh.isRefreshing()) {
                        quizrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Unable to process",Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (quizrefresh.isRefreshing()) {
                    quizrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<QuizFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getQuizList().size()!=0) {
            QuizAdapter quizAdapter = new QuizAdapter(this, response.body().getQuizList(),response.body().getQuizOptionList(),this);
            quizAdapter.notifyDataSetChanged();
            quizRv.setAdapter(quizAdapter);
            quizRv.scheduleLayoutAnimation();

            quizOptionLists = response.body().getQuizOptionList();
        }else
        {
            Toast.makeText(getApplicationContext(),"No Poll Available",Toast.LENGTH_SHORT).show();

        }
    }

    public void showProgress()
    {
      if (progressBar.getVisibility()== View.GONE)
      {
          progressBar.setVisibility(View.VISIBLE);
      }
    }

    public void dismissProgress()
    {
        if (progressBar.getVisibility()== View.VISIBLE)
        {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        fetchQuiz(token,"1");
    }


    @Override
    public void onContactSelected(QuizList quizList) {

        if (quizList.getReplied().equalsIgnoreCase("0")) {
            Intent quizdetail = new Intent(getApplicationContext(), QuizDetailActivity.class);
            quizdetail.putExtra("id", quizList.getId());
            quizdetail.putExtra("question", quizList.getQuestion());
            quizdetail.putExtra("replied", quizList.getReplied());
            quizdetail.putExtra("optionlist", (Serializable) quizOptionLists);
            startActivity(quizdetail);
        }else
        {
            Toast.makeText(this,"You Already Submited This Quiz",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
