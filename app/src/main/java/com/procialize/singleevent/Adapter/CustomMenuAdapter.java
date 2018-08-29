package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.GetterSetter.AttendeeList;
import com.procialize.singleevent.GetterSetter.EventMenuSettingList;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Naushad on 10/31/2017.
 */

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.MyViewHolder> {

    private List<EventMenuSettingList> eventMenuSettingLists;
    private Context context;
    private CustomMenuAdapterListner listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designationTv;
        public ImageView profileIv;
        public LinearLayout mainLL;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            locationTv = view.findViewById(R.id.locationTv);
            designationTv = view.findViewById(R.id.designationTv);

            profileIv = view.findViewById(R.id.profileIV);

            mainLL = view.findViewById(R.id.mainLL);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(eventMenuSettingLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public CustomMenuAdapter(Context context, List<EventMenuSettingList> eventMenuSettingLists, CustomMenuAdapterListner listener) {
        this.eventMenuSettingLists = eventMenuSettingLists;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menulistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        EventMenuSettingList menuSettingList = eventMenuSettingLists.get(position);

//        if (menuSettingList.getFieldValue().equalsIgnoreCase("0")) {
//            holder.nameTv.setVisibility(View.GONE);
//            holder.profileIv.setVisibility(View.GONE);
//            holder.mainLL.setVisibility(View.GONE);
//        } else {
//
//            holder.nameTv.setVisibility(View.VISIBLE);
//            holder.profileIv.setVisibility(View.VISIBLE);
//            holder.mainLL.setVisibility(View.VISIBLE);
//        }


//        if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu")) {
//            holder.mainLL.setVisibility(View.GONE);
//        } else {
//            holder.mainLL.setVisibility(View.VISIBLE);
//        }

        if (menuSettingList.getFieldValue().equalsIgnoreCase("0")) {

            holder.nameTv.setVisibility(View.GONE);
            holder.profileIv.setVisibility(View.GONE);
            holder.mainLL.setVisibility(View.GONE);
        } else {
            if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu")) {
                holder.nameTv.setVisibility(View.GONE);
                holder.profileIv.setVisibility(View.GONE);
                holder.mainLL.setVisibility(View.GONE);
            } else {
                holder.nameTv.setVisibility(View.VISIBLE);
                holder.profileIv.setVisibility(View.VISIBLE);
                holder.mainLL.setVisibility(View.VISIBLE);
            }
        }

        if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_my_travel")) {
            holder.nameTv.setText("My Travel");
            holder.profileIv.setImageResource(R.drawable.ic_travel);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_notification")) {
            holder.nameTv.setText("Notification");
            holder.profileIv.setImageResource(R.drawable.ic_notification);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_display_qr")) {
            holder.nameTv.setText("Qr Badge");
            holder.profileIv.setImageResource(R.drawable.qr_badge);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_qr_scanner")) {
            holder.nameTv.setText("Qr Scanner");
            holder.profileIv.setImageResource(R.drawable.ic_scan);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_quiz")) {
            holder.nameTv.setText("Quiz");
            holder.profileIv.setImageResource(R.drawable.ic_agenda);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_live_poll")) {
            holder.nameTv.setText("Live Poll");
            holder.profileIv.setImageResource(R.drawable.live_poll);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_survey")) {
            holder.nameTv.setText("Survey");
            holder.profileIv.setImageResource(R.drawable.survey);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_feedback")) {
            holder.nameTv.setText("Feedback");
            holder.profileIv.setImageResource(R.drawable.survey);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gallery_video")) {
            holder.nameTv.setText("Video");
            holder.profileIv.setImageResource(R.drawable.ic_videos);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_image_gallery")) {
            holder.nameTv.setText("Gallery");
            holder.profileIv.setImageResource(R.drawable.ic_gallery);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_document")) {
            holder.nameTv.setText("Documents");
            holder.profileIv.setImageResource(R.drawable.ic_documents);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_event_info")) {
            holder.nameTv.setText("Event Info");
            holder.profileIv.setImageResource(R.drawable.ic_info);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_q&a")) {
            holder.nameTv.setText("Q & A");
            holder.profileIv.setImageResource(R.drawable.ic_question);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_engagement")) {
            holder.nameTv.setText("Engagement");
            holder.profileIv.setImageResource(R.drawable.engagement);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_attendee")) {
            holder.nameTv.setText("Attendee");
            holder.profileIv.setImageResource(R.drawable.ic_attendee);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_speaker")) {
            holder.nameTv.setText("Speaker");
            holder.profileIv.setImageResource(R.drawable.ic_speaker);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {
            holder.nameTv.setText("Agenda");
            holder.profileIv.setImageResource(R.drawable.ic_agenda);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gen_info")) {
            holder.nameTv.setText("General Info");
            holder.profileIv.setImageResource(R.drawable.general_info);
        }


    }

    @Override
    public int getItemCount() {
        return eventMenuSettingLists.size();
    }


    public interface CustomMenuAdapterListner {
        void onContactSelected(EventMenuSettingList eventMenuSettingList);
    }
}