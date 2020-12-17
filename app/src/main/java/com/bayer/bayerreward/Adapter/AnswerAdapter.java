package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.QuizOptionList;
import com.bayer.bayerreward.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private List<QuizOptionList> quizSpecificOptionListnew;
    private List<Quiz> quizList;
    private Context context;
    String correctAns;
    String SelectedAns;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public AnswerAdapter(Context context, List<QuizOptionList> travelList, String correctAns, String SelectedAns) {
        this.quizSpecificOptionListnew = travelList;
        this.correctAns = correctAns;
        this.SelectedAns = SelectedAns;

        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_answers, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QuizOptionList optionList = quizSpecificOptionListnew.get(position);
        String option = optionList.getOptionId();


        try {
            if (correctAns.equalsIgnoreCase(option)) {
                if (correctAns.equalsIgnoreCase(SelectedAns)) {
                    holder.raiolayout.setBackgroundResource(R.drawable.quiz_correct);

                    holder.radio.setBackgroundResource(R.drawable.checked_radio);
                } else if (option.equalsIgnoreCase(correctAns)) {
                    holder.raiolayout.setBackgroundResource(R.drawable.quiz_correct);
                    holder.radio.setBackgroundResource(R.drawable.unchecked_radio);
                }
            } else if (option.equalsIgnoreCase(SelectedAns)) {

                holder.raiolayout.setBackgroundResource(R.drawable.quiz_wrong);
                holder.radio.setBackgroundResource(R.drawable.checked_radio);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.textno1.setText(decodeUnicode(optionList.getOption()));
    }

    @Override
    public int getItemCount() {
        return quizSpecificOptionListnew.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textno1;
        ImageView radio;
        LinearLayout raiolayout;

        public ViewHolder(View itemView) {
            super(itemView);


            textno1 = (TextView) itemView.findViewById(R.id.textno1);
            radio = (ImageView) itemView.findViewById(R.id.radio);
            raiolayout = (LinearLayout) itemView.findViewById(R.id.raiolayout);


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
