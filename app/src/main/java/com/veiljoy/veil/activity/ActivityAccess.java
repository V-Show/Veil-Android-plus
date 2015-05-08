package com.veiljoy.veil.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;
import com.veiljoy.veil.android.view.CommonLoadBar;

/**
 * Created by zhongqihong on 15/5/5.
 */
public class ActivityAccess extends BaseActivity {

    CommonLoadBar mCommonLoadBar;
    int mOption=0;
    EditText mETrepeatPsw;
    RadioGroup mOptionRG;
    boolean isStarted=false;
    Button mBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bottom_bar);

//        initViews();
//        initEvents();
//        init();

    }

    public void initViews(){
        mETrepeatPsw=(EditText)this.findViewById(R.id.activity_access_repeat_password);
        mOptionRG=(RadioGroup)this.findViewById(R.id.activity_access_radiogroup);
        mCommonLoadBar=(CommonLoadBar)this.findViewById(R.id.activity_access_load);
        mBtnConfirm=(Button)this.findViewById(R.id.activity_access_btn_confirm);
    }

    public void initEvents(){
        mOptionRG.setOnCheckedChangeListener(new OnOptionCheckedChangeListener());
        mBtnConfirm.setOnClickListener(new OnViewClickedListener());

    }

    public void init(){
       // mCommonLoadBar.startLoad();
    }

    class OnViewClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.activity_access_btn_confirm:
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


    class OnOptionCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.activity_access_sign_in:
                    mOption = 0;
                    mETrepeatPsw.setVisibility(View.GONE);
                    break;
                case R.id.activity_access_sign_up:
                    mETrepeatPsw.setVisibility(View.VISIBLE);
                    mOption = 1;
                    break;
            }
        }
    }
}
