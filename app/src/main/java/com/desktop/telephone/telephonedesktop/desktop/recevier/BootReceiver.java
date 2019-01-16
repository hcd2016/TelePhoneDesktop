package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        //接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。
//        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
//            String dataString = intent.getDataString();
//            String packageName = intent.getDataString().substring(8);
//            PackageManager pm = context.getPackageManager();
//            //所有的安装在系统上的应用程序包信息。
//            List<PackageInfo> packInfos = pm.getInstalledPackages(0);
//
////        for (PackageInfo packInfo : packInfos) {
//
//            for (int i = 0; i < packInfos.size(); i++) {
//                PackageInfo packInfo = packInfos.get(i);
//                AppInfoBean appInfo = new AppInfoBean();
//                //packInfo  相当于一个应用程序apk包的清单文件
//                String packname = packInfo.packageName;
//                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
//                String name = packInfo.applicationInfo.loadLabel(pm).toString();
//                if(packageName.equals(packname)) {
//                    //添加到桌面数据库
//                    DesktopIconBean desktopIconBean = new DesktopIconBean();
//                    desktopIconBean.setIconType(2);
//                    desktopIconBean.setTitle(name);
//                    desktopIconBean.setPackageName(packname);
//                    List<DesktopIconBean> list = DaoUtil.getDesktopIconBeanDao().loadAll();
//                    desktopIconBean.setMid(list.size());
//                    desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(list.size()));
//                    if(packageName.equals("com.tencent.mm")) {
//                        desktopIconBean.setImg_id_name("weixin_icon");
//                    }else {
//                        desktopIconBean.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
//                    }
//                    DaoUtil.getDesktopIconBeanDao().insert(desktopIconBean);
//
//
//                    //添加到所有应用数据库
//                    AppInfoBean appInfoBean = new AppInfoBean();
//                    appInfo.setPackageName(packname);
//                    appInfo.setAppIcon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
//                    appInfo.setAppName(name);
//                    appInfo.setId(null);
//                    appInfo.isShowDesktop = true;
//                    DaoUtil.getAppInfoBeanDao().insert(appInfoBean);
//                    EventBus.getDefault().post(EventBean.REFRESH_DESK);
//                }
//            }
//
//        }
    }
}
