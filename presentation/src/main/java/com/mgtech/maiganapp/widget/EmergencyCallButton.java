package com.mgtech.maiganapp.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

public class EmergencyCallButton extends View implements GestureDetector.OnGestureListener {
    private Paint paint;
    private static final int DEFAULT_SIZE = ViewUtils.dp2px(70);
    private static final int DEFAULT_IMG_SIZE = ViewUtils.dp2px(40);
    private static final int STATE_LEFT = 491;
    private static final int STATE_RIGHT = 776;
    private static final int STATE_NONE = 70;
    private int size = DEFAULT_SIZE;
//    private int imgSize = DEFAULT_IMG_SIZE;
    private float centerX;
    private float centerY;
//    private int imgPadding;
    private int state = STATE_RIGHT;
    private Bitmap bitmapCenter;
    private Bitmap bitmapLeft;
    private Bitmap bitmapRight;
    private GestureDetector detector;
    private ObjectAnimator animator;
    private Listener listener;
    public static final int LEFT= 0;
    public static final int RIGHT= 1;

    public EmergencyCallButton(Context context) {
        super(context);
        init(context);
    }

    public EmergencyCallButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmergencyCallButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public float getCenterX() {
        return centerX;
    }

    @Keep
    public void setCenterX(float centerX) {
        this.centerX = centerX;
        invalidate();
    }

    public void setOnClick(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        void onClick();
        void onLongClick();
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapCenter = ViewUtils.decodeSampleBitmapFromResource(context.getResources(), R.drawable.fragment_home_emergency_able_center,size,size);
        bitmapLeft = ViewUtils.decodeSampleBitmapFromResource(context.getResources(), R.drawable.fragment_home_emergency_able_left,size,size);
        bitmapRight = ViewUtils.decodeSampleBitmapFromResource(context.getResources(), R.drawable.fragment_home_emergency_able_right,size,size);
        size = bitmapCenter.getWidth();
        detector = new GestureDetector(context, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerY = SaveUtils.getEmergencyYPosition();
        centerX = SaveUtils.getEmergencyXPosition() ;
        if (centerY < 0) {
            centerY = ViewUtils.dp2px(75);
        }
        if (centerX < 0) {
            centerX = getWidth();
        }
    }

    private ObjectAnimator getAnimator(float x) {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(this, "centerX", centerX, x);
        }
        animator.setDuration(250);
        animator.setFloatValues(centerX, x);
        return animator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float dist;
                int w = getWidth();
                if (event.getX() > getWidth() / 2) {
                    dist = w - (size >>> 1);
                } else {
                    dist = (size >>> 1);
                }
                SaveUtils.setEmergencyPosition((int)dist,(int)centerY);
                getAnimator(dist).start();
                break;
            default:
        }
        return detector.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        determineState();
        drawBtn(canvas);
    }

    private void determineState() {
        int half = size >>> 1;
        int height = getHeight();
        int width = getWidth();
        if (centerY > height - half) {
            centerY = height - half;
        } else if (centerY < half) {
            centerY = half;
        }
        if (centerX >= width - half) {
            centerX = width - half;
            state = STATE_RIGHT;
        } else if (centerX <= half) {
            centerX = half;
            state = STATE_LEFT;
        } else {
            state = STATE_NONE;
        }
    }

    private void drawBtn(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        int half = size >>> 1;
        if (state == STATE_LEFT) {
            canvas.drawBitmap(bitmapLeft,0, centerY - half,paint);
        } else if (state == STATE_RIGHT) {
            canvas.drawBitmap(bitmapRight,getWidth() - size , centerY - half,paint);
        } else{
            canvas.drawBitmap(bitmapCenter,centerX - half, centerY - half,paint);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
         return isInSide(e);
    }

    private boolean isInSide(MotionEvent e){
        float x = e.getX();
        float y = e.getY();
        int half = size >>> 1;
        return x < centerX + half && x > centerX - half && y < centerY + half && y > centerY - half;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (listener != null && isInSide(e)){
            listener.onClick();
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        centerX = e2.getX();
        centerY = e2.getY();
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (listener != null && isInSide(e)){
            listener.onLongClick();
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
