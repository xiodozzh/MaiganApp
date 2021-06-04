package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.mgtech.maiganapp.R;

import java.util.Objects;

/**
 * Created by zhaixiang on 2017/8/11.
 * 删除
 */
public class ServiceUnavailableDialogFragment extends AppCompatDialogFragment {
    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public interface Callback{
        void check();
    }

    public static ServiceUnavailableDialogFragment getInstance() {
        return new ServiceUnavailableDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_service_unavailable, null);
        view.findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getActivity() instanceof Callback){
//                    ((Callback)getActivity()).check();
//                }
                if (callback != null){
                    callback.check();
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
