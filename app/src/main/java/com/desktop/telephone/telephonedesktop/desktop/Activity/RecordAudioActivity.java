package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.desktop.dialog.AudioRecordDialog;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.record.AudioFileFunc;
import com.desktop.telephone.telephonedesktop.view.record.AudioFileUtils;
import com.desktop.telephone.telephonedesktop.view.record.AudioRecorder;
import com.desktop.telephone.telephonedesktop.view.record.AudioRecorder.Status;
import com.desktop.telephone.telephonedesktop.view.record.MediaRecordFunc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 录音功能
 */
public class RecordAudioActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_mic_icon)
    ImageView ivMicIcon;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.rl_btn_container)
    RelativeLayout rlBtnContainer;
    @BindView(R.id.ll_btn_files)
    LinearLayout llBtnFiles;
    @BindView(R.id.ll_btn_complete)
    LinearLayout llBtnComplete;
    private MyRunnable mRunnable;
    private MediaRecorder recorder;
//    private AudioRecorder audioRecorder;
    private AudioRecordDialog audioRecordDialog;
    private MediaRecorder mMediaRecorder;
    private MediaRecordFunc mediaRecordFunc;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
//        audioRecorder = new AudioRecorder();
//        audioRecorder.createDefaultAudio(getCurrentTime());
    }

    //创建保存录音的目录
    private void createRecorderFile() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath = absolutePath + "/recorder";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    //获取当前时间，以其为名来保存录音
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    private boolean isPlaying = false;//是否正在录音
    private MyRunnable myRunnable;

    @OnClick({R.id.iv_back, R.id.rl_btn_container, R.id.ll_btn_files, R.id.ll_btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_btn_container://按钮点击
                if (isPlaying) {//正在播放,暂停播放
                    tvTimer.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.VISIBLE);
                    ivPause.setVisibility(View.GONE);
                    isPause = true;//暂停播放
                    myRunnable = null;
//                    pauseRecord();
                    mediaRecordFunc.pause();
                } else {//暂停或未开始播放,开始或继续播放
                    ivMicIcon.setVisibility(View.GONE);
                    tvTimer.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    ivPause.setVisibility(View.VISIBLE);
//                    if (audioRecorder == null) {
//                        audioRecorder = new AudioRecorder();
//                        audioRecorder.createDefaultAudio(getCurrentTime());
//                    }
                    if(mediaRecordFunc == null) {
                        mediaRecordFunc = new MediaRecordFunc(false);
                        mediaRecordFunc.startRecordAndFile();
                    }else {
                        if(isPause) {
                            mediaRecordFunc.resume();
                        }else {
                            mediaRecordFunc.startRecordAndFile();
                        }
                    }
//                    startRecord();

                    isPause = false;
                    isClick = true;
                    if (myRunnable == null) {
                        myRunnable = new MyRunnable();
                        myRunnable.run();
                    }
                }
                isPlaying = !isPlaying;
                break;
            case R.id.ll_btn_files:
                startActivity(AudioRecordListActivity.class);
                break;
            case R.id.ll_btn_complete://完成
                if(mediaRecordFunc == null) {
                    Utils.Toast("你还没有开始录音");
                    return;
                }
//                if(audioRecorder == null || audioRecorder.getStatus().equals(Status.STATUS_NO_READY )
//                        || audioRecorder.getStatus().equals(Status.STATUS_READY )) {
//                    Utils.Toast("你还没有开始录音");
//                    return;
//                }
                audioRecordDialog = new AudioRecordDialog(this);
                audioRecordDialog.setOnAudioRecordDialogClickListener(new AudioRecordDialog.OnAudioRecordDialogClickListener() {
                    @Override
                    public void saveClick() {//保存
//                        stopRecord();
                        mediaRecordFunc.stopRecordAndFile();
                        mediaRecordFunc = null;
                        //重置
//                        audioRecorder = null;
                        isPause = true;
                        currentSecond = 0;
                        myRunnable = null;
                        isPlaying = false;
                        ivMicIcon.setVisibility(View.VISIBLE);
                        tvTimer.setVisibility(View.GONE);
                        ivPlay.setVisibility(View.VISIBLE);
                        ivPause.setVisibility(View.GONE);
                        audioRecordDialog.dismiss();
                    }

                    @Override
                    public void backClick() {//返回
                        audioRecordDialog.dismiss();
                    }

                    @Override
                    public void deleteClick() {//删除
//                        String fileName = audioRecorder.getFileName()+".wav";
                        //停止录音,删除文件,重置计时
//                        audioRecorder.canel();
                        mediaRecordFunc.canel();

//                        List<File> wavFiles = AudioFileUtils.getWavFiles();
//                        Iterator<File> it = wavFiles.iterator();
//                        while (it.hasNext()) {
//                            String name = it.next().getName();
//                            if (name.equals(fileName)) {
//                                it.remove();
//                            }
//                        }
                        //重置
//                        audioRecorder = null;
                        isPause = true;
                        mediaRecordFunc = null;
                        currentSecond = 0;
                        myRunnable = null;
                        isPlaying = false;
                        ivMicIcon.setVisibility(View.VISIBLE);
                        tvTimer.setVisibility(View.GONE);
                        ivPlay.setVisibility(View.VISIBLE);
                        ivPause.setVisibility(View.GONE);
                        audioRecordDialog.dismiss();
                    }
                });
                audioRecordDialog.show();
                break;
        }
    }

    private boolean isClick = false;
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            if (!isPause) {//需要播放
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 1000);
                if(isClick) {//解决一秒误差问题
                    isClick = false;
                }else {
                    currentSecond = currentSecond + 1000;
                }
                tvTimer.setText(formatLongToTimeStr(currentSecond));
            }
        }
    }

//    private void stopRecord() {
//        audioRecorder.stopRecord();
//    }
//
//    private void startRecord() {
//        audioRecorder.startRecord();
//    }
//
//    public void pauseRecord() {
//        audioRecorder.pauseRecord();
//    }

    //计时器
    private Handler mhandle = new Handler();
    private long currentSecond = 0;//当前毫秒数
    private boolean isPause = false;//是否要暂停播放

    private String formatLongToTimeStr(Long l) {
        int minute = 0;
        int second = 0;
        second = l.intValue() / 1000;
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }
//        if (minute >= 60) {
//            minute = minute % 60;
//        }
        return (getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }
}