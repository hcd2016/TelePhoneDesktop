package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SosBean {
    @Id
    public Long id;
    public String name;
    public String phoneNum;
    public String smsContent;//短信内容

    @Generated(hash = 1436458145)
    public SosBean(Long id, String name, String phoneNum, String smsContent) {
        this.id = id;
        this.name = name;
        this.phoneNum = phoneNum;
        this.smsContent = smsContent;
    }

    @Generated(hash = 1646755404)
    public SosBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
