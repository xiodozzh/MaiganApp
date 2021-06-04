package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationReminderModel;
import com.mgtech.maiganapp.utils.ViewUtils;

public class ActivityMedicationReminderStateView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int RADIUS = ViewUtils.dp2px(4);
    private static final int LINE_WIDTH = ViewUtils.dp2px(1);
    public static final int IGNORE = MedicationReminderModel.IGNORE;
    public static final int TAKE = MedicationReminderModel.TAKEN;
    public static final int DEFAULT = MedicationReminderModel.DEFAULT;
    private int state;

    private int colorIgnore;
    private int colorDefault;

    public ActivityMedicationReminderStateView(Context context) {
        super(context);
        init(context);
    }

    public ActivityMedicationReminderStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivityMedicationReminderStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        colorDefault = ContextCompat.getColor(context, R.color.colorPrimary);
        colorIgnore = ContextCompat.getColor(context, R.color.light_text);
        paint.setStrokeWidth(LINE_WIDTH);
    }

    public void setState(int state){
        this.state = state;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == IGNORE){
            paint.setColor(colorIgnore);
            paint.setStyle(Paint.Style.FILL);
        }else if (state == TAKE){
            paint.setColor(colorDefault);
            paint.setStyle(Paint.Style.FILL);
        }else if (state == DEFAULT){
            paint.setColor(colorDefault);
            paint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawCircle(getWidth()/2,getHeight()/2,RADIUS,paint);
    }
}
