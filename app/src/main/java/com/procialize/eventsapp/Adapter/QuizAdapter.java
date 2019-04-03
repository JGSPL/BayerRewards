package com.procialize.eventsapp.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.procialize.eventsapp.ApiConstant.ApiConstant;
import com.procialize.eventsapp.DbHelper.DBHelper;
import com.procialize.eventsapp.GetterSetter.Quiz;
import com.procialize.eventsapp.GetterSetter.QuizOptionList;
import com.procialize.eventsapp.InnerDrawerActivity.QuizNewActivity;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Utility.MyApplication;

import java.util.ArrayList;
import java.util.List;

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
    String selectedAnswer;
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

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }


            quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }

            QuizNewActivity.questionTv.setText(quizList.get(position).getQuestion());
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
        //        EditText ans_edit;
//        RadioGroup viewGroup;

        public ViewHolder(View convertView) {
            super(convertView);
//            quiz_title_txt = (TextView) convertView
//                    .findViewById(R.id.quiz_question);
            //quiz_title_txt.setTag(position);

//            quiz_question_distruct = (TextView) convertView
//                    .findViewById(R.id.quiz_question_distruct);
//
//            ans_edit = (EditText) convertView
//                    .findViewById(R.id.ans_edit);
//
//            discript_layout = (LinearLayout) convertView
//                    .findViewById(R.id.discript_layout);

            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);
            ansList = (RecyclerView) convertView
                    .findViewById(R.id.ansList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            ansList.setLayoutManager(mLayoutManager);


//            viewGroup = (RadioGroup) convertView
//                    .findViewById(R.id.radiogroup);

//            textno1 = (TextView) convertView
//                    .findViewById(R.id.textno1);

//            textno = (TextView) convertView
//                    .findViewById(R.id.textno);


            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");

            Typeface typeFace1 = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");
//            quiz_title_txt.setTypeface(typeFace1);
//            quiz_question_distruct.setTypeface(typeFace);
//            ans_edit.setTypeface(typeFace);
//            textno.setTypeface(typeFace);
//            textno1.setTypeface(typeFace);

//            viewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//
//
//                    dataArray[position]=quizList.get(position).getQuestion();
//
////                    since only one package is allowed to be selected
////                    this logic clears previous selection
////                    it checks state of last radiogroup and
////                     clears it if it meets conditions
//                    if (lastCheckedRadioGroup != null
//                            && lastCheckedRadioGroup.getCheckedRadioButtonId()
//                            != radioGroup.getCheckedRadioButtonId()
//                            && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
//                        lastCheckedRadioGroup.clearCheck();
//
////                        Toast.makeText(QuizNewAdapter.this.context,
////                                "Radio button clicked " + radioGroup.getCheckedRadioButtonId(),
////                                Toast.LENGTH_SHORT).show();
//
//                    }
//                    lastCheckedRadioGroup = radioGroup;
//
//                }
//            });
        }
    }

}