package com.desktop.telephone.telephonedesktop.http;


import android.text.StaticLayout;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 */
public interface HttpApi {
    public static String baseUrl = "http://47.107.128.41:8080";

    //查询提交
    @FormUrlEncoded
    @POST("funs/getcreditreport")
    Call<JsonObject> commitQuery(@Field("userName") String userName,
                                 @Field("idno") String idno,
                                 @Field("phone") String phone,
                                 @Field("captcha") String captcha,
                                 @Field("RCaptchaKey") String RCaptchaKey,
                                 @Field("platform") String platform);

    /**
     * ---------------------------------------------------------------------------------------------------
     */

    //版本更新检查
    @FormUrlEncoded
    @POST("version/update")
    Call<JsonObject> checkUpdateVersion(@Query("appversion") String appVersion, @Query("platform") String platform);

    //检测借款金额是否超出限制
    @GET("credit-loan/check-limit")
    Call<JsonObject> checkLimit();

    //检测借款金额是否超出限制
    @FormUrlEncoded
    @POST(".cn")
    Call<JsonObject> getWeather();

}