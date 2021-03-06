package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.EventBlacklistInfoBean;
import com.desktop.telephone.telephonedesktop.util.BlackListFileUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 黑/红名单添加
 */
public class BlacklistAddActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_btn_save)
    TextView tvBtnSave;
    @BindView(R.id.tv_mode_desc)
    TextView tvModeDesc;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.ll_mode_container)
    LinearLayout llModeContainer;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private int type;
    private PopupWindow popupWindow;
    private AlertDialog alertDialog;
    private boolean isUpdate = false;//默认是修改

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist_add);
        ButterKnife.bind(this);
        initView();
        BlackListFileUtil.createFlies();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 1);
        String phoneNum = getIntent().getStringExtra("phoneNum");
        if (!TextUtils.isEmpty(phoneNum)) {//是修改
            isUpdate = true;
            tvTitle.setText("修改");
            etPhoneNum.setText(phoneNum);
            etPhoneNum.setSelection(phoneNum.length());
        } else {
            tvTitle.setText("添加");
        }
        modeStatus = type;
        if (modeStatus == 1) {
            tvModeDesc.setText("黑名单");
        } else {
            tvModeDesc.setText("红名单");
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_btn_save, R.id.ll_mode_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_btn_save://保存
                final String phoneNum = etPhoneNum.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    Utils.Toast("号码不能为空");
                    return;
                }
                List<BlackListInfoBean> list = DaoUtil.getBlackListInfoBeanDao().loadAll();
                if (list != null) {
                    boolean isRepetition = false;
                    for (final BlackListInfoBean blackListInfoBean : list) {//判断重复情况
                        if (blackListInfoBean.getPhone().equals(phoneNum)) {//号码在列表中已存在
                            isRepetition = true;
                            if (modeStatus == 1) {//黑名单类型
                                if (blackListInfoBean.getType() == modeStatus) {
//                                    if (isUpdate) {
//                                        Utils.Toast("保存成功");
//                                    } else {
                                        Utils.Toast("该号码已存在黑名单列表,请勿重复添加");
//                                    }
                                    return;
                                } else {
                                    alertDialog = new AlertDialog.Builder(BlacklistAddActivity.this)
                                            .setMessage("该号码已存在红名单列表,是否从红名单移至黑名单?")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    DaoUtil.getBlackListInfoBeanDao().delete(blackListInfoBean);

                                                    BlackListInfoBean addBlackListInfoBean = new BlackListInfoBean(null, phoneNum, modeStatus, Utils.getFormatDate());
                                                    DaoUtil.getBlackListInfoBeanDao().insert(addBlackListInfoBean);

                                                    EventBlacklistInfoBean eventBlacklistInfoBean = new EventBlacklistInfoBean();
                                                    eventBlacklistInfoBean.setNeedDelete(true);
                                                    eventBlacklistInfoBean.setDeletebean(blackListInfoBean);
                                                    eventBlacklistInfoBean.setAddbean(addBlackListInfoBean);
                                                    EventBus.getDefault().post(eventBlacklistInfoBean);
                                                    Utils.Toast("操作成功");
                                                    BlackListFileUtil.updateFile();//更新文件
                                                    return;
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
                                }
                            } else {//红名单类型
                                if (blackListInfoBean.getType() == modeStatus) {
                                    Utils.Toast("该号码已存在红名单列表,请勿重复添加");
                                    return;
                                } else {
                                    alertDialog = new AlertDialog.Builder(BlacklistAddActivity.this)
                                            .setMessage("该号码已存在黑名单列表,是否从黑名单移至红名单?")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    DaoUtil.getBlackListInfoBeanDao().delete(blackListInfoBean);

                                                    BlackListInfoBean addBlackListInfoBean = new BlackListInfoBean(null, phoneNum, modeStatus, Utils.getFormatDate());
                                                    DaoUtil.getBlackListInfoBeanDao().insert(addBlackListInfoBean);

                                                    EventBlacklistInfoBean eventBlacklistInfoBean = new EventBlacklistInfoBean();
                                                    eventBlacklistInfoBean.setNeedDelete(true);
                                                    eventBlacklistInfoBean.setDeletebean(blackListInfoBean);
                                                    eventBlacklistInfoBean.setAddbean(addBlackListInfoBean);
                                                    EventBus.getDefault().post(eventBlacklistInfoBean);
                                                    Utils.Toast("操作成功");
                                                    BlackListFileUtil.updateFile();//更新文件
                                                    return;
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
                                }
                            }
                        }
                    }
                    if (!isRepetition) {//不重复才正常添加
                        if (isUpdate) {
                            for (int i = 0; i < list.size(); i++) {
                                BlackListInfoBean blackListInfoBean = list.get(i);
                                if (blackListInfoBean.getPhone().equals(getIntent().getStringExtra("phoneNum"))) {
                                    if (modeStatus != blackListInfoBean.getType()) {//修改黑名单选的红名单添加,重设id保证排序
                                        blackListInfoBean.setId(null);
                                        blackListInfoBean.setType(modeStatus);
                                        blackListInfoBean.setPhone(phoneNum);
                                        blackListInfoBean.setDate(Utils.getFormatDate());
                                        DaoUtil.getBlackListInfoBeanDao().insert(blackListInfoBean);
                                        Utils.Toast("添加成功");
                                    }else {
                                        blackListInfoBean.setType(modeStatus);
                                        blackListInfoBean.setPhone(phoneNum);
                                        DaoUtil.getBlackListInfoBeanDao().update(blackListInfoBean);
                                        Utils.Toast("修改成功");
                                    }
                                    EventBus.getDefault().post(new EventBean(EventBean.BLACK_LIST_ALL_NOTIFAL));
                                }
                            }
                        } else {
                            BlackListInfoBean blackListInfoBean = new BlackListInfoBean(null, phoneNum, modeStatus, Utils.getFormatDate());
                            DaoUtil.getBlackListInfoBeanDao().insert(blackListInfoBean);
                            EventBlacklistInfoBean eventBlacklistInfoBean = new EventBlacklistInfoBean();
                            eventBlacklistInfoBean.setNeedDelete(false);
                            eventBlacklistInfoBean.setAddbean(blackListInfoBean);
                            EventBus.getDefault().post(eventBlacklistInfoBean);
                            Utils.Toast("添加成功");
                        }
                    }
                }
                BlackListFileUtil.updateFile();//更新黑名单文件
                finish();
                break;
            case R.id.ll_mode_container:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    showPop();
                }
                break;
        }
    }

    int modeStatus = 1;

    private void showPop() {
        final int[] choiseStatus = {modeStatus};//记录选中状态,关闭时有可能没确定.
        View view = View.inflate(this, R.layout.pop_add_type_choise, null);
        final ImageView ivBlacklist = view.findViewById(R.id.iv_blacklist);
        final ImageView ivWhitelist = view.findViewById(R.id.iv_whitelist);

        LinearLayout llModeBlacklistContainer = view.findViewById(R.id.ll_mode_blacklist_container);
        LinearLayout llModeWhitelistContainer = view.findViewById(R.id.ll_mode_whitelist_container);

        TextView btn_cancle = view.findViewById(R.id.btn_cancle);
        TextView btn_sure = view.findViewById(R.id.btn_sure);

        //初始化:
        if (modeStatus == 1) {
            ivBlacklist.setVisibility(View.VISIBLE);
        } else {
            ivWhitelist.setVisibility(View.VISIBLE);
        }

        llModeBlacklistContainer.setOnClickListener(new View.OnClickListener() {//选择黑名单模式
            @Override
            public void onClick(View view) {
                ivBlacklist.setVisibility(View.VISIBLE);
                ivWhitelist.setVisibility(View.GONE);
                choiseStatus[0] = 1;
            }
        });
        llModeWhitelistContainer.setOnClickListener(new View.OnClickListener() {//选择红名单模式
            @Override
            public void onClick(View view) {
                ivBlacklist.setVisibility(View.GONE);
                ivWhitelist.setVisibility(View.VISIBLE);
                choiseStatus[0] = 2;
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeStatus = choiseStatus[0];//保存选择状态
                if (modeStatus == 1) {
                    tvModeDesc.setText("黑名单");
                } else {
                    tvModeDesc.setText("红名单");
                }
                popupWindow.dismiss();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(llModeContainer);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivArrow.setImageResource(R.drawable.arrow_down);
            }
        });
        ivArrow.setImageResource(R.drawable.arrow_up);
    }

    public static void startActivity(Context context, int type, String phoneNum) {
        Intent intent = new Intent(context, BlacklistAddActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("phoneNum", phoneNum);
        context.startActivity(intent);
    }
}
