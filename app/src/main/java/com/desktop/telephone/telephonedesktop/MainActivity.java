package com.desktop.telephone.telephonedesktop;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.view.ScrollAdapter;
import com.desktop.telephone.telephonedesktop.view.ScrollLayout;

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
    }

    private void getDataFromCache() {
//        mDbUtils = DbUtils.create(this);
//            //使用xUtils，基于orderId从SQLite数据库中获取滑动控件
//            mList = mDbUtils.findAll(Selector.from(MoveItem.class).orderBy("orderId", false));
            mList = DaoUtil.querydata();
    }

    private void initView() {
//        //如果没有缓存数据，则手动添加10条
//        if (mList == null || mList.size() == 0) {
//            mList = new ArrayList<MoveItem>();
//            for (int i = 1; i < 11; i++) {
//                MoveItem item = new MoveItem();
//                //根据drawable name获取对于的ID
//                if(i < 5) {
//                    item.setImgdown(R.mipmap.item1_down);
//                    item.setImgurl(R.mipmap.item1_normal);
//                }else {
//                    item.setImgdown(R.mipmap.item2_dowm);
//                    item.setImgurl(R.mipmap.item2_normal);
//                }
//                mList.add(item);
//            }
//        }
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
//
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
