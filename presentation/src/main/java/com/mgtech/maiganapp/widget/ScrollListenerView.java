package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by zhaixiang on 2017/11/3.
 * 监听滑动变化的ScrollView
 */

public class ScrollListenerView extends ScrollView{
    private GestureDetector gestureDetector;
    private ScrollListener listener;
    private int minDistance = 1;
    private int lastX;
    private int lastY;
    private int scrollX;
    private int scrollY;

    public ScrollListener getListener() {
        return listener;
    }

    public void setListener(ScrollListener listener) {
        this.listener = listener;
    }

    public ScrollListenerView(Context context) {
        super(context);
        init(context);
    }

    public ScrollListenerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollListenerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
//        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onShowPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                scrollBy(0, (int) distanceY);
//                Log.e("ttttttttt", "onScroll: "  );
//                invalidate();
//                return false;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                return false;
//            }
//        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        gestureDetector.onTouchEvent(ev);
//        return true;
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (listener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) ev.getX();
                    lastY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) (ev.getX() - lastX);
                    int dy = (int) (ev.getY() - lastY);
//                    if (dx >= minDistance || dy >=minDistance){
//                        lastX = (int) ev.getX();
//                        lastY = (int) ev.getY();
                        if (Math.abs(scrollX-getScrollX()) > minDistance || Math.abs(scrollY - getScrollY())>minDistance){
                            scrollX = getScrollX();
                            scrollY = getScrollY();
                            listener.onScroll(scrollX,scrollY);
                        }
//                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface ScrollListener{
        void onScroll(int x,int y);
    }

    @Override
    public void computeScroll() {
        if (Math.abs(scrollX-getScrollX()) > minDistance || Math.abs(scrollY - getScrollY())>minDistance){
            scrollX = getScrollX();
            scrollY = getScrollY();
            listener.onScroll(scrollX,scrollY);
        }
        super.computeScroll();
    }
}
