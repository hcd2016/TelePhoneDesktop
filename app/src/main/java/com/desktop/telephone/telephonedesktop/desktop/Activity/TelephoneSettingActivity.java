package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TelephoneSettingActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
//    @BindView(R.id.tv_mode_desc)
//    TextView tvModeDesc;
//    @BindView(R.id.rb_show)
//    RadioButton rbShow;
//    @BindView(R.id.rb_hide)
//    RadioButton rbHide;
    @BindView(R.id.et_interchanger)
    EditText etInterchanger;
    @BindView(R.id.tv_save)
    TextView tvSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
//        boolean isShow = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_SHOW_COMMING_CALL_NUM, true);
//        if (isShow) {
//            rbShow.setChecked(true);
//        } else {
//            rbShow.setChecked(false);
//        }
        int time = SPUtil.getInstance().getInteger(SPUtil.KEY_INTERCHANGER_SETTING);
        etInterchanger.setText(time + "");
        etInterchanger.setSelection(etInterchanger.getText().toString().length());
    }

    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save://保存
                if (TextUtils.isEmpty(etInterchanger.getText().toString())) {
                    Utils.Toast("交换机参数不能为空");
                    return;
                }
//                SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_SHOW_COMMING_CALL_NUM, rbShow.isChecked());
//                if (rbShow.isChecked()) {
//                    CallUtil.showCallerIds(this, 1);
//                } else {
//                    CallUtil.showCallerIds(this, 0);
//                }
                String s = etInterchanger.getText().toString();
                SPUtil.getInstance().saveInteger(SPUtil.KEY_INTERCHANGER_SETTING, Integer.parseInt(s));
                CallUtil.interchangerSetting(this, Integer.parseInt(s));
                Utils.Toast("保存成功");
                break;
        }
    }
}
