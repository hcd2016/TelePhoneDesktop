package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.desktop.telephone.telephonedesktop.util.ContactsUtil;

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
    private int callStatus = 0;//呼叫状态,0为呼叫中,1为通话中

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
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
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String phoneNum = getIntent().getStringExtra("phoneNum");
        if (TextUtils.isEmpty(phoneNum)) {
            return;
        }
        setViewData(phoneNum);
    }

    private void setViewData(String phoneNum) {
        if (callStatus == 0) {//呼叫中
            tvStatus.setText("呼叫中...");
            //联系人显示
            String name = ContactsUtil.getDisplayNameByNumber(this, phoneNum);
            if (TextUtils.isEmpty(name)) {//没有该联系人
                tvCallName.setVisibility(View.GONE);
            } else {
                tvCallName.setVisibility(View.VISIBLE);
                tvCallName.setText(name);
            }
            //软键盘
            tvCallNum.setText(phoneNum);
            llKeyBordContainer.setVisibility(View.GONE);
            //录音
            llAudioRecord.setClickable(false);
            ivAudioRecord.setImageResource(R.drawable.audio_record_unclick);
            tvAudioRecord.setText("录音");
            //免提
            llHandFree.setClickable(false);
            ivHandFree.setImageResource(R.drawable.hand_free_unclick);
            tvHandFree.setText("免提");
            //挂断按钮
            ivHandUp.setVisibility(View.GONE);
        } else {//通话中
            callingRunnable = new CallingRunnable();
            callingRunnable.run();
        }
    }

    private Handler callingHandler = new Handler();
    private int callingSecond = 0;//通话计时
    private CallingRunnable callingRunnable;

    //通话计时
    private class CallingRunnable implements Runnable {
        @Override
        public void run() {
            //递归调用本runable对象，实现每隔一秒一次执行任务
            callingHandler.postDelayed(this, 1000);
            callingSecond = callingSecond + 1;
            tvStatus.setText(formatLongToTimeStr(callingSecond));
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
            hours = minute/60;
            minute = minute % 60;
        }
        return (getTwoLength(hours)+":"+getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    @OnClick({R.id.tv_key1, R.id.tv_key2, R.id.tv_key3, R.id.tv_key4, R.id.tv_key5, R.id.tv_key6, R.id.tv_key7, R.id.tv_key8, R.id.tv_key9, R.id.tv_key_xing, R.id.tv_key0, R.id.tv_key_jin, R.id.ll_audio_record, R.id.ll_hand_free, R.id.iv_hand_up, R.id.ll_key_bord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_key1:
                break;
            case R.id.tv_key2:
                break;
            case R.id.tv_key3:
                break;
            case R.id.tv_key4:
                break;
            case R.id.tv_key5:
                break;
            case R.id.tv_key6:
                break;
            case R.id.tv_key7:
                break;
            case R.id.tv_key8:
                break;
            case R.id.tv_key9:
                break;
            case R.id.tv_key_xing:
                break;
            case R.id.tv_key0:
                break;
            case R.id.tv_key_jin:
                break;
            case R.id.ll_audio_record:
                break;
            case R.id.ll_hand_free:
                break;
            case R.id.iv_hand_up:
                break;
            case R.id.ll_key_bord:
                break;
        }
    }

    public static void startActivity(Context context, String phoneNum) {
        Intent intent = new Intent(context, CallingActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        context.startActivity(intent);
    }
}
