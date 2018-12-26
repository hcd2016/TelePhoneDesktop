package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.bean.SosBean;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import java.util.List;

public class SoSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<SosBean> sosBeans = DaoUtil.getSosBeanDao().loadAll();
        if (sosBeans != null && sosBeans.size() != 0) {
            CallUtil.call(context, sosBeans.get(0).getPhoneNum(),true);
        }
        Utils.Toast("SOS");
    }
}
