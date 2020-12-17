package com.bayer.bayerreward.CustomTools;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Naushad on 11/9/2017.
 */

public class PixabayImageView extends AppCompatImageView {
    private float aspectRatio = 1.77f;

    public PixabayImageView(Context context) {
        super(context);
    }

    public PixabayImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAspectRatio(float ratio) {
        aspectRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (width * aspectRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}