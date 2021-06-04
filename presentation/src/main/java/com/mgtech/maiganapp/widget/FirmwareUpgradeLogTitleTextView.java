package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2018/3/20.
 * 固件升级文字背景修饰
 */

public class FirmwareUpgradeLogTitleTextView extends AppCompatTextView {
    private Paint paint;
    private Paint paintCircle;
    private int circleRadius;

    public FirmwareUpgradeLogTitleTextView(Context context) {
        super(context);
        init(context);
    }

    public FirmwareUpgradeLogTitleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FirmwareUpgradeLogTitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        paint = new Paint();
        paintCircle = new Paint();
        paint.setColor(Color.parseColor("#d1ebf5"));
        paint.setAntiAlias(true);
        paintCircle.setColor(Color.parseColor("#1b9dce"));
        paintCircle.setAntiAlias(true);
        circleRadius = ViewUtils.dp2px(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = circleRadius;
        int right = getWidth() - circleRadius;
        int bottom = getHeight();
        int top = 0;

        canvas.drawRoundRect(left,top,right,bottom,bottom/2,bottom/2,paint);
        canvas.drawCircle(left,bottom/2,circleRadius,paintCircle);
        canvas.drawCircle(right,bottom/2,circleRadius,paintCircle);
        super.onDraw(canvas);
    }

}
