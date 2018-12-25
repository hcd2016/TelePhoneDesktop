package com.desktop.telephone.telephonedesktop.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.desktop.telephone.telephonedesktop.MainActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;
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

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends FragmentActivity {
    public CountTimer countTimerView;
    public boolean isShowBanner = true;//通话界面不进入banner

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置屏幕长亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定竖屏
        if(this instanceof CallingActivity ){
            isShowBanner = false;
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
////                            new AlertFragmentDialog.Builder(MainActivity.this)
////                                    .setTitle("发现新版本")
////                                    .setContent(description)
////                                    .setLeftBtnText("立即更新")
////                                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
////                                        @Override
////                                        public void dialogLeftBtnClick() {
////                                            AppUpdateUtil.getInstance().downLoadApk(MainActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
////                                        }
////                                    })
////                                    .setCancel(false)
////                                    .build();
//                        } else {//非强制更新
////                            long lastTime = SpUtil.getLong(Constant.LAST_UPDATE_SHOW_TIME);
////                            long currentTimeMillis = System.currentTimeMillis();
////                            if (lastTime == 0 || currentTimeMillis - lastTime > 1000 * 60 * 60 * 24) {//未取消过 或 取消间隔大于1天
////                                new AlertFragmentDialog.Builder(MainActivity.this)
////                                        .setTitle("更新提示")
////                                        .setContent(description)
////                                        .setRightBtnText("立即更新")
////                                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
////                                            @Override
////                                            public void dialogRightBtnClick() {
////                                                AppUpdateUtil.getInstance().downLoadApk(MainActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
////                                            }
////                                        })
////                                        .setLeftBtnText("暂不更新")
////                                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
////                                            @Override
////                                            public void dialogLeftBtnClick() {
////                                                SpUtil.putLong(Constant.LAST_UPDATE_SHOW_TIME, System.currentTimeMillis());//记录上次取消时间
////                                            }
////                                        })
////                                        .setCancel(false)
////                                        .build();
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
                boolean isOpenBanner = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_OPEN_BANNER,true);
                boolean isBannerRunning = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
                if (!isBannerRunning && isOpenBanner && isShowBanner) {
                    timeStart();
                }
                break;
            //否则其他动作计时取消
            default:
                if(countTimerView != null ) {
                    countTimerView.cancel();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countTimerView != null) {
            countTimerView.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isOpenBanner = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_OPEN_BANNER,true);
        boolean isBannerRunning = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
        if (!isBannerRunning && isOpenBanner && isShowBanner) {
            timeStart();
        }
    }

    /**
     * 跳轉廣告
     */
    public void timeStart() {

        if(countTimerView != null) {
            countTimerView.cancel();
        }
        long startBeginTime = SPUtil.getInstance().getLong(SPUtil.KEY_BANNER_START_TIME, 1000 * 60 * 5);
        countTimerView = new CountTimer(startBeginTime,1000,this);
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
