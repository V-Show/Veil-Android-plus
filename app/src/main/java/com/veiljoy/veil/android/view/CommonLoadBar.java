package com.veiljoy.veil.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.veiljoy.veil.R;

/**
 * Created by zhongqihong on 15/5/5.
 */
public class CommonLoadBar extends RelativeLayout {

    ImageView ivLoadingLeft;
    ImageView ivLoadingRight;
    Context mContext;
    Animation animationRight;
    Animation animationLeft;

    public CommonLoadBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initWidget(context );
    }

    public CommonLoadBar(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        initWidget(context);

    }

    public CommonLoadBar(Context context) {
        super(context);
        initWidget(context);
    }

    private  void initWidget(Context context){


        mContext = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate( R.layout.activity_welcome , null );
        this.addView( layout );


        ivLoadingLeft = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_left);
        ivLoadingRight = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_right);
        animationLeft = AnimationUtils.loadAnimation(mContext, R.anim.common_loading_zoom_left);
        animationRight = AnimationUtils.loadAnimation(mContext, R.anim.common_loading_zoom_right);


    }

    public void startLoad(){
        ivLoadingRight.startAnimation(animationRight);
        ivLoadingLeft.startAnimation(animationLeft);
    }

    public void stopLoad(){
        ivLoadingRight.clearAnimation();
        ivLoadingLeft.clearAnimation();
    }
}
