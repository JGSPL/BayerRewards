package com.bayer.bayerreward.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.QuizOptionList;
import com.bayer.bayerreward.InnerDrawerActivity.QuizActivity;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Utility.MyApplication;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class QuizPagerAdapter extends PagerAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Quiz> quizList;
    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();

    // ArrayList<String> dataArray=new ArrayList<String>();


    String[] dataArray;
    String[] dataIDArray;
    String[] checkArray;
    String[] ansArray;
    ApiConstant constant = new ApiConstant();
    String quiz_options_id;
    MyApplication appDelegate;

    String[] flagArray;

    int flag = 0;

    private SparseIntArray mSpCheckedState = new SparseIntArray();


    Typeface typeFace;
    private RadioGroup lastCheckedRadioGroup = null;

    public QuizPagerAdapter(Activity activity, List<Quiz> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.quiz_row_test, view, true);

        TextView quiz_title_txt, quiz_question_distruct = null, textno1, textno;
        LinearLayout raiolayout;
//        EditText ans_edit;
        RadioGroup viewGroup;

        quiz_title_txt = (TextView) myImageLayout
                .findViewById(R.id.quiz_question);
        //quiz_title_txt.setTag(position);

//            quiz_question_distruct = (TextView) convertView
//                    .findViewById(R.id.quiz_question_distruct);
//
//            ans_edit = (EditText) convertView
//                    .findViewById(R.id.ans_edit);
//
//            discript_layout = (LinearLayout) convertView
//                    .findViewById(R.id.discript_layout);

        raiolayout = (LinearLayout) myImageLayout.findViewById(R.id.raiolayout);

        viewGroup = (RadioGroup) myImageLayout.findViewById(R.id.radiogroup);

        textno1 = (TextView) myImageLayout
                .findViewById(R.id.textno1);

//            textno = (TextView) convertView
//                    .findViewById(R.id.textno);


        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                "Roboto-Light.ttf");

        Typeface typeFace1 = Typeface.createFromAsset(activity.getAssets(),
                "Roboto-Light.ttf");
        quiz_title_txt.setTypeface(typeFace1);
//            quiz_question_distruct.setTypeface(typeFace);
//            ans_edit.setTypeface(typeFace);
//            textno.setTypeface(typeFace);
        textno1.setTypeface(typeFace);
        if (quizList.get(position).getQuiz_type() == null) {

            if (raiolayout.getVisibility() == View.GONE) {
                raiolayout.setVisibility(View.VISIBLE);
            }

            quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));

//					int count = position+1;
//					String no = "Q" + String.valueOf(count)+".";
//
//					holder.textno.setText(no);

            quizOptionList = QuizActivity.appDelegate.getQuizOptionList();
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


            int number = quizSpecificOptionListnew.size() + 1;

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);


            for (int row = 0; row < 1; row++) {
                LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.VERTICAL);

                //  holder.viewGroup.removeAllViews();
                //	if(flag==0)
                for (int i = 1; i < number; i++) {

                    RadioButton rdbtn = new RadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(quizSpecificOptionListnew.get(i - 1).getOption());
                    rdbtn.setTextColor(Color.WHITE);
                    rdbtn.setTextSize(14);
                    rdbtn.setBackgroundResource(R.drawable.agenda_bg);

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(5, 5, 5, 5);
//                    rdbtn.setPadding(10,5,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOptionId());

                    //  rdbtn.setOnCheckedChangeListener(activity.this);

//                    if (i == 1)
//                        rdbtn.setChecked(true);

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);
                        }
                    }

                    viewGroup.addView(rdbtn);

                    flag = 1;
                }
            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(0)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOptionId();
                }

            }
            int genid = viewGroup.getCheckedRadioButtonId();
            RadioButton radioButton = viewGroup.findViewById(genid);


            //  value[0] = radioButton.getText().toString();
            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }
        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("2")) {

            if (raiolayout.getVisibility() == View.VISIBLE) {
                raiolayout.setVisibility(View.GONE);
            }

            quiz_title_txt.setText(quizList.get(position).getQuestion());

//					int count = position+1;
//					String no = "Q" + String.valueOf(count)+".";
//
//					holder.textno1.setText(no);
            quiz_question_distruct.setText(quizList.get(position).getQuestion());
//            dataArray[position]= holder.ans_edit.getText().toString();

//            if (ansArray[position]!=null)
//            {
//                holder.ans_edit.setText(ansArray[position]);
//            }

        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("1")) {

            if (raiolayout.getVisibility() == View.GONE) {
                raiolayout.setVisibility(View.VISIBLE);
            }

            quiz_title_txt.setText(quizList.get(position).getQuestion());

//					int count = position+1;
//					String no = "Q" + String.valueOf(count)+".";
//
//					holder.textno.setText(no);

            quizOptionList = QuizActivity.appDelegate.getQuizOptionList();
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


            int number = quizSpecificOptionListnew.size() + 1;

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);


            for (int row = 0; row < 1; row++) {
                LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.VERTICAL);

                //  holder.viewGroup.removeAllViews();
                //	if(flag==0)
                for (int i = 1; i < number; i++) {

                    RadioButton rdbtn = new RadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(quizSpecificOptionListnew.get(i - 1).getOption());
                    rdbtn.setTextColor(Color.WHITE);
                    rdbtn.setTextSize(9);
                    rdbtn.setBackgroundResource(R.drawable.agenda_bg);

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(5, 5, 5, 5);
//                    rdbtn.setPadding(10,5,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOptionId());

                    //  rdbtn.setOnCheckedChangeListener(activity.this);

//                    if (i == 1)
//                        rdbtn.setChecked(true);

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);
                        }
                    }

                    viewGroup.addView(rdbtn);

                    flag = 1;
                }
            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(0)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOptionId();
                }

            }
            int genid = viewGroup.getCheckedRadioButtonId();
            RadioButton radioButton = viewGroup.findViewById(genid);


            //  value[0] = radioButton.getText().toString();
            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }
        }


        final TextWatcher txwatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dataArray[position] = s.toString();
                ansArray[position] = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                System.out.print("Hello");
            }
        };

//        holder.ans_edit.addTextChangedListener(txwatcher);
        viewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int genid = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = radioGroup.findViewById(genid);
                //  value[0] = radioButton.getText().toString();
                dataArray[position] = radioButton.getText().toString();
                checkArray[position] = radioButton.getText().toString();

            }
        });

        return myImageLayout;
    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
