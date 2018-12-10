package com.desktop.telephone.telephonedesktop.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.desktop.fragment.DesktopIconFragment;
import com.desktop.telephone.telephonedesktop.desktop.fragment.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMainActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        viewpager.setAdapter(myAdapter);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return new MainFragment();
            } else {
                return new DesktopIconFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
