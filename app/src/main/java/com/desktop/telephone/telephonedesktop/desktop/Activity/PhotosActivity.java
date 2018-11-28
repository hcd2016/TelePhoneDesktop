package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.desktop.telephone.telephonedesktop.view.ViewPagerScroller;

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
    @BindView(R.id.ll_btn_delete)
    LinearLayout llBtnDelete;
    @BindView(R.id.ll_btn_all_select)
    LinearLayout llBtnAllSelect;
    @BindView(R.id.tv_all_select)
    TextView tvAllSelect;
    @BindView(R.id.iv_all_select)
    ImageView ivAllSelect;
    @BindView(R.id.iv_selector_title)
    ImageView ivSelectorTitle;
    @BindView(R.id.rl_title_container)
    RelativeLayout rlTitleContainer;
    @BindView(R.id.frame_layout)
    RelativeLayout frameLayout;
    private PhotosAdapter photosAdapter;
    private List<Integer> list;
    private List<PhotoInfoBean> photoInfoBeanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        List<PhotoInfoBean> allPhotos = getAllPhotos();

//        list = new ArrayList<>();
//        for (int i = 0; i < allPhotos.size(); i++) {
//            list.add(R.mipmap.photos);
//        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recycleView.setLayoutManager(gridLayoutManager);
        photosAdapter = new PhotosAdapter(allPhotos);
        recycleView.setAdapter(photosAdapter);

        PhotosViewPagerAdapter photosViewPagerAdapter = new PhotosViewPagerAdapter(allPhotos);
        viewpager.setAdapter(photosViewPagerAdapter);
    }

    int status = 0;//0为非编辑状态,1为编辑状态,2为显示全屏照片状态

    @OnClick({R.id.iv_back, R.id.tv_title, R.id.ll_btn_delete, R.id.ll_btn_all_select, R.id.iv_selector_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (status == 1) {
                    tvTitle.setText("所有相册");
                    status = 0;
                    photosAdapter.selectorList.clear();//清除选中状态
                    ivBack.setImageResource(R.mipmap.arrow_left_white);
                    llDeleteContainer.setVisibility(View.GONE);
                    photosAdapter.notifyDataSetChanged();
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
                    photosAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
                break;
            case R.id.ll_btn_delete://删除
                break;
            case R.id.iv_selector_title://标题的勾选框
                int currentPosition = viewpager.getCurrentItem();
                if (photosAdapter.selectorList.contains(currentPosition)) {//之前是已选中
                    photosAdapter.selectorList.remove(new Integer(currentPosition));//注意,要删除的是值是position的项,而不是角标position的项
                    ivSelectorTitle.setImageResource(R.mipmap.unselector_icon);
                } else {
                    photosAdapter.selectorList.add(currentPosition);//记录选中position
                    ivSelectorTitle.setImageResource(R.mipmap.selector_icon);
                }
                if (photosAdapter.selectorList.size() > 0) {
                    tvTitle.setText("已选择 (" + photosAdapter.selectorList.size() + ")");
                } else {
                    tvTitle.setText("未选择");
                }
                if (photosAdapter.selectorList.size() != list.size()) {//只要不是全选中,按钮设置文案为全选
                    tvAllSelect.setText("全选");
                    ivAllSelect.setImageResource(R.mipmap.all_select_normal);
                }
                if (photosAdapter.selectorList.size() > 0) {//有选中
                    llBtnDelete.setClickable(true);
                    tvDelete.setTextColor(Utils.getColor(R.color.text_333333));
                    ivDelete.setImageResource(R.mipmap.delete_click_icon);
                } else {
                    llBtnDelete.setClickable(false);
                    tvDelete.setTextColor(Utils.getColor(R.color.text_999999));
                    ivDelete.setImageResource(R.mipmap.delete_unclick_icon);
                }
                photosAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_btn_all_select://全选
                if (photosAdapter.selectorList.size() == list.size()) {//已是全选状态,清空
                    photosAdapter.selectorList.clear();
                    tvAllSelect.setText("全选");
                    ivAllSelect.setImageResource(R.mipmap.all_select_normal);
                } else {//全选
                    photosAdapter.selectorList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        photosAdapter.selectorList.add(i);
                    }
                    tvAllSelect.setText("取消全选");
                    ivAllSelect.setImageResource(R.mipmap.all_select_icon);
                }
                if (photosAdapter.selectorList.size() > 0) {
                    tvTitle.setText("已选择 (" + photosAdapter.selectorList.size() + ")");
                } else {
                    tvTitle.setText("未选择");
                }
                if (photosAdapter.selectorList.size() > 0) {//有选中
                    llBtnDelete.setClickable(true);
                    tvDelete.setTextColor(Utils.getColor(R.color.text_333333));
                    ivDelete.setImageResource(R.mipmap.delete_click_icon);
                } else {
                    llBtnDelete.setClickable(false);
                    tvDelete.setTextColor(Utils.getColor(R.color.text_999999));
                    ivDelete.setImageResource(R.mipmap.delete_unclick_icon);
                }
                photosAdapter.notifyDataSetChanged();
                break;
        }
    }

    int perverClick = 0;//点击照片之前的状态

    class PhotosAdapter extends BaseQuickAdapter<PhotoInfoBean, BaseViewHolder> {
        List<Integer> selectorList = new ArrayList<>();//记录选中的图片

        public PhotosAdapter(@Nullable List<PhotoInfoBean> data) {
            super(R.layout.item_photos_show, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final PhotoInfoBean item) {
            final ResizableImageView iv_img = helper.getView(R.id.iv_img);
            final ResizableImageView iv_bg = helper.getView(R.id.iv_bg);
            final ImageView iv_selector = helper.getView(R.id.iv_selector);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(PhotosActivity.this).
                    load(item.fileNmae)
                    .apply(options)
                    .into(iv_img);
//            iv_img.setImageBitmap(BitmapFactory.decodeFile(item.fileNmae));
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
//                    ViewPagerScroller mPagerScroller=new ViewPagerScroller(PhotosActivity.this);
//                    mPagerScroller.initViewPagerScroll(viewpager);
//                    mPagerScroller.setScrollDuration(500);
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


//                    ImageView imageView = new ResizableImageView(PhotosActivity.this);
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.height = iv_img.getHeight();
//                    layoutParams.width = iv_img.getWidth();
////                    imageView.setLayoutParams(iv_img.getLayoutParams());
//
//                    int[] location = new int[2];
//                    iv_img.getLocationOnScreen(location);
//                    int x = location[0];
//                    int y = location[1];
////                    imageView.layout(iv_img.getLeft()+x,iv_img.getTop()+y,iv_img.getRight(),iv_img.getBottom());
//                    layoutParams.leftMargin = x+iv_img.getLeft();
//                    layoutParams.topMargin = y+iv_img.getTop();
//                    imageView.setLayoutParams(layoutParams);
//                    imageView.requestLayout();
//                    imageView.setImageResource(item);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(imageView);
//                    frameLayout.setVisibility(View.VISIBLE);
//
//
//                    Animation animation = AnimationUtils.loadAnimation(PhotosActivity.this, R.anim.anim_zoom_img);
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            frameLayout.setVisibility(View.GONE);
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
////                    imageView.startAnimation(animation);
//                    zoomImageFromThumb(frameLayout,imageView,item);
//                    if (status == 1) {
//                        rlTitleContainer.setVisibility(View.VISIBLE);
//                    } else {
//                        rlTitleContainer.setVisibility(View.GONE);
//                    }
//                    viewpager.setCurrentItem(position);
//                    viewpager.setVisibility(View.VISIBLE);
//                    ivSelectorTitle.setVisibility(View.VISIBLE);
//                    rlTitleContainer.setBackgroundResource(R.color.black);
//                    if (selectorList.contains(position)) {
//                        ivSelectorTitle.setImageResource(R.mipmap.selector_icon);
//                    } else {
//                        ivSelectorTitle.setImageResource(R.mipmap.unselector_icon);
//                    }
//                    status = 2;
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
                    llDeleteContainer.setVisibility(View.VISIBLE);
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
                    if (selectorList.size() != list.size()) {//只要不是全选中,按钮设置文案为全选
                        tvAllSelect.setText("全选");
                        ivAllSelect.setImageResource(R.mipmap.all_select_normal);
                    }
                    if (selectorList.size() > 0) {//有选中
                        llBtnDelete.setClickable(true);
                        tvDelete.setTextColor(Utils.getColor(R.color.text_333333));
                        ivDelete.setImageResource(R.mipmap.delete_click_icon);
                    } else {
                        llBtnDelete.setClickable(false);
                        tvDelete.setTextColor(Utils.getColor(R.color.text_999999));
                        ivDelete.setImageResource(R.mipmap.delete_unclick_icon);
                    }
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
            View view = View.inflate(PhotosActivity.this, R.layout.item_photos_viewpager, null);
            ImageView iv_img = view.findViewById(R.id.iv_img);
//            iv_img.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getFileNmae()));
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(PhotosActivity.this).
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

    @Override
    public void onBackPressed() {
        ivBack.performClick();
    }

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

    private Animator mCurrentAnimator;
    //系统的短时长动画持续时间（单位ms）
    // 对于不易察觉的动画或者频繁发生的动画
    // 这个动画持续时间是最理想的
    private int mShortAnimationDuration;

    private void zoomImageFromThumb(final View thumbView, final ImageView expandedImageView, int imageResId) {
        // 如果有动画在执行，立即取消，然后执行现在这个动画
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
//        // 加载高分辨率的图片
//        final ImageView expandedImageView = (ImageView) findViewById(
//                R.id.imageId);
        expandedImageView.setImageResource(imageResId);
        // 计算开始和结束位置的图片范围
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();
        // 开始的范围就是ImageButton的范围，
        // 结束的范围是容器（FrameLayout）的范围
        // getGlobalVisibleRect(Rect)得到的是view相对于整个硬件屏幕的Rect
        // 即绝对坐标，减去偏移，获得动画需要的坐标，即相对坐标
        // getGlobalVisibleRect(Rect,Point)中，Point获得的是view在它在
        // 父控件上的坐标与在屏幕上坐标的偏移
        thumbView.getGlobalVisibleRect(startBounds);
        thumbView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        // 下面这段逻辑其实就是保持纵横比
        float startScale;
        // 如果结束图片的宽高比比开始图片的宽高比大
        // 就是结束时“视觉上”拉宽了（压扁了）图片
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        // 隐藏小的图片，展示大的图片。当动画开始的时候，
        // 要把大的图片发在小的图片的位置上
        //小的设置透明
        thumbView.setAlpha(0f);
        //大的可见
        expandedImageView.setVisibility(View.VISIBLE);
        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);
        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        // 再次点击返回小的图片，就是上面扩大的反向动画。即预览完成
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }
                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
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

