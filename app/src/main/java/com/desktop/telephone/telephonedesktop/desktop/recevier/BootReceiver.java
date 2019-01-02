package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        //接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。
//        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
//            String packageName = intent.getDataString().substring(8);
//            if(packageName.equals("com.tencent.mm")) {//是微信
//                List<DesktopIconBean> list = DaoUtil.getDesktopIconBeanDao().loadAll();
//                for (int i = 0; i < list.size(); i++) {
//                    if(list.get(i).getPackageName().equals(""))
//                }
//            }
//
//        }
    }
}
