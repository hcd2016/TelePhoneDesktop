package com.desktop.telephone.telephonedesktop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.CoordinateBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 圆点indicator
 */
public class PointIndicatorView extends View {
    public List<CoordinateBean> indicators = new ArrayList<>();//每个圆点的坐标集
    public int indicatorsSize = 5;//圆点的个数
    public float radius;//圆点的半径
    public float spacingWith;//圆点间距
    public int pointDefaultColor = getResources().getColor(R.color.bg_gray);//圆点默认颜色
    public int pointChooiseColor = getResources().getColor(R.color.recharge_tips_color);//圆点选中颜色
    private Paint paint;
    private Canvas canvas;
    private int measuredHeight;
    private OnIndicatorViewClickListener onIndicatorViewClickListener;
    private ViewPager viewPager;//关联的viewpager
    private int currentSelectPosition = 0;//当前选中position

    public void setCurrentSelectPosition(int currentSelectPosition) {
        this.currentSelectPosition = currentSelectPosition;
    }


    public OnIndicatorViewClickListener getOnIndicatorViewClickListener() {
        return onIndicatorViewClickListener;
    }

    public void setOnIndicatorViewClickListener(OnIndicatorViewClickListener onIndicatorViewClickListener) {
        this.onIndicatorViewClickListener = onIndicatorViewClickListener;
    }

    public PointIndicatorView(Context context) {
        super(context);
    }

    public PointIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.indicatorView);
        pointDefaultColor = typedArray.getColor(R.styleable.indicatorView_pointDefaultColor, getResources().getColor(R.color.bg_gray));
        pointChooiseColor = typedArray.getColor(R.styleable.indicatorView_pointChooiseColor, getResources().getColor(R.color.recharge_tips_color));
        radius = typedArray.getDimension(R.styleable.indicatorView_radius, 10);
        spacingWith = typedArray.getDimension(R.styleable.indicatorView_spacingWith, 10);
        typedArray.recycle();
    }

    public PointIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽/高测量模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //设置默认宽高,当布局设置为warp_content时使用
        int defaultWidth;
        int defaultHeight;
        if (indicatorsSize != 0) {
            defaultWidth = (int) (2 * radius * indicatorsSize + spacingWith * (indicatorsSize - 1));
            defaultHeight = (int) (2 * radius);
        } else {
            defaultWidth = 0;
            defaultHeight = 0;
        }

        //用布局参数判断比用测量模式判断更严谨
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {//宽高都是包裹内容
            setMeasuredDimension(defaultWidth, defaultHeight);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(defaultWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, defaultHeight);
        }
////        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
////                - getPaddingRight(), MeasureSpec.AT_MOST);
////        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
////                - getPaddingRight(), heightMode);
////        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = getMeasuredHeight();
        measuredIndicator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawCirclePoint(canvas);
    }

    public void measuredIndicator() {
        indicators.clear();
        for (int i = 0; i < indicatorsSize; i++) {
            CoordinateBean coordinateBean = new CoordinateBean();
            if (i == 0) {//第一个圆点
                coordinateBean.centerX = radius;
            } else {
                coordinateBean.centerX = indicators.get(i - 1).centerX + radius * 2 + spacingWith;//圆心的X(第2个点起) = 上一个圆心的x+2个半径+圆心间距.
            }
            coordinateBean.centerY = measuredHeight / 2;//Y = view高度的一半
            indicators.add(coordinateBean);
        }
    }

    /**
     * 绘制圆点
     */
    public void drawCirclePoint(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
        }
        for (int i = 0; i < indicatorsSize; i++) {
            //设置选中圆点颜色
            if (currentSelectPosition == i) {
                paint.setColor(pointChooiseColor);
            } else {
                paint.setColor(pointDefaultColor);
            }
            canvas.drawCircle(indicators.get(i).centerX, indicators.get(i).centerY, radius, paint);
        }
    }

    public void setRelevanceViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
               setCurrentSelectPosition(i);
               invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//设置点击触发
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            for (int i = 0; i < indicators.size(); i++) {//根据坐标找到所属范围的点
                float centerX = indicators.get(i).centerX;
                float centerY = indicators.get(i).centerY;
                //X的范围是圆心X-半径 ~~~ 圆心X+半径+一半间距,Y的范围为圆心Y的正负半径
                if (x >= centerX - radius && x <= centerX + radius + spacingWith / 2
                        && y >= centerY - radius && y <= centerY + radius) {
                    if (null != onIndicatorViewClickListener) {
                        onIndicatorViewClickListener.onIndicatorViewClick();
                    }
                    viewPager.setCurrentItem(i);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    //设置圆点个数
    public void setIndicatorsSize(int indicatorsSize) {
        this.indicatorsSize = indicatorsSize;
    }

    public interface OnIndicatorViewClickListener {
        void onIndicatorViewClick();
    }
}