package com.desktop.telephone.telephonedesktop;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.desktop.telephone.telephonedesktop.base.App;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.ScrollAdapter;
import com.desktop.telephone.telephonedesktop.view.ScrollLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {
    Unbinder unbinder;
    @BindView(R.id.container)
    ScrollLayout mContainer;  // 滑动控件的容器Container


    // Container的Adapter
    private ScrollAdapter mItemsAdapter;
    // Container中滑动控件列表
    private List<DesktopIconBean> mList;

    //    //xUtils中操纵SQLite的助手类
//    private DbUtils mDbUtils;
    private List<DesktopIconBean> defaultList;
    //    @BindView(R.id.viewpager)
//    ViewPager viewpager;
//    @BindView(R.id.point_indicatorView)
//    PointIndicatorView pointIndicatorView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initIconData();
        // 初始化控件
        initView();
        //初始化容器Adapter
//        loadBackground();
        EventBus.getDefault().register(this);
        CallUtil.showCallerIds(this,1);
    }


//    private void getDataFromCache() {
////        mDbUtils = DbUtils.create(this);
////            //使用xUtils，基于orderId从SQLite数据库中获取滑动控件
////            mList = mDbUtils.findAll(Selector.from(MoveItem.class).orderBy("orderId", false));
////        mList = DaoUtil.querydata();
//    }

    /**
     * 桌面数据添加
     */
    private void initIconData() {
        defaultList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
//        for (int i = 0; i < 8; i++) {
            DesktopIconBean moveItem = new DesktopIconBean();
            moveItem.setMid(i);
            switch (i) {
                case 0:
                    //电话
                    moveItem.setIconType(1);
                    moveItem.setTitle("电话");
                    moveItem.setImg_id_name("phone_icon");
                    break;
                case 1:
                    //智能通讯录
                    moveItem.setIconType(1);
                    moveItem.setTitle("智能通讯录");
                    moveItem.setImg_id_name("call_records_icon");
                    break;
                case 2:
                    //电子相册
                    moveItem.setIconType(1);
                    moveItem.setTitle("电子相册");
                    moveItem.setImg_id_name("photo_icon");
                    break;
                case 3:
                    //黑白名单
                    moveItem.setIconType(1);
                    moveItem.setTitle("黑白名单");
                    moveItem.setImg_id_name("blacklist_icon");
                    break;
//                case 4:
//                    //一键拨号
//                    moveItem.setIconType(1);
//                    moveItem.setTitle("一键拨号");
//                    moveItem.setImg_id_name("one_key");
//                    break;
                case 4:
                    //录音
                    moveItem.setIconType(1);
                    moveItem.setTitle("录音");
                    moveItem.setImg_id_name("record_icon");
                    break;
                case 5:
                    //通话记录
                    moveItem.setIconType(1);
                    moveItem.setTitle("通话记录");
                    moveItem.setImg_id_name("address_list_icon");
                    break;
                case 6:
                    //sos
                    moveItem.setIconType(1);
                    moveItem.setTitle("SOS");
                    moveItem.setImg_id_name("sos_icon");
                    break;
                case 7:
                    //所有应用
                    moveItem.setIconType(1);
                    moveItem.setTitle("所有应用");
                    moveItem.setImg_id_name("all_apps_icon");
                    break;
                case 8://设置
                    PackageManager pm = getPackageManager();
                    //所有的安装在系统上的应用程序包信息。
                    List<PackageInfo> packInfos = pm.getInstalledPackages(0);
                    for (int j = 0; j < packInfos.size(); j++) {
                        PackageInfo packInfo = packInfos.get(j);
                        if(packInfo.packageName.equals("com.android.settings")) {
                            moveItem.setIconType(2);
                            moveItem.setTitle("设置");
                            moveItem.setPackageName(packInfo.packageName);
                            moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm)));
                        }
                    }
                    break;
                case 9://相机 记得改上面size
                    PackageManager pm1 = getPackageManager();
                    //所有的安装在系统上的应用程序包信息。
                    List<PackageInfo> packInfos1 = pm1.getInstalledPackages(0);
                    for (int j = 0; j < packInfos1.size(); j++) {
                        PackageInfo packInfo = packInfos1.get(j);
                        if(packInfo.packageName.equals("com.android.camera2")) {
//                        if(packInfo.packageName.equals("com.android.camera")) {
                            moveItem.setIconType(2);
                            moveItem.setTitle("相机");
                            moveItem.setPackageName(packInfo.packageName);
                            moveItem.setApp_icon(DaoUtil.drawableToByte(packInfo.applicationInfo.loadIcon(pm1)));
                        }
                    }
                    break;
            }

            moveItem.setIconBgColor(Utils.getColorBgFromPosition(i));

            defaultList.add(moveItem);
        }

//        List<DesktopIconBean> list = DaoUtil.getDesktopIconBeanDao().loadAll();
        mList = DaoUtil.querydata();
        if (mList == null || mList.size() == 0) {//数据库中没有列表(第一次安装)
            mList.addAll(defaultList);
            DaoUtil.saveNLists(defaultList);//保存默认的list
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppInfoBean event) {
        if (event.isShowDesktop) {//是要删除
            DesktopIconBeanDao desktopIconBeanDao = DaoUtil.getDesktopIconBeanDao();
            List<DesktopIconBean> personList = desktopIconBeanDao.queryBuilder()
                    .where(DesktopIconBeanDao.Properties.Title.eq(event.getAppName()))
                    .build().list();
            desktopIconBeanDao.delete(personList.get(0));
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getTitle().equals(event.appName)) {
                    mList.remove(i);
                }
            }
            mContainer.refreView();
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
            mList.add(desktopIconBean);
            DaoUtil.getDesktopIconBeanDao().insert(desktopIconBean);
            mContainer.refreView();
        }
    }

    private void initView() {
        //初始化Container的Adapter
        mItemsAdapter = new ScrollAdapter(this, mList);
        //设置Container添加删除Item的回调
        mContainer.setOnAddPage(new ScrollLayout.OnAddOrDeletePage() {
            @Override
            public void onAddOrDeletePage(int page, boolean isAdd) {

            }
        });
        //设置Container页面换转的回调，比如自第一页滑动第二页
        mContainer.setOnPageChangedListener(new ScrollLayout.OnPageChangedListener() {
            @Override
            public void onPage2Other(int n1, int n2) {

            }
        });
        //设置Container编辑模式的回调，长按进入修改模式
        mContainer.setOnEditModeListener(new ScrollLayout.OnEditModeListener() {
            @Override
            public void onEdit() {

            }
        });
        //设置Adapter
        mContainer.setSaAdapter(mItemsAdapter);
        //动态设置Container每页的列数为2行
        mContainer.setColCount(3);
        //动态设置Container每页的行数为4行
        mContainer.setRowCount(4);
        //调用refreView绘制所有的Item
        mContainer.refreView();
    }

    // 设置Container滑动背景图片
    private void loadBackground() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mContainer.setBackGroud(BitmapFactory.decodeResource(getResources(),
                R.drawable.bg2, options));
    }

//    private int getDrawableId(String name) {
//        return getResources().getIdentifier(name, "drawable", "com.jit.demo");
//    }

    @Override
    public void onBackPressed() {
        //back键监听，如果在编辑模式，则取消编辑模式
        if (mContainer.isEditting()) {
            mContainer.showEdit(false);
            return;
        }
//        else {
//            try {
//                //退出APP前，保存当前的Items，记得所有item的位置
//                List<MoveItem> list = mContainer.getAllMoveItems();
//                mDbUtils.saveAll(list);
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//            super.onBackPressed();
//            Process.killProcess(Process.myPid());
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DaoUtil.closeDb();
    }

    //    private void initView() {
//        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
//        viewpager.setAdapter(myAdapter);
//        pointIndicatorView.setRelevanceViewPager(viewpager);
//        pointIndicatorView.setIndicatorsSize(3);
//    }
//
//    class MyAdapter extends FragmentPagerAdapter {
//
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            return new DesktopIconFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }
}
