package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.FamilyIDBean;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 亲情号码详情,添加过的
 */
public class FamilyDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_btn_delete_desk)
    TextView tvBtnDeleteDesk;
    @BindView(R.id.tv_btn_call)
    TextView tvBtnCall;
    private DesktopIconBean desktop_bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentStatus();
        setContentView(R.layout.activity_family_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        desktop_bean = (DesktopIconBean) getIntent().getSerializableExtra("desktop_bean");
        if (desktop_bean != null) {
            tvName.setText(desktop_bean.getTitle());
            tvNum.setText(desktop_bean.getPhoneNum());
        }
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
            window.setStatusBarColor(Color.BLACK);
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    public static void startActivity(Context context, DesktopIconBean desktopIconBean) {
        Intent intent = new Intent(context, FamilyDetailActivity.class);
        intent.putExtra("desktop_bean", desktopIconBean);
        context.startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.tv_btn_delete_desk, R.id.tv_btn_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_btn_delete_desk://移出桌面
                if (desktop_bean != null) {
                    if (desktop_bean.getMid() == 11) {
                        desktop_bean.setTitle("亲情1");
                    } else if (desktop_bean.getMid() == 12) {
                        desktop_bean.setTitle("亲情2");
                    } else {
                        desktop_bean.setTitle("亲情3");
                    }
                    desktop_bean.setImg_id_name("add_contact_icon");
                    //删除sp中数据并保存
                    List<FamilyIDBean> list = SPUtil.getList(this, SPUtil.KEY_SHOW_FAMILY_LIST);
                    Iterator<FamilyIDBean> it = list.iterator();
                    while (it.hasNext()) {
                        FamilyIDBean next = it.next();
                        if (next.getDeskId().equals(desktop_bean.getId()) ) {
                            it.remove();
                        }
                    }
                    SPUtil.putList(this,SPUtil.KEY_SHOW_FAMILY_LIST,list);

                    DaoUtil.getDesktopIconBeanDao().update(desktop_bean);
                    //通知桌面刷新
                    EventBus.getDefault().post(new EventBean(EventBean.REFRESH_DESK));
                    finish();
                }

                break;
            case R.id.tv_btn_call:
                CallUtil.call(this, tvNum.getText().toString(),false);
                break;
        }
    }
}