package com.mgtech.maiganapp.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.ColorRes;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mgtech.maiganapp.R;

/**
 *
 * @author zhaixiang
 * 引导页面的点
 */

public class GuideDotView extends View {
    private Paint paint;
    private int radius;
    private boolean isSelected;
    private int selectColor;
    private int unSelectColor;
    private ObjectAnimator animator;
    private int color;

    public GuideDotView(Context context) {
        super(context);
        init();
    }

    public GuideDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuideDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getColor() {
        return color;
    }

    @Keep
    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.radius = getWidth()/2;
    }

    private void init() {
        this.paint = new Paint();

        this.unSelectColor = Color.parseColor("#dddddd");
        this.selectColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        color = unSelectColor;
        this.paint.setAntiAlias(true);
    }

    public void setColor(@ColorRes int unSelectColor, @ColorRes int selectColor){
        this.unSelectColor = ContextCompat.getColor(getContext(),unSelectColor);
        this.selectColor = ContextCompat.getColor(getContext(),selectColor);
    }

    public void select(){
        if (isSelected){
            return;
        }
        isSelected = true;

        getAnimator().start();
    }

    private ObjectAnimator getAnimator(){
        if (animator == null){
            animator = ObjectAnimator.ofArgb(this,"color",unSelectColor,selectColor);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return animator;
    }

    public void disSelect(){
        if (!isSelected){
            return;
        }
        isSelected = false;
        getAnimator().reverse();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius == 0) {
            radius = getWidth() / 2;
        }
        paint.setColor(color);
        canvas.drawCircle(radius,radius,radius, paint);
    }
}
