package com.procialize.singleevent.InnerDrawerActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.procialize.singleevent.Adapter.QuizFolderAdapter;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.GetterSetter.Quiz;
import com.procialize.singleevent.GetterSetter.QuizFolder;
import com.procialize.singleevent.GetterSetter.QuizOptionList;
import com.procialize.singleevent.InnerDrawerActivity.QuizActivity;

import com.procialize.singleevent.Parser.QuizFolderParser;
import com.procialize.singleevent.Parser.QuizOptionParser;
import com.procialize.singleevent.Parser.QuizParser;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.MyApplication;
import com.procialize.singleevent.Utility.ServiceHandler;
import com.procialize.singleevent.Utility.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class FolderQuizActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    // Session Manager Class
    private SessionManager session;

    // Access Token Variable
    private String accessToken,event_id;

    private String quiz_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    private String getQuizUrl = "";

    private ConnectionDetector cd;


    private ListView quizNameList;
    private QuizFolderAdapter adapter;

    private QuizParser quizParser;
    private QuizFolderParser quizFolderParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    private ArrayList<QuizFolder> quizFolders = new ArrayList<QuizFolder>();

    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();

    public String Jsontr;
    public static MyApplication appDelegate;
    ApiConstant constant;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView headerlogoIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        constant = new ApiConstant();
        appDelegate = (MyApplication) getApplicationContext();

        // Session Manager
        session = new SessionManager(getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");

        cd = new ConnectionDetector(getApplicationContext());

        // Initialize Get Quiz URL
        getQuizUrl = constant.baseUrl + constant.quizlist;

        quizNameList = (ListView) findViewById(R.id.quiz_list);
        quizNameList.setScrollingCacheEnabled(false);
        quizNameList.setAnimationCacheEnabled(false);

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(FolderQuizActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }


        quizNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                QuizFolder quiz = adapter.getQuestionIdFromList(position);


                if (quiz != null) {
                    if (quiz.getFolder_name() != null && !quiz.getFolder_name().equalsIgnoreCase("null")) {

                        if (Jsontr != null) {

                            QuizParser quizParser = new QuizParser();

                            quizList = new ArrayList<>();

                            Log.e("size", quizList.size() + "");
                            Log.e("size", quiz.getFolder_name());

                            quizList = quizParser.Quiz_Parser(Jsontr, quiz.getFolder_name());


                            if (/*quizList != null ||*/ quizList.size() > 0) {

                                if (quizList.get(0).getReplied().equals("1")) {

                                    Toast.makeText(FolderQuizActivity.this, "You already submitted the quiz.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Intent quizOptionIntent = new Intent(FolderQuizActivity.this, QuizActivity.class);
                                    quizOptionIntent.putExtra("folder", quiz.getFolder_name());
                                    startActivity(quizOptionIntent);
                                  //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            } else {

                                Toast.makeText(FolderQuizActivity.this,
                                        "Question not available.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        Toast.makeText(FolderQuizActivity.this,
                                "Question not available.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(FolderQuizActivity.this,
                            "Question not available.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

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
            pDialog = new ProgressDialog(FolderQuizActivity.this,
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

            Log.e("api_access_token",accessToken);
            Log.e("api_access_token",event_id);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getQuizUrl,
                    ServiceHandler.POST, nameValuePair);

            Log.d("quizresponse: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    Jsontr = jsonStr;

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    status = jsonResult.getString("status");
                    message = jsonResult.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (status.equalsIgnoreCase("success")) {


                //Get Folder Parser
                quizFolderParser = new QuizFolderParser();
                quizFolders = quizFolderParser.QuizFolder_Parser(jsonStr);

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

            adapter = new QuizFolderAdapter(FolderQuizActivity.this, quizFolders);
            quizNameList.setAdapter(adapter);
            quizNameList.setEmptyView(findViewById(android.R.id.empty));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
