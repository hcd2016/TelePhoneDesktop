package com.desktop.telephone.telephonedesktop.bean;

public class EventBean {
    String event="";
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public EventBean(String event) {
        this.event = event;
    }

    public static String BLACK_LIST_ALL_NOTIFAL = "blacklist_notify";//通知双列表刷新
    public static String CONTACTS_ADD_SUCCESS = "contacts_add_sucess";//联系人添加/删除成功,通知刷新列表
    public static String CALLING_RECORD_NOTIFAL = "calling_record_notifal";//通知通话记录刷新
    public static String REFRESH_DESK = "refresh_desk";//刷新桌面,亲情号码使用
}
