package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;

import butterknife.OnClick;


/**
 * 选择图片来源
 */

public class GuideHomePopupWindow extends PopupWindow {
    private View[] layoutsGuide;

    public interface Callback {
    }

    public GuideHomePopupWindow(final Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_guide_home, null);
        layoutsGuide = new View[3];
        layoutsGuide[0] = view.findViewById(R.id.layout_guide_hr);
        layoutsGuide[1] = view.findViewById(R.id.layout_guide_bp);
        layoutsGuide[2] = view.findViewById(R.id.layout_guide_measure);
        view.findViewById(R.id.iv_skip_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipGuide(v);
            }
        });
        view.findViewById(R.id.iv_skip_bp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipGuide(v);
            }
        });
        view.findViewById(R.id.iv_skip_measure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipGuide(v);
            }
        });
        showGuide();
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
//        this.setFocusable(true);
//        //设置SelectPicPopupWindow弹出窗体动画效果
////        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void showGuide() {
        if (!SaveUtils.doesGuideCheckHrWatched()) {
            showGuideIndex(0);
        } else if (!SaveUtils.doesGuideCheckBpWatched()) {
            showGuideIndex(1);
        } else if (!SaveUtils.doesGuideMeasureWatched()) {
            showGuideIndex(2);
        } else {
            dismiss();
//            showGuideIndex(-1);
        }
    }

    private void showGuideIndex(int index) {
        for (int i = 0; i < layoutsGuide.length; i++) {
            if (index == i) {
                layoutsGuide[i].setVisibility(View.VISIBLE);
            } else {
                layoutsGuide[i].setVisibility(View.GONE);
            }
        }
    }

    private void skipGuide(View view) {
        switch (view.getId()) {
            case R.id.iv_skip_hr:
                SaveUtils.guideCheckHr();
                break;
            case R.id.iv_skip_bp:
                SaveUtils.guideCheckBp();
                break;
            case R.id.iv_skip_measure:
                SaveUtils.guideMeasure();
                break;
            default:
        }
        showGuide();
    }

}
