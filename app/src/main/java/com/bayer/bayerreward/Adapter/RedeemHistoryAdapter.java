package com.bayer.bayerreward.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bayer.bayerreward.GetterSetter.redemption_data;
import com.bayer.bayerreward.GetterSetter.redemption_status;
import com.bayer.bayerreward.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class RedeemHistoryAdapter extends RecyclerView.Adapter<RedeemHistoryAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    String sessiondate;
    private List<redemption_data> redemption_datas;
    private List<redemption_status> redemption_status = new ArrayList<>();
    private List<redemption_status> list_redem = new ArrayList<>();
    private List<redemption_status> demostatus = new ArrayList<>();
    private redemption_status redem;
    private Context context;
    private RedeemHistoryAdapter.RedeemHistoryAdapterListner listener;
    Dialog myDialog;

    public RedeemHistoryAdapter(Context context, List<redemption_data> redemption_data, List<redemption_status> redemption_status) {
        this.redemption_datas = redemption_data;
        this.redemption_status = redemption_status;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        redem = new redemption_status();

    }

    @Override
    public RedeemHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.redeem_history_item, parent, false);

        return new RedeemHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RedeemHistoryAdapter.MyViewHolder holder, final int position) {
        final redemption_data travel = redemption_datas.get(position);

        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat(" dd/MM/yyyy");
            Date date1 = originalFormat.parse(travel.getRedemption_date());
            sessiondate = targetFormat.format(date1);
//            holder.date.setText(sessiondate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.date.setText(travel.getRequest_id());
        holder.catlogname.setText(travel.getProduct_name());
        holder.txt_qnty.setText(travel.getQuantity());
        holder.txt_point.setText(travel.getPoints());
//        holder.txt_status.setText(travel.getLast_status());
        demostatus.clear();
        if (redemption_status.size() > 0) {
            for (int i = 0; i < redemption_status.size(); i++) {
                if (redemption_status.get(i).getRequest_id().toString().equalsIgnoreCase(travel.getRequest_id().toString())) {
                    demostatus.add(redemption_status.get(i));
                }
            }
        }
        if (demostatus.size() == 0) {
            holder.txt_status.setText(travel.getLast_status());
        } else {
            holder.txt_status.setText(demostatus.get(0).getStatus());
        }

        if (travel.getLast_status() != null) {

            if (travel.getLast_status().equalsIgnoreCase("Requested")) {
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.activetab));
            } else if (travel.getLast_status().equalsIgnoreCase("Completed")) {
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.loginbutton));
            }
        }


        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_redem.clear();
                showratedialouge(redemption_status, travel.getRequest_id(), travel.getProduct_name(), travel.getQuantity(), travel.getPoints(), travel.getRedemption_date(), holder.txt_status.getText().toString());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return redemption_datas.size();
    }

    public interface RedeemHistoryAdapterListner {
        void onContactSelected(redemption_status travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_status, txt_point, txt_qnty, catlogname, date;
        RelativeLayout relative;


        public MyViewHolder(View view) {
            super(view);
            txt_status = view.findViewById(R.id.txt_status);
            txt_point = view.findViewById(R.id.txt_point);
            txt_qnty = view.findViewById(R.id.txt_qnty);
            date = view.findViewById(R.id.date);

            catlogname = view.findViewById(R.id.catlogname);
            relative = view.findViewById(R.id.relative);


        }
    }

    private void showratedialouge(List<redemption_status> travel, String request_id, String prodname, String qty, String points, String date, String status) {

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.statusdialog);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();

        ImageView img_cancel = myDialog.findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.cancel();
            }
        });


        TextView txt_point = myDialog.findViewById(R.id.txt_point);
        TextView txt_qty = myDialog.findViewById(R.id.txt_qty);
        TextView txt_catlogname = myDialog.findViewById(R.id.txt_catlogname);
        TextView txt_rwqdate = myDialog.findViewById(R.id.txt_rwqdate);
        TextView txt_rwid = myDialog.findViewById(R.id.txt_rwid);
        RecyclerView recycler_staus = myDialog.findViewById(R.id.recycler_staus);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_staus.setLayoutManager(mLayoutManager);
//        txt_rwid.setText("Request Date:" + date);
        txt_rwqdate.setText("Request Id:" + request_id);
        txt_catlogname.setText("Catlog Name:" + prodname);
        txt_qty.setText("Qty:" + qty);
        txt_point.setText("Points:" + points);
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat(" dd/MM/yyyy");
            Date date1 = originalFormat.parse(date);
            sessiondate = targetFormat.format(date1);
            txt_rwid.setText("Request Date:" + sessiondate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (travel.size() > 0) {
            for (int i = 0; i < travel.size(); i++) {
                if (travel.get(i).getRequest_id().equalsIgnoreCase(request_id)) {
                    list_redem.add(travel.get(i));
                }
            }

            if (list_redem.size() > 0) {
                redem.setRequest_id(request_id);
                redem.setStatus("Request Placed");
                redem.setStatus_changed_date(date);
            } else {
                redem.setRequest_id(request_id);
                redem.setStatus(status);
                redem.setStatus_changed_date(date);
            }

            redemption_status remst = new redemption_status(redem.getRequest_id(), redem.getStatus(), redem.getStatus_changed_date());
            list_redem.add(list_redem.size(), remst);


            StatusAdapter adapter = new StatusAdapter(context, list_redem);
            adapter.notifyDataSetChanged();
            recycler_staus.setAdapter(adapter);
        }


    }

}
