package com.api.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luxin on 15-12-12.
 */
public class MyCircleProgressBar extends View {

    private Paint mBackPaint;
    private Paint mFrontPaint;
    private Paint mTextPaint;

    private float mStrokeWidth=50;

    public MyCircleProgressBar(Context context) {
        super(context);
    }

    public MyCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
