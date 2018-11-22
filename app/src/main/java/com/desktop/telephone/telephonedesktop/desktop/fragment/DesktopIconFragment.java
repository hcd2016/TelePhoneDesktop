package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DesktopIconFragment extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_desk_top, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add("item"+i+1);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycleView.setLayoutManager(gridLayoutManager);
        DesktopIconAdapter desktopIconAdapter = new DesktopIconAdapter(list);
        recycleView.setAdapter(desktopIconAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class DesktopIconAdapter extends RecyclerView.Adapter<MyViewholder> {
        List<String> list;

        public DesktopIconAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(getContext(), R.layout.item_desktop_icon, null);
            return new MyViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder viewHolder, int i) {
            viewHolder.tvIcon.setText(list.get(i));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_icon)
        TextView tvIcon;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
