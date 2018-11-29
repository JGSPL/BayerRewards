package com.procialize.singleevent.Adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procialize.singleevent.GetterSetter.SurveyList;
import com.procialize.singleevent.R;

import java.util.List;


/**
 * Created by Naushad on 10/31/2017.
 */

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.MyViewHolder> {

    private List<SurveyList> surveyLists;
    private Context context;
    private FeedBackAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.nameTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(surveyLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public FeedBackAdapter(Context context, List<SurveyList> speakerList,FeedBackAdapterListner listener) {
        this.surveyLists = speakerList;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedbackrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SurveyList survey = surveyLists.get(position);
        holder.nameTv.setText(survey.getName());

    }

    @Override
    public int getItemCount() {
        return surveyLists.size();
    }

    public interface FeedBackAdapterListner {
        void onContactSelected(SurveyList survey);
    }
}