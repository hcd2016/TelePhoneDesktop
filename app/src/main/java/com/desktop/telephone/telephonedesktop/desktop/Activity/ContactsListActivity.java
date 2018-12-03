package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.util.PinYinUtils;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.TopSmoothScroller;
import com.desktop.telephone.telephonedesktop.view.WordsView;

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
    private List<String> list;
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        ButterKnife.bind(this);
        initView();
    }

    Handler handler = new Handler();


    /**
     * @param words 首字母
     */
    private void updateListView(String words) {
        for (int i = 0; i < list.size(); i++) {
            String headerWord = PinYinUtils.getPinyin(list.get(i)).substring(0,1);
            //将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(headerWord)) {
                //将列表选中哪一个
                TopSmoothScroller mScroller = new TopSmoothScroller(ContactsListActivity.this);
                mScroller.setTargetPosition(i);
                layoutManager.startSmoothScroll(mScroller);
                //找到开头的一个即可
                return;
            }
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
        list.add("张三");
        list.add("李四");
        list.add("王三");
        list.add("啊三");
        list.add("妈妈");
        list.add("爸爸");
        list.add("爷爷");
        list.add("把奶奶");
        list.add("外攻");
        list.add("外婆");
        list.add("各种");
        list.add("我");
        list.add("是");
        list.add("汉字哦");
        list.add("啊");
        list.add("黑");
        list.add("金");
        list.add("田");
        list.add("心");
        list.add("情");
        list.add("很");
        list.add("不");
        list.add("错");
        list.add("哦");
        list.add("是");
        list.add("吗");
        list.add("进");
        list.add("晚");
        list.add("打");
        list.add("老");
        list.add("虎");

        //对集合排序
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                //根据拼音进行排序
                return PinYinUtils.getPinyin(lhs).compareTo(PinYinUtils.getPinyin(rhs));
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
                wordsView.setTouchIndex(PinYinUtils.getPinyin(list.get(pastVisiblesItems)).substring(0, 1));

            }
        });
        recycleView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(list);
        recycleView.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(@Nullable List<String> data) {
            super(R.layout.item_contacts, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView tv_word = helper.getView(R.id.tv_word);
            helper.setText(R.id.tv_word, PinYinUtils.getPinyin(item).substring(0, 1));
            helper.setText(R.id.tv_name, item);
            //将相同字母开头的合并在一起
            if (helper.getLayoutPosition() == 0) {
                //第一个是一定显示的
                tv_word.setVisibility(View.VISIBLE);
            } else {
                //后一个与前一个对比,判断首字母是否相同，相同则隐藏
                String headerWord = getHeaderWord(getData().get(helper.getLayoutPosition() - 1));
                String word = getHeaderWord(item);
                if (word.equals(headerWord)) {
                    tv_word.setVisibility(View.GONE);
                } else {
                    tv_word.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public String getHeaderWord(String zhongwen) {
        return PinYinUtils.getPinyin(zhongwen).substring(0, 1);
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
