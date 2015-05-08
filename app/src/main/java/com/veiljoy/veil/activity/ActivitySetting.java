package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.view.SwitcherButton;

/**
 * Created by zhongqihong on 15/5/8.
 */
public class ActivitySetting extends BaseActivity {

    private SwitcherButton mSbRightSwitcherButton;
    private SwitcherButton.onSwitcherButtonClickListener mSwitcherButtonClickListener;
    private LinearLayout mVoicePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvent();
    }

    public void initViews(){
        mSbRightSwitcherButton=(SwitcherButton)this.findViewById(R.id.activity_setting_voice_switcher);
        mVoicePlayer=(LinearLayout)this.findViewById(R.id.activity_setting_voice_play_layout);
        SwitcherButton.SwitcherButtonState state=mSbRightSwitcherButton.getState();
        if(state==SwitcherButton.SwitcherButtonState.RIGHT){
            mVoicePlayer.setVisibility(View.GONE);
        }
    }

    public void initEvent(){
        mSbRightSwitcherButton.setLeftText("ON");
        mSbRightSwitcherButton.setRightText("OFF");
        mSbRightSwitcherButton
                .setOnSwitcherButtonClickListener(new OnSwitcherButtonClickListener() );

    }
    public class OnSwitcherButtonClickListener implements
            SwitcherButton.onSwitcherButtonClickListener {

        @Override
        public void onClick(SwitcherButton.SwitcherButtonState state) {

            switch (state) {
                case LEFT:
                    mVoicePlayer.setVisibility(View.VISIBLE);
                    break;

                case RIGHT:
                    mVoicePlayer.setVisibility(View.GONE);
                    break;
            }
        }

    }
}
