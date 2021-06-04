package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

public class SetDisplayTimePopupWindow extends PopupWindow {
    private RadioButton rb1;
    private RadioButton rb2;

    public interface Callback {
        void submit(DisplayPage isFullTime);
    }

    public SetDisplayTimePopupWindow(final Context context, final Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_set_bracelet_time_display_config, null);
        rb1 = view.findViewById(R.id.cb1);
        rb2 = view.findViewById(R.id.cb2);
        initCheckbox(context);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
                DisplayPage data = manager.getDisplayPage();
                data.setDatePageDisplay(rb2.isChecked() ? DisplayPage.DATE_COMPLEX : DisplayPage.DATE_SIMPLE);
                callback.submit(data);
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb2.setChecked(false);
                }
            }
        });
        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb1.setChecked(false);
                }
            }
        });
        view.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(true);
            }
        });
        view.findViewById(R.id.layout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb2.setChecked(true);
            }
        });
        ImageView img = (ImageView) view.findViewById(R.id.img_time2);
        if (!Locale.getDefault().getLanguage().equalsIgnoreCase(Locale.CHINESE.toString())){
            img.setImageResource(R.drawable.time2_en);
        }else{
            img.setImageResource(R.drawable.time2_ch);
        }
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

    private void initCheckbox(Context context) {
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        DisplayPage data =manager.getDisplayPage();
        rb1.setChecked(data.getDatePageDisplay() == DisplayPage.DATE_SIMPLE);
        rb2.setChecked(data.getDatePageDisplay() == DisplayPage.DATE_COMPLEX);
    }
}
