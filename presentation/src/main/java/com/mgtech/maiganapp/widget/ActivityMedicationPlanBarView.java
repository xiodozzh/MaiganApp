package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;

public class ActivityMedicationPlanBarView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float notTakeRatio;
    private float takeRatio;
    private float notRecordRatio;
    private int leftColor;
    private int takeColor;
    private int notTakeColor;
    private int notRecordColor;


    public ActivityMedicationPlanBarView(Context context) {
        super(context);
        init(context);
    }

    public ActivityMedicationPlanBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivityMedicationPlanBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        leftColor = ContextCompat.getColor(context, R.color.background_grey);
        takeColor = ContextCompat.getColor(context, R.color.primaryBlue);
        notTakeColor = ContextCompat.getColor(context, R.color.warningColor);
        notRecordColor = ContextCompat.getColor(context, R.color.light_text);

        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setRatio(float notTakeRatio, float takeRatio, float notRecordRatio) {
        this.notTakeRatio = notTakeRatio;
        this.notRecordRatio = notRecordRatio;
        this.takeRatio = takeRatio;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int lineWidth = getHeight();
        paint.setStrokeWidth(lineWidth);
        paint.setColor(leftColor);
        canvas.drawLine(lineWidth / 2, lineWidth / 2, getWidth() - lineWidth / 2, lineWidth / 2, paint);
        paint.setColor(notRecordColor);
        canvas.drawLine(lineWidth / 2, lineWidth / 2,
                (getWidth() - lineWidth) * (notTakeRatio + takeRatio + notRecordRatio) + lineWidth / 2, lineWidth / 2, paint);
        paint.setColor(notTakeColor);
        canvas.drawLine(lineWidth / 2, lineWidth / 2,
                (getWidth() - lineWidth) * (notTakeRatio + takeRatio) + lineWidth / 2, lineWidth / 2, paint);
        paint.setColor(takeColor);
        canvas.drawLine(lineWidth / 2, lineWidth / 2,
                (getWidth() - lineWidth) * takeRatio + lineWidth / 2, lineWidth / 2, paint);
    }
}
