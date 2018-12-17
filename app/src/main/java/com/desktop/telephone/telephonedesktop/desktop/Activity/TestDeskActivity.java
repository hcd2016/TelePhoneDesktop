package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.DensityUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.MyGridLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestDeskActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int line = 4;//行
    private int row = 3;//列
    private int pageCount = line * row;//分页数量
    private ArrayList<DesktopIconBean> defaultList;
    private List<DesktopIconBean> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentStatus();
        setContentView(R.layout.activity_record_test_desk);
        ButterKnife.bind(this);
        initIconData();
        initView();
//        EventBus.getDefault().register(this);
        CallUtil.showCallerIds(this, 1);
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
            window.setStatusBarColor(Color.BLACK);
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    /**
     * 桌面数据添加
     */
    private void initIconData() {
        defaultList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
                        if (packInfo.packageName.equals("com.android.settings")) {
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
                        if (packInfo.packageName.equals("com.android.camera2")) {
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
        mList = DaoUtil.querydata();
        if (mList == null || mList.size() == 0) {//数据库中没有列表(第一次安装)
            mList.addAll(defaultList);
            DaoUtil.saveNLists(defaultList);//保存默认的list
        }
    }

    private void initView() {
        List<List<DesktopIconBean>> lists = calculate(mList, pageCount);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(lists);
        viewpager.setAdapter(myPagerAdapter);
    }

    public List<List<DesktopIconBean>> calculate(List<DesktopIconBean> list, int pageCount) {
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

    private GridAdapter gridAdapter;
    class MyPagerAdapter extends PagerAdapter {
        List<List<DesktopIconBean>> list;
        private RecyclerView recycleView;

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
            View view = View.inflate(TestDeskActivity.this, R.layout.item_main_view_pager, null);
            recycleView = view.findViewById(R.id.recycleView);
            TextView header_view = view.findViewById(R.id.header_view);
            MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(TestDeskActivity.this, 3);
//            gridLayoutManager.setScrollEnabled(false);


            recycleView.setLayoutManager(gridLayoutManager);
            gridAdapter = new GridAdapter(list.get(position));

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(gridAdapter));
            itemTouchHelper.attachToRecyclerView(recycleView);

            // 开启拖拽
            gridAdapter.enableDragItem(itemTouchHelper, R.id.rl_item_container, true);
            gridAdapter.setOnItemDragListener(onItemDragListener);
            recycleView.setAdapter(gridAdapter);
            //剩余高度
            int viewHeight = Utils.getScreenHeight(TestDeskActivity.this) - getStatusBarHeight() - ((line + 1) * DensityUtil.dip2px(TestDeskActivity.this, 10));
            header_view.setHeight(viewHeight / 4);


            if (position == 0) {//是第一页,显示头布局
                header_view.setVisibility(View.VISIBLE);
            } else {
                header_view.setVisibility(View.GONE);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewGroup) container).removeView((View) object);
            object = null;
        }
    }

    OnItemDragListener onItemDragListener = new OnItemDragListener() {
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            Utils.Toast("长按了");
            delete_iv.setVisibility(View.VISIBLE);
            gridAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            Utils.Toast("位置换了");
        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
        }
    };

//    OnItemDragListener onItemDragListener = new OnItemDragListener() {
//        @Override
//        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
//            ToastUtils.showShortToast("你在拖拽第" + (pos + 1) + "个位置的item哦！");
//        }
//
//        @Override
//        public void onItemDragMoving(RecyclerView.ViewHolder source, int from,
//                                     RecyclerView.ViewHolder target, int to) {
//
//        }
//
//        @Override
//        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
//            ToastUtils.showShortToast("拖拽到了第" + (pos + 1) + "个位置哦！");
//        }
//    };

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private ImageView delete_iv;
    class GridAdapter extends BaseItemDraggableAdapter<DesktopIconBean, BaseViewHolder> {



        public GridAdapter(@Nullable List<DesktopIconBean> data) {
            super(R.layout.item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final DesktopIconBean item) {
            //计算item宽高
            int screenHeight = Utils.getScreenHeight(TestDeskActivity.this);
            int screenWidth = Utils.getScreenWidth(TestDeskActivity.this);
            helper.itemView.setLayoutParams(new AbsListView.LayoutParams(screenWidth / 3, (screenHeight - getStatusBarHeight()) / 4));

            //
            ImageView content_iv = helper.getView(R.id.content_iv);
            TextView tv_title = helper.getView(R.id.tv_title);
            delete_iv = helper.getView(R.id.delete_iv);
            RelativeLayout rl_item_container = helper.getView(R.id.rl_item_container);
            LinearLayout ll_item_container = helper.getView(R.id.ll_item_container);

            tv_title.setText(item.getTitle());
            int imgUrl = Utils.getAppIconId(item.getImg_id_name());
            rl_item_container.setBackgroundColor(item.getIconBgColor());

            if (item.getIconType() == 0 || item.getIconType() == 2) {
                byte[] app_icon = item.getApp_icon();
                Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
                BitmapDrawable bd = new BitmapDrawable(bmp);
                content_iv.setImageDrawable(bd);
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
                                CallActivity.startActivity(0,mContext);
//                                mContext.startActivity(new Intent(mContext,TestDeskActivity.class));
                                break;
                            case "电子相册":
                                mContext.startActivity(new Intent(mContext, PhotosActivity.class));
                                break;
                            case "SOS":
                                mContext.startActivity(new Intent(mContext, SosActivity.class));
                                break;
                            case "录音":
                                mContext.startActivity(new Intent(mContext, RecordAudioActivity.class));
                                break;
                            case "黑白名单":
                                mContext.startActivity(new Intent(mContext, BlacklistActivity.class));
                                break;
                            case "智能通讯录":
                                mContext.startActivity(new Intent(mContext, ContactsListActivity.class));
                                break;
                            case "通话记录":
                                CallActivity.startActivity(1,mContext);
                                break;
                            case "所有应用":
                                mContext.startActivity(new Intent(mContext, AllAppsActivity.class));
                                break;
                        }
                    } else if (item.getIconType() == 3) {//一键拨号
                        String phoneNum = item.getPhoneNum();
                        CallUtil.call(mContext,phoneNum);
                    } else {//系统或用户程序跳转
                        Utils.startApp(mContext, item.getPackageName());
                    }
                }
            });

            helper.getView(R.id.rl_item_container).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Utils.Toast("长按了");
                    return false;
                }
            });
        }
    }


//    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
//        @Override
//        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {}
//        @Override
//        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {}
//        @Override
//        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {}
//    };
//
//    ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
//    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
//itemTouchHelper.attachToRecyclerView(mRecyclerView);
//
//// 开启拖拽
//mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
//mAdapter.setOnItemDragListener(onItemDragListener);
//
//// 开启滑动删除
//mAdapter.enableSwipeItem();
//mAdapter.setOnItemSwipeListener(onItemSwipeListener);
}
