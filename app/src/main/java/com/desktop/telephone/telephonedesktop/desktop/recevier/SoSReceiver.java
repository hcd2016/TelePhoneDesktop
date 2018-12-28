package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.bean.SosBean;
import com.desktop.telephone.telephonedesktop.http.RetrofitUtil;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<SosBean> sosBeans = DaoUtil.getSosBeanDao().loadAll();
        if (sosBeans != null && sosBeans.size() != 0) {
            CallUtil.call(context, sosBeans.get(0).getPhoneNum(), true);
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
