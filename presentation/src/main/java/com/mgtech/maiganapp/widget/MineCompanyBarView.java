package com.mgtech.maiganapp.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorRes;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 *
 * @author zhaixiang
 * 公司
 */

public class MineCompanyBarView extends View {
    private Paint paint;
    private boolean isSelected;
    private int selectColor;
    private int unSelectColor;
    private ObjectAnimator animator;
    private int color;
    private int width = ViewUtils.dp2px(14);
    private int height = ViewUtils.dp2px(4);
    private int padding = ViewUtils.dp2px(4);

    public MineCompanyBarView(Context context) {
        super(context);
        init();
    }

    public MineCompanyBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MineCompanyBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        super.onMeasure(MeasureSpec.makeMeasureSpec(width+padding*2,MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
    }

    private void init() {
        this.paint = new Paint();
        this.unSelectColor = Color.parseColor("#dddddd");
        this.selectColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        this.paint.setStrokeCap(Paint.Cap.ROUND);
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
        paint.setColor(color);
        int radius = getHeight()/2;
        paint.setStrokeWidth(getHeight());
        canvas.drawLine(radius+padding, radius,getWidth()-radius-padding,radius,paint);
    }
}
