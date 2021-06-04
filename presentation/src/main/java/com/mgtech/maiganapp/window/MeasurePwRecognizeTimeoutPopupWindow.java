package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.maiganapp.R;

import java.util.Locale;

/**
 * Created by zhaixiang on 2018/1/5.
 * 设置手环显示页面
 */

public class MeasurePwRecognizeTimeoutPopupWindow extends PopupWindow {

    public interface Callback {
        void measureAgain();
        void goToGuide();
        void neverMind();
    }

    public MeasurePwRecognizeTimeoutPopupWindow(final Context context, final Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_measure_pw_recognize_timeout, null);
        view.findViewById(R.id.tv_measure_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.measureAgain();
                dismiss();
            }
        });
        view.findViewById(R.id.tv_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.goToGuide();
                dismiss();
            }
        });
        view.findViewById(R.id.tv_never_mind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.neverMind();
                dismiss();
            }
        });
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

}
