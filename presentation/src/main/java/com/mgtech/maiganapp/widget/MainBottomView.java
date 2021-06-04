package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;

/**
 * @author zhaixiang
 */
public class MainBottomView extends ConstraintLayout implements BottomNavigatorSelectView.SelectListener {
    public static final int POSITION_HOME = 0;
    public static final int POSITION_HEALTH = 1;
    public static final int POSITION_FRIEND = 3;
    public static final int POSITION_MINE = 4;

    private OnItemSelectedListener listener;
    private ViewGroup layoutItems;
    private ViewGroup layoutHome;
    private ViewGroup layoutHealth;
    private ViewGroup layoutFriend;
    private ViewGroup layoutMine;

    private int selectColor;
    private int unSelectColor;
    private BottomNavigatorSelectView selectView;

    @Override
    public void clickLeft() {
        if (listener != null) {
            listener.onMeasureEcgClick();
        }
    }

    @Override
    public void clickRight() {
        if (listener != null) {
            listener.onMeasurePwClick();
        }
    }

    public interface OnItemSelectedListener {
        /**
         * 底部导航栏选择
         *
         * @param position 点击的位置
         */
        void onBottomNavigatorItemClick(int position);

        /**
         * 点击测量脉图
         */
        void onMeasurePwClick();

        /**
         * 点击测量ECG
         */
        void onMeasureEcgClick();

    }

    public MainBottomView(Context context) {
        this(context, null);
    }

    public MainBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainBottomView);
        int childImgSrc0 = typedArray.getResourceId(R.styleable.MainBottomView_item_src_0, 0);
        int childImgSrc1 = typedArray.getResourceId(R.styleable.MainBottomView_item_src_1, 0);
        int childImgSrc2 = typedArray.getResourceId(R.styleable.MainBottomView_item_src_2, 0);
        int childImgSrc3 = typedArray.getResourceId(R.styleable.MainBottomView_item_src_3, 0);

        String text0 = typedArray.getString(R.styleable.MainBottomView_item_title_0);
        String text1 = typedArray.getString(R.styleable.MainBottomView_item_title_1);
        String text2 = typedArray.getString(R.styleable.MainBottomView_item_title_2);
        String text3 = typedArray.getString(R.styleable.MainBottomView_item_title_3);

        selectColor = typedArray.getColor(R.styleable.MainBottomView_selectedColor, ContextCompat.getColor(context, R.color.colorPrimary));
        unSelectColor = typedArray.getColor(R.styleable.MainBottomView_unSelectedColor, ContextCompat.getColor(context, R.color.colorAccent));
        int centerPopupTextColor = typedArray.getColor(R.styleable.MainBottomView_center_popup_text_color, ContextCompat.getColor(context, R.color.primary_text));
        String centerPopupTextLeft = typedArray.getString(R.styleable.MainBottomView_center_popup_text_left);
        String centerPopupTextRight = typedArray.getString(R.styleable.MainBottomView_center_popup_text_right);

        typedArray.recycle();

        selectColor = getResources().getColor(R.color.colorPrimary);
        unSelectColor = getResources().getColor(R.color.light_text);


        inflate(context, R.layout.activity_main_bottom, this);
        layoutItems = findViewById(R.id.layout_items);

        layoutHome = findViewById(R.id.layout_home);
        layoutHealth = findViewById(R.id.layout_health);
        layoutFriend = findViewById(R.id.layout_friend);
        layoutMine = findViewById(R.id.layout_mine);

        bindData(layoutHome, childImgSrc0, text0);
        bindData(layoutHealth, childImgSrc1, text1);
        bindData(layoutFriend, childImgSrc2, text2);
        bindData(layoutMine, childImgSrc3, text3);

        selectView = findViewById(R.id.view_select);
        selectView.setLeftText(centerPopupTextLeft);
        selectView.setRightText(centerPopupTextRight);
        selectView.setTextColor(centerPopupTextColor);
        selectView.setListener(this);
        selectView.open();

        ImageView centerView = findViewById(R.id.iv_measure);
        centerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectView.select();
            }
        });

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectView(v);
                selectView.close();
                if (listener != null) {
                    if (v == layoutHome) {
                        listener.onBottomNavigatorItemClick(POSITION_HOME);
                    } else if (v == layoutHealth) {
                        listener.onBottomNavigatorItemClick(POSITION_HEALTH);
                    } else if (v == layoutFriend) {
                        listener.onBottomNavigatorItemClick(POSITION_FRIEND);
                    } else if (v == layoutMine) {
                        listener.onBottomNavigatorItemClick(POSITION_MINE);
                    }
                }
            }
        };
        layoutHome.setOnClickListener(onClickListener);
        layoutHealth.setOnClickListener(onClickListener);
        layoutFriend.setOnClickListener(onClickListener);
        layoutMine.setOnClickListener(onClickListener);
    }


    private void bindData(View view, int src, String text) {
        TextView tv = view.findViewById(R.id.tv);
        tv.setText(text);
        ImageView iv = view.findViewById(R.id.iv);
        iv.setImageResource(src);
    }

    public void setUnread(boolean isUnread, int position) {
        ViewGroup view;
        switch (position) {
            case POSITION_HOME:
                view = layoutHome;
                break;
            case POSITION_FRIEND:
                view = layoutFriend;
                break;
            case POSITION_HEALTH:
                view = layoutHealth;
                break;
            case POSITION_MINE:
                view = layoutMine;
                break;
            default:
                return;
        }
        UnreadImageView iv = view.findViewById(R.id.iv);
        iv.setUnread(isUnread);
    }

    public void select(int position) {
        View child;
        switch (position) {
            case POSITION_HOME:
                child = layoutHome;
                break;
            case POSITION_HEALTH:
                child = layoutHealth;
                break;
            case POSITION_FRIEND:
                child = layoutFriend;
                break;
            case POSITION_MINE:
                child = layoutMine;
                break;
            default:
                return;
        }
        for (int i = 0; i < layoutItems.getChildCount(); i++) {
            View c = layoutItems.getChildAt(i);
            if (c == child) {
                selectChild(c, true);
            } else {
                selectChild(c, false);
            }
        }
    }

    private void selectView(View view) {
        for (int i = 0; i < layoutItems.getChildCount(); i++) {
            View c = layoutItems.getChildAt(i);
            if (c == view) {
                selectChild(c, true);
            } else {
                selectChild(c, false);
            }
        }
    }

    private void selectChild(View child, boolean select) {
        if (child instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) child;
            group.setSelected(select);
            for (int i = 0; i < group.getChildCount(); i++) {
                selectChild(group.getChildAt(i), select);
            }
        } else {
            child.setSelected(select);
            if (child instanceof ImageView) {
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable();
                if (drawable == null) {
                    return;
                }
                drawable = drawable.mutate();
                if (select) {
                    drawable.setColorFilter(selectColor, PorterDuff.Mode.SRC_ATOP);
                } else {
                    drawable.setColorFilter(unSelectColor, PorterDuff.Mode.SRC_ATOP);
                }
            } else if (child instanceof TextView) {
                TextView tv = (TextView) child;
                tv.setTextColor(select ? selectColor : unSelectColor);
            }
        }
    }

    public void setOnBottomNavigatorViewItemClickListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
