package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.media.AudioRecord;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.desktop.fragment.AudioRecordFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 录音文件列表
 */
public class AudioRecordListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        list.add("普通录音");
        list.add("通话录音");
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        viewpager.setAdapter(myAdapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return AudioRecordFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
    }
}
