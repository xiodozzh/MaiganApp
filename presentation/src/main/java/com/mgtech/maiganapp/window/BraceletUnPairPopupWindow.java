package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mgtech.maiganapp.R;

/**
 * 绑定
 */

public class BraceletUnPairPopupWindow extends PopupWindow {
    private static final String TAG = "unPair";
    private LinearLayout layoutUnLink;
    private LinearLayout layoutLink;

    public interface Callback{
        void unPair(boolean notifyBracelet);
    }

    public BraceletUnPairPopupWindow(Context context, Callback callback,boolean isLink) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_bracelet_un_pair, null);
        init(context,view,callback,isLink);
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void init(final Context context,View view,final Callback callback,boolean isLink){
        layoutUnLink = view.findViewById(R.id.layout_un_link);
        layoutLink = view.findViewById(R.id.layout_link);
        view.findViewById(R.id.btn_cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_submit1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.unPair(false);
                }
            }
        });
        view.findViewById(R.id.btn_submit2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.unPair(true);
                }
            }
        });
        setLink(isLink);
    }

    public void setLink(boolean link){
        Log.i(TAG, "setLink: "+link);
        layoutUnLink.setVisibility(link?View.GONE:View.VISIBLE);
        layoutLink.setVisibility(link?View.VISIBLE:View.GONE);
    }

}
