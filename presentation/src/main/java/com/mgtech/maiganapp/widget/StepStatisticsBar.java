package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaixiang on 2017/11/6.
 * 计步统计图表
 */

public class StepStatisticsBar extends View {
    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int MAX_VALUE = 10000;
    public static final float DATA_WIDTH_INTERVAL_RATION = 0.3f;
    private int width;
    private int height;
    private Paint axisPaint;
    private Paint dataPaint;
    private int baseColor;
    private int changeColor;
    private int axisTextHeight;
    private int axisTextPaddingTop = 8;

    private int timeSpanType = DAY;
    private List<Pair<Integer, Integer>> data;
    private List<Pair<Integer, Integer>> dayData;
    private List<Pair<Integer, Integer>> monthData;
    private List<Pair<Integer, Integer>> weekData;
    private List<Pair<Integer, String>> axisText;

    private String[] weekTexts;
    private Path dataPath;
    private int maxNumber;
    private int padding = 30;
    private Calendar calendar = Calendar.getInstance();


    public StepStatisticsBar(Context context) {
        super(context);
        init(context);
    }

    public StepStatisticsBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StepStatisticsBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        baseColor = ContextCompat.getColor(context, R.color.sport_color_select);
        changeColor = Color.argb(255, 132, 194, 255);
        axisPaint = new Paint();
        axisPaint.setColor(baseColor);
        axisPaint.setStrokeWidth(1);
        axisPaint.setAntiAlias(true);
        axisPaint.setTextAlign(Paint.Align.CENTER);

        axisPaint.setTextSize(16);
        Paint.FontMetrics metrics = axisPaint.getFontMetrics();
        axisTextHeight = (int) Math.abs(Math.ceil(metrics.top));

        dataPaint = new Paint();
        dataPaint.setColor(baseColor);
        dataPaint.setStyle(Paint.Style.FILL);
        dataPaint.setAntiAlias(true);

        weekTexts = context.getResources().getStringArray(R.array.week);
        dataPath = new Path();
        initData();
        setTimeSpanType(DAY);
    }

    private void initData() {
        this.dayData = new ArrayList<>();
        this.weekData = new ArrayList<>();
        this.monthData = new ArrayList<>();
        this.axisText = new ArrayList<>();
        testData();
    }

    private void testData() {
        for (int i = 0; i < 24; i++) {
            dayData.add(new Pair<>(i, (int) (Math.random() * 10000)));
        }
        for (int i = 1; i <= Utils.getMonthDay(calendar); i++) {
            monthData.add(new Pair<>(i, (int) (Math.random() * 10000)));
        }
        for (int i = 0; i < 7; i++) {
            weekData.add(new Pair<>(i, (int) (Math.random() * 10000)));
        }
    }


    public int getTimeSpanType() {
        return timeSpanType;
    }

    public void setTimeSpanType(int type) {
        switch (type) {
            case DAY:
                initDayAxis();
                data = dayData;
                maxNumber = 24;
                break;
            case WEEK:
                maxNumber = 7;
                initWeekAxis();
                data = weekData;
                break;
            case MONTH:
                data = monthData;
                maxNumber = Utils.getMonthDay(calendar);
                initMonthAxis();
                break;
            default:
                data = dayData;
                maxNumber = 24;
                initDayAxis();
        }
        timeSpanType = type;
        invalidate();
    }

    private void initDayAxis() {
        axisText.clear();
        axisText.add(new Pair<>(0, "00:00"));
        axisText.add(new Pair<>(4, "04:00"));
        axisText.add(new Pair<>(8, "08:00"));
        axisText.add(new Pair<>(12, "12:00"));
        axisText.add(new Pair<>(16, "16:00"));
        axisText.add(new Pair<>(20, "20:00"));
        axisText.add(new Pair<>(24, "24:00"));
    }

    private void initWeekAxis() {
        axisText.clear();
        for (int i = 0; i < 7; i++) {
            axisText.add(new Pair<>(i, weekTexts[i]));
        }
    }

    private void initMonthAxis() {
        axisText.clear();
        for (int i = 1; i <= maxNumber; i = i + 4) {
            axisText.add(new Pair<>(i, String.valueOf(i)));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        drawAxis(canvas);
        drawAxisText(canvas);
        drawData(canvas);
    }

    /**
     * 绘制轴线
     *
     * @param canvas 画布
     */
    private void drawAxis(Canvas canvas) {
        canvas.drawLine(0, height - axisTextHeight - axisTextPaddingTop, width, height - axisTextHeight - axisTextPaddingTop, axisPaint);
    }

    /**
     * 绘制轴线坐标
     *
     * @param canvas 画布
     */
    private void drawAxisText(Canvas canvas) {
        if (axisText == null || axisText.isEmpty()) {
            return;
        }
        int y = height - 5;
        int x = (width - padding * 2) / (maxNumber - 1);
        for (int i = 0; i < axisText.size(); i++) {
            Pair<Integer, String> pair = axisText.get(i);
            canvas.drawText(pair.second, (float) (x * pair.first + padding), y, axisPaint);
        }
    }

    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        if (data == null || data.isEmpty()) {
            return;
        }
        dataPath.reset();
//        int padding = width/(axisText.length)/2;
        int dx = (width - 2 * padding) / (maxNumber - 1);
        int w = Math.min((int) (dx * DATA_WIDTH_INTERVAL_RATION),15);

        int base = height - axisTextHeight - 2 * axisTextPaddingTop - w;
        double dy = (base - 10) / (float) MAX_VALUE;
        for (Pair<Integer, Integer> pair : data) {
            int x = pair.first * dx + padding-w/2;
            int y = (int) Math.round(pair.second * dy);
            Shader shader = new LinearGradient(0, base, 0, Math.min(base - y,20), changeColor, baseColor, Shader.TileMode.CLAMP);
            dataPaint.setShader(shader);
            dataPath.reset();
            dataPath.moveTo(x, base);
            dataPath.lineTo(x, base - y);
            dataPath.arcTo(x, base - y - w / 2, x + w, base - y + w / 2, -180, 180, false);
            dataPath.lineTo(x + w, base);
            dataPath.arcTo(x, base - w / 2, x + w, base + w / 2, 0, 180, false);
            canvas.drawPath(dataPath, dataPaint);
        }
    }


}
