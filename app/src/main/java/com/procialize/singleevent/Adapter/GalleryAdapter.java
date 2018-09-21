package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Activity.AttendeeDetailActivity;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.GetterSetter.FirstLevelFilter;
import com.procialize.singleevent.GetterSetter.FolderList;
import com.procialize.singleevent.GetterSetter.GalleryList;
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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {



    private List<FirstLevelFilter> filtergallerylists;
    private Context context;
    private GalleryAdapterListner listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public LinearLayout mainLL;
        public ImageView imageIv;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv =  view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            mainLL = view.findViewById(R.id.mainLL);
            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(filtergallerylists.get(getAdapterPosition()),filtergallerylists);
                }
            });
        }
    }


    public GalleryAdapter(Context context, List<FirstLevelFilter> filtergallerylists, GalleryAdapterListner listener) {

        this.filtergallerylists = filtergallerylists;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirstLevelFilter galleryList = filtergallerylists.get(position);

        holder.nameTv.setText(galleryList.getTitle());
        Glide.with(context).load(galleryList.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.imageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));
        if (galleryList.getFolderName()==null)
        {

        }else {
            holder.mainLL.setBackgroundResource(R.drawable.folder);
        }


    }

    @Override
    public int getItemCount() {
        return filtergallerylists.size();
    }

    public interface GalleryAdapterListner {
        void onContactSelected(FirstLevelFilter filtergallerylists,List<FirstLevelFilter> firstLevelFilters);
    }
}