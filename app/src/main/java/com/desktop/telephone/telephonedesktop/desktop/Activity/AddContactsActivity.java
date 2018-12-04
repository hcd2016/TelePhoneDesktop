package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加或编辑联系人
 */
public class AddContactsActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_desc)
    EditText etDesc;
    private int status = 0;//0为添加状态,1为编辑状态
    private ContactsBean contactsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (status == 0) {
            tvTitle.setText("新建联系人");
        } else {
            tvTitle.setText("编辑联系人");
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                contactsBean = (ContactsBean) bundle.getSerializable("constacts_bean");
                status = bundle.getInt("isEdit", 0);
                etName.setText(contactsBean.getName());
                etName.setSelection(contactsBean.getName().length());
                etEmail.setText(contactsBean.getEmail());
                etDesc.setText(contactsBean.getDesc());
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_save://保存
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String desc = etDesc.getText().toString();
                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Utils.Toast("电话号码不能为空");
                    return;
                }
                if (status == 0) {//新建
                    if (TextUtils.isEmpty(name)) {
                        name = phone;//未设置姓名phone设为姓名值
                    }
                    //不判重复,直接加入数据库
                    ContactsBean contactsBean = new ContactsBean(null, name, email, desc, phone);
                    DaoUtil.getContactsBeanDao().insert(contactsBean);
                    Utils.Toast("添加成功");
                } else {//修改
                    Long id = contactsBean.getId();
                    ContactsBean contactsBean = new ContactsBean(id, name, email, desc, phone);
                    DaoUtil.getContactsBeanDao().update(contactsBean);
                    Utils.Toast("修改成功");
                }
                EventBus.getDefault().post(new EventBean(EventBean.CONTACTS_ADD_SUCCESS));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DaoUtil.closeDb();
    }
}
