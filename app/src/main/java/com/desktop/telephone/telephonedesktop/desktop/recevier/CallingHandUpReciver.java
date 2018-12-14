package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

public class CallingHandUpReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(intent);
    }
}
