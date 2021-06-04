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
 * 运动记录 ChartView
 */

public class StepBarChartView extends BarChartView {
    private Paint paint;
    private Paint selectedPaint;
    private Paint bgPaint;
    private Paint textPaint;
    private Path dataPath;
    /**
     * 渐变颜色
     */
    private int textHeight;

    private int labelColor;
    private int bgStrokeColor;
    private int bgSelectedColor;
    private int bgColor;
    private int unSelectedColor;
    private int colorLight;
    private int colorDark;
    private int dataWidth;
    private int bottomPadding;
    /**
     * 基础颜色
     */
    private int selectedColor;
//    private float density;
    private DisplayMetrics displayMetrics;

    /**
     * 字体大小
     */
    private static final int textSizeDefault = 12;

    public StepBarChartView(Context context) {
        super(context);
        init(context);
    }

    public StepBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StepBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initColor(context);
        displayMetrics = context.getResources().getDisplayMetrics();
//        density = displayMetrics.density;
        dataWidth = ViewUtils.dp2px(20);
        bottomPadding = ViewUtils.dp2px( 16);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(selectedColor);

        selectedPaint = new Paint(paint);
        selectedPaint.setColor(selectedColor);

        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(ViewUtils.sp2px(displayMetrics, textSizeDefault));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        bgPaint = new Paint();
        bgPaint.setStrokeWidth(1f);

        dataPath = new Path();

        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        textHeight = (int) Math.abs(Math.ceil(metrics.top));
    }

    public void setTextSizeInSp(int textSize){
        textPaint.setTextSize(ViewUtils.sp2px(displayMetrics, textSize));
    }

    /**
     * 颜色初始化
     *
     * @param context 上下文
     */
    private void initColor(Context context) {
        this.selectedColor = ContextCompat.getColor(context, R.color.colorPrimary);
        this.labelColor = Color.parseColor("#848484");
        this.unSelectedColor = ContextCompat.getColor(context, R.color.colorPrimaryLight);
        this.bgColor = Color.rgb(246,246,246);
        this.bgStrokeColor = Color.rgb(213,213,213);
        this.bgSelectedColor = Color.WHITE;
        this.colorDark = ContextCompat.getColor(context,R.color.colorPrimaryLight);
        this.colorLight = ContextCompat.getColor(context,R.color.colorPrimaryWhite);
    }

    @Override
    public void onDrawData(Canvas canvas, RectF rectF, boolean isSelected, Integer data) {
        dataPath.reset();
        int half = dataWidth / 2;
        int posX = (int) rectF.centerX();
        int bottom = (int) (rectF.bottom - textHeight - bottomPadding - half);
        int top = Math.round((bottom - data * viewPort.getIntervalY()));
        Shader shader = new LinearGradient(0, top, 0, height, colorDark, colorLight, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        dataPath.moveTo(posX - half, bottom );
        dataPath.lineTo(posX - half, top);
        dataPath.arcTo(posX - half, top - half, posX + half, top + half, -180, 180, false);
        dataPath.lineTo(posX + half, bottom );
        dataPath.arcTo(posX - half, bottom - half, posX + half, bottom + half, 0, 180, false);
//        dataPath.arcTo(posX - half, bottom -half, posX + half, bottom + half, 0, 180, false);
        if (isSelected) {
            canvas.drawPath(dataPath, selectedPaint);
        } else {
            canvas.drawPath(dataPath, paint);
        }
    }

    @Override
    public void onDrawLabel(Canvas canvas, RectF rectF, boolean isSelected, String label) {
        if (isSelected) {
            textPaint.setColor(selectedColor);
        } else {
            textPaint.setColor(labelColor);
        }
        canvas.drawText(label, (int) rectF.centerX(), rectF.bottom - bottomPadding/2, textPaint);
    }

    @Override
    public void onDrawBackground(Canvas canvas, RectF rectF, boolean isSelected) {
        bgPaint.setStyle(Paint.Style.FILL);
        if (isSelected) {
            bgPaint.setColor(bgSelectedColor);
//            bgPaint.setStrokeWidth(1.5f);
        } else {
            bgPaint.setColor(bgColor);
//            bgPaint.setStrokeWidth(1f);
        }
        canvas.drawRect(rectF, bgPaint);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(bgStrokeColor);
        canvas.drawRect(rectF, bgPaint);

    }

    @Override
    protected int getMaxPosition() {
        return Math.round(dataValues.size() * viewPort.getIntervalX() - width);
    }

    @Override
    protected int getMinPosition() {
        return 0;
    }

    @Override
    protected int getDataPaddingTop() {
        return (int) (bottomPadding + dataWidth*1.5f);
    }

    @Override
    protected int getDataPaddingBottom() {
        return bottomPadding;
    }

    @Override
    protected boolean selectAfterScroll() {
        return false;
    }
}
