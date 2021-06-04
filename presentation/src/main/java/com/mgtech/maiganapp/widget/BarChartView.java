package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.mgtech.maiganapp.data.model.StepModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/10/19.
 * 可滑动的柱状图
 */

public abstract class BarChartView extends View implements NestedScrollingChild {

    public interface ItemSelectListener {
        void onSelect(int index);
    }
    private static final int VALUE_TOP_DEFAULT = 5000;
    private static final String TAG = "LineChartView";
    private int valueTop = VALUE_TOP_DEFAULT;
    private static final int valueRight = 40;
    protected List<StepModel> dataValues;
    protected int width;
    protected int height;
    private GestureDetector gestureDetector;
    private Scroller mScroller;

    protected int selectIndex;
    protected ViewPort viewPort;

    private ItemSelectListener itemSelectListener;
    private RectF rectOneData = new RectF();

    private int minValue = 0;

    /**
     * 一个视野中的bar个数
     */
    private int numberOfBarInOneSight = 7;


    public BarChartView(Context context) {
        this(context, null, 0);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.axisXLabels = new ArrayList<>();
        this.dataValues = new ArrayList<>();

        viewPort = new ViewPort(numberOfBarInOneSight, valueTop);
//        initTestLabels();
//        initTestData();
        mScroller = new Scroller(context);
        selectIndex = dataValues.size() - 1;
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                int index = getTouchedIndex(motionEvent.getX());
                jumpToPosSmoothly(index);
                if (itemSelectListener != null) {
                    itemSelectListener.onSelect(index);
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                mScroller.abortAnimation();
                viewPort.moveCurrentXBy((int) v);
                isMoving = true;
                invalidate();
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                mScroller.abortAnimation();
                needJump = true;
                mScroller.fling(viewPort.getCurrX(), 0, -(int) v, 0, viewPort.getMinCurrX() - getWidth(), viewPort.getMaxCurrX() + getWidth(), 0, 0);
                invalidate();
                return false;
            }
        });
    }

    protected abstract void onDrawData(Canvas canvas, RectF rectF, boolean isSelected, Integer data);

    protected abstract void onDrawLabel(Canvas canvas, RectF rectF, boolean isSelected, String label);

    protected abstract void onDrawBackground(Canvas canvas, RectF rectF, boolean isSelected);

    protected abstract int getMaxPosition();

    protected abstract int getMinPosition();

    protected abstract int getDataPaddingTop();

    protected abstract int getDataPaddingBottom();

    protected abstract boolean selectAfterScroll();

    public void setItemSelectListener(ItemSelectListener itemSelectListener) {
        this.itemSelectListener = itemSelectListener;
    }

    public void setData(List<StepModel> data) {
        int max = 0;
        for (StepModel model:data){
            max = Math.max(max,model.stepCount);
//            Log.i(TAG, "setData: "+ model);
        }
        valueTop = Math.max(max,VALUE_TOP_DEFAULT);

        this.dataValues.clear();
        this.dataValues.addAll(data);
        this.selectIndex = data.size()-1;
    }

    public void setNumberOfBarInSight(int number){
        this.numberOfBarInOneSight = number;
    }

    public void setMaxValue(int maxValue){
        this.valueTop = maxValue;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
        this.jumpToPos(index);
    }

    public void invalidateView(){
        selectIndex = dataValues.size() - 1;
        viewPort = new ViewPort(numberOfBarInOneSight, valueTop);
        viewPort.setWidth(width);
        viewPort.setPaddingTop(getDataPaddingTop());
        viewPort.setPaddingBottom(getDataPaddingBottom());
        viewPort.setHeight(height);

        viewPort.setMaxCurrX(getMaxPosition());
        viewPort.setMinCurrX(getMinPosition());
        viewPort.setCurrX(viewPort.getMaxCurrX());
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        viewPort.setWidth(width);
        viewPort.setPaddingTop(getDataPaddingTop());
        viewPort.setPaddingBottom(getDataPaddingBottom());
        viewPort.setHeight(height);

        viewPort.setMaxCurrX(getMaxPosition());
        viewPort.setMinCurrX(getMinPosition());
        viewPort.setCurrX(viewPort.getMaxCurrX());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int[] scale = getDataAvailableScale();
        for (int i = scale[0]; i <= scale[1]; i++) {
            drawOneDataAndLabel(i, canvas);
        }
        super.onDraw(canvas);
    }


    private void drawOneDataAndLabel(int index, Canvas canvas) {
        if (index < 0) {
            index = 0;
        } else if (index > dataValues.size() - 1) {
            index = dataValues.size() - 1;
        }
        rectOneData.left = viewPort.getIntervalX() * index - viewPort.getCurrX();
        rectOneData.right = viewPort.getIntervalX() * (index + 1) - viewPort.getCurrX();
        rectOneData.top = 0;
        rectOneData.bottom = height;
        onDrawBackground(canvas, rectOneData, index == selectIndex);
        if (dataValues.get(index).stepCount > minValue) {
            onDrawData(canvas, rectOneData, index == selectIndex, dataValues.get(index).stepCount);
        }
        onDrawLabel(canvas, rectOneData, index == selectIndex, dataValues.get(index).dateString);
    }

    /**
     * 获取离点击位置最近的bar
     *
     * @param x 点击x坐标
     * @return 最近的bar的编号
     */
    private int getTouchedIndex(float x) {
        int[] scope = getDataScope();
        int index = 0;
        int min = Integer.MAX_VALUE;
        for (int i = scope[0]; i <= scope[1]; i++) {
            int pos = viewPort.getPositionX(i);
            int d = (int) Math.abs(pos - x);
            if (d < min) {
                index = i;
                min = d;
            }
        }
        return index;
    }

    /**
     * 获取有效范围
     *
     * @return 有效范围
     */
    private int[] getDataAvailableScale() {
        int first = (int) Math.max(0, Math.ceil((viewPort.getCurrX()) / (float) viewPort.getIntervalX()));
        if (first != 0) {
            first -= 1;
        }
        int last = (int) Math.min(dataValues.size() - 1, Math.floor((width + viewPort.getCurrX()) / (float) viewPort.getIntervalX()));
        return new int[]{first, last};
    }


    /**
     * 绘制数据可视范围
     *
     * @return 最大值和最小值
     */
    private int[] getDataScope() {
        int first = (int) Math.ceil((viewPort.getCurrX()) / (float) viewPort.getIntervalX());
        if (first != 0) {
            first -= 1;
        }
        int last = (int) Math.floor((width + viewPort.getCurrX()) / (float) viewPort.getIntervalX());
        return new int[]{first, last};
    }

    private boolean isMoving = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isMoving & !needJump) {
                    isMoving = false;
                    needJump = true;
                    jumpToNearestPos();
                }
                stopNestedScroll();
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int curr = mScroller.getCurrX();
            viewPort.setCurrX(curr);
            if (curr >= viewPort.getMaxCurrX() || curr <= viewPort.getMinCurrX()) {
                mScroller.abortAnimation();
                if (needJump) {
                    jumpToNearestPos();
                    needJump = false;
                }
            }
            postInvalidate();
        } else {
            if (needJump) {
                jumpToNearestPos();
                needJump = false;
            }
        }
        super.computeScroll();
    }

    private boolean needJump = false;


    private void jumpToNearestPos() {
        int[] scale = getDataScope();
        int middle = Math.max(0, Math.min(dataValues.size() - 1, (scale[0] + scale[1]) / 2));
        int pos1 = viewPort.getPositionX(middle);
        int pos2 = viewPort.getPositionX(middle + 1);
        int delta1 = pos1 - width / 2;
        int delta2 = pos2 - width / 2;
        boolean isMiddleSelected = Math.abs(delta1) <= Math.abs(delta2);
        int delta = isMiddleSelected ? delta1 : delta2;
        if (selectAfterScroll()) {
            this.selectIndex = isMiddleSelected ? middle : middle + 1;
        }
        viewPort.moveCurrentXBy(delta);
        invalidate();
    }

    /**
     * 跳转到指定位置
     *
     * @param position 位置
     */
    private void jumpToPos(int position) {
        if (position < 0) {
            position = 0;
        } else if (position > dataValues.size() - 1) {
            position = dataValues.size() - 1;
        }
        int pos = viewPort.getPositionX(position);
        viewPort.moveCurrentXBy(pos - width / 2);
        invalidate();
    }

    private void jumpToPosSmoothly(int position) {
        if (position < 0) {
            position = 0;
        } else if (position > dataValues.size() - 1) {
            position = dataValues.size() - 1;
        }
        selectIndex = position;
        int pos = viewPort.getPositionX(position);
        int dx = pos - width / 2;
        needJump = false;
        mScroller.abortAnimation();
        mScroller.startScroll(viewPort.getCurrX(), 0, dx, 0, 400);
        invalidate();
    }

    public class ViewPort {
        /**
         * 当前左端位置
         */
        private int currX;
        /**
         * 当前顶端位置
         */
        private int currY;
        /**
         * 最大可到达
         */
        private int maxCurrX;
        /**
         * 最小可到达
         */
        private int minCurrX;
        /**
         * 水平最大数值
         */
        private float w;
        /**
         * 垂直最大数值
         */
        private float h;
        /**
         * 顶部边距
         */
        private int paddingTop;
        /**
         * 左边距
         */
        private int paddingLeft;
        /**
         * 底边距
         */
        private int paddingBottom;
        /**
         * 右边距
         */
        private int paddingRight;
        /**
         * 单位1代表的像素（宽）
         */
        private float intervalX;
        /**
         * 单位1代表的像素（高）
         */
        private float intervalY;
        /**
         * 像素宽
         */
        private int width;
        /**
         * 像素高
         */
        private int height;

        public ViewPort(float w, float h) {
            this.w = w;
            this.h = h;
        }

        public int getCurrX() {
            return currX;
        }

        public void setCurrX(int currX) {
            if (currX < minCurrX) {
                this.currX = minCurrX;
            } else if (currX > maxCurrX) {
                this.currX = maxCurrX;
            } else {
                this.currX = currX;
            }

        }

        public int getCurrY() {
            return currY;
        }

        public void setCurrY(int currY) {
            this.currY = currY;
        }

        public int getMaxCurrX() {
            return maxCurrX;
        }

        public void setMaxCurrX(int maxCurrX) {
            this.maxCurrX = maxCurrX;
        }

        public int getMinCurrX() {
            return minCurrX;
        }

        public void setMinCurrX(int minCurrX) {
            this.minCurrX = minCurrX;
        }

        public float getW() {
            return w;
        }

        public void setW(float w) {
            this.w = w;
        }

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }

        public int getPaddingTop() {
            return paddingTop;
        }

        public void setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
        }

        public int getPaddingLeft() {
            return paddingLeft;
        }

        public void setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
        }

        public int getPaddingBottom() {
            return paddingBottom;
        }

        public void setPaddingBottom(int paddingBottom) {
            this.paddingBottom = paddingBottom;
        }

        public int getPaddingRight() {
            return paddingRight;
        }

        public void setPaddingRight(int paddingRight) {
            this.paddingRight = paddingRight;
        }

        public float getIntervalX() {
            return intervalX;
        }

        public float getIntervalY() {
            return intervalY;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
            this.intervalX = ((width - paddingLeft - paddingRight) / w);
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
            intervalY = ((height - paddingTop - paddingBottom) / h);
        }

        public int getPositionX(float x) {
            return Math.round((x + 0.5f) * intervalX + paddingLeft - currX);
        }

        public void moveCurrentXBy(int dx) {
            currX += dx;
            currX = Math.min(maxCurrX, currX);
            currX = Math.max(minCurrX, currX);
        }
    }
}
