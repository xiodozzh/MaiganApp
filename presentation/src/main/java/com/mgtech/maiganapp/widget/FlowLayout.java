package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mgtech.maiganapp.R;

/**
 * Created by zhaixiang on 2017/8/8.
 * 水平方向流式布局
 */

public class FlowLayout extends ViewGroup {
    private int childHorizontalMargin;
    private int childVerticalMargin;

    public FlowLayout(Context context) {
        super(context);
        init();
    }



    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        childVerticalMargin = getContext().getResources().getDimensionPixelSize(R.dimen.narrow_padding);
        childHorizontalMargin = getContext().getResources().getDimensionPixelSize(R.dimen.narrow_padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        int selfWidth = resolveSize(0, widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childWidth = 0;
        int childHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = child.getLayoutParams();
            child.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight,
                            lp.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom,
                            lp.height));

            childWidth = Math.max(childWidth, child.getMeasuredWidth());
            childHeight = Math.max(childHeight, child.getMeasuredHeight());
        }
        int number = (selfWidth - paddingLeft - paddingRight) / (childWidth + childHorizontalMargin);

        int wantedHeight = (childHeight + childVerticalMargin) * (count / number + (count % number == 0 ? 0 : 1)) + paddingTop + paddingBottom;
        setMeasuredDimension(selfWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count == 0) {
            return;
        }

        int myWidth = r - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
//        View child = getChildAt(0);
        int childWidth = 0;
        int childHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            childWidth = Math.max(childWidth, child.getMeasuredWidth());
            childHeight = Math.max(childHeight, child.getMeasuredHeight());
        }

        int number = (myWidth - paddingLeft - paddingRight) / (childWidth+childHorizontalMargin);

        for (int i = 0; i < count; ++i) {
            int x = i % number;
            int y = i / number;
            View childView = getChildAt(i);
            childView.layout(paddingLeft + x * childWidth + x * childHorizontalMargin,
                    paddingTop + y * childHeight + y * childVerticalMargin,
                    paddingLeft + (x + 1) * childWidth + x * childHorizontalMargin,
                    paddingTop + (y + 1) * childHeight + y * childVerticalMargin);
        }
    }
}
