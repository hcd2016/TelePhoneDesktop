package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.CallRecordBean;
import com.desktop.telephone.telephonedesktop.gen.BlackListInfoBeanDao;
import com.desktop.telephone.telephonedesktop.gen.CallRecordBeanDao;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 已接电话
 */
public class CallInFragment extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    private List<CallRecordBean> list;

    public static CallInFragment newInstance() {
        CallInFragment fragment = new CallInFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_all_calls, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        list = DaoUtil.getCallRecordBeanDao().queryBuilder().where(CallRecordBeanDao.Properties.CallStatus.eq(1)).orderDesc(CallRecordBeanDao.Properties.Id).list();
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyAdapter myAdapter = new MyAdapter(list);
        recycleView.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseQuickAdapter<CallRecordBean, BaseViewHolder> {


        public MyAdapter(@Nullable List<CallRecordBean> data) {
            super(R.layout.item_all_calls, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CallRecordBean item) {
            int callStatus = item.getCallStatus();
            switch (callStatus) {
                case 0://已拨
                    helper.setImageResource(R.id.iv_status_icon, R.drawable.call_out);
                    break;
                case 1://已接
                    helper.setImageResource(R.id.iv_status_icon, R.drawable.call_in);
                    break;
                case 2://未接
                    helper.setImageResource(R.id.iv_status_icon, R.drawable.missed_call);
                    break;
                case 3://挂断
                    helper.setImageResource(R.id.iv_status_icon, R.drawable.hand_up);
                    break;
            }
            //联系人中有就显示姓名
            if(TextUtils.isEmpty(item.getName())) {
                helper.setText(R.id.tv_phone_num, item.getPhoneNum());
            }else {
                helper.setText(R.id.tv_phone_num,item.getName());
            }
            helper.setText(R.id.tv_date, item.getDate());
            helper.setText(R.id.tv_status_desc, item.getStatusDesc());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
