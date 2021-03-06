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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.veiljoy.spark.android.core.SparkApplication;
import com.veiljoy.spark.core.SimpleSparkListener;
import com.veiljoy.spark.core.SparkListener;
import com.veiljoy.veil.R;
import com.veiljoy.veil.adapter.ChatAdapter;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.bean.UserInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.imImpl.IMMessageItem;
import com.veiljoy.veil.utils.CommonUtils;
import com.veiljoy.veil.utils.FormatTools;
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
public class ActivityRoom extends ActivityChatSupport implements View.OnLongClickListener, View.OnClickListener {



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
    LinearLayout mTalkBar;
    FrameLayout mUserInfoLayout;
    ScrollView mScrollView;
    LinearLayout mImpression;
    ImageButton mMenu;
    int transHeight = 0;
    State mState = State.TOP;
    LinearLayout membersLayout;
    LinearLayout talkBarLayout;
    LinearLayout mOptionLayout;
    LinearLayout mVoiceLayout;
    LinearLayout mDetailLayout;
    RelativeLayout mChatListLayout;
    boolean mIsVoiceBtnShow = true;
    int mCurrFocusMember;
    private ImageButton mBack;
    BaseApplication application;
    SparkApplication mApp;
    SparkListener mSparkListener;
    int mSparkState = 0; // 0: idle, 1: connecting, 2: logging in, 3: signing up, 4: rubbing, 5: entering room
    final int MemberIds[] = {
            0,
            R.id.activity_room_member0_layout,
            R.id.activity_room_member1_layout,
            R.id.activity_room_member2_layout
    };

    /*当前正在播放的语言*/
    int mCurrPlayItem=0;
    /**
     * 最后显示时间的时间，超过5分钟显示一次时间
     * **/


    /*
 *监控录音时间
 */
    long beforeTime;
    long afterTime;
    int timeDistance;
    /**
     * integer: linearLayout id
     * Member
     * *
     */
    Map<Integer, Member> members = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (SparkApplication) this.getApplication();
        mSparkListener = new RoomSparkListener();
        mSparkState = 0;
        setContentView(R.layout.activity_chat_room);
        init();
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApp.registerSparkListener(mSparkListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mApp.unregisterSparkListener(mSparkListener);
    }

    private void clearMessage(){
        messagePool.clear();
        mChatAdapter.refreshList(messagePool);
        //mLVChat.setSelection(messagePool.size());
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
                o.setmAvatar(avatarPath);
                ((IMMessageVoiceEntity) o).setmVoiceTimeRange((int)((afterTime - beforeTime) / 1000));
                ((IMMessageVoiceEntity) o).setmVoiceFileName(mVoiceFileName);
                o.setmRead(1);
                break;
        }

        return o;
    }
    /*
        * 播放指定的语音项
        *
        * */
    class OnChatListItemClick implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            mCurrPlayItem= position;
            IMMessage msg = (IMMessage) getMessages().get(position);
            String uri = msg.getmUri();
            switch (IMMessage.Scheme.ofUri(uri)) {
                case VOICE:
                    IMMessageVoiceEntity voiceEntity = (IMMessageVoiceEntity) msg;
                    Log.v("chatActivity", "OnChatListItemClick " + voiceEntity.getmVoiceFileName());
                    VoiceUtils.getmInstance().play(voiceEntity.getmVoiceFileName());
                    break;
                case IMAGE:
                    break;
            }
        }
    }
    @Override
    protected void updateUserInfo(Map<String, UserInfo> userInfoList) {
    }

    private void initViews() {
        mChatListLayout = (RelativeLayout) this.findViewById(R.id.activity_room_chat_list_layout);

        mChatAdapter = new ChatAdapter(application, this, getMessages());
        mLVChat = (ListView) this.findViewById(R.id.activity_room_chat_list);
        mLVChat.setAdapter(mChatAdapter);

        mOptionLayout = (LinearLayout) this.findViewById(R.id.activity_room_option_layout);
        mVoiceLayout = (LinearLayout) this.findViewById(R.id.activity_room_voice_layout);
        mDetailLayout = (LinearLayout) this.findViewById(R.id.activity_room_detail_layout);

        mMenu = (ImageButton) this.findViewById(R.id.activity_room__ib_menu);
        mBtnTalk = (Button) this.findViewById(R.id.activity_room_btn_talk);
        mTalkLayout = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar_layout);
        mTalkBar = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar);
        mImpression = (LinearLayout) this.findViewById(R.id.activity_room_impression);
        mUserInfoLayout = (FrameLayout) this.findViewById(R.id.activity_room_bottom_layout);
        mBack = (ImageButton) this.findViewById(R.id.include_app_topbar_ib_change);
        mBack.setVisibility(View.INVISIBLE);
        ViewTreeObserver vto = mUserInfoLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mTalkLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int layoutHeight = mUserInfoLayout.getMeasuredHeight();

                        int height = mTalkBar.getMeasuredHeight();

                        Resources res = getResources();


                        int talkBarHeight = height + (int) (res.getDimension(R.dimen.chat_room_user_option_padding_top));

                        transHeight = layoutHeight - talkBarHeight;


                        if (mState == State.BOTTOM) {
                            ObjectAnimator.ofFloat(mTalkLayout, "translationY", 0).setDuration(2000).start();
                            mState = State.TOP;
                        } else if (mState == State.TOP) {
                            mState = State.BOTTOM;
                            ObjectAnimator.ofFloat(mTalkLayout, "translationY", transHeight).setDuration(0).start();
                        }

                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChatListLayout.getLayoutParams();
                        lp.setMargins(0, 0, 0, height);
                        mChatListLayout.setLayoutParams(lp);
                    }
                }
        );

        membersLayout = (LinearLayout) this.findViewById(R.id.activity_room_members_layout);
        talkBarLayout = (LinearLayout) this.findViewById(R.id.activity_room_talk_bar_layout);

        LinearLayout m0Layout = (LinearLayout) this.findViewById(R.id.activity_room_member0_layout);
        LinearLayout m1Layout = (LinearLayout) this.findViewById(R.id.activity_room_member1_layout);
        LinearLayout m2Layout = (LinearLayout) this.findViewById(R.id.activity_room_member2_layout);
        LinearLayout m3Layout = (LinearLayout) this.findViewById(R.id.activity_room_member3_layout);
        m0Layout.setOnClickListener(this);
        m1Layout.setOnClickListener(this);
        m2Layout.setOnClickListener(this);
        m3Layout.setOnClickListener(this);

        Member m0 = new Member();
        m0.avatar = (ImageView) this.findViewById(R.id.activity_room_iv_member0);
        m0.name = (TextView) this.findViewById(R.id.activity_room_tv_member0);
        members.put(R.id.activity_room_member0_layout, m0);
        mCurrFocusMember = R.id.activity_room_member0_layout;
        setMemberFocus(R.id.activity_room_member0_layout);

        Member m1 = new Member();
        m1.avatar = (ImageView) this.findViewById(R.id.activity_room_iv_member1);
        m1.name = (TextView) this.findViewById(R.id.activity_room_tv_member1);
        members.put(R.id.activity_room_member1_layout, m1);

        Member m2 = new Member();
        m2.avatar = (ImageView) this.findViewById(R.id.activity_room_iv_member2);
        m2.name = (TextView) this.findViewById(R.id.activity_room_tv_member2);
        members.put(R.id.activity_room_member2_layout, m2);

        Member m3 = new Member();
        m3.avatar = (ImageView) this.findViewById(R.id.activity_room_iv_member3);
        m3.name = (TextView) this.findViewById(R.id.activity_room_tv_member3);
        members.put(R.id.activity_room_member3_layout, m3);

        mVoiceLayout.setVisibility(View.VISIBLE);
        mOptionLayout.setVisibility(View.GONE);

        for (int index = 1; index < 4; index++) {
            members.get(MemberIds[index]).name.setText("empty");
            members.get(MemberIds[index]).avatar.setVisibility(View.INVISIBLE);
        }
    }

    private void initEvents() {
        mMenu.setOnClickListener(this);
        mBtnTalk.setOnLongClickListener(this);
        mBtnTalk.setOnTouchListener(new OnTalkBtnTouch());
        mDetailLayout.setOnClickListener(this);
      //  mLVChat.setOnItemClickListener(new OnChatListItemClick());
    }

    private void init() {
        application = (BaseApplication) getApplication();
        VoiceUtils.getmInstance().setOnVoiceRecordListener(new OnVoiceRecordListenerImpl());
        VoiceUtils.getmInstance().setOnVoicePlayListener(new OnVoicePlayListenerImpl());
//        if (previewScroll.getScrollY() != 0) {
//            previewScroll.smoothScrollTo(0, 0);
//        }
//
//        reLayoutScroll();
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
                Intent intent = new Intent(ActivityRoom.this, ActivitySetting.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                //startActivity(ActivitySetting.class,null);
                break;
            case R.id.activity_room_detail_layout:
                if (mState == State.BOTTOM) {
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY", 0).setDuration(2000).start();
                    mState = State.TOP;
                } else if (mState == State.TOP) {
                    mState = State.BOTTOM;
                    ObjectAnimator.ofFloat(mTalkLayout, "translationY", transHeight).setDuration(2000).start();
                }
                break;
            case R.id.activity_room_member0_layout:
            case R.id.activity_room_member1_layout:
            case R.id.activity_room_member2_layout:



                setMemberFocus(v.getId());
               // clearMessage();
                if(mCurrFocusMember==v.getId()){
                    if(mIsVoiceBtnShow){

                        mVoiceLayout.setVisibility(View.GONE);
                        mOptionLayout.setVisibility(View.VISIBLE);
                    } else {
                        mVoiceLayout.setVisibility(View.VISIBLE);
                        mOptionLayout.setVisibility(View.GONE);
                    }
                    mIsVoiceBtnShow = !mIsVoiceBtnShow;
                }
                mCurrFocusMember = v.getId();
                break;
            case R.id.activity_room_member3_layout:
                setMemberFocus(v.getId());
                mVoiceLayout.setVisibility(View.VISIBLE);
                mOptionLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void setMemberFocus(int id) {
        Set<Integer> set = members.keySet();
        Iterator<Integer> ite = set.iterator();
        while (ite.hasNext()) {
            int key = ite.next();
            if (key == id) {
                members.get(key).name.setTextColor(getResources().getColor(R.color.red));
            } else {
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

                    if(!DEBUG)
                         VoiceUtils.getmInstance().startRecord(mVoiceFileName);
                }
                break;
        }

        return true;
    }

    enum State {
        TOP,
        BOTTOM
    }

    class Member {
        ImageView avatar;
        TextView name;
    }

    ///mHeaderHeight
    class OnTalkBtnTouch implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            }

            if (isTalking) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {


                        if(DEBUG) {
                            afterTime = System.currentTimeMillis();
                            isTalking = false;

                            new OnVoiceRecordListenerImpl().onResult("");
                        }
                        else {
                            recordStop();
                        }
                    }
                    break;

                }
            }
            return false;
        }
    }

    class OnVoicePlayListenerImpl implements VoiceUtils.OnVoicePlayListener {

        @Override
        public void onPlayStop() {

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

            if (DEBUG) {
                sendMessage("" + "&"
                        + (afterTime - beforeTime) / 1000);

                return;
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


        }
    }

    public void recordStop() {
        Log.v("chatActivity", "stop record");
        afterTime = System.currentTimeMillis();
        isTalking = false;
        VoiceUtils.getmInstance().stop();
    }

    class RoomSparkListener extends SimpleSparkListener {
        @Override
        public void onReceiveMessage(String from, String body, String subject) {
        }

        @Override
        public void onJoin(int index, com.veiljoy.spark.core.UserInfo[] users) {
            if (index == 0) {
                // room owner
                TextView name = (TextView) findViewById(R.id.common_header_layout_title);
                name.setText(users[index].getNickname());
            } else {
                final int ids[] = {
                        0,
                        R.id.activity_room_member0_layout,
                        R.id.activity_room_member1_layout,
                        R.id.activity_room_member2_layout
                };
                members.get(ids[index]).name.setText(users[index].getNickname());
                members.get(ids[index]).avatar.setVisibility(View.VISIBLE);
                members.get(ids[index]).avatar.setImageBitmap(FormatTools.Bytes2Bitmap(users[index].getAvatar()));
            }
        }

        @Override
        public void onLeft(int index, com.veiljoy.spark.core.UserInfo[] users) {
            if (index > 0) {
                final int ids[] = {
                        0,
                        R.id.activity_room_member0_layout,
                        R.id.activity_room_member1_layout,
                        R.id.activity_room_member2_layout
                };
                members.get(ids[index]).name.setText("empty");
                members.get(ids[index]).avatar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
