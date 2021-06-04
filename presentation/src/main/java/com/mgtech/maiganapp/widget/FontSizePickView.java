package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

public class FontSizePickView extends View {
    private int sidePadding;
    private int indicatorHeight;
    private int markHeight;
    private int textBottomMargin;
    private static final int levels = 5;
    private String[] levelText;
    private float[] levelScale = Utils.FONT_SIZE;
    private Paint linePaint;
    private Paint indicatorPaint;
    private Paint textPaint;
    private int index = 1;
    private Path path;
    private float textStandardSize;
    private GestureDetector detector;
    private SelectListener listener;

    public interface SelectListener{
        void onFontSizeLevel(int index);
    }

    public SelectListener getListener() {
        return listener;
    }

    public void setListener(SelectListener listener) {
        this.listener = listener;
    }

    public FontSizePickView(Context context) {
        super(context);
        init(context);
    }

    public FontSizePickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontSizePickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FontSizePickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener = null;
    }

    private void init(Context context) {
        levelText = context.getResources().getStringArray(R.array.font_size_scale);
        sidePadding = context.getResources().getDimensionPixelSize(R.dimen.xnarrow_height);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        indicatorHeight = ViewUtils.dp2px(20);
        markHeight = ViewUtils.dp2px(12);
        textBottomMargin = ViewUtils.dp2px(14);
        textStandardSize = ViewUtils.sp2px(metrics, 16);

        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(context, R.color.primary_text));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint.setColor(ContextCompat.getColor(context, R.color.primary_text));
        linePaint.setStrokeWidth(1.2f);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStyle(Paint.Style.FILL);

        path = new Path();

        detector = new GestureDetector(context, new MyGestureListener());
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            setIndex(calculateIndex(e.getX()));
//            return super.onSingleTapConfirmed(e);
//        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            setIndex(calculateIndex(e.getX()));
            if (listener != null){
                listener.onFontSizeLevel(index);
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int newIndex = calculateIndex(e2.getX());
            if (newIndex != index){
                setIndex(newIndex);
                if (listener != null){
                    listener.onFontSizeLevel(index);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private int calculateIndex(float x) {
        int startX = sidePadding;
        int endX = getWidth() - sidePadding;
        int segmentWidth = (endX - startX) / (levels - 1);
        if (x < startX + segmentWidth / 2) {
            return 0;
        }
        if (x >= endX - segmentWidth / 2) {
            return levels - 1;
        }
        return Math.round(x - (startX + segmentWidth / 2)) / segmentWidth + 1;
    }

    /**
     * 设置指针值
     *
     * @param index 指针值
     */
    public void setIndex(int index) {
        if (index < 0 || index >= levels) {
            return;
        }
        this.index = index;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        int lineY = getHeight();
        int markY = lineY - markHeight;
        int startX = sidePadding;
        int endX = getWidth() - sidePadding;
        canvas.drawLine(startX, lineY, endX, lineY, linePaint);
        int segmentWidth = (endX - startX) / (levels - 1);
        for (int i = 0; i < levels; i++) {
            canvas.drawLine(sidePadding + i * segmentWidth, markY, sidePadding + i * segmentWidth, lineY, linePaint);
        }
        // 绘制指针
        path.reset();
        int indicatorTopY = lineY - indicatorHeight;
        int indicatorTopX = sidePadding + index * segmentWidth;
        int indicatorBottomX1 = indicatorTopX - indicatorHeight / 2;
        int indicatorBottomX2 = indicatorTopX + indicatorHeight / 2;
        path.moveTo(indicatorTopX, indicatorTopY);
        path.lineTo(indicatorBottomX1, lineY);
        path.lineTo(indicatorBottomX2, lineY);
        path.close();
        canvas.drawPath(path, indicatorPaint);

        // 绘制文字
        int textBaseLineY = indicatorTopY - textBottomMargin;
        for (int i = 0; i < levels; i++) {
            int x = sidePadding + i * segmentWidth;
            textPaint.setTextSize(textStandardSize * levelScale[i]);
            canvas.drawText(levelText[i], x, textBaseLineY, textPaint);
        }
    }

}
