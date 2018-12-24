package com.desktop.telephone.telephonedesktop.util.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.desktop.telephone.telephonedesktop.bean.WeatherBean;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetWeatherInfoTask extends AsyncTask<String,Void,String> {
    private Context context;
//    private RecyclerView weatherRecycler;

    public GetWeatherInfoTask(Context context) {
        this.context = context;
//        this.weatherRecycler = weatherRecycler;
    }

    @Override
    protected String doInBackground(String... strings) {
        return getWeatherInfo(strings[0]);
    }

    //耗时请求完毕执行ui线程更新
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s == null) {
            WeatherBean weatherBean = new WeatherBean();
            weatherBean.setSucess(false);
            EventBus.getDefault().post(weatherBean);
        }
        if (s != null && s.length() > 5){
//            weatherRecycler.setVisibility(View.VISIBLE);
//            initRecycler(s);
        }
    }

    private String getWeatherInfo(String cityName){
        HttpURLConnection connection;
        try{//将请求天气的接口转换成url
            URL url = new URL("http://wthrcdn.etouch.cn/weather_mini?city="+ cityName);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            if (code == 200){
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream  outputStream = new ByteArrayOutputStream();

                byte[] tmpBuffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(tmpBuffer)) != -1){
                    outputStream.write(tmpBuffer,0,len);
                }

                outputStream.close();
                inputStream.close();

                final String weatherStr = new String(outputStream.toByteArray(),"UTF-8");
                String weather = "";
                //解析json
                try {
                    return ParaseJsonUtils.parasrWeather(context,weatherStr);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
//            UseFileSingleton.appendStringToFileWithTimestamp(context,"请求天气",e);
            return null;
        }
    }

//    private void initRecycler(String s){
////        List<String> weatherData = new ArrayList<>();
////        for (int i =0; i < 4; i++){
////            weatherData.add(s);
////        }
////        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
////        weatherRecycler.setLayoutManager(linearLayoutManager);
////        weatherRecycler.setAdapter(new WeatherInfoRecyclerAdapter(context,weatherData));
//        handler.sendEmptyMessageDelayed(0x00,1000);
//    }

//    //handler使RecyclerView不断前进的动力
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
////            weatherRecycler.scrollBy(weatherRecycler.getScrollX() + 2,weatherRecycler.getScrollY());
//            handler.sendEmptyMessageDelayed(0x00,1000);
//        }
//    };
}
