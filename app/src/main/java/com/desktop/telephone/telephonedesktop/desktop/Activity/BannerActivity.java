package com.desktop.telephone.telephonedesktop.desktop.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.base.BaseActivity;
import com.desktop.telephone.telephonedesktop.bean.PhotoInfoBean;
import com.desktop.telephone.telephonedesktop.util.DaoUtil;
import com.desktop.telephone.telephonedesktop.util.GlideImageLoader;
import com.desktop.telephone.telephonedesktop.util.SPUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 轮播
 */
public class BannerActivity extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        final List<PhotoInfoBean> bannerList = DaoUtil.getPhotoInfoBeanDao().loadAll();
        if (bannerList == null || bannerList.size() == 0) {
            SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
            finish();
        }
        List<String> images = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            images.add(bannerList.get(i).getFileName());
        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
//        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        long banner_speed = SPUtil.getInstance().getLong(SPUtil.KEY_BANNER_SPEED, 3000);
        //设置轮播时间
        banner.setDelayTime((int) banner_speed);
//        //设置指示器位置（当banner模式中有指示器时）
//        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
                finish();
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                switch (i % 15) {
                    case 0:
                        banner.setBannerAnimation(Transformer.ForegroundToBackground);
                    break;
                    case 1:
                        banner.setBannerAnimation(Transformer.CubeIn);
                    break;
                    case 2:
                        banner.setBannerAnimation(Transformer.DepthPage);
                    break;
                    case 3:
                        banner.setBannerAnimation(Transformer.FlipHorizontal);
                    break;
                    case 4:
                        banner.setBannerAnimation(Transformer.FlipVertical);
                    break;
                    case 5:
                        banner.setBannerAnimation(Transformer.RotateDown);
                    break;
                    case 6:
                        banner.setBannerAnimation(Transformer.RotateUp);
                    break;
                    case 7:
                        banner.setBannerAnimation(Transformer.Stack);
                    break;
                    case 8:
                        banner.setBannerAnimation(Transformer.Tablet);
                    break;
                    case 9:
                        banner.setBannerAnimation(Transformer.ZoomIn);
                    break;
                    case 10:
                        banner.setBannerAnimation(Transformer.ZoomOut);
                    break;
                    case 11:
                        banner.setBannerAnimation(Transformer.ZoomOutSlide);
                    break;
                    case 12:
                        banner.setBannerAnimation(Transformer.Accordion);
                    break;
                    case 13:
                        banner.setBannerAnimation(Transformer.BackgroundToForeground);
                    break;
                    case 14:
                        banner.setBannerAnimation(Transformer.ForegroundToBackground);
                    break;
                    case 15:
                        banner.setBannerAnimation(Transformer.CubeOut);
                    break;
                }
//                    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
//                    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
//                    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
//                    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
//                    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
//                    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
//                    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
//                    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
//                    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
//                    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
//                    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
//                    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
//                    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
//                    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
//                    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
//                    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
//                    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;


            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_BANNER_RUNING, true);
        banner.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtil.getInstance().saveBoolean(SPUtil.KEY_IS_BANNER_RUNING, false);
    }
}