package com.bayer.bayerreward.Adapter;
//

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.QuizOptionList;
import com.bayer.bayerreward.InnerDrawerActivity.QuizActivity;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.MyApplication;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class QuizNewAdapter extends RecyclerView.Adapter<QuizNewAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Quiz> quizList;
    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    static ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();

    // ArrayList<String> dataArray=new ArrayList<String>();
    private ProgressDialog pDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    public static String[] dataArray;
    int[] righanswe;
    static String[] dataIDArray;
    static String[] checkArray;
    String[] ansArray;
    ApiConstant constant = new ApiConstant();
    String quiz_options_id;
    MyApplication appDelegate;
    String[] SelectedId;
    String[] flagArray;

    int flag = 0;
    String correctAnswer;
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    String selectedOption;
    public static int selectopt = 0;
    String accessToken, event_id, colorActive;
    Typeface typeFace;
    private RadioGroup lastCheckedRadioGroup = null;
    int count = 0;
    private String quizQuestionUrl = "";
    private SessionManager session;
    public static CountDownTimer waitTimer;
    int itempos;

    public QuizNewAdapter(Activity activity, List<Quiz> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        SelectedId = new String[quizList.size()];

        session = new SessionManager(activity.getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        quizQuestionUrl = constant.baseUrl + constant.quizsubmit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_test, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.setIsRecyclable(false);
//        holder.txt_time.setTextColor(Color.parseColor(colorActive));
//        holder.textno1.setTextColor(Color.parseColor(colorActive));
        if (quizList.get(position).getQuiz_type() == null) {

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

            holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));

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

            correctAnswer = quizList.get(position).getCorrect_answer();
            int number = quizSpecificOptionListnew.size() + 1;

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);


            for (int row = 0; row < 1; row++) {
                //LinearLayout ll = new LinearLayout(activity);
                // ll.setOrientation(LinearLayout.VERTICAL);
                //ll.setGravity(Gravity.CENTER);
                //  holder.viewGroup.removeAllViews();
                //	if(flag==0)

                for (int i = 1; i < number; i++) {

                    AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption().toString()));
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setTextSize(14);
//                    rdbtn.setBackgroundResource(R.drawable.livepollback);
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#585e44")//disabled
                                        , Color.parseColor("#e31e24")//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(10, 15, 10, 10);

                    // rdbtn.setPadding(10,10,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOptionId());
                    rdbtn.setBackgroundResource(R.drawable.livepollback);

//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                    rdbtn.setPaddingRelative(5, 5, 5, 5);
                    rdbtn.setPadding(15, 15, 15, 15);


                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);
                        }
                    }


                    holder.viewGroup.addView(rdbtn);

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
            int genid = holder.viewGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioButton = holder.viewGroup.findViewById(genid);


            //  value[0] = radioButton.getText().toString();
            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }
        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("2")) {

            if (holder.raiolayout.getVisibility() == View.VISIBLE) {
                holder.raiolayout.setVisibility(View.GONE);
            }

            holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));

//					int count = position+1;
//					String no = "Q" + String.valueOf(count)+".";
//
//					holder.textno1.setText(no);
            holder.quiz_question_distruct.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));
//            dataArray[position]= holder.ans_edit.getText().toString();

//            if (ansArray[position]!=null)
//            {
//                holder.ans_edit.setText(ansArray[position]);
//            }

        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("1")) {

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

            holder.quiz_title_txt.setText(quizList.get(position).getQuestion());

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
               /* LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setGravity(Gravity.CENTER);*/
                //  holder.viewGroup.removeAllViews();
                //	if(flag==0)

                for (int i = 1; i < number; i++) {

                    final AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setTextSize(9);
                    rdbtn.setBackgroundResource(R.drawable.livepollback);

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(10, 15, 10, 10);
                    // rdbtn.setPadding(10,10,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOptionId());
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#585e44")//disabled
                                        , Color.parseColor("#e31e24")//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
//                    rdbtn.setGravity(Gravity.CENTER);
                    rdbtn.setPadding(15, 15, 15, 15);
                    rdbtn.setPaddingRelative(5, 5, 5, 5);

                    // rdbtn.setCompoundDrawablePadding(5);

//                    ColorStateList colorStateList = new ColorStateList(
//                            new int[][]{
//                                    new int[]{-android.R.attr.state_checked}, // unchecked
//                                    new int[]{android.R.attr.state_checked}  // checked
//                            },
//                            new int[]{
//                                    Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.unchecked_radio))),
//                                    Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.checked_radio)))
//                            }
//                    );
//                    rdbtn.setSupportButtonTintList(colorStateList);
                    //  rdbtn.setOnCheckedChangeListener(activity.this);

//                    if (i == 1)
//                        rdbtn.setChecked(true);

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);

                        }
                    }


                    holder.viewGroup.addView(rdbtn);

                    flag = 1;
                }


/*
                for (int i = 1; i < number; i++) {

                    LinearLayout ll = new LinearLayout(activity);

                    LinearLayout l3 = new LinearLayout(activity);
                    FrameLayout fl = new FrameLayout(activity);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setPadding(10, 10, 10, 10);

                    LinearLayout ll2 = new LinearLayout(activity);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);
                    ll2.setPadding(10, 10, 10, 10);

                    LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                    AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(quizSpecificOptionListnew.get(i - 1).getOption());
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle);

//                rdbtn.setTypeface(typeFace);
                    // rdbtn.setOnClickListener(activity);


                    //radios.add(rdbtn);

                    // rdbtn.setBackgroundResource(R.drawable.edit_background);

                    rprms = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);

                    Float weight = 0.0f;



                    rpms2 = new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.MATCH_PARENT, weight);
                    rpms2.setMargins(5, 5, 5, 5);

                    ll.setBackgroundResource(R.drawable.bg_poll);
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

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);

                        }
                    }

                    holder.viewGroup.addView(ll, rprms);
                    holder.viewGroup.invalidate();
                    flag = 1;
                }
*/

            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(i)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOptionId();
                }

            }
            int genid = holder.viewGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioButton = holder.viewGroup.findViewById(genid);

//            ColorStateList colorStateList = new ColorStateList(
//                    new int[][]{
//                            new int[]{-android.R.attr.state_checked}, // unchecked
//                            new int[]{android.R.attr.state_checked}  // checked
//                    },
//                    new int[]{
//                            Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.unchecked_radio))),
//                            Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.checked_radio)))
//                    }
//            );
//            radioButton.setSupportButtonTintList(colorStateList);

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
        holder.viewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int genid = radioGroup.getCheckedRadioButtonId();
                int nonchecked = quizList.indexOf(correctAnswer);
                AppCompatRadioButton radioButton = radioGroup.findViewById(genid);
                dataArray[position] = radioButton.getText().toString();
                checkArray[position] = radioButton.getText().toString();
                String quizid = quizList.get(position).getId();
                selectedOption = quizSpecificOptionListnew.get(i - 1).getOptionId();
                selectopt = selectopt + 1;

                if (selectedOption.equalsIgnoreCase(correctAnswer)) {

                    count = count + 1;
                }

                for (int j = 0; j < quizOptionList.size(); j++) {
                    if (StringEscapeUtils.unescapeJava(radioButton.getText().toString()).equalsIgnoreCase(StringEscapeUtils.unescapeJava(quizOptionList.get(j).getOption().toString()))) {
                        if (quizid.equalsIgnoreCase(quizOptionList.get(j).getQuizId())) {
                            if (quizOptionList.get(j).getOptionId().equalsIgnoreCase(selectedOption)) {
                                SelectedId[position] = quizOptionList.get(j).getOptionId();
                                itempos = position + 1;
                            }
                        }
                    }
                }
            }
        });


    }

    public int getposition() {
        return itempos;

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

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        if (holder instanceof ViewHolder) {
            holder.setIsRecyclable(false);

//            waitTimer.start();

        }
        super.onViewDetachedFromWindow(holder);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_title_txt, quiz_question_distruct, textno1, txt_time;
        LinearLayout raiolayout;
        //        EditText ans_edit;
        RadioGroup viewGroup;

        public ViewHolder(View convertView) {
            super(convertView);
            quiz_title_txt = (TextView) convertView
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

            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);

            viewGroup = (RadioGroup) convertView
                    .findViewById(R.id.radiogroup);

            textno1 = (TextView) convertView
                    .findViewById(R.id.textno1);

            txt_time = (TextView) convertView
                    .findViewById(R.id.txt_time);

//            textno = (TextView) convertView
//                    .findViewById(R.id.textno);


            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");

            Typeface typeFace1 = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");
            quiz_title_txt.setTypeface(typeFace1);
//            quiz_question_distruct.setTypeface(typeFace);
//            ans_edit.setTypeface(typeFace);
//            textno.setTypeface(typeFace);
            textno1.setTypeface(typeFace);

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

    public String[] getselectedId() {
        return SelectedId;

    }

}