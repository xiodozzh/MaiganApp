package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.mgtech.maiganapp.R;

/**
 * Created by zhaixiang on 2017/10/24.
 * 可滚动Action Bar
 */

public class ScrollActionBar extends LinearLayout {
    private int state;
    private static final int UP_STATE = 0;
    private static final int DOWN_STATE = 1;
    private static final int GOING_UP_STATE = 2;
    private static final int GOING_DOWN_STATE = 3;
    private float total = 10000;
    private Scroller scroller;
    private int height;
    private float deltaHeight;

    public ScrollActionBar(Context context) {
        super(context);
        init(context);
    }

    public ScrollActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ScrollActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.scroller = new Scroller(context);
        this.height = context.getResources().getDimensionPixelSize(R.dimen.large_height);
        this.deltaHeight = height / total;
    }

    public void change() {
        if (state == DOWN_STATE) {
            scrollToTop();
        } else if (state == UP_STATE) {
            scrollToBottom();
        }
    }

    public void scrollToTop() {
        if (state == DOWN_STATE || state == GOING_DOWN_STATE) {
            state = GOING_UP_STATE;
            scroller.startScroll(0, 0, 0, (int) total, 800);
            invalidate();
        }

    }

    public void scrollToBottom() {
        if (state == UP_STATE || state == GOING_UP_STATE) {
            state = GOING_DOWN_STATE;
            scroller.startScroll(0, 0, 0, (int) total, 800);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (state == GOING_DOWN_STATE) {
                scrollTo(0, (int) (scroller.getCurrY() * deltaHeight));
            }
            if (state == GOING_UP_STATE) {
                scrollTo(0, (int) (height - scroller.getCurrY() * deltaHeight));
            }
            postInvalidate();
        } else {
            if (state == GOING_DOWN_STATE) {
                state = DOWN_STATE;
            }
            if (state == GOING_UP_STATE) {
                state = UP_STATE;
            }
        }
    }
}
