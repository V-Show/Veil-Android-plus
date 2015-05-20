package com.veiljoy.veil.imImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.veiljoy.veil.R;
import com.veiljoy.veil.im.IMMessage;

/**
 * Created by zhongqihong on 15/4/2.
 */
public abstract class IMMessageImageBaseItem extends IMMessageItem {

    protected Bitmap mBitmap;
    protected ImageView mIvImage;
    protected int mVoiceResId;
    View.OnLongClickListener mOnLongClickListener;
    View.OnClickListener mOnClickListener;

    public IMMessageImageBaseItem(IMMessage msg, Context context) {

        super(msg, context);

    }

    protected  void setmOnLongClickListener(View.OnLongClickListener l){
        this.mOnLongClickListener=l;
        if(mOnLongClickListener!=null&&mIvImage!=null){
            mIvImage.setOnLongClickListener(mOnLongClickListener);
        }
    }
    protected  void setmOnClickListener(View.OnClickListener l){
        this.mOnClickListener=l;
        if(mOnClickListener!=null&&mIvImage!=null){
            mIvImage.setOnClickListener(mOnClickListener);
        }
    }

    public abstract void initImages();

    @Override
    protected void onInitViews() {

        View view = mInflater.inflate(R.layout.message_simple_image, null);
        mLayoutMessageContainer.addView(view);
        mIvImage = (ImageView) view.findViewById(R.id.message_iv_msg_simple_image);


        initImages();
    }

    @Override
    protected void onFillMessage() {


        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), mVoiceResId);

        //mBitmap = PhotoUtils.getBitmapFromFile(mMsg.getmContent());
        mIvImage.setImageBitmap(mBitmap);

    }
}
