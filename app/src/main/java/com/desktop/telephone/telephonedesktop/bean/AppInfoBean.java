package com.desktop.telephone.telephonedesktop.bean;

import android.graphics.drawable.Drawable;

public class AppInfoBean {

    public String appName;
    public Drawable appIcon;
    public String packageName;
    public int uid;
    public boolean userApp;
    public boolean inRom;
    public boolean isShowDesktop;//是否显示在桌面

    public boolean isShowDesktop() {
        return isShowDesktop;
    }

    public void setShowDesktop(boolean showDesktop) {
        isShowDesktop = showDesktop;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
