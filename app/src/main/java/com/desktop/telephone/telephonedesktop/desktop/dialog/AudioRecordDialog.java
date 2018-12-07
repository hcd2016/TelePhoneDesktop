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
 * 录音完成操作dialog
 */
public class AudioRecordDialog extends TBaseDialog {

    @BindView(R.id.tv_btn_save)
    TextView tvBtnSave;
    @BindView(R.id.tv_btn_back)
    TextView tvBtnBack;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    private OnAudioRecordDialogClickListener onAudioRecordDialogClickListener;

    public void setOnAudioRecordDialogClickListener(OnAudioRecordDialogClickListener onAudioRecordDialogClickListener) {
        this.onAudioRecordDialogClickListener = onAudioRecordDialogClickListener;
    }


    public AudioRecordDialog(Context context) {
        super(context, R.layout.dialog_audio_record);
        setWindowParam(0.8f, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0);
    }

    public void setData(BlackListInfoBean blackListInfoBean) {
    }

    @OnClick({R.id.tv_btn_save, R.id.tv_btn_back, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_save:
                if(onAudioRecordDialogClickListener != null) {
                    onAudioRecordDialogClickListener.saveClick();
                }
                break;
            case R.id.tv_btn_back:
                if(onAudioRecordDialogClickListener != null) {
                    onAudioRecordDialogClickListener.backClick();
                }
                break;
            case R.id.tv_delete:
                if(onAudioRecordDialogClickListener != null) {
                    onAudioRecordDialogClickListener.deleteClick();
                }
                break;
        }
    }

    public interface OnAudioRecordDialogClickListener {
        void saveClick();

        void backClick();

        void deleteClick();

    }

}
