package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

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
    public static void call(Context context, String phoneNum) {//交换机待加
        boolean isHandFree = false;
        boolean isCallingWithTalking = SPUtil.getInstance().getBoolean(SPUtil.KEY_CALLING_WITH_TALKING, false);
        if (isCallingWithTalking) {
            Utils.Toast("当前正在通话中,不能呼出");
            return;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            Utils.Toast("呼叫号码不能为空");
            return;
        }
        int status = SPUtil.getInstance().getInteger(SPUtil.KEY_HAND_STATUS);
        if (status == 0) {//手柄未抬起就免提呼叫
            isHandFree = true;
        } else {
            isHandFree = false;
        }
        int i = SPUtil.getInstance().getInteger(SPUtil.KEY_INTERCHANGER_SETTING);
        Intent intent = new Intent();
        intent.setAction("com.tongen.Tel.APPLICATION_CALL");
        intent.putExtra("phoneNumber", i+"P" + phoneNum);
        intent.putExtra("isSecret", false);
        intent.putExtra("tag", "");
        context.sendBroadcast(intent);

        //跳转到呼叫中界面
        CallingActivity.startActivity(context, phoneNum, true, isHandFree);
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

    /**
     * 来电免提接听
     */
    public static void incommingAnswerWithFree(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.tongen.action.handfree.on");
        context.sendBroadcast(intent);
    }

    /**
     * 来电显示控制
     */
    public static void showCallerIds(Context context, int status) {
        Intent intent = new Intent();
        intent.putExtra("status", status);
        intent.setAction("com.tongen.action.set.showCallerIDs");
        context.sendBroadcast(intent);
    }

    /**
     * 交换机设置提交
     */
    public static void interchangerSetting(Context context, int time) {
        Intent intent = new Intent();
        intent.putExtra("time",time);
        intent.setAction("com.tongen.action.switch.time");
        context.sendBroadcast(intent);
    }
}
