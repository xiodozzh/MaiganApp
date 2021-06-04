package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

public class DynamicRefreshGraphView extends View {
    private static final String TAG = "DynamicGraphView";
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
    private Path oldPath;
    private Path path;
    private Paint pointPaint;
    private Paint linePaint;
    private boolean drawing;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int position;
    private int lineWidth = 2;

    public DynamicRefreshGraphView(Context context) {
        super(context);
        init();
    }

    public DynamicRefreshGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicRefreshGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DynamicRefreshGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        oldData = new ArrayList<>();
        newData = new ArrayList<>();
        cacheData = new LinkedList<>();
        path = new Path();
        oldPath = new Path();
        pointPaint = new Paint();
        pointPaint.setStrokeWidth(10);
        pointPaint.setColor(Color.parseColor("#ff00a99e"));
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        drawing = true;
    }

    public void appendPoint(int point) {
        cacheData.add(point);
        if (!drawing) {
            drawing = true;
            invalidate();
        }
    }

    public void appendPoints(int[] points) {
        for (int i:points) {
            cacheData.add(i);
        }
        if (!drawing) {
            drawing = true;
            invalidate();
        }
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
//        drawPositionPoint(canvas);
        if (drawing) {
            postInvalidateDelayed(20);
        }
    }


    private void dealWithData() {
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

    private void drawPositionPoint(Canvas canvas) {
        float x;
        float y;

        if (oldData.size() > position + 9) {
            int p = oldData.get(position + 9);
            x = Math.min((position + 9) * deltaWidth + paddingLeft, width - paddingRight);
            y = Math.max(height - p * deltaHeight - paddingBottom, paddingTop);
        } else {
            x = scope * deltaWidth / 2;
            y = scope * deltaHeight / 2;
        }
        canvas.drawPoint(x, y, pointPaint);
    }

    private void drawNewData(Canvas canvas) {
        path.reset();
        for (int i = 0; i < position; i++) {
            int p = newData.get(i);
            if (i == 0) {
                path.moveTo(Math.min(i * deltaWidth + paddingLeft, width - paddingRight), Math.max(height - p * deltaHeight - paddingBottom, paddingTop));
                continue;
            }
            path.lineTo(Math.min(i * deltaWidth + paddingLeft, width - paddingRight), Math.max(height - p * deltaHeight - paddingBottom, paddingTop));
        }
        canvas.drawPath(path, linePaint);
    }

    private void drawOldData(Canvas canvas) {
        oldPath.reset();
        int size = oldData.size();
        for (int i = position + GAP_NUMBER; i < pointNumber; i++) {
            int p = 0;
            if (i < size) {
                p = oldData.get(i);
            } else {
                p = scope / 2;
                continue;
            }
            if (i == position + GAP_NUMBER) {
                oldPath.moveTo(Math.min(i * deltaWidth + paddingLeft, width - paddingRight), Math.max(height - p * deltaHeight - paddingBottom, paddingTop));
                continue;
            }
            oldPath.lineTo(Math.min(i * deltaWidth + paddingLeft, width - paddingRight), Math.max(height - p * deltaHeight - paddingBottom, paddingTop));
        }
        canvas.drawPath(oldPath, linePaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        this.drawing = false;
        super.onDetachedFromWindow();
    }

    public void reset() {
        cacheData.clear();
        newData.clear();
        oldData.clear();
        position = 0;
        invalidate();
    }
}
