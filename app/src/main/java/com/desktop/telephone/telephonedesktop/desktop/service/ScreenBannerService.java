package com.desktop.telephone.telephonedesktop.desktop.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.desktop.telephone.telephonedesktop.desktop.recevier.AlarmReceiver;

/**
 * 开启轮播屏保
 */
public class ScreenBannerService extends Service {
    public ScreenBannerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*每次调用startService启动该服务都会执行*/
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long triggerTime = SystemClock.elapsedRealtime() + 60000;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

}
