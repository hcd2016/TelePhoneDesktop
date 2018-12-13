package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;

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
        Intent intent = new Intent();
        intent.setAction("com.tongen.Tel.OUTGOING_RINGING");
        intent.putExtra("phoneNumber", phoneNum);
        intent.putExtra("tag", "");
        //发送无序广播
        context.sendBroadcast(intent);
        //跳转到呼叫中界面

    }

}
