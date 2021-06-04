package com.mgtech.maiganapp.widget;

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

public class ProgressDecoView extends View {
    private Paint bgPaint;
    private Paint mainTextPaint;
    private Paint otherTextPaint;
    /**
     * 一圈代表的数字
     */
    private float maxValue = 100;
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
    private float currentPercent;

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

    private Series series;
    private int dataArcWidth = 28;

    private float value;
    private float animalProcess;

    private int barColor;

    private float density;
    private Listener listener;

    public ProgressDecoView(Context context) {
        super(context);
        init(context);
    }

    public ProgressDecoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressDecoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public interface Listener{
        void onText(int value);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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
        bgPaint.setColor(Color.GRAY);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeWidth(1f);

        mainTextPaint = new Paint();
        mainTextPaint.setColor(Color.WHITE);
        mainTextPaint.setAntiAlias(true);
        mainTextPaint.setTextSize(ViewUtils.sp2px(displayMetrics,16));
        mainTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = mainTextPaint.getFontMetrics();
        textHeight = (int) Math.abs(Math.ceil(metrics.ascent));

        otherTextPaint = new Paint();
        otherTextPaint.setColor(Color.WHITE);
        otherTextPaint.setAntiAlias(true);
        otherTextPaint.setTextAlign(Paint.Align.CENTER);
        otherTextPaint.setTextSize(ViewUtils.sp2px(displayMetrics,9));

        series = new Series();

        barColor = ContextCompat.getColor(getContext(), R.color.bar1_level_high);
    }

    public float getValue() {
        return value;
    }


    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }


    public void setValue(float value) {
        setData(value/maxValue);
        this.value = value;
    }

    public int getMaxValue() {
        return (int) maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private void setData(final float fraction) {
        animator = ValueAnimator.ofFloat(fraction, 1);
        animator.setDuration(500);
        animator.setInterpolator(new FastOutSlowInInterpolator());

        series.setColor(barColor);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animalProcess = (float) animation.getAnimatedValue();
                currentPercent = (1-animalProcess)*(value/maxValue)+animalProcess * fraction;
//                series.setColor(getMiddleColor(hsv1,hsv2,animalProcess));
                if (listener != null){
                    listener.onText((int) (currentPercent* maxValue));
                }
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
        int outRadius = width / 2;
        bgPaint.setColor(Color.LTGRAY);
        canvas.drawCircle(outRadius, outRadius, outRadius, bgPaint);
        bgPaint.setColor(Color.WHITE);
        canvas.drawCircle(outRadius, outRadius, outRadius -dataArcWidth, bgPaint);
    }

    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        if (series.getBound() == null ) {
            int center = width / 2;
            int radius = width / 2 ;
            int l = center - radius;
            int t = center - radius;
            int b = center + radius;
            int r = center + radius;
            RectF rectF = new RectF(l, t, b, r);
            series.setBound(rectF);
        }
        series.draw(canvas, currentPercent, dataArcWidth);
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        int x = width / 2;
        int y = width / 2 + textHeight / 2;
        int displayNumber1 = Math.round(animalProcess * value);
        mainTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("/",x,y-12,mainTextPaint);
        mainTextPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(displayNumber1+"",x-8,y-12,mainTextPaint);
        mainTextPaint.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText(String.format(Locale.getDefault(), "%1$d/%2$d", displayNumber1,displayNumber2), x, y-12, mainTextPaint);
        if (bottomLineText != null && !bottomLineText.isEmpty()) {
            canvas.drawText(bottomLineText, x, y + ViewUtils.dp2px(density,16), otherTextPaint);
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
