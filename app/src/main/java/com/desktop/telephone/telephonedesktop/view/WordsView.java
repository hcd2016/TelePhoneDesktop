package com.desktop.telephone.telephonedesktop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.util.DensityUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

public class WordsView extends View {
    /*绘制的列表导航字母*/
    private String words[] = { "#","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /*字母画笔*/
    private Paint wordsPaint;
    /*字母背景画笔*/
    private Paint bgPaint;
    /*每一个字母的宽度*/
    private int itemWidth;
    /*每一个字母的高度*/
    private int itemHeight;
    /*手指按下的字母索引*/
    private int touchIndex = 0;
    Context context;

    public WordsView(Context context) {
        super(context,null);
        this.context = context;
    }

    public WordsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        this.context = context;
    }

    public WordsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    //得到画布的宽度和每一个字母所占的高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        //使得边距好看一些
        int height = getMeasuredHeight() - 15;
        itemHeight = height / 27;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        bgPaint = new Paint();
//        bgPaint.setColor(Utils.getColor(R.color.colorPrimaryDark));
        wordsPaint = new Paint();
        wordsPaint.setTextSize(DensityUtil.dip2px(context,18));
        wordsPaint.setFakeBoldText(true);
        wordsPaint.setColor(Utils.getColor(R.color.text_333333));
        for (int i = 0; i < words.length; i++) {
            //判断是不是我们按下的当前字母
            if (touchIndex == i) {
                //绘制文字圆形背景
//                canvas.drawCircle(itemWidth / 2, itemHeight / 2 + i * itemHeight, 35, bgPaint);
                wordsPaint.setColor(Utils.getColor(R.color.colorPrimaryDark));
            } else {
                wordsPaint.setColor(Color.GRAY);
            }
            //获取文字的宽高
            Rect rect = new Rect();
            wordsPaint.getTextBounds(words[i], 0, 1, rect);
            int wordWidth = rect.width();
            //绘制字母
            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemWidth / 2 + i * itemHeight;
            canvas.drawText(words[i], wordX, wordY, wordsPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 当手指触摸按下的时候改变字母背景颜色
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //关键点===获得我们按下的是那个索引(字母)
                int index = (int) (y / itemHeight);
                if (index != touchIndex)
                    touchIndex = index;
                //防止数组越界
                if (onWordsChangeListener != null && 0 <= touchIndex && touchIndex <= words.length - 1) {
                    //回调按下的字母
                    onWordsChangeListener.wordsChange(words[touchIndex]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起,不做任何操作
                break;
        }
        return true;
    }

    private OnWordsChangeListener onWordsChangeListener;
    /*手指按下了哪个字母的回调接口*/
    public interface OnWordsChangeListener {
        void wordsChange(String words);
    }

    public void setOnWordsChangeListener(OnWordsChangeListener onWordsChangeListener) {
        this.onWordsChangeListener = onWordsChangeListener;
    }

    /*设置当前按下的是那个字母*/
    public void setTouchIndex(String word) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                touchIndex = i;
                invalidate();
                return;
            }
        }
    }
}
