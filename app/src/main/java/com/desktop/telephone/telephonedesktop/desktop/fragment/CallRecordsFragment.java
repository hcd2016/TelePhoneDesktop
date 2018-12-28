package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.CallRecordBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.ViewPagerSlide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 通话记录
 */
public class CallRecordsFragment extends Fragment {
    @BindView(R.id.viewpager)
    ViewPagerSlide viewpager;
    Unbinder unbinder;
    @BindView(R.id.tv_btn_all_calls)
    TextView tvBtnAllCalls;
    @BindView(R.id.tv_btn_dialed_calls)
    TextView tvBtnDialedCalls;
    @BindView(R.id.tv_btn_received_calls)
    TextView tvBtnReceivedCalls;
    @BindView(R.id.tv_btn_missed_calls)
    TextView tvBtnMissedCalls;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_call_records, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    boolean isAdd = false;

    private void initView() {
//        if (!isAdd) {
//            for (int i = 0; i < 20; i++) {
//                CallRecordBean callRecordBean = new CallRecordBean(null, 0, "13838389" + i, Utils.getFormatDate(), "未接通");
//                CallRecordBean callRecordBean1 = new CallRecordBean(null, 1, "13838389" + i, Utils.getFormatDate(), "5" + i + "秒");
//                CallRecordBean callRecordBean2 = new CallRecordBean(null, 2, "13838389" + i, Utils.getFormatDate(), "响铃5" + i + "秒");
//                CallRecordBean callRecordBean3 = new CallRecordBean(null, 3, "13838389" + i, Utils.getFormatDate(), "拒接");
//                DaoUtil.getCallRecordBeanDao().insert(callRecordBean);
//                DaoUtil.getCallRecordBeanDao().insert(callRecordBean1);
//                DaoUtil.getCallRecordBeanDao().insert(callRecordBean2);
//                DaoUtil.getCallRecordBeanDao().insert(callRecordBean3);
//            }
//            isAdd = true;
//        }


        tvBtnAllCalls.performClick();
        CallRecordAdapter callRecordAdapter = new CallRecordAdapter(getChildFragmentManager());
        viewpager.setAdapter(callRecordAdapter);
//        viewpager.setSlide(false);//禁用viewpager滑动
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                resetStaus();
                switch (i) {
                    case 0:
                        tvBtnAllCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                        tvBtnAllCalls.setBackgroundResource(R.drawable.shape_radius30_left);
                        break;
                    case 1:
                        tvBtnDialedCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                        tvBtnDialedCalls.setBackgroundResource(R.color.color_tonghua1);
                        break;
                    case 2:
                        tvBtnReceivedCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                        tvBtnReceivedCalls.setBackgroundResource(R.color.color_tonghua1);
                        break;
                    case 3:
                        tvBtnMissedCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                        tvBtnMissedCalls.setBackgroundResource(R.drawable.shape_radius30_right);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public static CallRecordsFragment newInstance() {
        CallRecordsFragment fragment = new CallRecordsFragment();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_btn_all_calls, R.id.tv_btn_dialed_calls, R.id.tv_btn_received_calls, R.id.tv_btn_missed_calls})
    public void onViewClicked(View view) {
        resetStaus();
        switch (view.getId()) {
            case R.id.tv_btn_all_calls://全部通话
                tvBtnAllCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                tvBtnAllCalls.setBackgroundResource(R.drawable.shape_radius30_left);
                viewpager.setCurrentItem(0);
                break;
            case R.id.tv_btn_dialed_calls://已拨电话
                tvBtnDialedCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                tvBtnDialedCalls.setBackgroundResource(R.color.color_tonghua1);
                viewpager.setCurrentItem(1);
                break;
            case R.id.tv_btn_received_calls://已接电话
                tvBtnReceivedCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                tvBtnReceivedCalls.setBackgroundResource(R.color.color_tonghua1);
                viewpager.setCurrentItem(2);
                break;
            case R.id.tv_btn_missed_calls://未接电话
                tvBtnMissedCalls.setTextColor(Utils.getColor(R.color.colorPrimary));
                tvBtnMissedCalls.setBackgroundResource(R.drawable.shape_radius30_right);
                viewpager.setCurrentItem(3);
                break;
        }
    }

    public void resetStaus() {
        tvBtnAllCalls.setTextColor(Utils.getColor(R.color.text_333333));
        tvBtnDialedCalls.setTextColor(Utils.getColor(R.color.text_333333));
        tvBtnMissedCalls.setTextColor(Utils.getColor(R.color.text_333333));
        tvBtnReceivedCalls.setTextColor(Utils.getColor(R.color.text_333333));

        tvBtnAllCalls.setBackgroundResource(R.drawable.shape_half_left_kuang);
        tvBtnDialedCalls.setBackgroundResource(R.drawable.shape_kuang);
        tvBtnReceivedCalls.setBackgroundResource(R.drawable.shape_kuang);
        tvBtnMissedCalls.setBackgroundResource(R.drawable.shape_half_right_kuang);
    }

    class CallRecordAdapter extends FragmentPagerAdapter {

        public CallRecordAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return AllCallsFragment.newInstance();
            } else if (i == 1) {
                return CallOutFragment.newInstance();
            } else if (i == 2) {
                return CallInFragment.newInstance();
            } else {
                return MissedCallFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
