package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 联系人bean类
 */
@Entity
public class ContactsBean implements Serializable {
    private static final long serialVersionUID = 6474821504743440284L;
    @Id
    public Long id;
    public String name = "";//姓名
    public String email = "";//邮箱
    public String desc = "";//备注
    public String phone = "";//手机号
    public boolean isShowFamily = false;

    public boolean isShowFamily() {
        return isShowFamily;
    }

    public void setShowFamily(boolean showFamily) {
        isShowFamily = showFamily;
    }

    @Generated(hash = 231460074)
    public ContactsBean(Long id, String name, String email, String desc,
            String phone, boolean isShowFamily) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.desc = desc;
        this.phone = phone;
        this.isShowFamily = isShowFamily;
    }

    @Generated(hash = 747317112)
    public ContactsBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsShowFamily() {
        return this.isShowFamily;
    }

    public void setIsShowFamily(boolean isShowFamily) {
        this.isShowFamily = isShowFamily;
    }
}
