package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 手柄放下
 */
public class HandOffReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SPUtil.getInstance().saveInteger(SPUtil.KEY_HAND_STATUS,0);
        EventBus.getDefault().post(intent);
    }
}
