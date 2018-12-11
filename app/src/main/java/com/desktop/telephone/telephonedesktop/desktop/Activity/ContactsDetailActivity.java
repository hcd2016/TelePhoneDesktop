package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.util.ContactsUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.ll_email_container)
    LinearLayout llEmailContainer;
    @BindView(R.id.ll_desc_container)
    LinearLayout llDescContainer;
    @BindView(R.id.ll_btn_delete_container)
    LinearLayout llBtnDeleteContainer;
    private ContactsBean constacts_bean;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
        ContactsBean bean = (ContactsBean) bundle.getSerializable("constacts_bean");
        constacts_bean = ContactsUtil.getDetailFromContactID(this, bean);
        setData(constacts_bean);

        llPhoneContainer.setOnClickListener(new View.OnClickListener() {//拨打电话
            @Override
            public void onClick(View v) {
                //todo
            }
        });
    }

    public void setData(ContactsBean constacts_bean) {
        if (constacts_bean != null) {
            collapsingToolbarLayout.setTitle(constacts_bean.name);
            tvPhone.setText(constacts_bean.phone);
            if (TextUtils.isEmpty(constacts_bean.email)) {
                llEmailContainer.setVisibility(View.GONE);
            } else {
                llEmailContainer.setVisibility(View.VISIBLE);
                tvEmail.setText(constacts_bean.email);
            }
            if (TextUtils.isEmpty(constacts_bean.desc)) {
                llDescContainer.setVisibility(View.GONE);
            } else {
                llDescContainer.setVisibility(View.VISIBLE);
                tvDesc.setText(constacts_bean.desc);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ContactsBean constacts_bean) {//修改成功,重置数据
        setData(constacts_bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ll_btn_edit_container, R.id.ll_btn_delete_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_btn_edit_container://编辑
                Bundle bundle = new Bundle();
                bundle.putSerializable("constacts_bean", constacts_bean);
                bundle.putInt("isEdit", 1);
                startActivity(AddContactsActivity.class, bundle);
                break;
            case R.id.ll_btn_delete_container://删除
                alertDialog = new AlertDialog.Builder(this)
                        .setMessage("确定要删除该联系人吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContactsUtil.delete(constacts_bean.getId());
//                                DaoUtil.getContactsBeanDao().delete(constacts_bean);
                                EventBus.getDefault().post(new EventBean(EventBean.CONTACTS_ADD_SUCCESS));
                                finish();
                                Utils.Toast("删除成功");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                break;
        }
    }
}
