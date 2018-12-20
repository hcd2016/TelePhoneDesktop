package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用来存储系统状态的bean
 */
@Entity
public class SystemStatusBean {
    @Id
    public Long id;
    public int blackListModeType = 0; //黑红名单模式,0为普通模式,1为黑名单,2为白名单

    @Generated(hash = 1277101765)
    public SystemStatusBean(Long id, int blackListModeType) {
        this.id = id;
        this.blackListModeType = blackListModeType;
    }

    @Generated(hash = 955554540)
    public SystemStatusBean() {
    }

    public int getBlackListModeType() {
        return blackListModeType;
    }

    public void setBlackListModeType(int blackListModeType) {
        this.blackListModeType = blackListModeType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
