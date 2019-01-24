package com.desktop.telephone.telephonedesktop.base;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.MainActivity;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CotrolActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.NewMainActivity;
import com.desktop.telephone.telephonedesktop.desktop.dialog.AlertFragmentDialog;
import com.desktop.telephone.telephonedesktop.http.HttpApi;
import com.desktop.telephone.telephonedesktop.http.RetrofitUtil;
import com.desktop.telephone.telephonedesktop.util.AppUpdateUtil;
import com.desktop.telephone.telephonedesktop.util.Constant;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.CountTimer;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    public CountTimer countTimerView;
    public boolean isShowBanner = true;//通话界面不进入banner

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        translucentStatus();
        super.onCreate(savedInstanceState);
//        //设置屏幕长亮
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定竖屏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            getWindow().setNavigationBarColor(Utils.getColor(R.color.colorPrimary));
//            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
//            getWindow().setNavigationBarColor(Color.WHITE);
//        }
//        if(this instanceof CallingActivity ){
//            isShowBanner = false;
//        }


    }

    //透明状态栏
    public void translucentStatus() {
        //透明状态栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Utils.getColor(R.color.text_666666));
            window.setNavigationBarColor(Utils.getColor(R.color.text_666666));
        }
    }
    //    /**
//     * 版本更新检查
//     */
//    private void checkVersionUpdate() {
//        Call<JsonObject> call = RetrofitUtil.create().checkUpdateVersion(Utils.getAppVersion(this), "android");
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                String json = response.body().toString();
//                try {
//                    JSONObject jsonObject = new JSONObject(json);
//                    JSONObject info = jsonObject.optJSONObject("info");
//
//                    if (info != null) {//有更新
//                        final int force = info.optInt("force");//1位强制更新,其他为普通更新
//                        final String url = HttpApi.baseUrl + info.optString("update_url");
//                        String description = info.optString("description");
////                        final String noticeTitle = App.getAPPName();
//                        final String authority = "com.credit.xiaowei.provider.fileprovider";
//                        if (force == 1) {//强制更新
//                            new AlertFragmentDialog.Builder(BaseActivity.this)
//                                    .setTitle("更新提示")
//                                    .setContent(description)
//                                    .setRightBtnText("立即更新")
//                                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
//                                        @Override
//                                        public void dialogRightBtnClick() {
////                                            AppUpdateUtil.getInstance().downLoadApk(BaseActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
//                                        }
//                                    })
//                                    .setCancel(false)
//                                    .build();
//
//                            new AlertFragmentDialog.Builder(MainActivity.this)
//                                    .setTitle("发现新版本")
//                                    .setContent(description)
//                                    .setLeftBtnText("立即更新")
//                                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
//                                        @Override
//                                        public void dialogLeftBtnClick() {
//                                            AppUpdateUtil.getInstance().downLoadApk(MainActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
//                                        }
//                                    })
//                                    .setCancel(false)
//                                    .build();
//                        } else {//非强制更新
//                            long lastTime = SpUtil.getLong(Constant.LAST_UPDATE_SHOW_TIME);
//                            long currentTimeMillis = System.currentTimeMillis();
//                            if (lastTime == 0 || currentTimeMillis - lastTime > 1000 * 60 * 60 * 24) {//未取消过 或 取消间隔大于1天
//                                new AlertFragmentDialog.Builder(MainActivity.this)
//                                        .setTitle("更新提示")
//                                        .setContent(description)
//                                        .setRightBtnText("立即更新")
//                                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
//                                            @Override
//                                            public void dialogRightBtnClick() {
//                                                AppUpdateUtil.getInstance().downLoadApk(MainActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
//                                            }
//                                        })
//                                        .setLeftBtnText("暂不更新")
//                                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
//                                            @Override
//                                            public void dialogLeftBtnClick() {
//                                                SpUtil.putLong(Constant.LAST_UPDATE_SHOW_TIME, System.currentTimeMillis());//记录上次取消时间
//                                            }
//                                        })
//                                        .setCancel(false)
//                                        .build();
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                t.toString();
//            }
//        });
//    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                boolean isOpenBanner = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_OPEN_BANNER, true);
                boolean isBannerRunning = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
                if (!isBannerRunning && isOpenBanner && isShowBanner) {
                    timeStart();
                }
                break;
            //否则其他动作计时取消
            default:
                if (countTimerView != null) {
                    countTimerView.cancel();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countTimerView != null) {
            countTimerView.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isOpenBanner = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_OPEN_BANNER, true);
        boolean isBannerRunning = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
        if (!isBannerRunning && isOpenBanner && isShowBanner) {
            timeStart();
        }

        if (!(this instanceof CotrolActivity)) {
            Call<JsonObject> call = RetrofitUtil.create().control(Utils.getPackageCode(this));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject object = new JSONObject(String.valueOf(response.body()));
                        int code = object.optInt("code");
                        if (code == 999) {
                            startActivity(CotrolActivity.class);
                        }

//                        else if (code == 0) {
//                            String versionUrl = object.optString("versionUrl");//下载地址
//                            int versionStatus = object.optInt("versionStatus");//0为强制升级,1为正常升级
//                            if (versionStatus == 0) {//强制升级
//
//                            } else if (versionStatus == 1) {//正常升级
//                                long last_millis = SPUtil.getInstance().getLong(SPUtil.KEY_LAST_NOTICE_TIME, 0);
//                                if (last_millis != 0 && (System.currentTimeMillis() - last_millis) < 24 * 60 * 60 * 1000) {//提示过且时间没到一天,不提示
//                                    return;
//                                }
//                                new AlertDialog.Builder(App.getContext(), R.style.Theme_AppCompat_Dialog)
//                                        .setTitle("系统升级")
//                                        .setMessage("有新版本系统需要升级")
//                                        .setPositiveButton("升级", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/app-release.apk";
//                                                installSilently(path);
//                                                Toast.makeText(App.getContext(), "升级中,请稍后...", Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                SPUtil.getInstance().saveLong(SPUtil.KEY_LAST_NOTICE_TIME, System.currentTimeMillis());//保存上次提示时间
//                                            }
//                                        })
//                                        .setCancelable(false)
//                                        .create().show();
//                            }
//                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.toString();
                }
            });
        }
    }

    /**
     * The install command installs a package to the system. Options:
     *
     * @command -l: install the package with FORWARD_LOCK.
     * @command -r: reinstall an existing app, keeping its data.
     * @command -t: allow test .apks to be installed.
     * @command -i: specify the installer package name.
     * @command -s: install package on sdcard.
     * @command -f: install package on internal flash.
     */
    /**
     * The uninstall command removes a package from the system. Options:
     * 静默安装
     *
     * @command -k: keep the data and cache directories around. after the
     * package removal.
     */
    private String installSilently(String path) {

        // 通过命令行来安装APK
        String[] args = {"pm", "install", "-r", path};
        String result = "";
        // 创建一个操作系统进程并执行命令行操作
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }
    /**
     * 在SD卡的指定目录上创建文件
     *
     * @param fileName
     */
    public File createFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void downLoadByNet(String url, final String dirName, final String filename) {
//        final String url = "https://www.vpfinance.cn/app/android/vpjr.apk";
//        final String dirname = Environment.getExternalStorageDirectory().getAbsolutePath();
//        final String filename = "vpjr.apk";
        Call<ResponseBody> call = RetrofitUtil.create().downLoadApk(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                File file = createFile(dirName + "/" + filename);
                try {
                    InputStream inputStream = response.body().byteStream();
                    OutputStream outputStream = new FileOutputStream(file);
                    //读取大文件
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int read = inputStream.read(buffer);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(buffer,0,read);
                    }
                    Toast.makeText(App.getContext(),"下载完成了",Toast.LENGTH_SHORT).show();
                    outputStream.flush();
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void downLoadByNotice(String url, final String dirName, final String filename) {
//        final String url = "https://www.vpfinance.cn/app/android/vpjr.apk";
//        final String dirname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
//        final String filename = "vpjr.apk";
        //通知栏下载
        Uri uri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置WIFI下和流量都可以进行更新
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        req.setDestinationInExternalFilesDir(this, dirName, filename);
        //通知栏标题
        req.setTitle("系统更新下载");
        //通知栏描述信息
        req.setDescription("正在下载,请稍后...");
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");

        //获取下载任务ID
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(req);
        registerDownLoadReceiver(this, id);//注册下载监听广播
    }

    //注册广播监听下载进度
    private void registerDownLoadReceiver(Context context, final long id) {
        final IntentFilter inflater = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver apkInstallReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == id) {//下载id一致
                    if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {//下载完成
//                        installApk(context, id, authority);
                        Toast.makeText(App.getContext(), "下载完成!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        context.registerReceiver(apkInstallReceiver, inflater);
    }

    /**
     * 跳轉廣告
     */
    public void timeStart() {

        if (countTimerView != null) {
            countTimerView.cancel();
        }
        long startBeginTime = SPUtil.getInstance().getLong(SPUtil.KEY_BANNER_START_TIME, 1000 * 60 * 5);
        countTimerView = new CountTimer(startBeginTime, 1000, this);
        countTimerView.start();
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
