package com.desktop.telephone.telephonedesktop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PhotoInfoBean {
    @Id(autoincrement = true)
    public long id;
    public String name;
    public String desc;
    public String fileName;
    @Generated(hash = 123658927)
    public PhotoInfoBean(long id, String name, String desc, String fileName) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.fileName = fileName;
    }

    @Generated(hash = 1175413973)
    public PhotoInfoBean() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
