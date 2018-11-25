package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktop.telephone.telephonedesktop.R;

public class DesktopIconFragment extends Fragment {
//    Unbinder unbinder;
//    @BindView(R.id.container)
//    ScrollLayout mContainer;  // 滑动控件的容器Container
//    private List<DesktopIconBean> list;
//
//
//    // Container的Adapter
//    private ScrollAdapter mItemsAdapter;
//    // Container中滑动控件列表
//    private List<MoveItem> mList;
//
//    //xUtils中操纵SQLite的助手类
//    private DbUtils mDbUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_desk_top, null);
//        unbinder = ButterKnife.bind(this, view);

//        // 从缓存中初始化滑动控件列表
//        getDataFromCache();
//        // 初始化控件
//        initView();
//        //初始化容器Adapter
//        loadBackground();
        return view;
    }




//    private void initView() {
//        list = new ArrayList<>();
//        for (int i = 0; i < 9; i++) {
//            DesktopIconBean desktopIconBean = new DesktopIconBean();
//            desktopIconBean.setIcon(R.mipmap.ic_launcher);
//            desktopIconBean.setTitle("item" + i);
//            list.add(desktopIconBean);
//        }
//
//    }

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
