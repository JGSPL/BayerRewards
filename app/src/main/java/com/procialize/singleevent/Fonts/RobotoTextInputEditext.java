package com.procialize.singleevent.Fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Naushad on 11/1/2017.
 */

@SuppressLint("AppCompatCustomView")
public class RobotoTextInputEditext extends TextInputEditText {

    public RobotoTextInputEditext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextInputEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextInputEditext(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Light_13935.ttf");
        setTypeface(tf ,1);

    }

}