package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.LoginUtils;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * ECG静态绘图
 *
 * @author zhaixiang
 */

public class EcgPartDataGraphView extends View {
    private static final String TAG = "EcgGraphView";
    public static final float WALK_SPEED = 25f;
    private static final int scope = 3800;
    private int inSample = 1;

    private float deltaHeight;
    private float deltaWidth;
    private int pointNumber;
    private int gridLineWidthBold = ViewUtils.dp2px(1.5f);
    private int gridLineWidth = ViewUtils.dp2px(0.8f);
    private int dataLineWidth = ViewUtils.dp2px(1.5f);
    private float[] data;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int bgGridColor;
    private float density;
    private Paint dataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float sampleRate = 512;
    private float unit;
    private boolean reverse;

    public EcgPartDataGraphView(Context context) {
        super(context);
        init(context, null);
    }

    public EcgPartDataGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EcgPartDataGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setSampleRate(float sampleRate) {
        if (sampleRate != 0f) {
            this.sampleRate = sampleRate;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        this.density = context.getResources().getDisplayMetrics().density;
        this.bgGridColor = ContextCompat.getColor(context, R.color.background_grey);
        this.bgPaint.setColor(bgGridColor);
        this.bgPaint.setStyle(Paint.Style.STROKE);

        this.dataPaint.setStrokeWidth(dataLineWidth);
        this.dataPaint.setColor(ContextCompat.getColor(context, R.color.black_text));
        this.dataPaint.setStyle(Paint.Style.STROKE);

        this.unit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                Resources.getSystem().getDisplayMetrics());
    }

    public void setData(float[] data, float sampleRate) {
        this.data = data;
        if (sampleRate != 0f) {
            this.sampleRate = sampleRate;
        }
        calculateWidth(getWidth(), getHeight());
        invalidate();
    }

    public void setDataAndReverse(float[] data, float sampleRate, boolean reverse) {
        this.data = data;
        this.reverse = reverse;
        if (sampleRate != 0f) {
            this.sampleRate = sampleRate;
        }
        calculateWidth(getWidth(), getHeight());
        invalidate();
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateWidth(w, h);
    }

    private void calculateWidth(int w, int h) {
        this.paddingLeft = getPaddingLeft();
        this.paddingTop = getPaddingTop();
        this.paddingRight = getPaddingRight();
        this.paddingBottom = getPaddingBottom();
        this.deltaHeight = (h - paddingTop - paddingBottom) / (float) scope;
        this.deltaWidth = WALK_SPEED * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                Resources.getSystem().getDisplayMetrics()) / sampleRate;
        this.inSample = (int) Math.max(1, Math.ceil(1 / deltaWidth));
        this.pointNumber = (int) ((w - paddingLeft - paddingRight) / deltaWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawData(canvas);
    }


    /**
     * 绘制背景
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        float startX = paddingLeft;
        float endX = getWidth() - paddingRight;
        float startY = paddingTop;
        float endY = getHeight() - paddingBottom;
        int totalHLine = (int) ((endX - startX) / unit);
        int totalVLine = (int) ((endY - startY) / unit);
        bgPaint.setColor(bgGridColor);
        for (int i = 0; i <= totalHLine; i++) {
            float x = startX + i * unit;
            if (i % 5 == 0) {
                bgPaint.setStrokeWidth(gridLineWidthBold);
            } else {
                bgPaint.setStrokeWidth(gridLineWidth);
            }
            canvas.drawLine(x, startY, x, endY, bgPaint);
        }
        for (int i = 0; i <= totalVLine; i++) {
            float y = endY - i * unit;
            if (i % 5 == 0) {
                bgPaint.setStrokeWidth(gridLineWidthBold);
            } else {
                bgPaint.setStrokeWidth(gridLineWidth);
            }
            canvas.drawLine(startX, y, endX, y, bgPaint);
        }
    }

    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        if (data == null) {
            return;
        }
        float startX = paddingLeft;
        float endY = getHeight() - paddingBottom;
        float startY = paddingTop;

        int size = Math.min(pointNumber, data.length);
        for (int i = 1; i < size / inSample; i++) {
            float x2 = startX + i * deltaWidth * inSample;
            float y2 = reverse ? startY + data[i * inSample] * deltaHeight : endY - data[i * inSample] * deltaHeight;

            float x1 = startX + (i - 1) * inSample * deltaWidth;
            float y1 = reverse ? startY + data[(i - 1) * inSample] * deltaHeight : endY - data[(i - 1) * inSample] * deltaHeight;
            if (i == size / inSample - 1) {
                canvas.drawLine(x1, y1, x2, reverse ? startY + data[data.length - 1] * deltaHeight : endY - data[data.length - 1] * deltaHeight, dataPaint);
            } else {
                canvas.drawLine(x1, y1, x2, y2, dataPaint);
            }
        }
    }
}
