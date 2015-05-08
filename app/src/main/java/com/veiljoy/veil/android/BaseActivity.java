package com.veiljoy.veil.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.veiljoy.veil.R;

/**
 * Created by zhongqihong on 15/3/31.
 */
public class BaseActivity extends Activity implements IActivitySupport{
    /**
     * 屏幕的宽度、高度、密度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
    }

    /**
     * 含有Bundle通过Class跳转界面 *
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        this.finish();
    }


    /**
     * 显示自定义Toast提示(来自res) *
     */
    public  void showCustomToast(int resId) {
        View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
                R.layout.common_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text))
                .setText(getString(resId));
        Toast toast = new Toast(BaseActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    /**
     * 显示自定义Toast提示(来自String) *
     */
    public void showCustomToast(String text) {
        View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
                R.layout.common_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(BaseActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }



    protected void LOG(String log){
        Log.v(this.getClass().getName(),log);
    }

    @Override
    public void startService() {

    }

    @Override
    public void stopService() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //((BaseApplication)this.getApplication()).exit();
    }
    //宽
//    public int getViewWidth(LinearLayout view){
//        view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        return view.getMeasuredWidth();
//    }
//    //高
//    public int getViewHeight(LinearLayout view){
//        view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        return view.getMeasuredHeight();
//    }
}
