package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.CallNumBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.ContactsListActivity;
import com.desktop.telephone.telephonedesktop.util.CallUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 拨号fragment
 */
public class CallFragment extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.rl_contacts_container)
    RelativeLayout rlContactsContainer;
    @BindView(R.id.ll_call_container)
    LinearLayout llCallContainer;
    @BindView(R.id.rl_delete_container)
    RelativeLayout rlDeleteContainer;
    private String phoneString = "";

    public static CallFragment newInstance() {
        CallFragment fragment = new CallFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_call, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        List<CallNumBean> list = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            CallNumBean callNumBean = new CallNumBean();
            switch (i) {
                case 1:
                    callNumBean.setNum(1 + "");
                    callNumBean.setLetter("");
                    break;
                case 2:
                    callNumBean.setNum(2 + "");
                    callNumBean.setLetter("ABC");
                    break;
                case 3:
                    callNumBean.setNum(3 + "");
                    callNumBean.setLetter("DEF");
                    break;
                case 4:
                    callNumBean.setNum(4 + "");
                    callNumBean.setLetter("GHI");
                    break;
                case 5:
                    callNumBean.setNum(5 + "");
                    callNumBean.setLetter("JKL");
                    break;
                case 6:
                    callNumBean.setNum(6 + "");
                    callNumBean.setLetter("MNO");
                    break;
                case 7:
                    callNumBean.setNum(7 + "");
                    callNumBean.setLetter("PQRS");
                    break;
                case 8:
                    callNumBean.setNum(8 + "");
                    callNumBean.setLetter("TUV");
                    break;
                case 9:
                    callNumBean.setNum(9 + "");
                    callNumBean.setLetter("WXYZ");
                    break;
                case 10:
                    callNumBean.setNum("*");
                    callNumBean.setLetter("");
                    break;
                case 11:
                    callNumBean.setNum("0");
                    callNumBean.setLetter("+");
                    break;
                case 12:
                    callNumBean.setNum("#");
                    callNumBean.setLetter("");
                    break;
            }
            list.add(callNumBean);
        }

        CallAdapter callAdapter = new CallAdapter(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycleView.setLayoutManager(gridLayoutManager);
        recycleView.setNestedScrollingEnabled(false);//禁止滑动
        recycleView.setAdapter(callAdapter);

        rlDeleteContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {//长按回退清空
                phoneString = "";
                tvPhoneNum.setText(phoneString);
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_phone_num, R.id.rl_contacts_container, R.id.ll_call_container, R.id.rl_delete_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_contacts_container://联系人
                startActivity(new Intent(getActivity(), ContactsListActivity.class));
                break;
            case R.id.ll_call_container://拨出
                CallUtil.call(getActivity(),tvPhoneNum.getText().toString());
                break;
            case R.id.rl_delete_container://回退
                String string = tvPhoneNum.getText().toString();
                if (!TextUtils.isEmpty(string)) {
                    phoneString = string.substring(0, string.length() - 1);
                    tvPhoneNum.setText(phoneString);
                }
                break;
        }
    }

    class CallAdapter extends BaseQuickAdapter<CallNumBean, BaseViewHolder> {

        public CallAdapter(@Nullable List<CallNumBean> data) {
            super(R.layout.item_call, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final CallNumBean item) {
            helper.setText(R.id.tv_num, item.getNum());
            helper.setText(R.id.tv_letter, item.getLetter());
            helper.getView(R.id.ll_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvPhoneNum.setText(phoneString += item.getNum());
                }
            });
        }
    }
}
