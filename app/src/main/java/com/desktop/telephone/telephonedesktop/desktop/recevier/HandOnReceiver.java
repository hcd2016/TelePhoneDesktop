package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;
import com.desktop.telephone.telephonedesktop.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 手柄抬起
 */
public class HandOnReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isShowCallingActivity = SPUtil.getInstance().getBoolean("isShowCallingActivity", false);
        SPUtil.getInstance().saveInteger(SPUtil.KEY_HAND_STATUS,1);
        if(!isShowCallingActivity) {//不是通话界面,显示拨号界面
            Intent intent1 = new Intent(context,CallActivity.class);
            intent1.putExtra("type",0);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        EventBus.getDefault().post(intent);
    }
}
