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
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showCustomToast("请稍后,正在进入房间...");

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return true;

            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                mActivity.startActivity(ActivityRoom.class, null);

            }

        });
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
