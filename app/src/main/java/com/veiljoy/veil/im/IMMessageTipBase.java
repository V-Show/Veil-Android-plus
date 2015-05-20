package com.veiljoy.veil.im;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zhongqihong on 15/5/18.
 */
public abstract class IMMessageTipBase extends LinearLayout {


    public IMMessageTipBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWidget(context );
    }

    public IMMessageTipBase(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IMMessageTipBase(Context context) {
        this(context,null);
    }

    abstract  void initWidget(Context context);


}
