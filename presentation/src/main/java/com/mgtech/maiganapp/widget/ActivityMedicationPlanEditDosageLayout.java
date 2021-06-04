package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class ActivityMedicationPlanEditDosageLayout extends ViewGroup {
    private List<MedicationPlanModel.DosageItem> dataList = new ArrayList<>();
    private static final int MAX_CHILDREN_SIZE = 4;
    private static final int CHILD_PADDING = ViewUtils.dp2px(16);
    private int childLength;
    private Listener listener;
    private String[] unitStrings;

    public ActivityMedicationPlanEditDosageLayout(Context context) {
        super(context);
        init();
    }

    public ActivityMedicationPlanEditDosageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActivityMedicationPlanEditDosageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        unitStrings = getResources().getStringArray(R.array.medicine_dosage_unit);
    }

    public interface Listener {

        /**
         * 点击选择
         */
        void clickUpdate(ActivityMedicationPlanEditDosageLayout view, int index, MedicationPlanModel.DosageItem item);

        /**
         * 点击添加
         */
        void clickAdd(ActivityMedicationPlanEditDosageLayout view);

        void clickDelete(ActivityMedicationPlanEditDosageLayout view, int index);
    }

    private String getUnitString(int unitIndex) {
        if (unitIndex < 0 || unitIndex >= unitStrings.length) {
            return "";
        }
        return unitStrings[unitIndex];
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public List<MedicationPlanModel.DosageItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<MedicationPlanModel.DosageItem> dataList) {
        this.dataList.clear();
        removeAllViews();
        int size = 0;
        if (dataList != null) {
            size = Math.min(MAX_CHILDREN_SIZE, dataList.size());
        }
        for (int i = 0; i < size; i++) {
            MedicationPlanModel.DosageItem item = dataList.get(i);
            ActivityMedicationPlanEditDosageCardView cardView = new ActivityMedicationPlanEditDosageCardView(getContext());
            cardView.setTime(item.time);
            if (item.dosage == Math.round(item.dosage)){
                cardView.setDosage(Math.round(item.dosage) + getUnitString(item.dosageUnitType));
            }else {
                cardView.setDosage(item.dosage + getUnitString(item.dosageUnitType));
            }
            cardView.setState(ActivityMedicationPlanEditDosageCardView.TYPE_NORMAL);
            cardView.setClickListener(clickListener);
            super.addView(cardView);
            this.dataList.add(item);
        }
        if (size < MAX_CHILDREN_SIZE) {
            ActivityMedicationPlanEditDosageCardView cardView = new ActivityMedicationPlanEditDosageCardView(getContext());
            cardView.setState(ActivityMedicationPlanEditDosageCardView.TYPE_NONE);
            cardView.setClickListener(clickListener);
            super.addView(cardView);
        }
    }

    public void addData(MedicationPlanModel.DosageItem item) {
        dataList.add(0, item);
        ActivityMedicationPlanEditDosageCardView cardView = new ActivityMedicationPlanEditDosageCardView(getContext());
        cardView.setTime(item.time);
        if (item.dosage == Math.round(item.dosage)){
            cardView.setDosage(Math.round(item.dosage) + getUnitString(item.dosageUnitType));
        }else {
            cardView.setDosage(item.dosage + getUnitString(item.dosageUnitType));
        }
        cardView.setState(ActivityMedicationPlanEditDosageCardView.TYPE_NORMAL);
        cardView.setClickListener(clickListener);

        int size = getChildCount();
        boolean isFull = size == MAX_CHILDREN_SIZE;
        if (isFull) {
            super.removeViewAt(MAX_CHILDREN_SIZE - 1);
            super.addView(cardView, MAX_CHILDREN_SIZE - 1);
        } else {
            super.addView(cardView, getChildCount() - 1);
        }
    }

    public void update(int index, MedicationPlanModel.DosageItem item) {
        if (index >= dataList.size()) {
            return;
        }
        dataList.set(index, item);
        ActivityMedicationPlanEditDosageCardView cardView = (ActivityMedicationPlanEditDosageCardView) getChildAt(index);
        if (item.dosage == Math.round(item.dosage)){
            cardView.setDosage(Math.round(item.dosage) + getUnitString(item.dosageUnitType));
        }else {
            cardView.setDosage(item.dosage + getUnitString(item.dosageUnitType));
        }
        cardView.setTime(item.time);
        cardView.setState(ActivityMedicationPlanEditDosageCardView.TYPE_NORMAL);
        cardView.invalidate();
    }

    public void removeData(int index) {
        dataList.remove(index);
        removeViewAt(index);
        int size = getChildCount();
        ActivityMedicationPlanEditDosageCardView last = (ActivityMedicationPlanEditDosageCardView) getChildAt(size - 1);
        if (last.getState() != ActivityMedicationPlanEditDosageCardView.TYPE_NONE) {
            ActivityMedicationPlanEditDosageCardView cardView = new ActivityMedicationPlanEditDosageCardView(getContext());
            cardView.setState(ActivityMedicationPlanEditDosageCardView.TYPE_NONE);
            cardView.setClickListener(clickListener);
            super.addView(cardView, size);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        childLength = (width - (MAX_CHILDREN_SIZE - 1) * CHILD_PADDING) / MAX_CHILDREN_SIZE;
        measureChildren(MeasureSpec.makeMeasureSpec(childLength, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childLength, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, childLength);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout((childLength + CHILD_PADDING) * i,
                    0,
                    (childLength + CHILD_PADDING) * i + childLength,
                    childLength);

        }
    }

    private ActivityMedicationPlanEditDosageCardView.ClickListener clickListener =
            new ActivityMedicationPlanEditDosageCardView.ClickListener() {
                @Override
                public void clickDelete(ActivityMedicationPlanEditDosageCardView view) {
                    int index = getChildIndex(view);
                    if (listener != null && index != -1) {
//                        removeData(index);
                        listener.clickDelete(ActivityMedicationPlanEditDosageLayout.this, index);
                    }
                }

                @Override
                public void clickSelect(ActivityMedicationPlanEditDosageCardView view) {
                    view.setState(ActivityMedicationPlanEditDosageCardView.TYPE_SELECTED);
                    int index = getChildIndex(view);
                    if (listener != null && index != -1) {
                        listener.clickUpdate(ActivityMedicationPlanEditDosageLayout.this, index, dataList.get(index));
                    }
                }

                @Override
                public void clickAdd(ActivityMedicationPlanEditDosageCardView view) {
                    if (listener != null) {
                        listener.clickAdd(ActivityMedicationPlanEditDosageLayout.this);
                    }
                }
            };

    private int getChildIndex(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            if (view == getChildAt(i)) {
                return i;
            }
        }
        return -1;
    }
}
