package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaixiang on 2017/7/25.
 * 网格
 */

public class NetView extends View {
    private int width;
    private int height;
    private Paint paint;
    private Path path;

    private int column = 10;
    private int row = 8;
    private float deltaWidth;
    private float deltaHeight;

    public NetView(Context context) {
        super(context);
        init();
    }

    public NetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.path = new Path();
        this.paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        deltaWidth = width / (float)(5 * column);
        deltaHeight = height / (float)(5 * row);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(Color.WHITE);
        this.paint.setStrokeWidth(4);
        this.paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(0,0,width,height,15,15,paint);

        for (int i = 0; i < column * 5; i++) {
            if (i % 5 == 4) {
                paint.setStrokeWidth(2);
                paint.setColor(Color.parseColor("#66FFFFFF"));
            }else{
                paint.setStrokeWidth(1);
                paint.setColor(Color.parseColor("#22FFFFFF"));
            }
            canvas.drawLine(deltaWidth * (i + 1), 0, deltaWidth * (i + 1), height, paint);
        }
        for (int i = 0; i < row * 5; i++) {
            if (i % 5 == 4) {
                paint.setStrokeWidth(2);
                paint.setColor(Color.parseColor("#66FFFFFF"));
            }else{
                paint.setStrokeWidth(1);
                paint.setColor(Color.parseColor("#22FFFFFF"));
            }
            canvas.drawLine(0, deltaHeight * (i + 1), width, deltaHeight * (i + 1), paint);
        }
    }
}
