package com.veiljoy.veil.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
    ImageButton mMenu;
    int transHeight=0;
    State mState=State.TOP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initViews();
        initEvents();


    }



    private void initViews() {

//
        mMenu=(ImageButton)this.findViewById(R.id.activity_room__ib_menu);

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

                transHeight=layoutHeight - height;


                if(mState==State.BOTTOM){
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",0).setDuration(2000).start();
                    mState=State.TOP;
                }
                else if(mState==State.TOP){
                    mState=State.BOTTOM;
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",transHeight).setDuration(0).start();
                }

                Log.v(TAG,"XXXXX111 "+mTalkLayout.getX());
               }
        }
    );


    }


    private void initEvents() {
        mMenu.setOnClickListener(this);
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
            case R.id.activity_room__ib_menu:

                if(mState==State.BOTTOM){
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",0).setDuration(2000).start();
                    mState=State.TOP;
                }
                else if(mState==State.TOP){
                    mState=State.BOTTOM;
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",transHeight).setDuration(2000).start();
                }

                Log.v(TAG,"XXXXX "+mTalkLayout.getX());


                break;

        }
    }

    enum State{
        TOP,
        BOTTOM
    }
}
