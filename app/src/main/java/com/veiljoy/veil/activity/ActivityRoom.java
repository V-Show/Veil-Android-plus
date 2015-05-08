package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;

/**
 * Created by zhongqihong on 15/5/7.
 */
public class ActivityRoom extends BaseActivity implements View.OnClickListener {


    final String TAG = "ActivityRoom";
    ScrollView previewScroll;
    private boolean drawScroll = true;
    Animation mTalkBarAnim;
    Button mBtnTalk;
    LinearLayout mTalkLayout;
    LinearLayout mTalkBar ;
    FrameLayout mUserInfoLayout;
    ScrollView mScrollView;
    LinearLayout mImpression;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);


        initViews();
        initEvents();
//       init();

    }

    private void initViews() {

//        previewScroll = (ScrollView)findViewById( R.id.previewScroll );
//        final int apiLevel = Build.VERSION.SDK_INT;
//        if( apiLevel >= 9 )
//        { // 2.3
//            previewScroll.setOverScrollMode( View.OVER_SCROLL_NEVER );
//        }
//
        mTalkBarAnim = AnimationUtils.loadAnimation(this, R.anim.talk_bar_popup);
        mTalkBarAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImpression.invalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTalkBarAnim.setFillAfter(true);
      //  mScrollView=(ScrollView)this.findViewById(R.id.activity_room_scrollview);
        mBtnTalk = (Button) this.findViewById(R.id.activity_room_btn_talk);
        mTalkLayout = (LinearLayout) this.findViewById(R.id.activity_room_talk_layout);
        mTalkBar = (LinearLayout) this.findViewById(R.id.activity_chat_talk);
        mImpression=(LinearLayout)this.findViewById(R.id.activity_room_impression);

        mUserInfoLayout=(FrameLayout)this.findViewById(R.id.activity_room_bottom_layout);

        ViewTreeObserver vto = mUserInfoLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(
             new ViewTreeObserver.OnGlobalLayoutListener(){

            @Override
             public void onGlobalLayout() {
                mTalkLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int layoutHeight = mUserInfoLayout.getMeasuredHeight();

                int height= mTalkBar.getMeasuredHeight();

                Log.d(TAG, "layoutHeight=" + layoutHeight

                                + "height=" + height

                                + "layoutHeight - height= " + (layoutHeight - height)
                        +"  +mDensity="+  +mDensity
                );
//layoutHeight - height
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTalkLayout.getLayoutParams();
                params.setMargins(0, layoutHeight - height-100, 0, 0);

                mTalkLayout.setLayoutParams(params);
               }
        }
    );
//
//        mScrollView.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View arg0, MotionEvent arg1) {
//                return false;
//            }
//        });


    }


    private void initEvents() {
        mBtnTalk.setOnClickListener(this);
    }

    private void init() {
        if (previewScroll.getScrollY() != 0) {
            previewScroll.smoothScrollTo(0, 0);
        }

        reLayoutScroll();
    }

    private void reLayoutScroll() {


        previewScroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            View viewGap;

            @Override
            public void onGlobalLayout() {
                viewGap = findViewById(R.id.chat_bottom_bar_user_layout);
                final int pictureHeight = viewGap.getLayoutParams().height;
                Log.d("PreviewHotActivity", "reLayoutScroll,pictureH=" + pictureHeight
                        + ",scrollH=" + previewScroll.getHeight());
                viewGap.getLayoutParams().height = previewScroll.getHeight();

                if (pictureHeight == previewScroll.getHeight()) {
                    drawScroll = true;
                    previewScroll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    drawScroll = false;
                }
            }
        });
        previewScroll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (drawScroll) {
                    previewScroll.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return drawScroll;
            }
        });
    }

    private void smoothScrollMore() {
        if (previewScroll.getScrollY() != 0) {
            previewScroll.smoothScrollTo(0, 0);
        } else {
            previewScroll.smoothScrollTo(0, previewScroll.getMaxScrollAmount());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_room_btn_talk:
                mImpression.setVisibility(View.VISIBLE);
                mTalkLayout.startAnimation(mTalkBarAnim);
                break;
        }
    }
}
