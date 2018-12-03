package com.desktop.telephone.telephonedesktop.desktop.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.TBaseDialog;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 删除黑白名单
 */
public class BlacklistDeleteDialog extends TBaseDialog {
    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    private BtnClickListener btnClickListener;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_btn_call)
    TextView tvBtnCall;
    @BindView(R.id.tv_btn_add_to_list)
    TextView tvBtnAddToList;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    public BlacklistDeleteDialog(Context context) {
        super(context, R.layout.dialog_delete);
        setWindowParam(0.8f, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0);
    }

    public void setData(BlackListInfoBean blackListInfoBean) {
        tvPhoneNum.setText(blackListInfoBean.getPhone());
        if (blackListInfoBean.getType() == 1) {
            tvBtnAddToList.setText("加入白名单");
        } else {
            tvBtnAddToList.setText("加入黑名单");
        }
    }

    @OnClick({R.id.tv_btn_call, R.id.tv_btn_add_to_list, R.id.tv_delete,R.id.tv_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_call:
                if (btnClickListener != null) {
                    btnClickListener.callClick();
                }
                dismiss();
                break;
            case R.id.tv_btn_add_to_list:
                if (btnClickListener != null) {
                    btnClickListener.addToListClick();
                }
                dismiss();
                break;
            case R.id.tv_delete:
                if (btnClickListener != null) {
                    btnClickListener.deleteClick();
                }
                dismiss();
                break;
            case R.id.tv_update:
                if (btnClickListener != null) {
                    btnClickListener.updateClick();
                }
                dismiss();
                break;
        }
    }

    public interface BtnClickListener {
        void callClick();

        void addToListClick();

        void deleteClick();

        void updateClick();
    }

}
