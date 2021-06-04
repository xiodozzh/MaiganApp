package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.databinding.Observable;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.BluetoothStateViewModel;

/**
 * Created by zhaixiang on 2018/1/5.
 * 设置手环显示页面
 */

public class BleStatePairPopupWindow extends PopupWindow {
    public interface Callback{
        void onDismiss();
    }

    public BleStatePairPopupWindow(final Context context,final Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_ble_state_pair, null);
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null){
                    callback.onDismiss();
                }
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
