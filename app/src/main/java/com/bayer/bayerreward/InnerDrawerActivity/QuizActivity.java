package com.bayer.bayerreward.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Adapter.QuizNewAdapter;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.QuizOptionList;
import com.bayer.bayerreward.Parser.QuizOptionParser;
import com.bayer.bayerreward.Parser.QuizParser;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.MyApplication;
import com.bayer.bayerreward.Utility.ServiceHandler;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class QuizActivity extends AppCompatActivity implements OnClickListener {

    private ProgressDialog pDialog;
    // Session Manager Class
    private SessionManager session;

    // Access Token Variable
    private String accessToken, event_id, colorActive;

    String quiz_question_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    private String getQuizUrl = "";

    private ConnectionDetector cd;


    private ApiConstant constant = new ApiConstant();

    RecyclerView quizNameList;
    private QuizNewAdapter adapter;

    private QuizParser quizParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    RelativeLayout relative;
    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();

    public static MyApplication appDelegate;
    public static String foldername = "null";
    Button submit, btnNext;
    ImageView headerlogoIv;
    TextView questionTv;
    TextView txt_count;
    ViewPager pager;
    //    QuizPagerAdapter pagerAdapter;
    LinearLayoutManager recyclerLayoutManager;
    String MY_PREFS_NAME = "ProcializeInfo";
    private static LinearLayout layoutHolder;
    LinearLayoutManager llm;
    int count1 = 1;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    int count = 1;
    boolean flag = true;
    boolean flag1 = true;
    boolean flag2 = true;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.activetab), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, FolderQuizActivity.class);
                startActivity(intent);
                finish();


            }
        });


        quizQuestionUrl = constant.baseUrl + constant.quizsubmit;
        procializeDB = new DBHelper(QuizActivity.this);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        appDelegate = (MyApplication) getApplicationContext();

        foldername = getIntent().getExtras().getString("folder");

        // Session Manager
        session = new SessionManager(getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        cd = new ConnectionDetector(getApplicationContext());

        // Initialize Get Quiz URL
        getQuizUrl = constant.baseUrl + constant.quizlist;


        submit = (Button) findViewById(R.id.submit);
        btnNext = (Button) findViewById(R.id.btnNext);

        submit.setOnClickListener(this);
//        btnNext.setOnClickListener(this);

        quizNameList = (RecyclerView) findViewById(R.id.quiz_list);
        questionTv = (TextView) findViewById(R.id.questionTv);
        txt_count = (TextView) findViewById(R.id.txt_count);
        relative = (RelativeLayout) findViewById(R.id.relative);
        questionTv.setText(decodeUnicode(foldername));
        quizNameList.setLayoutFrozen(true);
        questionTv.setBackgroundColor(Color.parseColor("#ffffff"));

        quizNameList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        llm = (LinearLayoutManager) quizNameList.getLayoutManager();

//        if (adapter.waitTimer != null) {
//            adapter.waitTimer.cancel();
//            adapter.waitTimer = null;
//
//        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = adapter.getSelectedOption();
                String correctOption = quizList.get(llm.findLastVisibleItemPosition()).getCorrect_answer();
                int i = adapter.getItemCount();
                int pos = adapter.getposition();
//                adapter.getItemViewType(llm.findLastVisibleItemPosition());
                if (pos == llm.findLastVisibleItemPosition() + 1) {
                    if (i != count) {
                        if (option != llm.findLastVisibleItemPosition()) {
                            quizNameList.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + 1);
                            txt_count.setText(count + 1 + "/" + i);
                            count = count + 1;
                            if (quizList.size() == llm.findLastVisibleItemPosition() + 2) {

                                btnNext.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                            } else {
                                btnNext.setVisibility(View.VISIBLE);
                                submit.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(QuizActivity.this, "Please Select Option", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Please Select Option", Toast.LENGTH_SHORT).show();
                }


            }
        });

        quizNameList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                recyclerView.setLayoutFrozen(true);
//                if (viewModel.isItemSelected) {
                if (llm.findLastVisibleItemPosition() >= 0) {
                    recyclerView.stopScroll();
                }
//                }
            }
        });




/*
        quizNameList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                recyclerView.setLayoutFrozen(true);
//                if (viewModel.isItemSelected) {
                if (llm.findLastVisibleItemPosition() >= 0) {
                    recyclerView.stopScroll();
                }
//                }
            }
        });
*/
//        recyclerLayoutManager = new LinearLayoutManager(this);
//        quizNameList.setLayoutManager(recyclerLayoutManager);

        pager = findViewById(R.id.pager);

//		quizNameList.setItemViewCacheSize(0);
        quizNameList.setAnimationCacheEnabled(true);
        quizNameList.setDrawingCacheEnabled(true);
        quizNameList.hasFixedSize();

//
//
//		quizNameList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//			}
//		});

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(QuizActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

            // videoDBList = dbHelper.getVideoList();
            //
            // adapter = new VideosListAdapter(this, videoDBList);
            // videoList.setAdapter(adapter);
        }

        CommonFunction.crashlytics("QuizActivity", "");
        firbaseAnalytics(this, "QuizActivity", "");
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class getQuizList extends AsyncTask<Void, Void, Void> {

        String status = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            // Showing progress dialog
            pDialog = new ProgressDialog(QuizActivity.this,
                    R.style.Base_Theme_AppCompat_Dialog_Alert);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));

            nameValuePair.add(new BasicNameValuePair("event_id",
                    event_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getQuizUrl,
                    ServiceHandler.POST, nameValuePair);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    status = jsonResult.getString("status");
                    message = jsonResult.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (status.equalsIgnoreCase("success")) {

                // Get Quiz Parser
                quizParser = new QuizParser();
                quizList = quizParser.Quiz_Parser(jsonStr, foldername);

                // Get Quiz Option List
                quizOptionParser = new QuizOptionParser();
                quizOptionList = quizOptionParser.Quiz_Option_Parser(jsonStr);

                procializeDB.clearQuizTable();
                procializeDB.insertQuizTable(quizList, db);

                appDelegate.setQuizOptionList(quizOptionList);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

//            pagerAdapter = new QuizPagerAdapter(QuizActivity.this, quizList);
//            pager.setAdapter(pagerAdapter);
//            pagerAdapter.notifyDataSetChanged();

            adapter = new QuizNewAdapter(QuizActivity.this, quizList);
            quizNameList.setAdapter(adapter);
            int itemcount = adapter.getItemCount();
            txt_count.setText(1 + "/" + itemcount);
            if (quizList.size() > 1) {
                btnNext.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);

            } else {
                btnNext.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }


//			Parcelable state = quizNameList.onSaveInstanceState();
//			quizNameList.onRestoreInstanceState(state);
//			quizNameList.setEmptyView(findViewById(android.R.id.empty));

        }
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            if (adapter.waitTimer != null) {
                adapter.waitTimer.cancel();

                adapter.waitTimer = null;

            }
            Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
            Log.e("size", adapter.getItemCount() + "");


            String[] data = adapter.getselectedData();
            String[] question = adapter.getselectedquestion();
            String[] selected = adapter.getselectedId();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = quizList.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    if (data[i] != null) {
                        if (quizList.get(i).getQuiz_type() != null) {
                            if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = selected[i];

                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getOptionId().equals(idno)) {
                                            question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                        }
                                    }
                                } else {
                                    valid = false;
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = selected[i];

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getOptionId().equals(idno)) {
                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    }
                                }
                            } else {
                                valid = false;
                            }
                        }
                    } else {
                        valid = false;
                    }

                }


                Log.e("valid", question_ans.toString());
                Log.e("valid", question_id.toString());
                Log.e("valid", valid.toString());


                if (valid == true) {
                    quiz_question_id = question_id[0];
                    quiz_options_id = question_ans[0];
                    int answers = adapter.getCorrectOption();
                    new postQuizQuestion().execute();
//                    Intent intent = new Intent(QuizActivity.this, YourScoreActivity.class);
//                    intent.putExtra("folderName", foldername);
//                    intent.putExtra("Answers", answers);
//                    intent.putExtra("TotalQue", adapter.getselectedData().length);
//                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
                }


            }

//			//adapter.notifyDataSetChanged();
//			quizNameList.post(new Runnable() {
//				@Override
//				public void run() {
//
//						for (int i = 0; i < adapter.getItemCount(); i++) {
//
//							//View view = quizNameList.getChildAt(i);
//							View view = quizNameList.getLayoutManager().findViewByPosition(i);
////							View view= quizNameList.findViewHolderForAdapterPosition(i).itemView;
//
//							//QuizNewAdapter.ViewHolder holder= (QuizNewAdapter.ViewHolder) quizNameList.findViewHolderForAdapterPosition(i);
//
//
////						QuizNewAdapter.ViewHolder holder =
////								quizNameList.findViewHolderForItemId(adapter.getItemId(i));
//							//View view = holder.itemView;
//							//View view=adapter.getItemId();
////				if (view == null) {
////					try {
////						final int firstListItemPosition = 0;
////						final int lastListItemPosition = firstListItemPosition
////								+ quizNameList.getChildCount();
////
////						if (i < firstListItemPosition || i > lastListItemPosition) {
////							//This may occure using Android Monkey, else will work otherwise
//////							/view = quizNameList.getAdapter().getItemViewType(i, null, quizNameList);
//////							view = quizNameList.getAdapter().getItemViewType(i);
////							view=quizNameList.findViewHolderForAdapterPosition(i).itemView;
////						} else {
////							final int childIndex = i - firstListItemPosition;
////							view = quizNameList.getChildAt(childIndex);
////						}
////					} catch (Exception e) {
////						e.printStackTrace();
////						view = null;
////					}
////				}
//
//							if (i != 0) {
//								question_id[0] = question_id[0] + "$#";
//								question_ans[0] = question_ans[0] + "$#";
//							}
//
//							String id = quizList.get(i).getId();
//
//							question_id[0] = question_id[0] + id;
//
//
//							if (quizList.get(i).getQuiz_type().equalsIgnoreCase("1")) {
//
//								radioGroup[0] = view.findViewById(R.id.radiogroup);
//
//								int genid = radioGroup[0].getCheckedRadioButtonId();
//								radioButton[0] = radioGroup[0].findViewById(genid);
//								value[0] = radioButton[0].getText().toString();
//
//
//								for (int j = 0; j < quizOptionList.size(); j++) {
//									if (quizOptionList.get(j).getOption().equals(value[0]) && quizOptionList.get(j).getQuiz_id().equals(id)) {
//										question_ans[0] = question_ans[0] + quizOptionList.get(j).getOption_id();
//									}
//								}
//
//
//							} else if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
//
//								ans_edit[0] = view.findViewById(R.id.ans_edit);
//
//								question_ans[0] = question_ans[0] + ans_edit[0].getText().toString().trim();
//
//								if (ans_edit[0].getText().toString().trim().equalsIgnoreCase("")) {
//									check[0] = check[0] + 1;
//								}
//							}
//						}
//
//					quiz_question_id = question_id[0];
//					quiz_options_id = question_ans[0];
//
//					if (quiz_question_id!=null && quiz_options_id!=null) {
//						if (!quiz_options_id.equalsIgnoreCase("") && !quiz_question_id.equalsIgnoreCase("")) {
//							if (check[0] == 0) {
//								new postQuizQuestion().execute();
//							} else {
//								Toast.makeText(QuizActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
//							}
//						}
//					}
//				}
//			});
//
//
//			Log.e("id", question_id[0]);
//			Log.e("ans", question_ans[0]);
//
//			quiz_question_id = question_id[0];
//			quiz_options_id = question_ans[0];
//
//			if (quiz_question_id!=null && quiz_options_id!=null) {
//			    if (!quiz_options_id.equalsIgnoreCase("") && !quiz_question_id.equalsIgnoreCase("")) {
//                    if (check[0] == 0) {
//                        new postQuizQuestion().execute();
//                    } else {
//                        Toast.makeText(QuizActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
//                    }
//                }
//			}
//


        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        }

    }


    private class postQuizQuestion extends AsyncTask<Void, Void, Void> {

        String error = "";
        String message = "";
        String total_questions = "";
        String total_correct_answer = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(QuizActivity.this,
                    R.style.Base_Theme_AppCompat_Dialog_Alert);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));
            nameValuePair.add(new BasicNameValuePair("event_id",
                    event_id));
            nameValuePair.add(new BasicNameValuePair("quiz_id", quiz_question_id));
            nameValuePair.add(new BasicNameValuePair("quiz_options_id",
                    quiz_options_id));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(quizQuestionUrl,
                    ServiceHandler.POST, nameValuePair);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    error = jsonResult.getString("status");
                    message = jsonResult.getString("msg");
                    total_correct_answer = jsonResult.getString("total_correct_answer");
                    total_questions = jsonResult.getString("total_questions");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            if (error.equalsIgnoreCase("success")) {
                int answers = adapter.getCorrectOption();
                Intent intent = new Intent(QuizActivity.this, YourScoreActivity.class);
                intent.putExtra("folderName", foldername);
                intent.putExtra("Answers", total_correct_answer);
                intent.putExtra("TotalQue", total_questions);
                startActivity(intent);
                finish();
            } else {

                Toast.makeText(QuizActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        adapter.selectopt = 0;
//        if (adapter.waitTimer != null) {
//            adapter.waitTimer.cancel();
//            adapter.waitTimer = null;
//        }
        Intent intent = new Intent(QuizActivity.this, FolderQuizActivity.class);
        startActivity(intent);
        finish();


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
