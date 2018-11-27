package com.desktop.telephone.telephonedesktop.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 宽=高的imageView
 */
public class ResizableImageView extends android.support.v7.widget.AppCompatImageView {
    public ResizableImageView(Context context) {
        super(context);
    }


    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
