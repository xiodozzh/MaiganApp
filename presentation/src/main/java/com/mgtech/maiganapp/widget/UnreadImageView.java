package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.mgtech.maiganapp.utils.ViewUtils;

public class UnreadImageView extends AppCompatImageView {
    private int offset;
    private int dotRadius;
    private int numberRoundRadius;
    private int numberLength;
    private int textSize;
    private boolean unread;
    private int unreadNumber;
    private boolean isDot;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int unreadColor;
    private int textColor;

    public UnreadImageView(Context context) {
        super(context);
        init(context);
    }

    public UnreadImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UnreadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        offset = ViewUtils.dp2px(2, context);
        dotRadius = ViewUtils.dp2px(5, context);
        numberRoundRadius = ViewUtils.dp2px(7, context);
        numberLength = ViewUtils.dp2px(2, context);
        textSize = ViewUtils.sp2px(10, context);
        unreadColor = Color.RED;
        textColor = Color.WHITE;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
        isDot = true;
        invalidate();
    }

    public void setUnreadNumber(int number) {
        isDot = false;
        this.unreadNumber = number;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingEnd();
        if (isDot) {
            if (unread) {
                paint.setColor(unreadColor);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStyle(Paint.Style.FILL);
                int y = Math.max(paddingTop - offset, 0) + dotRadius;
                int x = getWidth() - Math.max(paddingRight - offset, 0) - dotRadius;
                canvas.drawCircle(x, y, dotRadius, paint);
            }
        } else {
            if (unreadNumber > 0) {
                paint.setColor(unreadColor);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(numberRoundRadius * 2);
                int y = paddingTop - offset + numberRoundRadius;
                int startX = getWidth() - paddingRight - numberRoundRadius - numberLength + offset;
                int endX = getWidth() - paddingRight - numberRoundRadius + offset;
                canvas.drawLine(startX, y, endX, y, paint);

                paint.setColor(textColor);
                paint.setTextSize(textSize);
                paint.setTextAlign(Paint.Align.CENTER);
                String text;
                if (unreadNumber >= 99) {
                    text = "99+";
                } else {
                    text = String.valueOf(unreadNumber);
                }
                canvas.drawText(text, (startX + endX) / 2, y, paint);
            }
        }
    }
}
