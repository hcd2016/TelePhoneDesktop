package com.desktop.telephone.telephonedesktop.bean;

import java.io.Serializable;

public class FamilyIDBean  implements Serializable{
    public Long showId;//是否显示id记录
    public Long deskId;//桌面id
    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

}
