package com.procialize.vivo_app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.procialize.vivo_app.GetterSetter.LivePollList;
import com.procialize.vivo_app.GetterSetter.LivePollOptionList;
import com.procialize.vivo_app.R;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.MyViewHolder> {

    private List<LivePollList> pollLists;
    private List<LivePollOptionList> optionLists;
    private Context context;
    private PollAdapterListner listener;

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
                    listener.onContactSelected(pollLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public PollAdapter(Context context, List<LivePollList> pollLists,List<LivePollOptionList> optionLists, PollAdapterListner listener) {
        this.pollLists = pollLists;
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
        final LivePollList pollList = pollLists.get(position);



        holder.nameTv.setText(pollList.getQuestion());

    }

    @Override
    public int getItemCount() {
        return pollLists.size();
    }

    public interface PollAdapterListner {
        void onContactSelected(LivePollList pollList);
    }
}