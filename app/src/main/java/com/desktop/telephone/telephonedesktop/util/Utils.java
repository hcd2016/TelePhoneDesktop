package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.App;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    /**
     * 包名直接跳转
     **/
    public static void startApp(Context context, String pkgName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public static final int getColor(int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(App.getContext(), id);
        } else {
            return App.getContext().getResources().getColor(id);
        }
    }

    private static Toast toast = null;

    public static void Toast(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }


    private static Map<String, Integer> appIconIdMap = new HashMap<>();
    static {
        appIconIdMap.put("phone_icon", R.drawable.phone_icon);//电话
        appIconIdMap.put("call_records_icon", R.drawable.call_records_icon);//智能通讯录
        appIconIdMap.put("photo_icon", R.drawable.photo_icon);//电子相册
        appIconIdMap.put("blacklist_icon", R.drawable.blacklist_icon);//黑白名单
        appIconIdMap.put("one_key", R.drawable.one_key);//一键拨号
        appIconIdMap.put("record_icon", R.drawable.record_icon);//录音
        appIconIdMap.put("address_list_icon", R.drawable.address_list_icon);//通话记录
        appIconIdMap.put("sos_icon", R.drawable.sos_icon);//sos
        appIconIdMap.put("all_apps_icon", R.drawable.all_apps_icon);//所有应用
    }

    /**
     * 我的应用
     * 根据appName动态获取app的iconId
     *
     * @param appName
     * @return
     */
    public static Integer getAppIconId(String appName) {
        if (appIconIdMap.containsKey(appName)) {
            return appIconIdMap.get(appName);
        }
        return 0;
    }

    /**
     * 获得屏幕的宽
     */
    public static int getScreenWidth(Context context) {
        WindowManager window = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕的高
     */
    public static int getScreenHeight(Context context) {
        WindowManager window = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static String getFormatDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }
}
