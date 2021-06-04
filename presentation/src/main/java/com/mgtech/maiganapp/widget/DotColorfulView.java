package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgtech.maiganapp.R;

/**
 * @author zhaixiang
 */
public class DotColorfulView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DotColorfulView(Context context) {
        super(context);
        init(context,null);
    }

    public DotColorfulView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DotColorfulView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotColorfulView);
        int color = typedArray.getColor(R.styleable.DotColorfulView_color,ContextCompat.getColor(context,R.color.primaryBlue));
        typedArray.recycle();

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(getWidth()/2,getHeight()/2);
        canvas.drawCircle(getWidth()/2,getHeight()/2,radius,paint);
    }
}
