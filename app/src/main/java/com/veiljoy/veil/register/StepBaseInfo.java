package com.veiljoy.veil.register;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.veiljoy.veil.R;
import com.veiljoy.veil.activity.ActivityUserInfo;
import com.veiljoy.veil.android.view.BaseDialog;
import com.veiljoy.veil.utils.SharePreferenceUtil;

/**
 * Created by zhongqihong on 15/5/12.
 */
public class StepBaseInfo extends RegisterStep {

    private EditText mEtName;
    private RadioGroup mRgGender;
    private RadioButton mRbMale;
    private RadioButton mRbFemale;
    private BaseDialog mBaseDialog;
    private boolean mIsChange = true;
    private boolean mIsGenderAlert;
    public StepBaseInfo(ActivityUserInfo activity, View contentRootView) {

        super(activity, contentRootView);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        mEtName = (EditText) findViewById(R.id.reg_baseinfo_et_name);
        mRgGender = (RadioGroup) findViewById(R.id.reg_baseinfo_rg_gender);
        mRbMale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_male);
        mRbFemale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_female);
    }

    @Override
    public void initEvents() {
        mRgGender.setOnCheckedChangeListener(new OnGenderCheckedListener());
    }

    @Override
    public boolean validate() {

        if (isNull(mEtName)) {
            showCustomToast("请输入用户名");
            mEtName.requestFocus();
            return false;
        }
        if (mRgGender.getCheckedRadioButtonId() < 0) {
            showCustomToast("请选择性别");
            return false;
        }
        SharePreferenceUtil.setName(mEtName.getText().toString().trim());
        return true;

    }

    @Override
    public boolean isChange() {
        return mIsChange;
    }

    @Override
    public void doNext() {
        mOnNextActionListener.next();
    }

    class  OnGenderCheckedListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mIsChange = true;
            int gender=0;
            if (!mIsGenderAlert) {
                mIsGenderAlert = true;
                mBaseDialog = BaseDialog.getDialog(mContext, "提示", "注册成功后性别将不可更改",
                        "确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                mBaseDialog.show();
            }
            switch (checkedId) {
                case R.id.reg_baseinfo_rb_male:
                    mRbMale.setChecked(true);
                    gender =0;
                    break;

                case R.id.reg_baseinfo_rb_female:
                    mRbFemale.setChecked(true);
                    gender=1;
                    break;

            }
            SharePreferenceUtil.setGender(gender);
        }
    }
}
