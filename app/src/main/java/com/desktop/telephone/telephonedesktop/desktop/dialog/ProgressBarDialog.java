package com.desktop.telephone.telephonedesktop.desktop.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.TBaseDialog;

public class ProgressBarDialog extends TBaseDialog{
    public ProgressBarDialog(Context context) {
        super(context, R.layout.dialog_progress_bar);
        setWindowParam(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
    }
}
