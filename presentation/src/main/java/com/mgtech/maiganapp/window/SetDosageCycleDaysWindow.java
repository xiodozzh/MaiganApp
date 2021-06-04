package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.mgtech.maiganapp.R;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;


/**
 * @author zhaixiang
 * 设置手环显示页面
 */

public class SetDosageCycleDaysWindow extends PopupWindow {
    private MaterialNumberPicker npDays;


    public interface Callback {
        void submit(int dayNumber);
    }

    public SetDosageCycleDaysWindow(final Context context, final Callback callback,int day) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_set_dosage_cycle_days, null);
        npDays = view.findViewById(R.id.np_day);

        initNumberPicker(context, day);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = npDays.getValue();
                callback.submit(value);
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

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initNumberPicker(Context context, int value) {
        npDays.setMaxValue(30);
        npDays.setMinValue(1);
        npDays.setWrapSelectorWheel(false);


        int separatorColor = ContextCompat.getColor(context, R.color.grey200);
        npDays.setSeparatorColor(separatorColor);

        npDays.setValue(value);
    }
}
