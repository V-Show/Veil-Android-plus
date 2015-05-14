package com.veiljoy.veil.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.bean.UserInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.utils.CommonUtils;
import com.veiljoy.veil.utils.VoiceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by zhongqihong on 15/5/7.
 */
public class ActivityRoom extends ActivityChatSupport implements  View.OnLongClickListener,View.OnClickListener {


    boolean DEBUG=true;

    final String TAG = "ActivityRoom";
    ListView mLVChat;
    ChatAdapter mChatAdapter;
    ScrollView previewScroll;
    private boolean drawScroll = true;
    private boolean isTalking;
    String avatarPath;
    private String currMsgType;
    private String mVoiceFileName = null;
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
    LinearLayout mOptionLayout;
    LinearLayout mVoiceLayout;
    LinearLayout mDetailLayout;
    RelativeLayout mChatListLayout;
    boolean mIsVoiceBtnShow=true;
    int mCurrFocusMember;
    private ImageButton mBack;
    BaseApplication application;

    /*
 *监控录音时间
 */
    long beforeTime;
    long afterTime;
    int timeDistance;
    /**
     * integer: linearLayout id
     * Member
     * **/
    Map<Integer,Member>members=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        init();
        initViews();
        initEvents();


    }

    @Override
    protected void receiveNewMessage(IMMessage message) {

    }

    @Override
    protected void refreshMessage(List<IMMessage> messages) {
        mChatAdapter.refreshList(messagePool);
        mLVChat.setSelection(messagePool.size());
    }

    @Override
    protected IMMessage makeMessage() {
        IMMessage o = null;
        IMMessage.Scheme type = IMMessage.Scheme.ofUri(currMsgType);
        switch (type) {
            case VOICE:
                o = new IMMessageVoiceEntity();
                o.setmUri(IMMessage.Scheme.VOICE.wrap(""));
                Random r = new Random(System.currentTimeMillis());
                o.setmMessageType(Math.abs(r.nextInt() % 2));
                o.setmAvatar(avatarPath);
                ((IMMessageVoiceEntity) o).setmVoiceTimeRange(Math.abs(r.nextInt() % 20));
                ((IMMessageVoiceEntity) o).setmVoiceFileName(mVoiceFileName);

                break;
        }

        return o;
    }

    @Override
    protected void updateUserInfo(Map<String, UserInfo> userInfoList) {

    }


    private void initViews() {

        mChatListLayout=(RelativeLayout)this.findViewById(R.id.activity_room_chat_list_layout);

        mChatAdapter = new ChatAdapter(application, this, getMessages());
        mLVChat = (ListView) this.findViewById(R.id.activity_room_chat_list);
        mLVChat.setAdapter(mChatAdapter);

        mOptionLayout=(LinearLayout)this.findViewById(R.id.activity_room_option_layout);
        mVoiceLayout=(LinearLayout)this.findViewById(R.id.activity_room_voice_layout);
        mDetailLayout=(LinearLayout)this.findViewById(R.id.activity_room_detail_layout);

        mMenu=(ImageButton)this.findViewById(R.id.activity_room__ib_menu);
        mBtnTalk = (Button) this.findViewById(R.id.activity_room_btn_talk);
        mTalkLayout = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar_layout);
        mTalkBar = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar);
        mImpression=(LinearLayout)this.findViewById(R.id.activity_room_impression);
        mUserInfoLayout=(FrameLayout)this.findViewById(R.id.activity_room_bottom_layout);
        mBack=(ImageButton)this.findViewById(R.id.include_app_topbar_ib_change);
        mBack.setVisibility(View.INVISIBLE);
        ViewTreeObserver vto = mUserInfoLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(
             new ViewTreeObserver.OnGlobalLayoutListener(){

            @Override
             public void onGlobalLayout() {
                mTalkLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int layoutHeight = mUserInfoLayout.getMeasuredHeight();

                int height= mTalkBar.getMeasuredHeight();

                Resources res = getResources();


                int talkBarHeight=height+(int)(res.getDimension(R.dimen.chat_room_user_option_padding_top));

                transHeight=layoutHeight - talkBarHeight;


                if(mState==State.BOTTOM){
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",0).setDuration(2000).start();
                    mState=State.TOP;
                }
                else if(mState==State.TOP){
                    mState=State.BOTTOM;
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY",transHeight).setDuration(0).start();
                }

                FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams) mChatListLayout.getLayoutParams();
                lp.setMargins(0,0,0,height);
                mChatListLayout.setLayoutParams(lp);


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
        mCurrFocusMember=R.id.activity_room_member0_layout;
        setMemberFocus(R.id.activity_room_member0_layout);

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



        mVoiceLayout.setVisibility(View.VISIBLE);
        mOptionLayout.setVisibility(View.GONE);


    }


    private void initEvents() {
        mMenu.setOnClickListener(this);
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
        mDetailLayout.setOnClickListener(this);
    }

    private void init() {
        application = (BaseApplication) getApplication();
        VoiceUtils.getmInstance().setOnVoiceRecordListener(new OnVoiceRecordListenerImpl());
//        if (previewScroll.getScrollY() != 0) {
//            previewScroll.smoothScrollTo(0, 0);
//        }
//
//        reLayoutScroll();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.activity_room__ib_menu:
                Intent intent=new Intent(ActivityRoom.this,ActivitySetting.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                //startActivity(ActivitySetting.class,null);
                break;
            case R.id.activity_room_detail_layout:
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
                setMemberFocus(v.getId());

                if(mCurrFocusMember==v.getId()){
                    if(mIsVoiceBtnShow){
                        mVoiceLayout.setVisibility(View.GONE);
                        mOptionLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        mVoiceLayout.setVisibility(View.VISIBLE);
                        mOptionLayout.setVisibility(View.GONE);
                    }
                    mIsVoiceBtnShow=!mIsVoiceBtnShow;
                }
                mCurrFocusMember=v.getId();
                break;


            case R.id.activity_room_member3_layout:
                setMemberFocus(v.getId());
                mVoiceLayout.setVisibility(View.VISIBLE);
                mOptionLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void setMemberFocus(int id){

        Set<Integer> set=members.keySet();
        Iterator<Integer>ite=set.iterator();
        while (ite.hasNext()){
            int key=ite.next();
            if(key==id){
                members.get(key).name.setTextColor(getResources().getColor(R.color.red));
            }
            else {
                members.get(key).name.setTextColor(getResources().getColor(R.color.light_gray));
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.activity_room_btn_talk:
                if (!isTalking) {

                    Log.v("chatActivity", "start record");
                    beforeTime = System.currentTimeMillis();
                    isTalking = true;
                    currMsgType = IMMessage.Scheme.VOICE.wrap("test");
                    mVoiceFileName = VoiceUtils.generateFileName();

                    VoiceUtils.getmInstance().startRecord(mVoiceFileName);
                }


                break;
        }

        return true;
    }

    enum State{
        TOP,
        BOTTOM
    }

    class Member{
        ImageView avatar;
        TextView name;
    }
    ///mHeaderHeight
    class OnTalkBtnTouch implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction()==MotionEvent.ACTION_DOWN){

            }

            if (isTalking) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        recordStop();
                        if(DEBUG) {
                            new OnVoiceRecordListenerImpl().onResult("");
                        }
                    }
                    break;

                }
            }
            return false;
        }
    }

    /*
   * 发送语音消息
   * */
    class OnVoiceRecordListenerImpl implements VoiceUtils.OnVoiceRecordListener {

        @Override
        public void onBackgroundRunning() {
            Log.v("chatActivity", "onBackgroundRunning");
        }

        @Override
        public void onResult(String fileName) {
            Log.v("chatActivity", "onResult " + fileName);

            if(DEBUG){
                sendMessage("" + "&"
                        + (afterTime - beforeTime) / 1000);

                return ;
            }
            if (fileName != null) {
                mVoiceFileName = fileName;

                String path = fileName.substring(fileName.lastIndexOf(File.separator), fileName.length());

                String file = path.substring(1, path.lastIndexOf(VoiceUtils.suffix));

                String voiceFile = CommonUtils.VOICE_SIGN
                        + CommonUtils.GetImageStr(mVoiceFileName) + "@" + file
                        + CommonUtils.VOICE_SIGN;
                Log.v(TAG, "voice file name " + file);
                sendMessage(voiceFile + "&"
                        + (afterTime - beforeTime) / 1000);


            }

        }
        @Override
        public void onPreRecord() {
            Log.v("chatActivity", "onPreRecord ");

        }
    }
    public void recordStop() {
        Log.v("chatActivity", "stop record");
        afterTime = System.currentTimeMillis();
        isTalking = false;
        VoiceUtils.getmInstance().stop();

    }
}
