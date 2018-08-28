package com.procialize.singleevent.Fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Naushad on 11/1/2017.
 */

@SuppressLint("AppCompatCustomView")
public class RobotoTextview extends TextView {

    public RobotoTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Light.ttf");
        setTypeface(tf ,1);

    }

}

//    Roboto-Light.ttf

