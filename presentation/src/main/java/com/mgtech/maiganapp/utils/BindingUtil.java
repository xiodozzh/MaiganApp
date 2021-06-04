package com.mgtech.maiganapp.utils;

import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by zhaixiang on 2017/4/5.
 * 绑定帮助类
 */

public class BindingUtil {
    public static boolean isEmpty(String s){
        return s.isEmpty();
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }
}
