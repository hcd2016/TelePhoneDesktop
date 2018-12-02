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
    public long id;
    public int blackListModeType = 0; //黑白名单模式,0为普通模式,1为

    @Generated(hash = 591960185)
    public SystemStatusBean(long id, int blackListModeType) {
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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
