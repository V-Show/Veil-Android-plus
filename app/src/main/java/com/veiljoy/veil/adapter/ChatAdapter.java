package com.veiljoy.veil.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.veiljoy.veil.android.BaseApplication;
import com.veiljoy.veil.bean.BaseInfo;
import com.veiljoy.veil.im.IMMessage;
import com.veiljoy.veil.im.IMMessageFactory;
import com.veiljoy.veil.im.IMMessageVoiceEntity;
import com.veiljoy.veil.imImpl.IMMessageItem;
import com.veiljoy.veil.utils.VoiceUtils;

public class ChatAdapter extends BaseObjectListAdapter {

    public ChatAdapter(BaseApplication application, Context context,
                       List<? extends BaseInfo> datas) {
        super(application, context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        final IMMessage msg = (IMMessage) getItem(position);

        if (msg != null) {

            final IMMessageItem msgItem = IMMessageFactory.getInstance().getMessageItem(msg, mContext);
            msgItem.fillContent();
            view = msgItem.getRootView();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String uri = msg.getmUri();
                    switch (IMMessage.Scheme.ofUri(uri)) {
                        case VOICE:
                            IMMessageVoiceEntity voiceEntity = (IMMessageVoiceEntity) msg;
                            Log.v("chatActivity", "OnChatListItemClick " + voiceEntity.getmVoiceFileName());
                            VoiceUtils.getmInstance().play(voiceEntity.getmVoiceFileName());
                            break;
                        case IMAGE:
                            break;
                    }
                    msg.setmRead(1);
                    msgItem.onStatusChanged(0);
                }
            });
        }


        return view;
    }


    public void refreshList(List<IMMessage> items) {
        this.mDatas = items;
        this.notifyDataSetChanged();
    }
}
