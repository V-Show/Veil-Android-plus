package com.veiljoy.veil.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.view.SwitcherButton;

/**
 * Created by zhongqihong on 15/5/8.
 */
public class ActivitySetting extends BaseActivity implements View.OnClickListener{

    private TextView mIVTitle;
    private TextView mIVSubTitle;
    private SwitcherButton mSbRightSwitcherButton;
    private SwitcherButton.onSwitcherButtonClickListener mSwitcherButtonClickListener;
    private LinearLayout mVoicePlayer;
    private ImageButton mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvent();
    }

    public void initViews(){
        mIVTitle=(TextView)this.findViewById(R.id.common_header_layout_title);
        mIVSubTitle=(TextView)this.findViewById(R.id.common_header_layout_subtitle);
        mIVTitle.setText("Setting");
        mIVSubTitle.setVisibility(View.GONE);

        mBack=(ImageButton)this.findViewById(R.id.include_app_topbar_ib_change);
        mSbRightSwitcherButton=(SwitcherButton)this.findViewById(R.id.activity_setting_voice_switcher);
        mVoicePlayer=(LinearLayout)this.findViewById(R.id.activity_setting_voice_play_layout);
        SwitcherButton.SwitcherButtonState state=mSbRightSwitcherButton.getState();
        if(state==SwitcherButton.SwitcherButtonState.RIGHT){
            mVoicePlayer.setVisibility(View.GONE);
        }
    }

    public void initEvent(){

        mBack.setOnClickListener(this);
        mSbRightSwitcherButton.setLeftText("ON");
        mSbRightSwitcherButton.setRightText("OFF");
        mSbRightSwitcherButton
                .setOnSwitcherButtonClickListener(new OnSwitcherButtonClickListener() );

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.include_app_topbar_ib_change:
                Intent data=new Intent();
                data.putExtra("datacount", (1));
                ActivitySetting.this.setResult(1, data);
                this.finish();

                break;
        }

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
