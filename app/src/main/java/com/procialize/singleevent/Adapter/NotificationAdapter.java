package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * Created by Naushad on 10/31/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationList> notificationLists;
    private Context context;
    private NotificationAdapterListner listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv,dataTv,dateTv;
        public ImageView profileIv;
        Button replyBtn;
        ImageView arrowIv;
        private ProgressBar progressView;

        public MyViewHolder(View view) {
            super(view);
            nameTv =  view.findViewById(R.id.nameTv);
            dataTv =  view.findViewById(R.id.dataTv);
            dateTv =  view.findViewById(R.id.dateTv);

            replyBtn =  view.findViewById(R.id.replyBtn);

            arrowIv =  view.findViewById(R.id.arrowIv);

            profileIv = view.findViewById(R.id.profileIV);

            progressView = view.findViewById(R.id.progressView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(notificationLists.get(getAdapterPosition()));
                }
            });

            replyBtn.setOnClickListener(new View.OnClickListener() {
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

        holder.nameTv.setText(notificationList.getAttendeeFirstName()+" "+notificationList.getAttendeeLastName());

        holder.dataTv.setText(StringEscapeUtils.unescapeJava(notificationList.getNotificationContent()));


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(notificationList.getNotificationDate());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM , yyyy KK:mm", Locale.ENGLISH);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);

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
            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
        }else if (notificationList.getNotificationType().equalsIgnoreCase("Like"))
        {
            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
        }else  if (notificationList.getNotificationType().equalsIgnoreCase("Msg"))
        {
            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.GONE);
        }else {
            holder.replyBtn.setVisibility(View.GONE);
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