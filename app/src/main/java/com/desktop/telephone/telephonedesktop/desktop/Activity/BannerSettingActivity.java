package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.PhotoInfoBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
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
    //    private List<PhotoInfoBean> allPhotos;
    private List<PhotoInfoBean> bannerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        bannerList = DaoUtil.getPhotoInfoBeanDao().loadAll();
        boolean isOpenBanner = SPUtil.getInstance().getBoolean(SPUtil.KEY_IS_OPEN_BANNER,true);
        long bannerSpeed = SPUtil.getInstance().getLong(SPUtil.KEY_BANNER_SPEED);
        long bannerBeginTime = SPUtil.getInstance().getLong(SPUtil.KEY_BANNER_START_TIME);

        switchOff.setChecked(isOpenBanner);
        if(switchOff.isChecked()) {
            llCloseContainer.setVisibility(View.VISIBLE);
        }else {
            llCloseContainer.setVisibility(View.GONE);
        }
        switchOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    llCloseContainer.setVisibility(View.VISIBLE);
                }else {
                    llCloseContainer.setVisibility(View.GONE);
                }
            }
        });
        if(bannerSpeed != 0 && bannerBeginTime != 0) {
            etBannerSpeed.setText(bannerSpeed/1000+"");
            etBannerBeginTime.setText(bannerBeginTime/(1000*60)+"");
        }

        etBannerBeginTime.setSelection(etBannerBeginTime.getText().toString().length());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recycleView.setLayoutManager(gridLayoutManager);
        bannerSetingsAdapter = new BannerSetingsAdapter(bannerList);
        View empty_view = View.inflate(this, R.layout.empty_view, null);
        bannerSetingsAdapter.setEmptyView(empty_view);
        recycleView.setAdapter(bannerSetingsAdapter);

        PhotosViewPagerAdapter photosViewPagerAdapter = new PhotosViewPagerAdapter(bannerList);
        viewpager.setAdapter(photosViewPagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if(status == 2) {
                    if(bannerSetingsAdapter.selectorList.contains(i)) {
                        ivSelectorTitle.setImageResource(R.drawable.selector_icon);
                    }else {
                        ivSelectorTitle.setImageResource(R.drawable.unselector_icon);
                    }
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    int perverClick = 0;

    @OnClick({R.id.iv_back, R.id.tv_btn_save, R.id.btn_banner,R.id.iv_selector_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (status == 1) {
                    tvTitle.setText("轮播设置");
                    status = 0;
                    bannerSetingsAdapter.selectorList.clear();//清除选中状态
                    ivBack.setImageResource(R.drawable.arrow_left_white);
                    tvBtnSave.setText("保存");
                    tvBtnSave.setVisibility(View.VISIBLE);
                    ivSelectorTitle.setVisibility(View.GONE);
                    bannerSetingsAdapter.notifyDataSetChanged();
                } else if (status == 2) {//返回列表界面
                    if (perverClick == 0) {//之前是非编辑状态
                        status = 0;
                        ivBack.setImageResource(R.drawable.arrow_left_white);
                        tvBtnSave.setText("保存");
                        tvBtnSave.setVisibility(View.VISIBLE);
                        ivSelectorTitle.setVisibility(View.GONE);
                    } else {
                        ivBack.setImageResource(R.drawable.close_white_icon);
                        status = 1;
                        tvBtnSave.setText("移出轮播");
                        tvBtnSave.setVisibility(View.VISIBLE);
                        ivSelectorTitle.setVisibility(View.GONE);
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
            case R.id.tv_btn_save://保存 or 删除
                if (status == 0) {
                    tvBtnSave.setText("保存");
                    String bannerBeginTime = etBannerBeginTime.getText().toString();
                    String bannerSpeed = etBannerSpeed.getText().toString();
                    if (TextUtils.isEmpty(bannerBeginTime)) {
                        Utils.Toast("轮播开启时间不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(bannerSpeed)) {
                        Utils.Toast("轮播速度不能为空");
                        return;
                    }
                    if(bannerBeginTime.equals("0")) {
                        Utils.Toast("轮播开启时间不能为0");
                        return;
                    }
                    if(bannerSpeed.equals("0")) {
                        Utils.Toast("轮播速度不能为0");
                        return;
                    }
                    SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_OPEN_BANNER,switchOff.isChecked());
                    SPUtil.getInstance().saveLong(SPUtil.KEY_BANNER_START_TIME, Long.parseLong(bannerBeginTime) * 60 * 1000);
                    SPUtil.getInstance().saveLong(SPUtil.KEY_BANNER_SPEED, Long.parseLong(bannerSpeed) * 1000);
                    SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_BANNER_RUNING,false);
                    timeStart();//保存成功重新开启计时
                    Utils.Toast("保存成功");
                } else {
                    tvBtnSave.setText("移出轮播");
                    if(bannerSetingsAdapter.selectorList.size() == 0) {
                        Utils.Toast("请选择移出图片");
                        return;
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(this).
                            setTitle("确定要移出轮播吗?")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    List<PhotoInfoBean> deleteList = new ArrayList<>();
                                    for (int i = 0; i < bannerSetingsAdapter.selectorList.size(); i++) {//获取要删除的集合
                                        PhotoInfoBean photoInfoBean = bannerList.get(bannerSetingsAdapter.selectorList.get(i));
                                        DaoUtil.getPhotoInfoBeanDao().delete(photoInfoBean);
                                        deleteList.add(photoInfoBean);
                                    }
                                    for (PhotoInfoBean photoInfoBean : deleteList) {
                                        bannerList.remove(photoInfoBean);
                                    }
                                    bannerSetingsAdapter.selectorList.clear();
                                    bannerSetingsAdapter.notifyDataSetChanged();
                                    Utils.Toast("移出成功");

//                                    for (PhotoInfoBean photoInfoBean : deleteList) {
//                                        File file = new File(photoInfoBean.getFileName());
//                                        getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{photoInfoBean.getFileName()});//删除系统缩略图
//                                        file.delete();//删除SD中图片
//                                        allPhotos.remove(photoInfoBean);
//                                    }
//                                    photosAdapter.selectorList.clear();
//                                    Toast.makeText(PhotosActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
//                                    photosAdapter.notifyDataSetChanged();
//                                    if (allPhotos.size() == 0) {//已删完图片,退出编辑状态
//                                        ivBack.performClick();
//                                    }
                                }
                            })
                            .create();
                    alertDialog.show();
                }
                break;
            case R.id.btn_banner://banner预览
                if (bannerList == null || bannerList.size() == 0) {
                    Toast.makeText(this, "请从所有相册中先加入轮播图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!switchOff.isChecked()) {
                    Utils.Toast("请先开启轮播");
                    return;
                }
                if (TextUtils.isEmpty(etBannerSpeed.getText().toString())) {
                    Utils.Toast("轮播速度不能为空");
                    return;
                }
                Intent intent = new Intent(this,BannerActivity.class);
                intent.putExtra("is_preview",true);
                intent.putExtra(SPUtil.KEY_BANNER_SPEED, Long.parseLong(etBannerSpeed.getText().toString()) * 1000);
                startActivity(intent);
                break;
//            case R.id.switch_off://轮播开关
//                if (switchOff.isChecked()) {
//                    llCloseContainer.setVisibility(View.VISIBLE);
////                    SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_OPEN_BANNER,true);
//                } else {
//                    llCloseContainer.setVisibility(View.GONE);
////                    SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_OPEN_BANNER,false);
//                }
//                break;
            case R.id.iv_selector_title://
                int currentPosition = viewpager.getCurrentItem();
                if (bannerSetingsAdapter.selectorList.contains(currentPosition)) {//之前是已选中
                    bannerSetingsAdapter.selectorList.remove(new Integer(currentPosition));//注意,要删除的是值是position的项,而不是角标position的项
                    ivSelectorTitle.setImageResource(R.drawable.unselector_icon);
                } else {
                    bannerSetingsAdapter.selectorList.add(currentPosition);//记录选中position
                    ivSelectorTitle.setImageResource(R.drawable.selector_icon);
                }
                if (bannerSetingsAdapter.selectorList.size() > 0) {
                    tvTitle.setText("已选择 (" + bannerSetingsAdapter.selectorList.size() + ")");
                } else {
                    tvTitle.setText("未选择");
                }
                bannerSetingsAdapter.notifyDataSetChanged();
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
            photoInfoBean.setFileName(new String(data, 0, data.length - 1));
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
                    load(item.fileName)
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
                iv_selector.setImageResource(R.drawable.selector_icon);
                iv_bg.setVisibility(View.VISIBLE);
            } else {
                iv_selector.setImageResource(R.drawable.unselector_icon);
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
                                ivSelectorTitle.setImageResource(R.drawable.selector_icon);
                            } else {
                                ivSelectorTitle.setImageResource(R.drawable.unselector_icon);
                            }
                            status = 2;

                            tvBtnSave.setVisibility(View.GONE);
                            ivSelectorTitle.setVisibility(View.VISIBLE);
                        }
                    }, 350);
                }
            });

            rl_item_container.setOnLongClickListener(new View.OnLongClickListener() {//长按进入编辑状态
                @Override
                public boolean onLongClick(View view) {
                    tvTitle.setText("未选择");
                    ivBack.setImageResource(R.drawable.close_white_icon);
                    status = 1;
                    perverClick = status;

                    tvBtnSave.setText("移出轮播");
                    tvBtnSave.setVisibility(View.VISIBLE);
                    ivSelectorTitle.setVisibility(View.GONE);
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
//                        ivAllSelect.setImageResource(R.drawable.all_select_normal);
//                    }
//                    if (selectorList.size() > 0) {//有选中
//                        llBtnDelete.setClickable(true);
//                        tvDelete.setTextColor(Utils.getColor(R.color.text_333333));
//                        ivDelete.setImageResource(R.drawable.delete_click_icon);
//                    } else {
//                        llBtnDelete.setClickable(false);
//                        tvDelete.setTextColor(Utils.getColor(R.color.text_999999));
//                        ivDelete.setImageResource(R.drawable.delete_unclick_icon);
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
//            iv_img.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getFileName()));
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(BannerSettingActivity.this).
                    load(list.get(position).fileName)
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

    @Override
    public void onBackPressed() {
        ivBack.performClick();
    }
}
