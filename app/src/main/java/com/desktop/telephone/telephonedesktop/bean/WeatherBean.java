package com.desktop.telephone.telephonedesktop.bean;

public class WeatherBean {
    public String city;//城市
    public String weather;//天气
    public String dushu;//气温

    public boolean isSucess = true;//如果为false表示失败了

    public boolean isSucess() {
        return isSucess;
    }

    public void setSucess(boolean sucess) {
        isSucess = sucess;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDushu() {
        return dushu;
    }

    public void setDushu(String dushu) {
        this.dushu = dushu;
    }
}
