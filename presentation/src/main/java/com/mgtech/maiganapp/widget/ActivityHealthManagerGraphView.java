package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhaixiang
 */
public class ActivityHealthManagerGraphView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float singleWidth = ViewUtils.dp2px(5);
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private List<IndicatorDataModel> dataList = new ArrayList<>();
    private int number = 30;
    private float maxValue = 200;
    private float minValue = 40;
    private float singlePadding;
    private int backgroundColor;
    private int abnormalColor;
    private int normalColor;

    public ActivityHealthManagerGraphView(Context context) {
        super(context);
        init(context);
    }

    public ActivityHealthManagerGraphView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ActivityHealthManagerGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        paint.setStrokeWidth(singleWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        backgroundColor = ContextCompat.getColor(context, R.color.background_grey);
        abnormalColor = ContextCompat.getColor(context, R.color.warningColor);
        normalColor = ContextCompat.getColor(context, R.color.colorPrimary);
    }

    public void setTimes(long startTime, long endTime){
        startCalendar.setTimeInMillis(startTime);
        startCalendar.set(Calendar.HOUR_OF_DAY,0);
        startCalendar.set(Calendar.MINUTE,0);
        startCalendar.set(Calendar.SECOND,0);
        startCalendar.set(Calendar.MILLISECOND,0);

        endCalendar.setTimeInMillis(endTime);
        endCalendar.set(Calendar.HOUR_OF_DAY,0);
        endCalendar.set(Calendar.MINUTE,0);
        endCalendar.set(Calendar.SECOND,0);
        endCalendar.set(Calendar.MILLISECOND,0);
        number = Utils.calculateNumberOfDaysBetweenDays(startCalendar, endCalendar);
        invalidate();
    }

    public void setData(List<IndicatorDataModel> list){
        dataList.clear();
        if (list != null) {
            dataList.addAll(list);
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        singlePadding = (w - singleWidth * number)/(number-1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackBar(canvas);
        drawDataBar(canvas);
    }

    /**
     * 绘制背景
     * @param canvas 画布
     */
    private void drawBackBar(Canvas canvas) {
        for (int i = 0; i < number; i++) {
            drawBar(canvas,i,minValue,maxValue,backgroundColor);
        }
    }

    /**
     * 绘制数据
     * @param canvas 画布
     */
    private void drawDataBar(Canvas canvas){
        long start = startCalendar.getTimeInMillis();
        float unit = 24 * 60 * 60 *1000;
        for (IndicatorDataModel model :dataList) {
            int index = (int) ((model.time - start)/unit);
            if (index < 0 || index >= number){
                continue;
            }
            int color = model.achieveGoal? normalColor:abnormalColor;
            drawBar(canvas,index,model.pd,model.ps,color);
        }
    }

    private void drawBar(Canvas canvas, int index, float min, float max, int color){
        if (min > max){
            return;
        }
        min = Math.max(min,minValue);
        max = Math.min(max,maxValue);
        paint.setColor(color);
        int x = (int) (index * (singleWidth + singlePadding) + singleWidth/2f);
        int top = (int) ((getHeight()- singleWidth) * (maxValue - max)/(maxValue-minValue)+ singleWidth/2f);
        int bottom = (int) ((getHeight()- singleWidth) * (maxValue -min)/(maxValue - minValue) + singleWidth/2f);
        canvas.drawLine(x,top,x,bottom,paint);
    }
}
