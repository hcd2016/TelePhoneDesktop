package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAppsActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

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
        List<PackageInfo> allApps = getAllApps(this);
        for (int i = 0; i < allApps.size(); i++) {
            AppInfoBean appInfoBean = new AppInfoBean();
            appInfoBean.setPackageName(allApps.get(i).packageName);
            appInfoBean.setAppIcon(getPackageManager().getApplicationIcon(allApps.get(i).applicationInfo));
            appInfoBean.setAppName(getPackageManager().getApplicationLabel(allApps.get(i).applicationInfo).toString());
            appInfoList.add(appInfoBean);
        }
        MyAdapter myAdapter = new MyAdapter(appInfoList);
        recycleView.setAdapter(myAdapter);
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
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.tvAppName.setText(list.get(i).getAppName());
            myViewHolder.ivAppIcon.setImageDrawable(list.get(i).getAppIcon());
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public static List<PackageInfo> getAllApps(Context context) {

        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);
            // if()里的值如果<=0则为自己装的程序，否则为系统工程自带
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                // apps.add(pak);
            }
            apps.add(pak);
        }
        return apps;
    }
}
