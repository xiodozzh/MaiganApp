package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by zhaixiang on 2017/11/3.
 * 用于垂直旋转的layout
 */

public class RotateLayout extends RelativeLayout{
    private Camera camera;
    private float fraction;

    public RotateLayout(Context context) {
        super(context);
        init();
    }

    public RotateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        camera = new Camera();
        camera.setLocation(0,0,camera.getLocationZ()*3);
    }

    public void setFraction(float fraction){
        this.fraction = fraction;
        setAlpha(1-fraction);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        canvas.save();
        canvas.translate(centerX,centerY);
        camera.save();
        camera.rotateX(90* fraction);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.translate(-centerX,-centerY);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}
