package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhaixiang
 */
public class PulseWaveGraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "PWSurfaceView";
    private int backgroundColor;
    private int lineColor;

    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 600;
    private int scope = 4095;
    private List<Integer> data = new ArrayList<>();
    private LinkedBlockingQueue<Integer> cacheData = new LinkedBlockingQueue<>();

    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader shader;
    private float sampleRate = 128;
    private boolean drawing;
    private final Object monitor = new Object();
    private ExecutorService service;
    private Canvas canvas;
    private SurfaceHolder holder;

    public PulseWaveGraphSurfaceView(Context context) {
        this(context, null);
    }

    public PulseWaveGraphSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulseWaveGraphSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        lineColor = R.color.colorPrimary;
        int lineWidth = ViewUtils.dp2px(1);
        backgroundColor = R.color.white;
        scope = 4095;
        pointNumber = 600;
        paint.setColor(ContextCompat.getColor(getContext(), lineColor));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(lineWidth);

        holder = getHolder();
        holder.addCallback(this);

        setFocusable(true);
        setKeepScreenOn(true);
    }

    public void appendWithoutDraw(short[] points) {
        synchronized (monitor) {
            for (short s : points) {
                cacheData.add((int) s);
            }
        }
    }

    public void reset() {
        cacheData.clear();
        data.clear();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawing = true;
        service = Executors.newCachedThreadPool();
        service.execute(drawRunnable);
        service.execute(cacheRunnable);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        this.deltaWidth = w / (float) pointNumber;
        this.deltaHeight = h / (float) scope;
        shader = new LinearGradient(0, 0, 0, h, new int[]{ContextCompat.getColor(getContext(), lineColor)
                , Color.parseColor("#00f7f7f7"), Color.parseColor("#00f7f7f7")}, new float[]{0, 0.7f, 1}
                , Shader.TileMode.REPEAT);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawing = false;
        service.shutdown();
    }



    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while (drawing) {
                try {
                    Thread.sleep(20);
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        drawPw(canvas);
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
    };

    private Runnable cacheRunnable = new Runnable() {
        @Override
        public void run() {
            while (drawing) {
                while (!cacheData.isEmpty()) {
                    Log.i(TAG, "run: ");
                    try {
                        synchronized (monitor) {
//                            if (!cacheData.isEmpty()) {
//                                consume();
//                            }
                            while (cacheData.size() > 16) {
                                consume();
                            }
                        }
                        Thread.sleep((long) Math.floor(1000 / sampleRate));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep((long) (1000 / sampleRate));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void consume() {
        data.add(cacheData.poll());
        while (data.size() >= pointNumber) {
            data.remove(0);
        }
    }

    private void drawPw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), backgroundColor));
        int paddingLeft = getPaddingStart();
        int paddingRight = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth();
        int height = getHeight();

        int size = data.size();
        int offset =pointNumber - size;
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
}
