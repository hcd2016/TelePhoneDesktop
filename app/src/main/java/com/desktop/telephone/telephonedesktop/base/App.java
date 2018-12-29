package com.desktop.telephone.telephonedesktop.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
//import com.lidroid.xutils.DbUtils;
import com.desktop.telephone.telephonedesktop.db.DBManager;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.gen.DaoMaster;
import com.desktop.telephone.telephonedesktop.gen.DaoSession;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;
import com.desktop.telephone.telephonedesktop.http.RetrofitUtil;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.weather.ParaseJsonUtils;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Intent intent = new Intent();
        intent.setAction("com.tongen.startDial");
        context.sendBroadcast(intent);

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
