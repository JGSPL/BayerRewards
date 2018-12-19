package com.procialize.singleevent.Adapter;
//

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.GetterSetter.Quiz;
import com.procialize.singleevent.GetterSetter.QuizOptionList;
import com.procialize.singleevent.InnerDrawerActivity.QuizActivity;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Utility.MyApplication;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuizNewAdapter extends RecyclerView.Adapter<QuizNewAdapter.ViewHolder> {
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

    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive;

    public QuizNewAdapter(Activity activity, List<Quiz> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];

        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_test, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        PackageModel packageModel = quizList.get(position);
//        holder.packageName.setText(packageModel.getPackageName());
//
//        int id = (position + 1) * 100;
//        for (String price : packageModel.getPriceList()) {
//            RadioButton rb = new RadioButton(QuizNewAdapter.this.context);
//            rb.setId(id++);
//            rb.setText(price);
//
//            holder.priceGroup.addView(rb);
//        }

        holder.setIsRecyclable(false);
        if (quizList.get(position).getQuiz_type() == null) {

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

            holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));

            holder.quiz_title_txt.setTextColor(Color.parseColor(colorActive));

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
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setTextSize(14);
                    rdbtn.setBackgroundResource(R.drawable.agenda_bg);

//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);

                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#4d4d4d")//disabled
                                        , Color.parseColor(colorActive) //enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(0, 8, 0, 5);
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    p.setMargins(0, 20, 0, 10);

//                    rdbtn.setPadding(0, 10, 0, 10);
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

//                    if (i == 1) {
//                        rdbtn.setChecked(true);
//                    } else {
//                        rdbtn.setChecked(false);
//                    }
                    holder.viewGroup.addView(rdbtn, params);


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
            RadioButton radioButton = holder.viewGroup.findViewById(genid);


            //  value[0] = radioButton.getText().toString();
            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();

            }
        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("2")) {

            if (holder.raiolayout.getVisibility() == View.VISIBLE) {
                holder.raiolayout.setVisibility(View.GONE);
            }

            holder.quiz_title_txt.setText(quizList.get(position).getQuestion());

//					int count = position+1;
//					String no = "Q" + String.valueOf(count)+".";
//
//					holder.textno1.setText(no);
            holder.quiz_question_distruct.setText(quizList.get(position).getQuestion());
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
                LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.VERTICAL);

                //  holder.viewGroup.removeAllViews();
                //	if(flag==0)
                for (int i = 1; i < number; i++) {

                    RadioButton rdbtn = new RadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setTextSize(9);
                    rdbtn.setBackgroundResource(R.drawable.agenda_bg);

//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#4d4d4d")
                                        , Color.parseColor(colorActive)//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(0, 8, 0, 5);
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    p.setMargins(0, 20, 0, 10);
//                    rdbtn.setPadding(0, 10, 0, 10);
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
//                    if (i == 1) {
//                        rdbtn.setChecked(true);
//                    } else {
//                        rdbtn.setChecked(false);
//                    }
                    holder.viewGroup.addView(rdbtn, params);

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
            RadioButton radioButton = holder.viewGroup.findViewById(genid);


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
                RadioButton radioButton = radioGroup.findViewById(genid);
                //  value[0] = radioButton.getText().toString();
                dataArray[position] = radioButton.getText().toString();
                checkArray[position] = radioButton.getText().toString();

            }
        });


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

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        if (holder instanceof ViewHolder) {
            holder.setIsRecyclable(false);
        }
        super.onViewDetachedFromWindow(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_title_txt, quiz_question_distruct, textno1, textno;
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

//            textno = (TextView) convertView
//                    .findViewById(R.id.textno);


            typeFace = Typeface.createFromAsset(activity.getAssets(),
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