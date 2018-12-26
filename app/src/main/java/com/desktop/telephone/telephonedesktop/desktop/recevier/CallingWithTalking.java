package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

/**
 * 通话过程中拨打电话
 */
public class CallingWithTalking extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.Toast("您正在通话中...");
//        SPUtil.getInstance().saveBoolean(SPUtil.KEY_CALLING_WITH_TALKING,true);
    }
}
