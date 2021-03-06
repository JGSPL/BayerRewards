package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bayer.bayerreward.CustomTools.PicassoTrustAll;
import com.bayer.bayerreward.GetterSetter.FirstLevelFilter;
import com.bayer.bayerreward.InnerDrawerActivity.GalleryFirstLevelActivity;
import com.bayer.bayerreward.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class GalleryFirstLevelAdapter extends RecyclerView.Adapter<GalleryFirstLevelAdapter.MyViewHolder> {


    String colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    private List<FirstLevelFilter> filtergallerylists;
    private Context context;
    private GalleryFirstLevelAdapterListener listener;

    public GalleryFirstLevelAdapter(Context context, List<FirstLevelFilter> filtergallerylists, GalleryFirstLevelAdapterListener listener) {

        this.filtergallerylists = filtergallerylists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_first_level, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirstLevelFilter galleryList = filtergallerylists.get(position);
//        if (galleryList.getTitle().contains("/"))
//        {
//            String[] parts = galleryList.getTitle().split("/");
//            String part1 = parts[0];
//            String part2 = parts[1];
//            holder.nameTv.setText(part2);
//        }else
//        {
        holder.nameTv.setText(galleryList.getTitle());
//        }

        holder.progressBar.setVisibility(View.GONE);
        PicassoTrustAll.getInstance(context)
                .load(galleryList.getFileName())
                .fit()
                .placeholder(R.drawable.gallery_placeholder)
                .into(holder.imageIv);

//        Glide.with(context).load(galleryList.getFileName())
//                .apply(RequestOptions.skipMemoryCacheOf(false))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                holder.progressBar.setVisibility(View.GONE);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                holder.progressBar.setVisibility(View.GONE);
//                return false;
//            }
//        }).into(holder.imageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

        if (galleryList.getFolderName().equalsIgnoreCase(GalleryFirstLevelActivity.foldername)) {

        } else {
            holder.imageIv.setBackgroundResource(R.drawable.folder_back);
        }

    }

    @Override
    public int getItemCount() {
        return filtergallerylists.size();
    }

    public interface GalleryFirstLevelAdapterListener {
        void onContactSelected(FirstLevelFilter filtergallerylists, List<FirstLevelFilter> filtergallerylist);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;
        public LinearLayout mainLL;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(filtergallerylists.get(getAdapterPosition()), filtergallerylists);
                }
            });
        }
    }

}