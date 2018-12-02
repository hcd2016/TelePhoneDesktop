package com.desktop.telephone.telephonedesktop.bean;

/**
 * 用于传递BlacklistInfoBean的多种情况
 */
public class EventBlacklistInfoBean {
    public boolean needDelete;//是否需要删除
    public BlackListInfoBean deletebean;//需要删除的bean
    public BlackListInfoBean addbean;//需要添加的bean
    public boolean isNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        this.needDelete = needDelete;
    }

    public BlackListInfoBean getDeletebean() {
        return deletebean;
    }

    public void setDeletebean(BlackListInfoBean deletebean) {
        this.deletebean = deletebean;
    }

    public BlackListInfoBean getAddbean() {
        return addbean;
    }

    public void setAddbean(BlackListInfoBean addbean) {
        this.addbean = addbean;
    }

}
