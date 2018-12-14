package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;

/**
 * 来电界面
 */
public class IncomingCallingActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_calling);
    }
}
