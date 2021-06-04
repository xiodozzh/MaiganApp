package com.mgtech.maiganapp.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.ThresholdModel;
import com.mgtech.maiganapp.viewmodel.SetAbnormalRangeViewModel;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * @date 2017/8/29
 * 设置异常推送
 */

public class SetAbnormalRangeActivity extends BaseActivity<SetAbnormalRangeViewModel> {
    public static final String TYPE = "type";
    public static final String ENTITY = "entity";
    public static final int PS = 0;
    public static final int PD = 1;
//    public static final int HR = 2;
    public static final int V0 = 3;

    private PopupWindow popupWindow;
    private static Handler handler = new Handler();
    @Bind(R.id.root)
    View root;
    @Bind(R.id.layout_upper)
    View upper;
    @Bind(R.id.layout_lower)
    View lower;
    private int[] currentValue;
    private int[] rangeUpper;
    private int[] rangeLower;
    private int[] standard;
    private static int[] PS_UPPER = {100, 160};
    private static int[] PS_LOWER = {80, 90};
    private static int[] PD_UPPER = {80, 110};
    private static int[] PD_LOWER = {50, 70};
    private static int[] HR_UPPER = {90, 120};
    private static int[] HR_LOWER = {50, 70};
    private static int[] PS_STANDARD = {90, 140};
    private static int[] PD_STANDARD = {60, 90};
    private static int[] HR_STANDARD = {60, 100};

    public static Intent getCallingIntent(Context context, int type, ThresholdModel model) {
        Intent intent = new Intent(context, SetAbnormalRangeActivity.class);
        intent.putExtra(SetAbnormalRangeActivity.TYPE, type);
        intent.putExtra(SetAbnormalRangeActivity.ENTITY, model);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.saveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Intent intent = new Intent();
                intent.putExtra(ENTITY, viewModel.thresholdModel);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        int type = getIntent().getIntExtra(TYPE, PS);
        ThresholdModel model = getIntent().getParcelableExtra(ENTITY);
        viewModel.setType(type);
        viewModel.thresholdModel = model;
        switch (type) {
            case PS:
                rangeUpper = PS_UPPER;
                rangeLower = PS_LOWER;
                standard = PS_STANDARD;
                currentValue = new int[]{model.psLow, model.psHigh};
                viewModel.title.set(getString(R.string.notification_ps_abnormal_reminder));
                break;
            case PD:
                rangeUpper = PD_UPPER;
                rangeLower = PD_LOWER;
                standard = PD_STANDARD;
                currentValue = new int[]{model.pdLow, model.pdHigh};
                viewModel.title.set(getString(R.string.notification_pd_abnormal_reminder));
                break;
//            case HR:
//                rangeUpper = HR_UPPER;
//                rangeLower = HR_LOWER;
//                standard = HR_STANDARD;
//                currentValue = new int[]{model.hrLow, model.hrHigh};
//                viewModel.title.set(getString(R.string.notification_hr_abnormal_reminder));
//                break;
            default:
        }
        viewModel.setRange(currentValue);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_abnormal_range;
    }

    @OnClick(R.id.btn_back)
    void back() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @OnClick(R.id.btn_save)
    void save() {
        viewModel.save();
    }

    @OnClick(R.id.layout_upper)
    void upper() {
        showPopupWindow(rangeUpper[0] - 1, rangeUpper[1], currentValue[1], new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (value == rangeUpper[0] - 1) {
                    return getString(R.string.notification_no_reminder);
                } else if (value == standard[1]) {
                    return value + getString(R.string.notification_standard);
                }
                return value + "";
            }
        }, new Select() {
            @Override
            public void onSelect(int number) {
                int[] current = viewModel.getCurrentRange();
                if (number == rangeUpper[0] - 1) {
                    current[1] = 0;
                } else {
                    current[1] = number;
                }
                viewModel.setRange(current);
            }
        }, getString(R.string.notification_set_upper_limit));
    }

    @OnClick(R.id.layout_lower)
    void lower() {
        showPopupWindow(rangeLower[0] - 1, rangeLower[1], currentValue[0], new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (value == rangeLower[0] - 1) {
                    return getString(R.string.notification_no_reminder);
                } else if (value == standard[0]) {
                    return value + getString(R.string.notification_standard);
                }
                return value + "";
            }
        }, new Select() {
            @Override
            public void onSelect(int number) {
                int[] current = viewModel.getCurrentRange();
                if (number == rangeLower[0] - 1) {
                    current[0] = 0;
                } else {
                    current[0] = number;
                }
                viewModel.setRange(current);
            }
        }, getString(R.string.notification_set_lower_limit));
    }

    private void showPopupWindow(int min, int max, int current, NumberPicker.Formatter formatter, final Select callback,
                                 String title) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.popup_window_select_number, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        final MaterialNumberPicker numberPicker = view.findViewById(R.id.np);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        if (current == 0) {
            numberPicker.setValue(min);
        } else {
            numberPicker.setValue(current);
        }
        numberPicker.setFormatter(formatter);
        numberPicker.setWrapSelectorWheel(false);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSelect(numberPicker.getValue());
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popup_window);

        upper.setEnabled(false);
        lower.setEnabled(false);

        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
                handler.postDelayed(popupDismissRunnable, 100);
            }
        });
        popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private Runnable popupDismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (upper != null) {
                upper.setEnabled(true);
                lower.setEnabled(true);
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(popupDismissRunnable);
        super.onDestroy();
    }

    private interface Select {
        void onSelect(int number);
    }

    /**
     * 设置屏幕变暗
     *
     * @param bgAlpha 1 最亮 0 最暗
     */
    @Override
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

}
