package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

/**
 * 去电接通广播
 */
public class CallingConnectReciver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(intent);
    }
}
