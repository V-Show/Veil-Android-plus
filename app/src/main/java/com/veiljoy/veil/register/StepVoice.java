package com.veiljoy.veil.register;

import android.os.AsyncTask;
import android.view.View;

import com.veiljoy.veil.activity.ActivityRoom;
import com.veiljoy.veil.activity.ActivityUserInfo;

/**
 * Created by zhongqihong on 15/5/12.
 */
public class StepVoice extends RegisterStep {

    public StepVoice(ActivityUserInfo activity, View contentRootView) {
        super(activity, contentRootView);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
    }

    @Override
    public void doNext() {
        mOnNextActionListener.next();
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public boolean isChange() {
        return true;
    }
}
