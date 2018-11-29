package com.desktop.telephone.telephonedesktop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.desktop.telephone.telephonedesktop.base.App;
import com.desktop.telephone.telephonedesktop.gen.DaoMaster;
import com.desktop.telephone.telephonedesktop.gen.DaoSession;

public class DBManager {
    private final static String dbName = "appinfo.db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private DaoSession sDaoSession;
    private static DaoMaster sDaoMaster;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     */
    public static DBManager getInstance(){
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(App.getContext());
                }
            }
        }
        return mInstance;
    }

    public DaoSession getNewSession() {
        DaoMaster mDaoSession = new DaoMaster(getWritableDatabase());
        return mDaoSession.newSession();
    }


    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     * @return
     */
    public DaoMaster getDaoMaster(String DBName){
        if(sDaoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DBName, null);
            sDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection(){
        closeHelper();
        closeDaoSession();
    }

    private void closeHelper(){
        if(openHelper != null){
            openHelper.close();
            openHelper = null;
        }
    }
    private void closeDaoSession() {
        if (sDaoSession != null) {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }
}
