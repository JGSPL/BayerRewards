package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.procialize.singleevent.Fragments.GeneralInfo;

import com.procialize.singleevent.GetterSetter.InfoList;
import com.procialize.singleevent.R;


import java.util.List;


public class GeneralInfoListAdapter extends RecyclerView.Adapter<GeneralInfoListAdapter.MyViewHolder> {

    List<InfoList> infoList;
    Context context;
    GeneralInfoListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_info;
        public View layout;

        public MyViewHolder(View view) {
            super(view);
            layout = view;
            txt_info = view.findViewById(R.id.txt_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactSelected(infoList.get(getAdapterPosition()));

                }
            });

        }
    }


    public GeneralInfoListAdapter(Context context, List<InfoList> infoList, GeneralInfoListener listener) {
        this.context = context;
        this.infoList = infoList;
        this.listener = listener;
    }

    @Override
    public GeneralInfoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.general_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final InfoList infonew = infoList.get(position);

        holder.txt_info.setText(infonew.getName());


    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public interface GeneralInfoListener {
        void onContactSelected(InfoList firstLevelFilter);
    }
}