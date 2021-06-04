package com.mgtech.maiganapp.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Keep;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class HomeHeaderCircleView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final float SCALE_MIN = 0.75f;
    private static final float SCALE_MAX = 1f;

    private static final int FLIP_MIN = 0;
    private static final int FLIP_MAX = 180;
    private static final int BITMAP_LENGTH = ViewUtils.dp2px(160);
    private Bitmap bitmap;
    private Rect srcRect;
    private Rect desRect;
    private ObjectAnimator animator;
    private float animatorFraction;
    private boolean state;
    private Camera camera;

    public HomeHeaderCircleView(Context context) {
        super(context);
        init(context, null);
    }

    public HomeHeaderCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HomeHeaderCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public HomeHeaderCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public float getAnimatorFraction() {
        return animatorFraction;
    }

    @Keep
    public void setAnimatorFraction(float animatorFraction) {
        this.animatorFraction = animatorFraction;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeHeaderCircleView);
        int res = typedArray.getResourceId(R.styleable.HomeHeaderCircleView_res, R.drawable.fragment_home_header_circle);
        typedArray.recycle();
        bitmap = ViewUtils.decodeSampleBitmapFromResource(context.getResources(), res, BITMAP_LENGTH, BITMAP_LENGTH);
        camera = new Camera();
        camera.setLocation(0,0,-100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        desRect = new Rect(-w/2, -h/2, w/2, h/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scale = SCALE_MIN + (SCALE_MAX - SCALE_MIN) * Math.abs(animatorFraction - 0.5f) * 2;
        float flip = FLIP_MIN + (FLIP_MAX - FLIP_MIN) * animatorFraction;
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(scale, scale, 0, 0);
        camera.save();
        camera.rotateY(flip);
        camera.applyToCanvas(canvas);
        canvas.drawBitmap(bitmap, srcRect, desRect, paint);
        camera.restore();
        canvas.restore();
    }

    private ObjectAnimator getAnimator() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(this, "animatorFraction", 0, 1);
            animator.setDuration(400);
        }
        return animator;
    }

    public void changeState() {
        state = !state;
        if (state) {
            getAnimator().start();
        } else {
            getAnimator().reverse();
        }
    }

    public boolean isAnimationOn(){
        if (animator != null){
            return animator.isRunning();
        }
        return false;
    }
}
