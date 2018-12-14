package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;

/**
 * 来电广播
 */
public class IncomingCallingRecevie extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra("phoneNumber");
        Intent intent1 = new Intent(context, CallingActivity.class);
        intent1.putExtra("phoneNum", phoneNumber);
        intent1.putExtra("isCalling", false);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
