package com.desktop.telephone.telephonedesktop.desktop.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;

import com.desktop.telephone.telephonedesktop.desktop.recevier.AlarmReceiver;
import com.desktop.telephone.telephonedesktop.util.Utils;

import java.util.Calendar;
import java.util.Date;

import okhttp3.internal.Util;

/**
 * 开启轮播屏保
 */
public class ScreenBannerService extends Service {
    private static PowerManager.WakeLock wakeLock;
    public String beginTime = "7:00";
    public String endTime = "23:00";

    public ScreenBannerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //开关屏幕常亮
    public static void keepScreenOn(Context context, boolean on) {
        if (on) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
            wakeLock.acquire();
        } else {
            if (wakeLock != null) {
                wakeLock.release();
                wakeLock = null;
            }
        }
    }

    /*每次调用startService启动该服务都会执行*/
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 23 && hour >= 7) {//当前是白天时段,设为白天
            keepScreenOn(this, true);
        } else {//设为黑夜
            keepScreenOn(this, false);
        }
//        boolean b = Utils.isCurrentInTimeScope(7, 0, 23, 0);//判断当前时间是否在7:00~23:00时间段内

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 6*1000;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

}
