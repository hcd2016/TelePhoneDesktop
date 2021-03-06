package com.desktop.telephone.telephonedesktop.bean;

import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AppInfoBean {
    @Id(autoincrement = true)
    public Long id;
    public int sortId;
    public String appName;
    public byte[] appIcon;
    public String packageName;
    public int uid;
    public boolean userApp;
    public boolean inRom;
    public boolean isShowDesktop;//是否显示在桌面
    public int iconType;////图标类型，0为系统应用，1为自定义添加,2为用户添加应用
    public String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1252385361)
    public AppInfoBean() {
    }

    @Generated(hash = 1356607411)
    public AppInfoBean(Long id, int sortId, String appName, byte[] appIcon,
            String packageName, int uid, boolean userApp, boolean inRom,
            boolean isShowDesktop, int iconType, String phoneNum) {
        this.id = id;
        this.sortId = sortId;
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.uid = uid;
        this.userApp = userApp;
        this.inRom = inRom;
        this.isShowDesktop = isShowDesktop;
        this.iconType = iconType;
        this.phoneNum = phoneNum;
    }

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

    public byte[] getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(byte[] appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getSortId() {
        return this.sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public boolean getUserApp() {
        return this.userApp;
    }

    public boolean getInRom() {
        return this.inRom;
    }

    public boolean getIsShowDesktop() {
        return this.isShowDesktop;
    }

    public void setIsShowDesktop(boolean isShowDesktop) {
        this.isShowDesktop = isShowDesktop;
    }

    @Override
    public boolean equals(Object obj) {
        if(packageName.equals(((AppInfoBean)obj).getPackageName()))
            return true;//这里以name为判定标准。
        else {
            return false;
        }
    }
}
