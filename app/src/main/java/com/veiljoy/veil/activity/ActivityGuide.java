package com.veiljoy.veil.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;

/**
 * Created by zhongqihong on 15/5/8.
 */
public class ActivityGuide extends BaseActivity implements GestureDetector.OnGestureListener {

    private static final int FLING_MIN_DISTANCE = 100;
    private ViewFlipper flipper;
    private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);




        // 注册一个GestureDetector
        detector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.activity_guide_vf_viewflipper);

//        initViews();
//        initEvents();
//        init();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触屏事件交给手势识别类处理
        return this.detector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
            //设置View进入和退出的动画效果
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            this.flipper.showNext();
            return true;
        }
        if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_out));
            this.flipper.showPrevious();
            return true;
        }
        return false;
    }
}
