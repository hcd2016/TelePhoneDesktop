package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * 来电广播
 */
public class IncomingCallingRecevie extends BroadcastReceiver {
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra("phoneNumber");
        Intent intent1 = new Intent(context, CallingActivity.class);
        intent1.putExtra("phoneNum", phoneNumber);
        intent1.putExtra("isCalling", false);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
//        wakeLock.acquire(10000); // 点亮屏幕
//        wakeLock.release(); // 释放
        wakeUpAndUnlock(context);
    }

    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context
                .getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }
}
