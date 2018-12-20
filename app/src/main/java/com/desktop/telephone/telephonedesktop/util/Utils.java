package com.desktop.telephone.telephonedesktop.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
        appIconIdMap.put("call_records_icon", R.drawable.call_records_icon);//智能通讯录
        appIconIdMap.put("photo_icon", R.drawable.photo_icon);//电子相册
        appIconIdMap.put("blacklist_icon", R.drawable.blacklist_icon);//黑红名单
//        appIconIdMap.put("one_key", R.drawable.one_key);//一键拨号
        appIconIdMap.put("record_icon", R.drawable.record_icon);//录音
        appIconIdMap.put("address_list_icon", R.drawable.address_list_icon);//通话记录
        appIconIdMap.put("sos_icon", R.drawable.sos_icon);//sos
        appIconIdMap.put("all_apps_icon", R.drawable.all_apps_icon);//所有应用
    }


    public static int getColorBgFromPosition(int position) {
        switch (position % 10) {
            case 0:
                return Utils.getColor(R.color.limegreen);
            case 1:
                return Utils.getColor(R.color.steelblue);
            case 2:
                return Utils.getColor(R.color.slateblue);
            case 3:
                return Utils.getColor(R.color.dimgray);
            case 4:
                return Utils.getColor(R.color.coral);
            case 5:
                return Utils.getColor(R.color.orchid);
            case 6:
                return Utils.getColor(R.color.darkkhaki);
            case 8:
                return Utils.getColor(R.color.steelblue);
            case 7:
                return Utils.getColor(R.color.crimson);
            case 9:
                return Utils.getColor(R.color.darkgreen);
            default:
                return Utils.getColor(R.color.darkorange);
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
}
