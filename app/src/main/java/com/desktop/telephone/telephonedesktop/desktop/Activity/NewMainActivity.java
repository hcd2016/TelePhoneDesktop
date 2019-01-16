package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.App;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.WeatherBean;
import com.desktop.telephone.telephonedesktop.gen.AppInfoBeanDao;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;
import com.desktop.telephone.telephonedesktop.http.RetrofitUtil;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.DensityUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.util.weather.GetLocationUtils;
import com.desktop.telephone.telephonedesktop.util.weather.ParaseJsonUtils;
import com.desktop.telephone.telephonedesktop.view.MyGridLayoutManager;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMainActivity extends BaseActivity {
    private static PowerManager.WakeLock wakeLock;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int line = 4;//行
    private int row = 3;//列
    private int pageCount = line * row;//分页数量
    private ArrayList<DesktopIconBean> defaultList;
    private List<DesktopIconBean> mList;
    private List<GridAdapter> adpterList;
    private List<List<DesktopIconBean>> lists;
    private BroadcastReceiver deletePackageReceiver;
    private MyPagerAdapter myPagerAdapter;
    private LocationClient locationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentStatus();
        setContentView(R.layout.activity_record_test_desk);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initIconData();
        initView();
        screenControl();
        CallUtil.showCallerIds(this, 1);
        registerHomeKeyReceiver(this);
        initLocationOption();
    }


    /**
     * 初始化定位参数配置
     */
    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();

//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000 * 60 * 10);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
////可选，设置是否需要地址描述
//        locationOption.setIsNeedLocationDescribe(false);
////可选，设置是否需要设备方向结果
//        locationOption.setNeedDeviceDirect(false);
////可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        locationOption.setLocationNotify(true);
////可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        locationOption.setIgnoreKillProcess(true);
////可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        locationOption.setIsNeedLocationDescribe(false);
////可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        locationOption.setIsNeedLocationPoiList(false);
////可选，默认false，设置是否收集CRASH信息，默认收集
//        locationOption.SetIgnoreCacheException(false);
////可选，默认false，设置是否开启Gps定位
//        locationOption.setOpenGps(false);
////可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
//        locationOption.setIsNeedAltitude(false);
//开始定位
        locationClient.setLocOption(locationOption);
        locationClient.start();
    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();

            View view = myPagerAdapter.getHeaderItemView();
            final LinearLayout ll_weather_container = view.findViewById(R.id.ll_weather_container);
            final TextView tv_weather = view.findViewById(R.id.tv_weather);
            final TextView tv_city = view.findViewById(R.id.tv_city);
            final TextView tv_dushu = view.findViewById(R.id.tv_dushu);
            final ImageView iv_weather = view.findViewById(R.id.iv_weather);

            String cityCode = location.getCityCode();
            String city = location.getCity();
            tv_city.setText(city);
            if (TextUtils.isEmpty(city)) {
                ll_weather_container.setVisibility(View.GONE);
                return;
            } else {
                if (city.contains("市")) {
                    city = city.replace("市", "");
                }
            }

//            String weatherInfo = getWeatherInfo(city);
            Call<JsonObject> call = RetrofitUtil.create().getWeather(city);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    ParaseJsonUtils.parasrWeather(NewMainActivity.this, response.body().toString());
                    String weatherInfo = "";
                    WeatherBean weatherBean = null;
                    String desc = "";
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        JSONObject data = object.getJSONObject("data");
                        JSONArray weatherArray = data.getJSONArray("forecast");
                        for (int i = 0; i < 1; i++) {
                            JSONObject today = weatherArray.getJSONObject(i);
                            desc = today.optString("type");
                        }
                        tv_weather.setText(desc);
                        tv_dushu.setText(data.optString("wendu") + "℃");
                        ll_weather_container.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(desc)) {
                            if (desc.contains("雨")) {
                                iv_weather.setImageResource(R.drawable.weather_yu);
                            } else if (desc.contains("晴")) {
                                iv_weather.setImageResource(R.drawable.weather_qing);
                            } else if (desc.contains("雪")) {
                                iv_weather.setImageResource(R.drawable.weather_xue);
                            } else {
                                iv_weather.setImageResource(R.drawable.weather_yin);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ll_weather_container.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.toString();
                    ll_weather_container.setVisibility(View.GONE);
                }
            });
        }
    }


    //透明状态栏
    public void translucentStatus() {
        //透明状态栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Utils.getColor(R.color.text_black333));
            window.setNavigationBarColor(Utils.getColor(R.color.text_black333));
        }
    }

    //自定义的广播接收者
    private HomeWatcherReceiver mHomeKeyReceiver = null;

    //注册广播接收者，监听Home键
    private void registerHomeKeyReceiver(Context context) {
        mHomeKeyReceiver = new HomeWatcherReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    //取消监听广播接收者
    private void unregisterHomeKeyReceiver(Context context) {
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    class HomeWatcherReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        //按Home按键
                        Intent intent1 = new Intent(context, NewMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                }
            }
        }
    }

    /**
     * 桌面数据添加
     */
    private void initIconData() {
//        DaoUtil.getDesktopIconBeanDao().deleteAll();
        defaultList = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
        PackageManager pm = getPackageManager();
        //所有的安装在系统上的应用程序包信息。
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        for (int i = 0; i < 16; i++) {
//        for (int i = 0; i < 15; i++) {
            DesktopIconBean moveItem = new DesktopIconBean();
            moveItem.setMid(i);
            switch (i) {
                case 8:
                    //电话
                    moveItem.setIconType(1);
                    moveItem.setTitle("电话");
                    moveItem.setImg_id_name("phone_icon");
                    break;
                case 7:
                    //联系人
                    moveItem.setIconType(1);
                    moveItem.setTitle("联系人");
                    moveItem.setImg_id_name("call_records_icon");
                    break;
                case 3:
                    //相册
                    moveItem.setIconType(1);
                    moveItem.setTitle("图库");
                    moveItem.setImg_id_name("photo_icon");
                    break;
                case 5:
                    //黑红名单
                    moveItem.setIconType(1);
                    moveItem.setTitle("黑红名单");
                    moveItem.setImg_id_name("blacklist_icon");
                    break;
                case 14:
                    //录音
                    moveItem.setIconType(1);
                    moveItem.setTitle("录音");
                    moveItem.setImg_id_name("record_icon");
                    break;
                case 15:
//                case 0:
                    //通话记录
                    moveItem.setIconType(1);
                    moveItem.setTitle("通话记录");
                    moveItem.setImg_id_name("address_list_icon");
                    break;
                case 6:
                    //sos
                    moveItem.setIconType(1);
                    moveItem.setTitle("紧急呼叫");
                    moveItem.setImg_id_name("sos_icon");
                    break;
                case 9:
                    //所有应用
                    moveItem.setIconType(1);
                    moveItem.setTitle("所有应用");
                    moveItem.setImg_id_name("all_apps_icon");
                    break;
                case 1://设置
//                    PackageManager pm = getPackageManager();
//                    //所有的安装在系统上的应用程序包信息。
//                    List<PackageInfo> packInfos = pm.getInstalledPackages(0);
                    for (int j = 0; j < packInfos.size(); j++) {
                        PackageInfo packInfo = packInfos.get(j);
                        if (packInfo.packageName.equals("com.android.settings")) {
                            moveItem.setIconType(2);
                            moveItem.setTitle("设置");
                            moveItem.setPackageName(packInfo.packageName);
                            moveItem.setImg_id_name("settings_icon");
//                            moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                        }
                    }
                    break;
                case 0://相机 记得改上面size
//                    PackageManager pm1 = getPackageManager();
//                    //所有的安装在系统上的应用程序包信息。
//                    List<PackageInfo> packInfos1 = pm1.getInstalledPackages(0);
                    for (int j = 0; j < packInfos.size(); j++) {
                        PackageInfo packInfo = packInfos.get(j);
                        if (packInfo.packageName.equals("com.android.camera2")) {
//                        if (packInfo.packageName.equals("com.android.camera")) {
                            moveItem.setIconType(2);
                            moveItem.setTitle("相机");
                            moveItem.setPackageName(packInfo.packageName);
                            moveItem.setImg_id_name("photos_icon");
//                            moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm1)));
                        }
                    }
                    break;

                case 4://浏览器
                    for (int j = 0; j < packInfos.size(); j++) {
                        PackageInfo packInfo = packInfos.get(j);
                        if (packInfo.packageName.equals("com.android.browser")) {
                            moveItem.setIconType(2);
                            moveItem.setTitle("上网");
                            moveItem.setImg_id_name("intent_icon");
                            moveItem.setPackageName(packInfo.packageName);
//                            moveItem.setImg_id_name("settings_icon");
//                            moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                        }
                    }
                    break;
                case 2://微信
                    boolean isHaveWeiXin = false;
                    for (int j = 0; j < packInfos.size(); j++) {
                        PackageInfo packInfo = packInfos.get(j);
                        if (packInfo.packageName.equals("com.tencent.mm")) {
                            isHaveWeiXin = true;
//                            moveItem.setImg_id_name("settings_icon");
//                            moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                        }
                    }
                    SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_HAVE_WEIXIN,isHaveWeiXin);
                    if (isHaveWeiXin) {
                        moveItem.setIconType(2);
                        moveItem.setTitle("微信");
                        moveItem.setPackageName("com.tencent.mm");
                        moveItem.setImg_id_name("weixin_icon");
                    } else {
                        for (int j = 0; j < packInfos.size(); j++) {
                            PackageInfo packInfo = packInfos.get(j);
                            if (packInfo.packageName.equals("com.android.calendar")) {
                                moveItem.setIconType(1);
                                moveItem.setTitle("日历");
                                moveItem.setPackageName("com.android.calendar");
                                moveItem.setImg_id_name("rili_icon");
//                                moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                            }
                        }
                    }
                    break;
                case 10:
//                case 8:
                    //分机设置
                    moveItem.setIconType(1);
                    moveItem.setTitle("分机设置");
                    moveItem.setImg_id_name("phone_setting");
                    break;
                case 11:
                    //亲情号码1
                    moveItem.setIconType(4);
                    moveItem.setTitle("亲情1");
                    moveItem.setImg_id_name("add_contact_icon");
                    break;
                case 12:
                    //亲情号码2
                    moveItem.setIconType(4);
                    moveItem.setTitle("亲情2");
                    moveItem.setImg_id_name("add_contact_icon");
                    break;
                case 13:
                    //亲情号码3
                    moveItem.setIconType(4);
                    moveItem.setTitle("亲情3");
                    moveItem.setImg_id_name("add_contact_icon");
                    break;
            }
            moveItem.setMid(i);
            moveItem.setIconBgColor(Utils.getColorBgFromPosition(i));
            defaultList.add(moveItem);
        }
        for (int j = 0; j < packInfos.size(); j++) {
            PackageInfo packInfo = packInfos.get(j);
            String packname = packInfo.packageName;
            String appName = packInfo.applicationInfo.loadLabel(pm).toString();
            if (packname.equals("com.chinatelecom.bestpayclient")
                    || packname.equals("com.ximalaya.ting.android") || packname.equals("com.yidian.health")
                    || packname.equals("com.sinyee.babybus.chants") || packname.equals("com.guangjun.cookbook")) {
                DesktopIconBean desktopIconBean = new DesktopIconBean();
                desktopIconBean.setIconType(2);
                desktopIconBean.setTitle(appName);
                desktopIconBean.setPackageName(packInfo.packageName);
                desktopIconBean.setMid(defaultList.size());
                desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(defaultList.size()));
                desktopIconBean.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                defaultList.add(desktopIconBean);
            }
        }

        mList = DaoUtil.querydata();
        if (mList == null || mList.size() == 0) {//数据库中没有列表(第一次安装)
            mList.addAll(defaultList);
            DaoUtil.saveNLists(defaultList);//保存默认的list
        }
    }

    private void initView() {
        lists = calculate(mList, pageCount);
        adpterList = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            GridAdapter gridAdapter = new GridAdapter(lists.get(i));
            adpterList.add(gridAdapter);
        }
        myPagerAdapter = new MyPagerAdapter(lists);
        viewpager.setAdapter(myPagerAdapter);
    }


    //开关屏幕常亮
    @SuppressLint("InvalidWakeLockTag")
    public static void keepScreenOn(Context context, boolean on) {
        if (on) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
            wakeLock.acquire();
        } else {
            if (wakeLock != null) {
                wakeLock.release();
                wakeLock = null;
            }
        }
    }


    private Handler screenHandler = new Handler();

    /**
     * 屏幕常亮控制g
     */
    public void screenControl() {
        new Runnable() {
            @Override
            public void run() {
                screenHandler.postDelayed(this, 1000 * 30);
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
                String time = sDateFormat.format(date);
//                Calendar calendar = Calendar.getInstance();
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                String[] split = time.split(":");

                if (Integer.parseInt(split[0]) < 23 && Integer.parseInt(split[0]) >= 7) {//当前是白天时段,设为白天
                    keepScreenOn(NewMainActivity.this, true);
                } else {//设为黑夜
                    keepScreenOn(NewMainActivity.this, false);
                }
            }
        }.run();
    }

    public static OkHttpClient genericClient() {
        Cache cache = new Cache(new File(App.getContext().getCacheDir(), "jsxjxCache"),
                1024 * 1024 * 100);
        //日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                Logger.t("http").e(message);
            }
        });

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)   //缓存
                .retryOnConnectionFailure(false)
                .addInterceptor(logging)
//                .addInterceptor(headerInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }

    public List<List<DesktopIconBean>> calculate(List<DesktopIconBean> list,
                                                 int pageCount) {
        List<List<DesktopIconBean>> myList = new ArrayList<>();
        int size = list.size();
        if (size <= pageCount - row) {//小于=一页
            List<DesktopIconBean> innerList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                innerList.add(list.get(i));
            }
            myList.add(innerList);
        } else {//超过一页
            //第一页list
            List<DesktopIconBean> firstList = new ArrayList<>();
            for (int i = 0; i < pageCount - row; i++) {
                firstList.add(list.get(i));
            }

            //剩下的list
            List<DesktopIconBean> nextList = new ArrayList<>();
            for (int i = pageCount - row; i < size; i++) {
                nextList.add(list.get(i));
            }
            List<List<DesktopIconBean>> lists = splitList(nextList, pageCount);//把剩下的list分页

            lists.add(0, firstList);
            myList.addAll(lists);//
        }
        return myList;
    }

    public <T> List<List<T>> splitList(List<T> list, int pageSize) {

        int listSize = list.size();
        int page = (listSize + (pageSize - 1)) / pageSize;

        List<List<T>> listArray = new ArrayList<List<T>>();
        for (int i = 0; i < page; i++) {
            List<T> subList = new ArrayList<T>();
            for (int j = 0; j < listSize; j++) {
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
                if (pageIndex == (i + 1)) {
                    subList.add(list.get(j));
                }
                if ((j + 1) == ((j + 1) * pageSize)) {
                    break;
                }
            }
            listArray.add(subList);
        }
        return listArray;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppInfoBean event) {
        if (event.isShowDesktop) {//是要删除
            DesktopIconBeanDao desktopIconBeanDao = DaoUtil.getDesktopIconBeanDao();
            List<DesktopIconBean> personList = desktopIconBeanDao.queryBuilder()
                    .where(DesktopIconBeanDao.Properties.Title.eq(event.getAppName()))
                    .build().list();
            if (event.getIconType() == 3) {//是一键拨号
                for (int i = 0; i < personList.size(); i++) {
                    if (personList.get(i).getPackageName().equals(event.getPackageName())) {
                        desktopIconBeanDao.delete(personList.get(i));
                    }
                }
            } else {
                desktopIconBeanDao.delete(personList.get(0));
            }

            //刷新
            int currentItem = viewpager.getCurrentItem();
            mList = DaoUtil.querydata();
            adpterList = null;
            myPagerAdapter = null;
            initView();
            viewpager.setCurrentItem(currentItem);

        } else {//是要添加
            DesktopIconBean desktopIconBean = new DesktopIconBean();
            desktopIconBean.setId(null);
            desktopIconBean.setTitle(event.getAppName());
            desktopIconBean.setIconType(event.getIconType());
            desktopIconBean.setPhoneNum(event.getPhoneNum());
            if (event.getIconType() != 3) {
                desktopIconBean.setApp_icon(event.getAppIcon());
            }
            desktopIconBean.setMid(mList.size());
            desktopIconBean.setIconBgColor(Utils.getColorBgFromPosition(mList.size()));
            desktopIconBean.setPackageName(event.getPackageName());
//            mList.add(desktopIconBean);
            DaoUtil.getDesktopIconBeanDao().insert(desktopIconBean);

            //刷新
            int currentItem = viewpager.getCurrentItem();
            mList = DaoUtil.querydata();
            adpterList = null;
            myPagerAdapter = null;
            initView();
            viewpager.setCurrentItem(currentItem);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        if (event.getEvent().equals(EventBean.REFRESH_DESK)) {
            //刷新
            int currentItem = viewpager.getCurrentItem();
            mList = DaoUtil.querydata();
            adpterList = null;
            myPagerAdapter = null;
            initView();
            viewpager.setCurrentItem(currentItem);
        }
    }

    private Handler timerHandler = new Handler();
    private Handler weatherHandler = new Handler();

    class MyPagerAdapter extends PagerAdapter {
        List<List<DesktopIconBean>> list;
        private RecyclerView recycleView;
        private GridAdapter gridAdapter;
        public View headerItemView;

        public View getHeaderItemView() {
            return headerItemView;
        }

        public void setHeaderItemView(View headerItemView) {
            this.headerItemView = headerItemView;
        }


        public MyPagerAdapter(List<List<DesktopIconBean>> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = View.inflate(NewMainActivity.this, R.layout.item_main_view_pager, null);
            recycleView = view.findViewById(R.id.recycleView);
            final TextView tv_time = view.findViewById(R.id.tv_time);
            final TextView tv_date = view.findViewById(R.id.tv_date);
            LinearLayout ll_header_container = view.findViewById(R.id.ll_header_container);

            if (position == 0) {
                setHeaderItemView(view);
            }

            //剩余高度
            int viewHeight = Utils.getScreenHeight(NewMainActivity.this) - getStatusBarHeight() - ((line + 3) * DensityUtil.dip2px(NewMainActivity.this, 2));
            ViewGroup.LayoutParams lp;
            lp = ll_header_container.getLayoutParams();
            lp.height = viewHeight / line;
            ll_header_container.setLayoutParams(lp);

            //每一秒计时一次更新时间
            new Runnable() {
                @Override
                public void run() {
                    timerHandler.postDelayed(this, 1000);
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat timeFm = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                    SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 EEEE", Locale.CHINA);
                    tv_time.setText(timeFm.format(date));
                    tv_date.setText(dateFm.format(date));
                }
            }.run();


            MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(NewMainActivity.this, row);
            gridLayoutManager.setScrollEnabled(false);
            recycleView.setLayoutManager(gridLayoutManager);
            final List<DesktopIconBean> dataList = list.get(position);
            gridAdapter = adpterList.get(position);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(gridAdapter));
            itemTouchHelper.attachToRecyclerView(recycleView);

            if (position != 0) {
                // 开启拖拽
                gridAdapter.enableDragItem(itemTouchHelper, R.id.rl_item_container, true);
            }

            final int firstMid = dataList.get(0).getMid();
            gridAdapter.setOnItemDragListener(new OnItemDragListener() {
                @Override
                public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                    viewHolder.itemView.findViewById(R.id.delete_iv).setVisibility(View.VISIBLE);
                }

                @Override
                public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                    source.itemView.findViewById(R.id.delete_iv).setVisibility(View.GONE);
                }

                @Override
                public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                    Log.i("myList", mList.toString());

                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.get(i).setMid(firstMid + i);
                        DaoUtil.getDesktopIconBeanDao().update(dataList.get(i));
                    }
                }

            });
            recycleView.setAdapter(gridAdapter);

//            header_view.setHeight(viewHeight / line);


            if (position == 0)

            {//是第一页,显示头布局
                ll_header_container.setVisibility(View.VISIBLE);
            } else

            {
                ll_header_container.setVisibility(View.GONE);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position,
                                @NonNull Object object) {
            ((ViewGroup) container).removeView((View) object);
            object = null;
        }
    }

    private boolean isEdit = false;//是否是编辑状态,编辑状态下显示删除按钮.

    @Override
    public void onBackPressed() {
        //back键监听，如果在编辑模式，则取消编辑模式
        for (int i = 0; i < adpterList.size(); i++) {
            adpterList.get(i).editList.clear();//清除所有选中
            adpterList.get(i).notifyDataSetChanged();
        }
    }

    private ImageView delete_iv;
    private AlertDialog alertDialog;

    class GridAdapter extends BaseItemDraggableAdapter<DesktopIconBean, BaseViewHolder> {
        public List<Long> editList = new ArrayList<>();

        public GridAdapter(@Nullable List<DesktopIconBean> data) {
            super(R.layout.item, data);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void convert(BaseViewHolder helper, final DesktopIconBean item) {
            //计算item宽高
            int screenHeight = Utils.getScreenHeight(NewMainActivity.this);
            int screenWidth = Utils.getScreenWidth(NewMainActivity.this);
            helper.itemView.setLayoutParams(new AbsListView.LayoutParams(screenWidth / 3, (screenHeight - getStatusBarHeight()) / 4));

            //
            ImageView content_iv = helper.getView(R.id.content_iv);
            TextView tv_title = helper.getView(R.id.tv_title);
            delete_iv = helper.getView(R.id.delete_iv);
            RelativeLayout rl_item_container = helper.getView(R.id.rl_item_container);
            LinearLayout ll_item_container = helper.getView(R.id.ll_item_container);

            if (editList.contains(item.getId())) {
                delete_iv.setVisibility(View.VISIBLE);
            } else {
                delete_iv.setVisibility(View.GONE);
            }

            tv_title.setText(item.getTitle());
            int imgUrl = Utils.getAppIconId(item.getImg_id_name());

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(DensityUtil.dip2px(NewMainActivity.this, 5));
            drawable.setColor(item.getIconBgColor());
            rl_item_container.setBackground(drawable);

            if (item.getIconType() == 0 || item.getIconType() == 2) {
                if (item.getTitle().equals("设置") || item.getTitle().equals("相机") ||  item.getTitle().equals("上网")) {
                    content_iv.setImageResource(imgUrl);
                } else {
                    if(item.getTitle().equals("微信")) {
                        if(SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_HAVE_WEIXIN)) {
                            content_iv.setImageResource(imgUrl);
                        }else {
                            byte[] app_icon = item.getApp_icon();
                            Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
                            BitmapDrawable bd = new BitmapDrawable(bmp);
                            content_iv.setImageDrawable(bd);
                        }
                    }else if(item.getTitle().equals("日历")){
                        if(SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_HAVE_WEIXIN)) {
                            byte[] app_icon = item.getApp_icon();
                            Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
                            BitmapDrawable bd = new BitmapDrawable(bmp);
                            content_iv.setImageDrawable(bd);
                        }else {
                            content_iv.setImageResource(imgUrl);
                        }
                    }else {
                        byte[] app_icon = item.getApp_icon();
                        Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
                        BitmapDrawable bd = new BitmapDrawable(bmp);
                        content_iv.setImageDrawable(bd);
                    }
                }
//                if(SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_HAVE_WEIXIN)) {
//                    if(item.getTitle().equals("微信")) {
//                        content_iv.setImageResource(imgUrl);
//                    }
//                    if(item.getTitle().equals("日历")) {
//                        byte[] app_icon = item.getApp_icon();
//                        Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
//                        BitmapDrawable bd = new BitmapDrawable(bmp);
//                        content_iv.setImageDrawable(bd);
//                    }
//                }else {
//                    if(item.getTitle().equals("微信")) {
//                        byte[] app_icon = item.getApp_icon();
//                        Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
//                        BitmapDrawable bd = new BitmapDrawable(bmp);
//                        content_iv.setImageDrawable(bd);
//                    }
//                    if(item.getTitle().equals("日历")) {
//                        content_iv.setImageResource(imgUrl);
//                    }
//                }
            } else if (item.getIconType() == 3) {//一键拨号
                content_iv.setImageResource(R.drawable.one_key);
            } else {
                content_iv.setImageResource(imgUrl);
            }

            //点击事件
            helper.getView(R.id.rl_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getIconType() == 1) {//自定义应用指定跳转
                        switch (item.getTitle()) {
                            case "电话":
                                CallActivity.startActivity(0, mContext);
//                                mContext.startActivity(new Intent(mContext,NewMainActivity.class));
                                break;
                            case "图库":
                                mContext.startActivity(new Intent(mContext, PhotosActivity.class));
                                break;
                            case "紧急呼叫":
                                mContext.startActivity(new Intent(mContext, SosActivity.class));
                                break;
                            case "录音":
                                mContext.startActivity(new Intent(mContext, RecordAudioActivity.class));
                                break;
                            case "黑红名单":
                                mContext.startActivity(new Intent(mContext, BlacklistActivity.class));
                                break;
                            case "联系人":
                                mContext.startActivity(new Intent(mContext, ContactsListActivity.class));
                                break;
                            case "通话记录":
                                CallActivity.startActivity(1, mContext);
                                break;
                            case "所有应用":
                                mContext.startActivity(new Intent(mContext, AllAppsActivity.class));
                                break;
                            case "分机设置":
                                mContext.startActivity(new Intent(mContext, TelephoneSettingActivity.class));
                                break;
                        }
                    } else if (item.getIconType() == 3) {//一键拨号
                        String phoneNum = item.getPhoneNum();
                        CallUtil.call(mContext, phoneNum, false);
                    } else if (item.getIconType() == 4) {//亲情号码
                        if (item.getMid() == 11) {
                            if (item.getTitle().equals("亲情1")) {
                                Intent intent = new Intent(mContext, ContactsListActivity.class);
                                intent.putExtra("desk_id", item.getId());
                                mContext.startActivity(intent);
                            } else {
                                FamilyDetailActivity.startActivity(NewMainActivity.this, item);
                            }
                        } else if (item.getMid() == 12) {
                            if (item.getTitle().equals("亲情2")) {
                                Intent intent = new Intent(mContext, ContactsListActivity.class);
                                intent.putExtra("desk_id", item.getId());
                                mContext.startActivity(intent);
                            } else {
                                FamilyDetailActivity.startActivity(NewMainActivity.this, item);
                            }
                        } else {
                            if (item.getTitle().equals("亲情3")) {
                                Intent intent = new Intent(mContext, ContactsListActivity.class);
                                intent.putExtra("desk_id", item.getId());
                                mContext.startActivity(intent);
                            } else {
                                FamilyDetailActivity.startActivity(NewMainActivity.this, item);
                            }
                        }
                    } else {//系统或用户程序跳转
                        String packageName = item.getPackageName();
                        Utils.startApp(mContext, item.getPackageName());
                    }
                }
            });

            helper.getView(R.id.rl_item_container).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    editList.add(item.getId());//长按保存显示了删除的
                    notifyDataSetChanged();
                    return false;
                }
            });

            delete_iv.setOnClickListener(new View.OnClickListener() {//删除
                @Override
                public void onClick(View view) {
                    if (item.getIconType() == 0 || item.getIconType() == 1 || item.getIconType() == 4) {
                        Toast.makeText(mContext, "该应用为系统应用，不能卸载", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (item.getIconType() == 3) {//是一键拨号
                        alertDialog = new AlertDialog.Builder(mContext)
                                .setMessage("确定要删除" + item.getTitle() + "?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DaoUtil.getDesktopIconBeanDao().delete(item);
                                        int currentItem = viewpager.getCurrentItem();
                                        mList = DaoUtil.querydata();
                                        initView();
                                        viewpager.setCurrentItem(currentItem);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.dismiss();
                                    }
                                }).create();
                        alertDialog.show();
                        Utils.Toast("删除成功");
                    } else {
                        Uri uri = Uri.fromParts("package", item.getPackageName(), null);
                        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                        mContext.startActivity(intent);

                        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
                        filter.addDataScheme("package");
                        //卸载成功
                        //如果删除后少了一屏，则移动到前一屏，并进行页面刷新
                        deletePackageReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                if (intent.getAction() == Intent.ACTION_PACKAGE_REMOVED) {//卸载成功

                                    AppInfoBean appInfoBean = DaoUtil.querydataByWhere(AppInfoBeanDao.Properties.PackageName.eq(item.getPackageName()));
                                    if (appInfoBean != null) {
                                        DaoUtil.getAppInfoBeanDao().delete(appInfoBean);
                                    }

                                    DaoUtil.getDesktopIconBeanDao().delete(item);
                                    int currentItem = viewpager.getCurrentItem();
                                    mList = DaoUtil.querydata();
                                    initView();
                                    viewpager.setCurrentItem(currentItem);
                                    Utils.Toast("卸载成功");
                                }
                                context.unregisterReceiver(deletePackageReceiver);
                                DaoUtil.closeDb();
                            }
                        };
                        mContext.registerReceiver(deletePackageReceiver, filter);
                    }
                }
            });
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DaoUtil.closeDb();
    }

}
