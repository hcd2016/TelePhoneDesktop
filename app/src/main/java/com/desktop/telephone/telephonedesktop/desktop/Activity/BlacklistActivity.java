package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.SystemStatusBean;
import com.desktop.telephone.telephonedesktop.desktop.fragment.BlackListFramgent;
import com.desktop.telephone.telephonedesktop.util.BlackListFileUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlacklistActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_mode_desc)
    TextView tvModeDesc;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.ll_mode_container)
    LinearLayout llModeContainer;
    @BindView(R.id.ll_btn_add_container)
    LinearLayout llBtnAddContainers;
    private List<String> tabList;
    private PopupWindow popupWindow;
    private List<SystemStatusBean> systemStatusBeans;
    private MyApdater myApdater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tabList = new ArrayList<>();
        tabList.add("黑名单");
        tabList.add("红名单");
        myApdater = new MyApdater(getSupportFragmentManager());
        viewpager.setAdapter(myApdater);
        tablayout.setupWithViewPager(viewpager);
        systemStatusBeans = DaoUtil.getSystemStatusBeanDao().loadAll();
        if (systemStatusBeans != null && systemStatusBeans.size() != 0) {
            modeStatus = systemStatusBeans.get(0).getBlackListModeType();
        } else {
            DaoUtil.getSystemStatusBeanDao().insert(new SystemStatusBean(null, modeStatus));
            systemStatusBeans = DaoUtil.getSystemStatusBeanDao().loadAll();
        }
        //初始化:
        if (modeStatus == 0) {
            tvModeDesc.setText("普通模式");
        } else if (modeStatus == 1) {
            tvModeDesc.setText("黑名单模式");
        } else {
            tvModeDesc.setText("红名单模式");
        }

    }

    @OnClick({R.id.iv_back, R.id.ll_mode_container, R.id.ll_btn_add_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_mode_container://模式选择
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    showModePop();
                }
                break;
            case R.id.ll_btn_add_container://添加
                BlacklistAddActivity.startActivity(this, viewpager.getCurrentItem() + 1, "");
                break;
        }
    }

    private int modeStatus = 0; //0为普通,1为黑名单,2为红名单

    private void showModePop() {
        final int[] choiseStatus = {modeStatus};//记录选中状态,关闭时有可能没确定.
        View view = View.inflate(this, R.layout.pop_mode_choise, null);
        final ImageView ivGouNomal = view.findViewById(R.id.iv_gou_nomal);
        final ImageView ivBlacklist = view.findViewById(R.id.iv_blacklist);
        final ImageView ivWhitelist = view.findViewById(R.id.iv_whitelist);

        LinearLayout llModeNomalContainer = view.findViewById(R.id.ll_mode_nomal_container);
        LinearLayout llModeBlacklistContainer = view.findViewById(R.id.ll_mode_blacklist_container);
        LinearLayout llModeWhitelistContainer = view.findViewById(R.id.ll_mode_whitelist_container);

        TextView btn_cancle = view.findViewById(R.id.btn_cancle);
        TextView btn_sure = view.findViewById(R.id.btn_sure);

        //初始化:
        if (modeStatus == 0) {
            ivGouNomal.setVisibility(View.VISIBLE);
        } else if (modeStatus == 1) {
            ivBlacklist.setVisibility(View.VISIBLE);
        } else {
            ivWhitelist.setVisibility(View.VISIBLE);
        }

        llModeNomalContainer.setOnClickListener(new View.OnClickListener() {//选择普通模式
            @Override
            public void onClick(View view) {
                ivGouNomal.setVisibility(View.VISIBLE);
                ivBlacklist.setVisibility(View.GONE);
                ivWhitelist.setVisibility(View.GONE);
                choiseStatus[0] = 0;
            }
        });
        llModeBlacklistContainer.setOnClickListener(new View.OnClickListener() {//选择黑名单模式
            @Override
            public void onClick(View view) {
                ivGouNomal.setVisibility(View.GONE);
                ivBlacklist.setVisibility(View.VISIBLE);
                ivWhitelist.setVisibility(View.GONE);
                choiseStatus[0] = 1;
            }
        });
        llModeWhitelistContainer.setOnClickListener(new View.OnClickListener() {//选择红名单模式
            @Override
            public void onClick(View view) {
                ivGouNomal.setVisibility(View.GONE);
                ivBlacklist.setVisibility(View.GONE);
                ivWhitelist.setVisibility(View.VISIBLE);
                choiseStatus[0] = 2;
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeStatus = choiseStatus[0];//保存选择状态
                if (modeStatus == 0) {
                    tvModeDesc.setText("普通模式");
                } else if (modeStatus == 1) {
                    tvModeDesc.setText("黑名单模式");
                } else {
                    tvModeDesc.setText("红名单模式");
                }
//                if (systemStatusBeans == null || systemStatusBeans.size() == 0) {
//                    SystemStatusBean systemStatusBean = new SystemStatusBean(null, modeStatus);
//                    DaoUtil.getSystemStatusBeanDao().insertOrReplace(systemStatusBean);
//                } else {
                BlackListFileUtil.setModeChangeFile(modeStatus);//保存模式对应文件处理,在保存数据库之前调用
                systemStatusBeans.get(0).setBlackListModeType(modeStatus);
                DaoUtil.getSystemStatusBeanDao().update(systemStatusBeans.get(0));
//                }

                popupWindow.dismiss();
                Utils.Toast("设置成功");
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(llModeContainer);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivArrow.setImageResource(R.drawable.arrow_down);
            }
        });
        ivArrow.setImageResource(R.drawable.arrow_up);
    }


    class MyApdater extends FragmentPagerAdapter {

        public MyApdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return BlackListFramgent.newInstance(1);//1为黑名单,2为红名单
            } else {
                return BlackListFramgent.newInstance(2);
            }
        }

        @Override
        public int getCount() {
            return tabList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }
    }

}
