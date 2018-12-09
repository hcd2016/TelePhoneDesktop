package com.desktop.telephone.telephonedesktop.desktop.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.ContactsListActivity;
import com.desktop.telephone.telephonedesktop.desktop.bluetooth.android.bluetooth.client.pbap.BluetoothPbapClient;
import com.desktop.telephone.telephonedesktop.desktop.bluetooth.android.vcard.VCardEntry;
import com.desktop.telephone.telephonedesktop.desktop.dialog.ProgressBarDialog;
import com.desktop.telephone.telephonedesktop.util.ContactsUtil;
import com.desktop.telephone.telephonedesktop.util.PinYinUtils;
import com.desktop.telephone.telephonedesktop.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChiosePairedActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private BluetoothPbapClient bluetoothPbapClient;
    private ProgressBarDialog progressBarDilog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_paired);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        List<BluetoothDevice> list = new ArrayList<>();

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                list.add(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(list);
        View view = View.inflate(this, R.layout.empty_view, null);
        myAdapter.setEmptyView(view);
        recycleView.setAdapter(myAdapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_DONE:
                    if (msg.obj != null) {
                        Utils.Toast("handler");
                        bluetoothPbapClient.disconnect();
                        List<VCardEntry> vCardEntryList = (List<VCardEntry>) msg.obj;//获取蓝牙通讯录插入到数据库
                        List<ContactsBean> allPhoneContacts = ContactsUtil.getAllPhoneContacts();
                        for (int i = 0; i < allPhoneContacts.size(); i++) {//去重
                            Iterator<VCardEntry> it = vCardEntryList.iterator();
                            while (it.hasNext()) {
                                VCardEntry next = it.next();
                                String name = "";
                                String number = "";
                                VCardEntry.NameData nameData = next.getNameData();
                                if (nameData != null) {
                                    name = nameData.displayName;
                                }
                                List<VCardEntry.PhoneData> phoneList = next.getPhoneList();
                                if (phoneList != null) {
                                    number = phoneList.get(0).getNumber();
                                    // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
                                    number = number.replaceAll("^(\\+86)", "");
                                    number = number.replaceAll("^(86)", "");
                                    number = number.replaceAll("-", "");
                                    number = number.replaceAll(" ", "");
                                    number = number.trim();
                                }
                                //如果已存在同样的姓名和电话号码,去除不再添加
                                if (name.equals(allPhoneContacts.get(i).getName()) && number.equals(allPhoneContacts.get(i).getPhone())) {
                                    it.remove();
                                }
                            }
                        }

                        //插入数据库
                        for (int i = 0; i < vCardEntryList.size(); i++) {
                            String name = "";
                            String number = "";
                            String email = "";
                            List<VCardEntry.PhoneData> phoneList = vCardEntryList.get(i).getPhoneList();
                            if (phoneList != null) {
                                number = phoneList.get(0).getNumber();
                                // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
                                number = number.replaceAll("^(\\+86)", "");
                                number = number.replaceAll("^(86)", "");
                                number = number.replaceAll("-", "");
                                number = number.replaceAll(" ", "");
                                number = number.trim();
                            }
                            VCardEntry.NameData nameData = vCardEntryList.get(i).getNameData();
                            if (nameData != null) {
                                name = nameData.displayName;
                            }
                            List<VCardEntry.EmailData> emailList = vCardEntryList.get(i).getEmailList();
                            if (emailList != null) {
                                email = emailList.get(0).getAddress();
                            }

                            //插入raw_contacts表，并获取_id属性
                            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                            ContentResolver resolver = getContentResolver();
                            ContentValues values = new ContentValues();
                            long contact_id = ContentUris.parseId(resolver.insert(uri, values));
                            //插入data表
                            uri = Uri.parse("content://com.android.contacts/data");
                            //add Name
                            values.put("raw_contact_id", contact_id);
                            values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/name");
                            values.put("data1", name);
                            resolver.insert(uri, values);
                            values.clear();
                            //add Phone
                            values.put("raw_contact_id", contact_id);
                            values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
                            values.put("data2", "2");   //手机
                            values.put("data1", number);
                            resolver.insert(uri, values);
                            values.clear();
                            //add email
                            values.put("raw_contact_id", contact_id);
                            values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
                            values.put("data2", "2");   //单位
                            values.put("data1", email);
                            resolver.insert(uri, values);
                        }
                        progressBarDilog.dismiss();
                        Utils.Toast("导入成功!");
                        EventBus.getDefault().post(new EventBean(EventBean.CONTACTS_ADD_SUCCESS));
                    }
                    break;
            }
        }
    };


    class MyAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

        public MyAdapter(@Nullable List<BluetoothDevice> data) {
            super(R.layout.item_choise_paired, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final BluetoothDevice item) {
            helper.setText(R.id.tv_name, item.getName());
//            helper.setText(R.id.tv_desc, item.getBondState());

            helper.getView(R.id.ll_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBarDilog = new ProgressBarDialog(ChiosePairedActivity.this);
                    progressBarDilog.setCancelable(false);
                    progressBarDilog.show();
                    bluetoothPbapClient = new BluetoothPbapClient(item, mHandler);
                    bluetoothPbapClient.connect();
                    boolean isLeadSucess = bluetoothPbapClient.pullPhoneBook(BluetoothPbapClient.PB_PATH);
                    if(!isLeadSucess ) {
                        progressBarDilog.dismiss();
                        Utils.Toast("导入失败,请稍后重新尝试");
                    }
//                    bluetoothPbapClient.disconnect();
                }
            });
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
