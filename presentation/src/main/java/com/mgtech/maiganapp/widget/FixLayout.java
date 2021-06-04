package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mgtech.maiganapp.R;

/**
 * Created by zhaixiang on 2018/1/10.
 * 固定长宽比
 */

public class FixLayout extends FrameLayout {
    private float ratio = 1f;

    public FixLayout(@NonNull Context context) {
        super(context);
    }

    public FixLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public FixLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet atts){
        TypedArray typedArray = context.obtainStyledAttributes(atts, R.styleable.FixLayout);
        ratio = typedArray.getFloat(R.styleable.FixLayout_ratio,1f);
        typedArray.recycle();
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float currRatio = height /(float)width;
        if (ratio >= currRatio){
            width = (int) (height /ratio);
        }else{
            height = (int) (ratio * width);
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
