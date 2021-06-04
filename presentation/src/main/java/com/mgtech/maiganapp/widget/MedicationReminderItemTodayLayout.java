//package com.mgtech.maiganapp.widget;
//
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.support.annotation.Keep;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//
//import com.mgtech.maiganapp.utils.ViewUtils;
//
///**
// * @author zhaixiang
// */
//public class MedicationReminderItemTodayLayout extends RelativeLayout {
//    private int minLayoutHeight = ViewUtils.dp2px(68);
//    private ObjectAnimator animator;
//    private int maxLayoutHeight = ViewUtils.dp2px(124);
//    private int currentHeight;
//    private ViewGroup.LayoutParams params;
//
//    public MedicationReminderItemTodayLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public MedicationReminderItemTodayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public MedicationReminderItemTodayLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        Log.i("layout", "onSizeChanged: " + w + " " + h);
////        if (minLayoutHeight == 0 ||maxLayoutHeight == 0) {
////            minLayoutHeight = getChildAt(0).getHeight();
////            maxLayoutHeight = getChildAt(1).getHeight() + minLayoutHeight;
////            params = getLayoutParams();
////        }
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        params = getLayoutParams();
//        Log.i("layout", "onFinishInflate: " + minLayoutHeight + " " + maxLayoutHeight);
//
//    }
//
//    public int getCurrentHeight() {
//        return currentHeight;
//    }
//
//    @Keep
//    public void setCurrentHeight(int currentHeight) {
//        this.currentHeight = currentHeight;
//        Log.i("layout", "setCurrentHeight: " + currentHeight);
//        params.height = this.currentHeight;
//        setLayoutParams(params);
//        invalidate();
//    }
//
//    public void open(){
//        getAnimator().start();
//    }
//
//    public void close(){
//        getAnimator().reverse();
//    }
//
//
//    public ObjectAnimator getAnimator() {
//        if (animator == null){
//            Log.i("layout", "getAnimator: " + minLayoutHeight + " " + maxLayoutHeight);
//            animator = ObjectAnimator.ofInt(this,"currentHeight", minLayoutHeight, maxLayoutHeight);
//        }
//        return animator;
//    }
//
//}
