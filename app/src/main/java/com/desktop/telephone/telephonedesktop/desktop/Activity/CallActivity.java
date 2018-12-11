package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.desktop.fragment.CallFragment;
import com.desktop.telephone.telephonedesktop.desktop.fragment.CallRecordsFragment;
import com.desktop.telephone.telephonedesktop.desktop.fragment.ContactsListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallActivity extends BaseActivity {
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<String> tabs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tabs = new ArrayList<>();
        tabs.add("拨号");
        tabs.add("通话记录");
        tabs.add("联系人");

        CallAdapter callAdapter = new CallAdapter(getSupportFragmentManager());
        viewpager.setAdapter(callAdapter);
        tablayout.setupWithViewPager(viewpager);

        int type = getIntent().getIntExtra("type", 0);//0为拨号,1为通话记录,2为联系人
        viewpager.setCurrentItem(type);
    }

    class CallAdapter extends FragmentPagerAdapter {

        public CallAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return CallFragment.newInstance();
            } else if (i == 1) {
                return CallRecordsFragment.newInstance();
            } else {
                return ContactsListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }

    public static void startActivity(int type, Context context) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
