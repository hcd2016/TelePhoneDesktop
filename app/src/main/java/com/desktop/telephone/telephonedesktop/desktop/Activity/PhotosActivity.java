package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.view.ResizableImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 电子相册
 */
public class PhotosActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_delete_container)
    LinearLayout llDeleteContainer;
    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(R.mipmap.photos);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recycleView.setLayoutManager(gridLayoutManager);
        photosAdapter = new PhotosAdapter(list);
        recycleView.setAdapter(photosAdapter);
    }

    boolean isEditStatus = false;//是否选择状态

    @OnClick({R.id.iv_back, R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (isEditStatus) {
                    tvTitle.setText("所有相册");
                    isEditStatus = false;
                    photosAdapter.notifyDataSetChanged();
                    ivBack.setImageResource(R.mipmap.arrow_left_white);
                    llDeleteContainer.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;
        }
    }

    class PhotosAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
        List<Integer> selectorList = new ArrayList<>();//记录选中的图片

        public PhotosAdapter(@Nullable List<Integer> data) {
            super(R.layout.item_photos_show, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Integer item) {
            ResizableImageView iv_img = helper.getView(R.id.iv_img);
            ResizableImageView iv_bg = helper.getView(R.id.iv_bg);
            final ImageView iv_selector = helper.getView(R.id.iv_selector);
            iv_bg.setVisibility(View.GONE);
//            初始化状态
            if (isEditStatus) {
                iv_selector.setVisibility(View.VISIBLE);
            } else {
                iv_selector.setVisibility(View.GONE);
            }

            iv_img.setOnLongClickListener(new View.OnLongClickListener() {//长按进入编辑状态
                @Override
                public boolean onLongClick(View view) {
                    tvTitle.setText("未选择");
                    ivBack.setImageResource(R.mipmap.close_white_icon);
                    isEditStatus = true;
                    notifyDataSetChanged();
                    llDeleteContainer.setVisibility(View.VISIBLE);
                    return false;
                }
            });


        }
    }


//    /**
//     * 需要从数据库中获取的信息：
//     * BUCKET_DISPLAY_NAME  文件夹名称
//     * DATA  文件路径
//     */
//    private final String[] projection = new String[]{
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.Media.DATA};


}


/**
 * //     * 通过ContentResolver 从媒体数据库中读取图片信息
 * //
 */
//    Cursor cursor = getContentResolver().query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  //限制类型为图片
//            projection,
//            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
//            new String[]{"image/jpeg", "image/png"},  // 这里筛选了jpg和png格式的图片
//            MediaStore.Images.Media.DATE_ADDED); // 排序方式：按添加时间排序

