package com.nenton.popularmovies.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by serge on 03.03.2018.
 */

public class ImageViewCustom extends AppCompatImageView {

    public static final double ASPECT_RATIO = 0.69;

    public ImageViewCustom(Context context) {
        super(context);
    }

    public ImageViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth;
        int mHeight;

        mWidth = getMeasuredWidth();
        mHeight = (int) (mWidth / ASPECT_RATIO);

        setMeasuredDimension(mWidth, mHeight);
    }
}
