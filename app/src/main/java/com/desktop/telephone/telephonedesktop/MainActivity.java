package com.desktop.telephone.telephonedesktop;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.view.ScrollAdapter;
import com.desktop.telephone.telephonedesktop.view.ScrollLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.DbUtils;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.ByteArrayOutputStream;
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
    private List<DesktopIconBean> myList;
    //    @BindView(R.id.viewpager)
//    ViewPager viewpager;
//    @BindView(R.id.point_indicatorView)
//    PointIndicatorView pointIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 从缓存中初始化滑动控件列表
        getDataFromCache();
        // 初始化控件
        initView();
        //初始化容器Adapter
        loadBackground();
        EventBus.getDefault().register(this);
    }

    private void getDataFromCache() {
//        mDbUtils = DbUtils.create(this);
//            //使用xUtils，基于orderId从SQLite数据库中获取滑动控件
//            mList = mDbUtils.findAll(Selector.from(MoveItem.class).orderBy("orderId", false));
        mList = DaoUtil.querydata();
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
            desktopIconBean.setTitle(event.getAppName());
            desktopIconBean.setIconType(event.getIconType());
            desktopIconBean.setApp_icon(event.getAppIcon());
            desktopIconBean.setMid(mList.size());
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
        mContainer.setRowCount(3);
        //调用refreView绘制所有的Item
        mContainer.refreView();
    }

    // 设置Container滑动背景图片
    private void loadBackground() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mContainer.setBackGroud(BitmapFactory.decodeResource(getResources(),
                R.mipmap.bg, options));
    }

    private int getDrawableId(String name) {
        return getResources().getIdentifier(name, "drawable", "com.jit.demo");
    }

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
