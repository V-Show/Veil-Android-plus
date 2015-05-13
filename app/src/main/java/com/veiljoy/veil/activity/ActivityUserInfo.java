package com.veiljoy.veil.activity;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.view.BaseDialog;
import com.veiljoy.veil.register.RegisterStep;
import com.veiljoy.veil.register.StepBaseInfo;
import com.veiljoy.veil.register.StepCharacter;
import com.veiljoy.veil.register.StepVoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqihong on 15/5/8.
 */
public class ActivityUserInfo extends BaseActivity implements View.OnClickListener,
        RegisterStep.onNextActionListener {

    private BaseDialog mBackDialog;
    private ViewFlipper mVfFlipper;
    private Button mBtnPrevious;
    private Button mBtnNext;
    private RegisterStep mCurrentStep;
    private int mCurrentStepIndex = 1;
    private StepBaseInfo mStepBaseInfo=null;
    private StepCharacter mStepCharacter=null;
    private StepVoice mStepVoice=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initViews();
        initEvents();
        init();

    }

    private void init() {
        initBackDialog();
        mCurrentStep = initStep();
        mCurrentStep.setOnNextActionListener(this);
    }

    private void initEvents() {

        mBtnPrevious.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    private void initViews() {

        mVfFlipper=(ViewFlipper)this.findViewById(R.id.activity_guide_vf_viewflipper);
        mVfFlipper.setDisplayedChild(0);
        mBtnPrevious = (Button) findViewById(R.id.reg_btn_previous);
        mBtnNext = (Button) findViewById(R.id.reg_btn_next);


    }
    private void doPrevious() {
        mCurrentStepIndex--;
        mCurrentStep = initStep();
        mCurrentStep.setOnNextActionListener(this);
        mVfFlipper.setInAnimation(this, R.anim.push_right_in);
        mVfFlipper.setOutAnimation(this, R.anim.push_right_out);
        mVfFlipper.showPrevious();

    }


    private RegisterStep initStep() {
        switch (mCurrentStepIndex) {
            case 1:
                if (mStepBaseInfo == null) {
                    mStepBaseInfo = new StepBaseInfo(this, mVfFlipper.getChildAt(0));
                }
                mBtnPrevious.setText("返    回");
                mBtnNext.setText("下一步");
                return mStepBaseInfo;

            case 2:
                if (mStepCharacter == null) {
                    mStepCharacter = new StepCharacter(this, mVfFlipper.getChildAt(1));
                }
                mBtnPrevious.setText("上一步");
                mBtnNext.setText("下一步");
                return mStepCharacter;

            case 3:
                if (mStepVoice == null) {
                    mStepVoice = new StepVoice(this,
                            mVfFlipper.getChildAt(2));
                }
              //  mHeaderLayout.setTitleRightText("设置密码", null, "3/6");
                mBtnPrevious.setText("跳过");
                mBtnNext.setText("下一步");
                return mStepVoice;


        }
        return null;
    }

    private void doNext() {
        if (mCurrentStep.validate()) {
            if (mCurrentStep.isChange()) {
                mCurrentStep.doNext();
            } else {
                next();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.reg_btn_previous:
                if (mCurrentStepIndex <= 1) {
                    mBackDialog.show();
                } else {
                    doPrevious();
                }
                break;

            case R.id.reg_btn_next:
                if (mCurrentStepIndex < 3) {
                    doNext();
                } else {
                    if (mCurrentStep.validate()) {
                        mCurrentStep.doNext();
                    }
                }
                break;
        }
    }

    @Override
    public void next() {
        mCurrentStepIndex++;
        mCurrentStep = initStep();
        mCurrentStep.setOnNextActionListener(this);
        mVfFlipper.setInAnimation(this, R.anim.push_left_in);
        mVfFlipper.setOutAnimation(this, R.anim.push_left_out);
        mVfFlipper.showNext();
    }
    private void initBackDialog() {
        mBackDialog = BaseDialog.getDialog(ActivityUserInfo.this, "提示",
                "确认要放弃注册么?", "确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }, "取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        mBackDialog.setButton1Background(R.drawable.btn_bg_confirm);

    }
    @Override
    public void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
        super.putAsyncTask(asyncTask);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentStepIndex <= 1) {
            mBackDialog.show();
        } else {
            doPrevious();
        }
    }
}
