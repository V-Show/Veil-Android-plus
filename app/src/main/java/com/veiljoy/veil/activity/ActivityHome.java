package com.veiljoy.veil.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.android.view.BaseDialog;
import com.veiljoy.veil.im.IMUserBase;
import com.veiljoy.veil.imof.MUCHelper;
import com.veiljoy.veil.imof.MUCJoinTask;
import com.veiljoy.veil.imof.MUCRoomManager;
import com.veiljoy.veil.imof.UserAccessManager;
import com.veiljoy.veil.init.InitializationTask;
import com.veiljoy.veil.utils.AppStates;
import com.veiljoy.veil.utils.Constants;
import com.veiljoy.veil.utils.SharePreferenceUtil;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class ActivityHome extends BaseActivity {

    IMUserBase.OnUserLogin mUserLoginTask;
    ImageView ivLoadingLeft;
    ImageView ivLoadingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showWelcomeAnimation();
        init();
    }

    public void init() {
        if (AppStates.verifyAccount() && AppStates.verifyStates()) {
            if (AppStates.verifyStates())
                if (false) {
                    // 首先是否后台运行
//                    new InitializationTask(new OnReEnterMUC()).execute();
                } else {
                    new InitializationTask(new OnInitListener()).execute();
                }
        } else {
            startActivity(ActivityRegister.class, null);
        }
    }

    public void enter() {
        new UserLoginTask().execute();
    }

    class UserLoginTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            //showCustomToast("正在登录...");
            mUserLoginTask.preLogin();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String name = SharePreferenceUtil.getName();
            String psw = SharePreferenceUtil.getPasswd();

            int ret = mUserLoginTask.onLogin(name, psw);
            return ret;
        }

        @Override
        protected void onPostExecute(Integer code) {
            switch (code) {
                case Constants.LOGIN_SUCCESS: // 登录成功
                    //showCustomToast(R.string.login_success);
                    break;
                case Constants.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                    //showCustomToast(R.string.message_invalid_username_password);
                    break;
                case Constants.SERVER_UNAVAILABLE:// 服务器连接失败
                    //showCustomToast(R.string.message_server_unavailable);
                    break;
                case Constants.LOGIN_ERROR:// 未知异常
                    //showCustomToast(R.string.unrecoverable_error);
                    break;
            }

            boolean rel = mUserLoginTask.onLoginResult(code);
            if (!rel) {
                startActivity(ActivityRegister.class, null);
                finish();
            } else {
                new MUCJoinTask(null, ActivityHome.this).execute("");
            }
        }
    }

    private void enterChatRoom() {
        startActivity(ActivityChat.class, null);
    }

    private void showWelcomeAnimation() {
        ivLoadingLeft = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_left);
        ivLoadingRight = (ImageView) this.findViewById(R.id.activity_welcome_iv_loading_right);
        Animation animationLeft = AnimationUtils.loadAnimation(ActivityHome.this, R.anim.common_loading_zoom_left);

        ivLoadingLeft.startAnimation(animationLeft);

        Animation animationRight = AnimationUtils.loadAnimation(ActivityHome.this, R.anim.common_loading_zoom_right);

        ivLoadingRight.startAnimation(animationRight);
    }

    class OnInitListener implements InitializationTask.InitializationListener {
        @Override
        public void onResult(int code) {
            if (code != -1) {
                mUserLoginTask = new UserAccessManager();
                enter();
            }
        }

        @Override
        public int inBackground(int code) {
            return code;
        }
    }

//    class OnReEnterMUC implements InitializationTask.InitializationListener {
//        @Override
//        public void onResult(int code) {
//            if (code != -1) {
//                enterChatRoom();
//            }
//        }
//
//        @Override
//        public int inBackground(int code) {
//
//
//            MUCRoomManager.getInstance(ActivityHome.this).enterRoom();
//            return code;
//
//        }
//
//    }


}
