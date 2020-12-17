package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.GetterSetter.region_data;
import com.bayer.bayerreward.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<region_data> region_datas;
    private Context context;
    private RegionAdapter.RegionAdapterListner listener;

    public RegionAdapter(Context context, List<region_data> region_data) {
        this.region_datas = region_data;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public RegionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.territory_items, parent, false);

        return new RegionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RegionAdapter.MyViewHolder holder, int position) {
        final region_data travel = region_datas.get(position);

        holder.txt_name.setText(travel.getShop_name());
        holder.txt_place.setText(travel.getRegion());
        int rank=position+1;
        holder.txt_rank.setText("Rank : "+rank);
        holder.txt_points.setText(travel.getTotal_points());

        Glide.with(context).load(ApiConstant.profilepic + travel.getProfile_pic()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.img_prof.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(holder.img_prof);


    }

    @Override
    public int getItemCount() {
        return region_datas.size();
    }

    public interface RegionAdapterListner {
        void onContactSelected(region_data travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_place, txt_rank,txt_points;
        ImageView img_prof, imgTvel;
        LinearLayout linTicket;


        public MyViewHolder(View view) {
            super(view);
            txt_name = view.findViewById(R.id.txt_name);
            txt_place = view.findViewById(R.id.txt_place);
            txt_rank = view.findViewById(R.id.txt_rank);
            txt_points = view.findViewById(R.id.txt_points);

            img_prof = view.findViewById(R.id.img_prof);


        }
    }
}
