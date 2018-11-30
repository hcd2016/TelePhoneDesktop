package com.desktop.telephone.telephonedesktop.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
//import com.lidroid.xutils.DbUtils;
import com.desktop.telephone.telephonedesktop.db.DBManager;
import com.desktop.telephone.telephonedesktop.gen.DaoMaster;
import com.desktop.telephone.telephonedesktop.gen.DaoSession;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;

import java.util.List;

public class App extends Application {
    private static Context context;
    private List<DesktopIconBean> myList;//界面初始化
    private static DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        DBManager.getInstance();
        setupDatabase();
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


    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库app.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "appinfo.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }
}
