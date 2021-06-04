package com.mgtech.maiganapp.widget;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Keep;
import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class BottomNavigatorSelectView extends View {
    private static final String TAG = BottomNavigatorSelectView.class.getSimpleName();
    private int buttonWidth;
    private int buttonPaddingBottom;
    private int bitmapLength;
    private int bitmapX;
    private int bitmapY;
    private int textSize;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float animationFraction;
    private ObjectAnimator animator;
    private GestureDetector detector;
    private boolean open;
    private PorterDuffXfermode xfermode;
    private SelectListener listener;
    private String leftText = "";
    private String rightText = "";
    private int textColor;
    private static final float DIVIDER_FRACTION = 0.5f;
    private Bitmap bitmapBp;
    private Bitmap bitmapEcg;
    private int primeColor;
    private int textLength = 0;
    private int textBaselineOffset;
    private Rect textBounds1 = new Rect();
    private Rect textBounds2 = new Rect();

    private Rect leftClickRect = new Rect();
    private Rect rightClickRect = new Rect();

    public interface SelectListener {
        /**
         * 点击左弹出按钮
         */
        void clickLeft();

        /**
         * 点击右弹出按钮
         */
        void clickRight();
    }

    public BottomNavigatorSelectView(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigatorSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigatorSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置左文字
     *
     * @param leftText 左文字
     */
    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    /**
     * 设置右文字
     *
     * @param rightText 右文字
     */
    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    /**
     * 设置字体颜色
     *
     * @param textColor 颜色
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    private void init(Context context) {
        buttonWidth = ViewUtils.dp2px(60,context);
        buttonPaddingBottom = ViewUtils.dp2px(4, context);
        bitmapLength = ViewUtils.dp2px(44, context);
        bitmapX = ViewUtils.dp2px(20,context);
        bitmapY = ViewUtils.dp2px(110, context);
        textSize = ViewUtils.sp2px(16, context);

        paint.setStyle(Paint.Style.FILL);
        detector = new GestureDetector(getContext(), new MeasureGestureListener());
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        bitmapBp = ViewUtils.decodeSampleBitmapFromResource(getResources(), R.drawable.measure_pw_btn, bitmapLength, bitmapLength);
        bitmapEcg = ViewUtils.decodeSampleBitmapFromResource(getResources(), R.drawable.measure_ecg_btn, bitmapLength, bitmapLength);
        primeColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        textColor = ContextCompat.getColor(getContext(), R.color.white);
        paint.setTextSize(textSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        textBaselineOffset = (int) (metrics.bottom + metrics.top) / 2;
    }

    @Keep
    public float getAnimationFraction() {
        return animationFraction;
    }

    @Keep
    public void setAnimationFraction(float animationFraction) {
        this.animationFraction = animationFraction;
        invalidate();
    }

    public void setListener(SelectListener listener) {
        this.listener = listener;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha(255);
        paint.setColor(primeColor);
//        canvas.drawCircle(getWidth() / 2, getHeight() - CENTER_HEIGHT, RADIUS * animationFraction, paint);
        int height = 0;
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(bitmapLength);

        if (textLength == 0) {
            paint.getTextBounds(leftText, 0, leftText.length(), textBounds1);
            paint.getTextBounds(rightText, 0, rightText.length(), textBounds2);
            int textLength1 = textBounds1.right - textBounds1.left;
            int textLength2 = textBounds2.right - textBounds2.left;
            if (textLength1 > textLength2) {
                textLength = textLength1 + bitmapLength / 2;
            } else {
                textLength = textLength2 + bitmapLength / 2;
            }
        }
        int leftStart = (getWidth() >> 1) - bitmapX - bitmapLength / 2 - 1;
        int leftEnd = (getWidth() >> 1) - bitmapX - bitmapLength / 2;
        int rightStart = (getWidth() >> 1) + bitmapX + bitmapLength / 2 + 1;
        int rightEnd = (getWidth() >> 1) + bitmapX + bitmapLength / 2;

        if (animationFraction < DIVIDER_FRACTION) {
            float fraction = animationFraction / DIVIDER_FRACTION;
            height = getHeight() - (int) (fraction * bitmapY);
            float offsetX = (1 - fraction) * (bitmapX + bitmapLength / 2);
            canvas.drawLine(leftStart + offsetX, height, leftEnd + offsetX, height, paint);
            canvas.drawLine(rightStart - offsetX, height, rightEnd - offsetX, height, paint);
            canvas.drawBitmap(bitmapEcg, leftEnd - bitmapLength / 2 + offsetX, height - bitmapLength / 2, paint);
            canvas.drawBitmap(bitmapBp, rightEnd - bitmapLength / 2 - offsetX, height - bitmapLength / 2, paint);

        } else {
            height = getHeight() - bitmapY;
            float widthFraction = (animationFraction - DIVIDER_FRACTION) / (1 - DIVIDER_FRACTION);
            canvas.drawLine(leftStart - textLength * widthFraction, height, leftEnd, height, paint);
            canvas.drawLine(rightStart + textLength * widthFraction, height, rightEnd, height, paint);
            canvas.drawBitmap(bitmapEcg, (getWidth() >> 1) - bitmapX - bitmapLength, height - bitmapLength / 2, paint);
            canvas.drawBitmap(bitmapBp, (getWidth() >> 1) + bitmapX, height - bitmapLength / 2, paint);


            leftClickRect.left = (int) (leftStart - textLength * widthFraction) - bitmapLength / 2;
            leftClickRect.right = leftEnd + bitmapLength / 2;
            leftClickRect.top = height - bitmapLength / 2;
            leftClickRect.bottom = height + bitmapLength / 2;

            rightClickRect.left = rightEnd - bitmapLength / 2;
            rightClickRect.right = (int) (rightStart + textLength * widthFraction + bitmapLength / 2);
            rightClickRect.top = height - bitmapLength / 2;
            rightClickRect.bottom = height + bitmapLength / 2;
        }

        if (animationFraction >= 1) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
//            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(leftText, leftEnd - bitmapLength / 2 - (textBounds1.right - textBounds1.left), height - textBaselineOffset, paint);
            canvas.drawText(rightText, rightEnd + bitmapLength / 2, height - textBaselineOffset, paint);
        }
    }

    public void select() {
        open = !open;
        if (open) {
            getAnimator().start();
        } else {
            getAnimator().reverse();
        }
    }

    public void open() {
        open = true;
        getAnimator().start();
    }

    public void close() {
        if (open) {
            open = false;
            getAnimator().reverse();
        }
    }

    private ObjectAnimator getAnimator() {
        if (animator == null) {
            Keyframe keyframe1 = Keyframe.ofFloat(0, 0);
            Keyframe keyframe2 = Keyframe.ofFloat(0.48f, DIVIDER_FRACTION);
            Keyframe keyframe3 = Keyframe.ofFloat(0.52f, DIVIDER_FRACTION);
            Keyframe keyframe4 = Keyframe.ofFloat(1, 1);
//            animator = ObjectAnimator.ofFloat(this, "animationFraction", 0, 1);
            PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("animationFraction", keyframe1, keyframe2, keyframe3, keyframe4);
            animator = ObjectAnimator.ofPropertyValuesHolder(this, holder);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(500);
        }
        return animator;
    }

    private class MeasureGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            if (open) {
                close();
                if (isInLeftRegion(e)) {
                    if (listener != null) {
                        listener.clickLeft();
                    }
                    return true;
                } else if (isInRightRegion(e)) {
                    if (listener != null) {
                        listener.clickRight();
                    }
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
//            if (isInLeftRegion(e)) {
//                if (listener != null) {
//                    listener.clickLeft();
//                }
//            } else if (isInRightRegion(e)) {
//                if (listener != null) {
//                    listener.clickRight();
//                }
//            }
//            close();
            return true;
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
    }

    private boolean isInBtnRegion(MotionEvent e) {
        return e.getX() > (getWidth() - buttonWidth) / 2 && e.getX() < (getWidth() + buttonWidth) / 2
                && e.getY() > (getHeight() - buttonWidth - buttonPaddingBottom)
                && e.getY() < (getHeight() - buttonPaddingBottom);
    }

    private boolean isInLeftRegion(MotionEvent e) {
        return !isInBtnRegion(e) && isInRect(leftClickRect, e);
    }

    private boolean isInRightRegion(MotionEvent e) {
        return !isInBtnRegion(e) && isInRect(rightClickRect, e);
    }

    private boolean isInRect(Rect rect, MotionEvent e) {
        return e.getX() > rect.left && e.getX() < rect.right && e.getY() > rect.top && e.getY() < rect.bottom;
    }
}
