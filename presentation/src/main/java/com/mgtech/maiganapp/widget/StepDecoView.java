package com.mgtech.maiganapp.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * Created by zhaixiang on 2017/11/6.
 * 计步
 */

public class StepDecoView extends View {
    private Paint bgPaint;
    private Paint mainTextPaint;
    private Paint otherTextPaint;
    /**
     * 一圈代表的数字
     */
    private float maxValue = 10000;
    /**
     * 顶端代表的数字
     */
    private float minValue = 0;
    /**
     * 上一次动画结束的数字
     */
    private float lastValue = 0;
    /**
     * 当前动画数字
     */
    private float currentValue;
    /**
     * 数据数字
     */
    private float dataValue;
    private ValueAnimator animator;
    /**
     * 组件宽（高）
     */
    private int width;
    /**
     * 变化的文字的高度
     */
    private int textHeight;

    /**
     * 首行文字
     */
    private String topLineText = "今日已走";

    /**
     * 底行文字
     */
    private String bottomLineText = "目标：10000步";

    public StepDecoView(Context context) {
        super(context);
        init(context);
    }

    public StepDecoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StepDecoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public String getTopLineText() {
        return topLineText;
    }

    public void setTopLineText(String topLineText) {
        this.topLineText = topLineText;
    }

    public String getBottomLineText() {
        return bottomLineText;
    }

    public void setBottomLineText(String bottomLineText) {
        this.bottomLineText = bottomLineText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
    }

    private void init(Context context) {
        AssetManager assetManager = context.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, "lcdd.ttf");

        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(2.5f);

        mainTextPaint = new Paint();
        mainTextPaint.setColor(Color.WHITE);
        mainTextPaint.setAntiAlias(true);
        mainTextPaint.setTextSize(70);
        mainTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = mainTextPaint.getFontMetrics();
        textHeight = (int) Math.abs(Math.ceil(metrics.ascent));
//        textHeight = (int) Math.abs(Math.ceil(metrics.top));
        mainTextPaint.setTypeface(typeface);

        otherTextPaint = new Paint();
        otherTextPaint.setColor(Color.WHITE);
        otherTextPaint.setAntiAlias(true);
        otherTextPaint.setTextAlign(Paint.Align.CENTER);
//        setData(5634);
    }

    public void setData(int value) {
        lastValue = currentValue;
        dataValue = value;
//        animator = ValueAnimator.ofFloat(lastValue,value+maxValue);
        animator = ValueAnimator.ofFloat(lastValue, value);
        animator.setDuration(1000);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initWidth();
        drawBackground(canvas);
        drawData(canvas);
        drawText(canvas);
    }

    /**
     * 绘制背景
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        bgPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, width / 2, width * 11 / 24, bgPaint);
        bgPaint.setStyle(Paint.Style.FILL);
        int outRadius = width / 2 - 2;
        for (int i = 0; i < 48; i++) {
            int x = (int) (width / 2 + Math.cos(Math.PI / 24 * i) * outRadius);
            int y = (int) (width / 2 + Math.sin(Math.PI / 24 * i) * outRadius);
            canvas.drawCircle(x, y, 2f, bgPaint);
        }
    }

    private Path dataPath = new Path();

    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        double radians = 2 * Math.PI * ((currentValue - minValue) / (double) (maxValue - minValue) - 0.25);
        int posRadius = width * 11 / 24;
        int x = (int) (width / 2 + Math.cos(radians) * posRadius);
        int y = (int) (width / 2 + Math.sin(radians) * posRadius);
        canvas.drawCircle(x, y, width / 24, bgPaint);

        // test
//        dataPath.reset();
//        double radians1 = 2 * Math.PI * ((currentValue + 100 - minValue) / (double) (maxValue - minValue) - 0.25);
//        double radians2 = 2 * Math.PI * ((currentValue - 100 - minValue) / (double) (maxValue - minValue) - 0.25);
//        int x1 = (int) (width / 2 + Math.cos(radians1) * posRadius);
//        int y1 = (int) (width / 2 + Math.sin(radians1) * posRadius);
//
//        int x2 = (int) (width / 2 + Math.cos(radians2) * posRadius);
//        int y2 = (int) (width / 2 + Math.sin(radians2) * posRadius);
//
//        int xi = (int) (width / 2 + Math.cos(radians) * (posRadius - width / 24));
//        int yi = (int) (width / 2 + Math.sin(radians) * (posRadius - width / 24));
//
//        int xo = (int) (width / 2 + Math.cos(radians) * (posRadius + width / 24));
//        int yo = (int) (width / 2 + Math.sin(radians) * (posRadius + width / 24));
//
//        dataPath.moveTo(xi,yi);
//        dataPath.quadTo(x1,y1,xo,yo);
//        dataPath.quadTo(x2,y2,xi,yi);
//        dataPath.close();
//        canvas.drawPath(dataPath,bgPaint);
//        canvas.drawCircle(x1,y1,width/48,bgPaint);
//        canvas.drawCircle(x2,y2,width/48,bgPaint);
//        canvas.drawCircle(xi,yi,width/48,bgPaint);
//        canvas.drawCircle(xo,yo,width/48,bgPaint);
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        int x = width / 2;
        int y = width / 2 + textHeight / 2;
//        int displayNumber = Math.round( currentValue/ (dataValue + maxValue) * dataValue);
        int displayNumber = Math.round(currentValue / (dataValue) * dataValue);

        canvas.drawText(String.format(Locale.getDefault(), "%04d", displayNumber), x, y, mainTextPaint);
        if (topLineText != null && !topLineText.isEmpty()) {
            otherTextPaint.setTextSize(30);
            canvas.drawText(topLineText, x, y - textHeight - 10, otherTextPaint);
        }
        if (bottomLineText != null && !bottomLineText.isEmpty()) {
            otherTextPaint.setTextSize(18);
            canvas.drawText(bottomLineText, x, y + 32, otherTextPaint);
        }
    }

    private void initWidth() {
        if (width == 0) {
            width = getWidth();
        }
    }
}
