package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/7/17.
 * 动态绘图
 */

public class EcgRefreshGraphView extends View {
    private static final String TAG = "EcgRefreshGraphView";
    private int height;
    private int width;
    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 400 * 4;
    private static final int GAP_NUMBER = 200;
    private int scope = 4095;
    private List<Integer> oldData;
    private List<Integer> newData;
    private LinkedList<Integer> cacheData;
    private Paint linePaint;
    private boolean drawing;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int position;
    private int lineWidth = 2;

    public EcgRefreshGraphView(Context context) {
        super(context);
        init(context, null);
    }

    public EcgRefreshGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EcgRefreshGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EcgRefreshGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EcgRefreshGraphView);
        int color = typedArray.getColor(R.styleable.EcgRefreshGraphView_lineColor, Color.parseColor("#28bdfe"));
        typedArray.recycle();
        oldData = new ArrayList<>();
        newData = new ArrayList<>();
        cacheData = new LinkedList<>();

        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(color);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        drawing = true;
    }


    public synchronized void appendPoints(int[] points) {
        for (int i : points) {
            cacheData.add(i);
        }
//        if (!drawing) {
//            drawing = true;
            postInvalidate();
//        }
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        linePaint.setStrokeWidth(lineWidth);
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
        this.deltaWidth = width / pointNumber;
    }

    public void setPointScope(int scope) {
        this.scope = scope;
        this.deltaHeight = height / scope;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height = MeasureSpec.getSize(heightMeasureSpec) + 2;
        this.width = MeasureSpec.getSize(widthMeasureSpec);
        this.paddingLeft = getPaddingLeft();
        this.paddingTop = getPaddingTop();
        this.paddingRight = getPaddingRight();
        this.paddingBottom = getPaddingBottom();
        this.deltaWidth = (width - paddingLeft - paddingRight) / (float) pointNumber;
        this.deltaHeight = (height - paddingTop - paddingBottom) / (float) scope;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dealWithData();
        drawOldData(canvas);
        drawNewData(canvas);
//        if (drawing) {
//            postInvalidateDelayed(20);
//        }
    }


    private synchronized void dealWithData() {
        List<Integer> temp;
        while (!cacheData.isEmpty()) {
            newData.add(cacheData.poll());
            position = newData.size();
            if (newData.size() >= pointNumber) {
                temp = oldData;
                oldData = newData;
                newData = temp;
                newData.clear();
                position = 0;
            }
        }
    }

    private void drawNewData(Canvas canvas) {
        float lastX = paddingLeft;
        float lastY = scope/2;
        for (int i = 0; i < position; i++) {
            int p = newData.get(i);
            float x = getXPosition(i);
            float y = getYPosition(p);
            if (i != 0){
                canvas.drawLine(lastX, lastY, x, y, linePaint);
            }
            lastX = x;
            lastY = y;
        }
    }

    private void drawOldData(Canvas canvas) {
        int size = Math.min(pointNumber,oldData.size());
        float lastX = paddingLeft;
        float lastY = scope/2;
        for (int i = position + GAP_NUMBER; i < size; i++) {
            int p = oldData.get(i);
            float x = getXPosition(i);
            float y = getYPosition(p);
            if (i != position + GAP_NUMBER){
                canvas.drawLine(lastX, lastY, x, y, linePaint);
            }
            lastX = x;
            lastY = y;
        }
    }

    private float getYPosition(int value){
        return Math.max(height - value * deltaHeight - paddingBottom, paddingTop);
    }

    private float getXPosition(int index){
        return Math.min(index * deltaWidth + paddingLeft, width - paddingRight);
    }

    @Override
    protected void onDetachedFromWindow() {
        this.drawing = false;
        super.onDetachedFromWindow();
    }

    public synchronized void reset() {
        cacheData.clear();
        newData.clear();
        oldData.clear();
        position = 0;
        drawing = false;
        invalidate();
    }
}
