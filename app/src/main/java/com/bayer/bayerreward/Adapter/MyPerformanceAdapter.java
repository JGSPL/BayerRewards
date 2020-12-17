package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayer.bayerreward.GetterSetter.product_achived_planned;
import com.bayer.bayerreward.R;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyPerformanceAdapter extends RecyclerView.Adapter<MyPerformanceAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<product_achived_planned> travelLists;
    private Context context;


    public MyPerformanceAdapter(Context context, List<product_achived_planned> travelList) {
        this.travelLists = travelList;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyPerformanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myperformance_item, parent, false);

        return new MyPerformanceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyPerformanceAdapter.MyViewHolder holder, int position) {
        final product_achived_planned travel = travelLists.get(position);

        holder.txt_product.setText(travel.getProduct_name());
        holder.txt_plannvol.setText(travel.getPlanned_volume());
        holder.txt_achivedvol.setText(travel.getAchived_volume());

        Double total = (Double.parseDouble(travel.getAchived_volume()) / Double.parseDouble(travel.getPlanned_volume())) * 100;


        if (total.isInfinite()) {
            holder.txt_total.setText("0 %");
        } else {
            holder.txt_total.setText(new DecimalFormat("##.##").format(total) + "%");
        }


    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_product, txt_plannvol, txt_achivedvol, txt_total;


        public MyViewHolder(View view) {
            super(view);
            txt_product = view.findViewById(R.id.txt_product);
            txt_plannvol = view.findViewById(R.id.txt_plannvol);
            txt_achivedvol = view.findViewById(R.id.txt_achivedvol);

            txt_total = view.findViewById(R.id.txt_total);

        }
    }
}
