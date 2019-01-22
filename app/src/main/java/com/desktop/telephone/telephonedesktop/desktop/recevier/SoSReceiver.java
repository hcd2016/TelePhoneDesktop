package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.bean.SosBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallingActivity;
import com.desktop.telephone.telephonedesktop.http.RetrofitUtil;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<SosBean> sosBeans = DaoUtil.getSosBeanDao().loadAll();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if(cn.getClassName().equals(CallingActivity.class.getName())) {//当前正在通话中
            EventBus.getDefault().post(intent);
        }else {
            if (sosBeans != null && sosBeans.size() != 0) {
                CallUtil.call(context, sosBeans.get(0).getPhoneNum(), true);
            }
        }
        Utils.Toast("SOS");
        for (int i = 0; i < sosBeans.size(); i++) {
            Call<JsonObject> call = RetrofitUtil.create().sendSms(sosBeans.get(i).getPhoneNum(), sosBeans.get(i).getSmsContent());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    response.body();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.toString();
                }
            });
        }
    }
}
