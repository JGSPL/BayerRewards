package com.procialize.eventsapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class RobotoTextViewHeader extends AppCompatTextView {

    public RobotoTextViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoTextViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextViewHeader(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DINPro-Light_13935.ttf");
        setTypeface(tf, 1);


    }

}
