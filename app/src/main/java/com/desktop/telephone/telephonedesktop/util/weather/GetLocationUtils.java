package com.desktop.telephone.telephonedesktop.util.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;


import com.desktop.telephone.telephonedesktop.bean.WeatherBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

public class GetLocationUtils {
    public static Geocoder geocoder;    //获取并解析位置用
//    public static RecyclerView weatherRecycler;     //用于循环播放天气信息的RecyclerView，会被传到获取天气的异步请求类中
    public static Context mContext;

    public static void getCityByLocation(Context context){
        mContext = context;
//        weatherRecycler = recyclerView;
        geocoder = new Geocoder(context, Locale.getDefault());
        LocationManager locationManager;        //管理Location及其他信息
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(serviceName);
        //privider类型
        String provider = LocationManager.NETWORK_PROVIDER;
        //定位设置
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);//低精度
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(false);//不允许资费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低耗电

        Location location = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            List<String> allProviders = locationManager.getAllProviders();
            location = locationManager.getLastKnownLocation(provider);
            if(location == null) {
                WeatherBean weatherBean = new WeatherBean();
                weatherBean.setSucess(false);
                EventBus.getDefault().post(weatherBean);
                return;
            }
            //获取位置需要访问Google提供的服务进行位置解析，比较耗时所以就放在task中，（其实在Android6.0及以下，可以直接放在主线程中执行，Android8.0必须放在Task中）
            new GetCityNameByGeocoder(context,geocoder).execute(location);
        }

        //第一个参数提供的类型，第二个参数更新周期(毫秒)，第三个最小距离间隔500m
        locationManager.requestLocationUpdates(provider,300000,500,locationListener);
    }

    private final static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //获取位置需要访问Google提供的服务进行位置解析，比较耗时所以就放在task中，（其实在Android6.0及以下，可以直接放在主线程中执行，Android8.0必须放在Task中）
            new GetCityNameByGeocoder(mContext,geocoder).execute(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
