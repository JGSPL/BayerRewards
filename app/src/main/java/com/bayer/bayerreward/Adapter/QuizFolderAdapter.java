package com.bayer.bayerreward.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bayer.bayerreward.GetterSetter.QuizFolder;
import com.bayer.bayerreward.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuizFolderAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<QuizFolder> quizList;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public QuizFolderAdapter(Activity activity, List<QuizFolder> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public Object getItem(int location) {
        return quizList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public QuizFolder getQuestionIdFromList(int position) {
        return quizList.get(position);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.quiz_row, null);

            holder = new ViewHolder();

            // holder.video_preview_img = (ImageView) convertView
            // .findViewById(R.id.video_preview_img);

            holder.quiz_title_txt = (TextView) convertView
                    .findViewById(R.id.video_title_txt);

            holder.linQuiz = (LinearLayout)convertView.findViewById(R.id.linQuiz);
//            holder.linQuiz.setBackgroundColor(Color.parseColor(colorActive));

            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");
            holder.quiz_title_txt.setTypeface(typeFace);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getFolder_name()));
        } catch (Exception e) {
            e.printStackTrace();
            String result=decodeUnicode(quizList.get(position).getFolder_name());
            holder.quiz_title_txt.setText(result);
//            String lastFourDigits = "";     //substring containing last 4 characters
//            String input = quizList.get(position).getFolder_name();     //substring containing last 4 characters
//
//            if (input.length() > 2)
//            {
//                lastFourDigits = input.substring(input.length() - 2);
//            }
//            else
//            {
//                lastFourDigits = input;
//            }
//            if(lastFourDigits.equalsIgnoreCase("u0")){
//                String result=input.substring(0, input.length() - 2);
//                holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(result));
//            }else {
//                holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getFolder_name()));
//            }
//            holder.quiz_title_txt.setText(quizList.get(position).getFolder_name());
//            unescapeJavaString(quizList.get(position).getFolder_name());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView quiz_title_txt;
        LinearLayout linQuiz;
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
