package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.mgtech.maiganapp.R;

public class BottomNavigatorCenterLayout extends ConstraintLayout {
    public BottomNavigatorCenterLayout(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigatorCenterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigatorCenterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        inflate(context, R.layout.layout_bottom_navigator_center, this);
    }


}
