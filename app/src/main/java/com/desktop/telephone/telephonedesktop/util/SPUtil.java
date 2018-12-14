package com.desktop.telephone.telephonedesktop.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.desktop.telephone.telephonedesktop.base.App;

/**
 * @ClassName: SPUtil
 * @Description: SharedPreference工具类
 */
public class SPUtil {
    public static final String KEY_BANNER_START_TIME = "banner_start_time";//轮播开始时间
    public static final String KEY_BANNER_SPEED = "banner_speed";//轮播速度
    public static final String KEY_IS_BANNER_RUNING = "is_banner_running";//是否正在轮播
    public static final String KEY_IS_OPEN_BANNER = "is_open_banner";//是否开启轮播

    public static final String KEY_HAND_STATUS = "key_hand_status";//记录手柄状态,0为放下,1为抬起.
    public static final String KEY_CALLING_WITH_TALKING = "key_calling_with_talking";//通话过程中拨打电话


    public static final String SHARED_PREFERENCE_NAME = "sp_file";
    private static SPUtil uniqueInstance = null;


    private SPUtil() {
        // 单例存在.
    }

    public static SPUtil getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SPUtil();
        }
        return uniqueInstance;
    }

    public void saveBoolean(String key, boolean value) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    public boolean getBoolean(String key,boolean b) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, b);
    }

    public void saveLong(String key, long value) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).commit();
    }

    public long getLong(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public void saveString(String key, String value) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public void deleteSpByKey(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public void saveInteger(String key, int value) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public int getInteger(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }
}
