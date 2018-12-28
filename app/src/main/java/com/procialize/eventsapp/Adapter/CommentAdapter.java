package com.procialize.eventsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.procialize.eventsapp.ApiConstant.ApiConstant;
import com.procialize.eventsapp.GetterSetter.CommentDataList;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Naushad on 10/31/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public List<CommentDataList> commentLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    String flag;
    private Context context;
    private CommentAdapterListner listener;


    public CommentAdapter(Context context, List<CommentDataList> commentLists, CommentAdapterListner listener, String flag) {
        this.commentLists = commentLists;
        this.listener = listener;
        this.context = context;
        this.flag = flag;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentlistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CommentDataList comment = commentLists.get(position);
        holder.nameTv.setTextColor(Color.parseColor(colorActive));

//        if (flag.equalsIgnoreCase("Notification")) {
//            holder.moreIv.setVisibility(View.GONE);
//        } else {
//            holder.moreIv.setVisibility(View.VISIBLE);
//        }

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        holder.nameTv.setText(comment.getFirstName() + " " + comment.getLastName());

        try {
//            Date date1 = formatter.parse(comment.getCreated());
//
//            DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
//
//            String date = originalFormat.format(date1);
//
//            holder.dateTv.setText(date);


            SimpleDateFormat formatter = null;

            String formate1 = ApiConstant.dateformat;
            String formate2 = ApiConstant.dateformat1;

            if (Utility.isValidFormat(formate1, comment.getCreated(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat);
            } else if (Utility.isValidFormat(formate2, comment.getCreated(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat1);
            }

            Date date1 = formatter.parse(comment.getCreated());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);


            Log.e("date", commentLists.size() + "");

        } catch (ParseException e) {
            e.printStackTrace();
        }
//


        if (comment.getProfilePic() != null) {
            Glide.with(context).load(ApiConstant.profilepic + comment.getProfilePic())
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
        } else {
            holder.progressView.setVisibility(View.GONE);

        }

        if (comment.getComment().contains("gif")) {
            holder.gifIV.setVisibility(View.VISIBLE);
//            holder.commentTv.setVisibility(View.GONE);
            holder.progressViewgif.setVisibility(View.VISIBLE);
            holder.commentTv.setText("GIF");
            Glide.with(context)
                    .load(commentLists.get(position).getComment())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressViewgif.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressViewgif.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.gifIV);


        } else {
            holder.progressViewgif.setVisibility(View.GONE);
            holder.commentTv.setVisibility(View.VISIBLE);
            holder.gifIV.setVisibility(View.GONE);
            holder.commentTv.setText(StringEscapeUtils.unescapeJava(comment.getComment()));

        }
    }

    @Override
    public int getItemCount() {
        return commentLists.size();
    }

    public interface CommentAdapterListner {

        void onContactSelected(CommentDataList comment);

        void onMoreSelected(CommentDataList comment, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, commentTv, dateTv;
        public ImageView profileIv;
        public ProgressBar progressView, progressViewgif;
        public ImageView moreIv;
        public LinearLayout textcommentContainer;
        public ImageView gifIV;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            commentTv = view.findViewById(R.id.commentTv);
            dateTv = view.findViewById(R.id.dateTv);

            profileIv = view.findViewById(R.id.profileIV);
            gifIV = view.findViewById(R.id.gifIV);

            moreIv = view.findViewById(R.id.moreIv);

            progressView = view.findViewById(R.id.progressView);
            progressViewgif = view.findViewById(R.id.progressViewgif);
            textcommentContainer = view.findViewById(R.id.textcommentContainer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(commentLists.get(getAdapterPosition()));
                }
            });

            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreSelected(commentLists.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }
}