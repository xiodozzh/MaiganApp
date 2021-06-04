package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.List;

/**
 * 侧边字母选择
 * @author zhaixiang
 */

public class SideBarView extends View {
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private int choose = -1;
    private static final int MAX_SINGLE_HEIGHT = ViewUtils.dp2px(30);
    private static final int MIN_SINGLE_HEIGHT = ViewUtils.dp2px(10);
    private Paint paint;
    private TextView mTextDialog;
    private int chooseColor;
    private int color;
    private int padding;

    //当我们滑动的时候会弹出Dialog  这个Dialog主要是一个TextView 这个方法是设置TextView的内容的方法。
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBarView(Context context) {
        super(context);
        init(context);
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        paint = new Paint();
        paint.setTextSize(ViewUtils.sp2px(displayMetrics, 14));
        chooseColor = ContextCompat.getColor(context, R.color.primary_text);
        color = ContextCompat.getColor(context, R.color.secondary_text);
//        paint.setTypeface(Typeface.DEFAULT_BOLD);//画笔设置默认加粗
        paint.setAntiAlias(true);//设置抗锯齿为True
    }

    public void setLetters(List<String> letters){
        if (letters != null) {
            b = new String[letters.size()];
            for (int i = 0; i < b.length; i++) {
                b[i] = letters.get(i);
            }
            invalidate();
        }
    }

    /**
     * 重写OnDraw方法 侧边条实现的核心
     * <p>
     * 每次调用invalidate(); 会调用一次onDraw方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = Math.min(MAX_SINGLE_HEIGHT,height / b.length);
        singleHeight = Math.max(MIN_SINGLE_HEIGHT,singleHeight);
        padding = (height - singleHeight * b.length)/2;
        padding = Math.max(0,padding);
        Log.i("tag", "onDraw: "+ singleHeight + " " + padding);
        for (int i = 0; i < b.length; i++) {
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setFakeBoldText(false);
            if (i == choose) {
                paint.setColor(chooseColor);
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight + padding;
            canvas.drawText(b[i], xPos, yPos, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;

        OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

        int c = (int) (y / getHeight() * b.length);
        if (y < padding){
            c = -1;
        }else if(y > getHeight() -padding){
            c = b.length - 1;
        }else{
            c = (int) ((y-padding) / (getHeight()-padding*2) * b.length);
        }

        switch (action) {
            //当手指抬起的时候
            case MotionEvent.ACTION_UP:
//                setBackgroundDrawable(new ColorDrawable(0x00000000));//设置一个完全透明的背景
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    //将之前的dialog设置为隐藏
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    //定义接口
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
