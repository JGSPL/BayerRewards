package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayer.bayerreward.GetterSetter.redemption_status;
import com.bayer.bayerreward.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<redemption_status> territory_datas;
    private Context context;


    public StatusAdapter(Context context, List<redemption_status> territory_data) {
        this.territory_datas = territory_data;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public StatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_item, parent, false);

        return new StatusAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StatusAdapter.MyViewHolder holder, int position) {
        final redemption_status travel = territory_datas.get(position);


        holder.txt_title.setText("Status ");
        holder.txt_status.setText(travel.getStatus());


    }

    @Override
    public int getItemCount() {

        return territory_datas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_status, txt_title;


        public MyViewHolder(View view) {
            super(view);
            txt_status = view.findViewById(R.id.txt_status);
            txt_title = view.findViewById(R.id.txt_title);


        }
    }
}
