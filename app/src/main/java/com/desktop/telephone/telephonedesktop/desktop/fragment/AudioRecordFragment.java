package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.record.AudioFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AudioRecordFragment extends Fragment {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    private int type = 0;//0为普通录音,1为通话录音
    private MyAdapter myAdapter;
    private AlertDialog alertDialog;
    private List<File> normalList;
    private List<File> callList;

    public static AudioRecordFragment newInstance(int type) {
        Bundle argz = new Bundle();
        argz.putInt("type", type);
        AudioRecordFragment fragment = new AudioRecordFragment();
        fragment.setArguments(argz);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_audio_record, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        List<File> wavFiles = AudioFileUtils.getWavFiles();
        Collections.reverse(wavFiles);
        //普通列表
        normalList = new ArrayList<>();
        //通话列表
        callList = new ArrayList<>();
        for (int i = 0; i < wavFiles.size(); i++) {
            File file = wavFiles.get(i);
            if (file.getName().contains("call")) {
                callList.add(file);
            } else {
                normalList.add(file);
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            int type = bundle.getInt("type");
            this.type = type;
        }
        if (type == 0) {
            myAdapter = new MyAdapter(normalList);
        } else {
            myAdapter = new MyAdapter(callList);
        }
        View view = View.inflate(getActivity(),R.layout.empty_view,null);
        myAdapter.setEmptyView(view);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(myAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

        public MyAdapter(@Nullable List<File> data) {
            super(R.layout.item_audio_record, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final File item) {
            helper.setText(R.id.tv_file_name, item.getName());
            helper.setText(R.id.tv_date, Utils.getCreateTime(item.getAbsolutePath()));
            helper.getView(R.id.ll_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.openFile(item.getAbsolutePath(),getActivity());
                }
            });
            helper.getView(R.id.ll_item_container).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("删除录音")
                            .setMessage("确定要删除该录音")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Utils.removeFile(item.getAbsolutePath());
                                    if(type == 0) {
                                        normalList.remove(item);
                                    }else {
                                        callList.remove(item);
                                    }
                                    Utils.Toast("删除成功");
                                    alertDialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                    return false;
                }
            });
        }
    }
}
