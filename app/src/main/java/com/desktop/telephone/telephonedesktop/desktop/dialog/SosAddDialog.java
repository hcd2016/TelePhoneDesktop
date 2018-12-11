package com.desktop.telephone.telephonedesktop.desktop.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.TBaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * sos添加
 */
public class SosAddDialog extends TBaseDialog {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_btn_sure)
    TextView tvBtnSure;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    public SosAddDialog(Context context) {
        super(context, R.layout.dialog_sos_add);
        setWindowParam(0.8f, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
    }

    public void setOnConfirmClickListner(OnConfirmClickListner onConfirmClickListner) {
        this.onConfirmClickListner = onConfirmClickListner;
    }

    public String getName() {
        return etName.getText().toString().trim();
    }

    public String getPhoneNum() {
        return etPhoneNum.getText().toString().trim();
    }

    public String getContent() {
        return etContent.getText().toString().trim();
    }

    private OnConfirmClickListner onConfirmClickListner;

    @OnClick({R.id.tv_cancel, R.id.tv_btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_btn_sure:
                if (onConfirmClickListner != null) {
                    onConfirmClickListner.confirmClick();
                }
                dismiss();
                break;
        }
    }

    public interface OnConfirmClickListner {
        void confirmClick();
    }
}
