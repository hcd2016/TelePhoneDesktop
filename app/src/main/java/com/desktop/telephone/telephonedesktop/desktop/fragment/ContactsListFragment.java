package com.desktop.telephone.telephonedesktop.desktop.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.AddContactsActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.ContactsDetailActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.ContactsListActivity;
import com.desktop.telephone.telephonedesktop.desktop.bluetooth.LeadHintActivity;
import com.desktop.telephone.telephonedesktop.desktop.bluetooth.android.bluetooth.client.pbap.BluetoothPbapClient;
import com.desktop.telephone.telephonedesktop.desktop.bluetooth.android.vcard.VCardEntry;
import com.desktop.telephone.telephonedesktop.desktop.dialog.ContactsDeleteDialog;
import com.desktop.telephone.telephonedesktop.desktop.dialog.ProgressBarDialog;
import com.desktop.telephone.telephonedesktop.util.ContactsUtil;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.DensityUtil;
import com.desktop.telephone.telephonedesktop.util.PinYinUtils;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.TopSmoothScroller;
import com.desktop.telephone.telephonedesktop.view.WordsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactsListFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.wordsView)
    WordsView wordsView;
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.ll_menu_container)
    LinearLayout llMenuContainer;
    @BindView(R.id.ll_btn_add_container)
    LinearLayout llBtnAddContainer;
    @BindView(R.id.ll_container)
    RelativeLayout llContainer;
    private List<ContactsBean> list;
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private AlertDialog alertDialog;
    private BluetoothPbapClient bluetoothPbapClient;
    private ProgressBarDialog progressBarDilog;
    private AlertDialog alertDialog1;
    private PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_contacts_list, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    Handler handler = new Handler();


    /**
     * @param words 首字母
     */
    private void updateListView(String words) {
        for (int i = 0; i < list.size(); i++) {
            String headerWord = PinYinUtils.getPinyin(list.get(i).getName()).substring(0, 1).toUpperCase();
            if (!headerWord.matches("[A-Z]")) {
                headerWord = "#";
            }
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(headerWord.toUpperCase())) {
                //将列表选中哪一个
                TopSmoothScroller mScroller = new TopSmoothScroller(getActivity());
                mScroller.setTargetPosition(i);
                layoutManager.startSmoothScroll(mScroller);
                //找到开头的一个即可
                return;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {//添加or修改成功
        if (event.getEvent().equals(EventBean.CONTACTS_ADD_SUCCESS)) {
            list.clear();
//            List<ContactsBean> contactsBeans = DaoUtil.getContactsBeanDao().loadAll();
            List<ContactsBean> contactsBeans = ContactsUtil.getContactsName(getActivity());
//            对集合排序
            Collections.sort(contactsBeans, new Comparator<ContactsBean>() {
                @Override
                public int compare(ContactsBean lhs, ContactsBean rhs) {
                    //根据拼音进行排序
                    return PinYinUtils.getPinyin(lhs.getName().toUpperCase()).compareTo(PinYinUtils.getPinyin(rhs.getName()).toUpperCase());
                }
            });
            list.addAll(contactsBeans);
            myAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        wordsView.setOnWordsChangeListener(new WordsView.OnWordsChangeListener() {
            @Override
            public void wordsChange(String words) {
                tvWord.setText(words);
                tvWord.setVisibility(View.VISIBLE);
                //清空之前的所有消息
                handler.removeCallbacksAndMessages(null);
                //500ms后让tv隐藏
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvWord.setVisibility(View.GONE);
                    }
                }, 1000);
                updateListView(words);
            }
        });
        list = new ArrayList<>();
        list = ContactsUtil.getContactsName(getActivity());
        //对集合排序
        Collections.sort(list, new Comparator<ContactsBean>() {
            @Override
            public int compare(ContactsBean lhs, ContactsBean rhs) {
                //根据拼音进行排序
                return PinYinUtils.getPinyin(lhs.getName().toUpperCase()).compareTo(PinYinUtils.getPinyin(rhs.getName().toUpperCase()));
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //当滑动列表的时候，更新右侧字母列表的选中状态
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                if (list.size() != 0 && pastVisiblesItems >= 0) {
                    String s = PinYinUtils.getPinyin(list.get(pastVisiblesItems).getName()).substring(0, 1).toUpperCase();
                    if (!s.matches("[A-Z]")) {
                        s = "#";
                    }
                    wordsView.setTouchIndex(s);
                }

            }
        });
        recycleView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(list);
        recycleView.setAdapter(myAdapter);
    }


    private ContactsDeleteDialog deleteDialog;

    class MyAdapter extends BaseQuickAdapter<ContactsBean, BaseViewHolder> {

        public MyAdapter(@Nullable List<ContactsBean> data) {
            super(R.layout.item_contacts, data);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void convert(final BaseViewHolder helper, final ContactsBean item) {
            TextView tv_word = helper.getView(R.id.tv_word);
            String name = item.getName();
            String firstWord = PinYinUtils.getPinyin(name).substring(0, 1).toUpperCase();//小写转大写
            if (firstWord.matches("[A-Z]")) {//是A到Z
                helper.setText(R.id.tv_word, firstWord);
            } else {
                helper.setText(R.id.tv_word, "#");
            }
            helper.setText(R.id.tv_name, name);
            //将相同字母开头的合并在一起
            if (helper.getLayoutPosition() == 0) {
                //第一个是一定显示的
                tv_word.setVisibility(View.VISIBLE);
            } else {
                //后一个与前一个对比,判断首字母是否相同，相同则隐藏
                String headerWord = getHeaderWord(getData().get(helper.getLayoutPosition() - 1).getName());
                String word = getHeaderWord(name);
                //首字母不区分大小写,非A-Z当#一类处理
                String s1 = headerWord.toUpperCase();
                String s = word.toUpperCase();
                if (!s1.matches("[A-Z]")) {
                    s1 = "#";
                }
                if (!s.matches("[A-Z]")) {
                    s = "#";
                }
                if (s.equals(s1)) {
                    tv_word.setVisibility(View.INVISIBLE);
                } else {
                    tv_word.setVisibility(View.VISIBLE);
                }
            }

            TextView tv_header_icon = helper.getView(R.id.tv_header_icon);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(Utils.getColorBgFromPosition(helper.getLayoutPosition()));

            char last = name.charAt(name.length() - 1);//最后一个字符
            if (Utils.isChineseA(last)) {
                tv_header_icon.setText(last + "");
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv_header_icon.getLayoutParams();
                layoutParams.width = DensityUtil.dip2px(getActivity(), 54);
                layoutParams.height = DensityUtil.dip2px(getActivity(), 54);
                layoutParams.setMargins(5,0,0,0);
                tv_header_icon.setLayoutParams(layoutParams);
                tv_header_icon.setBackground(drawable);
            } else {
                tv_header_icon.setText("");
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv_header_icon.getLayoutParams();
                layoutParams.width = DensityUtil.dip2px(getActivity(), 60);
                layoutParams.height = DensityUtil.dip2px(getActivity(), 60);
                layoutParams.setMargins(0,0,0,0);
                tv_header_icon.setLayoutParams(layoutParams);
                tv_header_icon.setBackgroundResource(R.drawable.iv_avatar_default);
            }

            helper.getView(R.id.tv_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("constacts_bean", item);
                    Intent intent = new Intent(getActivity(), ContactsDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    startActivity(ContactsDetailActivity.class, bundle);
                }
            });
            helper.getView(R.id.tv_name).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {//删除
                    ContactsDeleteDialog contactsDeleteDialog = new ContactsDeleteDialog(getActivity());
                    contactsDeleteDialog.setBtnClickListener(new ContactsDeleteDialog.BtnClickListener() {
                        @Override
                        public void onKeyCallClick() {//生成一键拨号
                            alertDialog = new AlertDialog.Builder(getActivity())
                                    .setMessage("确定要将该联系人生成桌面一键拨号吗?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ContactsBean contactsBean = ContactsUtil.getDetailFromContactID(getActivity(), item);
                                            //包名存id用来区分同名
                                            AppInfoBean appInfoBean = new AppInfoBean(null,0,contactsBean.getName(),null,item.id+"",0,false,false,false,3,contactsBean.getPhone());
                                            EventBus.getDefault().post(appInfoBean);
                                            Utils.Toast("生成成功");
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .create();
                            alertDialog.show();
                        }

                        @Override
                        public void deleteClick() {//删除
                            alertDialog = new AlertDialog.Builder(getActivity())
                                    .setMessage("确定要删除该联系人吗?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ContactsUtil.delete(list.get(helper.getLayoutPosition()).getId());
//                                    DaoUtil.getContactsBeanDao().delete(list.get(helper.getLayoutPosition()));
                                            list.remove(helper.getLayoutPosition());
                                            notifyDataSetChanged();
                                            Utils.Toast("删除成功");
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .create();
                            alertDialog.show();
                        }
                    });
                    contactsDeleteDialog.show();
                    return false;
                }
            });
        }
    }

    public String getHeaderWord(String zhongwen) {
        return PinYinUtils.getPinyin(zhongwen).substring(0, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.ll_btn_add_container, R.id.ll_menu_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.iv_back:
//                getActivity().finish();
//                break;
            case R.id.ll_btn_add_container://新建联系人
                startActivity(new Intent(getActivity(), AddContactsActivity.class));
                break;
            case R.id.ll_menu_container://菜单
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    View popView = View.inflate(getActivity(), R.layout.pop_contacts_menu, null);
                    popView.findViewById(R.id.tv_daoru).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//导入
                            startActivity(new Intent(getActivity(), LeadHintActivity.class));
                            popupWindow.dismiss();
                        }
                    });
                    popView.findViewById(R.id.tv_daochu).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//导出
                            alertDialog1 = new AlertDialog.Builder(getActivity())
                                    .setTitle("导出到存储设备")
                                    .setMessage("是否将联系人列表导出至 " + Environment.getExternalStorageDirectory().getPath() + "/contacts.vcf?" + "导出后,请妥善保管您的联系人信息。")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog1.dismiss();
                                        }
                                    })
                                    .setPositiveButton("导出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            try {
                                                exportContacts();
                                            } catch (Exception e) {
                                                Utils.Toast("导出失败,请稍后重试");
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .create();
                            alertDialog1.show();
                            popupWindow.dismiss();
                        }
                    });
                    popupWindow = new PopupWindow(popView, DensityUtil.dip2px(getActivity(), 160), DensityUtil.dip2px(getActivity(), 100));
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());

                    int[] ints = new int[2];
                    llMenuContainer.getLocationInWindow(ints);
                    int x = ints[0];
                    int y = ints[1];
                    popupWindow.showAtLocation(llMenuContainer, Gravity.NO_GRAVITY, x, y - popupWindow.getHeight() -10);
                    break;
                }
        }
    }

    /**
     * Exporting contacts from the phone
     */
    public void exportContacts() throws Exception {
        String path = Environment.getExternalStorageDirectory().getPath() + "/contacts.vcf";

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int index = cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
        FileOutputStream fout = new FileOutputStream(path);
        byte[] data = new byte[1024 * 1];
        while (cur.moveToNext()) {
            String lookupKey = cur.getString(index);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
            AssetFileDescriptor fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");
            FileInputStream fin = fd.createInputStream();
            int len = -1;
            while ((len = fin.read(data)) != -1) {
                fout.write(data, 0, len);
            }
            fin.close();
        }
        fout.close();
        Utils.Toast("导出成功");
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
                        for (int i = 0; i < vCardEntryList.size(); i++) {
                            String name = "";
                            String number = "";
                            String email = "";
                            List<VCardEntry.PhoneData> phoneList = vCardEntryList.get(i).getPhoneList();
                            if (phoneList != null) {
                                number = phoneList.get(0).getNumber();
                                if (number.contains("-")) {
                                    number.replace("-", "");
                                }
                                if (number.contains(" ")) {
                                    number.replace(" ", "");
                                }
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
                            ContentResolver resolver = getActivity().getContentResolver();
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

                        //刷新页面数据
                        list.clear();
                        List<ContactsBean> contactsBeans = ContactsUtil.getContactsName(getActivity());
                        //            对集合排序
                        Collections.sort(contactsBeans, new Comparator<ContactsBean>() {
                            @Override
                            public int compare(ContactsBean lhs, ContactsBean rhs) {
                                //根据拼音进行排序
                                return PinYinUtils.getPinyin(lhs.getName().toUpperCase()).compareTo(PinYinUtils.getPinyin(rhs.getName()).toUpperCase());
                            }
                        });
                        list.addAll(contactsBeans);
                        myAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    public static ContactsListFragment newInstance() {
//        Bundle argz = new Bundle();
        ContactsListFragment fragment = new ContactsListFragment();
//        fragment.setArguments(argz);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
