package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.mgtech.maiganapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaixiang on 2016/10/18.
 * 展示数据
 */

public class SetPwRemarkFragment extends AppCompatDialogFragment {

    public static final String CONTENT ="content";
    @Bind(R.id.et)
    EditText et;
    private OnTextChangeListener listener;

    public static SetPwRemarkFragment getInstance() {
        return new SetPwRemarkFragment();
    }

    public SetPwRemarkFragment() {
    }

    public void setListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    public interface OnTextChangeListener {
        void setRemark(String remark);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_set_pw_mark, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            et.setText(bundle.getString(CONTENT));
            et.setSelection(et.getText().length());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {
        try {
//            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//            int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//            Window window = getDialog().getWindow();
//            if (window != null) {
//                window.setLayout(width, height);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                if (et != null) {
                    InputMethodManager inManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 500);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_positive)
    void commit() {
        if (listener != null) {
            String s = et.getText().toString().trim();
            listener.setRemark(s);
        }
        dismiss();
    }

    @OnClick(R.id.btn_negative)
    void cancel() {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
