package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayer.bayerreward.GetterSetter.TravelList;
import com.bayer.bayerreward.GetterSetter.account_history_data;
import com.bayer.bayerreward.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DialogHistoryAdapter extends RecyclerView.Adapter<DialogHistoryAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<account_history_data> travelLists;
    private Context context;
    private DialogHistoryAdapter.DialogHistoryAdapterListner listener;

    public DialogHistoryAdapter(Context context, List<account_history_data> travelList) {
        this.travelLists = travelList;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public DialogHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        return new DialogHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DialogHistoryAdapter.MyViewHolder holder, int position) {
        final account_history_data travel = travelLists.get(position);

        holder.txt_date.setText(travel.getProduct_name());
        holder.txt_mpins.setText(travel.getPack());
        holder.txt_currency.setText(travel.getM_points_value());
    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }

    public interface DialogHistoryAdapterListner {
        void onContactSelected(TravelList travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date, txt_mpins, txt_currency;


        public MyViewHolder(View view) {
            super(view);
            txt_date = view.findViewById(R.id.txt_date);
            txt_mpins = view.findViewById(R.id.txt_mpins);
            txt_currency = view.findViewById(R.id.txt_currency);


        }
    }
}