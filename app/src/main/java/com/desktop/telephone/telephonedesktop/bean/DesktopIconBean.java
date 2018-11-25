package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
    private int iconType = 0;//图标类型，0为系统应用，1为自定义添加
    @Generated(hash = 192794647)
    public DesktopIconBean(Long id, int mid, int img_normal, int img_pressed,
            int orderId, String title, int iconType) {
        this.id = id;
        this.mid = mid;
        this.img_normal = img_normal;
        this.img_pressed = img_pressed;
        this.orderId = orderId;
        this.title = title;
        this.iconType = iconType;
    }
    @Generated(hash = 1700589037)
    public DesktopIconBean() {
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
