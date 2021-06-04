package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2018/4/4.
 * 指示计算结果的bar
 */

public class IndicatorBarView extends View {
    private Paint paintBar;
    private Paint paintText;
    private Path pathBar;
    private int colorLow;
    private int colorNormal;
    private int colorHigh;
    private int barWidth;
    private int textHeight;
    private int textPadding;
    private int barPadding;
    private float upperLimit = 90;
    private float lowerLimit = 60;
    private int textYOffset;
    private String textLow;
    private String textNormal;
    private String textHigh;
    private float value = 70;

    private int dataRadius1;
    private int dataRadius2;

    public IndicatorBarView(Context context) {
        super(context);
        init(context);
    }

    public IndicatorBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndicatorBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int textSize = ViewUtils.sp2px(metrics, 14);

        paintBar = new Paint();
        paintBar.setAntiAlias(true);

        pathBar = new Path();

        colorLow = ContextCompat.getColor(context,R.color.warningColor);
        colorNormal = Color.parseColor("#38D5DB");
        colorHigh = ContextCompat.getColor(context,R.color.warningColor);

        paintText = new Paint();
        paintText.setColor(Color.DKGRAY);
        paintText.setAntiAlias(true);
        paintText.setTextSize(textSize);
        paintText.setTextAlign(Paint.Align.CENTER);

        textHeight = ViewUtils.dp2px(metrics.density, 20);
        textPadding = ViewUtils.dp2px(metrics.density, 4);
        barWidth = ViewUtils.dp2px(metrics.density, 6);
        dataRadius1 = ViewUtils.dp2px(metrics.density, 3);
        dataRadius2 = ViewUtils.dp2px(metrics.density, 6);
        barPadding = ViewUtils.dp2px(metrics.density, 16);
        textYOffset = ViewUtils.getTextOffset(paintText);

        textLow = context.getString(R.string.lower);
        textNormal = context.getString(R.string.normal);
        textHigh = context.getString(R.string.higher);
    }

    public void setValue(float value, float lowerLimit, float upperLimit) {
        this.value = value;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(textHeight * 2 + textPadding * 2 + barWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBar(canvas);
        drawData(canvas);
        drawUpText(canvas);
        drawDownText(canvas);
    }

    private void drawData(Canvas canvas) {

        // 范围
        float startValue = lowerLimit - (upperLimit - lowerLimit);
        float endValue = upperLimit + (upperLimit - lowerLimit);

        // 数据点的位置
        float position = 0;
        if (value <= startValue) {
            position = 0;
        } else if (value >= endValue) {
            position = 1;
        } else {
            position = (value - startValue) / (endValue - startValue);
        }
        int p = Math.round(position * (getWidth()-barPadding*2))+barPadding;
        int center = textHeight + textPadding + barWidth / 2;
        // 数据点颜色
        if (value < lowerLimit) {
            paintBar.setColor(colorLow);
        } else if (value > upperLimit) {
            paintBar.setColor(colorHigh);
        } else {
            paintBar.setColor(colorNormal);
        }
        canvas.drawCircle(p, center, dataRadius2, paintBar);
        paintBar.setColor(Color.WHITE);
        canvas.drawCircle(p, center, dataRadius1, paintBar);
    }

    /**
     * 绘制bar
     *
     * @param canvas 画布
     */
    private void drawBar(Canvas canvas) {
        int top = textHeight + textPadding;
        int bottom = top + barWidth;
        int middle1 = getMiddle1();
        int middle2 = getMiddle2();
        pathBar.reset();
        pathBar.moveTo(barPadding + barWidth / 2, top);
        pathBar.lineTo(middle1, top);
        pathBar.lineTo(middle1, bottom);
        pathBar.lineTo(barPadding +barWidth / 2, bottom);
        pathBar.arcTo(barPadding, top, barPadding + barWidth, bottom, 90, 180, true);
        pathBar.close();
        paintBar.setColor(colorLow);
        canvas.drawPath(pathBar, paintBar);

        pathBar.reset();
        pathBar.moveTo(middle2, top);
        pathBar.lineTo(getWidth() - barWidth / 2- barPadding, top);
        pathBar.arcTo(getWidth() - barWidth- barPadding, top, getWidth()-barPadding, bottom, -90, 180, false);
        pathBar.lineTo(middle2, bottom);
        pathBar.lineTo(middle2, top);
        pathBar.close();
        paintBar.setColor(colorHigh);
        canvas.drawPath(pathBar, paintBar);

        paintBar.setColor(colorNormal);
        canvas.drawRect(middle1, top, middle2, bottom, paintBar);
    }

    private void drawUpText(Canvas canvas) {
        canvas.drawText(String.valueOf(lowerLimit), getMiddle1(), textHeight + textYOffset, paintText);
        canvas.drawText(String.valueOf(upperLimit), getMiddle2(), textHeight + textYOffset, paintText);
    }

    private void drawDownText(Canvas canvas) {
        int middle1 = getMiddle1();
        int middle2 = getMiddle2();
        int textY = textHeight * 3 / 2 + textPadding * 2 + barWidth - textYOffset;
        canvas.drawText(textLow, (barPadding +middle1) / 2, textY, paintText);
        canvas.drawText(textNormal, (middle1 + middle2) / 2, textY, paintText);
        canvas.drawText(textHigh, (getWidth()-barPadding + middle2) / 2, textY, paintText);
    }

    private int getMiddle1() {
        return (getWidth() - barPadding * 2) / 3 + barPadding;
    }

    private int getMiddle2() {
        return (getWidth() - barPadding * 2) / 3 * 2 + barPadding;
    }
}
