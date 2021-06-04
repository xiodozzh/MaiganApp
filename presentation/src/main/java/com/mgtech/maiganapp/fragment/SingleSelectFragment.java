package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * @date 2016/10/18
 * 展示数据
 */

public class SingleSelectFragment extends AppCompatDialogFragment {
    @Bind(R.id.rg)
    RadioGroup radioGroup;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private OnSelectChangedListener listener;
    public static final String PARA_TITLE = "text";
    public static final String PARA_DEFAULT_VALUE = "default";
    public static final String PARA_ITEMS = "items";

    public static SingleSelectFragment getInstance() {
        return new SingleSelectFragment();
    }

    public SingleSelectFragment() {
    }

    public void setListener(OnSelectChangedListener listener) {
        this.listener = listener;
    }

    public interface OnSelectChangedListener {
        void onSelectChanged(int position);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_single_select, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        tvTitle.setText(bundle.getString(PARA_TITLE, ""));
        String[] strings = bundle.getStringArray(PARA_ITEMS);
        if (strings != null && strings.length != 0) {
            for (String text : strings) {
                RadioButton rb = (RadioButton) getActivity().getLayoutInflater().inflate(R.layout.radio_button_fragment, null);
                rb.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        getActivity().getResources().getDimensionPixelSize(R.dimen.normal_height)));
                rb.setText(text);
                radioGroup.addView(rb);
            }
        }
        int defaultValue = bundle.getInt(PARA_DEFAULT_VALUE);
        if (defaultValue < 0 || (strings != null && defaultValue >= strings.length)) {
            defaultValue = 0;
        }
        ((RadioButton) radioGroup.getChildAt(defaultValue)).setChecked(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_commit)
    void commit() {
        if (listener != null) {
            int index = 0;
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (((RadioButton) radioGroup.getChildAt(i)).isChecked()) {
                    index = i;
                    break;
                }
            }
            listener.onSelectChanged(index);
        }
        dismiss();
    }

    @Override
    public void onResume() {
        try {
            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
            Window window = getDialog().getWindow();
            if (window != null) {
                ViewGroup.LayoutParams params = window.getAttributes();
                params.width = width;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setAttributes((android.view.WindowManager.LayoutParams) params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @OnClick(R.id.btn_cancel)
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
