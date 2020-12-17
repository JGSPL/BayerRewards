package com.bayer.bayerreward.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.QuizOptionList;
import com.bayer.bayerreward.InnerDrawerActivity.QuizNewActivity;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.ArrayList;
import java.util.List;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class AnswersActivity extends AppCompatActivity {

    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();
    ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private DBHelper dbHelper;
    String[] dataArray;
    String[] dataIDArray;
    String[] checkArray;
    String[] ansArray;
    RadioGroup viewGroup;
    int flag = 0;
    String quiz_options_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        viewGroup = (RadioGroup)
                findViewById(R.id.radiogroup);

        dbHelper = new DBHelper(AnswersActivity.this);

        procializeDB = new DBHelper(AnswersActivity.this);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();


        Intent intent = getIntent();
        String folder_name = intent.getStringExtra("folderName");
        quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
        quizList=procializeDB.getQuizList(folder_name);

        quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
        if (quizSpecificOptionListnew.size() > 0) {
            quizSpecificOptionListnew.clear();
        }

        quizList = procializeDB.getQuizList(folder_name);
        for (int i = 0; i < quizOptionList.size()-1; i++) {

            if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(i).getId())) {

                QuizOptionList quizTempOptionList = new QuizOptionList();

                quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                quizTempOptionList.setOptionId(quizOptionList.get(i)
                        .getOptionId());
                quizTempOptionList.setQuizId(quizOptionList.get(i)
                        .getQuizId());

                quizSpecificOptionListnew.add(quizTempOptionList);

            }

        }

        CommonFunction.crashlytics("AnswerActivity","");
        firbaseAnalytics(this,"AnswerActivity","");
    }
}