package com.procialize.vivo_app.Fonts;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

@SuppressLint("AppCompatCustomView")
public class DinproBold extends TextInputEditText {

    public DinproBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DinproBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DinproBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Medium.ttf");
        setTypeface(tf, 1);

    }

}