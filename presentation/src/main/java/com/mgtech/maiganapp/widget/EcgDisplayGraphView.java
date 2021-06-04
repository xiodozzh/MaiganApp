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
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 *
 * @author zhaixiang
 * ECG静态绘图
 */

public class EcgDisplayGraphView extends View {
    private static final String TAG = "EcgGraphView";
    private int height;
    private int width;
    private float deltaWidth;
    private float deltaHeight;
    private int pointNumber = 400 * 4;
    private static final int GAP_NUMBER = 200;
    private int scope = 4095;
    private float[] data;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int position;
    private int lineWidth = 2;
    private Paint bgPaint;
    private int bgColor;
    private int bgGridColor;
    private float density;
    private int maxPos;
    private Paint paintData;
    private int reboundWidth;
    private Path path = new Path();

    public EcgDisplayGraphView(Context context) {
        super(context);
        init(context);
    }

    public EcgDisplayGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EcgDisplayGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EcgDisplayGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.data = new float[]{};
        this.density = context.getResources().getDisplayMetrics().density;
        this.bgPaint = new Paint();
        this.bgPaint.setStrokeWidth(ViewUtils.dp2px(density, 1));
        this.bgColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        this.bgGridColor = Color.parseColor("#66ffffff");

        this.paintData = new Paint();
        this.paintData.setStrokeWidth(ViewUtils.dp2px(density, 1));
        this.paintData.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        this.paintData.setAntiAlias(true);
        this.paintData.setStyle(Paint.Style.STROKE);
    }

    public void setData(float[] data) {
        this.data = data;
        this.maxPos = (int) (data.length * deltaWidth - width);
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
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
        this.reboundWidth = width/5;
        this.maxPos = (int) (data.length * deltaWidth - width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawBg(canvas);
        drawData(canvas);
    }


    /**
     * 绘制数据
     *
     * @param canvas 画布
     */
    private void drawData(Canvas canvas) {
        if (data.length <= 1) {
            return;
        }
        path.reset();
        path.moveTo(getDataPositionX(-1),getDataPositionY(data[0]));
        for (int i = 0; i < data.length; i++) {
            float x = getDataPositionX(i);
            float y = getDataPositionY(data[i]);
            if (x < 0 ){
                continue;
            }else if (x > width){
                break;
            }
            path.lineTo(x,y);
        }
        canvas.drawPath(path,paintData);
    }

    private float getDataPositionX(int i){
        return deltaWidth * i - position;
    }

    private float getDataPositionY(float value){
        return height - value * deltaHeight - paddingTop;
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.drawColor(bgColor);
        int dx = ViewUtils.dp2px(density, 5);
        bgPaint.setColor(bgGridColor);
        for (int i = 0; i < width / dx; i++) {
            if (i % 5 == 0) {
                bgPaint.setStrokeWidth(1.2f);
            } else {
                bgPaint.setStrokeWidth(1f);
            }
            canvas.drawLine(dx * i, 0, dx * i, height, bgPaint);
        }
        for (int i = 0; i < height / dx; i++) {
            if (i % 5 == 0) {
                bgPaint.setStrokeWidth(1.2f);
            } else {
                bgPaint.setStrokeWidth(1f);
            }
            canvas.drawLine(0, dx * i, width, dx * i, bgPaint);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
