package com.desktop.telephone.telephonedesktop.desktop.recevier;

/**
 * 黑名单短信拦截
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.bean.SystemStatusBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;

import java.util.List;

/**
 * 短信接收者
 *
 */
public class SmsRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
//            abortBroadcast();
//            // 接收有SMS传递的数据
//            Bundle bundle = intent.getExtras();
//            // 判断是否有数据
//            if (bundle != null) {
//                // 通过pdus可以获得接收到的所有短信消息
//                Object[] pdus = (Object[]) bundle.get("pdus");
//                // 构建短信对象array,并依据收到的对象长度来创建array的大小
//                SmsMessage[] messages = new SmsMessage[pdus.length];
//                for (int i = 0; i < pdus.length; i++) {
//                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                }
//                for (SmsMessage message : messages) {
//                    // 获得短信来源
//                    String address = message.getDisplayOriginatingAddress();
//                    if (address.startsWith("+86")) {
//                        address = address.substring(address.indexOf("+86")
//                                + "+86".length());
//                    }
//                    List<SystemStatusBean> systemStatusBeans = DaoUtil.getSystemStatusBeanDao().loadAll();
//                    if(systemStatusBeans != null && systemStatusBeans.size() != 0) {
//                        int blackListModeType = systemStatusBeans.get(0).getBlackListModeType();
//                        List<BlackListInfoBean> blackListInfoBeans = DaoUtil.getBlackListInfoBeanDao().loadAll();
//                        if(blackListModeType == 0) {//普通模式,不拦截
//                            return;
//                        }else if(blackListModeType == 1) { //黑名单模式,拦截黑名单中的号码
//                            for (int i = 0; i < blackListInfoBeans.size(); i++) {
//                                if(blackListInfoBeans.get(i).getType() == 1) {//黑名单列表
//                                    if(blackListInfoBeans.get(i).getPhone().equals(address)) {
//                                        // 中止发送通知
//                                        abortBroadcast();
//                                    }
//                                }
//                            }
//
//                        }else {//白名单模式,拦截非白名单所有号码
//                            for (int i = 0; i < blackListInfoBeans.size(); i++) {
//                                if(blackListInfoBeans.get(i).getType() == 2) {
//                                    if(!blackListInfoBeans.get(i).getPhone().equals(address)) {//只要不是白名单号码 全部拦截
//                                        // 中止发送通知
//                                        abortBroadcast();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                DaoUtil.closeDb();
//            }
        }
    }
}