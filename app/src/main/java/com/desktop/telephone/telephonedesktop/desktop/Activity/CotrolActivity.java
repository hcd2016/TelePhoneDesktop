package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.http.RetrofitUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CotrolActivity extends BaseActivity {
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_btn_try)
    TextView tvBtnTry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotrol);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_btn_try)
    public void onViewClicked() {
        Call<JsonObject> call = RetrofitUtil.create().control(Utils.getPackageCode(this));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    int code = object.optInt("code");
                    if(code == 999) {
                        Utils.Toast("请联系管理员...");
                    }else {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.toString();
            }
        });
    }
}
