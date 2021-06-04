package com.mgtech.maiganapp.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by zhaixiang on 2017/4/24.
 * 关注好友条目
 */

public class RelationItemLayout extends LinearLayout {
    private Scroller mScroller;
    private int scrollState;
    private int mMaxLength;
    /**
     * 未滑动
     */
    private static final int STATE_SCROLL_STATE_1 = 1;
    /**
     * 滑动后
     */
    private static final int STATE_SCROLL_STATE_2 = 0;
    /**
     * 取消滑动开始
     */
    private static final int STATE_2_SCROLLING_TO_STATE_1 = 2;
    /**
     * 滑动开始
     */
    private static final int STATE_1_SCROLLING_TO_STATE_2 = 3;

    public RelationItemLayout(Context context) {
        super(context);
        init(context);
    }

    public RelationItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RelationItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RelationItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context,new AccelerateDecelerateInterpolator());
        scrollState = STATE_SCROLL_STATE_1;
    }

    /**
     * 设置滑动最大偏移量
     * @param maxScrollLength 最大滑动偏移
     */
    public void setMaxLength(int maxScrollLength){
        this.mMaxLength = maxScrollLength;
    }

    public void offsetBack(){
        int position = getScrollX();
        mScroller.startScroll(position,0,-position,0,200);
        invalidate();
        scrollState = STATE_2_SCROLLING_TO_STATE_1;
    }

    /**
     * 是否在滑动打开或者正在打开状态
     * @return true 滑动或正在滑动向打开
     */
    public boolean isOn(){
        return scrollState == STATE_SCROLL_STATE_2 || scrollState == STATE_1_SCROLLING_TO_STATE_2;
    }

    /**
     * 关闭
     */
    private void scrollOff(){
        if (scrollState == STATE_SCROLL_STATE_2) {
            mScroller.startScroll(getScrollX(), 0, -mMaxLength, 0, 200);
            invalidate();
            scrollState = STATE_2_SCROLLING_TO_STATE_1;
        }
    }

    /**
     * 打开
     */
    private void scrollOn(){
        if (scrollState == STATE_SCROLL_STATE_1) {
            mScroller.startScroll(getScrollX(), 0, mMaxLength, 0, 200);
            invalidate();
            scrollState = STATE_1_SCROLLING_TO_STATE_2;
        }
    }

    /**
     * 切换状态
     */
    public void switchState(){
        if (scrollState == STATE_SCROLL_STATE_2) {
            scrollOff();
        }else if (scrollState == STATE_SCROLL_STATE_1){
            scrollOn();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            if (scrollState == STATE_2_SCROLLING_TO_STATE_1){
                scrollState = STATE_SCROLL_STATE_1;
            }
            if (scrollState == STATE_1_SCROLLING_TO_STATE_2){
                scrollState = STATE_SCROLL_STATE_2;
            }
        }
    }
}
