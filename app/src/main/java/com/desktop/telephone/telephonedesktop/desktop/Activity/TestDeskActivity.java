package com.desktop.telephone.telephonedesktop.desktop.Activity;

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
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.MyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestDeskActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int line = 3;//行
    private int row = 4;//列

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_test_desk);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {


        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> list1 = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                if (i == 0 && j >= 9) {
                    continue;
                }
                list1.add("item" + j);
            }
            list.add(list1);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(list);
        viewpager.setAdapter(myPagerAdapter);
    }

    public void calculate(List<DesktopIconBean> list) {
        List<List<DesktopIconBean>> myList = new ArrayList<>();
        List<DesktopIconBean> innerList = new ArrayList<>();
        int size = list.size();
        if (size > (line - 1) * row) {//小于=一页
            for (int i = 0; i < size; i++) {
                innerList.add(list.get(i));
            }
        } else {//超过一页
            int f = size - (line - 1) * row;//去除第一页剩下的
            int newSize = f % (line * row);
            for (int i = 0; i < size; i++) {

            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        List<List<String>> list;
        private RecyclerView recycleView;

        public MyPagerAdapter(List<List<String>> list) {
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
            GridAdapter gridAdapter = new GridAdapter(list.get(position));

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(gridAdapter));
            itemTouchHelper.attachToRecyclerView(recycleView);

            // 开启拖拽
            gridAdapter.enableDragItem(itemTouchHelper, R.id.ll_item_container, true);
            gridAdapter.setOnItemDragListener(onItemDragListener);
            recycleView.setAdapter(gridAdapter);
//            recycleView.setNestedScrollingEnabled(false);
            int height = recycleView.getMeasuredHeight();
            header_view.setHeight((Utils.getScreenHeight(TestDeskActivity.this) - getStatusBarHeight()) / 4);


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
        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
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

    class GridAdapter extends BaseItemDraggableAdapter<String, BaseViewHolder> {
//        boolean isHeader = false;

        public GridAdapter(@Nullable List<String> data) {
            super(R.layout.item_grid, data);
//            this.isHeader = isHeader;
        }


        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int screenHeight = Utils.getScreenHeight(TestDeskActivity.this);
            int screenWidth = Utils.getScreenWidth(TestDeskActivity.this);
            helper.itemView.setLayoutParams(new AbsListView.LayoutParams(screenWidth / 3, (screenHeight - getStatusBarHeight()) / 4));
            helper.setText(R.id.tv_item, item);
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
