package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class PulseWaveGraphView extends View {
    private int backgroundColor;
    private int lineColor;

    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 600;
    private int scope = 4095;
    private List<Integer> data = new ArrayList<>();
    private LinkedList<Integer> cacheData = new LinkedList<>();

    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader shader;

    public PulseWaveGraphView(Context context) {
        this(context, null);
    }

    public PulseWaveGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulseWaveGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.PulseWaveGraphView);
        lineColor = typedArray.getResourceId(R.styleable.PulseWaveGraphView_pulse_line_color,R.color.colorPrimary);
        int lineWidth = typedArray.getDimensionPixelOffset(R.styleable.PulseWaveGraphView_pulse_line_width,1);
        backgroundColor = typedArray.getResourceId(R.styleable.PulseWaveGraphView_pulse_bg,R.color.background_grey);
        scope = typedArray.getInt(R.styleable.PulseWaveGraphView_pulse_scope,4095);
        pointNumber = typedArray.getInt(R.styleable.PulseWaveGraphView_pulse_point_number,600);
        typedArray.recycle();

        paint.setColor(ContextCompat.getColor(getContext(), lineColor));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(lineWidth);


        setFocusable(true);
        setKeepScreenOn(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.deltaWidth = w / (float) pointNumber;
        this.deltaHeight = h / (float) scope;

        shader = new LinearGradient(0,0,0,h,new int[]{ContextCompat.getColor(getContext(), lineColor)
                ,Color.parseColor("#00f7f7f7"),Color.parseColor("#00f7f7f7")},new float[]{0,0.7f,1}
                ,Shader.TileMode.REPEAT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ContextCompat.getColor(getContext(), backgroundColor));
        int paddingLeft = getPaddingStart();
        int paddingRight = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth();
        int height = getHeight();

        // 将缓存数据添加到绘制集合
        synchronized (this) {
            data.addAll(cacheData);
            cacheData.clear();
        }
        // 如果超出绘制范围，则移除之前的点
        while (data.size() >= pointNumber) {
            data.remove(0);
        }
        int size = data.size();
        int offset = pointNumber - size;
        path.reset();
        float startX = 0;
        for (int i = 0; i < size; i++) {
            int p = data.get(i);
            if (i == 0) {
                startX = Math.min((offset + i) * deltaWidth + paddingLeft, width - paddingRight);
                path.moveTo(Math.min((offset + i) * deltaWidth + paddingLeft, width - paddingRight),
                        Math.max(height - p * deltaHeight - paddingBottom, paddingTop));
                continue;
            }
            path.lineTo(Math.min((offset + i) * deltaWidth + paddingLeft, width - paddingRight),
                    Math.max(height - p * deltaHeight - paddingBottom, paddingTop));
        }
        paint.setShader(null);
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        if (size != 0) {
            path.lineTo(width, height + 10);
            path.lineTo(startX, height + 10);
            path.close();
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(shader);
            canvas.drawPath(path, paint);
        }
    }


    public synchronized void appendWithoutDraw(int point) {
        cacheData.addLast(point);
    }

    public synchronized void appendWithoutDraw(short[] points) {
        for (short s :points) {
            cacheData.add((int) s);
        }
        invalidate();
    }

    public synchronized void reset() {
        cacheData.clear();
        synchronized (this){
            data.clear();
        }
        invalidate();
    }
}
