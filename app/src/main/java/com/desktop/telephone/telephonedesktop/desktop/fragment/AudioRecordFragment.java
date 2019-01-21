package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.record.AudioFileFunc;
import com.desktop.telephone.telephonedesktop.view.record.AudioFileUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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
    @BindView(R.id.player_view)
    PlayerView playerView;
    private int type = 0;//0为普通录音,1为通话录音
    private MyAdapter myAdapter;
    private AlertDialog alertDialog;
    private List<File> normalList;
    private List<File> callList;
    private SimpleExoPlayer player;

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
        List<File> wavFiles = AudioFileFunc.getAmrFiles();
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
        View view = View.inflate(getActivity(), R.layout.empty_view, null);
        myAdapter.setEmptyView(view);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(myAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void startPlay(File file) {

        // 得到默认合适的带宽
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// 创建跟踪的工厂
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
// 创建跟踪器
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
// 创建player
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
// 绑定player
        playerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "ExoPlayer"), bandwidthMeter);
// 创建要播放的媒体的MediaSource
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
// 准备播放器的MediaSource
        player.prepare(mediaSource);
// 当准备完毕后直接播放
        player.setPlayWhenReady(true);
        playerView.findViewById(R.id.rl_btn_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        playerView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                playerView.setVisibility(View.GONE);
            }
        });
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
//                    Utils.openFile(item.getAbsolutePath(),getActivity());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    String path = Environment.getExternalStorageDirectory().getPath()+ "/1.mp4";//该路径可以自定义
//                    File file = new File(path);
//                    Uri uri = Uri.fromFile(item);
//                    intent.setDataAndType(uri, "audio/*");
//                    startActivity(intent);

//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse(item.getAbsolutePath()), "video/wav");
//                    startActivity(intent);

                    playerView.setVisibility(View.VISIBLE);
                    startPlay(item);
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
                                    if (type == 0) {
                                        normalList.remove(item);
                                    } else {
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
