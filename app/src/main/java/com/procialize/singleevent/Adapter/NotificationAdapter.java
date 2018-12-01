package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.GetterSetter.NotificationList;
import com.procialize.singleevent.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.//preeti
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationList> notificationLists;
    private Context context;
    private NotificationAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv,dataTv,messageTV,txt_msg;
        public ImageView profileIv;
        Button replyBtn;
        ImageView arrowIv,ivtype,arrowIvmsg;
        private ProgressBar progressView;
        LinearLayout notiLin;

        public MyViewHolder(View view) {
            super(view);
            nameTv =  view.findViewById(R.id.nameTv);
            dataTv =  view.findViewById(R.id.dataTv);
            messageTV =  view.findViewById(R.id.messageTV);
            txt_msg = view.findViewById(R.id.txt_msg);

            replyBtn =  view.findViewById(R.id.replyBtn);

            arrowIv =  view.findViewById(R.id.arrowIv);
            arrowIvmsg= view.findViewById(R.id.arrowIvmsg);
            ivtype =  view.findViewById(R.id.ivtype);

            profileIv = view.findViewById(R.id.profileIV);

            progressView = view.findViewById(R.id.progressView);
            notiLin = view.findViewById(R.id.notiLin);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(notificationLists.get(getAdapterPosition()));
                }
            });

            arrowIvmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReplyClick(notificationLists.get(getAdapterPosition()));
                }
            });
        }
    }
    public NotificationAdapter(Context context, List<NotificationList> notificationLists, NotificationAdapterListner listener) {
        this.notificationLists = notificationLists;
        this.listener=listener;
        this.context=context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive","");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotificationList notificationList = notificationLists.get(position);

        holder.notiLin.setBackgroundColor(Color.parseColor(colorActive));
        holder.nameTv.setTextColor(Color.parseColor(colorActive));

        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.arrowIv.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.arrowIv.setImageDrawable(drawable);

        Drawable drawable1 = DrawableCompat.wrap(holder.arrowIv.getDrawable());
        DrawableCompat.setTintList(drawable1, csl);
        holder.arrowIvmsg.setImageDrawable(drawable1);



        holder.messageTV.setText(StringEscapeUtils.unescapeJava(notificationList.getNotificationContent()));

        if (notificationList.getNotificationType().equalsIgnoreCase("Msg")) {
            holder.txt_msg.setText("Sent You Message");
            holder.nameTv.setText(notificationList.getAttendeeFirstName()+" "+notificationList.getAttendeeLastName());

        } else if (notificationList.getNotificationType().equalsIgnoreCase("Like")) {
            holder.txt_msg.setText("Liked Your Post");
            holder.nameTv.setText(notificationList.getAttendeeFirstName()+" "+notificationList.getAttendeeLastName());

        } else if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt")) {
            holder.txt_msg.setText("Commented On Your Post");
            holder.nameTv.setText(notificationList.getAttendeeFirstName()+" "+notificationList.getAttendeeLastName());

        }else{
            holder.nameTv.setText(notificationList.getAttendeeFirstName()+" "+notificationList.getAttendeeLastName());

        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(notificationList.getNotificationDate());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM , yyyy KK:mm", Locale.ENGLISH);

            String date = originalFormat.format(date1);

            holder.dataTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (notificationList.getProfilePic() != null) {

            Glide.with(context).load(ApiConstant.profilepic + notificationList.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIv).onLoadStarted(context.getDrawable(R.drawable.profilepic_placeholder));

        }else
        {
            holder.progressView.setVisibility(View.GONE);

        }

        if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt"))
        {
//            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.ivtype.setImageResource(R.drawable.notifycoment);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        }else if (notificationList.getNotificationType().equalsIgnoreCase("Like"))
        {
//            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);

            holder.arrowIvmsg.setVisibility(View.GONE);

            holder.ivtype.setImageResource(R.drawable.notifylike);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        }else  if (notificationList.getNotificationType().equalsIgnoreCase("Msg"))
        {
//            holder.replyBtn.setVisibility(View.VISIBLE);
            //holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.VISIBLE);

            holder.ivtype.setImageResource(R.drawable.notifymessage);
            holder.arrowIv.setVisibility(View.GONE);
            holder.arrowIvmsg.setImageResource(R.drawable.messageiv);


        }else {
//            holder.replyBtn.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.GONE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.arrowIvmsg.setVisibility(View.GONE);


            holder.ivtype.setImageResource(R.drawable.notifyadmin);
            holder.arrowIv.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public interface NotificationAdapterListner {
        void onContactSelected(NotificationList notification);

        void onReplyClick(NotificationList notification);
    }
}