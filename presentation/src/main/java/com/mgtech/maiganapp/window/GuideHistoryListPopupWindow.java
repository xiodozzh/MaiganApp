package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;


/**
 * 选择图片来源
 */

public class GuideHistoryListPopupWindow extends PopupWindow {

    public interface Callback{
        void onDismiss();
    }

    public GuideHistoryListPopupWindow(final Context context,final Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_guide_history_list, null);
        view.findViewById(R.id.iv_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUtils.guideCheckHistoryList();
                callback.onDismiss();
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
