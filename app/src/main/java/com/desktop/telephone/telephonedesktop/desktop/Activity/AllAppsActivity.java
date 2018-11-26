package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllAppsActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        List<AppInfoBean> appInfoList = new ArrayList<>();
        appInfoList.addAll(getAppInfos(this));
        myAdapter = new MyAdapter(appInfoList);
        recycleView.setAdapter(myAdapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<AppInfoBean> list;

        public MyAdapter(List<AppInfoBean> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(AllAppsActivity.this, R.layout.item_all_apps, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
            if (list.get(i).isShowDesktop) {
                myViewHolder.ivAddOrRemove.setImageResource(R.mipmap.delete_icon);
                myViewHolder.ivAddOrRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(list.get(i));//通知桌面删除
                        Toast.makeText(AllAppsActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                        list.get(i).isShowDesktop = false;
                        sortShowDesktopList(list);
                        notifyDataSetChanged();
                    }
                });
            } else {
                myViewHolder.ivAddOrRemove.setImageResource(R.mipmap.add_icon);
                myViewHolder.ivAddOrRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(list.get(i));//通知桌面添加
                        Toast.makeText(AllAppsActivity.this, "添加桌面成功", Toast.LENGTH_SHORT).show();
                        list.get(i).isShowDesktop = true;
                        sortShowDesktopList(list);
                        notifyDataSetChanged();
                    }
                });
            }
            myViewHolder.tvAppName.setText(list.get(i).getAppName());
            myViewHolder.ivAppIcon.setImageDrawable(list.get(i).getAppIcon());
            myViewHolder.ll_item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startApp(list.get(i).getPackageName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_app_icon)
        ImageView ivAppIcon;
        @BindView(R.id.tv_app_name)
        TextView tvAppName;
        @BindView(R.id.iv_add_or_remove)
        ImageView ivAddOrRemove;
        @BindView(R.id.ll_item_container)
        LinearLayout ll_item_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static List<AppInfoBean> getAppInfos(Context context) {
        PackageManager pm = context.getPackageManager();
        //所有的安装在系统上的应用程序包信息。
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfoBean> appInfos = new ArrayList<AppInfoBean>();
        for (PackageInfo packInfo : packInfos) {
            boolean notActiveApp = NotActiveApp(context, packInfo.packageName);
            if (notActiveApp)
                continue;

            AppInfoBean appInfo = new AppInfoBean();
            //packInfo  相当于一个应用程序apk包的清单文件
            String packname = packInfo.packageName;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            String name = packInfo.applicationInfo.loadLabel(pm).toString();

            //应用程序信息的标记 相当于用户提交的答卷
            int flags = packInfo.applicationInfo.flags;
            //操作系统分配给应用系统的一个固定的编号。一旦应用程序被装到手机 id就固定不变了。
            int uid = packInfo.applicationInfo.uid;

            appInfo.setUid(uid);
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//用户程序
                appInfo.setUserApp(true);
            } else {//系统程序
                appInfo.setUserApp(false);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {//手机的内存
                appInfo.setInRom(true);
            } else {//手机外存储设备
                appInfo.setInRom(false);
            }
            appInfo.setPackageName(packname);
            appInfo.setAppIcon(icon);
            appInfo.setAppName(name);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    /**
     * 判断app能不能主动启动 否就隐藏
     */
    public static boolean NotActiveApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null)
            return true;
        return false;
    }

    /**
     * 包名直接跳转
     **/
    public void startApp(String pkgName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            startActivity(intent);
        }
    }

    //排序
    public void sortShowDesktopList(List<AppInfoBean> list) {
        List<AppInfoBean> showList = new ArrayList<>();
        List<AppInfoBean> noShowList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isShowDesktop) {
                showList.add(list.get(i));
            } else {
                noShowList.add(list.get(i));
            }
        }
        list.clear();
        list.addAll(showList);
        list.addAll(noShowList);
    }
}
