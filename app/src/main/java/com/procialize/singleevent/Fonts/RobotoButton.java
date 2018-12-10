package com.procialize.singleevent.Fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Naushad on 11/1/2017.
 */

@SuppressLint("AppCompatCustomView")
public class RobotoButton extends Button {

    public RobotoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoButton(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Regular.ttf");
        setTypeface(tf ,1);

    }

}

//    Roboto-Light.ttf
