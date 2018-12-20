package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.view.ScrollAdapter;
import com.desktop.telephone.telephonedesktop.view.ScrollLayout;

import org.greenrobot.greendao.DbUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DesktopIconFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.container)
    ScrollLayout mContainer;  // 滑动控件的容器Container
    private List<DesktopIconBean> list;


    // Container的Adapter
    private ScrollAdapter mItemsAdapter;
    // Container中滑动控件列表
    private List<DesktopIconBean> defaultList;

    //xUtils中操纵SQLite的助手类
    private DbUtils mDbUtils;
    private List<DesktopIconBean> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_desk_top, null);
        unbinder = ButterKnife.bind(this, view);
        initIconData();
        initView();


//        // 从缓存中初始化滑动控件列表
//        getDataFromCache();
//        // 初始化控件
//        initView();
//        //初始化容器Adapter
//        loadBackground();
        return view;
    }

    private void initView() {
        //初始化Container的Adapter
        mItemsAdapter = new ScrollAdapter(getActivity(), mList);
        //设置Container添加删除Item的回调
        mContainer.setOnAddPage(new ScrollLayout.OnAddOrDeletePage() {
            @Override
            public void onAddOrDeletePage(int page, boolean isAdd) {

            }
        });
        //设置Container页面换转的回调，比如自第一页滑动第二页
        mContainer.setOnPageChangedListener(new ScrollLayout.OnPageChangedListener() {
            @Override
            public void onPage2Other(int n1, int n2) {
            }
        });
        //设置Container编辑模式的回调，长按进入修改模式
        mContainer.setOnEditModeListener(new ScrollLayout.OnEditModeListener() {
            @Override
            public void onEdit() {

            }
        });
        //设置Adapter
        mContainer.setSaAdapter(mItemsAdapter);
        //动态设置Container每页的列数为2行
        mContainer.setColCount(3);
        //动态设置Container每页的行数为4行
        mContainer.setRowCount(3);
        //调用refreView绘制所有的Item
        mContainer.refreView();
    }

//    // 设置Container滑动背景图片
//    private void loadBackground() {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        mContainer.setBackGroud(BitmapFactory.decodeResource(getResources(),
//                R.drawable.bg2, options));
//    }

    /**
     * 桌面数据添加
     */
    private void initIconData() {
        defaultList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            DesktopIconBean moveItem = new DesktopIconBean();
            moveItem.setMid(i);
            switch (i) {
                case 0:
                    //电话
                    moveItem.setIconType(1);
                    moveItem.setTitle("电话");
                    moveItem.setImg_id_name("phone_icon");
                    break;
                case 1:
                    //智能通讯录
                    moveItem.setIconType(1);
                    moveItem.setTitle("智能通讯录");
                    moveItem.setImg_id_name("call_records_icon");
                    break;
                case 2:
                    //电子相册
                    moveItem.setIconType(1);
                    moveItem.setTitle("电子相册");
                    moveItem.setImg_id_name("photo_icon");
                    break;
                case 3:
                    //黑红名单
                    moveItem.setIconType(1);
                    moveItem.setTitle("黑红名单");
                    moveItem.setImg_id_name("blacklist_icon");
                    break;
//                case 4:
//                    //一键拨号
//                    moveItem.setIconType(1);
//                    moveItem.setTitle("一键拨号");
//                    moveItem.setImg_id_name("one_key");
//                    break;
                case 4:
                    //录音
                    moveItem.setIconType(1);
                    moveItem.setTitle("录音");
                    moveItem.setImg_id_name("record_icon");
                    break;
                case 5:
                    //通话记录
                    moveItem.setIconType(1);
                    moveItem.setTitle("通话记录");
                    moveItem.setImg_id_name("address_list_icon");
                    break;
                case 6:
                    //sos
                    moveItem.setIconType(1);
                    moveItem.setTitle("SOS");
                    moveItem.setImg_id_name("sos_icon");
                    break;
                case 7:
                    //所有应用
                    moveItem.setIconType(1);
                    moveItem.setTitle("所有应用");
                    moveItem.setImg_id_name("all_apps_icon");
                    break;
            }
            defaultList.add(moveItem);
        }

//        List<DesktopIconBean> list = DaoUtil.getDesktopIconBeanDao().loadAll();
        mList = DaoUtil.querydata();
        if(mList == null || mList.size() == 0) {//数据库中没有列表(第一次安装)
            mList.addAll(defaultList);
            DaoUtil.saveNLists(defaultList);//保存默认的list
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

//    class DesktopIconAdapter extends RecyclerView.Adapter<MyViewholder> {
//        List<String> list;
//
//        public DesktopIconAdapter(List<String> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            View view = View.inflate(getContext(), R.layout.item_desktop_icon, null);
//            return new MyViewholder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewholder viewHolder, int i) {
//            viewHolder.tvIcon.setText(list.get(i));
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//    }


//    public class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return list.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            ViewHolder mHolder;
//            if (view == null) {
//                mHolder = new ViewHolder();
//                view = View.inflate(getActivity(), R.layout.item_desktop_icon, null);
//                mHolder.tv_title = view.findViewById(R.id.tv_title);
//                mHolder.iv_icon = view.findViewById(R.id.iv_icon);
//                view.setTag(mHolder);
//            } else {
//                mHolder = (ViewHolder) view.getTag();
//            }
//            mHolder.tv_title.setText(list.get(i).getTitle());
//            mHolder.iv_icon.setImageResource(list.get(i).getIcon());
//            return view;
//        }
//    }
//
//    class ViewHolder {
//        private TextView tv_title;
//        private ImageView iv_icon;
//    }
}
