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
 * 删除联系人
 */
public class ContactsDeleteDialog extends TBaseDialog {
    @BindView(R.id.tv_one_key_call)
    TextView tvOneKeyCall;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    private BtnClickListener btnClickListener;

    public ContactsDeleteDialog(Context context) {
        super(context, R.layout.dialog_delete_contacts);
        setWindowParam(0.8f, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0);
    }

    @OnClick({R.id.tv_one_key_call, R.id.tv_delete, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_one_key_call:
                if (btnClickListener != null) {
                    btnClickListener.onKeyCallClick();
                }
                dismiss();
                break;
            case R.id.tv_delete:
                if (btnClickListener != null) {
                    btnClickListener.deleteClick();
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public interface BtnClickListener {
        void onKeyCallClick();

        void deleteClick();
    }

}
