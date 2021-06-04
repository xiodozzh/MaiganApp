package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.View;
import android.view.animation.Animation;

import com.mgtech.maiganapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaixiang on 2017/8/11.
 * 删除
 */
public class DeleteDialogFragment extends AppCompatDialogFragment {
    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public interface Callback{
        void delete(boolean isDelete);
    }

    public static DeleteDialogFragment getInstance() {
        return new DeleteDialogFragment();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_delete, null);
        ButterKnife.bind(this, view);
        setCancelable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

//    @Override
//    public void onResume() {
//        try {
//            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//            int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//            Window window = getDialog().getWindow();
//            if (window != null) {
//                window.setLayout(width, height);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        super.onResume();
//    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick(R.id.btn_delete)
    void commit() {
        if (callback != null){
            callback.delete(true);
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        if (callback != null){
            callback.delete(false);
        }
        dismiss();
    }


}
