package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;

/**
 * Created by zhaixiang on 2017/3/31.
 * 向下的三角形
 */

public class TriangleView extends View {
    private Paint paint;
    private Path path;
    private Context context;

    public TriangleView(Context context) {
        super(context);
        init(context);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void init(Context context) {
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        this.path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        path.moveTo(0,height /2);
        path.lineTo(width/2,height);
        path.lineTo(width,height/2);
        canvas.drawPath(path,paint);
        super.onDraw(canvas);
    }
}
