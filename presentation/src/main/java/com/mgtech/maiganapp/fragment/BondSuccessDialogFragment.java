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
public class BondSuccessDialogFragment extends AppCompatDialogFragment {
    private Callback callback;

    public interface Callback{
        void goToAdjust(boolean isToAdjust);
    }

    public static BondSuccessDialogFragment getInstance() {
        return new BondSuccessDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_bond_success, null);
        ButterKnife.bind(this, view);
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = inflater.inflate(R.layout.fragment_bond_success, container,false);
//        ButterKnife.bind(this, view);
//        setCancelable(false);
//        return view;
//    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback){
            this.callback = (Callback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
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

    @OnClick(R.id.btn_bond)
    void commit() {
        if (callback != null){
            callback.goToAdjust(true);
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        if (callback != null){
            callback.goToAdjust(false);
        }
        dismiss();
    }


}
