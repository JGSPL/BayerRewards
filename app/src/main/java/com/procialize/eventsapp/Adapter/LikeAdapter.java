package com.procialize.eventsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventsapp.ApiConstant.ApiConstant;
import com.procialize.eventsapp.GetterSetter.AttendeeList;
import com.procialize.eventsapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sneha on 10/31/2017.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.MyViewHolder> {

    //    private LikeAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<AttendeeList> attendeeListList;
    private Context context;


    public LikeAdapter(Context context, List<AttendeeList> attendeeListList) {
        this.attendeeListList = attendeeListList;
//        this.listener=listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.likelist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AttendeeList attendeeList = attendeeListList.get(position);

        if (attendeeListList.get(position).getProfilePic() != null) {
            Glide.with(context).load(ApiConstant.profilepic + attendeeListList.get(position).getProfilePic()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.imageIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.imageIv);
        } else {
            holder.imageIv.setImageResource(R.drawable.profilepic_placeholder);
        }

        holder.nameTv.setTextColor(Color.parseColor(colorActive));

        holder.nameTv.setText(attendeeList.getFirstName() + " " + attendeeList.getLastName());

    }

    @Override
    public int getItemCount() {
        return attendeeListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);


        }
    }


}