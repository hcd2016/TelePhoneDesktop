package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;

/**
 * 电话相关广播发送工具类
 */
public class CallUtil {

    /**
     * 呼叫,直接拨出
     *
     * @param context
     * @param phoneNum
     */
    public static void call(Context context, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            Utils.Toast("呼叫号码不能为空");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.tongen.Tel.APPLICATION_CALL");
        intent.putExtra("phoneNumber", phoneNum);
        intent.putExtra("isSecret", false);
        intent.putExtra("tag", "");
        context.sendBroadcast(intent);

        //跳转到呼叫中界面
        CallingActivity.startActivity(context, phoneNum);
    }

    /**
     * 通话中,拨号键盘点拨
     */
    public static void callingKeyIn(Context context, String key) {
        Intent intent = new Intent();
        intent.setAction("com.tongen.Tel.POINT_IDLE");
        intent.putExtra("phoneNumber", key);
        context.sendBroadcast(intent);
    }

    /**
     * 打开或关闭免提,0为关闭,1为打开
     */
    public static void handFreeControl(Context context, int status) {
        Intent intent = new Intent();
        intent.setAction("com.tongen.HANDS_FREE");
        intent.putExtra("control", status);
        context.sendBroadcast(intent);
    }

    /**
     * 免提状态下挂断通话
     */
    public static void handUpWithFree(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.tongen.Tel.IDLE");
        context.sendBroadcast(intent);
    }
}
