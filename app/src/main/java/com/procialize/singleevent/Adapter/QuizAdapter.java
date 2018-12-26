package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.procialize.singleevent.GetterSetter.QuizList;
import com.procialize.singleevent.GetterSetter.QuizOptionList;
import com.procialize.singleevent.R;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    private List<QuizList> quikzLists;
    private List<QuizOptionList> optionLists;
    private Context context;
    private QuizAdapterListner listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;

        public MyViewHolder(View view) {
            super(view);
            nameTv =  view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(quikzLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public QuizAdapter(Context context, List<QuizList> quikzLists, List<QuizOptionList> optionLists, QuizAdapterListner listener) {
        this.quikzLists = quikzLists;
        this.optionLists = optionLists;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final QuizList quizList = quikzLists.get(position);

        holder.nameTv.setText(quizList.getQuestion());

    }

    @Override
    public int getItemCount() {
        return quikzLists.size();
    }

    public interface QuizAdapterListner {
        void onContactSelected(QuizList quizList);
    }
}