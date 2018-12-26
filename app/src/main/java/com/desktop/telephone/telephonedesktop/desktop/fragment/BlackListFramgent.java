package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.EventBlacklistInfoBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.BlacklistAddActivity;
import com.desktop.telephone.telephonedesktop.desktop.dialog.BlacklistDeleteDialog;
import com.desktop.telephone.telephonedesktop.gen.BlackListInfoBeanDao;
import com.desktop.telephone.telephonedesktop.util.BlackListFileUtil;
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

public class BlackListFramgent extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    private int type = 1;//1为红名单,2为黑名单
    private List<BlackListInfoBean> list;
    private List<BlackListInfoBean> blackList;
    private List<BlackListInfoBean> whiteList;
    private MyAdapter blackListAdapter;
    private MyAdapter whiteListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.framgent_blacklist, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public static BlackListFramgent newInstance(int type) {
        Bundle argz = new Bundle();
        argz.putInt("type", type);
        BlackListFramgent fragment = new BlackListFramgent();
        fragment.setArguments(argz);
        return fragment;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type");
        }
        blackList = new ArrayList<>();
        whiteList = new ArrayList<>();
        initData();
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (type == 1) {
            blackListAdapter = new MyAdapter(blackList);
            View emptyView = View.inflate(getActivity(), R.layout.empty_view, null);
            blackListAdapter.setEmptyView(emptyView);
            recycleView.setAdapter(blackListAdapter);
            blackListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                    showDeleteDialog(position);
                    return false;
                }
            });
        } else {
            whiteListAdapter = new MyAdapter(whiteList);
            View emptyView = View.inflate(getActivity(), R.layout.empty_view, null);
            whiteListAdapter.setEmptyView(emptyView);
            recycleView.setAdapter(whiteListAdapter);
            whiteListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                    showDeleteDialog(position);
                    return false;
                }
            });
        }
    }

    private void initData() {
        list = DaoUtil.getBlackListInfoBeanDao().queryBuilder().orderDesc(BlackListInfoBeanDao.Properties.Id).list();
        if (list == null) {
            list = new ArrayList<>();
        } else {
            for (BlackListInfoBean blackListInfoBean : list) {
                if (blackListInfoBean.getType() == 1) {
                    blackList.add(blackListInfoBean);
                } else {
                    whiteList.add(blackListInfoBean);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBlacklistInfoBean event) {//添加成功
        if (event != null) {
            BlackListInfoBean addbean = event.getAddbean();
            BlackListInfoBean deletebean = event.getDeletebean();
            if (type == 1) {//当前是黑名单fragment
                if (event.isNeedDelete()) {
                    if (deletebean.getType() == 1) {//删除黑名单,添加到红名单
                        Iterator<BlackListInfoBean> it = blackList.iterator();
                        while (it.hasNext()) {
                            if (it.next().getPhone().equals(deletebean.getPhone())) {
                                it.remove();
                            }
                        }
                    } else {
                        blackList.add(0, addbean);
                        blackListAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (addbean.getType() == 1) {//是黑名单添加
                        blackList.add(0, addbean);
                        blackListAdapter.notifyDataSetChanged();
                    }
                }
                blackListAdapter.notifyDataSetChanged();
            } else {//当前是红名单framgment
                if (event.isNeedDelete()) {
                    if (deletebean.getType() == 1) {
                        whiteList.add(0, addbean);
                    } else {
                        Iterator<BlackListInfoBean> it = whiteList.iterator();
                        while (it.hasNext()) {
                            if (it.next().getPhone().equals(deletebean.getPhone())) {
                                it.remove();
                            }
                        }
                    }
                } else {
                    if (addbean.getType() == 2) {//是红名单添加
                        whiteList.add(0, addbean);
                    }
                }
                whiteListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {//添加成功
        if (event.getEvent().equals(EventBean.BLACK_LIST_ALL_NOTIFAL)) {
            if(type == 1) {
                blackList.clear();
                initData();
                blackListAdapter.notifyDataSetChanged();
            }else {
                whiteList.clear();
                initData();
                whiteListAdapter.notifyDataSetChanged();
            }
        }
    }

//            if (event.isNeedDelete()) {
//                if (addbean.getType() == 1) {//删除黑名单,添加到红名单
//                    blackList.remove(deletebean);
//                } else {
//                    blackList.add(addbean);
//                }
//
//            } else {
//                whiteList.add(addbean);
//                whiteList.remove(deletebean);
//                whiteListAdapter.notifyDataSetChanged();
//            }
//        }
//        }
//    }

    //长按删除dialog
    private void showDeleteDialog(final int position) {
        BlacklistDeleteDialog blacklistDeleteDialog = new BlacklistDeleteDialog(getActivity());
        blackList.clear();
        whiteList.clear();
        initData();
//        if (queryList != null && queryList.size() != 0) {
        final BlackListInfoBean blackListInfoBean;
        if (type == 1) {
            blackListInfoBean = blackList.get(position);
        } else {
            blackListInfoBean = whiteList.get(position);
        }
        blacklistDeleteDialog.setData(blackListInfoBean);
        blacklistDeleteDialog.setBtnClickListener(new BlacklistDeleteDialog.BtnClickListener() {
            @Override
            public void callClick() {//拨号
                CallUtil.call(getActivity(),blackListInfoBean.getPhone(),false);
            }

            @Override
            public void addToListClick() {//加入黑/红名单
                if(type == 1) {//加入到红名单
                    DaoUtil.getBlackListInfoBeanDao().delete(blackListInfoBean);

                    BlackListInfoBean addBlackListInfoBean = new BlackListInfoBean(null, blackListInfoBean.getPhone(), 2, Utils.getFormatDate());
                    DaoUtil.getBlackListInfoBeanDao().insert(addBlackListInfoBean);

                    EventBlacklistInfoBean eventBlacklistInfoBean = new EventBlacklistInfoBean();
                    eventBlacklistInfoBean.setNeedDelete(true);
                    eventBlacklistInfoBean.setDeletebean(blackListInfoBean);
                    eventBlacklistInfoBean.setAddbean(addBlackListInfoBean);
                    EventBus.getDefault().post(eventBlacklistInfoBean);
                    Utils.Toast("操作成功");
                    BlackListFileUtil.updateFile();//更新文件
                    return;
                }else {
                    DaoUtil.getBlackListInfoBeanDao().delete(blackListInfoBean);

                    BlackListInfoBean addBlackListInfoBean = new BlackListInfoBean(null, blackListInfoBean.getPhone(), 1, Utils.getFormatDate());
                    DaoUtil.getBlackListInfoBeanDao().insert(addBlackListInfoBean);

                    EventBlacklistInfoBean eventBlacklistInfoBean = new EventBlacklistInfoBean();
                    eventBlacklistInfoBean.setNeedDelete(true);
                    eventBlacklistInfoBean.setDeletebean(blackListInfoBean);
                    eventBlacklistInfoBean.setAddbean(addBlackListInfoBean);
                    EventBus.getDefault().post(eventBlacklistInfoBean);
                    Utils.Toast("操作成功");
                }



//                if (type == 1) {
//                    blackList.remove(blackListInfoBean);
//                    blackListInfoBean.setType(2);
//                    DaoUtil.getBlackListInfoBeanDao().update(blackListInfoBean);
//                    blackListAdapter.notifyDataSetChanged();
//                } else {
//                    whiteList.remove(blackListInfoBean);
//                    blackListInfoBean.setType(1);
//                    DaoUtil.getBlackListInfoBeanDao().update(blackListInfoBean);
//                    whiteListAdapter.notifyDataSetChanged();
//                }
//                EventBus.getDefault().post(new EventBean(EventBean.BLACK_LIST_ALL_NOTIFAL));
//                Utils.Toast("操作成功");
                BlackListFileUtil.updateFile();//更新文件
            }

            @Override
            public void deleteClick() {//删除
                DaoUtil.getBlackListInfoBeanDao().delete(blackListInfoBean);
                if (type == 1) {
                    blackList.remove(position);
                    blackListAdapter.notifyDataSetChanged();
                } else {
                    whiteList.remove(position);
                    whiteListAdapter.notifyDataSetChanged();
                }
                Utils.Toast("删除成功");
                BlackListFileUtil.updateFile();//更新文件
            }

            @Override
            public void updateClick() {
                BlacklistAddActivity.startActivity(getActivity(),type,blackListInfoBean.getPhone());
            }
        });
        blacklistDeleteDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DaoUtil.closeDb();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    class MyAdapter extends BaseQuickAdapter<BlackListInfoBean, BaseViewHolder> {

        public MyAdapter(@Nullable List<BlackListInfoBean> data) {
            super(R.layout.item_blacklist, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BlackListInfoBean item) {
            helper.setText(R.id.tv_num, item.getPhone());
            helper.setText(R.id.tv_date, item.getDate());
            if (item.getType() == 1) {
                helper.setText(R.id.tv_desc, "黑名单号码");
                helper.setTextColor(R.id.tv_desc,Utils.getColor(R.color.text_666666));
                helper.setTextColor(R.id.tv_num,Utils.getColor(R.color.text_333333));
            } else {
                helper.setText(R.id.tv_desc, "红名单号码");
                helper.setTextColor(R.id.tv_desc,Utils.getColor(R.color.color_7));
                helper.setTextColor(R.id.tv_num,Utils.getColor(R.color.color_7));
            }
        }
    }
}
