package com.procialize.vivo_app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import com.procialize.vivo_app.ApiConstant.ApiConstant;
import com.procialize.vivo_app.GetterSetter.SelfieList;
import com.procialize.vivo_app.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.MyViewHolder> {



    public List<SelfieList> selfieList;
    private Context context;
    private SelfieAdapterListner listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dataTv,countTv;
        public LinearLayout mainLL;
        public ImageView imageIv,likeIv,moreIV;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            dataTv =  view.findViewById(R.id.dataTv);
            countTv =  view.findViewById(R.id.countTv);
            imageIv = view.findViewById(R.id.imageIv);
            likeIv = view.findViewById(R.id.likeIv);
            moreIV = view.findViewById(R.id.moreIV);
            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(selfieList.get(getAdapterPosition()),imageIv);
                }
            });

            likeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeListener(v,selfieList.get(getAdapterPosition()),getAdapterPosition(),countTv,likeIv);
                }
            });

            moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreListner(v,selfieList.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }


    public SelfieAdapter(Context context, List<SelfieList> selfieList, SelfieAdapterListner listener) {

        this.selfieList = selfieList;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selfierow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SelfieList galleryList = selfieList.get(position);

        holder.dataTv.setText(StringEscapeUtils.unescapeJava(galleryList.getTitle()));
        holder.countTv.setText(galleryList.getTotalLikes());
        Glide.with(context).load(ApiConstant.selfieimage+galleryList.getFileName())
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


        if (galleryList.getLikeFlag().equals("1")) {


            holder.likeIv.setImageResource(R.drawable.ic_afterlike);

        } else {
            holder.likeIv.setImageResource(R.drawable.ic_like);
        }

    }

    @Override
    public int getItemCount() {
        return selfieList.size();
    }

    public interface SelfieAdapterListner {
        void onContactSelected(SelfieList selfieList,ImageView imageView);


        void onLikeListener(View v, SelfieList selfieList, int position, TextView count,ImageView likeIv);

        void onMoreListner(View v, SelfieList selfieList, int position);

    }
}