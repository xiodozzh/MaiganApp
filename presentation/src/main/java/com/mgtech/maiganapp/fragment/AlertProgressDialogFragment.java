package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

public class AlertProgressDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_progress_dialog, null);
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {
        try {
            int width = ViewUtils.dp2px(150);
            int height = ViewUtils.dp2px(150);
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
