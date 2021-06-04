//package com.mgtech.maiganapp.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.Drawable;
//import android.support.v4.content.ContextCompat;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.mgtech.maiganapp.R;
//import com.mgtech.maiganapp.utils.ViewUtils;
//
///**
// * @author zhaixiang
// */
//public class BottomNavigator extends ViewGroup implements BottomNavigatorSelectView.SelectListener {
//    private static final int BACKGROUND_HEIGHT = ViewUtils.dp2px(50);
//    private static final int MEASURE_BITMAP_WIDTH = ViewUtils.dp2px(86);
//    private static final int CHILD_COUNT = 5;
//    private static final int SIDE_CHILD_COUNT = 4;
//
//    private int currentIndex;
//
//    private int selectedColor;
//    private int unSelectedColor;
//    private OnItemSelectedListener onItemSelectedListener;
//    private BottomNavigatorSelectView centerView;
//
//
//    public interface OnItemSelectedListener {
//        /**
//         * 底部导航栏选择
//         *
//         * @param position 点击的位置
//         */
//        void onBottomNavigatorItemClick(int position);
//
//        /**
//         * 点击测量脉图
//         */
//        void onMeasurePwClick();
//
//        /**
//         * 点击测量ECG
//         */
//        void onMeasureEcgClick();
//
//    }
//
//    public BottomNavigator(Context context) {
//        this(context, null);
//    }
//
//    public BottomNavigator(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public BottomNavigator(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigator);
//        int childImgSrc0 = typedArray.getResourceId(R.styleable.BottomNavigator_item_src_0, 0);
//        int childImgSrc1 = typedArray.getResourceId(R.styleable.BottomNavigator_item_src_1, 0);
//        int childImgSrc2 = typedArray.getResourceId(R.styleable.BottomNavigator_item_src_2, 0);
//        int childImgSrc3 = typedArray.getResourceId(R.styleable.BottomNavigator_item_src_3, 0);
//
//        String text0 = typedArray.getString(R.styleable.BottomNavigator_item_title_0);
//        String text1 = typedArray.getString(R.styleable.BottomNavigator_item_title_1);
//        String text2 = typedArray.getString(R.styleable.BottomNavigator_item_title_2);
//        String text3 = typedArray.getString(R.styleable.BottomNavigator_item_title_3);
//
//        selectedColor = typedArray.getColor(R.styleable.BottomNavigator_selectedColor, ContextCompat.getColor(context, R.color.colorPrimary));
//        unSelectedColor = typedArray.getColor(R.styleable.BottomNavigator_unSelectedColor, ContextCompat.getColor(context, R.color.colorAccent));
//        int centerPopupText = typedArray.getColor(R.styleable.BottomNavigator_center_popup_text_color, ContextCompat.getColor(context, R.color.primary_text));
//        String centerPopupTextLeft = typedArray.getString(R.styleable.BottomNavigator_center_popup_text_left);
//        String centerPopupTextRight = typedArray.getString(R.styleable.BottomNavigator_center_popup_text_right);
//
//        typedArray.recycle();
//
//        centerView = generateCenterChildLayout(centerPopupText, centerPopupTextLeft, centerPopupTextRight);
//        addView(generateChildLayout(childImgSrc0, text0));
//        addView(generateChildLayout(childImgSrc1, text1));
//        addView(generateChildLayout(childImgSrc2, text2));
//        addView(generateChildLayout(childImgSrc3, text3));
//        addView(centerView);
//
//        select(currentIndex);
//
//        for (int i = 0; i < SIDE_CHILD_COUNT; i++) {
//            final int index = i;
//            getChildAt(i).setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onItemSelectedListener != null) {
//                        onItemSelectedListener.onBottomNavigatorItemClick(index);
//                    }
//                }
//            });
//        }
//        setBackgroundColor(Color.TRANSPARENT);
//    }
//
//    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
//        this.onItemSelectedListener = onItemSelectedListener;
//    }
//
//    public boolean isCenterOpen() {
//        return centerView.isOpen();
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int leftPadding = getPaddingStart();
//        int rightPadding = getPaddingRight();
//        int topPadding = getPaddingTop();
//        int bottomPadding = getPaddingBottom();
//
//        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        // 子view数量必须为5
//        if (CHILD_COUNT != getChildCount()) {
//            return;
//        }
//        int chileWidthSpec = MeasureSpec.makeMeasureSpec(
//                (specWidthSize - leftPadding - rightPadding - MEASURE_BITMAP_WIDTH) / 4, MeasureSpec.EXACTLY);
//        int chileHeightSpec = MeasureSpec.makeMeasureSpec(
//                specHeightSize - topPadding - bottomPadding, MeasureSpec.AT_MOST);
//        for (int i = 0; i < SIDE_CHILD_COUNT; i++) {
//            getChildAt(i).measure(chileWidthSpec, chileHeightSpec);
//        }
//
//        int centerChildWidthSpec = MeasureSpec.makeMeasureSpec(specWidthSize, MeasureSpec.EXACTLY);
//        int centerChildHeightSpec = MeasureSpec.makeMeasureSpec(specHeightSize - topPadding - bottomPadding, MeasureSpec.EXACTLY);
//        getChildAt(SIDE_CHILD_COUNT).measure(centerChildWidthSpec, centerChildHeightSpec);
//        setMeasuredDimension(specWidthSize, specHeightSize);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        if (getChildCount() != CHILD_COUNT) {
//            return;
//        }
//        int top = getHeight() - BACKGROUND_HEIGHT;
//        int left = getPaddingLeft();
//        int width = (getWidth() - getPaddingLeft() - getPaddingRight() - MEASURE_BITMAP_WIDTH) / 4;
//        getChildAt(0).layout(left, top, width, getHeight());
//        getChildAt(1).layout(left + width, top, width * 2, getHeight());
//        getChildAt(2).layout(left + width * 2 + MEASURE_BITMAP_WIDTH, top, left + width * 3 + MEASURE_BITMAP_WIDTH, getHeight());
//        getChildAt(3).layout(left + width * 3 + MEASURE_BITMAP_WIDTH, top, left + width * 4 + MEASURE_BITMAP_WIDTH, getHeight());
//        getChildAt(4).layout(0, 0, r - l, b - t);
//    }
//
//    private View generateChildLayout(int imgRes, String text) {
//        View layout =  LayoutInflater.from(
//                getContext()).inflate(R.layout.layout_bottom_navigator_item, this, false);
//        TextView tv = layout.findViewById(R.id.tv);
//        ImageView iv = layout.findViewById(R.id.iv);
//        tv.setText(text);
//        iv.setImageResource(imgRes);
//        return layout;
//    }
//
//    /**
//     * 生成中间按钮
//     *
//     * @param centerPopupTextColor 文字颜色
//     * @param centerPopupTextLeft  左文字
//     * @param centerPopupTextRight 右文字
//     * @return 中间按钮
//     */
//    private BottomNavigatorSelectView generateCenterChildLayout(int centerPopupTextColor, String centerPopupTextLeft, String centerPopupTextRight) {
//        BottomNavigatorSelectView view = new BottomNavigatorSelectView(getContext());
//        view.setListener(this);
//        view.setLeftText(centerPopupTextLeft);
//        view.setRightText(centerPopupTextRight);
//        view.setTextColor(centerPopupTextColor);
//        return view;
//    }
//
//    /**
//     * 选择
//     *
//     * @param position 位置
//     */
//    public void select(int position) {
//        if (position >= getChildCount() || position < 0) {
//            return;
//        }
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            if (i == position) {
//                selectChild(child, true);
//            } else {
//                selectChild(child, false);
//            }
//        }
//        currentIndex = position;
//    }
//
//    private void selectChild(View child, boolean select) {
//        if (child instanceof ViewGroup) {
//            ViewGroup group = (ViewGroup) child;
//            group.setSelected(select);
//            for (int i = 0; i < group.getChildCount(); i++) {
//                selectChild(group.getChildAt(i), select);
//            }
//        } else {
//            if (child instanceof ImageView) {
//                ImageView iv = (ImageView) child;
//                Drawable drawable = iv.getDrawable().mutate();
//                drawable.setColorFilter(select ? selectedColor : unSelectedColor, PorterDuff.Mode.SRC_ATOP);
//            } else if (child instanceof TextView) {
//                TextView tv = (TextView) child;
//                tv.setTextColor(select ? selectedColor : unSelectedColor);
//            }
//        }
//    }
//
//
//    @Override
//    public void clickLeft() {
//        if (onItemSelectedListener != null) {
//            onItemSelectedListener.onMeasurePwClick();
//        }
//    }
//
//    @Override
//    public void clickRight() {
//        if (onItemSelectedListener != null) {
//            onItemSelectedListener.onMeasureEcgClick();
//        }
//    }
//
//}
