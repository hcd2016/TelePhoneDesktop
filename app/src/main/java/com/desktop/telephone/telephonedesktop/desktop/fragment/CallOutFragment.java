package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.EvenCallRecordBean;
import com.desktop.telephone.telephonedesktop.gen.BlackListInfoBeanDao;
import com.desktop.telephone.telephonedesktop.gen.CallRecordBeanDao;
import com.desktop.telephone.telephonedesktop.gen.ContactsBeanDao;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 已拨电话
 */
public class CallOutFragment extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    private List<CallRecordBean> list;
    private MyAdapter myAdapter;
    private AlertDialog alertDialog;

    public static CallOutFragment newInstance() {
        CallOutFragment fragment = new CallOutFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_all_calls, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        list = DaoUtil.getCallRecordBeanDao().queryBuilder().where(CallRecordBeanDao.Properties.CallStatus.eq(0)).orderDesc(CallRecordBeanDao.Properties.Id).list();
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new MyAdapter(list);
        View view = View.inflate(getActivity(), R.layout.empty_view, null);
        myAdapter.setEmptyView(view);
        recycleView.setAdapter(myAdapter);
    }

    //添加了记录刷新列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenCallRecordBean event) {
        CallRecordBean callRecordBean = event.getCallRecordBean();
        if (callRecordBean.getCallStatus() == 0) {
            if (event.isAdd) {
                list.add(0, callRecordBean);
            } else {
                Iterator<CallRecordBean> iterator = list.iterator();
                while (iterator.hasNext()) {
                    CallRecordBean next = iterator.next();
                    if (next.getId() == callRecordBean.getId()) {
                        iterator.remove();
                    }
                }
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    class MyAdapter extends BaseQuickAdapter<CallRecordBean, BaseViewHolder> {


        public MyAdapter(@Nullable List<CallRecordBean> data) {
            super(R.layout.item_all_calls, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final CallRecordBean item) {
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
            if (TextUtils.isEmpty(item.getName())) {
                helper.setText(R.id.tv_phone_num, item.getPhoneNum());
            } else {
                helper.setText(R.id.tv_phone_num, item.getName());
            }
            helper.setText(R.id.tv_date, item.getDate());
            helper.setText(R.id.tv_status_desc, item.getStatusDesc());
            helper.getView(R.id.ll_item_container).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {//长按删除
                    alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("删除记录")
                            .setMessage("确定要删除这条记录吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DaoUtil.getCallRecordBeanDao().delete(item);
                                    EvenCallRecordBean evenCallRecordBean = new EvenCallRecordBean(item, false);
                                    EventBus.getDefault().post(evenCallRecordBean);
                                    Utils.Toast("删除成功");
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .create();
                    alertDialog.show();
                    return false;
                }
            });
            helper.getView(R.id.ll_item_container).setOnClickListener(new View.OnClickListener() {//点击事件
                @Override
                public void onClick(View view) {
                    CallUtil.call(getActivity(),item.getPhoneNum(),false);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DaoUtil.closeDb();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
