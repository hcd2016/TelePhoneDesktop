package com.desktop.telephone.telephonedesktop.base;

import android.app.Application;
import android.content.Context;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
//import com.lidroid.xutils.DbUtils;
import com.desktop.telephone.telephonedesktop.db.GreenDaoManager;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;

import java.util.ArrayList;
import java.util.List;

public class APP extends Application {
    private static Context context;
    private List<DesktopIconBean> myList;//界面初始化

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        GreenDaoManager.getInstance();
        initIconData();
    }

    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }
    private static DesktopIconBeanDao getDesktopIconBeanDao() {
        return GreenDaoManager.getInstance().getSession().getDesktopIconBeanDao();
    }

    /**
     * 桌面数据添加
     */
    private void initIconData() {
        myList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            DesktopIconBean moveItem = new DesktopIconBean();
            moveItem.setMid(i);
            switch (i) {
                case 0:
                    //电话
                    moveItem.setIconType(1);
                    moveItem.setTitle("电话");
                    moveItem.setImg_normal(R.mipmap.phone_icon);
                    moveItem.setImg_pressed(R.mipmap.phone_icon);
                    break;
                case 1:
                    //智能通讯录
                    moveItem.setIconType(1);
                    moveItem.setTitle("智能通讯录");
                    moveItem.setImg_normal(R.mipmap.call_records_icon);
                    moveItem.setImg_pressed(R.mipmap.call_records_icon);
                    break;
                case 2:
                    //电子相册
                    moveItem.setIconType(1);
                    moveItem.setTitle("电子相册");
                    moveItem.setImg_normal(R.mipmap.photo_icon);
                    moveItem.setImg_pressed(R.mipmap.photo_icon);
                    break;
                case 3:
                    //黑白名单
                    moveItem.setIconType(1);
                    moveItem.setTitle("黑白名单");
                    moveItem.setImg_normal(R.mipmap.blacklist_icon);
                    moveItem.setImg_pressed(R.mipmap.blacklist_icon);
                    break;
                case 4:
                    //一键拨号
                    moveItem.setIconType(1);
                    moveItem.setTitle("一键拨号");
                    moveItem.setImg_normal(R.mipmap.one_key);
                    moveItem.setImg_pressed(R.mipmap.one_key);
                    break;
                case 5:
                    //录音
                    moveItem.setIconType(1);
                    moveItem.setTitle("录音");
                    moveItem.setImg_normal(R.mipmap.record_icon);
                    moveItem.setImg_pressed(R.mipmap.record_icon);
                    break;
                case 6:
                    //通话记录
                    moveItem.setIconType(1);
                    moveItem.setTitle("通话记录");
                    moveItem.setImg_normal(R.mipmap.address_list_icon);
                    moveItem.setImg_pressed(R.mipmap.address_list_icon);
                    break;
                case 7:
                    //sos
                    moveItem.setIconType(1);
                    moveItem.setTitle("SOS");
                    moveItem.setImg_normal(R.mipmap.sos_icon);
                    moveItem.setImg_pressed(R.mipmap.sos_icon);
                    break;
                case 8:
                    //所有应用
                    moveItem.setIconType(1);
                    moveItem.setTitle("所有应用");
                    moveItem.setImg_normal(R.mipmap.all_apps_icon);
                    moveItem.setImg_pressed(R.mipmap.all_apps_icon);
                    break;
            }
            myList.add(moveItem);
            myList.add(moveItem);
        }

        List<DesktopIconBean> list = getDesktopIconBeanDao().loadAll();
        if(list == null || list.size() == 0) {
            saveNLists(myList);
        }
    }

    /**
     * 批量插入或修改用户信息
     * @param list      用户信息列表
     */
    public void saveNLists(final List<DesktopIconBean> list){
        if(list == null || list.isEmpty()){
            return;
        }
        getDesktopIconBeanDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    DesktopIconBean desktopIconBean = list.get(i);
                    getDesktopIconBeanDao().insertOrReplace(desktopIconBean);
                }
            }
        });

    }
}
