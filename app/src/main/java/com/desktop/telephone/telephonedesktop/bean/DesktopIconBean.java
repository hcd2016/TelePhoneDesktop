package com.desktop.telephone.telephonedesktop.bean;

import android.graphics.drawable.Drawable;

import com.desktop.telephone.telephonedesktop.base.APP;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.sql.Blob;

@Entity
public class DesktopIconBean {

    @Id(autoincrement = true)
    private Long id;
    private int mid;
    //正常模式下的item的Drawable Id
    private int img_normal;
    //按下模式下的item的Drawable Id
    private int img_pressed;
    //item的排序字段
    private int orderId;
    private String title;
    private int iconType = 0;//图标类型，0为系统应用，1为自定义添加,2为用户添加应用
    private byte[] app_icon;
    private String packageName = APP.getContext().getPackageName();//应用包名,默认为本应用包名

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public byte[] getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(byte[] app_icon) {
        this.app_icon = app_icon;
    }

    public DesktopIconBean(Long id, int mid, int img_normal, int img_pressed,
            int orderId, String title, int iconType) {
        this.id = id;
        this.mid = mid;
        this.orderId = orderId;
        this.title = title;
        this.iconType = iconType;
    }
    public DesktopIconBean() {
    }

    public DesktopIconBean(Long id, int mid, int img_normal, int img_pressed,
            int orderId, String title, int iconType, byte[] app_icon) {
        this.id = id;
        this.mid = mid;
        this.img_normal = img_normal;
        this.img_pressed = img_pressed;
        this.orderId = orderId;
        this.title = title;
        this.iconType = iconType;
        this.app_icon = app_icon;
    }

    @Generated(hash = 1777396061)
    public DesktopIconBean(Long id, int mid, int img_normal, int img_pressed,
            int orderId, String title, int iconType, byte[] app_icon,
            String packageName) {
        this.id = id;
        this.mid = mid;
        this.img_normal = img_normal;
        this.img_pressed = img_pressed;
        this.orderId = orderId;
        this.title = title;
        this.iconType = iconType;
        this.app_icon = app_icon;
        this.packageName = packageName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getMid() {
        return this.mid;
    }
    public void setMid(int mid) {
        this.mid = mid;
    }
    public int getImg_normal() {
        return this.img_normal;
    }
    public void setImg_normal(int img_normal) {
        this.img_normal = img_normal;
    }
    public int getImg_pressed() {
        return this.img_pressed;
    }
    public void setImg_pressed(int img_pressed) {
        this.img_pressed = img_pressed;
    }
    public int getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getIconType() {
        return this.iconType;
    }
    public void setIconType(int iconType) {
        this.iconType = iconType;
    }
}