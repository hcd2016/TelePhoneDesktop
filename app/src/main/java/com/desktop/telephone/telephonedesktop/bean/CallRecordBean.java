package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CallRecordBean {
    @Id
    public Long Id;
    public int callStatus = 0;//拨号状态,0为已拨,1为已接,2为未接,3为主动挂断
    public String phoneNum;//如联系人有该号码,则显示为联系人名字
    public String date;//发生时间
    public String statusDesc;//状态描述
    public String name = "";//若该号码联系人中存在,设置name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Generated(hash = 1034955805)
    public CallRecordBean(Long Id, int callStatus, String phoneNum, String date,
            String statusDesc, String name) {
        this.Id = Id;
        this.callStatus = callStatus;
        this.phoneNum = phoneNum;
        this.date = date;
        this.statusDesc = statusDesc;
        this.name = name;
    }

    @Generated(hash = 727764067)
    public CallRecordBean() {
    }

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

}
