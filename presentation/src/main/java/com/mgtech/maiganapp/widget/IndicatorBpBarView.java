package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2018/4/4.
 * 指示计算结果的bar
 */

public class IndicatorBpBarView extends View {
    private Paint paintBar;
    private Paint paintText;
    private Path pathBar;
    private int colorHigher;
    private int colorNormal;
    private int colorHighest;
    private int colorValue;
    private int barWidth;
    private int textHeight;
    private int textPadding;
    private int barPadding;
    private float psHighestValue = 140;
    private float psHigherValue = 120;
    private float psValue = 90;

    private float pdHighestValue = 90;
    private float pdHigherValue = 80;
    private float pdValue = 50;

    private int textYOffset;
    private String textPs;
    private String textPd;

    private int dataRadius;
    private int dataWidth;
    private int dataHeight;
    private int dataLineLength;

    public IndicatorBpBarView(Context context) {
        super(context);
        init(context);
    }

    public IndicatorBpBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndicatorBpBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int textSize = ViewUtils.sp2px(metrics, 12);

        paintBar = new Paint();
        paintBar.setAntiAlias(true);

        pathBar = new Path();

        colorNormal = ContextCompat.getColor(context, R.color.measure_result_bar_normal);
        colorHigher = ContextCompat.getColor(context, R.color.measure_result_bar_higher0);
        colorHighest = ContextCompat.getColor(context, R.color.measure_result_bar_higher1);
        colorValue = ContextCompat.getColor(context, R.color.grey_text);

        paintText = new Paint();
        paintText.setColor(colorValue);
        paintText.setAntiAlias(true);
        paintText.setTextSize(textSize);
        paintText.setTextAlign(Paint.Align.CENTER);

        textPadding = ViewUtils.dp2px(metrics.density, 4);
        textHeight = ViewUtils.dp2px(metrics.density, 20);
        barWidth = ViewUtils.dp2px(6);
        dataWidth = ViewUtils.dp2px(2);
        dataRadius = ViewUtils.dp2px(3.5f);
        dataLineLength = 2 * barWidth;
        dataHeight = dataRadius + dataLineLength;
        barPadding = ViewUtils.dp2px(metrics.density, 16);
        textYOffset = ViewUtils.getTextOffset(paintText);

        textPs = context.getString(R.string.PS);
        textPd = context.getString(R.string.PD);
    }

    public void setValue(float ps, float pd) {
        this.psValue = ps;
        this.pdValue = pd;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(textHeight * 2 + textPadding * 2 + 2 * barPadding + dataHeight * 2 - barWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBar(canvas);
        drawData(canvas);
        drawUpText(canvas);
    }

    private void drawData(Canvas canvas) {

        // 范围
        float psStartValue = 0;
        float psEndValue = 200;

        float pdStartValue = 0;
        float pdEndValue = 200;
        int width = (getWidth() - barPadding * 2)/3;
        // 数据点的位置
        int pPs, pPd;
        if (psValue <= psHigherValue) {
            pPs = Math.round(width * psValue / psHigherValue) + barPadding;
        } else if (psValue >= psHighestValue) {
            pPs = Math.round(width * (psValue- psHighestValue) / (psEndValue - psHighestValue)) + width * 2 + barPadding;
        } else {
            pPs =Math.round(width * (psValue- psHigherValue) / (psHighestValue - psHigherValue)) + width  + barPadding;
        }
        if (pdValue <= pdHigherValue) {
            pPd = Math.round(width * pdValue / pdHigherValue) + barPadding;
        } else if (pdValue >= pdHighestValue) {
            pPd = Math.round(width * (pdValue- pdHighestValue) / (pdEndValue - pdHighestValue)) + width * 2 + barPadding;
        } else {
            pPd =Math.round(width * (pdValue- pdHigherValue) / (pdHighestValue - pdHigherValue)) + width  + barPadding;
        }
        if (pPs < barPadding){
            pPs = barPadding;
        }else if ( pPs > getWidth() - barPadding){
           pPs = getWidth() - barPadding;
        }
        if (pPd < barPadding){
            pPd = barPadding;
        }else if ( pPd > getWidth() - barPadding){
            pPd = getWidth() - barPadding;
        }
//        int pPs = Math.round(positionPs * (getWidth()-barPadding*2))+barPadding;
//        int pPd = Math.round(positionPd * (getWidth()-barPadding*2))+barPadding;
        int top = textHeight + textPadding + dataHeight - barWidth;
        int bottom = top + barWidth;
        // 数据点颜色
        paintBar.setColor(colorValue);
        paintBar.setStrokeWidth(dataWidth);
        canvas.drawLine(pPs, bottom, pPs, bottom - dataLineLength, paintBar);
        canvas.drawLine(pPd, top, pPd, top + dataLineLength, paintBar);
        canvas.drawCircle(pPs, bottom - dataLineLength, dataRadius, paintBar);
        canvas.drawCircle(pPd, top + dataLineLength, dataRadius, paintBar);
    }

    /**
     * 绘制bar
     *
     * @param canvas 画布
     */
    private void drawBar(Canvas canvas) {
        int top = textHeight + textPadding + dataHeight - barWidth;
        int bottom = top + barWidth;
        int middle1 = getMiddle1();
        int middle2 = getMiddle2();
        pathBar.reset();
        pathBar.moveTo(barPadding + barWidth / 2, top);
        pathBar.lineTo(middle1, top);
        pathBar.lineTo(middle1, bottom);
        pathBar.lineTo(barPadding + barWidth / 2, bottom);
        pathBar.arcTo(barPadding, top, barPadding + barWidth, bottom, 90, 180, true);
        pathBar.close();
        paintBar.setColor(colorNormal);
        canvas.drawPath(pathBar, paintBar);

        pathBar.reset();
        pathBar.moveTo(middle2, top);
        pathBar.lineTo(getWidth() - barWidth / 2 - barPadding, top);
        pathBar.arcTo(getWidth() - barWidth - barPadding, top, getWidth() - barPadding, bottom, -90, 180, false);
        pathBar.lineTo(middle2, bottom);
        pathBar.lineTo(middle2, top);
        pathBar.close();
        paintBar.setColor(colorHighest);
        canvas.drawPath(pathBar, paintBar);

        paintBar.setColor(colorHigher);
        canvas.drawRect(middle1, top, middle2, bottom, paintBar);
    }

    private void drawUpText(Canvas canvas) {
        float m1 = getMiddle1();
        float m2 = getMiddle2();
        float centerHeight = 2 * dataHeight - barWidth;
        float yPs = textHeight + textYOffset;
        float yPd = textHeight + centerHeight + textPadding * 2 - paintText.ascent();
        paintText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(Math.round(psHigherValue)), m1, yPs, paintText);
        canvas.drawText(String.valueOf(Math.round(pdHigherValue)), m1, yPd, paintText);
        canvas.drawText(String.valueOf(Math.round(psHighestValue)), m2, yPs, paintText);
        canvas.drawText(String.valueOf(Math.round(pdHighestValue)), m2, yPd, paintText);
        paintText.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(textPs, barPadding, yPs, paintText);
        canvas.drawText(textPd, barPadding, yPd, paintText);
    }


    private int getMiddle1() {
        return (getWidth() - barPadding * 2) / 3 + barPadding;
    }

    private int getMiddle2() {
        return (getWidth() - barPadding * 2) / 3 * 2 + barPadding;
    }
}
