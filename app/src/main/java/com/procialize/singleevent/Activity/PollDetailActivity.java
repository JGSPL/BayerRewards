package com.procialize.singleevent.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Adapter.PollAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.LivePollFetch;
import com.procialize.singleevent.GetterSetter.LivePollOptionList;
import com.procialize.singleevent.GetterSetter.LivePollSubmitFetch;
import com.procialize.singleevent.InnerDrawerActivity.LivePollActivity;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String questionId, question, replyFlag, quiz_options_id;
    List<LivePollOptionList> optionLists;
    List<LivePollOptionList> AlloptionLists;
    TextView questionTv;
    RadioGroup ll;
    Button subBtn;
    private APIService mAPIService;
    ProgressDialog progress;
    String selected;
    int Count;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ViewGroup viewGroup;
    List<RadioButton> radios;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);

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
                Intent intent = new Intent(PollDetailActivity.this, LivePollActivity.class);
                startActivity(intent);
                finish();
            }
        });


        optionLists = new ArrayList<>();
        mAPIService = ApiUtils.getAPIService();

        questionTv = findViewById(R.id.questionTv);
        subBtn = findViewById(R.id.subBtn);
        subBtn.setOnClickListener(this);

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        radios = new ArrayList<RadioButton>();
        try {
            questionId = getIntent().getExtras().getString("id");
            question = getIntent().getExtras().getString("question");
            replyFlag = getIntent().getExtras().getString("replied");
            AlloptionLists = (List<LivePollOptionList>) getIntent().getSerializableExtra("optionlist");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (replyFlag != null) {
            if (replyFlag.equalsIgnoreCase("1")) {
                subBtn.setVisibility(View.GONE);
            } else {
                subBtn.setVisibility(View.VISIBLE);
            }
        }

        questionTv.setText(question);

        if (AlloptionLists.size() != 0) {
            for (int i = 0; i < AlloptionLists.size(); i++) {

                if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                    Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                    optionLists.add(AlloptionLists.get(i));
                }
            }
        }

        if (optionLists.size() != 0) {

            viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
            // viewGroup.setOnCheckedChangeListener(this);

            addRadioButtons(optionLists.size() + 1);

            for (int i = 0; i < optionLists.size(); i++) {

                if (optionLists
                        .get(0)
                        .getOption()
                        .equalsIgnoreCase(
                                optionLists.get(i).getOption())) {

                    quiz_options_id = optionLists.get(i)
                            .getOptionId();

                }

            }
        }


    }


    public void LivePollSubmitFetch(String token, String eventid, String pollid, String polloptionid) {
        showProgress();
        mAPIService.LivePollSubmitFetch(token, eventid, pollid, polloptionid).enqueue(new Callback<LivePollSubmitFetch>() {
            @Override
            public void onResponse(Call<LivePollSubmitFetch> call, Response<LivePollSubmitFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponse(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollSubmitFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
            }
        });
    }

    public void showResponse(Response<LivePollSubmitFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            Toast.makeText(PollDetailActivity.this,
                    "Answer posted successfully", Toast.LENGTH_SHORT)
                    .show();
            optionLists.clear();
            if (AlloptionLists.size() != 0) {
                for (int i = 0; i < AlloptionLists.size(); i++) {

                    if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                        Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                        optionLists.add(AlloptionLists.get(i));
                    }
                }

                replyFlag = "1";
                subBtn.setVisibility(View.GONE);
                if (optionLists.size() != 0) {

                    viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
                    // viewGroup.setOnCheckedChangeListener(this);

                    addRadioButtons(optionLists.size() + 1);

                    for (int i = 0; i < optionLists.size(); i++) {

                        if (optionLists
                                .get(0)
                                .getOption()
                                .equalsIgnoreCase(
                                        optionLists.get(i).getOption())) {

                            quiz_options_id = optionLists.get(i)
                                    .getOptionId();

                        }

                    }
                }


            }

        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();

        }
    }

    public void showProgress() {
        progress = new ProgressDialog(this);
        progress.setMessage("Loading....");
//        progress.setTitle("Progress");
        progress.show();
    }

    public void dismissProgress() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }


    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();

        /*
         * String[] color = { "#2196F3", "#00BCD4", "#FF5722", "#8BC34A",
         * "#FF9800", "#1B5E20" };
         */

        String[] color = {"#000000", "#6b6b6b", "#76c8e2", "#057189", "#0573a5", "#000000", "#6b6b6b", "#76c8e2", "#057189", "#0573a5"};

        Float totalUser = 0.0f;

//        ArrayList<LivePollOptionList> tempOptionListnew;
//
//        if (replyFlag.equalsIgnoreCase("result")) {
//
//            if (optionLists.size() > 0) {
//                tempOptionListnew = quizOptionListFinal;
//            } else {
//                tempOptionListnew = quizSpecificOptionListnew;
//            }
//
//        } else {
//
//            tempOptionListnew = quizSpecificOptionListnew;
//
//        }

        for (int k = 0; k < optionLists.size(); k++) {

            if (optionLists.get(k).getLivePollId()
                    .equalsIgnoreCase(questionId)) {
                totalUser = (totalUser + Float.parseFloat(optionLists
                        .get(k).getTotalUser()));

            }

        }

        // quiz_layout.setBackgroundColor(Color.parseColor("#1B5E20"));

        for (int row = 0; row < 1; row++) {

            for (int i = 1; i < number; i++) {

                LinearLayout ll = new LinearLayout(this);

                LinearLayout l3 = new LinearLayout(this);
                FrameLayout fl = new FrameLayout(this);

                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(10, 10, 10, 10);

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(10, 10, 10, 10);

                LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId((row * 2) + i);
                rdbtn.setText(optionLists.get(i - 1).getOption());
                rdbtn.setTextColor(Color.BLACK);
//                rdbtn.setTypeface(typeFace);
                rdbtn.setOnClickListener(this);

                if (replyFlag.equalsIgnoreCase("1")) {

                    rdbtn.setClickable(false);
                    rdbtn.setChecked(false);

                } else {

                    if (i == 1)
                        rdbtn.setChecked(true);
                }

                radios.add(rdbtn);

                // rdbtn.setBackgroundResource(R.drawable.edit_background);

                rprms = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rprms.setMargins(5, 5, 5, 5);

                Float weight = 0.0f;

                if (replyFlag.equalsIgnoreCase("1")) {

                    // if (quizSpecificOptionListnew.get(i -
                    // 1).getLive_poll_id()
                    // .equalsIgnoreCase(questionId)) {
                    ll2.setBackgroundColor(Color.parseColor(color[i]));

                    weight = ((Float.parseFloat(optionLists.get(i - 1)
                            .getTotalUser()) / totalUser) * 100);

                    // }

                } else {

                    weight = 100.0f;
                }

                rpms2 = new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, weight);
                rpms2.setMargins(5, 5, 5, 5);

                ll.setBackgroundResource(R.drawable.edit_background);
                ll.setWeightSum(100);
                ll.setLayoutParams(rprms);

                l3.setLayoutParams(rprms);
                l3.setWeightSum(100);

                // ll2.setBackgroundColor(Color.parseColor(color[i]));
                ll2.setLayoutParams(rpms2);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                params.gravity = Gravity.CENTER;

                rprmsRdBtn = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rprms.setMargins(5, 5, 5, 5);
                rdbtn.setLayoutParams(rprmsRdBtn);

                l3.addView(ll2, rpms2);

                fl.addView(l3, rprms);
                fl.addView(rdbtn, rprmsRdBtn);

                // ll2.addView(rdbtn, rprmsRdBtn);
                ll.addView(fl, params);

                viewGroup.addView(ll, rprms);
                viewGroup.invalidate();
            }

        }

    }

    @Override
    public void onClick(View v) {
        if (v == subBtn) {
            if (replyFlag.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), "You Already Submited This Poll", Toast.LENGTH_SHORT).show();

            } else {
                if (quiz_options_id != null) {
                    LivePollSubmitFetch(token, eventid, questionId, quiz_options_id);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select something", Toast.LENGTH_SHORT).show();

                }
            }

        } else {

            String option = ((RadioButton) v).getText().toString();

            for (RadioButton radio : radios) {
                if (!radio.getText().equals(option)) {

                    radio.setChecked(false);

                }

            }

            for (int i = 0; i < optionLists.size(); i++) {

                if (option.equalsIgnoreCase(optionLists.get(i)
                        .getOption())) {

                    quiz_options_id = optionLists.get(i)
                            .getOptionId();
                }

            }

        }
    }

    public void fetchPoll(String token, String eventid) {
        showProgress();
        mAPIService.LivePollFetch(token, eventid).enqueue(new Callback<LivePollFetch>() {
            @Override
            public void onResponse(Call<LivePollFetch> call, Response<LivePollFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponseLiveList(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();

            }
        });
    }

    public void showResponseLiveList(Response<LivePollFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getLivePollList().size() != 0) {
//            PollAdapter pollAdapter = new PollAdapter(this, response.body().getLivePollList(), response.body().getLivePollOptionList(), this);
//            pollAdapter.notifyDataSetChanged();
//            pollRv.setAdapter(pollAdapter);
//            pollRv.scheduleLayoutAnimation();

            optionLists = response.body().getLivePollOptionList();
        } else {
            Toast.makeText(getApplicationContext(), "No Poll Available", Toast.LENGTH_SHORT).show();

        }
    }
}
