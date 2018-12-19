package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.desktop.telephone.telephonedesktop.desktop.service.ScreenBannerService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ScreenBannerService.class);
        context.startService(i);
    }
}
