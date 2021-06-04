package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhaixiang on 2017/7/17.
 * 动态绘图
 */

public class PwDrawingGraphView extends View {
    private static final String TAG = "GraphSurfaceView";
    private int height;
    private int width;
    private float deltaHeight;
    private static final float GAP_TIME = 0.3f;
    private static final float TOTAL_TIME = 3f;
    private int scope = 3750;
    private List<Pair<Float, Integer>> oldData;
    private List<Pair<Float, Integer>> newData;
    private Paint linePaint;
    private Paint shaderPaint;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private float position;
    private int lineWidth = ViewUtils.dp2px(2);
    private Path pathNew;
    private Path pathOld;

    private Path pathShadeNew;
    private Path pathShadeOld;
    private final Object monitor = new Object();
    private Shader shader;

    public PwDrawingGraphView(Context context) {
        super(context);
        init(context, null);
    }

    public PwDrawingGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PwDrawingGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PwDrawingGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        oldData = new ArrayList<>();
        newData = new ArrayList<>();
        pathNew = new Path();
        pathOld = new Path();
        linePaint = new Paint();
        shaderPaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(color);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        shaderPaint.setStyle(Paint.Style.FILL);
        shaderPaint.setAntiAlias(true);
    }

    public void appendWithoutDraw(short[] points, float sampleRate) {
        for (short point : points) {
            consume((1 / sampleRate), point);
        }
        invalidate();
    }

//    public void appendPoints(int point, float sampleRate) {
//        consume((1/sampleRate),point);
//    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        linePaint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shader = new LinearGradient(0,0,0,h,new int[]{ContextCompat.getColor(getContext(), R.color.colorPrimary)
                ,Color.parseColor("#00f7f7f7"),Color.parseColor("#00f7f7f7")},new float[]{0,0.7f,1}
                ,Shader.TileMode.REPEAT);
        shaderPaint.setShader(shader);
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
        this.deltaHeight = (height - paddingTop - paddingBottom) / (float) scope;
    }

    private float getYPosition(int value) {
        return Math.max(height - value * deltaHeight - paddingBottom, paddingTop);
    }

    private float getXPosition(float time) {
        float deltaWidth = (getWidth() - paddingLeft - paddingRight) / TOTAL_TIME;
        return Math.min(time * deltaWidth + paddingLeft, width - paddingRight);
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        this.drawing = false;
//        service.shutdown();
//        super.onDetachedFromWindow();
//    }


    public void reset() {
        synchronized (monitor) {
            newData.clear();
            oldData.clear();
            pathOld.reset();
            pathNew.reset();
            position = 0;
        }
    }

//    public void setDrawing(boolean drawing) {
//        if (!this.drawing && drawing) {
//            startDraw();
//        }
//        this.drawing = drawing;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        drawOldPath(canvas);
        drawNewPath(canvas);
//        drawPath(canvas);
    }

    private void drawPath(Canvas canvas) {
        int color = Color.WHITE;
        canvas.drawColor(color);
//        canvas.drawPath(pathOld, linePaint);
//        canvas.drawPath(pathNew, linePaint);
    }

//    private Runnable dealCacheDataRunnable = new Runnable() {
//        @Override
//        public void run() {
//            while (drawing) {
//                while (!cacheData.isEmpty()) {
//                    try {
//                        synchronized (monitor) {
//                            if (!cacheData.isEmpty()) {
//                                consume();
//                            }
//                            while (cacheData.size() > 16) {
//                                consume();
//                            }
//                        }
//                        Thread.sleep((long) (1000 / sampleRate));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    };

    private void consume(float x, int y) {
        int size = newData.size();
        if (size == 0) {
            newData.add(new Pair<>(x, y));
            return;
        }
        Pair<Float, Integer> last = newData.get(size - 1);
        float newX = last.first + x;
        if (newX >= TOTAL_TIME) {
            newX -= TOTAL_TIME;
            List<Pair<Float, Integer>> temp;
            temp = oldData;
            oldData = newData;
            newData = temp;
            newData.clear();
        }
        position = newX;
        newData.add(new Pair<>(newX, y));
    }

    private void drawNewPath(Canvas canvas) {
        int size = newData.size();
        float startX = 0;
        float endX = 0;
        pathNew.reset();
        for (int i = 0; i < size; i++) {
            Pair<Float, Integer> pair = newData.get(i);
            float x = getXPosition(pair.first);
            float y = getYPosition(pair.second);
            if (i != 0) {
                pathNew.lineTo(x, y);
            } else {
                pathNew.moveTo(x, y);
            }
            if (i == 0){
                startX = x;
            }else if (i == size -1){
                endX = x;
            }
        }
        canvas.drawPath(pathNew, linePaint);
        pathNew.lineTo(endX,getHeight());
        pathNew.lineTo(startX,getHeight());
        pathNew.close();
        canvas.drawPath(pathNew, shaderPaint);
    }

    private void drawOldPath(Canvas canvas) {
        int size = oldData.size();
        int start = 0;
        float startX = 0;
        float startY = 0;
        float endX = 0;
        pathOld.reset();
        for (int i = 0; i < size; i++) {
            Pair<Float, Integer> pair = oldData.get(i);
            float time = pair.first;
            if (time < position + GAP_TIME) {
                start = i + 1;
                continue;
            }
            float x = getXPosition(time);
            float y = getYPosition(pair.second);
            if (i != start) {
                pathOld.lineTo(x, y);
                endX = x;

            } else {
                pathOld.moveTo(x, y);
                startX = x;
                startY = y;
            }
        }
        canvas.drawPath(pathOld, linePaint);
        if (endX != 0 && startX != 0) {

            pathOld.lineTo(endX, getHeight());
            pathOld.lineTo(startX, getHeight());
            pathOld.lineTo(startX, startY);
            canvas.drawPath(pathOld, shaderPaint);
        }
    }

}
