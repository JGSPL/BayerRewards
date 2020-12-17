package com.bayer.bayerreward.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.QuizOptionList;
import com.bayer.bayerreward.InnerDrawerActivity.QuizNewActivity;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Utility.MyApplication;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Naushad on 10/31/2017.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Quiz> quizList;
    private List<Quiz> question;
    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    // ArrayList<String> dataArray=new ArrayList<String>();
    String MY_PREFS_NAME = "ProcializeInfo";

    String[] dataArray;
    int[] righanswe;
    String[] dataIDArray;
    String[] checkArray;
    String[] ansArray;
    ApiConstant constant = new ApiConstant();
    String quiz_options_id;
    MyApplication appDelegate;

    String[] flagArray;

    int flag = 0;
    String correctAnswer;
    String selectedAnswer, colorActive;
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    String selectedOption;
    int selectopt = 0;

    Typeface typeFace;
    private RadioGroup lastCheckedRadioGroup = null;
    int count = 0;
    AnswerAdapter adapter;


    public QuizAdapter(Activity activity, List<Quiz> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        procializeDB = new DBHelper(activity);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @SuppressLint("RestrictedApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (quizList.get(position).getQuiz_type() == null) {

            holder.txt_question.setText(decodeUnicode(quizList.get(position).getQuestion()));
            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

//            holder.txt_question.setTextColor(Color.parseColor(colorActive));
            quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOptionList quizTempOptionList = new QuizOptionList();

                    quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                    quizTempOptionList.setOptionId(quizOptionList.get(i)
                            .getOptionId());
                    quizTempOptionList.setQuizId(quizOptionList.get(i)
                            .getQuizId());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }

            correctAnswer = quizList.get(position).getCorrect_answer();
            selectedAnswer = quizList.get(position).getSelected_option();

            if (correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                count = count + 1;
            }

            int number = quizSpecificOptionListnew.size() + 1;

            adapter = new AnswerAdapter(activity, quizSpecificOptionListnew, correctAnswer, selectedAnswer);
            holder.ansList.setAdapter(adapter);

        }

    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public String[] getselectedData() {
        return dataArray;

    }

    public String[] getselectedquestion() {
        return dataIDArray;

    }

    public int getSelectedOption() {
        return selectopt;

    }

    public int getCorrectOption() {
        return count;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        //        TextView quiz_title_txt, quiz_question_distruct, textno1, textno;
        LinearLayout raiolayout;
        RecyclerView ansList;
        TextView txt_question;
        //        EditText ans_edit;
//        RadioGroup viewGroup;

        public ViewHolder(View convertView) {
            super(convertView);

            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);
            ansList = (RecyclerView) convertView
                    .findViewById(R.id.ansList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            ansList.setLayoutManager(mLayoutManager);

            txt_question = convertView.findViewById(R.id.txt_question);
        }
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