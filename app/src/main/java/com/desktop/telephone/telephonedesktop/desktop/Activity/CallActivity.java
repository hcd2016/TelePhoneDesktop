package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.desktop.fragment.CallFragment;
import com.desktop.telephone.telephonedesktop.desktop.fragment.CallRecordsFragment;
import com.desktop.telephone.telephonedesktop.desktop.fragment.ContactsListFragment;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallActivity extends BaseActivity {
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<String> tabs;
    public static final String HAND_OFF = "com.tongen.action.handle.off";//手柄放下
    public static final String HAND_ON = "com.tongen.action.handle.on";//手柄抬起

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定竖屏
        translucentStatus();
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
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


    private void initView() {
        tabs = new ArrayList<>();
        tabs.add("拨号");
        tabs.add("通话记录");
        tabs.add("联系人");

        CallAdapter callAdapter = new CallAdapter(getSupportFragmentManager());
        viewpager.setAdapter(callAdapter);
        tablayout.setupWithViewPager(viewpager);
        int type = getIntent().getIntExtra("type", 0);//0为拨号,1为通话记录,2为联系人
        viewpager.setCurrentItem(type);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null) {
            int type = intent.getIntExtra("type", 0);
            viewpager.setCurrentItem(type);
        }
    }

    private boolean isShowing = false;//当前activity是否在前台

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Intent event) {
        switch (event.getAction()) {
            case HAND_OFF://手柄放下
                if (isShowing) {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowing = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShowing = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class CallAdapter extends FragmentPagerAdapter {

        public CallAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return CallFragment.newInstance();
            } else if (i == 1) {
                return CallRecordsFragment.newInstance();
            } else {
                return ContactsListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }

    public static void startActivity(int type, Context context) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
