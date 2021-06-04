package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.mgtech.maiganapp.R;

import java.util.Objects;


/**
 * @author Jesse
 */
public class AuthorizedServiceDeleteDialogFragment extends AppCompatDialogFragment {
    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public interface Callback{
        void submit();
    }

    public static AuthorizedServiceDeleteDialogFragment getInstance() {
        return new AuthorizedServiceDeleteDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_authorized_service_delete_dialog, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.submit();
                }
                dismiss();
            }
        });
        setCancelable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }
}
