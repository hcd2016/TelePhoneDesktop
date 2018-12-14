package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

/**
 * 来电未接广播
 */
public class CallingMissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(intent);
    }
}
