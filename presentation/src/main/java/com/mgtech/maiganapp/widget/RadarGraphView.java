package com.mgtech.maiganapp.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.annotation.Keep;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class RadarGraphView extends View implements GestureDetector.OnGestureListener {
    private static final String TAG = "RadarGraphView";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int HALF_CIRCLE_ANGLE = 180;
    private static final int ICON_SIDE_SMALL = ViewUtils.dp2px(20);
    private static final int ICON_SIDE_BIG = ViewUtils.dp2px(28);
    private static final int ICON_PADDING = ViewUtils.dp2px(4);
    private static final int LINE_WIDTH = ViewUtils.dp2px(1);
    private static final int RADAR_ARM_LENGTH = ViewUtils.dp2px(50);
    private int centerX;
    private int centerY;
    private int armLength;
    private float singleAngle;
    private float offsetAngle;
    private int radarLineColor;
    private int radarDataColor;
    private int radarLineLevelNumber;

    float[] lineX;
    float[] lineY;
    private Path path;
    private float[] data;
    private float maxValue;
    private int count;
    private int textHeight = ViewUtils.dp2px(25);
    private Bitmap[] icons;
    private Bitmap[] selectedIcons;

    private Rect rectSrc;
    private Rect rectDst;

    private GestureDetector detector;
    private ObjectAnimator animator;

    private int textColor;
    private int textSize;
    private String[] titles;
    private Listener listener;

    private int currentIndex = 0;

    public RadarGraphView(Context context) {
        super(context);
        init(context, null);
    }

    public RadarGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public interface Listener {
        void onItemSelect(int index);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarGraphView);
        radarDataColor = typedArray.getColor(R.styleable.RadarGraphView_data_area_color, Color.parseColor("#942bbdfe"));
        radarLineColor = typedArray.getColor(R.styleable.RadarGraphView_radar_line_color, Color.parseColor("#ffaaaaaa"));
        count = typedArray.getInt(R.styleable.RadarGraphView_side_count, 6);
        maxValue = typedArray.getFloat(R.styleable.RadarGraphView_max_data_value, 10f);
        radarLineLevelNumber = typedArray.getInt(R.styleable.RadarGraphView_radar_level_count, 3);
        int[] iconRes = new int[10];
        int[] selectIconRes = new int[10];
        titles = new String[10];
        iconRes[0] = typedArray.getResourceId(R.styleable.RadarGraphView_icon0, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[1] = typedArray.getResourceId(R.styleable.RadarGraphView_icon1, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[2] = typedArray.getResourceId(R.styleable.RadarGraphView_icon2, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[3] = typedArray.getResourceId(R.styleable.RadarGraphView_icon3, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[4] = typedArray.getResourceId(R.styleable.RadarGraphView_icon4, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[5] = typedArray.getResourceId(R.styleable.RadarGraphView_icon5, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[6] = typedArray.getResourceId(R.styleable.RadarGraphView_icon6, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[7] = typedArray.getResourceId(R.styleable.RadarGraphView_icon7, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[8] = typedArray.getResourceId(R.styleable.RadarGraphView_icon8, R.drawable.fragment_history_health_graph_radar_icon_bp);
        iconRes[9] = typedArray.getResourceId(R.styleable.RadarGraphView_icon9, R.drawable.fragment_history_health_graph_radar_icon_bp);


        selectIconRes[0] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon0, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[1] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon1, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[2] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon2, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[3] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon3, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[4] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon4, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[5] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon5, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[6] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon6, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[7] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon7, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[8] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon8, R.drawable.fragment_history_health_graph_radar_icon_bp_select);
        selectIconRes[9] = typedArray.getResourceId(R.styleable.RadarGraphView_selected_icon9, R.drawable.fragment_history_health_graph_radar_icon_bp_select);

        textColor = typedArray.getColor(R.styleable.RadarGraphView_text_color, Color.parseColor("#28dbfe"));
        textSize = typedArray.getDimensionPixelSize(R.styleable.RadarGraphView_text_size, ViewUtils.dp2px(18));

        titles[0] = typedArray.getString(R.styleable.RadarGraphView_title0);
        titles[1] = typedArray.getString(R.styleable.RadarGraphView_title1);
        titles[2] = typedArray.getString(R.styleable.RadarGraphView_title2);
        titles[3] = typedArray.getString(R.styleable.RadarGraphView_title3);
        titles[4] = typedArray.getString(R.styleable.RadarGraphView_title4);
        titles[5] = typedArray.getString(R.styleable.RadarGraphView_title5);
        titles[6] = typedArray.getString(R.styleable.RadarGraphView_title6);
        titles[7] = typedArray.getString(R.styleable.RadarGraphView_title7);
        titles[8] = typedArray.getString(R.styleable.RadarGraphView_title8);
        titles[9] = typedArray.getString(R.styleable.RadarGraphView_title9);
        typedArray.recycle();

        lineX = new float[count];
        lineY = new float[count];
        path = new Path();
        icons = new Bitmap[count];
        selectedIcons = new Bitmap[count];

        for (int i = 0; i < count; i++) {
            icons[i] = BitmapFactory.decodeResource(context.getResources(), iconRes[i % iconRes.length]);
            selectedIcons[i] = BitmapFactory.decodeResource(context.getResources(), selectIconRes[i % iconRes.length]);
        }
        data = new float[10];
        rectSrc = new Rect();
        rectDst = new Rect();

        detector = new GestureDetector(context, this);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = (int) (fontMetrics.bottom - fontMetrics.top);
    }

    public void setData(float hr, float bp, float pm, float tpr, float co, float v0) {
        data[0] = bp;
        data[1] = v0;
        data[2] = tpr;
        data[3] = hr;
        data[4] = co;
        data[5] = pm;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = (h - textHeight - ICON_PADDING) / 2;
        armLength = RADAR_ARM_LENGTH;
        singleAngle = 360f / count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        calculatePoint();
        drawRadarLine(canvas);
        drawRadarData(canvas);
        drawIcons(canvas);
        drawText(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = resolveSize((RADAR_ARM_LENGTH + ICON_PADDING * 2 + ICON_SIDE_BIG * 2) * 2, widthMeasureSpec);
        int height = resolveSize((RADAR_ARM_LENGTH + ICON_PADDING * 2 + ICON_SIDE_BIG * 2 + textHeight) * 2, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Keep
    public float getOffsetAngle() {
        return offsetAngle;
    }

    @Keep
    public void setOffsetAngle(float offsetAngle) {
        this.offsetAngle = offsetAngle;
        invalidate();
    }

    private void calculatePoint() {
        for (int i = 0; i < count; i++) {
            float alpha = (float) Math.toRadians(singleAngle * i + offsetAngle + 90);
            lineX[i] = (float) Math.cos(alpha) * armLength;
            lineY[i] = (float) Math.sin(alpha) * armLength;
        }
    }

    /**
     * 绘制雷达图的背景线
     *
     * @param canvas 画布
     */
    private void drawRadarLine(Canvas canvas) {
        paint.setColor(radarLineColor);
        paint.setStrokeWidth(LINE_WIDTH);
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < radarLineLevelNumber; j++) {
                float scale = (radarLineLevelNumber - j) / (float) radarLineLevelNumber;
                canvas.drawLine(
                        lineX[i % count] * scale,
                        lineY[i % count] * scale,
                        lineX[(i + 1) % count] * scale,
                        lineY[(i + 1) % count] * scale, paint);
            }
            canvas.drawLine(0, 0, lineX[i], lineY[i], paint);
        }

    }

    /**
     * 绘制雷达图
     *
     * @param canvas 画布
     */
    private void drawRadarData(Canvas canvas) {
        path.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(radarDataColor);

        for (int i = 0; i < count; i++) {
            float x = lineX[i] * data[i] / maxValue;
            float y = lineY[i] * data[i] / maxValue;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制图标
     *
     * @param canvas 画布
     */
    private void drawIcons(Canvas canvas) {
        paint.setColor(Color.WHITE);
        for (int i = 0; i < count; i++) {
            int length = armLength + ICON_PADDING + ICON_SIDE_BIG / 2;
            float iconAngle = (singleAngle * i + offsetAngle + 90) % 360;
            if (iconAngle < 0) {
                iconAngle += 360;
            }
            float alpha = (float) Math.toRadians(iconAngle);
            float x = (float) Math.cos(alpha) * length;
            float y = (float) Math.sin(alpha) * length;

            if (iconAngle - 90 >= singleAngle && iconAngle - 90 <= singleAngle * (count - 1)) {
                rectDst.left = (int) (x - ICON_SIDE_SMALL / 2);
                rectDst.top = (int) (y - ICON_SIDE_SMALL / 2);
                rectDst.right = (int) (x + ICON_SIDE_SMALL / 2);
                rectDst.bottom = (int) (y + ICON_SIDE_SMALL / 2);
            } else {
                float delta = Math.abs((iconAngle - 90) / singleAngle);
                int size = (int) (ICON_SIDE_BIG + (ICON_SIDE_SMALL - ICON_SIDE_BIG) * delta);

                rectDst.left = (int) (x - size / 2);
                rectDst.top = (int) (y - size / 2);
                rectDst.right = (int) (x + size / 2);
                rectDst.bottom = (int) (y + size / 2);
            }
            if (i == currentIndex) {
                rectSrc.left = 0;
                rectSrc.top = 0;
                rectSrc.right = selectedIcons[i].getWidth();
                rectSrc.bottom = selectedIcons[i].getHeight();
                canvas.drawBitmap(selectedIcons[i], rectSrc, rectDst, paint);
            } else {
                rectSrc.left = 0;
                rectSrc.top = 0;
                rectSrc.right = icons[i].getWidth();
                rectSrc.bottom = icons[i].getHeight();
                canvas.drawBitmap(icons[i], rectSrc, rectDst, paint);
            }
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(titles[currentIndex], 0, armLength + ICON_SIDE_BIG + textHeight + 2 * ICON_PADDING, paint);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int clickIndex = getRegion(e);
        if (clickIndex != -1) {
            float angle = clickIndex * singleAngle;
            if (angle > HALF_CIRCLE_ANGLE) {
                angle -= 360;
            }
            float stopAngle = offsetAngle - angle;
            stopAngle -= stopAngle % (int) singleAngle;
            calculateCurrentIndex(stopAngle);
            getAnimator(offsetAngle, stopAngle).start();
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

    /**
     * 获取点击的位置
     *
     * @param e 点击事件
     * @return 位置，-1为无效
     */
    private int getRegion(MotionEvent e) {
        float x = e.getX() - centerX;
        float y = e.getY() - centerY;
        if (x * x + y * y < armLength * armLength / 4) {
            return -1;
        }
        // 0~360度角度
        double a = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
        Log.i(TAG, "getRegion: " + a);
        return (int) ((a + 270 + singleAngle / 2) % 360 / singleAngle);
    }

    private ObjectAnimator getAnimator(float start, float stop) {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(this, "offsetAngle", 0, 1);
        }
        animator.setFloatValues(start, stop);
        return animator;
    }

    /**
     * 计算新的选中的index
     *
     * @param stopOffset 动画停止的偏移角度
     */
    private void calculateCurrentIndex(float stopOffset) {
        currentIndex = (count - (int) (stopOffset / singleAngle)) % count;
        if (currentIndex < 0) {
            currentIndex += count;
        }
        if (listener != null) {
            listener.onItemSelect(currentIndex);
        }
    }
}
