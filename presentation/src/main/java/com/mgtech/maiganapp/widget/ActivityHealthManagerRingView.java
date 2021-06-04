package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class ActivityHealthManagerRingView extends View {
    private static final int WIDTH = ViewUtils.dp2px(12);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int totalNumber = 1;
    private int reachNumber = 0;
    private int unReachNumber = 0;
    private int unReachColor;
    private int reachColor;
    private int backgroundColor;

    public ActivityHealthManagerRingView(Context context) {
        super(context);
        init(context);
    }

    public ActivityHealthManagerRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivityHealthManagerRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setNumber(int reachNumber, int unReachNumber, int totalNumber) {
        this.reachNumber = reachNumber;
        this.unReachNumber = unReachNumber;
        this.totalNumber = totalNumber;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int side = Math.max(height,width);
        setMeasuredDimension(side,side);
    }

    private void init(Context context){
        unReachColor = ContextCompat.getColor(context, R.color.warningColor);
        reachColor = ContextCompat.getColor(context, R.color.colorPrimary);
        backgroundColor = Color.parseColor("#eeeeee");

        paint.setStrokeWidth(WIDTH);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (totalNumber == 0){
            return;
        }
        float unReachRation = (reachNumber + unReachNumber) /(float)totalNumber;
        float reachRation = reachNumber /(float)totalNumber;
        paint.setColor(backgroundColor);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2 - WIDTH/2,paint);

        paint.setColor(unReachColor);
        canvas.drawArc(WIDTH/2,WIDTH/2,getWidth()- WIDTH/2,getHeight()- WIDTH/2,
                -90,unReachRation*360,false,paint);
        paint.setColor(reachColor);
        canvas.drawArc(WIDTH/2,WIDTH/2,getWidth()- WIDTH/2,getHeight()- WIDTH/2,
                -90,reachRation*360,false,paint);
    }
}
