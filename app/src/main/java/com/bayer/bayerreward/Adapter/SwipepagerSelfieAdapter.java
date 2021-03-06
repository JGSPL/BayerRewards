package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.GetterSetter.SelfieList;
import com.bayer.bayerreward.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gauravnaik309 on 01/03/18.
 */

public class SwipepagerSelfieAdapter extends PagerAdapter {

    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive;
    private List<SelfieList> images;
    private LayoutInflater inflater;
    private Context context;

    public SwipepagerSelfieAdapter(Context context, List<SelfieList> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slideselfie, view, false);

        SelfieList firstLevelFilter = images.get(position);


        ImageView myImage = myImageLayout.findViewById(R.id.image);
        TextView name = myImageLayout.findViewById(R.id.name);
        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        Glide.with(context).load(ApiConstant.selfieimage + firstLevelFilter.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(myImage).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

        name.setText(StringEscapeUtils.unescapeJava(firstLevelFilter.getTitle()));
        name.setTextColor(Color.parseColor(colorActive));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}