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

//                    DesktopIconBean desktopIconBean = new DesktopIconBean();
//                    desktopIconBean.setId(null);
//                    desktopIconBean.setTitle(event.getAppName());
//                    desktopIconBean.setIconType(event.getIconType());
//                    desktopIconBean.setPhoneNum(event.getPhoneNum());
//                    if (event.getIconType() != 3) {
//                        desktopIconBean.setApp_icon(event.getAppIcon());
//                    }
//                    desktopIconBean.setMid(mList.size());
//                    desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(mList.size()));
//                    desktopIconBean.setPackageName(event.getPackageName());
////            mList.add(desktopIconBean);
//                    DaoUtil.getDesktopIconBeanDao().insert(desktopIconBean);

                    //添加到桌面数据库
                    List<DesktopIconBean> list = DaoUtil.getDesktopIconBeanDao().loadAll();
                    DesktopIconBean desktopIconBean = new DesktopIconBean();
                    desktopIconBean.setId(null);
                    desktopIconBean.setMid(list.size());
                    desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(list.size()));
                    desktopIconBean.setIconType(2);
                    desktopIconBean.setPackageName(packageName);
                    desktopIconBean.setTitle(name);
                    desktopIconBean.setApp_icon(DaoUtil.drawableToByte(icon));
                    DaoUtil.getDesktopIconBeanDao().insert(desktopIconBean);

                    EventBus.getDefault().post(new EventBean(EventBean.REFRESH_DESK));
                }
            }
        }
    }
}
