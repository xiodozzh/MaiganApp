package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesse
 */
public class MeasurePwResultImageView extends View {
    private int backgroundColor;
    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 600;
    private int scope = 4095;
    private int linePadding = ViewUtils.dp2px(4);
    private List<List<Integer>> data = new ArrayList<>();
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader shader;
    private int lineColor;
    private static final int MIN_FRAGMENT_SIZE = 10;


    public MeasurePwResultImageView(Context context) {
        super(context);
        init();
    }

    public MeasurePwResultImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeasurePwResultImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundColor = ContextCompat.getColor(getContext(), R.color.white);
        lineColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(ViewUtils.dp2px(1.5f));
    }

    public void setData(List<Float> rawData) {
        if (rawData == null) {
            return;
        }
        data.clear();
        int start = 0;
        int size = rawData.size();
        while (start < size) {
            List<Integer> list = new ArrayList<>();
            for (int i = start; i < size; i++) {
                int d = Math.round(rawData.get(i));
                start++;
                if (d == 65535) {
                    break;
                } else {
                    list.add(d);
                }
            }
            if (list.size() > MIN_FRAGMENT_SIZE) {
                data.add(list);
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        deltaWidth = ViewUtils.getScreenWidth() / (float) pointNumber;
        int totalNumber = 0;
        int size = 0;
        for (List<Integer> list : data) {
            totalNumber += list.size();
            size++;
        }
        int totalWidth = Math.round(deltaWidth * totalNumber + linePadding * (size - 1));
        super.onMeasure(MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.deltaWidth = ViewUtils.getScreenWidth() / (float) pointNumber;
        this.deltaHeight = h / (float) scope;
        shader = new LinearGradient(0, 0, 0, h, new int[]{lineColor
                , Color.parseColor("#00f7f7f7"), Color.parseColor("#00f7f7f7")}, new float[]{0, 0.7f, 1}
                , Shader.TileMode.REPEAT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        int paddingLeft = getPaddingStart();
        int paddingRight = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth();
        int height = getHeight();

        // 如果超出绘制范围，则移除之前的点
        int offset = 0;
        for (int j =0; j< data.size(); j++) {
            List<Integer>list = data.get(j);
            int size = list.size();
            path.reset();
            float startX = 0;
            float endX = 0;
            for (int i = 0; i < size; i++) {
                int p = list.get(i);
                float x =  (offset + i) * deltaWidth + paddingLeft + j * linePadding;
                float y = Math.max(height - p * deltaHeight - paddingBottom, paddingTop);
                if (i == 0) {
                    startX = x;
                    path.moveTo(x, y);
                    continue;
                }
                endX = x;
                path.lineTo(x, y);
            }
            offset += size;
            paint.setShader(null);
            paint.setXfermode(null);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);

            path.lineTo(endX, height + 10);
            path.lineTo(startX, height + 10);
            path.close();
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(shader);
            canvas.drawPath(path, paint);
        }

    }
}
