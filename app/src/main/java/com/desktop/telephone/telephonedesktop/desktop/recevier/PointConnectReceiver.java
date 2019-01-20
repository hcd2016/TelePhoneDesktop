package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.desktop.service.ScreenBannerService;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 点播连接成功
 */
public class PointConnectReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String substring = phoneNumber.substring(0, 1);
        if(substring.equals(SPUtil.getString(SPUtil.KEY_INTERCHANGER_SETTING))) {
            CallUtil.call(context,phoneNumber.substring(1));
        }else {
            CallUtil.call(context,phoneNumber);
        }
        EventBus.getDefault().post(intent);
    }
}
