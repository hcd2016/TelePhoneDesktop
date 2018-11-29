package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.PhotoInfoBean;
import com.desktop.telephone.telephonedesktop.util.Utils;
import com.desktop.telephone.telephonedesktop.view.ResizableImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BannerSettingActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_btn_save)
    TextView tvBtnSave;
    @BindView(R.id.switch_off)
    Switch switchOff;
    @BindView(R.id.et_banner_begin_time)
    EditText etBannerBeginTime;
    @BindView(R.id.et_banner_speed)
    EditText etBannerSpeed;
    @BindView(R.id.ll_close_container)
    LinearLayout llCloseContainer;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rl_title_container)
    RelativeLayout rlTitleContainer;
    @BindView(R.id.iv_selector_title)
    ImageView ivSelectorTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private BannerSetingsAdapter bannerSetingsAdapter;
    private List<PhotoInfoBean> allPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        allPhotos = getAllPhotos();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recycleView.setLayoutManager(gridLayoutManager);
        bannerSetingsAdapter = new BannerSetingsAdapter(allPhotos);
        View empty_view = View.inflate(this, R.layout.empty_view, null);
        bannerSetingsAdapter.setEmptyView(empty_view);
        recycleView.setAdapter(bannerSetingsAdapter);

        PhotosViewPagerAdapter photosViewPagerAdapter = new PhotosViewPagerAdapter(allPhotos);
        viewpager.setAdapter(photosViewPagerAdapter);
    }

    int perverClick = 0;
    @OnClick({R.id.iv_back, R.id.tv_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (status == 1) {
                    tvTitle.setText("所有相册");
                    status = 0;
                    bannerSetingsAdapter.selectorList.clear();//清除选中状态
                    ivBack.setImageResource(R.mipmap.arrow_left_white);
                    bannerSetingsAdapter.notifyDataSetChanged();
                } else if (status == 2) {//返回列表界面
                    if (perverClick == 0) {//之前是非编辑状态
                        status = 0;
                        ivBack.setImageResource(R.mipmap.arrow_left_white);
                    } else {
                        ivBack.setImageResource(R.mipmap.close_white_icon);
                        status = 1;
                    }
                    viewpager.setVisibility(View.GONE);
                    ivSelectorTitle.setVisibility(View.GONE);
                    rlTitleContainer.setBackgroundResource(R.color.colorPrimaryDark);
                    rlTitleContainer.setVisibility(View.VISIBLE);
                    bannerSetingsAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
                break;
            case R.id.tv_btn_save:
                break;
        }
    }

    int status = 0;
    /**
     * 获取系统所有图片
     */
    public List<PhotoInfoBean> getAllPhotos() {
        List<PhotoInfoBean> list = new ArrayList<>();
        Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的名称
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的生成日期
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取图片的详细信息
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));

            PhotoInfoBean photoInfoBean = new PhotoInfoBean();
            photoInfoBean.setName(name);
            photoInfoBean.setDesc(desc);
            photoInfoBean.setFileNmae(new String(data, 0, data.length - 1));
            list.add(photoInfoBean);
        }
        return list;
    }


    class BannerSetingsAdapter extends BaseQuickAdapter<PhotoInfoBean, BaseViewHolder> {
        List<Integer> selectorList = new ArrayList<>();//记录选中的图片

        public BannerSetingsAdapter(@Nullable List<PhotoInfoBean> data) {
            super(R.layout.item_photos_show, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final PhotoInfoBean item) {
            final ResizableImageView iv_img = helper.getView(R.id.iv_img);
            final ResizableImageView iv_bg = helper.getView(R.id.iv_bg);
            final ImageView iv_selector = helper.getView(R.id.iv_selector);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(BannerSettingActivity.this).
                    load(item.fileNmae)
                    .apply(options)
                    .into(iv_img);
            iv_bg.setVisibility(View.GONE);
//            初始化状态
            if (status == 1) {
                iv_selector.setVisibility(View.VISIBLE);
            } else {
                iv_selector.setVisibility(View.GONE);
            }
            final int position = helper.getLayoutPosition();
            if (selectorList.contains(position)) {
                iv_selector.setImageResource(R.mipmap.selector_icon);
                iv_bg.setVisibility(View.VISIBLE);
            } else {
                iv_selector.setImageResource(R.mipmap.unselector_icon);
                iv_bg.setVisibility(View.GONE);
            }
            RelativeLayout rl_item_container = helper.getView(R.id.rl_item_container);
//
            rl_item_container.setOnClickListener(new View.OnClickListener() {//照片点击事件
                @Override
                public void onClick(View view) {
                    viewpager.setCurrentItem(position);
                    viewpager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 1) {
                                rlTitleContainer.setVisibility(View.VISIBLE);
                            } else {
                                rlTitleContainer.setVisibility(View.GONE);
                            }
                            viewpager.setVisibility(View.VISIBLE);
                            ivSelectorTitle.setVisibility(View.VISIBLE);
                            rlTitleContainer.setBackgroundResource(R.color.black);
                            if (selectorList.contains(position)) {
                                ivSelectorTitle.setImageResource(R.mipmap.selector_icon);
                            } else {
                                ivSelectorTitle.setImageResource(R.mipmap.unselector_icon);
                            }
                            status = 2;
                        }
                    }, 350);
                }
            });

            rl_item_container.setOnLongClickListener(new View.OnLongClickListener() {//长按进入编辑状态
                @Override
                public boolean onLongClick(View view) {
                    tvTitle.setText("未选择");
                    ivBack.setImageResource(R.mipmap.close_white_icon);
                    status = 1;
                    perverClick = status;
                    notifyDataSetChanged();
                    return false;
                }
            });

            iv_selector.setOnClickListener(new View.OnClickListener() {//选择图片点击
                @Override
                public void onClick(View view) {//勾选框点击
                    if (selectorList.contains(position)) {//之前是已选中
                        selectorList.remove(new Integer(position));//注意,要删除的是值是position的项,而不是角标position的项
                    } else {
                        selectorList.add(position);//记录选中position
                    }
                    if (selectorList.size() > 0) {
                        tvTitle.setText("已选择 (" + selectorList.size() + ")");
                    } else {
                        tvTitle.setText("未选择");
                    }
//                    if (selectorList.size() != allPhotos.size()) {//只要不是全选中,按钮设置文案为全选
//                        tvAllSelect.setText("全选");
//                        ivAllSelect.setImageResource(R.mipmap.all_select_normal);
//                    }
//                    if (selectorList.size() > 0) {//有选中
//                        llBtnDelete.setClickable(true);
//                        tvDelete.setTextColor(Utils.getColor(R.color.text_333333));
//                        ivDelete.setImageResource(R.mipmap.delete_click_icon);
//                    } else {
//                        llBtnDelete.setClickable(false);
//                        tvDelete.setTextColor(Utils.getColor(R.color.text_999999));
//                        ivDelete.setImageResource(R.mipmap.delete_unclick_icon);
//                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    class PhotosViewPagerAdapter extends PagerAdapter {
        List<PhotoInfoBean> list;

        public PhotosViewPagerAdapter(List<PhotoInfoBean> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = View.inflate(BannerSettingActivity.this, R.layout.item_photos_viewpager, null);
            ImageView iv_img = view.findViewById(R.id.iv_img);
//            iv_img.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getFileNmae()));
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(BannerSettingActivity.this).
                    load(list.get(position).fileNmae)
                    .apply(options)
                    .into(iv_img);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
