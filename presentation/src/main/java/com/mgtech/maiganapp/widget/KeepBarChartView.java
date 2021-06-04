package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2017/11/13.
 * keep style ChartView
 */

public class KeepBarChartView extends BarChartView {
    private Paint paint;
    private Paint selectedPaint;
    private Paint baseLinePaint;
    private Paint textPaint;
    private Path dataPath;
    /**
     * 渐变颜色
     */
    private int changeColor;
    private int textHeight;

    private int labelColor;
    private int labelSelectedColor;
    private int dataWidth;
    /**
     * 基础颜色
     */
    private int baseColor;

    public KeepBarChartView(Context context) {
        super(context);
        init(context);
    }

    public KeepBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeepBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initColor(context);
        DisplayMetrics displayMetrics =  context.getResources().getDisplayMetrics();
//        float density =displayMetrics.density;
        dataWidth = ViewUtils.dp2px(20);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedPaint = new Paint(paint);
        selectedPaint.setColor(baseColor);

        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(ViewUtils.sp2px(displayMetrics, 12));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        baseLinePaint = new Paint();
        baseLinePaint.setColor(labelColor);

        dataPath = new Path();

        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        textHeight = (int) Math.abs(Math.ceil(metrics.top));

    }

    /**
     * 颜色初始化
     *
     * @param context 上下文
     */
    private void initColor(Context context) {
        this.baseColor = ContextCompat.getColor(context, R.color.sport_color_select);
        this.changeColor = Color.argb(128, 132, 194, 255);
        this.labelColor = Color.parseColor("#e0e0e0");
        this.labelSelectedColor = Color.GRAY;
    }

    @Override
    public void onDrawData(Canvas canvas, RectF rectF, boolean isSelected, Integer data) {
        dataPath.reset();
        int posX = (int) rectF.centerX();
        int posY = (int) (rectF.bottom - data * viewPort.getIntervalY() + textHeight);
        int half = dataWidth / 2;
        Shader shader = new LinearGradient(0, posY, 0, height, baseColor, changeColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        dataPath.moveTo(posX - half, rectF.bottom);
        dataPath.lineTo(posX - half, Math.min(posY + half, rectF.bottom));
        dataPath.arcTo(posX - half, posY, posX + half, posY + dataWidth, -180, 180, false);
        dataPath.lineTo(posX + half, rectF.bottom);
        if (isSelected) {
            canvas.drawPath(dataPath, selectedPaint);
        } else {
            canvas.drawPath(dataPath, paint);
        }
    }

    @Override
    public void onDrawLabel(Canvas canvas, RectF rectF, boolean isSelected, String label) {
        if (isSelected) {
            textPaint.setColor(labelSelectedColor);
        } else {
            textPaint.setColor(labelColor);
        }
        canvas.drawText(label, (int) rectF.centerX(), textHeight, textPaint);
    }

    @Override
    public void onDrawBackground(Canvas canvas, RectF rectF, boolean isSelected) {
        int topPosY = (int) (rectF.top + textHeight);
        canvas.drawLine(rectF.centerX(), topPosY, rectF.centerX(), rectF.bottom, baseLinePaint);
    }

    @Override
    protected int getMaxPosition() {
//        return indicatorDataModels.size() * viewPort.getIntervalX() - width;
        return Math.round(dataValues.size() * viewPort.getIntervalX() - width / 2 - viewPort.getIntervalX() / 2);
    }

    @Override
    protected int getMinPosition() {
//        return 0;
        return Math.round(-width / 2 + viewPort.getIntervalX() / 2);
    }

    @Override
    protected int getDataPaddingTop() {
        return 0;
    }

    @Override
    protected int getDataPaddingBottom() {
        return 0;
    }

    @Override
    protected boolean selectAfterScroll() {
        return true;
    }
}
