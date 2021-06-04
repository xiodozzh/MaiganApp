package com.mgtech.maiganapp.widget;

import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2017/11/6.
 * 血压
 */

public class BloodPressureDecoView extends View {
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
    private float currentPercent1;
    private float currentPercent2;

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
    private String topLineText = "";

    /**
     * 底行文字
     */
    private String bottomLineText = "mmHg";

    private Series series1;
    private Series series2;
    private int dataArcWidth = 14;

    private int value1;
    private int value2;
    private float animalProcess;

    private int barColor1;
    private int barColor2;

    private float density;

    public BloodPressureDecoView(Context context) {
        super(context);
        init(context);
    }

    public BloodPressureDecoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BloodPressureDecoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
        dataArcWidth = ViewUtils.dp2px(density,6);
        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(1f);

        mainTextPaint = new Paint();
        mainTextPaint.setColor(Color.WHITE);
        mainTextPaint.setAntiAlias(true);
        mainTextPaint.setTextSize(ViewUtils.sp2px(displayMetrics,30));
        mainTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = mainTextPaint.getFontMetrics();
        textHeight = (int) Math.abs(Math.ceil(metrics.ascent));

        otherTextPaint = new Paint();
        otherTextPaint.setColor(Color.WHITE);
        otherTextPaint.setAntiAlias(true);
        otherTextPaint.setTextAlign(Paint.Align.CENTER);
        otherTextPaint.setTextSize(ViewUtils.sp2px(displayMetrics,14));

        series1 = new Series();
        series2 = new Series();

        barColor1 = ContextCompat.getColor(getContext(), R.color.bar1_level_high);
        barColor2 = ContextCompat.getColor(getContext(), R.color.bar2_level_high);
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }

    public int getBarColor1() {
        return barColor1;
    }

    public void setBarColor1(int barColor1) {
        this.barColor1 = barColor1;
    }

    public int getBarColor2() {
        return barColor2;
    }

    public void setBarColor2(int barColor2) {
        this.barColor2 = barColor2;
    }

    public void setValue(float value1, float value2) {
        if ((int)value1 == this.value1 && (int)value2 == this.value2){
            return;
        }
        this.value1 = (int) value1;
        this.value2 = (int) value2;
        setData(value1/maxValue,value2/maxValue);
    }

    public int getMaxValue() {
        return (int) maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private void setData(final float value1, final float value2) {
        Keyframe keyframe1= Keyframe.ofFloat(0,0);
        // 时间经过 50% 的时候，动画完成度 100%
        Keyframe keyframe2 = Keyframe.ofFloat(0.95f, 1.05f);
        // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
        Keyframe keyframe3 = Keyframe.ofFloat(1, 1f);
        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("progress", keyframe1, keyframe2, keyframe3);

        animator = ValueAnimator.ofPropertyValuesHolder(holder);

//        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        int color1 = ContextCompat.getColor(getContext(), R.color.grey400);

        final float[] hsv1 = new float[3];
        final float[] hsv2 = new float[3];
        final float[] hsv3 = new float[3];
        Color.colorToHSV(color1, hsv1);
        Color.colorToHSV(barColor1, hsv2);
        Color.colorToHSV(barColor2, hsv3);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animalProcess = (float) animation.getAnimatedValue();
                currentPercent1 = animalProcess * value1;
                currentPercent2 = animalProcess * value2;
                series1.setColor(getMiddleColor(hsv1,hsv2,animalProcess));
                series2.setColor(getMiddleColor(hsv1,hsv3,animalProcess));
                postInvalidate();
            }
        });

        animator.start();
    }

    /**
     * 计算颜色
     *
     * @param startColor 开始颜色
     * @param endColor   最终颜色
     * @param fraction   进度百分比
     * @return 中间颜色
     */
    private int getMiddleColor(float[] startColor, float[] endColor, float fraction) {
        float[] hsv = new float[3];
        if (endColor[0] - startColor[0] > 180) {
            endColor[0] -= 360;
        } else if (endColor[0] - startColor[0] < -180) {
            endColor[0] += 360;
        }
        hsv[0] = startColor[0] + (endColor[0] - startColor[0]) * fraction;
        if (hsv[0] > 360) {
            hsv[0] -= 360;
        } else if (hsv[0] < 0) {
            hsv[0] += 360;
        }
        hsv[1] = startColor[1] + (endColor[1] - startColor[1]) * fraction;
        hsv[2] = startColor[2] + (endColor[2] - startColor[2]) * fraction;
        return Color.HSVToColor(hsv);
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
        int outRadius = width / 2;
        bgPaint.setAlpha(180);
        bgPaint.setStrokeWidth(1);
        int number = 144;
        for (int i = 0; i < number; i++) {
            double cos = Math.cos(Math.PI / number * 2 * i);
            double sin = Math.sin(Math.PI / number * 2 * i);
            int xo = (int) Math.round(width / 2 + cos * outRadius);
            int yo = (int) Math.round(width / 2 + sin * outRadius);

            int xi = (int) Math.round(width / 2 + cos * outRadius * 0.85);
            int yi = (int) Math.round(width / 2 + sin * outRadius * 0.85);
            canvas.drawLine(xi, yi, xo, yo, bgPaint);
        }
        canvas.drawCircle(outRadius, outRadius, (int) (outRadius * 0.80- dataArcWidth/2), bgPaint);
    }

    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        if (series1.getBound() == null || series2.getBound() == null) {
            int center = width / 2;
            int radius = (int) (width / 2 * 0.80);
            int l = center - radius;
            int t = center - radius;
            int b = center + radius;
            int r = center + radius;
            RectF rectF = new RectF(l, t, b, r);
            series1.setBound(rectF);
            series2.setBound(rectF);
        }
        series1.draw(canvas, currentPercent1, dataArcWidth);
        series2.draw(canvas, currentPercent2, dataArcWidth);
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        int x = width / 2;
        int y = width / 2 + textHeight / 2;
        int displayNumber1 = Math.round(animalProcess * value1);
        int displayNumber2 = Math.round(animalProcess * value2);
        mainTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("/",x,y-12,mainTextPaint);
        mainTextPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(displayNumber1+"",x-8,y-12,mainTextPaint);
        mainTextPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(displayNumber2+"",x+12,y-12,mainTextPaint);

        int textPadding = ViewUtils.dp2px(density,16);
        canvas.drawText(getContext().getString(R.string.blood_pressure),x, width / 2 - textHeight / 2 - textPadding,otherTextPaint);

//        canvas.drawText(String.format(Locale.getDefault(), "%1$d/%2$d", displayNumber1,displayNumber2), x, y-12, mainTextPaint);
        if (bottomLineText != null && !bottomLineText.isEmpty()) {
            canvas.drawText(bottomLineText, x, y + textPadding, otherTextPaint);
        }
    }

    private void initWidth() {
        if (width == 0) {
            width = getWidth();
        }
    }

    private class Series {
        private Paint paint;
        private RectF boundInset;
        private RectF bound;
        private Path path;
        private int currentColor;

        public Series() {
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.path = new Path();
        }

        public void setBound(RectF rectF) {
            this.bound = rectF;
            this.boundInset = null;
        }

        public RectF getBound() {
            return this.bound;
        }

        public void setColor(int color) {
            this.currentColor = color;
        }

        public void draw(Canvas canvas, float percent, int width) {
            if (boundInset == null) {
                boundInset = new RectF(bound);
                boundInset.inset(width, width);
            }
            int center = Math.round(bound.centerX());
            int radius = (int) ((bound.right - bound.left) / 2);
            float sweep = percent * 360;


            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(center, center);
            path.lineTo(center, bound.top);
            path.arcTo(bound, -90, sweep, false);
            path.lineTo(center, center);
            path.lineTo(center, boundInset.top);
            path.arcTo(boundInset, -90, sweep, false);
            path.lineTo(center, center);

            double cos = Math.cos(Math.PI * 2 * (percent - 0.25));
            double sin = Math.sin(Math.PI * 2 * (percent - 0.25));
            float x = (float) (center + cos * (radius - width / 2));
            float y = (float) (center + sin * (radius - width / 2));
            paint.setColor(currentColor);
            canvas.drawCircle(x, y, width / 2, paint);
            canvas.drawCircle(center, bound.top + width / 2, width / 2, paint);
            canvas.drawPath(path, paint);
        }
    }

}
