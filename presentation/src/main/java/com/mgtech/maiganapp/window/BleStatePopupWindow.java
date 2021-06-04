package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.databinding.Observable;

import com.bumptech.glide.Glide;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.BluetoothStateViewModel;

/**
 * Created by zhaixiang on 2018/1/5.
 * 设置手环显示页面
 */

public class BleStatePopupWindow extends PopupWindow {
    private View layoutBleOpen;
    private View layoutBleBind;
    private View layoutScan;
    private View layoutReason;
//    private BluetoothStateViewModel model;

    public BleStatePopupWindow(final Context context,final BluetoothStateViewModel model) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_ble_state, null);
        layoutBleOpen = view.findViewById(R.id.layout_ble_open);
        layoutBleBind = view.findViewById(R.id.layout_bind);
        layoutScan = view.findViewById(R.id.layout_scan);
        layoutReason = view.findViewById(R.id.layout_reason);
        model.showOpenBleLayout.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refresh(model);
            }
        });
        model.showScanLayout.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refresh(model);
            }
        });
        model.showBondLayout.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refresh(model);
            }
        });
//        model.showBondLayout.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                refresh(model);
//            }
//        });
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

    private  void refresh(BluetoothStateViewModel model){
        layoutBleOpen.setVisibility(model.showOpenBleLayout.get()?View.VISIBLE:View.GONE);
        layoutBleBind.setVisibility((!model.showOpenBleLayout.get()&& model.showBondLayout.get())?View.VISIBLE:View.GONE);
        layoutScan.setVisibility((!model.showOpenBleLayout.get()&& model.showScanLayout.get())?View.VISIBLE:View.GONE);
        layoutReason.setVisibility((model.showScanLayout.get())?View.VISIBLE:View.GONE);
    }

}
