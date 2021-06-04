package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public class FragmentFriendStateBarView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float fraction;
    private int startFrom;
    private int startTo;
    private int endFrom;
    private int endTo;
    private Rect textBounds1 = new Rect();
    private Rect textBounds2 = new Rect();
    private static final int PADDING = ViewUtils.dp2px(23);
    private static final int CENTER = ViewUtils.dp2px(46);

    public FragmentFriendStateBarView(Context context) {
        super(context);
        init(context);
    }

    public FragmentFriendStateBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FragmentFriendStateBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(context, R.color.primaryBlue));
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.subhead_material));
        String text1 = context.getString(R.string.friend_i_monitor);
        String text2 = context.getString(R.string.friend_monitor_me);
        paint.getTextBounds(text1,0,text1.length(), textBounds1);
        paint.getTextBounds(text2,0,text2.length(), textBounds2);
    }

    public void setFraction(float fraction){
        this.fraction = fraction;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int textWidth1 = textBounds1.right - textBounds1.left;
        int textWidth2 = textBounds2.right - textBounds2.left;
//        startFrom = getWidth()/2 - PADDING - textWidth1;
//        endFrom = getWidth()/2 - PADDING;
//        startTo = getWidth()/2 + PADDING;
//        endTo = getWidth()/2 + PADDING + textWidth2;
        startFrom = (getWidth() - CENTER - textWidth1 - textWidth2)/2;
        endFrom = startFrom + textWidth1;
        endTo = getWidth() - startFrom;
        startTo = endTo - textWidth2;
        paint.setStrokeWidth(getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int start = (int) (startFrom + fraction * (startTo - startFrom));
        int end = (int) (endFrom + fraction * (endTo - endFrom));
        canvas.drawLine(start,getHeight()/2,end,getHeight()/2,paint);
    }
}
