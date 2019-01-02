package com.desktop.telephone.telephonedesktop.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.desktop.telephone.telephonedesktop.base.App;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public static final String KEY_IS_SHOW_COMMING_CALL_NUM = "key_is_show_comming_call_num";//是否显示来电
    public static final String KEY_IS_SEND_COMMING_CALL = "key_is_send_comming_call";//第一次是否发送过来电
    public static final String KEY_INTERCHANGER_SETTING = "key_interchanger_setting";//第一次是否发送过来电
    public static final String KEY_IS_HAVE_WEIXIN = "key_is_have_weixin";//第一次是否发送过来电

    public static final String KEY_SHOW_FAMILY_LIST = "key_show_family_list";//显示亲情list


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

    public boolean getBoolean(String key, boolean b) {
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

    public static void saveString(String key, String value) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public void deleteSpByKey(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void saveInteger(String key, int value) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInteger(String key) {
        SharedPreferences sp = App.getContext().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }


    /**
     * 存储List集合
     *
     * @param context 上下文
     * @param key     存储的键
     * @param list    存储的集合
     */
    public static void putList(Context context, String key, List<? extends Serializable> list) {
        try {
            put(context, key, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取List集合
     *
     * @param context 上下文
     * @param key     键
     * @param <E>     指定泛型
     * @return List集合
     */
    public static <E extends Serializable> List<E> getList(Context context, String key) {
        try {
            return (List<E>) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 存储对象
     */
    private static void put(Context context, String key, Object obj)
            throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        saveString(key, objectStr);
    }

    /**
     * 获取对象
     */
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException {
        String wordBase64 = getString(key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }

}
