package com.veiljoy.veil.register;

import java.util.regex.Pattern;

import com.veiljoy.veil.activity.ActivityUserInfo;
import com.veiljoy.veil.android.BaseApplication;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

public abstract class RegisterStep {
	protected ActivityUserInfo mActivity;
	protected Context mContext;
	private View mContentRootView;
	protected onNextActionListener mOnNextActionListener;

	public RegisterStep(ActivityUserInfo activity, View contentRootView) {
		mActivity = activity;
		mContext = (Context) mActivity;
		mContentRootView = contentRootView;

	}

	public abstract void initViews();

	public abstract void initEvents();

	public abstract boolean validate();

	public abstract boolean isChange();

	public View findViewById(int id) {
		return mContentRootView.findViewById(id);
	}

	public void doPrevious() {

	}

	public void doNext() {

	}

	public void nextAnimation() {

	}

	public void preAnimation() {

	}

	protected boolean isNull(EditText editText) {
		String text = editText.getText().toString().trim();
		if (text != null && text.length() > 0) {
			return false;
		}
		return true;
	}

	protected boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	protected boolean matchEmail(String text) {
		if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
				.matches()) {
			return true;
		}
		return false;
	}
    protected void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
        mActivity.putAsyncTask(asyncTask);
    }


	protected void showCustomToast(String text) {
		mActivity.showCustomToast(text);
	}


	protected int getScreenWidth() {
		return mActivity.getScreenWidth();
	}

	protected BaseApplication getBaseApplication() {
		return mActivity.getBaseApplication();
	}

	public void setOnNextActionListener(onNextActionListener listener) {
		mOnNextActionListener = listener;
	}

	public interface onNextActionListener {
		void next();
	}
}
