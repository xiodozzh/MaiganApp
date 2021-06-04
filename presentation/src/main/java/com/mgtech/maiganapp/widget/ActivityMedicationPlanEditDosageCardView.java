package com.mgtech.maiganapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class ActivityMedicationPlanEditDosageCardView extends View implements GestureDetector.OnGestureListener {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SELECTED = 1;
    public static final int TYPE_NONE = 2;

    private int dotRadius = ViewUtils.dp2px(9f);
    private int dotImgLength = ViewUtils.dp2px(4f);
    private int state = TYPE_NORMAL;
    private int cornerRadius = ViewUtils.dp2px(8);
    private int lineWidth = ViewUtils.dp2px(1);
    private int dotOffset = ViewUtils.dp2px(5);
    private int normalColor;
    private int noneColor;
    private int dotColor;
    private Bitmap bitmap;
    private int textSize = ViewUtils.sp2px(12);
    private String time = "08:20";
    private String dosage = "1片";
    private int textHeight;
    private int textOffset;
    private int textPadding;
    private GestureDetector detector;
    private ClickListener clickListener;

    public interface ClickListener{
        /**
         * 点击删除
         */
        void clickDelete(ActivityMedicationPlanEditDosageCardView view);

        /**
         * 点击选择
         */
        void clickSelect(ActivityMedicationPlanEditDosageCardView view);

        /**
         * 点击添加
         */
        void clickAdd(ActivityMedicationPlanEditDosageCardView view);
    }

    public ActivityMedicationPlanEditDosageCardView(Context context) {
        super(context);
        init(context);
    }

    public ActivityMedicationPlanEditDosageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ActivityMedicationPlanEditDosageCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void init(Context context) {
        normalColor = ContextCompat.getColor(context, R.color.primaryBlue);
        noneColor = ContextCompat.getColor(context, R.color.grey_text);
        dotColor = ContextCompat.getColor(context, R.color.warningRed);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(false);
        bitmap = ViewUtils.decodeSampleBitmapFromResource(context.getResources(),
                R.drawable.activity_medication_plan_edit_dosage_add, getWidth() - dotOffset, getHeight() - dotOffset);

        Paint.FontMetrics metrics = paint.getFontMetrics();
        textHeight = (int) (metrics.bottom - metrics.top);
        textOffset = (int) (metrics.ascent / 2);
        textPadding = ViewUtils.dp2px(3);
        detector = new GestureDetector(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int length = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == TYPE_NONE) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(noneColor);
            canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, (getHeight() - bitmap.getHeight()) / 2, paint);
            canvas.drawRoundRect(dotOffset, dotOffset, getHeight() - dotOffset, getWidth() - dotOffset, cornerRadius, cornerRadius, paint);
        } else if (state == TYPE_NORMAL) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(normalColor);
            paint.setStrokeWidth(lineWidth);
            canvas.drawRoundRect(dotOffset, dotOffset, getHeight() - dotOffset, getWidth() - dotOffset, cornerRadius, cornerRadius, paint);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(time, getWidth() / 2, getHeight() / 2 - textPadding, paint);
            canvas.drawText(dosage, getWidth() / 2, getHeight() / 2 + textPadding + textHeight + textOffset, paint);
            paint.setColor(dotColor);
            canvas.drawCircle(getWidth() - dotRadius, dotRadius, dotRadius, paint);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(lineWidth * 2);
            canvas.drawLine(getWidth() - dotRadius - dotImgLength, dotRadius, getWidth() - dotRadius + dotImgLength, dotRadius, paint);
        } else {
            paint.setColor(normalColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(dotOffset, dotOffset, getHeight() - dotOffset, getWidth() - dotOffset, cornerRadius, cornerRadius, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(time, getWidth() / 2, getHeight() / 2 - textPadding, paint);
            canvas.drawText(dosage, getWidth() / 2, getHeight() / 2 + textPadding + textHeight + textOffset, paint);
        }
    }

    public void setState(int state) {
        this.state = state;
        invalidate();
    }

    public void setTime(String time) {
        this.time = time;
        invalidate();
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
        invalidate();
    }

    public int getState() {
        return state;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return clickListener != null;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (clickListener != null) {
            if (state == TYPE_NORMAL) {
                if (isClickCircle(e)) {
                    clickListener.clickDelete(this);
                }else{
                    clickListener.clickSelect(this);
                }
            }else{
                clickListener.clickAdd(this);
            }
        }
            return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private boolean isClickCircle(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        return x > getWidth() - dotRadius * 2  && y < dotRadius * 2;
    }
}
