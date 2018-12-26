package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procialize.singleevent.CustomTools.RoundCornersTransformation;
import com.procialize.singleevent.GetterSetter.FirstLevelFilter;
import com.procialize.singleevent.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {



    private List<FirstLevelFilter> filtergallerylists;
    private Context context;
    private GalleryAdapterListner listener;

    String MY_PREFS_NAME = "ProcializeInfo";
    SharedPreferences prefs;
    String colorActive;

    public GalleryAdapter(Context context, List<FirstLevelFilter> filtergallerylists, GalleryAdapterListner listener) {

        this.filtergallerylists = filtergallerylists;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirstLevelFilter galleryList = filtergallerylists.get(position);

        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

        holder.nameTv.setText(galleryList.getTitle());
//        Glide.with(context).load(galleryList.getFileName())
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
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


        int color = Color.parseColor(colorActive);
        holder.img.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);


        holder.progressBar.setVisibility(View.GONE);
        Picasso.with(context)
                .load(galleryList.getFileName())
                .transform(new RoundCornersTransformation(20, 0, false, true))
                .placeholder(context.getDrawable(R.drawable.folder_back))
                .into(holder.imageIv);


        if (galleryList.getFolderName()==null)
        {

        }else {
            holder.imageIv.setBackgroundResource(R.drawable.folder_back);
        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public LinearLayout mainLL;
        public ImageView imageIv, img;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            img = view.findViewById(R.id.img);
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

    @Override
    public int getItemCount() {
        return filtergallerylists.size();
    }

    public interface GalleryAdapterListner {
        void onContactSelected(FirstLevelFilter filtergallerylists,List<FirstLevelFilter> firstLevelFilters);
    }
}