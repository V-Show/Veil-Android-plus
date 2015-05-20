package com.veiljoy.veil.imImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageVoiceEntity;



/**
 * Created by zhongqihong on 15/4/2.
 */
public class IMMessageVoiceItem extends IMMessageImageBaseItem implements View.OnLongClickListener,View.OnClickListener{

    protected Bitmap mBitmap;
    protected ImageView mIvImage;
    private IMMessageVoiceEntity mVoiceEntity;
    private ImageView mIVTalking;
    private AnimationDrawable mAnimation;
    private int mProgress;


    public IMMessageVoiceItem(IMMessage msg, Context context) {
        super(msg, context);
        mVoiceEntity = (IMMessageVoiceEntity) msg;



    }

    protected void fillStatus(){
        super.fillStatus();

        View view = mInflater.inflate(R.layout.message_status, null);

        mLayoutMsgStatusContainer.addView(view);

        mIVTalking=(ImageView)view.findViewById(R.id.message_status_iv_talking);



    }


    Handler mHandler = new Handler() {


        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    startLoadingAnimation();
                    break;

                case 1:
                    updateLoadingProgress();
                    break;

                case 2:
                    stopLoadingAnimation();
                    break;
            }
        }

    };

    protected  void onFillMessage(){
        super.onFillMessage();
        setmOnClickListener(this);
        setmOnLongClickListener(this);
    }

    @Override
    public void  onStatusChanged(int status) {

        if(status==0){
            mHandler.sendEmptyMessage(0);
        }

    }

    private void startLoadingAnimation() {
        mLayoutMsgStatusContainer.setVisibility(View.VISIBLE);
        mAnimation = new AnimationDrawable();
        mAnimation.addFrame(getDrawable(R.drawable.ic_talking_right_01), 300);
        mAnimation.addFrame(getDrawable(R.drawable.ic_talking_right_02), 300);
        mAnimation.addFrame(getDrawable(R.drawable.ic_talking_right_03), 300);
        mAnimation.setOneShot(false);
        mIVTalking.setImageDrawable(mAnimation);
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mHandler.sendEmptyMessage(1);
    }

    private void stopLoadingAnimation() {
        if (mAnimation != null) {
            mAnimation.stop();
            mAnimation = null;
        }

        if(mIvImage!=null){
            mIvImage.setVisibility(View.VISIBLE);
            if (mBitmap != null) {
                mIvImage.setImageBitmap(mBitmap);
            }
        }
        mLayoutMsgStatusContainer.setVisibility(View.GONE);

    }

    private void updateLoadingProgress() {
        if (mProgress < 100) {
            mProgress++;
            mHandler.sendEmptyMessageDelayed(1, 100);
        } else {
            mProgress = 0;
            mHandler.sendEmptyMessage(2);
        }
    }
    @SuppressWarnings("deprecation")
    private Drawable getDrawable(int id) {
        return new BitmapDrawable(BitmapFactory.decodeResource(
                mContext.getResources(), id));
    }

    @Override
    public void initImages() {
        /*根据语音时间长短去判断使用图片*/

        if (mVoiceEntity.getmVoiceTimeRange() > 25) {
            mVoiceResId = R.mipmap.ic_voice_img_1;
        } else if (mVoiceEntity.getmVoiceTimeRange() > 15) {
            mVoiceResId = R.mipmap.ic_voice_img_2;
        }
        else if (mVoiceEntity.getmVoiceTimeRange() > 10) {
            mVoiceResId = R.mipmap.ic_voice_img_3;
        }
        else if (mVoiceEntity.getmVoiceTimeRange() > 5) {
            mVoiceResId = R.mipmap.ic_voice_img_4;
        }
        else {
            mVoiceResId = R.mipmap.ic_voice_img_5;
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
