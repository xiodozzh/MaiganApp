package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.View;
import android.view.animation.Animation;

import com.mgtech.maiganapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaixiang on 2017/7/21.
 * 请求绑定
 */
public class BraceletSyncParametersFragment extends AppCompatDialogFragment {
    private Callback callback;

    public interface Callback{
        void sync(boolean sync);
    }

    public static BraceletSyncParametersFragment getInstance() {
        return new BraceletSyncParametersFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_bracelet_sync_parameters, null);
        ButterKnife.bind(this, view);
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback){
            callback = (Callback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick(R.id.btn_bond)
    void commit() {
        if (callback != null){
            callback.sync(true);
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        if (callback != null){
            callback.sync(false);
        }
        dismiss();
    }


}
