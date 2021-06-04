package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mgtech.maiganapp.R;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;


/**
 * Created by zhaixiang on 2018/1/5.
 * 设置手环显示页面
 */

public class SetMedicineIntervalPopupWindow extends PopupWindow {
    private MaterialNumberPicker npHour;
    private MaterialNumberPicker npMinute;
    private String[] hourArray;
    private String[] minuteArray;

    public interface Callback {
        void submit(int hour, int minute);
    }

    public SetMedicineIntervalPopupWindow(final Context context, final Callback callback, int hour, int minute) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_set_medicine_interval, null);
        String hourString = "小时";
        String minString = "分钟";
        hourArray = new String[24];
        for (int i = 0; i < 24; i++) {
            hourArray[i] = String.valueOf(i) + hourString;
        }
        minuteArray = new String[12];
        for (int i = 0; i < 12; i++) {
            minuteArray[i] = String.valueOf(i*5)+ minString;
        }


        npHour = (MaterialNumberPicker) view.findViewById(R.id.np_hour);
        npMinute = (MaterialNumberPicker) view.findViewById(R.id.np_minute);

        initNumberPicker(context,hour,minute);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.submit(npHour.getValue(),npMinute.getValue()*5);
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initNumberPicker(Context context,int hour,int minute) {
        npHour.setMaxValue(23);
        npHour.setMinValue(0);
        npHour.setWrapSelectorWheel(false);

        npMinute.setMaxValue(11);
        npMinute.setMinValue(0);
        npMinute.setWrapSelectorWheel(true);

        int separatorColor = ContextCompat.getColor(context,R.color.grey300);
        npHour.setSeparatorColor(separatorColor);
        npMinute.setSeparatorColor(separatorColor);

        npMinute.setDisplayedValues(minuteArray);
        npHour.setDisplayedValues(hourArray);

        npHour.setValue(hour);
        npMinute.setValue(minute / 5);
    }
}
