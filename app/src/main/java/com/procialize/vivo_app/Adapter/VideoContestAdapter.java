package com.procialize.vivo_app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.procialize.vivo_app.ApiConstant.ApiConstant;
import com.procialize.vivo_app.GetterSetter.VideoContest;
import com.procialize.vivo_app.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class VideoContestAdapter extends RecyclerView.Adapter<VideoContestAdapter.MyViewHolder> {


    public List<VideoContest> videoContestList;
    private Context context;
    private VideoContestAdapterListner listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dataTv, countTv;
        public LinearLayout mainLL;
        public ImageView likeIv, moreIV, shareIV;
        public ImageView videoPlayerStandard;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            dataTv = view.findViewById(R.id.dataTv);
            countTv = view.findViewById(R.id.countTv);
            videoPlayerStandard = view.findViewById(R.id.videoPlayerStandard);
            likeIv = view.findViewById(R.id.likeIv);
            moreIV = view.findViewById(R.id.moreIV);
            shareIV = view.findViewById(R.id.shareIV);
            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(videoContestList.get(getAdapterPosition()));
                }
            });

            likeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeListener(v, videoContestList.get(getAdapterPosition()), getAdapterPosition(), countTv, likeIv);
                }
            });

            moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreListner(v, videoContestList.get(getAdapterPosition()), getAdapterPosition());

                }
            });

            shareIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShareListner(v, videoContestList.get(getAdapterPosition()), getAdapterPosition());

                }
            });
        }
    }


    public VideoContestAdapter(Context context, List<VideoContest> videoContestList, VideoContestAdapterListner listener) {

        this.videoContestList = videoContestList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videocontestrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final VideoContest galleryList = videoContestList.get(position);

        holder.dataTv.setText(StringEscapeUtils.unescapeJava(galleryList.getTitle()));
        holder.countTv.setText(galleryList.getTotalLikes());

//        holder.videoPlayerStandard.setUp(ApiConstant.selfievideo+galleryList.getFileName()
//                , JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");

        Glide.with(holder.videoPlayerStandard.getContext()).load(ApiConstant.selfievideo + galleryList.getThumbName()).into(holder.videoPlayerStandard);


        if (galleryList.getLikeFlag().equals("1")) {

            holder.likeIv.setImageResource(R.drawable.ic_afterlike);

        } else {
            holder.likeIv.setImageResource(R.drawable.ic_like);
        }

    }

    @Override
    public int getItemCount() {
        return videoContestList.size();
    }

    public interface VideoContestAdapterListner {
        void onContactSelected(VideoContest videoContest);

        void onLikeListener(View v, VideoContest videoContest, int position, TextView count, ImageView likeIv);

        void onMoreListner(View v, VideoContest videoContest, int position);

        void onShareListner(View v, VideoContest videoContest, int position);

    }
}