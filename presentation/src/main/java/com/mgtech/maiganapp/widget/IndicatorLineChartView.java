package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import com.mgtech.domain.entity.IndicatorDescription;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IndicatorStatisticDataModel;
import com.mgtech.maiganapp.utils.IndicatorUtils;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhaixiang
 */
public class IndicatorLineChartView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint limitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint yAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint titleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static final int INDEX_HR = 0;
    public static final int INDEX_BP = 1;
    public static final int INDEX_V0 = 2;
    public static final int INDEX_TPR = 3;
    public static final int INDEX_PM = 4;
    public static final int INDEX_CO = 5;

    public static final int INDEX_PD = 6;

    private static final int[] RANGE_HR = IndicatorUtils.RANGE_HR;
    private static final int[] RANGE_PS = IndicatorUtils.RANGE_PS;
    private static final int[] RANGE_PD = IndicatorUtils.RANGE_PD;
    private static final int[] RANGE_V0 = IndicatorUtils.RANGE_V0;
    private static final int[] RANGE_TPR = IndicatorUtils.RANGE_TPR;
    private static final int[] RANGE_PM = IndicatorUtils.RANGE_PM;
    private static final int[] RANGE_CO = IndicatorUtils.RANGE_CO;

    private static final int LINE_COUNT = 6;

    private static final int DOT_RADIUS = ViewUtils.dp2px(4);

    private static final int SELECT_REGION_RADIUS = ViewUtils.dp2px(8);

    private String dayUnitText = "时";
    private String monthUnitText = "日";

    public static final int DAY = 0;
    public static final int MONTH = 1;

    private int show;
    /**
     * 单位水平距离
     */
    private float unitX;
    /**
     * 单位垂直距离
     */
    private float[] unitYs = new float[LINE_COUNT];

    private float offsetX;
    private float offsetY;

    private int titleHeight = ViewUtils.dp2px(26);
    private int axisYLabelWidth = ViewUtils.dp2px(30);
    private int axisYLabelTextPadding = ViewUtils.dp2px(8);
    private int axisXLabelHeight = ViewUtils.dp2px(14);
    private int paddingLeft = ViewUtils.dp2px(8);
    private int paddingRight = ViewUtils.dp2px(35);
    private int paddingTop = ViewUtils.dp2px(0);
    private int paddingBottom = ViewUtils.dp2px(8);
    private int titleRadius = ViewUtils.dp2px(2);
    private int dataLineWidth = ViewUtils.dp2px(2f);

    private RectF realRect = new RectF();

    private IndicatorStatisticDataModel data;
    private Calendar calendar = Calendar.getInstance();
    private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleX = 1;
    private float scaleY = 1;
    private OverScroller scroller;
    private int selectIndex = -1;
    private int selectLine = -1;
    private float titleTextYOffset;

    private static final String DAY_TIME_FORMAT = "EEE HH:mm:ss";
    private static final String MONTH_TIME_FORMAT = "yyyy-MM-dd EEE";
    private Rect titleTextBounds = new Rect();

    private static final float[] INIT_SCOPE_Y = {
            RANGE_HR[1] - RANGE_HR[0],
            RANGE_PS[1] - RANGE_PS[0],
            RANGE_V0[1] - RANGE_V0[0],
            RANGE_TPR[1] - RANGE_TPR[0],
            RANGE_PM[1] - RANGE_PM[0],
            RANGE_CO[1] - RANGE_CO[0]
    };

    private static final float[] MIN_Y = {
            RANGE_HR[0],
            RANGE_PS[0],
            RANGE_V0[0],
            RANGE_TPR[0],
            RANGE_PM[0],
            RANGE_CO[0]
    };
    private static final float[] MAX_Y = {
            RANGE_HR[1],
            RANGE_PS[1],
            RANGE_V0[1],
            RANGE_TPR[1],
            RANGE_PM[1],
            RANGE_CO[1]
    };
    private String[] dataUnitText = {
            "bpm",
            "mmHg",
            "Pa*s",
            "mmHg*min/L",
            "mmHg",
            "L/min"
    };

    private static final float[][]DEFAULT_LIMIT_LINES = new float[][]{
            IndicatorUtils.NORMAL_RANGE_HR,
            IndicatorUtils.NORMAL_RANGE_PS,
            IndicatorUtils.NORMAL_RANGE_V0,
            IndicatorUtils.NORMAL_RANGE_TPR,
            IndicatorUtils.NORMAL_RANGE_PM,
            IndicatorUtils.NORMAL_RANGE_CO
    };

    private float[][] limitValue = new float[][]{
            IndicatorUtils.NORMAL_RANGE_HR,
            IndicatorUtils.NORMAL_RANGE_PS,
            IndicatorUtils.NORMAL_RANGE_V0,
            IndicatorUtils.NORMAL_RANGE_TPR,
            IndicatorUtils.NORMAL_RANGE_PM,
            IndicatorUtils.NORMAL_RANGE_CO,
            IndicatorUtils.NORMAL_RANGE_PD,


    };

    private int[] TYPES = new int[]{
            NetConstant.HR,
            NetConstant.PS,
            NetConstant.V0,
            NetConstant.TPR,
            NetConstant.PM,
            NetConstant.CO
    };

    private static final float[] LIMIET_RANGE_PD = IndicatorUtils.NORMAL_RANGE_PD;

    private float[] limitLineBp = {56, 97, 140};

    private static final float[] INIT_DELTA_Y = new float[]{
            20,
            25,
            1.5f,
            8,
            20,
            3
    };

    /**
     * 起始Y坐标精度
     */
    private static final float[] INIT_ACCURACY = {
            20,
            25,
            1,
            8,
            20,
            3,
    };

    private int dataColor1;
    private int dataColor2;
    private int selectDataColor;
    private int axisColor;
    private int warningRedColor;
    private int warningYellowColor;

    private int timeMode;

    private float fontTop;
    private float fontBottom;

    private Path limitPath = new Path();

    public IndicatorLineChartView(Context context) {
        super(context);
        init(context, null);
    }

    public IndicatorLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndicatorLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        data = new IndicatorStatisticDataModel();
        scroller = new OverScroller(context);
        dataColor1 = ContextCompat.getColor(context, R.color.colorPrimary);
        dataColor2 = ContextCompat.getColor(context, R.color.primaryGreen);
        axisColor = ContextCompat.getColor(context, R.color.empty_data_text_color);
        warningRedColor = ContextCompat.getColor(context, R.color.warningColor);
        warningYellowColor = ContextCompat.getColor(context, R.color.warningYellow);

        selectDataColor = dataColor1;
        paintDot.setColor(dataColor1);
        paintDot.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(ViewUtils.sp2px(12));


        fontTop = textPaint.getFontMetrics().top;
        fontBottom = textPaint.getFontMetrics().bottom;

        titleTextPaint.setTextAlign(Paint.Align.CENTER);
        titleTextPaint.setTextSize(ViewUtils.sp2px(14));
        titleTextPaint.setColor(Color.WHITE);
        float top = titleTextPaint.getFontMetrics().top;
        float bottom = titleTextPaint.getFontMetrics().bottom;
        titleTextYOffset = (top + bottom) / 2;

        detector = new GestureDetector(context, onGestureListener);
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);

        limitPaint.setColor(Color.RED);
        limitPaint.setStrokeWidth(ViewUtils.dp2px(1f));
        limitPaint.setStyle(Paint.Style.STROKE);

        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{16, 8}, 1);
        limitPaint.setPathEffect(dashPathEffect);

        yAxisPaint.setColor(axisColor);
        yAxisPaint.setStrokeWidth(ViewUtils.dp2px(1f));
        yAxisPaint.setStyle(Paint.Style.STROKE);

        dayUnitText = context.getString(R.string.line_chart_day_unit);
        monthUnitText = context.getString(R.string.line_chart_month_unit);

    }

    public void setSex(int sex){
        for (int i = 0; i < TYPES.length; i++) {
//            if (i == INDEX_BP){
//                continue;
//            }
            IndicatorDescription indicatorDescription = IndicatorDescription.get(TYPES[i],sex);
            if (indicatorDescription == null){
                limitValue[i] = DEFAULT_LIMIT_LINES[i];
            }else{
                limitValue[i] = new float[]{indicatorDescription.getLowerLimit(),indicatorDescription.getUpperLimit()};
            }
            Log.i("chart", TYPES[i] + "limit: "+ Arrays.toString(limitValue[i]) + sex);
        }
//        IndicatorDescription psDescription = IndicatorDescription.get(NetConstant.PS,sex);
        IndicatorDescription pdDescription = IndicatorDescription.get(NetConstant.PD,sex);
        if ( pdDescription == null){
            limitValue[INDEX_PD] = LIMIET_RANGE_PD;
        }else {
            limitValue[INDEX_PD] = new float[]{pdDescription.getLowerLimit(), pdDescription.getUpperLimit()};
        }
        limitLineBp = new float[]{limitValue[INDEX_PD][0], limitValue[INDEX_PD][1],limitValue[INDEX_BP][0],limitValue[INDEX_BP][1]};
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleGestureDetector.onTouchEvent(event);
//        if (result) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
        if (!scaleGestureDetector.isInProgress()) {
            result = detector.onTouchEvent(event);
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return result;
    }


    /**
     * 显示index
     *
     * @param index 显示的项
     */
    public void showIndex(int index) {
//        if (index == INDEX_PS || index == INDEX_PD) {
//            show = (1 << INDEX_PS) | (1 << INDEX_PD);
//        } else {
//            show = 1 << index;
//        }
        show = 1 << index;
        invalidate();
    }

    public void setData(IndicatorStatisticDataModel data) {
        if (data == null) {
            invalidate();
            return;
        }
        this.data = data;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        realRect = new RectF(
                paddingLeft + axisYLabelWidth,
                paddingTop + titleHeight,
                getWidth() - paddingRight,
                getHeight() - paddingBottom - axisXLabelHeight);

        for (int i = 0; i < LINE_COUNT; i++) {
            unitYs[i] = (realRect.bottom - realRect.top) / INIT_SCOPE_Y[i];
        }
        setTimeMode(DAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBounds(canvas);
        drawAxisX(canvas);
        drawAxisY(canvas);
        drawYLine(canvas);
        drawLimitLine(canvas);
        drawSelectData(canvas);
        drawData(canvas);
    }

    private void drawLimitLine(Canvas canvas) {
        float xMin = realRect.left;
        float xMax = realRect.right;
        int index = getLineIndex();
        float[] limit;
        if (index == INDEX_BP) {
            limit = limitLineBp;
        } else {
            limit = limitValue[index];
        }
//        limit = limitValue[index];
        float min = MIN_Y[index];
        float max = MAX_Y[index];
        for (float aLimit : limit) {
            if (min < aLimit && max > aLimit) {
                float y = valueToCanvasPosition(index, aLimit) - offsetY;
                if (y < realRect.bottom && y > realRect.top) {
                    limitPath.reset();
                    limitPath.moveTo(xMin, y);
                    limitPath.lineTo(xMax, y);
                    canvas.drawPath(limitPath, limitPaint);
                }
            }
        }
    }

    private void drawYLine(Canvas canvas) {
        float xMin = realRect.left;
        float xMax = realRect.right;
        int index = getLineIndex();
        float min = MIN_Y[index];
        float max = MAX_Y[index];
        float delta = INIT_DELTA_Y[index];
        float start = min + delta;
        while (start <= max) {
            float y = valueToCanvasPosition(index, start) - offsetY;
            if (y < realRect.bottom && y > realRect.top) {
                limitPath.reset();
                limitPath.moveTo(xMin, y);
                limitPath.lineTo(xMax, y);
                canvas.drawPath(limitPath, yAxisPaint);
            }
            start += delta;
        }
    }


    /**
     * 设置时间类型
     *
     * @param timeMode DAY 日视图， MONTH 月视图
     */
    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
        this.unitX = (realRect.right - realRect.left) / (getMaxXValue());
    }

    private void drawAxisX(Canvas canvas) {
        canvas.save();
        canvas.clipRect(realRect.left - ViewUtils.dp2px(10),
                realRect.bottom,
                realRect.right,
                getHeight());
        canvas.translate(-offsetX, 0);
        float y = realRect.bottom + axisXLabelHeight;

        textPaint.setTextAlign(Paint.Align.CENTER);
        float start = 1;
        float end = getMaxXValue() - 1;
        while (start <= end) {
            float x = realRect.left + start * unitX * scaleX;
            canvas.drawText(String.valueOf((int) start), x, y, textPaint);
            start += getAxisXAccuracy();
        }
        canvas.translate(offsetX, 0);
        canvas.restore();

        textPaint.setTextAlign(Paint.Align.LEFT);
        if (timeMode == DAY) {
            canvas.drawText(dayUnitText, realRect.right + ViewUtils.dp2px(1), y, textPaint);
        } else if (timeMode == MONTH) {
            canvas.drawText(monthUnitText, realRect.right + ViewUtils.dp2px(1), y, textPaint);
        }
        textPaint.setTextAlign(Paint.Align.RIGHT);
    }

    /**
     * 获取x轴刻度精度
     *
     * @return 精度
     */
    private int getAxisXAccuracy() {
        return Math.max(1, (int) Math.floor(4.5f / scaleX));
    }

    /**
     * 绘制y轴
     *
     * @param canvas 画布
     */
    private void drawAxisY(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, realRect.top - ViewUtils.dp2px(10), realRect.left, realRect.bottom + ViewUtils.dp2px(10));
        canvas.translate(0, -offsetY);
        float x = realRect.left - axisYLabelTextPadding;

        textPaint.setTextAlign(Paint.Align.RIGHT);
        int index = getLineIndex();
        float start = MIN_Y[index];
        float end = MAX_Y[index];
        while (start <= end) {
            float y = valueToCanvasPosition(index, start) - (fontTop + fontBottom) / 2;
            canvas.drawText(String.valueOf((int) start), x, y, textPaint);
            start += getAxisYAccuracy();
        }
        canvas.translate(0, offsetY);
        canvas.restore();
//        textPaint.setTextAlign(Paint.Align.CENTER);
//        canvas.drawText(dataUnitText[index], x, realRect.top - ViewUtils.dp2px(5), textPaint);
//        textPaint.setTextAlign(Paint.Align.RIGHT);
    }

    /**
     * 获取y轴刻度精度
     *
     * @return 精度
     */
    private int getAxisYAccuracy() {
        float acc = INIT_ACCURACY[getLineIndex()];
        if (scaleY > 3) {
            return (int) (acc / 2);
        } else {
            return (int) acc;
        }
    }

    private void drawBounds(Canvas canvas) {
        paint.setColor(axisColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ViewUtils.dp2px(1));
        canvas.drawRect(realRect, paint);
    }

    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        canvas.save();
        canvas.clipRect(realRect);
        canvas.translate(-offsetX, -offsetY);
        paint.setStrokeWidth(dataLineWidth);
        int i = getLineIndex();
        drawDataLine(canvas, i);
        drawDataDot(canvas, i);
        if (i == INDEX_BP) {
            drawDataLine(canvas, INDEX_PD);
            drawDataDot(canvas, INDEX_PD);
        }
        canvas.translate(offsetX, offsetY);
        canvas.restore();
    }

    /**
     * 绘制序号为lineIndex的数据线
     *
     * @param canvas    画布
     * @param lineIndex 数据编号
     */
    private void drawDataLine(Canvas canvas, int lineIndex) {
        int color = lineIndex == INDEX_PD ? dataColor2 : dataColor1;
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paintDot.setColor(color);

        float lastX = 0;
        float lastY = 0;
        List<IndicatorStatisticDataModel.Data> list = getDataList(lineIndex);

        int size = list.size();
        for (int j = 0; j < size; j++) {
            IndicatorStatisticDataModel.Data indicatorDataModel = list.get(j);
            float x = timeToCanvasPosition(indicatorDataModel.time);
            float y = valueToCanvasPosition(lineIndex, indicatorDataModel.value);
            if (j != 0) {
                canvas.drawLine(lastX, lastY, x, y, paint);
            }
            lastX = x;
            lastY = y;
        }
    }
    /**
     * 绘制序号为lineIndex的数据点
     *
     * @param canvas    画布
     * @param lineIndex 数据编号
     */
    private void drawDataDot(Canvas canvas, int lineIndex) {
        int color = lineIndex == INDEX_PD ? dataColor2 : dataColor1;
        paintDot.setColor(color);

        List<IndicatorStatisticDataModel.Data> list = getDataList(lineIndex);
        int size = list.size();
        for (int j = 0; j < size; j++) {
            IndicatorStatisticDataModel.Data indicatorDataModel = list.get(j);
            float x = timeToCanvasPosition(indicatorDataModel.time);
            float y = valueToCanvasPosition(lineIndex, indicatorDataModel.value);
            if (j == selectIndex && lineIndex == selectLine) {
                paintDot.setColor(selectDataColor);
                paintDot.setAlpha(80);
                canvas.drawCircle(x, y, DOT_RADIUS * 2f, paintDot);
                paintDot.setAlpha(255);
                canvas.drawCircle(x, y, DOT_RADIUS, paintDot);
                paintDot.setColor(color);
            } else {
                paintDot.setColor(color);
                canvas.drawCircle(x, y, DOT_RADIUS, paintDot);
            }
        }
    }

    private int getDataStatus(int lineIndex, int dataIndex) {
        int status = 0;
        float[] limit;
//        if (lineIndex == INDEX_PD){
//            limit = LIMIET_RANGE_PD;
//        }else{
//            if (lineIndex < limitValue.length && lineIndex >= 0){
//                limit = limitValue[lineIndex];
//            }else{
//                return status;
//            }
//        }

        if (lineIndex < limitValue.length && lineIndex >= 0){
            limit = limitValue[lineIndex];
        }else{
            return status;
        }

        List<IndicatorStatisticDataModel.Data> list = getDataList(lineIndex);
        if (dataIndex >= 0 && dataIndex < list.size()) {
            float value = list.get(dataIndex).value;
            if (value < limit[0]) {
                status = -1;
            } else if (value > limit[1]) {
                status = 1;
            }
        }
        return status;
    }

    private List<IndicatorStatisticDataModel.Data> getDataList(int index) {
        List<IndicatorStatisticDataModel.Data> list;
        switch (index) {
            case INDEX_HR:
                list = data.hrList;
                break;
            case INDEX_BP:
                list = data.psList;
                break;
            case INDEX_PD:
                list = data.pdList;
                break;
            case INDEX_PM:
                list = data.pmList;
                break;
            case INDEX_TPR:
                list = data.tprList;
                break;
            case INDEX_CO:
                list = data.coList;
                break;
            case INDEX_V0:
                list = data.v0List;
                break;
            default:
                list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 绘制选中后的标题文字，线，边框
     *
     * @param canvas 画布
     */
    private void drawSelectData(Canvas canvas) {
        paint.setColor(selectDataColor);
        paint.setStrokeWidth(dataLineWidth);
        paint.setStyle(Paint.Style.FILL);
        Pair<String, Long> pair = getTitleAndTime();
        String titleText = pair.first;
        long time = pair.second;
        if (time == 0 || TextUtils.isEmpty(titleText)) {
            return;
        }
        titleTextPaint.getTextBounds(titleText, 0, titleText.length(), titleTextBounds);
        int titleWidth = titleTextBounds.width() + 2 * ViewUtils.dp2px(16);
        float titleLeft;
        float titleRight;
        float titleTop = paddingTop;
        float titleBottom = paddingTop + titleHeight;
        float x = timeToCanvasPosition(time) - offsetX;
        if (!(x > realRect.right || x < realRect.left)) {
            canvas.drawLine(x, realRect.top, x, realRect.bottom, paint);
        }
        if (x < 0 || x > realRect.right) {
            return;
        } else if (x - titleWidth / 2 < realRect.left) {
            titleLeft = realRect.left;
        } else if (x + titleWidth / 2 > realRect.right) {
            titleLeft = realRect.right - titleWidth;
        } else {
            titleLeft = x - titleWidth / 2;
        }

        titleRight = titleLeft + titleWidth;
        canvas.drawRoundRect(titleLeft, titleTop, titleRight, titleBottom, titleRadius, titleRadius, paint);
        canvas.drawText(titleText,
                (titleLeft + titleRight) / 2,
                (titleBottom + titleTop) / 2 - titleTextYOffset, titleTextPaint);
    }

    private float getOffsetXMin() {
        return 0;
    }

    private float getOffsetXMax() {
        return getMaxXValue() * unitX * (scaleX - 1);
    }

    private float getOffsetYMin() {
        int index = getLineIndex();
        return -(MAX_Y[index] - MIN_Y[index]) * unitYs[index] * (scaleY - 1);
    }

    private float getOffsetYMax() {
        return 0;
    }

    /**
     * 重计算偏移和缩放，使之在合理范围内
     */
    private void resizeOffset() {
        offsetX = Math.min(offsetX, getOffsetXMax());
        offsetX = Math.max(offsetX, getOffsetXMin());

        offsetY = Math.min(offsetY, getOffsetYMax());
        offsetY = Math.max(offsetY, getOffsetYMin());
    }

    private void resizeScale() {
        scaleX = Math.max(1, scaleX);
        scaleY = Math.max(1, scaleY);
    }

    private Pair<String, Long> getTitleAndTime() {
        long time;
        String timeString = "";
        String value;
        int lineIndex = getLineIndex();
        if (lineIndex == INDEX_BP) {
            List<IndicatorStatisticDataModel.Data> psList = getDataList(INDEX_BP);
            List<IndicatorStatisticDataModel.Data> pdList = getDataList(INDEX_PD);
            if (selectIndex < 0 || selectIndex >= psList.size() || selectIndex >= pdList.size()) {
                return new Pair<>("", 0L);
            }
            value = Math.round(psList.get(selectIndex).value) + "/" + Math.round(pdList.get(selectIndex).value)
                    + dataUnitText[lineIndex % dataUnitText.length];
            time = psList.get(selectIndex).time;
        } else {
            List<IndicatorStatisticDataModel.Data> list = getDataList(lineIndex);
            if (selectIndex < 0 || selectIndex >= list.size()) {
                return new Pair<>("", 0L);
            }
            value = Math.round(list.get(selectIndex).value) + dataUnitText[lineIndex % dataUnitText.length];
            time = list.get(selectIndex).time;
        }
        if (timeMode == DAY) {
            timeString = DateFormat.format(DAY_TIME_FORMAT, time).toString();
        } else if (timeMode == MONTH) {
            timeString = DateFormat.format(MONTH_TIME_FORMAT, time).toString();
        }
        return new Pair<>(timeString + " | " + value, time);
    }

    /**
     * 计算横坐标
     *
     * @param time 时间
     * @return 横坐标
     */
    private float timeToCanvasPosition(long time) {
        float x = 0;
        calendar.setTimeInMillis(time);
        if (timeMode == DAY) {
            x = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60f;
        } else if (timeMode == MONTH) {
            x = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return realRect.left + x * unitX * scaleX;
    }

    /**
     * 值在画布的坐标
     *
     * @param index 值的类型
     * @param value 值
     * @return Y坐标
     */
    private float valueToCanvasPosition(int index, float value) {
        if (index == INDEX_PD){
            index = INDEX_BP;
        }
        return realRect.bottom - (value - MIN_Y[index]) * unitYs[index] * scaleY;
    }


    /**
     * 是否显示该类型数据
     *
     * @param index 数据index
     * @return true此数据显示
     */
    private boolean isTypeMatch(int index) {
        return (show & (1 << index)) == (1 << index);
    }

    /**
     * 查找当前显示的类别
     *
     * @return 类别
     */
    private int getLineIndex() {
        for (int i = 0; i < LINE_COUNT; i++) {
            if (isTypeMatch(i)) {
                return i;
            }
        }
        return 0;
    }

    private int getMaxXValue() {
        if (timeMode == DAY) {
            return 24;
        } else if (timeMode == MONTH) {
            if (data.hrList.isEmpty()) {
                return 31;
            } else {
                long time = data.hrList.get(0).time;
                calendar.setTimeInMillis(time);
                return Utils.getMonthDay(calendar);
            }
        }
        return 0;
    }

    /**
     * 缩放手势监听器
     */
    private ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        private float initScaleX;
        private float initScaleY;

        private float initSpanX;
        private float initSpanY;

        private float initOffsetX;
        private float initOffsetY;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleX = initScaleX * (detector.getCurrentSpanX() / initSpanX);
//            scaleY = initScaleY * (detector.getCurrentSpanY() / initSpanY);
            scaleY = 1;
            resizeScale();

            offsetX = initOffsetX + detector.getFocusX() * (scaleX - initScaleX);
            offsetY = initOffsetY - detector.getFocusY() * (scaleY - initScaleY);
            resizeOffset();
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initScaleX = scaleX;
            initScaleY = scaleY;

            initSpanX = detector.getCurrentSpanX();
            initSpanY = detector.getCurrentSpanY();

            initOffsetX = offsetX;
            initOffsetY = offsetY;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    };

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.getCurrX();
            offsetY = scroller.getCurrY();
            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 滑动手势监听器
     */
    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
//            return !(scaleX == 1 && scaleY == 1 && getSelectPoint(e) == -1);
            scroller.forceFinished(true);
            return realRect.contains(e.getX(), e.getY());
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            offsetX += distanceX;
            offsetY += distanceY;
            resizeOffset();
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            scroller.fling((int) offsetX, (int) offsetY, (int) -velocityX, (int) -velocityY, (int) getOffsetXMin(),
                    (int) getOffsetXMax(), (int) getOffsetYMin(), (int) getOffsetYMax());
            ViewCompat.postInvalidateOnAnimation(IndicatorLineChartView.this);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            getSelectPoint(e);
            setSelectStatusColor();
            invalidate();
            return true;
        }
    };

    /**
     * 根据点击位置判断选中的点
     *
     * @param e 点击位置
     * 选中点，如果距离太远则返回-1
     */
    private void getSelectPoint(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        int lineIndex = getLineIndex();
        List<IndicatorStatisticDataModel.Data> line = getDataList(lineIndex);
        int size = line.size();
        selectIndex = -1;
        selectLine = -1;
        float radiusX = SELECT_REGION_RADIUS ;
        float radiusY = SELECT_REGION_RADIUS * 3;
        for (int i = 0; i < size; i++) {
            IndicatorStatisticDataModel.Data model = line.get(i);
            float px = timeToCanvasPosition(model.time) - offsetX;
            float py = valueToCanvasPosition(lineIndex, model.value) - offsetY;

            if (isInRegion(x,y,radiusX,radiusY,px,py)){
                this.selectIndex = i;
                this.selectLine = lineIndex;
                return;
            }
        }
        if (selectIndex == -1 && lineIndex == INDEX_BP){
            List<IndicatorStatisticDataModel.Data> linePd = getDataList(INDEX_PD);
            size = linePd.size();
            for (int i = 0; i < size; i++) {
                IndicatorStatisticDataModel.Data model = linePd.get(i);
                float px = timeToCanvasPosition(model.time) - offsetX;
                float py = valueToCanvasPosition(lineIndex, model.value) - offsetY;

                if (isInRegion(x,y,radiusX,radiusY,px,py)){
                    this.selectIndex = i;
                    this.selectLine = INDEX_PD;
                    return;
                }
            }
        }
    }

    /**
     * 是否在点的范围内
     *
     * @param pointX  点击位置x
     * @param pointY  点击位置y
     * @param radiusX 有效半径
     * @param radiusY 有效半径
     * @param targetX 目标位置x
     * @param targetY 目标位置y
     * @return true 点击位置在目标位置内
     */
    private boolean isInRegion(float pointX, float pointY, float radiusX, float radiusY,float targetX, float targetY) {
        return Math.abs(pointX - targetX) < radiusX && Math.abs(pointY - targetY) < radiusY;
    }

    private void setSelectStatusColor(){
        int status = getDataStatus(selectLine,selectIndex);
        if (status > 0){
            selectDataColor = warningRedColor;
        }else if (status < 0){
            selectDataColor = warningYellowColor;
        }else {
            selectDataColor = selectLine == INDEX_PD ? dataColor2 : dataColor1;
        }
    }
}
