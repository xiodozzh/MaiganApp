package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.LunarCalendar;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.Calendar;

/**
 * Created by zhaixiang on 2017/11/24.
 * 日历单页
 */

public class CalendarViewPagerItem extends View {
    private String[] weekText;
    private Paint weekHeaderTitlePaint;
    private Paint calendarPaint;
    private Paint selectPaint;
    private Paint lunarCalendarPaint;
    private DisplayMetrics displayMetrics;
    private int headerHeight;
    private int headerTextHeight;
    private int calendarTextHeight;
    private int lunarCalendarTextHeight;
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar workCalendar = Calendar.getInstance();
    private LunarCalendar lunarCalendar;
    private Decorate decorate;
    private int todayTextColor;
    private int selectTextColor;
    private int defaultTextColor;
    private static final Calendar TODAY = Calendar.getInstance();
    private GestureDetector gestureDetector;
    private Callback callback;

    public interface Decorate {
        void drawCalendar(int l, int t, int r, int b, Calendar calendar, Canvas canvas);
    }

    public interface Callback{
        boolean isSelectDay(Calendar calendar);

        void selectDay(Calendar calendar);
    }

    public CalendarViewPagerItem(Context context) {
        super(context);
        init(context);
    }

    public CalendarViewPagerItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarViewPagerItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void init(Context context) {
        this.lunarCalendar = new LunarCalendar();
        this.weekHeaderTitlePaint = new Paint();
        this.calendarPaint = new Paint();
        this.lunarCalendarPaint = new Paint();
        this.selectPaint = new Paint();
        this.todayTextColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        this.selectTextColor = ContextCompat.getColor(context, R.color.white);
        this.defaultTextColor = ContextCompat.getColor(context, R.color.grey800);

        this.weekHeaderTitlePaint.setTextAlign(Paint.Align.CENTER);
        this.weekHeaderTitlePaint.setAntiAlias(true);
        this.calendarPaint.setTextAlign(Paint.Align.CENTER);
        this.calendarPaint.setAntiAlias(true);
        this.lunarCalendarPaint.setTextAlign(Paint.Align.CENTER);
        this.lunarCalendarPaint.setAntiAlias(true);
        this.selectPaint.setColor(todayTextColor);
        this.selectPaint.setAntiAlias(true);

        this.displayMetrics = context.getResources().getDisplayMetrics();
        this.headerHeight = ViewUtils.dp2px(20);
        this.weekText = context.getResources().getStringArray(R.array.week);
        setHeaderTitleSize(10);
        setCalendarSize(16);
        setLunarCalendarSize(9);
        setDisplayDay(Calendar.getInstance());
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int index = getIndex(e.getX());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startCalendar.getTimeInMillis());
                calendar.add(Calendar.DATE, index);
                if (callback != null){
                    callback.selectDay(calendar);
                }
                invalidate();
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    public void setHeaderTitleSize(int sp) {
        this.weekHeaderTitlePaint.setTextSize(ViewUtils.sp2px(displayMetrics, sp));
        Paint.FontMetrics metrics = weekHeaderTitlePaint.getFontMetrics();
        headerTextHeight = (int) Math.abs(Math.ceil(metrics.top));
        invalidate();
    }

    public void setCalendarSize(int sp) {
        this.calendarPaint.setTextSize(ViewUtils.sp2px(displayMetrics, sp));
        Paint.FontMetrics metrics = calendarPaint.getFontMetrics();
        calendarTextHeight = (int) Math.abs(Math.ceil(metrics.top));
        invalidate();
    }

    public void setLunarCalendarSize(int sp) {
        this.lunarCalendarPaint.setTextSize(ViewUtils.sp2px(displayMetrics, sp));
        Paint.FontMetrics metrics = lunarCalendarPaint.getFontMetrics();
        lunarCalendarTextHeight = (int) Math.abs(Math.ceil(metrics.top));
        invalidate();
    }

    public void setWeekText(String[] week) {
        if (week != null && week.length == 7) {
            this.weekText = week;
            invalidate();
        }
    }

    public void setDisplayDay(Calendar day) {
        startCalendar = Utils.getFirstDayOfWeek(day);
    }

    public void selectDay(Calendar day) {
//        selectCalendar.setTimeInMillis(day.getTimeInMillis());
    }

    public void setDecorate(Decorate decorate) {
        this.decorate = decorate;
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawHeader(canvas);
        drawDay(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
    }

//    private boolean isSameWeek(Calendar calendar) {
//        long time = calendar.getTimeInMillis() - startCalendar.getTimeInMillis();
//        return time >= 0 && time < 7 * 24 * 3600 * 1000;
//    }

    private boolean isSameDay(Calendar calendar) {
        long time = calendar.getTimeInMillis() - workCalendar.getTimeInMillis();
        return time >= 0 && time < 24 * 3600 * 1000;
    }

    private void drawDay(Canvas canvas) {
        int w = getWidth() / 7;
        int height = getHeight();
        int h1 = getHeight() / 4 + headerHeight * 3 / 4 + calendarTextHeight / 2;
        int h2 = h1 + lunarCalendarTextHeight + 8;

        workCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
        Log.i("dayTest", "drawDay: " + workCalendar.toString());
        for (int i = 0; i < 7; i++) {
            if (decorate != null) {
                decorate.drawCalendar(w * i, 0, (i + 1) * w, height, workCalendar, canvas);
            } else {
                if (callback != null && callback.isSelectDay(workCalendar)) {
                    setTextColor(selectTextColor);
                    canvas.drawCircle(w * i + w / 2, height / 2 + headerHeight / 3, Math.min(w, height / 2 - headerHeight / 2) - 4, selectPaint);
                } else if (isSameDay(TODAY)) {
                    setTextColor(todayTextColor);
                } else {
                    setTextColor(defaultTextColor);
                }
                canvas.drawText(workCalendar.get(Calendar.DATE) + "", w * i + w / 2, h1, calendarPaint);
                canvas.drawText(lunarCalendar.getLunarDayString(workCalendar), w * i + w / 2, h2, lunarCalendarPaint);
            }
            workCalendar.add(Calendar.DATE, 1);
        }
    }

    private void setTextColor(int color) {
        calendarPaint.setColor(color);
        lunarCalendarPaint.setColor(color);
    }

    private void drawHeader(Canvas canvas) {
        int w = getWidth() / 7;
        int h = headerHeight / 2 + headerTextHeight / 2;
        for (int i = 0; i < 7; i++) {
            canvas.drawText(weekText[i], w * i + w / 2, h, weekHeaderTitlePaint);
        }
    }

    private int getIndex(float x) {
        int w = getWidth() / 7;
        return (int) (x / w);
    }

}
