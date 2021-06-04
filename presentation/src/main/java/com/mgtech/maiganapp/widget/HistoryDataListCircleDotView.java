package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class HistoryDataListCircleDotView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DIAMETER = ViewUtils.dp2px(7f);
    private static final int LINE_WIDTH = ViewUtils.dp2px(1f);
    private static final int LINE_COLOR = Color.parseColor("#c0c0c0");
    private int abnormalColor = Color.parseColor("#ff4e00");
    private int normalColor = Color.parseColor("#38d5db");
    private boolean abnormal;

    public HistoryDataListCircleDotView(Context context) {
        super(context);
        init(context);
    }

    public HistoryDataListCircleDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HistoryDataListCircleDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setAbnormal(boolean abnormal) {
        this.abnormal = abnormal;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = resolveSize(DIAMETER, widthMeasureSpec);
        setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
    }

    private void init(Context context) {
        paint.setStyle(Paint.Style.FILL);
        normalColor = ContextCompat.getColor(context, R.color.colorPrimary);
        abnormalColor = ContextCompat.getColor(context, R.color.warningColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setColor(LINE_COLOR);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
        paint.setColor(abnormal ? abnormalColor : normalColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, DIAMETER / 2, paint);
    }
}
