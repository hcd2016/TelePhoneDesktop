package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.SosBean;
import com.desktop.telephone.telephonedesktop.desktop.dialog.SosAddDialog;
import com.desktop.telephone.telephonedesktop.gen.SosBeanDao;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * sos,发送短信到添加的号码
 */
public class SosActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private List<SosBean> list;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        List<SosBean> sosBeans = DaoUtil.getSosBeanDao().loadAll();
        if (list != null && list.size() > 0) {
            list.addAll(sosBeans);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recycleView.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter(this.list);
        View view = View.inflate(this, R.layout.sos_foot_view, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加
                final SosAddDialog sosAddDialog = new SosAddDialog(SosActivity.this);
                sosAddDialog.setOnConfirmClickListner(new SosAddDialog.OnConfirmClickListner() {
                    @Override
                    public void confirmClick() {
                        String content = sosAddDialog.getContent();
                        String name = sosAddDialog.getName();
                        String phoneNum = sosAddDialog.getPhoneNum();

                        if (TextUtils.isEmpty(name)) {
                            Utils.Toast("姓名不能为空");
                            return;
                        }
                        if (TextUtils.isEmpty(phoneNum)) {
                            Utils.Toast("号码不能为空");
                            return;
                        }
                        if (TextUtils.isEmpty(content)) {
                            Utils.Toast("发送内容不能为空");
                            return;
                        }
                        SosBean sosBean = new SosBean(null, name, phoneNum, content);
                        SosActivity.this.list.add(sosBean);
                        myAdapter.notifyDataSetChanged();
                        DaoUtil.getSosBeanDao().insert(sosBean);
                    }
                });
                sosAddDialog.show();
            }
        });
        myAdapter.setFooterView(view);
        recycleView.setAdapter(myAdapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    class MyAdapter extends BaseQuickAdapter<SosBean, BaseViewHolder> {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone_num)
        TextView tvPhoneNum;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public MyAdapter(@Nullable List<SosBean> data) {
            super(R.layout.item_sos, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SosBean item) {
            helper.setText(R.id.tv_name, "姓名: " + item.getName());
            helper.setText(R.id.tv_phone_num, "号码: " + item.getPhoneNum());
            helper.setText(R.id.tv_content, "发送内容: " + item.getSmsContent());
        }
    }

}
