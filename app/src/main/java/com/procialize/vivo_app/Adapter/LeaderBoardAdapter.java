package com.procialize.vivo_app.Adapter;

import android.content.Context;
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
import com.procialize.vivo_app.ApiConstant.ApiConstant;
import com.procialize.vivo_app.GetterSetter.LeaderBoard;
import com.procialize.vivo_app.R;

import java.util.List;

/**
 * Created by Rahul on 23-10-2018.
 */

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

private List<LeaderBoard> leadLists;
private Context context;
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPoints, tvName;
    public ImageView ivpic;

    public MyViewHolder(View view) {
        super(view);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPoints = (TextView) view.findViewById(R.id.tvPoints);
        ivpic = (ImageView) view.findViewById(R.id.ivpic);

    }
}


    public LeaderBoardAdapter(Context context, List<LeaderBoard> docList) {
        this.leadLists = docList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LeaderBoard leaderBoard = leadLists.get(position);
        holder.tvName.setText(leaderBoard.getFirst_name()+" "+leaderBoard.getLast_name());
        holder.tvPoints.setText(leaderBoard.getPoint());
        if (leaderBoard.getProfile_pic()!=null)
        {
            Glide.with(context).load(ApiConstant.profilepic+leaderBoard.getProfile_pic()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.ivpic.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.ivpic);
        }else
        {
            holder.ivpic.setImageResource(R.drawable.profilepic_placeholder);
        }

    }

    @Override
    public int getItemCount() {
        return leadLists.size();
    }


}
