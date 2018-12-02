package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BlackListInfoBean {
    @Id(autoincrement = true)
    public Long id;
    public String phone;
    public int type = 1;//1为黑名单,2为白名单
    public String date;

    @Generated(hash = 867824689)
    public BlackListInfoBean(Long id, String phone, int type, String date) {
        this.id = id;
        this.phone = phone;
        this.type = type;
        this.date = date;
    }

    @Generated(hash = 1501404343)
    public BlackListInfoBean() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
