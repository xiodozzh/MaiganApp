package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mgtech.maiganapp.R;


/**
 * 选择图片来源
 */

public class SelectPicturePopupWindow extends PopupWindow {

    public interface Callback {
        void selectFromPic();
        void selectFromCamera();
    }

    public SelectPicturePopupWindow(final Context context, final Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_select_picture, null);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_from_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectFromPic();
                dismiss();
            }
        });
        view.findViewById(R.id.tv_from_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectFromCamera();
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
}
