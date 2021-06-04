package com.mgtech.maiganapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

@BindingMethods({@BindingMethod(type = AppCompatTextView.class, attribute = "toast",method = "showToast")})
public class ToastView extends AppCompatTextView {
    public ToastView(Context context) {
        super(context);
    }

    public ToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showToast(String toast){
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
