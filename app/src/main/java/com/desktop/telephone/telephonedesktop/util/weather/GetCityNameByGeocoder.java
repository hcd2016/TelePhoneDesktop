package com.desktop.telephone.telephonedesktop.util.weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.WeatherBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GetCityNameByGeocoder extends AsyncTask<Location,Void,String> {
    private Context context;
    private Geocoder geocoder;
//    private RecyclerView weatherrecycler;

    public GetCityNameByGeocoder(Context context, Geocoder geocoder) {
        this.context = context;
        this.geocoder = geocoder;
//        this.weatherrecycler = weatherrecycler;
    }

    @Override
    protected String doInBackground(Location... locations) {
        return getCityName(locations[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null){//返回的城市不为空，执行请求天气Task
            new GetWeatherInfoTask(context).execute(s);
        }else {
            WeatherBean weatherBean = new WeatherBean();
            weatherBean.setSucess(false);
            EventBus.getDefault().post(weatherBean);
        }
    }

    private String getCityName(Location location){
        String tmpCity = "";
        double lat = 0;
        double lng = 0;
        List<Address> addressList = null;
        if (location != null){
            lat = location.getLatitude();
            lng = location.getLongitude();
        }else {
            tmpCity = null;
        }
        try {
            //此处访问Google的位置服务，需要放在异步线程中
            addressList = geocoder.getFromLocation(lat,lng,1);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() >0){
            Address address = addressList.get(0);
            //获取目标城市
            tmpCity = address.getLocality();
        }
        if (tmpCity.length() > 0 && !"".equals(tmpCity)){
            tmpCity = tmpCity.substring(0,(tmpCity.length() - 1));
        }
        return tmpCity;
    }
}
