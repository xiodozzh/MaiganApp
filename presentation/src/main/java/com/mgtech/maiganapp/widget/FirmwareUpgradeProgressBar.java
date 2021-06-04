package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2018/3/20.
 *
 */

public class FirmwareUpgradeProgressBar extends View{
    private Paint bgPaint;
    private Paint strokePaint;
    private Paint barPaint;
    private Paint textPaint;
    private float progress = 0f;
    private int textCenter;

    public FirmwareUpgradeProgressBar(Context context) {
        super(context);
        init(context);
    }

    public FirmwareUpgradeProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FirmwareUpgradeProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#bfedff"));
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint();
        strokePaint.setColor(Color.parseColor("#a7dff4"));
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(ViewUtils.dp2px(1));

        barPaint = new Paint();
        barPaint.setColor(Color.parseColor("#2bacdc"));
        barPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(ViewUtils.sp2px(displayMetrics,16));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setColor(Color.WHITE);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textCenter = (int) Math.ceil(fm.bottom +fm.top)/2;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawBar(canvas);
        super.onDraw(canvas);
    }

    private void drawBar(Canvas canvas) {
        int bottom = getHeight();
        int right = Math.round((getWidth()-bottom) * progress)+bottom;
        canvas.drawRoundRect(0,0,right,bottom,bottom/2,bottom/2,barPaint);
        if (progress > 0.01f) {
            String text = String.valueOf(Math.round(progress * 100)) + "%";
            canvas.drawText(text, 0, text.length(), right - 15, bottom / 2 - textCenter, textPaint);
        }
    }

    private void drawBg(Canvas canvas) {
        canvas.drawRoundRect(0,0,getWidth(),getHeight(),getWidth()/2,getWidth()/2,bgPaint);
        canvas.drawRoundRect(0,0,getWidth(),getHeight(),getWidth()/2,getWidth()/2,strokePaint);
    }
}
