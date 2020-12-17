package com.bayer.bayerreward.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.account_history_data;
import com.bayer.bayerreward.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccountHistoryAdapter extends RecyclerView.Adapter<AccountHistoryAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    String total_points;
    private List<account_history_data> travelLists;
    private Context context;
    int points1;
    private List<account_history_data> histrorylist = new ArrayList<>();
    private List<account_history_data> histrorylist1 = new ArrayList<>();
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    Dialog myDialog;
    int calculatedpoint = 0;
    int calculatecr = 0;

    public AccountHistoryAdapter(Context context, List<account_history_data> travelList, String total_points) {
        this.travelLists = travelList;
        this.total_points = total_points;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

        dbHelper = new DBHelper(context);

        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
        db = procializeDB.getReadableDatabase();


    }

    @Override
    public AccountHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_accounthistory, parent, false);

        return new AccountHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AccountHistoryAdapter.MyViewHolder holder, int position) {
        final account_history_data travel = travelLists.get(position);



        holder.txt_date.setText(travel.getTransaction_date());

        histrorylist = dbHelper.getAccHistoryDetail(travel.getTransaction_date());
        int mpin = 0, crdr = 0;
        try {
            for (int i = 0; i <= histrorylist.size(); i++) {
                mpin = mpin + Integer.parseInt(histrorylist.get(i).getM_points_value());
                crdr = crdr + Integer.parseInt(histrorylist.get(i).getPack());
            }
        } catch (Exception e) {

        }
        histrorylist1 = dbHelper.getAccHistoryDetail(travel.getTransaction_date());
        holder.txt_mpins.setText(String.valueOf(histrorylist1.size()));
        try {
            for (int i = 0; i <= histrorylist.size(); i++) {
                crdr = crdr + Integer.parseInt(histrorylist.get(i).getM_points_value());
            }
        } catch (Exception e) {

        }
        holder.txt_currency.setText(String.valueOf(crdr));
        if (position == 0) {
            holder.txt_balance.setText(total_points);
            calculatedpoint = Integer.parseInt(total_points);
            calculatecr = crdr;
        } else {
            int point = calculatedpoint - calculatecr;
            holder.txt_balance.setText(String.valueOf(point));
            calculatedpoint = Integer.parseInt(holder.txt_balance.getText().toString());
            calculatecr = crdr;
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showratedialouge(travel.getTransaction_date());
            }
        });


    }


    @Override
    public int getItemCount() {
        return travelLists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_balance,
                txt_currency,
                txt_mpins,
                txt_date;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);
            txt_balance = view.findViewById(R.id.txt_balance);
            txt_currency = view.findViewById(R.id.txt_currency);
            txt_mpins = view.findViewById(R.id.txt_mpins);
            txt_date = view.findViewById(R.id.txt_date);
            linear = view.findViewById(R.id.linear);


        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void showratedialouge(final String date) {

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.acchistory_dialog);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();


        TextView txt_date = myDialog.findViewById(R.id.txt_date);
        ImageView img_cancel = myDialog.findViewById(R.id.img_cancel);
        RecyclerView recycler_history = myDialog.findViewById(R.id.recycler_history);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_history.setLayoutManager(mLayoutManager);
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = originalFormat.parse(date);
            String sessiondate = targetFormat.format(date1);
            txt_date.setText(sessiondate);

        } catch (Exception e) {
            e.printStackTrace();
        }


        histrorylist1 = dbHelper.getAccHistoryDetail(date);

        DialogHistoryAdapter adapter = new DialogHistoryAdapter(context, histrorylist1);
        adapter.notifyDataSetChanged();
        recycler_history.setAdapter(adapter);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.cancel();
            }
        });

    }
}
