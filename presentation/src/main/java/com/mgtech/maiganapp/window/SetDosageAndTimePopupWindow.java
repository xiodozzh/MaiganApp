package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mgtech.maiganapp.R;

import java.util.Calendar;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;


/**
 * @author zhaixiang
 * 设置手环显示页面
 */

public class SetDosageAndTimePopupWindow extends PopupWindow {
    private MaterialNumberPicker npDigit;
    private MaterialNumberPicker npFraction;
    private MaterialNumberPicker npUnit;

//    private Group dosgeGroup;
//    private Group timeGroup;

    private MaterialNumberPicker npHour;
    private MaterialNumberPicker npMinute;

    private TextView tvTime;
    private TextView tvDosage;
    private TextView tvTimeDivider;

    private static final String[] fraction = {".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9"};
    private String[] units;

    public interface Callback {
        void submit(float data, int unitIndex, long time);
    }

    public SetDosageAndTimePopupWindow(final Context context, final Callback callback, float dosageValue, int dosageUnitIndex, long time) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_set_dosage_and_time, null);
        units = context.getResources().getStringArray(R.array.medicine_dosage_unit);
        tvDosage = view.findViewById(R.id.btn_dosage);
        tvTimeDivider = view.findViewById(R.id.tv_time_divider);
        tvTime = view.findViewById(R.id.btn_time);
        tvTime.setSelected(true);
//        dosgeGroup = view.findViewById(R.id.group_dosage);
//        timeGroup = view.findViewById(R.id.group_time);
//        dosgeGroup.setVisibility(View.GONE);
//        timeGroup.setVisibility(View.VISIBLE);
        tvDosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timeGroup.setVisibility(View.GONE);
//                dosgeGroup.setVisibility(View.VISIBLE);
                showDosage();
                tvDosage.setSelected(true);
                tvTime.setSelected(false);
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timeGroup.setVisibility(View.VISIBLE);
//                dosgeGroup.setVisibility(View.GONE);
                showTime();
                tvDosage.setSelected(false);
                tvTime.setSelected(true);
            }
        });
        npDigit = view.findViewById(R.id.np_digit);
        npFraction = view.findViewById(R.id.np_fraction);
        npUnit = view.findViewById(R.id.np_unit);
        npHour = view.findViewById(R.id.np_hour);
        npMinute = view.findViewById(R.id.np_minute);

        initNumberPicker(context, dosageValue, dosageUnitIndex);
        initTimePicker(context, time);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = npDigit.getValue() + npFraction.getValue() / 10f;
                int unit = npUnit.getValue();
                int hour = npHour.getValue();
                int minute = npMinute.getValue() * 5;
                if (value == 0) {
                    Toast.makeText(context, context.getString(R.string.medication_dosage_cannot_zero), Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                callback.submit(value, unit, calendar.getTimeInMillis());
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showTime();

        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initNumberPicker(Context context, float value, int unit) {
        npDigit.setMaxValue(200);
        npDigit.setMinValue(0);
        npDigit.setWrapSelectorWheel(false);

        npFraction.setMaxValue(9);
        npFraction.setMinValue(0);
        npFraction.setDisplayedValues(fraction);
        npFraction.setWrapSelectorWheel(false);

        npUnit.setMinValue(0);
        npUnit.setMaxValue(units.length - 1);
        npUnit.setDisplayedValues(units);
        npUnit.setWrapSelectorWheel(false);

        int separatorColor = ContextCompat.getColor(context, R.color.grey200);
        npDigit.setSeparatorColor(separatorColor);
        npUnit.setSeparatorColor(separatorColor);
        npFraction.setSeparatorColor(separatorColor);

        int digit = (int) value;
        int fraction = ((int) (value * 10)) % 10;

        npDigit.setValue(digit);
        npFraction.setValue(fraction);
        npUnit.setValue(unit);
    }

    private void initTimePicker(Context context, long time) {
        String[] hourArray = new String[24];
        String[] minArray = new String[12];
        for (int i = 0; i < hourArray.length; i++) {
            if (i < 10) {
                hourArray[i] = "0" + i;
            } else {
                hourArray[i] = String.valueOf(i);
            }
        }
        int min;
        for (int i = 0; i < minArray.length; i++) {
            min = i *5;
            if (min < 10) {
                minArray[i] = "0" + min;
            } else {
                minArray[i] = String.valueOf(min);
            }
        }

        npHour.setMaxValue(23);
        npHour.setMinValue(0);
        npHour.setDisplayedValues(hourArray);
        npHour.setWrapSelectorWheel(true);

        npMinute.setMaxValue(11);
        npMinute.setMinValue(0);
        npMinute.setDisplayedValues(minArray);
        npMinute.setWrapSelectorWheel(true);


        int separatorColor = ContextCompat.getColor(context, R.color.grey200);
        npHour.setSeparatorColor(separatorColor);
        npMinute.setSeparatorColor(separatorColor);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        npHour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        npMinute.setValue(calendar.get(Calendar.MINUTE)/5);
    }

    private void showDosage(){
        npDigit.setVisibility(View.VISIBLE);
        npFraction.setVisibility(View.VISIBLE);
        npUnit.setVisibility(View.VISIBLE);

        tvTimeDivider.setVisibility(View.GONE);
        npHour.setVisibility(View.GONE);
        npMinute.setVisibility(View.GONE);
    }

    private void showTime(){
        npDigit.setVisibility(View.GONE);
        npFraction.setVisibility(View.GONE);
        npUnit.setVisibility(View.GONE);

        tvTimeDivider.setVisibility(View.VISIBLE);
        npHour.setVisibility(View.VISIBLE);
        npMinute.setVisibility(View.VISIBLE);
    }
}
