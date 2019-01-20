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
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            PackageManager pm = context.getPackageManager();
            String packageName = intent.getDataString().substring(8);
            //所有的安装在系统上的应用程序包信息。
            List<PackageInfo> packInfos = pm.getInstalledPackages(0);
            for (int i = 0; i < packInfos.size(); i++) {
                PackageInfo packInfo = packInfos.get(i);
                //packInfo  相当于一个应用程序apk包的清单文件
                String packname = packInfo.packageName;
                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                String name = packInfo.applicationInfo.loadLabel(pm).toString();

                if (packname.equals(packageName)) {
                    //添加到所有应用数据库
                    AppInfoBean appInfoBean = new AppInfoBean();
                    appInfoBean.setIconType(2);
                    appInfoBean.setPackageName(packname);
                    appInfoBean.setAppIcon(DaoUtil.drawableToByte(icon));
                    appInfoBean.setAppName(name);
                    appInfoBean.setId(null);
                    appInfoBean.setIsShowDesktop(true);
                    appInfoBean.setAppName(name);
                    DaoUtil.getAppInfoBeanDao().insert(appInfoBean);

//                    添加到桌面数据库
                    DesktopIconBean desktopIconBean = new DesktopIconBean();
                    desktopIconBean.setIconType(2);
                    desktopIconBean.setTitle(name);
                    desktopIconBean.setPackageName(packname);
                    List<DesktopIconBean> list = DaoUtil.getDesktopIconBeanDao().loadAll();
                    desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(list.size()));
                    if (packageName.equals("com.tencent.mm")) {//是微信，安装到第二个位置
                        desktopIconBean.setImg_id_name("weixin_icon");
                        desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(2));
                        desktopIconBean.setMid(2);
                        for (int j = 0; j < list.size(); j++) {
                            if(list.get(j).getMid() >= 2) {//其他往后移
                                list.get(j).setMid(list.get(j).getMid()+1);
                                DaoUtil.getDesktopIconBeanDao().update(list.get(j));
                            }
                        }
                    } else {
                        desktopIconBean.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                        desktopIconBean.setMid(list.size());
                    }
                    DaoUtil.getDesktopIconBeanDao().insert(desktopIconBean);

                    EventBus.getDefault().post(new EventBean(EventBean.REFRESH_DESK));
                }
            }
        }
    }
}
