package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 联系人详情
 */
public class ContactsDetailActivity extends BaseActivity {
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_phone_container)
    LinearLayout llPhoneContainer;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.ll_btn_edit_container)
    LinearLayout llBtnEditContainer;
    private ContactsBean constacts_bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        collapsingToolbarLayout.setExpandedTitleColor(Utils.getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Utils.getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleStyle);//设置展开标题字体样式
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTitleStyle);//设置收缩标题字体样式
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.LEFT);//设置收缩后标题的位置
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.LEFT | Gravity.BOTTOM);////设置展开后标题的位置
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        constacts_bean = (ContactsBean) bundle.getSerializable("constacts_bean");
        if (constacts_bean != null) {
            collapsingToolbarLayout.setTitle(constacts_bean.name);
            tvPhone.setText(constacts_bean.phone);
            tvEmail.setText(constacts_bean.email);
            tvDesc.setText(constacts_bean.desc);
        }

        llPhoneContainer.setOnClickListener(new View.OnClickListener() {//拨打电话
            @Override
            public void onClick(View v) {
                //todo
            }
        });
    }

    @OnClick(R.id.ll_btn_edit_container)//编辑
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("constacts_bean",constacts_bean);
        bundle.putInt("isEdit",1);
        startActivity(AddContactsActivity.class,bundle);
    }

}
