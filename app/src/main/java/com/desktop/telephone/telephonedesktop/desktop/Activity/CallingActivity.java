package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.CallRecordBean;
import com.desktop.telephone.telephonedesktop.bean.EvenCallRecordBean;
import com.desktop.telephone.telephonedesktop.desktop.recevier.CallingConnectReciver;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.ContactsUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;
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
    @BindView(R.id.iv_answer)
    ImageView ivAnswer;
    @BindView(R.id.ll_btn_container)
    LinearLayout llBtnContainer;
    @BindView(R.id.ll_iv_answer)
    LinearLayout llIvAnswer;
    @BindView(R.id.ll_iv_hand_up)
    LinearLayout llIvHandUp;
    private boolean isCalling = true;//true为呼叫,false为来电
    private int callStatus = 0;//呼叫状态,0为呼叫中,1为通话中
    private boolean isHandFree = false;//是否开启免提
    private int callingRecordStatus = 0;//通话记录状态,0为已拨,1为已接,2为未接,3为主动挂断


    public static final String CALLING_CONNECT = "com.tongen.Tel.OUTGOING_RINGING";//通话接通
    public static final String HAND_OFF = "com.tongen.action.handle.off";//手柄放下
    public static final String HAND_ON = "com.tongen.action.handle.on";//手柄抬起
    public static final String CALLING_MISSED = "com.tongen.Tel.INCOMING_MISSED";//来电未接广播
    public static final String INCOMMING_HAND_UP = "com.tongen.Tel.INCOMING_IDLE";//来电挂断
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
        hideBottomUIMenu();

        setContentView(R.layout.activity_calling);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        SPUtil.getInstance().saveBoolean("isShowCallingActivity", true);//保存当前是否打开通话界面
        phoneNum = getIntent().getStringExtra("phoneNum");
        isCalling = getIntent().getBooleanExtra("isCalling", false);
        isHandFree = getIntent().getBooleanExtra("isHandFree", false);
        setViewData();
//        registerReceivers();
    }

    //相关广播注册
    private void registerReceivers() {
        //通话接通广播
        callingConnectReciver = new CallingConnectReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CALLING_CONNECT);
        registerReceiver(callingConnectReciver, intentFilter);
    }

//    private void unRegisterReceivers() {
//        unregisterReceiver(callingConnectReciver);
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Intent event) {
        switch (event.getAction()) {
            case CALLING_CONNECT://通话接通
                tvStatus.setVisibility(View.VISIBLE);
                llBtnContainer.setVisibility(View.VISIBLE);
                callStatus = 1;
                setViewData();
                break;
            case HAND_OFF://手柄放下
                if (!isHandFree) {//手柄放下时不是免提状态才挂断电话
                    finish();
                }
//                else {//手柄放下时是免提状态,显示挂断按钮
//                    ivHandUp.setVisibility(View.VISIBLE);
//                }
                break;
            case HAND_ON://手柄抬起
                if (!isCalling && callStatus == 0) {//是来电呼叫中状态,拿起手柄=接听电话
                    tvStatus.setText("00:00:00");//重新开始计时
                    callingSecond = 0;
                    callStatus = 1;
                    callingRecordStatus = 1;//已接
                    setViewData();
                }
                if (isHandFree) {//当前是免提状态,切换为听筒状态
                    llHandFree.performClick();
                }
                break;
            case CALLING_MISSED://来电未接
                callingRecordStatus = 2;//未接来电
                String phoneNumber = event.getStringExtra("phoneNumber");
                //通知栏
                showNotification(phoneNumber);
                finish();
                break;
            case INCOMMING_HAND_UP://来电挂断(对方挂断)
                finish();
                break;
        }
    }

    private void setViewData() {
        if (TextUtils.isEmpty(phoneNum)) {
            return;
        }
        if (!isCalling) {//是来电
            if (callStatus == 0) {
                tvStatus.setVisibility(View.GONE);
                //联系人显示
                String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
                if (TextUtils.isEmpty(name)) {//没有该联系人
                    tvCallName.setVisibility(View.INVISIBLE);
                } else {
                    tvCallName.setVisibility(View.VISIBLE);
                    tvCallName.setText(name);
                }
                tvCallNum.setText(phoneNum);
                //软键盘
                llKeyBordContainer.setVisibility(View.INVISIBLE);

                llBtnContainer.setVisibility(View.GONE);
                ivAnswer.setVisibility(View.VISIBLE);//来电显示接听按钮
                llIvAnswer.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.VISIBLE);
                //联系人显示
                String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
                if (TextUtils.isEmpty(name)) {//没有该联系人
                    tvCallName.setVisibility(View.INVISIBLE);
                } else {
                    tvCallName.setVisibility(View.VISIBLE);
                    tvCallName.setText(name);
                }
                tvCallNum.setText(phoneNum);
                //软键盘
                llKeyBordContainer.setVisibility(View.INVISIBLE);
                llBtnContainer.setVisibility(View.VISIBLE);
                ivAnswer.setVisibility(View.GONE);//来电显示接听按钮
                llIvAnswer.setVisibility(View.GONE);
                ivHandUp.setVisibility(View.GONE);
                llIvHandUp.setVisibility(View.GONE);
            }

        } else {
            tvStatus.setVisibility(View.VISIBLE);
            //联系人显示
            String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
            if (TextUtils.isEmpty(name)) {//没有该联系人
                tvCallName.setVisibility(View.INVISIBLE);
            } else {
                tvCallName.setVisibility(View.VISIBLE);
                tvCallName.setText(name);
            }
            tvCallNum.setText(phoneNum);

            //软键盘
            llKeyBordContainer.setVisibility(View.INVISIBLE);
            llBtnContainer.setVisibility(View.VISIBLE);

            //挂断按钮
            if (isHandFree) {
                ivHandUp.setVisibility(View.VISIBLE);
                llIvHandUp.setVisibility(View.VISIBLE);
            } else {
                ivHandUp.setVisibility(View.GONE);
                llIvHandUp.setVisibility(View.GONE);
            }
            ivAnswer.setVisibility(View.GONE);
            llIvAnswer.setVisibility(View.GONE);
        }

        if (callingRunnable == null) {
            callingRunnable = new CallingRunnable();
            callingRunnable.run();
        }

        //录音
        llAudioRecord.setClickable(true);
        ivAudioRecord.setImageResource(R.drawable.audio_record_icon);
        tvAudioRecord.setText("录音");

        if (isHandFree) {
            ivHandFree.setImageResource(R.drawable.hand_free_close);
            ivHandUp.setVisibility(View.VISIBLE);
            llIvHandUp.setVisibility(View.VISIBLE);
        } else {
            //免提
            llHandFree.setClickable(true);
            ivHandFree.setImageResource(R.drawable.hands_free_icon);
            tvHandFree.setText("免提");
        }

//        if (callStatus == 0) {//呼叫中
//            if (isCalling) {//是主动呼叫
////                tvStatus.setText("呼叫中...");
////                //联系人显示
////                String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
////                if (TextUtils.isEmpty(name)) {//没有该联系人
////                    tvCallName.setVisibility(View.INVISIBLE);
////                } else {
////                    tvCallName.setVisibility(View.VISIBLE);
////                    tvCallName.setText(name);
////                }
////                //软键盘
////                tvCallNum.setText(phoneNum);
////                llKeyBordContainer.setVisibility(View.INVISIBLE);
////                //录音
////                llAudioRecord.setClickable(false);
////                ivAudioRecord.setImageResource(R.drawable.audio_record_unclick);
////                tvAudioRecord.setText("录音");
////                //免提
////                llHandFree.setClickable(false);
////                ivHandFree.setImageResource(R.drawable.hand_free_unclick);
////                tvHandFree.setText("免提");
////                //挂断按钮
////                if (isHandFree) {
////                    ivHandUp.setVisibility(View.VISIBLE);
////                } else {
////                    ivHandUp.setVisibility(View.GONE);
////                }
////                ivAnswer.setVisibility(View.GONE);
//            } else {//是来电
//                tvStatus.setVisibility(View.GONE);
//                //联系人显示
//                String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
//                if (TextUtils.isEmpty(name)) {//没有该联系人
//                    tvCallName.setVisibility(View.INVISIBLE);
//                } else {
//                    tvCallName.setVisibility(View.VISIBLE);
//                    tvCallName.setText(name);
//                }
//                tvCallNum.setText(phoneNum);
//                //软键盘
//                llKeyBordContainer.setVisibility(View.INVISIBLE);
//
//                llBtnContainer.setVisibility(View.GONE);
//                ivAnswer.setVisibility(View.VISIBLE);//来电显示接听按钮
//            }
//        } else {//通话中
//            tvStatus.setVisibility(View.VISIBLE);
//            //联系人显示
//            String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNum);
//            if (TextUtils.isEmpty(name)) {//没有该联系人
//                tvCallName.setVisibility(View.INVISIBLE);
//            } else {
//                tvCallName.setVisibility(View.VISIBLE);
//                tvCallName.setText(name);
//            }
//
//            //软键盘
//            llKeyBordContainer.setVisibility(View.INVISIBLE);
//            llBtnContainer.setVisibility(View.VISIBLE);
//
//            //挂断按钮
//            if (isHandFree) {
//                ivHandUp.setVisibility(View.VISIBLE);
//            } else {
//                ivHandUp.setVisibility(View.GONE);
//            }
//            ivAnswer.setVisibility(View.GONE);
//
//            if (callingRunnable == null) {
//                callingRunnable = new CallingRunnable();
//                callingRunnable.run();
//            }
//
//            //录音
//            llAudioRecord.setClickable(true);
//            ivAudioRecord.setImageResource(R.drawable.audio_record_icon);
//            tvAudioRecord.setText("录音");
//
//            if(isHandFree) {
//                ivHandFree.setImageResource(R.drawable.hand_free_close);
//                ivHandUp.setVisibility(View.VISIBLE);
//            }else {
//                //免提
//                llHandFree.setClickable(true);
//                ivHandFree.setImageResource(R.drawable.hands_free_icon);
//                tvHandFree.setText("免提");
//            }
//        }
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

    @Override
    public void onBackPressed() {

    }

//    /**
//     * 隐藏虚拟按键，并且全屏
//     */
//    protected void hideBottomUIMenu(){
//        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
////          | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            decorView.setSystemUiVisibility(uiOptions);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//    }

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

    @OnClick({R.id.tv_key1, R.id.tv_key2, R.id.tv_key3, R.id.tv_key4, R.id.tv_key5, R.id.tv_key6, R.id.tv_key7, R.id.tv_key8, R.id.tv_key9, R.id.tv_key_xing, R.id.tv_key0, R.id.tv_key_jin, R.id.ll_audio_record, R.id.ll_hand_free, R.id.iv_hand_up, R.id.ll_key_bord, R.id.iv_answer})
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
                if (isHandFree) {//之前是免提,关闭免提
                    int status = SPUtil.getInstance().getInteger(SPUtil.KEY_HAND_STATUS);
                    if (status == 0) {
                        Utils.Toast("请先拿起手柄");
                        return;
                    }
                    ivHandFree.setImageResource(R.drawable.hands_free_icon);
//                    CallUtil.handFreeControl(this, 0);
                    ivHandUp.setVisibility(View.GONE);
                    llIvHandUp.setVisibility(View.GONE);
                    CallUtil.handFreeControl(this, 0);//通知系统打开或关闭免提
                } else {//打开免提
                    ivHandFree.setImageResource(R.drawable.hand_free_close);
                    CallUtil.handFreeControl(this, 1);
                    int status = SPUtil.getInstance().getInteger(SPUtil.KEY_HAND_STATUS);
//                    if (status == 1) {//手柄抬起
//                        ivHandUp.setVisibility(View.GONE);
//                    } else {
                    ivHandUp.setVisibility(View.VISIBLE);
                    llIvHandUp.setVisibility(View.VISIBLE);
//                    }
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
            case R.id.iv_answer://来电免提接听按钮
                tvStatus.setText("00:00:00");
                callingSecond = 0;//接通再计时
                callingRecordStatus = 1;//已接来电
                CallUtil.incommingAnswerWithFree(this);
                isHandFree = true;//设为免提接听
                CallUtil.handFreeControl(this, 1);//通知系统打开免提
                callStatus = 1;//更换为通话中状态
                setViewData();
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

    //获取当前格式化时间
    private String getCurrentFormatTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {

            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }
    }


    private int Notification_ID = 0;

    /**
     * 未接电话通知栏
     *
     * @param phoneNumber
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(String phoneNumber) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // TODO Auto-generated method stub
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);//设置图标
        builder.setTicker("您有1条未接来电");//手机状态栏的提示
        builder.setContentTitle("您有1条未接来电");//设置标题
        //联系人显示
        String name = ContactsUtil.getContactNameByPhoneNumber(this, phoneNumber);
        if (TextUtils.isEmpty(name)) {//没有该联系人
            builder.setContentText("您有一条来自" + phoneNumber + "的来电");//设置通知内容
        } else {
            builder.setContentText("您有一条来自" + name + "的来电");//设置通知内容
        }
        builder.setWhen(System.currentTimeMillis());//设置通知时间
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("type", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);//点击后的意图
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯
        builder.setDefaults(Notification.DEFAULT_SOUND);//设置提示声音
        builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动
        Notification notification = builder.build();//4.1以上，以下要用getNotification()
        manager.notify(Notification_ID, notification);
    }


    /**
     * @param context
     * @param phoneNum
     * @param isCalling  是否是主动呼叫
     * @param isHandFree 是否是免提呼叫
     */
    public static void startActivity(Context context, String phoneNum, boolean isCalling, boolean isHandFree, boolean isFromReciver) {
        Intent intent = new Intent(context, CallingActivity.class);
        if (isFromReciver) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("isCalling", isCalling);
        intent.putExtra("isHandFree", isHandFree);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCallingStop = true;
        audioRecordHandler.removeCallbacksAndMessages(null);
        audioRecordHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
//        unRegisterReceivers();
        SPUtil.getInstance().saveBoolean("isShowCallingActivity", false);//保存当前是否打开通话界面
//        SPUtil.getInstance().saveBoolean(SPUtil.KEY_CALLING_WITH_TALKING, false);

        //保存通话记录 0为已拨,1为已接,2为未接,3为主动挂断(没有主动挂断)
        int status = callingRecordStatus;
        String phoneNumber = phoneNum;
        String name = tvCallName.getText().toString();//没有则为空
        String date = getCurrentFormatTime();
        String desc = "";
        if (status == 0) {
            desc = formatTimeS(callingSecond);
        } else if (status == 1) {
            desc = formatTimeS(callingSecond);
        } else if (status == 2) {
            desc = "响铃" + formatTimeS(callingSecond);
        }
        CallRecordBean callRecordBean = new CallRecordBean(null, status, phoneNumber, date, desc, name);
        DaoUtil.getCallRecordBeanDao().insert(callRecordBean);
        DaoUtil.closeDb();
        EvenCallRecordBean evenCallRecordBean = new EvenCallRecordBean(callRecordBean, true);
        EventBus.getDefault().post(evenCallRecordBean);
    }

    public static String formatTimeS(long seconds) {
        int temp = 0;
        StringBuffer sb = new StringBuffer();
        if (seconds > 3600) {
            temp = (int) (seconds / 3600);
            sb.append((seconds / 3600) < 10 ? "0" + temp + ":" : temp + ":");
            temp = (int) (seconds % 3600 / 60);
            changeSeconds(seconds, temp, sb);
        } else {
            temp = (int) (seconds % 3600 / 60);
            changeSeconds(seconds, temp, sb);
        }
        return sb.toString();
    }

    private static void changeSeconds(long seconds, int temp, StringBuffer sb) {
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        temp = (int) (seconds % 3600 % 60);
        sb.append((temp < 10) ? "0" + temp : "" + temp);
    }
}
