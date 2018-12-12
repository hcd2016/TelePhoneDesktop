package com.desktop.telephone.telephonedesktop.bean;

import com.desktop.telephone.telephonedesktop.base.App;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DesktopIconBean {

    @Id(autoincrement = true)
    private Long id;
    private int mid;
    private String img_id_name;//获取本地资源文件对应的名称

    public String getImg_id_name() {
        return img_id_name;
    }

    public void setImg_id_name(String img_id_name) {
        this.img_id_name = img_id_name;
    }
    public int iconBgColor;//桌面背景
    public int getIconBgColor() {
        return iconBgColor;
    }

    public void setIconBgColor(int iconBgColor) {
        this.iconBgColor = iconBgColor;
    }

    //    //正常模式下的item的Drawable Id
//    private int img_normal;
//    //按下模式下的item的Drawable Id
//    private int img_pressed;
    //item的排序字段
    private int orderId;
    private String title;
    private int iconType = 0;//图标类型，0为系统应用，1为自定义添加,2为用户添加应用,3为一键拨号
    private byte[] app_icon;
    private String packageName = App.getContext().getPackageName();//应用包名,默认为本应用包名
    private String phoneNum;//一键拨号时的号码,一键拨号时姓名存为,title存联系人名称

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


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

    public DesktopIconBean() {
    }

    @Generated(hash = 495355149)
    public DesktopIconBean(Long id, int mid, String img_id_name, int iconBgColor, int orderId,
            String title, int iconType, byte[] app_icon, String packageName,
            String phoneNum) {
        this.id = id;
        this.mid = mid;
        this.img_id_name = img_id_name;
        this.iconBgColor = iconBgColor;
        this.orderId = orderId;
        this.title = title;
        this.iconType = iconType;
        this.app_icon = app_icon;
        this.packageName = packageName;
        this.phoneNum = phoneNum;
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
