package com.procialize.vivo_app.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.vivo_app.Activity.HomeActivity;
import com.procialize.vivo_app.Activity.ProfileActivity;
import com.procialize.vivo_app.R;



public class Util
{

    //final static String logoImg= HomeActivity.logoImg;

    public static void logomethod(final Context context, final ImageView headerlogoIv)
    {
        Glide.with(context).load("http://www.procialize.info/uploads/app_logo/" + HomeActivity.logoImg).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                headerlogoIv.setImageResource(R.drawable.splashlogo);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(headerlogoIv);
    }
    public static void logomethod1(final Context context, final ImageView headerlogoIv)
    {
        Glide.with(context).load("http://www.procialize.info/uploads/app_logo/" + ProfileActivity.logoImg).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                headerlogoIv.setImageResource(R.drawable.splashlogo);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(headerlogoIv);
    }

}
