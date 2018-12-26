package com.procialize.singleevent.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
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
import com.github.barteksc.pdfviewer.util.Constants;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.GetterSetter.SelfieList;
import com.procialize.singleevent.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

public class SelfieAdapterNew extends BaseAdapter {

    List<SelfieList> selfieList;
    private LayoutInflater inflater;
    Activity activity;

    Constants constant = new Constants();

    public SelfieAdapterNew(Activity c, List<SelfieList> selfieList) {
        activity = c;
        this.selfieList = selfieList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return selfieList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public SelfieList getImageFromList(int position) {
        return selfieList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        if (convertView == null) {

        convertView = inflater.inflate(R.layout.selfierow, null);

        holder = new ViewHolder();

        Display dispDefault = ((WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int totalwidth = dispDefault.getWidth();

//        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
//                "fonts/FuturaStd-Medium.ttf");


        SelfieList selfie = selfieList.get(position);


        holder.dataTv = convertView.findViewById(R.id.dataTv);
        holder.countTv = convertView.findViewById(R.id.countTv);
        holder.imageIv = convertView.findViewById(R.id.imageIv);
        holder.likeIv = convertView.findViewById(R.id.likeIv);
        holder.moreIV = convertView.findViewById(R.id.moreIV);
        holder.progressBar = convertView.findViewById(R.id.progressBar);

        holder.imageIv.setLayoutParams(new LinearLayout.LayoutParams(
                totalwidth / 2, totalwidth / 2));

        convertView.setTag(holder);


        holder.dataTv.setText(StringEscapeUtils.unescapeJava(selfie.getTitle()));
        holder.countTv.setText(selfie.getTotalLikes());
        Glide.with(activity).load(ApiConstant.selfieimage + selfie.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource
                    dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.imageIv).onLoadStarted(activity.getDrawable(R.drawable.gallery_placeholder));


        if (selfie.getLikeFlag().equals("1")) {


            holder.likeIv.setImageResource(R.drawable.ic_afterlike);

        } else {
            holder.likeIv.setImageResource(R.drawable.ic_like);
        }


        return convertView;
    }

    public static String getEmojiFromString(String emojiString) {

        if (!emojiString.contains("\\u")) {

            return emojiString;
        }
        String EmojiEncodedString = "";

        int position = emojiString.indexOf("\\u");

        while (position != -1) {

            if (position != 0) {
                EmojiEncodedString += emojiString.substring(0, position);
            }

            String token = emojiString.substring(position + 2, position + 6);
            emojiString = emojiString.substring(position + 6);
            EmojiEncodedString += (char) Integer.parseInt(token, 16);
            position = emojiString.indexOf("\\u");
        }
        EmojiEncodedString += emojiString;

        return EmojiEncodedString;
    }

    static class ViewHolder {
        TextView countTv, txtLike, dataTv;
        ImageView imageIv, likeIv, moreIV;
        private ProgressBar progressBar;

    }
}
