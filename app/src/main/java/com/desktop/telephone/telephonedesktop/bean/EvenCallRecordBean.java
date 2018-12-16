package com.desktop.telephone.telephonedesktop.bean;

public class EvenCallRecordBean {
    public CallRecordBean callRecordBean;
    public boolean isAdd;

    public EvenCallRecordBean(CallRecordBean callRecordBean, boolean isAdd) {
        this.callRecordBean = callRecordBean;
        this.isAdd = isAdd;
    }

    public CallRecordBean getCallRecordBean() {
        return callRecordBean;
    }

    public void setCallRecordBean(CallRecordBean callRecordBean) {
        this.callRecordBean = callRecordBean;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
