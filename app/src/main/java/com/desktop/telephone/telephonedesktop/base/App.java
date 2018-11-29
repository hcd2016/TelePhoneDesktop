package com.desktop.telephone.telephonedesktop.base;

import android.app.Application;
import android.content.Context;

import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
//import com.lidroid.xutils.DbUtils;
import com.desktop.telephone.telephonedesktop.db.DBManager;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;

import java.util.List;

public class App extends Application {
    private static Context context;
    private List<DesktopIconBean> myList;//界面初始化

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DBManager.getInstance();
    }

    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }
    private static DesktopIconBeanDao getDesktopIconBeanDao() {
        return DBManager.getInstance().getNewSession().getDesktopIconBeanDao();
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
