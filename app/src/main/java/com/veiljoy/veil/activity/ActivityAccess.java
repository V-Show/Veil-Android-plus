package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.view.CommonLoadBar;
import com.veiljoy.veil.utils.SharePreferenceUtil;
import com.veiljoy.veil.utils.StringUtils;

/**
 * Created by zhongqihong on 15/5/5.
 */
public class ActivityAccess extends BaseActivity {
    CallBackProcessor mCallBackProcessor;
    CommonLoadBar mCommonLoadBar;
    int mOption=0;
    EditText mEtRePwd;
    RadioGroup mOptionRG;
    boolean isStarted=false;
    Button mBtnConfirm;
    EditText mETUserName;
    EditText mEtPwd;
    String mAccount=null;
    String pwd = null;
    String rePwd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        initViews();
        initEvents();
        init();

    }

    public void initViews(){
        mETUserName=(EditText)this.findViewById(R.id.activity_access_username);
        mEtPwd=(EditText)this.findViewById(R.id.activity_access_password);
        mEtRePwd=(EditText)this.findViewById(R.id.activity_access_repeat_password);
        mOptionRG=(RadioGroup)this.findViewById(R.id.activity_access_radiogroup);
        mCommonLoadBar=(CommonLoadBar)this.findViewById(R.id.activity_access_load);
        mBtnConfirm=(Button)this.findViewById(R.id.activity_access_btn_confirm);
    }

    public void clearUp(){
        mETUserName.setText("");
        mEtPwd.setText("");
        mEtRePwd.setText("");
    }



    public void initEvents(){
        mOptionRG.setOnCheckedChangeListener(new OnOptionCheckedChangeListener());
        mBtnConfirm.setOnClickListener(new OnViewClickedListener());

    }

    public void init(){
        mCallBackProcessor=new CallBackProcessor();
    }

    class OnViewClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.activity_access_btn_confirm:

                    if(!confirm())
                    {
                        return ;
                    }

                    isStarted=!isStarted;

                    if(isStarted){

                        mCommonLoadBar.setVisibility(View.VISIBLE);
                        mCommonLoadBar.startLoad();
                    }
                    else{
                        mCommonLoadBar.stopLoad();
                    }



                    break;
            }

        }
    }


    public boolean confirm(){
        if(!validateAccount()){
            return false;
        }
                    /*
                    * trying to log in
                    * */
        if(mOption==0){
            if(!validatePsw()){
                return false;
            }
        }
                    /*
                    * trying to log out
                    * */

        if(mOption==1){
            if(!validateRePsw()){
                return false;
            }
        }

        return true;
    }


    class OnOptionCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.activity_access_sign_in:
                    mOption = 0;
                    clearUp();
                    mEtRePwd.setVisibility(View.GONE);
                    break;
                case R.id.activity_access_sign_up:
                    mEtRePwd.setVisibility(View.VISIBLE);
                    mOption = 1;
                    clearUp();
                    break;
            }
        }
    }


    private boolean validateAccount() {
        mAccount = null;
        if (StringUtils.isNull(mETUserName)) {
            showCustomToast("请输入不见号码或登录邮箱");
            mETUserName.requestFocus();
            return false;
        }
        String account = mETUserName.getText().toString().trim();

        if (account.length() < 3) {
            showCustomToast("请输入大于3位的账号");
            return false;
        }

        if (StringUtils.matchEmail(account)) {
            mAccount = account;
            return true;
        }
        if (StringUtils.matchNumber(account)) {
            mAccount = account;
            return true;
        }
        if (StringUtils.matchAccount(account)) {
            mAccount = account;
            return true;
        }

        showCustomToast("账号格式不正确");
        mETUserName.requestFocus();
        return false;
    }

    private boolean validateRePsw(){

        if(!validatePsw()){
            return false;
        }

        if (StringUtils.isNull(mEtRePwd)) {
            showCustomToast("请重复输入一次密码");
            mEtRePwd.requestFocus();
            return false;
        } else {
            rePwd = mEtRePwd.getText().toString().trim();
            pwd = mEtPwd.getText().toString().trim();
            if (!pwd.equals(rePwd)) {
                showCustomToast("两次输入的密码不一致");
                mEtRePwd.requestFocus();
                return false;
            }
        }
        SharePreferenceUtil.setPasswd(mEtRePwd.getText().toString().trim());

        return true;
    }

    private boolean validatePsw(){

        if (StringUtils.isNull(mEtPwd)) {
            showCustomToast("请输入密码");
            mEtPwd.requestFocus();
            return false;
        } else {
            pwd = mEtPwd.getText().toString().trim();
            if (pwd.length() < 6) {
                showCustomToast("密码不能小于6位");
                mEtPwd.requestFocus();
                return false;
            }
        }


        return true;
    }

    public static interface SignUpCallBack{

        public void postSignUpResult(int code);

    }
    public static interface SignInCallBack{

        public void postSignInResult(int code);

    }

    class CallBackProcessor implements SignInCallBack,SignUpCallBack{

        @Override
        public void postSignInResult(int code) {

        }

        @Override
        public void postSignUpResult(int code) {

        }
    }
}
