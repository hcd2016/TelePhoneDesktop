package com.desktop.telephone.telephonedesktop.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.desktop.telephone.telephonedesktop.R;

import java.io.File;
import java.util.List;

/**
 * APP版本更新工具类
 */
public class AppUpdateUtil {
    public long id;
    public static AppUpdateUtil appUpdateUtil = null;
    private DownloadManager downloadManager;
    private TextView tv_percent;
    private ProgressBar pb;
    private AlertDialog.Builder processDialog;
    private AlertDialog show;
    private TextView btn_instanll;
    private TextView tv_title;
    private BroadcastReceiver apkInstallReceiver;

    public static AppUpdateUtil getInstance() {
        if (null == appUpdateUtil) {
            appUpdateUtil = new AppUpdateUtil();
        }
        return appUpdateUtil;
    }

    /**
     * apk下载
     *
     * @param downLoadUrl 下载路径
     * @param apkName     保存的apk名称,随意
     * @param noticeTitle 通知栏下载器显示的标题
     * @param authority   配置在清单文件的 provider 所设置的主机名
     * @param isForce     是否是强制更新
     */
    public void downLoadApk(Activity activity, final String downLoadUrl, final String apkName, String noticeTitle, String authority, int isForce) {
        checkDownLoadService(activity); //检测是否开启下载服务
        startDownload(activity, downLoadUrl, apkName, noticeTitle, authority, isForce);//开启下载流程
    }

    //注册广播监听下载进度
    private void registerDownLoadReceiver(Context context, final long id, final String authority) {
        final IntentFilter inflater = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        apkInstallReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == id) {//下载id一致
                    if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {//下载完成
                        installApk(context, id, authority);
                    }
                }
            }
        };
        context.registerReceiver(apkInstallReceiver, inflater);
    }

    /**
     * 安装apk
     *
     * @param context
     * @param id
     * @param authority fileprovider配置的主机地址,解决7.0以上版本兼容问题
     */
    private void installApk(Context context, long id, String authority) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String path = findPathById(id);//通过id获取下载时设置的路径(非必要,该路径也可直接获取设置通知栏下载路径时的路径req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, apkName); )
        File file = new File(path);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkUri = FileProvider.getUriForFile(context, authority, file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        if (apkInstallReceiver != null) {//如是广播监听安装,下载完取消注册
            context.unregisterReceiver(apkInstallReceiver);
        }
        context.startActivity(intent);
    }

    //通过id获取下载时设置的路径
    private String findPathById(long downId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String fileUri = c.getString(fileUriIdx);
                    String fileName = null;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (fileUri != null) {
                            fileName = Uri.parse(fileUri).getPath();
                        }
                    } else {
                        //Android 7.0以上的方式：请求获取写入权限，这一步报错
                        //过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
                        int fileNameIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                        fileName = c.getString(fileNameIdx);
                    }
                    return fileName;
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    /**
     * 开启下载流程
     *
     * @param activity
     * @param downLoadUrl 下载路径
     * @param apkName     保存的apk名称,随意
     * @param noticeTitle 通知栏下载器显示的标题
     * @param authority   配置在清单文件的 provider 所设置的主机名
     * @param isForce     是否是强制更新
     */
    private void startDownload(final Activity activity, final String downLoadUrl, final String apkName, final String noticeTitle, final String authority, final int isForce) {
        if (isOpenWifi(activity)) {//开启WIFI状态
            startNotifacationDownLoad(activity, downLoadUrl, apkName, noticeTitle, authority, isForce); //开启通知栏下载
        } else {//未开启WIFI,
            new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog)
                    .setTitle("温馨提示")
                    .setMessage("您当前WIFI未开启,将耗费较大流量,是否继续下载?")
                    .setPositiveButton("朕不差这点流量", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startNotifacationDownLoad(activity, downLoadUrl, apkName, noticeTitle, authority, isForce); //开启通知栏下载
                        }
                    })
                    .setNegativeButton("去开启WIFI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                            activity.startActivityForResult(intent, Constant.OPEN_WIFI_FINISH);
                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }
    }

    /**
     * 检测当前网络状态,判断是否开启WIFI
     */
    private boolean isOpenWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    /**
     * 开启通知栏下载
     *
     * @param context
     * @param downLoadUrl 下载路径
     * @param apkName     保存的apk名称,随意
     * @param noticeTitle 通知栏下载器显示的标题
     * @param authority   配置在清单文件的 provider 所设置的主机名
     * @param isForce     是否是强制更新
     */
    private void startNotifacationDownLoad(final Context context, String downLoadUrl, String apkName, String noticeTitle, final String authority, final int isForce) {
        //通知栏下载
        Uri uri = Uri.parse(downLoadUrl);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置WIFI下和流量都可以进行更新
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, apkName);
        //通知栏标题
        req.setTitle(noticeTitle);
        //通知栏描述信息
        req.setDescription("正在下载,请稍后...");
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");

        //内容观察者监听下载进度
        MyContentObserver myContentObserver = new MyContentObserver(null);
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, myContentObserver);

        //获取下载任务ID
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        id = downloadManager.enqueue(req);

        if (isForce != 1) {//非强制更新,后台下载不显示进度弹窗,下载完成才安装
            registerDownLoadReceiver(context, id, authority);//注册下载监听广播
        } else {//强制更新,进度弹框不允许关闭.
            //显示进度的对话框
            View view = View.inflate(context, R.layout.progress_dialog, null);
            tv_percent = (TextView) view.findViewById(R.id.tv_percent);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            pb = (ProgressBar) view.findViewById(R.id.pb);
            btn_instanll = (TextView) view.findViewById(R.id.btn_instanll);
            btn_instanll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//注意.这里点击后不关闭弹窗
                    installApk(context, id, authority);
                }
            });
            show = new AlertDialog.Builder(context).setView(view).setCancelable(false).show();
        }
    }

    //检测是否开启下载服务
    private void checkDownLoadService(Context context) {
        if (!canDownloadState(context)) {//未开启过下载服务
            Toast.makeText(context, "下载服务未启用,请您启用", Toast.LENGTH_SHORT).show();
            String packageName = "com.android.providers.downloads";
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            PackageManager packageManager = context.getPackageManager();
            List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                context.startActivity(intent);
            }
            return;
        }
    }

    //检测是否开启过下载服务
    public boolean canDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //查询数据库监听下载进度.
    class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            final Cursor cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                float progress = (float) Math.floor(percent * 100);
                if (progress > 0) {
                    tv_percent.setText(progress + "%");
                    pb.setProgress((int) progress, true);
                }
                if (progress == 100) {
                    btn_instanll.setEnabled(true);
                    tv_title.setText("下载完成");
                }
            }
            cursor.close();
        }
    }
}
