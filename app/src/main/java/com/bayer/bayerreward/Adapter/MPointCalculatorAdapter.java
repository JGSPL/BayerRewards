package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.MpointCalc;
import com.bayer.bayerreward.GetterSetter.m_points_list;
import com.bayer.bayerreward.R;

import java.util.List;

public class MPointCalculatorAdapter extends RecyclerView.Adapter<MPointCalculatorAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<m_points_list> travelLists;
    private Context context;
    Long mpoints, points, TotalPoints;
    Long points1;
    String str;

    public MPointCalculatorAdapter(Context context, List<m_points_list> travelList) {
        this.travelLists = travelList;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpointcalcitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final m_points_list travel = travelLists.get(position);

        holder.txt_product.setText(travel.getProduct_name());
        holder.txt_packsize.setText(travel.getPack());
        holder.mpointspermpin.setText(travel.getPoints());


        holder.txt_noofmpin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace
                    holder.txt_noofmpin.getText().clear();
                }
                return false;
            }
        });

        holder.txt_noofmpin.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    try {

                        if (holder.txt_noofmpin.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Please Enter Value", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            points = Long.parseLong(holder.mpointspermpin.getText().toString().trim());
                            mpoints = Long.parseLong(holder.txt_noofmpin.getText().toString().trim());

                            TotalPoints = points * mpoints;
                            String s = String.valueOf(TotalPoints);
                            str = s.replace(".", "");

//                            Integer result = Integer.valueOf(str) / 10;
                            holder.txt_total.setText(String.valueOf(str));

                            points1 = Long.parseLong(holder.txt_noofmpin.getText().toString().trim());
                            String txt_score = MpointCalc.txt_score.getText().toString().trim();
                            String txt_total = holder.txt_total.getText().toString().trim();

                            if (txt_score.equalsIgnoreCase("")) {
//                                txt_score = "0";
//                                Float mPointtotal = Float.valueOf(txt_score) + Float.valueOf(txt_total);
                                String s1 = String.valueOf(txt_total);
                                String res = s1.replace(".", "");
                                MpointCalc.txt_score.setText(res);
                            } else {
                                Long mPointtotal = Long.parseLong(txt_score) + Long.parseLong(txt_total);
                                String s1 = String.valueOf(mPointtotal);
                                String res = s1.replace(".", "");
                                MpointCalc.txt_score.setText(res);
                            }
                            holder.txt_noofmpin.setFocusable(false);
                            holder.txt_noofmpin.setEnabled(false);
                            return true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_product, txt_packsize, mpointspermpin, txt_total;
        EditText txt_noofmpin;


        public MyViewHolder(View view) {
            super(view);
            txt_product = view.findViewById(R.id.txt_product);
            txt_packsize = view.findViewById(R.id.txt_packsize);
            mpointspermpin = view.findViewById(R.id.mpointspermpin);
            txt_total = view.findViewById(R.id.txt_total);

            txt_noofmpin = view.findViewById(R.id.txt_noofmpin);


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
}
