package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.EventSettingList;
import com.bayer.bayerreward.GetterSetter.QuestionSpeakerList;
import com.bayer.bayerreward.GetterSetter.SpeakerQuestionList;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class QASpeakerAdapter extends RecyclerView.Adapter<QASpeakerAdapter.MyViewHolder> {

    String token, message;
    String QA_like_question, QA_reply_question;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<SpeakerQuestionList> speakerQuestionLists;
    private List<QuestionSpeakerList> questionSpeakerLists;
    private Context context;
    private QASpeakerAdapterListner listener;
    private String speakername;
    private APIService mAPIService;

    public QASpeakerAdapter(Context context, List<SpeakerQuestionList> speakerQuestionLists, List<QuestionSpeakerList> questionSpeakerLists, QASpeakerAdapterListner listener, String speakername) {
        this.speakerQuestionLists = speakerQuestionLists;
        this.questionSpeakerLists = questionSpeakerLists;
        this.listener = listener;
        this.context = context;
        this.speakername = speakername;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qarow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SpeakerQuestionList question = speakerQuestionLists.get(position);
        holder.nameTv.setTextColor(Color.parseColor(colorActive));

        holder.nameTv.setText(question.getFirstName() + " " + question.getLastName());
        holder.QaTv.setText(StringEscapeUtils.unescapeJava(question.getQuestion()));


        if (question.getAnswer() != null && QA_reply_question.equalsIgnoreCase("1")) {
            if (!question.getAnswer().equalsIgnoreCase("null")) {
                holder.AnsTv.setText("Ans :- " + StringEscapeUtils.unescapeJava(question.getAnswer()));
            } else {
                holder.AnsTv.setVisibility(View.GONE);
            }
        }

        holder.countTv.setText(question.getTotalLikes() + " Likes");

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);


        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }


        SimpleDateFormat formatter = null;

        String formate1 = ApiConstant.dateformat;
        String formate2 = ApiConstant.dateformat1;

        if (Utility.isValidFormat(formate1, question.getCreated(), Locale.UK)) {
            formatter = new SimpleDateFormat(ApiConstant.dateformat);
        } else if (Utility.isValidFormat(formate2, question.getCreated(), Locale.UK)) {
            formatter = new SimpleDateFormat(ApiConstant.dateformat1);
        }
        try {
            Date date1 = formatter.parse(question.getCreated());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (QA_like_question.equalsIgnoreCase("1")) {
            holder.likeLL.setVisibility(View.VISIBLE);
        } else {
            holder.likeLL.setVisibility(View.GONE);

        }


        if (question.getLikeFlag().equals("0")) {
            holder.likeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
        } else {
            holder.likeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_afterlike));
            holder.likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);


        }


    }

    @Override
    public int getItemCount() {
        return speakerQuestionLists.size();
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("Q&A_like_question")) {
                QA_like_question = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_reply_question")) {
                QA_reply_question = eventSettingLists.get(i).getFieldValue();
            }
        }
    }

    public interface QASpeakerAdapterListner {
        void onContactSelected(SpeakerQuestionList question);

        void onLikeListener(View v, SpeakerQuestionList question, int position, TextView count, ImageView likeIv);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv, QaTv, AnsTv, countTv;
        public ImageView likeIv;
        LinearLayout likeLL;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            dateTv = view.findViewById(R.id.dateTv);
            QaTv = view.findViewById(R.id.QaTv);
            AnsTv = view.findViewById(R.id.AnsTv);
            countTv = view.findViewById(R.id.countTv);
            likeLL = view.findViewById(R.id.likeLL);

            likeIv = view.findViewById(R.id.likeIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(speakerQuestionLists.get(getAdapterPosition()));
                }
            });

            likeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeListener(v, speakerQuestionLists.get(getAdapterPosition()), getAdapterPosition(), countTv, likeIv);
                }
            });
        }
    }
}