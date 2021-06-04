package com.mgtech.maiganapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mgtech.maiganapp.utils.ViewUtils;

public class MeasurePwRecognizeTimeoutDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        try {
            int width = ViewUtils.dp2px(150);
            int height = ViewUtils.dp2px(300);
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setLayout(width, height);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onResume();
    }
}
