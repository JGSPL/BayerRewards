package com.procialize.singleevent.InnerDrawerActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Adapter.QAAttendeeAdapter;
import com.procialize.singleevent.Adapter.QASpeakerAdapter;
import com.procialize.singleevent.Adapter.SpeakerAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.AgendaLisQA;
import com.procialize.singleevent.GetterSetter.QASpeakerFetch;
import com.procialize.singleevent.GetterSetter.QuestionSpeakerList;
import com.procialize.singleevent.GetterSetter.SpeakerList;
import com.procialize.singleevent.GetterSetter.SpeakerQuestionList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QASpeakerActivity extends AppCompatActivity implements QASpeakerAdapter.QASpeakerAdapterListner {


    public RecyclerView qaRv;
    public Button postbtn;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    private APIService mAPIService;
    ArrayList<String> list;
    Spinner spinner;
    public static String Selectedspeaker, SelectedspeakerId;
    List<QuestionSpeakerList> agendaLisQAS;
    Boolean change = true;
    public QASpeakerAdapter qaSpeakerAdapter;
    Dialog myDialog;
    String token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaspeaker);

       // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");


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
        Util.logomethod(this,headerlogoIv);


        qaRv = findViewById(R.id.qaRv);
        postbtn = findViewById(R.id.postbtn);
        postbtn.setBackgroundColor(Color.parseColor(colorActive));
        qaRvrefresh = findViewById(R.id.qaRvrefresh);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);

        list = new ArrayList<>();
        agendaLisQAS = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);


        QAFetch(token, eventid);

        // use a linear layout manager
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        qaRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
       // qaRv.setLayoutAnimation(animation);


        qaRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                QAFetch(token, eventid);
            }
        });


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selectedspeaker = parent.getItemAtPosition(position).toString();
                SelectedspeakerId = agendaLisQAS.get(position).getSpeakerId();
                QAFetch(token, eventid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void QAFetch(String token, String eventid) {
        showProgress();
        mAPIService.QASpeakerFetch(token, eventid).enqueue(new Callback<QASpeakerFetch>() {
            @Override
            public void onResponse(Call<QASpeakerFetch> call, Response<QASpeakerFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QASpeakerFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<QASpeakerFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {


            if (response.body().getQuestionSpeakerList().size() != 0 && change == true) {

                change = false;

                agendaLisQAS = response.body().getQuestionSpeakerList();

                for (int i = 0; i < response.body().getQuestionSpeakerList().size(); i++) {
                    list.add(response.body().getQuestionSpeakerList().get(i).getFirstName());
                }

                // Creating adapter for spinner

                ArrayAdapter dataAdapter = new ArrayAdapter(this, R.layout.spinner_item, list);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
            }


            if (SelectedspeakerId != null) {
                ArrayList<SpeakerQuestionList> speakerQuestionLists = new ArrayList<>();
                for (int j = 0; j < response.body().getSpeakerQuestionList().size(); j++) {
                    if (SelectedspeakerId.equalsIgnoreCase(response.body().getSpeakerQuestionList().get(j).getSpeakerId())) {
                        speakerQuestionLists.add(response.body().getSpeakerQuestionList().get(j));
                    }
                }

                if (!(response.body().getQuestionSpeakerList().isEmpty())) {
                    qaSpeakerAdapter = new QASpeakerAdapter(QASpeakerActivity.this, speakerQuestionLists, response.body().getQuestionSpeakerList(), this, Selectedspeaker);
                    qaSpeakerAdapter.notifyDataSetChanged();
                    qaRv.setAdapter(qaSpeakerAdapter);
                    qaRv.scheduleLayoutAnimation();
                } else {
                    setContentView(R.layout.activity_empty_view);
                    ImageView imageView = findViewById(R.id.back);
                    TextView text_empty = findViewById(R.id.text_empty);
                    text_empty.setText("Q&A not available");
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }


        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();

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


    private void showratedialouge() {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));

        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        cancelbtn.setBackgroundColor(Color.parseColor(colorActive));
        cancelbtn.setBackgroundColor(Color.parseColor(colorActive));


        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);

        nametv.setTextColor(Color.parseColor(colorActive));

        title.setText("Post Question");

        nametv.setText("To " + Selectedspeaker);

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    PostQuetion(token, eventid, msg, Selectedspeaker, SelectedspeakerId);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QASpeakerActivity.this);
                    builder.setTitle("");
                    builder.setMessage("Please post a question");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    //
                   // Toast.makeText(getApplicationContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PostQuetion(final String token, final String eventid, String msg, String selectedspeaker, String selectedspeakerId) {
        mAPIService.QASpeakerPost(token, eventid, msg, selectedspeaker, selectedspeakerId).enqueue(new Callback<QASpeakerFetch>() {
            @Override
            public void onResponse(Call<QASpeakerFetch> call, Response<QASpeakerFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    if (myDialog != null) {
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                    }
                    QAFetch(token, eventid);

                } else {

                    Toast.makeText(QASpeakerActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QASpeakerFetch> call, Throwable t) {
                Toast.makeText(QASpeakerActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onContactSelected(SpeakerQuestionList question) {

    }

    @Override
    public void onLikeListener(View v, SpeakerQuestionList question, int position, TextView countTv, ImageView likeIv) {

        if (question.getLikeFlag().equals("1")) {


            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            try {

                int count = Integer.parseInt(question.getTotalLikes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + " Likes");

                } else {
                    countTv.setText("0" + " Likes");
                }

//            QAFetch(token,eventid);
                QALike(token, eventid, question.getId(), question.getSpeakerId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_afterlike));

            QALike(token, eventid, question.getId(), question.getSpeakerId());


            try {

                int count = Integer.parseInt(question.getTotalLikes());


                count = count + 1;
                countTv.setText(count + " Likes");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void QALike(String token, String eventid, String questionid, String sessionid) {
        mAPIService.QASpeakerLike(token, eventid, questionid, sessionid).enqueue(new Callback<QASpeakerFetch>() {
            @Override
            public void onResponse(Call<QASpeakerFetch> call, Response<QASpeakerFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

                    Toast.makeText(QASpeakerActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QASpeakerFetch> call, Throwable t) {
                Toast.makeText(QASpeakerActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showLikeResponse(Response<QASpeakerFetch> response) {

        if (response.body().getMsg().equalsIgnoreCase("success")) {
//            Toast.makeText(QASpeakerActivity.this,response.message(),Toast.LENGTH_SHORT).show();
            ArrayList<SpeakerQuestionList> speakerQuestionLists = new ArrayList<>();
            for (int j = 0; j < response.body().getSpeakerQuestionList().size(); j++) {
                if (SelectedspeakerId.equalsIgnoreCase(response.body().getSpeakerQuestionList().get(j).getSpeakerId())) {
                    speakerQuestionLists.add(response.body().getSpeakerQuestionList().get(j));
                }
            }

            qaSpeakerAdapter = new QASpeakerAdapter(QASpeakerActivity.this, speakerQuestionLists, response.body().getQuestionSpeakerList(), this, Selectedspeaker);
            qaSpeakerAdapter.notifyDataSetChanged();
            qaRv.setAdapter(qaSpeakerAdapter);
        } else {
            Toast.makeText(QASpeakerActivity.this, response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
