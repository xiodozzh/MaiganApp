package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.ColorRes;
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

public class DynamicGraphView extends View {
    private static final String TAG = "DynamicGraphView";
    private int height;
    private int width;
    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 600;
    private int scope = 4095;
    private List<Integer> data;
    private LinkedList<Integer> cacheData;
    private Path path;
//    private Paint paint;
    private Paint linePaint;
    private boolean drawing;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

//    public DynamicGraphView(Context context) {
//        super(context);
//        init();
//    }

    public DynamicGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DynamicGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public DynamicGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.DynamicGraphView);
        int lineColor = a.getResourceId(R.styleable.DynamicGraphView_line_color,R.color.colorPrimary);
        int lineWidth = a.getDimensionPixelOffset(R.styleable.DynamicGraphView_line_width,1);
        a.recycle();
        data = new ArrayList<>();
        cacheData = new LinkedList<>();
        path = new Path();
        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(ContextCompat.getColor(getContext(), lineColor));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        drawing = true;
    }

    public void appendWithoutDraw(int point){
        cacheData.add(point);
        if (!drawing){
            drawing = true;
            invalidate();
        }
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    /**
     * 设置一屏内点的数量
     * @param pointNumber 点数
     */
    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
        this.deltaWidth = width / pointNumber;
    }

    /**
     * 设置屏幕点的最大值
     * @param scope 最大值
     */
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
        this.deltaWidth = (width -paddingLeft -paddingRight)/ (float) pointNumber;
        this.deltaHeight = (height -paddingTop -paddingBottom)/ (float) scope;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        while (!cacheData.isEmpty()){
            while (data.size() >= pointNumber){
                data.remove(0);
            }
            int i = cacheData.poll();
            data.add(i);
        }
        if (data.isEmpty()){
            if (drawing){
                postInvalidateDelayed(20);
            }
            return;
        }
        int size = data.size();
        int offset = pointNumber - size;
        path.reset();
        for (int i = 0; i < size; i++) {
            int p = data.get(i);
            if (i == 0){
                path.moveTo(Math.min((offset + i)*deltaWidth+paddingLeft,width-paddingRight),Math.max(height - p *deltaHeight-paddingBottom,paddingTop));
                continue;
            }
            path.lineTo(Math.min((offset + i)*deltaWidth+paddingLeft,width-paddingRight),Math.max(height - p *deltaHeight-paddingBottom,paddingTop));
        }
        canvas.drawPath(path,linePaint);
        if (drawing) {
            postInvalidateDelayed(20);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        this.drawing = false;
        super.onDetachedFromWindow();
    }

    public void reset() {
        cacheData.clear();
        data.clear();
        invalidate();
    }
}
