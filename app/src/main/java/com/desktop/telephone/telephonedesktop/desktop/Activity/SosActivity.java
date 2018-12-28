package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.desktop.telephone.telephonedesktop.bean.EvenCallRecordBean;
import com.desktop.telephone.telephonedesktop.bean.SosBean;
import com.desktop.telephone.telephonedesktop.desktop.dialog.SosAddDialog;
import com.desktop.telephone.telephonedesktop.gen.SosBeanDao;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;

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
        if (sosBeans != null && sosBeans.size() > 0) {
            list.addAll(sosBeans);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
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
                            Utils.Toast("本机号码不能为空");
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

    private AlertDialog alertDialog;
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
        protected void convert(BaseViewHolder helper, final SosBean item) {
            helper.getView(R.id.carview).setBackgroundColor(Utils.getColorBgFromPosition(helper.getLayoutPosition()));
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_phone_num, item.getPhoneNum());
            helper.setText(R.id.tv_content, item.getSmsContent());
            helper.getView(R.id.carview).setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    alertDialog = new AlertDialog.Builder(SosActivity.this)
                            .setTitle("删除记录")
                            .setMessage("确定要删除这条记录吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    list.remove(item);
                                    DaoUtil.getSosBeanDao().delete(item);
                                    notifyDataSetChanged();
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
            helper.getView(R.id.carview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CallUtil.call(SosActivity.this,item.getPhoneNum(),false);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DaoUtil.closeDb();
    }
}
