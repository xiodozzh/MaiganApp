package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * Created by zhaixiang on 2018/3/6.
 * 弹出的测量按钮
 */

public class MeasureView extends View {
    private static final int STATE_HIDE = 0;
    private static final int STATE_SHOW = 1;
    private static final int STATE_SHOWING = 3;
    private static final int STATE_HIDING = 4;
    /**
     * 动画进度
     */
    private float progress = 0f;
    /**
     * 点移动到顶部
     */
    private static final float midProgress1 = 0.4f;
    /**
     * 点展开
     */
    private static final float midProgress2 = 0.5f;
    /**
     * 点收起
     */
    private static final float midProgress3 = 0.6f;
    /**
     * 背景画笔
     */
    private Paint paint;
    /**
     * 文字画笔
     */
    private Paint textPaint;
    /**
     * 点当前宽度
     */
    private float pointWidth;
    /**
     * 点当前高度
     */
    private float pointHeight;
    /**
     * 移动时点的半径
     */
    private float state1PointSizePx;
    /**
     * 展开后点高度
     */
    private float state2PointHeightPx;
    /**
     * 展开后点宽度
     */
    private float state2PointWidthPx;
    /**
     * 手机像素密度
     */
    private float density;
    /**
     * 点中心x轴位置
     */
    private float centerX;
    /**
     * 点1中心y轴位置
     */
    private float centerY1;
    /**
     * 点2中心y轴位置
     */
    private float centerY2;
    private Scroller scroller;
    private int state = STATE_HIDE;
    /**
     * 第一点轨迹半径
     */
    private float pathRadius1;
    /**
     * 第二点轨迹半径
     */
    private float pathRadius2;
    /**
     * 文字高度
     */
    private float textHeight;

    private ClickListener listener;
    private GestureDetector gestureDetector;

    /**
     * 点击回调函数
     */
    public interface ClickListener {
        /**
         * 点击测量ECG
         */
        void measureEcgClick();

        /**
         * 点击测量脉图
         */
        void measurePwClick();
    }


    public MeasureView(Context context) {
        super(context);
        init(context);
    }

    public MeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
        paint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.white));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(ViewUtils.sp2px(displayMetrics, 16));
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.top;
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(220);
        state1PointSizePx = ViewUtils.dp2px(density, 28);
        state2PointHeightPx = ViewUtils.dp2px(density, 32);
        scroller = new Scroller(context);
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                onClick(e.getX(), e.getY());
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick(e.getX(), e.getY());
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                onClick(e.getX(), e.getY());
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void show() {
        if (state == STATE_HIDE || state == STATE_HIDING) {
            scroller.startScroll(0, 0, 500, 0, 1000);
            invalidate();
            state = STATE_SHOWING;
        }
    }

    public void hide() {
        if (state == STATE_SHOW || state == STATE_SHOWING) {
            scroller.startScroll(500, 0, 1000, 0, 1000);
            invalidate();
            state = STATE_HIDING;
        }
    }

    public void switchState() {
        if (state == STATE_SHOW || state == STATE_SHOWING) {
            hide();
        } else if (state == STATE_HIDE || state == STATE_HIDING) {
            show();
        }
    }

    public int getState() {
        return state;
    }

    private void onClick(float x, float y) {
        if (state != STATE_SHOW || listener == null) {
            return;
        }
        int delta = 20;
        if (x >= centerX - pointWidth / 2 - delta && x <= centerX + pointWidth / 2 + delta) {
            if (y >= centerY1 - pointHeight / 2 - delta && y <= centerY1 + pointHeight / 2 + delta) {
                listener.measureEcgClick();
            } else if (y >= centerY2 - pointHeight / 2 - delta && y <= centerY2 + pointHeight / 2 + delta) {
                listener.measurePwClick();
            } else {
                hide();
            }
        } else {
            hide();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return state == STATE_SHOW|| state == STATE_SHOWING || super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            progress = scroller.getCurrX() / 1000f;
            postInvalidate();
        } else {
            if (state == STATE_HIDING) {
                state = STATE_HIDE;
            } else if (state == STATE_SHOWING) {
                state = STATE_SHOW;
            }
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        state2PointWidthPx = width / 3;
        pathRadius1 = width / 4;
        pathRadius2 = pathRadius1 + ViewUtils.dp2px(density, 48);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();
        canvas.drawRoundRect(centerX - pointWidth / 2, centerY1 - pointHeight / 2, centerX + pointWidth / 2,
                centerY1 + pointHeight / 2, pointHeight / 2, pointHeight / 2, paint);
        canvas.drawRoundRect(centerX - pointWidth / 2, centerY2 - pointHeight / 2, centerX + pointWidth / 2,
                centerY2 + pointHeight / 2, pointHeight / 2, pointHeight / 2, paint);
        if (progress > midProgress1 && progress < midProgress3) {
            canvas.drawText("测量心电图", centerX, centerY1 - textHeight / 2, textPaint);
            canvas.drawText("测量脉图", centerX, centerY2 - textHeight / 2, textPaint);
        }
    }

    private void calculate() {
        if (progress < midProgress1) {
            pointWidth = state1PointSizePx;
            pointHeight = state1PointSizePx;
            float p = progress / midProgress1;
            double cos = Math.cos(Math.PI / 2 * p);
            double sin = Math.sin(Math.PI / 2 * p);
            centerX = (float) (pathRadius1 * cos + getWidth() / 2);
            centerY1 = (float) (getHeight() - pathRadius1 * sin);
            centerY2 = (float) (getHeight() - pathRadius2 * sin);
        } else if (progress < midProgress2) {
            float p = (progress - midProgress1) / (midProgress2 - midProgress1);
            pointWidth = state2PointWidthPx * p + state1PointSizePx * (1 - p);
            pointHeight = state2PointHeightPx * p + state1PointSizePx * (1 - p);
            centerX = getWidth() / 2;
            centerY1 = getHeight() - pathRadius1;
            centerY2 = getHeight() - pathRadius2;

        } else if (progress < midProgress3) {
            float p = (progress - midProgress2) / (midProgress3 - midProgress2);
            pointWidth = state2PointWidthPx * (1 - p) + state1PointSizePx * p;
            pointHeight = state2PointHeightPx * (1 - p) + state1PointSizePx * p;

            centerX = getWidth() / 2;
            centerY1 = getHeight() - pathRadius1;
            centerY2 = getHeight() - pathRadius2;


        } else {
            pointWidth = state1PointSizePx;
            pointHeight = state1PointSizePx;

            float r = getWidth() / 4;
            float p = (progress - midProgress3) / (1 - midProgress3);
            double cos = Math.cos(Math.PI / 2 * (1 - p));
            double sin = Math.sin(Math.PI / 2 * (1 - p));
            centerX = (float) (getWidth() / 2 - r * cos);
            centerY1 = (float) (getHeight() - pathRadius1 * sin);
            centerY2 = (float) (getHeight() - pathRadius2 * sin);


        }
    }
}
