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
public class ActivityWeeklyReportGraphView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float singleWidth = ViewUtils.dp2px(10);
    private float singleHeight = ViewUtils.dp2px(60);
    private float textPadding = ViewUtils.dp2px(2);
    private float textSize = ViewUtils.dp2px(10);
    private float paddingSide = ViewUtils.dp2px(16);
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private List<IndicatorDataModel> dataList = new ArrayList<>();
    private String[] weekdays;
    private int number = 7;
    private float maxValue = 200;
    private float minValue = 40;
    private float singlePadding;
    private int backgroundColor;
    private int abnormalColor;
    private int normalColor;

    public ActivityWeeklyReportGraphView(Context context) {
        super(context);
        init(context);
    }

    public ActivityWeeklyReportGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ActivityWeeklyReportGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        paint.setStrokeWidth(singleWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setTextSize(textSize);
        textPaint.setColor(ContextCompat.getColor(context, R.color.light_grey_text));
        textPaint.setTextAlign(Paint.Align.CENTER);

        backgroundColor = ContextCompat.getColor(context, R.color.background_grey);
        abnormalColor = ContextCompat.getColor(context, R.color.warningColor);
        normalColor = ContextCompat.getColor(context, R.color.primaryGreen);

        weekdays = getResources().getStringArray(R.array.week);
    }

    /**
     * 左闭右开
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public void setCalendar(long startTime, long endTime){
        startCalendar.setTimeInMillis(startTime);
        endCalendar.setTimeInMillis(endTime);
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
        singlePadding = (w-paddingSide*2 - singleWidth * number)/(number-1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackBar(canvas);
        drawDataBar(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas){
//        Calendar calendar = Calendar.getInstance();
        long start = startCalendar.getTimeInMillis();
        float unit = 24 * 60 * 60 *1000;
        long end = endCalendar.getTimeInMillis();
        int index = 0;
        while (start < end){
//            calendar.setTimeInMillis(start);
            String day = weekdays[index%7];
            int x = (int) (index * (singleWidth + singlePadding) + singleWidth/2f + paddingSide);
            int y = (int) (getHeight()- textPadding );
            canvas.drawText(day,x,y,textPaint);
            start += unit;
            index++;
        }
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
        paint.setColor(color);
        min = Math.max(min,minValue);
        max = Math.min(max,maxValue);
        int x = (int) (index * (singleWidth + singlePadding) + singleWidth/2f+ paddingSide);
        int top = (int) ((singleHeight- singleWidth) * (maxValue - max)/(maxValue-minValue)+ singleWidth/2f);
        int bottom = (int) ((singleHeight- singleWidth) * (maxValue -min)/(maxValue - minValue) + singleWidth/2f);
        canvas.drawLine(x,top,x,bottom,paint);
    }

}
