package com.desktop.telephone.telephonedesktop.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.view.CountTimer;

public class BaseActivity extends AppCompatActivity {
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
