package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

public class EcgDrawingGraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "GraphSurfaceView";
    private int height;
    private int width;
    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 400 * 4;
    private static final int GAP_NUMBER = 200;
    private int scope = 4095;
    private List<Integer> oldData;
    private List<Integer> newData;
    private BlockingQueue<Integer> cacheData;
    private Paint linePaint;
    private boolean drawing;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int position;
    private int lineWidth = ViewUtils.dp2px(1);
    private float sampleRate = 489;
    private static final int FREQUENCE = 20;
    private int drawDataNumber;
    private SurfaceHolder holder;
    private Path pathNew;
    private Path pathOld;
    private final Object monitor = new Object();
    private ExecutorService service;
    private float backgroundAlpha = 1;

    public EcgDrawingGraphSurfaceView(Context context) {
        super(context);
        init(context, null);
    }

    public EcgDrawingGraphSurfaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EcgDrawingGraphSurfaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EcgDrawingGraphSurfaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EcgRefreshGraphView);
//        int color = typedArray.getColor(R.styleable.EcgRefreshGraphView_lineColor, Color.parseColor("#28bdfe"));
//        typedArray.recycle();
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        oldData = new ArrayList<>();
        newData = new ArrayList<>();
        cacheData = new LinkedBlockingQueue<>();
        holder = getHolder();
        holder.addCallback(this);
        pathNew = new Path();
        pathOld = new Path();
        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(color);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        drawDataNumber = (int) Math.ceil(sampleRate / FREQUENCE);
        setZOrderOnTop(true);
        setZOrderMediaOverlay(true);
//        setFocusable(true);
//        setFocusableInTouchMode(true);
    }

    public void appendPoints(double[] points) {
        synchronized (monitor) {
            for (double i : points) {
                cacheData.add((int) i);
            }
//            monitor.notifyAll();
        }
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        linePaint.setStrokeWidth(lineWidth);
    }

    public void setBackgroundAlpha(float alpha){
        backgroundAlpha = alpha;
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

    private float getYPosition(int value) {
        return Math.max(height - value * deltaHeight - paddingBottom, paddingTop);
    }

    private float getXPosition(int index) {
        return Math.min(index * deltaWidth + paddingLeft, width - paddingRight);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow: ");
        this.drawing = false;
        holder.removeCallback(this);
        super.onDetachedFromWindow();
    }


    public void reset() {
        synchronized (monitor) {
            cacheData.clear();
            newData.clear();
            oldData.clear();
            pathOld.reset();
            pathNew.reset();
            position = 0;
            backgroundAlpha = 1;
//            monitor.notifyAll();
        }
    }

    public void setDrawing(boolean drawing) {
//        Log.i(TAG, "setDrawing: "+ drawing);
//        if (!this.drawing){
//            new H
//        }
//        this.drawing = drawing;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        drawing = true;
//        new Thread(this).start();
//        new Thread(dealCacheDataRunnable).start();
        service = Executors.newCachedThreadPool();
        service.execute(this);
        service.execute(dealCacheDataRunnable);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
        drawing = false;
        service.shutdown();
    }

    private Canvas canvas;

    @Override
    public void run() {
        while (drawing) {
            try {
                Thread.sleep(20);
                canvas = holder.lockCanvas();
                Log.i(TAG, "run: ");
                if (canvas != null) {
                    drawOldPath();
                    drawNewPath();
                    drawPath(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void drawPath(Canvas canvas) {
        int color = Color.WHITE;
//        if (backgroundAlpha != 1){
//            color = Color.parseColor("#999999");
//        }
        canvas.drawColor(color);
        canvas.drawPath(pathOld, linePaint);
        canvas.drawPath(pathNew, linePaint);
    }

    private Runnable dealCacheDataRunnable = new Runnable() {
        @Override
        public void run() {
            while (drawing) {
                while (!cacheData.isEmpty()) {
                    try {
                        synchronized (monitor) {
                            if (!cacheData.isEmpty()) {
                                consume();
                            }
                            while(cacheData.size() > 256){
                                consume();
                            }
                        }
                        Thread.sleep((long) (1000 / sampleRate));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                    try {
//                        monitor.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    };

    private void consume(){
        newData.add(cacheData.poll());
        position = newData.size();
        if (newData.size() >= pointNumber) {
            List<Integer> temp;
            temp = oldData;
            oldData = newData;
            newData = temp;
            newData.clear();
            position = 0;
        }
    }

    private void drawNewPath() {
        for (int i = 0; i < position; i++) {
            int p = newData.get(i);
            float x = getXPosition(i);
            float y = getYPosition(p);
            if (i != 0) {
                pathNew.lineTo(x, y);
            } else {
                pathNew.reset();
                pathNew.moveTo(x, y);
            }
        }
    }

    private void drawOldPath() {
        int size = Math.min(pointNumber, oldData.size());
        for (int i = position + GAP_NUMBER; i < size; i++) {
            int p = oldData.get(i);
            float x = getXPosition(i);
            float y = getYPosition(p);
            if (i != position + GAP_NUMBER) {
                pathOld.lineTo(x, y);
            } else {
                pathOld.reset();
                pathOld.moveTo(x, y);
            }
        }
    }

}
