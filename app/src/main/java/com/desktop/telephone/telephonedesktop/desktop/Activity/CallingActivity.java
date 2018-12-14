package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.desktop.recevier.CallingConnectReciver;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.ContactsUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.view.record.AudioRecorderCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 呼叫中,通话界面.
 */
public class CallingActivity extends BaseActivity {
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_call_name)
    TextView tvCallName;
    @BindView(R.id.tv_call_num)
    TextView tvCallNum;
    @BindView(R.id.tv_key_in)
    TextView tvKeyIn;
    @BindView(R.id.tv_key1)
    TextView tvKey1;
    @BindView(R.id.tv_key2)
    TextView tvKey2;
    @BindView(R.id.tv_key3)
    TextView tvKey3;
    @BindView(R.id.tv_key4)
    TextView tvKey4;
    @BindView(R.id.tv_key5)
    TextView tvKey5;
    @BindView(R.id.tv_key6)
    TextView tvKey6;
    @BindView(R.id.tv_key7)
    TextView tvKey7;
    @BindView(R.id.tv_key8)
    TextView tvKey8;
    @BindView(R.id.tv_key9)
    TextView tvKey9;
    @BindView(R.id.tv_key_xing)
    TextView tvKeyXing;
    @BindView(R.id.tv_key0)
    TextView tvKey0;
    @BindView(R.id.tv_key_jin)
    TextView tvKeyJin;
    @BindView(R.id.iv_audio_record)
    ImageView ivAudioRecord;
    @BindView(R.id.tv_audio_record)
    TextView tvAudioRecord;
    @BindView(R.id.ll_audio_record)
    LinearLayout llAudioRecord;
    @BindView(R.id.iv_hand_free)
    ImageView ivHandFree;
    @BindView(R.id.tv_hand_free)
    TextView tvHandFree;
    @BindView(R.id.ll_hand_free)
    LinearLayout llHandFree;
    @BindView(R.id.iv_key_bord)
    ImageView ivKeyBord;
    @BindView(R.id.tv_key_bord)
    TextView tvKeyBord;
    @BindView(R.id.iv_hand_up)
    ImageView ivHandUp;
    @BindView(R.id.ll_key_bord)
    LinearLayout llKeyBord;
    @BindView(R.id.ll_key_bord_container)
    LinearLayout llKeyBordContainer;
    private int callStatus = 1;//呼叫状态,0为呼叫中,1为通话中
    private boolean isHandFree = false;//是否开启免提

    public static final String CALLING_CONNECT = "com.tongen.Tel.OUTGOING_RINGING";//通话接通
    public static final String HAND_OFF = "om.tongen.action.handle.off";//手柄放下
    public static final String HAND_ON = "com.tongen.action.handle.on";//手柄放下
    private String phoneNum;
    private CallingConnectReciver callingConnectReciver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_calling);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        SPUtil.getInstance().saveBoolean("isShowCallingActivity",true);//保存当前是否打开通话界面
        phoneNum = getIntent().getStringExtra("phoneNum");
        setViewData();
        registerReceivers();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
    }

    //相关广播注册
    private void registerReceivers() {
        //通话接通广播
        callingConnectReciver = new CallingConnectReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CALLING_CONNECT);
        registerReceiver(callingConnectReciver, intentFilter);
    }

    private void unRegisterReceivers() {
        unregisterReceiver(callingConnectReciver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Intent event) {
        switch (event.getAction()) {
            case CALLING_CONNECT://通话接通
                callStatus = 1;
                setViewData();
                break;
            case HAND_OFF://手柄放下
                //挂断电话
                //todo 通话计时完成等操作
                finish();
                break;
            case HAND_ON://手柄抬起

                break;
        }
    }

    private void setViewData() {
        if (TextUtils.isEmpty(phoneNum)) {
            return;
        }
        if (callStatus == 0) {//呼叫中
            tvStatus.setText("呼叫中...");
            //联系人显示
            String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
            if (TextUtils.isEmpty(name)) {//没有该联系人
                tvCallName.setVisibility(View.INVISIBLE);
            } else {
                tvCallName.setVisibility(View.VISIBLE);
                tvCallName.setText(name);
            }
            //软键盘
            tvCallNum.setText(phoneNum);
            llKeyBordContainer.setVisibility(View.INVISIBLE);
            //录音
            llAudioRecord.setClickable(false);
            ivAudioRecord.setImageResource(R.drawable.audio_record_unclick);
            tvAudioRecord.setText("录音");
            //免提
            llHandFree.setClickable(false);
            ivHandFree.setImageResource(R.drawable.hand_free_unclick);
            tvHandFree.setText("免提");
            //挂断按钮
            if (isHandFree) {
                ivHandUp.setVisibility(View.VISIBLE);
            } else {
                ivHandUp.setVisibility(View.GONE);
            }
        } else {//通话中
            if (callingRunnable == null) {
                callingRunnable = new CallingRunnable();
                callingRunnable.run();
            }

            //录音
            llAudioRecord.setClickable(true);
            ivAudioRecord.setImageResource(R.drawable.audio_record_icon);
            tvAudioRecord.setText("录音");

            //免提
            llHandFree.setClickable(true);
            ivHandFree.setImageResource(R.drawable.hands_free_icon);
            tvHandFree.setText("免提");
        }
    }

    private Handler callingHandler = new Handler();
    private int callingSecond = 0;//通话计时
    private CallingRunnable callingRunnable;
    private AudioRecordRunnable audioRecordRunnable;
    private Handler audioRecordHandler = new Handler();
    private boolean isCallingStop = false;

    //通话计时
    private class CallingRunnable implements Runnable {
        @Override
        public void run() {
            if (!isCallingStop) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                callingHandler.postDelayed(this, 1000);
                callingSecond = callingSecond + 1;
                tvStatus.setText(formatLongToTimeStr(callingSecond));
            }
        }
    }

    private int audioRecordSecond;
    private boolean isAudioRecordStop = false;

    //录音计时
    private class AudioRecordRunnable implements Runnable {
        @Override
        public void run() {
            if (!isAudioRecordStop) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                audioRecordHandler.postDelayed(this, 1000);
                audioRecordSecond = audioRecordSecond + 1;
                tvAudioRecord.setText(formatLongToTimeStr(audioRecordSecond));
            }
        }
    }


    private String formatLongToTimeStr(int callingSecond) {
        int minute = 0;
        int second = callingSecond;
        int hours = 0;
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute >= 60) {
            hours = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hours) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    private AudioRecorderCall audioRecorderCall;//通话录音
    public String keyInValue = "";
    private boolean isShowKeybord = false;

    @OnClick({R.id.tv_key1, R.id.tv_key2, R.id.tv_key3, R.id.tv_key4, R.id.tv_key5, R.id.tv_key6, R.id.tv_key7, R.id.tv_key8, R.id.tv_key9, R.id.tv_key_xing, R.id.tv_key0, R.id.tv_key_jin, R.id.ll_audio_record, R.id.ll_hand_free, R.id.iv_hand_up, R.id.ll_key_bord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_key1:
                keyInValue += "1";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "1");
                break;
            case R.id.tv_key2:
                keyInValue += "2";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "2");
                break;
            case R.id.tv_key3:
                keyInValue += "3";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "3");
                break;
            case R.id.tv_key4:
                keyInValue += "4";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "4");
                break;
            case R.id.tv_key5:
                keyInValue += "5";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "5");
                break;
            case R.id.tv_key6:
                keyInValue += "6";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "6");
                break;
            case R.id.tv_key7:
                keyInValue += "7";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "7");
                break;
            case R.id.tv_key8:
                keyInValue += "8";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "8");
                break;
            case R.id.tv_key9:
                keyInValue += "9";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "9");
                break;
            case R.id.tv_key_xing:
                keyInValue += "*";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "*");
                break;
            case R.id.tv_key0:
                keyInValue += "0";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "0");
                break;
            case R.id.tv_key_jin:
                keyInValue += "#";
                tvKeyIn.setText(keyInValue);
                CallUtil.callingKeyIn(this, "#");
                break;
            case R.id.ll_audio_record://录音
                ivAudioRecord.setImageResource(R.drawable.audio_recording_icon);
                isAudioRecordStop = false;
                if (audioRecorderCall == null) {//未开启录音,开启录音
                    audioRecorderCall = new AudioRecorderCall();
                    audioRecorderCall.createDefaultAudio(getCurrentTime());
                    audioRecorderCall.startRecord();
                    if (audioRecordRunnable == null) {
                        audioRecordRunnable = new AudioRecordRunnable();
                        audioRecordRunnable.run();
                    }
                } else {//录音中,结束录音
                    ivAudioRecord.setImageResource(R.drawable.audio_record_icon);
                    tvAudioRecord.setText("录音");
                    isAudioRecordStop = true;
                    audioRecorderCall.stopRecord();
                    audioRecorderCall = null;
                    audioRecordRunnable = null;
                    audioRecordSecond = 0;
                }
                break;
            case R.id.ll_hand_free://免提
                CallingActivity.startActivity(this,phoneNum);
                if (isHandFree) {//之前是免提,关闭免提
                    ivHandFree.setImageResource(R.drawable.hands_free_icon);
                    CallUtil.handFreeControl(this, 0);
                    ivHandUp.setVisibility(View.GONE);
                } else {//打开免提
                    ivHandFree.setImageResource(R.drawable.hand_free_close);
                    CallUtil.handFreeControl(this, 1);
                    ivHandUp.setVisibility(View.VISIBLE);
                }
                isHandFree = !isHandFree;
                break;
            case R.id.iv_hand_up://挂断,只有免提状态才出现这个按钮
                CallUtil.handUpWithFree(this);
                finish();
                break;
            case R.id.ll_key_bord://软键盘
                if (isShowKeybord) {//之前打开,关闭
                    llKeyBordContainer.setVisibility(View.INVISIBLE);
                } else {
                    llKeyBordContainer.setVisibility(View.VISIBLE);
                }
                isShowKeybord = !isShowKeybord;
                break;
        }
    }


    //获取当前时间，以其为名来保存录音
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    public static void startActivity(Context context, String phoneNum) {
        Intent intent = new Intent(context, CallingActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCallingStop = true;
        audioRecordHandler.removeCallbacksAndMessages(null);
        audioRecordHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        unRegisterReceivers();
        SPUtil.getInstance().saveBoolean("isShowCallingActivity",false);//保存当前是否打开通话界面
    }
}
