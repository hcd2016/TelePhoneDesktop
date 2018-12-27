package com.desktop.telephone.telephonedesktop.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
        appIconIdMap.put("call_records_icon", R.drawable.call_records_icon);//联系人
        appIconIdMap.put("photo_icon", R.drawable.photo_icon);//相册
        appIconIdMap.put("blacklist_icon", R.drawable.blacklist_icon);//黑红名单
//        appIconIdMap.put("one_key", R.drawable.one_key);//一键拨号
        appIconIdMap.put("record_icon", R.drawable.record_icon);//录音
        appIconIdMap.put("address_list_icon", R.drawable.address_list_icon);//通话记录
        appIconIdMap.put("sos_icon", R.drawable.sos_icon);//sos
        appIconIdMap.put("all_apps_icon", R.drawable.all_apps_icon);//所有应用
        appIconIdMap.put("phone_setting", R.drawable.phone_setting);//所有应用
        appIconIdMap.put("photos_icon", R.drawable.photos_desk_icon);//相机
        appIconIdMap.put("settings_icon", R.drawable.settings_desk_icon);//设置
        appIconIdMap.put("add_contact_icon", R.drawable.add_contact_icon);//亲情添加
        appIconIdMap.put("family_header", R.drawable.family_header_icon);//亲情头像
    }


    public static int getColorBgFromPosition(int position) {
        switch (position % 11) {
            case 0:
                return Utils.getColor(R.color.color_1);
            case 1:
                return Utils.getColor(R.color.color_2);
            case 2:
                return Utils.getColor(R.color.color_3);
            case 3:
                return Utils.getColor(R.color.dimgray);
            case 4:
                return Utils.getColor(R.color.coral);
            case 5:
                return Utils.getColor(R.color.color_6);
            case 6:
                return Utils.getColor(R.color.color_7);
            case 7:
                return Utils.getColor(R.color.color_8);
            case 8:
                return Utils.getColor(R.color.color_9);
            case 9:
                return Utils.getColor(R.color.color_10);
            case 10:
                return Utils.getColor(R.color.color_11);
            default:
                return Utils.getColor(R.color.colorPrimary);
        }
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

    /**
     * 读取文件创建时间
     */
    public static String getCreateTime(String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(f.lastModified()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static final String DATA_TYPE_AUDIO = "audio/*";

    /**
     * 打开文件
     *
     * @param filePath 文件的全路径，包括到文件名
     */
    public static void openFile(String filePath, Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            //如果文件不存在
            Toast.makeText(context, "打开失败，原因：文件已经被移动或者删除", Toast.LENGTH_SHORT).show();
            return;
        }
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            intent = generateVideoAudioIntent(filePath, DATA_TYPE_AUDIO);
        }
//        } else if (end.equals("3gp") || end.equals("mp4")) {
//            intent = generateVideoAudioIntent(filePath,DATA_TYPE_VIDEO);
//        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_IMAGE);
//        } else if (end.equals("apk")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_APK);
//        }else if (end.equals("html") || end.equals("htm")){
//            intent = getHtmlFileIntent(filePath);
//        } else if (end.equals("ppt")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_PPT);
//        } else if (end.equals("xls")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_EXCEL);
//        } else if (end.equals("doc")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_WORD);
//        } else if (end.equals("pdf")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_PDF);
//        } else if (end.equals("chm")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_CHM);
//        } else if (end.equals("txt")) {
//            intent = generateCommonIntent(filePath, DATA_TYPE_TXT);
//        } else {
//            intent = generateCommonIntent(filePath,DATA_TYPE_ALL);
//        }
        context.startActivity(intent);
    }

    /**
     * 产生打开视频或音频的Intent
     *
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private static Intent generateVideoAudioIntent(String filePath, String dataType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(filePath);
        intent.setDataAndType(getUri(intent, file), dataType);
        return intent;
    }

    /**
     * 获取对应文件的Uri
     *
     * @param intent 相应的Intent
     * @param file   文件对象
     * @return
     */
    private static Uri getUri(Intent intent, File file) {
        Uri uri = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //判断版本是否在7.0以上
//            uri =
//                    FileProvider.getUriForFile(App.getContext(),
//                            App.getContext().getPackageName() + ".fileprovider",
//                            file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
        uri = Uri.fromFile(file);
//        }
        return uri;
    }

    public static void removeFile(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
//                removeFile(filePath);
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断字符是否为中文
     *
     * @param a
     * @return
     */
    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    public static boolean isChineseA(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        }
        return false;
    }

    //获取当前app的版本号
    public static String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour 开始小时，例如22
     * @param beginMin  开始小时的分钟数，例如30
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
        // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
        // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    public static void setScreenOn(Context context, Activity activity) {
        //设置屏幕长亮
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
