package com.mgtech.maiganapp.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;


/**
 * @author zhaixiang
 */
public class DanceView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private Camera camera = new Camera();
    private float angle = 0;
    private float rotation = 0;
    private ValueAnimator animator;
    private LightingColorFilter filter = new LightingColorFilter(0xffffff,0x000800);


    public DanceView(Context context) {
        this(context, null);
    }

    public DanceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = decodeSampleBitmapFromResource(getContext().getResources(), R.mipmap.ic_launcher, 100, 100);
        if (animator != null){
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(-1, 6);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 0) {
                    angle = 0;
                    rotation = (value + 1) * 45;
                    int color = (Math.round(0x08 * Math.abs(rotation) / 45)<<8) & 0x00ff00;
                    filter = new LightingColorFilter(0xffffff,color);
                } else if (value <= 5) {
                    filter = new LightingColorFilter(0xffffff,0x000800);
                    rotation = 45;
                    angle = value * 360 / 5;
                } else {
                    angle = 0;
                    rotation = (6-value) * 45;
                    int color = (Math.round(0x08 * Math.abs(rotation) / 45f)<<8) & 0x00ff00;
                    filter = new LightingColorFilter(0xffffff,color);

                }
                postInvalidate();
            }
        });
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = getWidth() / 2 - ViewUtils.dp2px(50);
        int top = getHeight() / 2 - ViewUtils.dp2px(50);
        Log.i("Dance", "onDraw: " + angle);

        paint.setColorFilter(filter);
        canvas.drawColor(Color.WHITE);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(-angle);
        canvas.clipRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, 0);
        camera.rotateX(-rotation);
        camera.applyToCanvas(canvas);
        camera.rotateX(rotation);

        canvas.rotate(angle);
        canvas.translate(-getWidth() / 2, -getHeight() / 2);
        canvas.drawBitmap(bitmap, left, top, paint);
        canvas.restore();


        paint.setColorFilter(null);

        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(-angle);
        canvas.clipRect(-getWidth() / 2, 0, getWidth() / 2, getHeight() / 2);


        canvas.rotate(angle);
        canvas.translate(-getWidth() / 2, -getHeight() / 2);
        canvas.drawBitmap(bitmap, left, top, paint);
        canvas.restore();

    }


    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * 计算压缩率
     *
     * @param options   图片参数信息
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return 压缩率
     */
    private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int scale = Math.min(options.outWidth / reqWidth, options.outHeight / reqHeight);
        if (scale < 1) {
            scale = 1;
        }
        return scale;
    }


}
