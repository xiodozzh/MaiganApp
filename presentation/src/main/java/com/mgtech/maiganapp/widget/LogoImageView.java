package com.mgtech.maiganapp.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by zhaixiang on 2017/introductor1/10.
 */

public class LogoImageView extends AppCompatImageView {
    public LogoImageView(Context context) {
        super(context);
    }

    public LogoImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(widthMeasureSpec) * 10 / 28;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize,heightMode));
    }
}
