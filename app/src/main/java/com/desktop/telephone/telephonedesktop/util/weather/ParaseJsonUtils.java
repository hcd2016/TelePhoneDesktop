package com.desktop.telephone.telephonedesktop.util.weather;

import android.content.Context;

import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.WeatherBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParaseJsonUtils {
    public static String parasrWeather(Context context, String string) {
        String weatherInfo = "";
        WeatherBean weatherBean = null;
        String dushu = "";
        try {
            JSONObject object = new JSONObject(string);
            JSONObject data = object.getJSONObject("data");
//            weatherInfo += data.optString("city");
            JSONArray weatherArray = data.getJSONArray("forecast");
            for (int i = 0; i < 1; i++) {
                JSONObject today = weatherArray.getJSONObject(i);
                dushu = today.optString("type");
//                weatherInfo += ",天气:" + today.optString("type");
//                weatherInfo += "," + today.optString("low") + "," + today.optString("high");
//                weatherInfo += "," + today.optString("fengxiang");
//                String fengli = today.optString("fengli");
//                weatherInfo += fengli.substring(fengli.lastIndexOf("[") + 1 ,fengli.lastIndexOf("级]") + 1);
            }
            weatherBean = new WeatherBean();
            weatherBean.setCity(data.optString("city"));
            weatherBean.setDushu(data.optString("wendu"));
            weatherBean.setWeather(dushu);
            EventBus.getDefault().post(weatherBean);
        } catch (Exception e) {
            e.printStackTrace();
            weatherBean = new WeatherBean();
            weatherBean.setSucess(false);
            EventBus.getDefault().post(weatherBean);
            return null;
        }
        return weatherInfo;
    }
}
