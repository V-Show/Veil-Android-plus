package com.veiljoy.veil.activity;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    LinearLayout membersLayout;
    LinearLayout talkBarLayout;
    /**
     * integer: linearLayout id
     * Member
     * **/
    Map<Integer,Member>members=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initViews();
        initEvents();


    }



    private void initViews() {



        mMenu=(ImageButton)this.findViewById(R.id.activity_room__ib_menu);

        mBtnTalk = (Button) this.findViewById(R.id.activity_room_btn_talk);
        mTalkLayout = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar_layout);
        mTalkBar = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar_layout);
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

                Resources res = getResources();



                transHeight=layoutHeight - height-(int)(res.getDimension(R.dimen.chat_room_user_option_padding_top));


                if(mState==State.BOTTOM){
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",0).setDuration(2000).start();
                    mState=State.TOP;
                }
                else if(mState==State.TOP){
                    mState=State.BOTTOM;
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",transHeight).setDuration(0).start();
                }


               }
        }


    );

        membersLayout=(LinearLayout)this.findViewById(R.id.activity_room_members_layout);
        talkBarLayout=(LinearLayout)this.findViewById(R.id.activity_room_talk_bar_layout);

        LinearLayout m0Layout=(LinearLayout)this.findViewById(R.id.activity_room_member0_layout);
        LinearLayout m1Layout=(LinearLayout)this.findViewById(R.id.activity_room_member1_layout);
        LinearLayout m2Layout=(LinearLayout)this.findViewById(R.id.activity_room_member2_layout);
        LinearLayout m3Layout=(LinearLayout)this.findViewById(R.id.activity_room_member3_layout);
        m0Layout.setOnClickListener(this);
        m1Layout.setOnClickListener(this);
        m2Layout.setOnClickListener(this);
        m3Layout.setOnClickListener(this);

        Member m0=new Member();
        m0.avatar=(ImageView)this.findViewById(R.id.activity_room_iv_member0);
        m0.name=(TextView)this.findViewById(R.id.activity_room_tv_member0);
        members.put(R.id.activity_room_member0_layout,m0);

        Member m1=new Member();
        m1.avatar=(ImageView)this.findViewById(R.id.activity_room_iv_member1);
        m1.name=(TextView)this.findViewById(R.id.activity_room_tv_member1);
        members.put(R.id.activity_room_member1_layout,m1);

        Member m2=new Member();
        m2.avatar=(ImageView)this.findViewById(R.id.activity_room_iv_member2);
        m2.name=(TextView)this.findViewById(R.id.activity_room_tv_member2);
        members.put(R.id.activity_room_member2_layout,m2);

        Member m3=new Member();
        m3.avatar=(ImageView)this.findViewById(R.id.activity_room_iv_member3);
        m3.name=(TextView)this.findViewById(R.id.activity_room_tv_member3);
        members.put(R.id.activity_room_member3_layout,m3);


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
                break;
            case R.id.activity_room_member0_layout:
            case R.id.activity_room_member1_layout:
            case R.id.activity_room_member2_layout:
                int id=v.getId();
                Set<Integer> set=members.keySet();
                Iterator<Integer>ite=set.iterator();
                while (ite.hasNext()){
                    int key=ite.next();
                    if(key==id){
                        members.get(key).name.setTextColor(getResources().getColor(R.color.red));
                    }
                    else {
                        members.get(key).name.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
                talkBarLayout.setVisibility(View.GONE);
                break;

            case R.id.activity_room_member3_layout:
                membersLayout.setVisibility(View.GONE);
                break;
        }
    }

    enum State{
        TOP,
        BOTTOM
    }

    class Member{
        ImageView avatar;
        TextView name;
    }
}
