package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.EventBean;
import com.desktop.telephone.telephonedesktop.bean.EventBlacklistInfoBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.PinYinUtils;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.TopSmoothScroller;
import com.desktop.telephone.telephonedesktop.view.WordsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通讯录
 */
public class ContactsListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.wordsView)
    WordsView wordsView;
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.ll_btn_add_container)
    LinearLayout llBtnAddContainer;
    private List<ContactsBean> list;
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    Handler handler = new Handler();


    /**
     * @param words 首字母
     */
    private void updateListView(String words) {
        for (int i = 0; i < list.size(); i++) {
            String headerWord = PinYinUtils.getPinyin(list.get(i).getName()).substring(0, 1).toUpperCase();
            if(!headerWord.matches("[A-Z]")) {
                headerWord = "#";
            }
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(headerWord.toUpperCase())) {
                //将列表选中哪一个
                TopSmoothScroller mScroller = new TopSmoothScroller(ContactsListActivity.this);
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
            List<ContactsBean> contactsBeans = DaoUtil.getContactsBeanDao().loadAll();
//            对集合排序
            Collections.sort(contactsBeans, new Comparator<ContactsBean>() {
                @Override
                public int compare(ContactsBean lhs, ContactsBean rhs) {
                    //根据拼音进行排序
                    return PinYinUtils.getPinyin(lhs.getName()).compareTo(PinYinUtils.getPinyin(rhs.getName()));
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
        list = DaoUtil.getContactsBeanDao().loadAll();
        //对集合排序
        Collections.sort(list, new Comparator<ContactsBean>() {
            @Override
            public int compare(ContactsBean lhs, ContactsBean rhs) {
                //根据拼音进行排序
                return PinYinUtils.getPinyin(lhs.getName()).compareTo(PinYinUtils.getPinyin(rhs.getName()));
            }
        });
        layoutManager = new LinearLayoutManager(this);
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
                    if(!s.matches("[A-Z]")) {
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

    class MyAdapter extends BaseQuickAdapter<ContactsBean, BaseViewHolder> {

        public MyAdapter(@Nullable List<ContactsBean> data) {
            super(R.layout.item_contacts, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final ContactsBean item) {
            TextView tv_word = helper.getView(R.id.tv_word);
            String firstWord = PinYinUtils.getPinyin(item.getName()).substring(0, 1).toUpperCase();//小写转大写
            if (firstWord.matches("[A-Z]")) {//是A到Z
                helper.setText(R.id.tv_word, firstWord);
            } else {
                helper.setText(R.id.tv_word, "#");
            }
            helper.setText(R.id.tv_name, item.getName());
            //将相同字母开头的合并在一起
            if (helper.getLayoutPosition() == 0) {
                //第一个是一定显示的
                tv_word.setVisibility(View.VISIBLE);
            } else {
                //后一个与前一个对比,判断首字母是否相同，相同则隐藏
                String headerWord = getHeaderWord(getData().get(helper.getLayoutPosition() - 1).getName());
                String word = getHeaderWord(item.getName());
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
                    tv_word.setVisibility(View.GONE);
                } else {
                    tv_word.setVisibility(View.VISIBLE);
                }
            }
            helper.getView(R.id.tv_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("constacts_bean", item);
                    startActivity(ContactsDetailActivity.class, bundle);
                }
            });
            helper.getView(R.id.tv_name).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {//删除
                    alertDialog = new AlertDialog.Builder(ContactsListActivity.this)
                            .setMessage("确定要删除该联系人吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DaoUtil.getContactsBeanDao().delete(list.get(helper.getLayoutPosition()));
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
                    return false;
                }
            });
        }
    }

    public String getHeaderWord(String zhongwen) {
        return PinYinUtils.getPinyin(zhongwen).substring(0, 1);
    }

    @OnClick({R.id.iv_back, R.id.ll_btn_add_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_btn_add_container://新建联系人
                startActivity(AddContactsActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DaoUtil.closeDb();
    }
}
