package com.mgtech.maiganapp.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.Observable;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.BraceletFindViewModel;

import butterknife.Bind;
import butterknife.OnClick;

public class BraceletFindActivity extends BleActivity<BraceletFindViewModel> {
    @Bind(R.id.iv_rotate)
    ImageView ivRotate;
    private ObjectAnimator animator;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BraceletFindActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        viewModel.finding.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getRotateAnimator().start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLinkState();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_find)
    void find(){
        viewModel.startFind();
    }

    private ObjectAnimator getRotateAnimator(){
        if (animator == null){
            animator = ObjectAnimator.ofFloat(ivRotate,"rotation",0,360 * 3);
            animator.setDuration(BraceletFindViewModel.SEARCHING_TIME);
        }
        return animator;
    }

    @OnClick(R.id.btn_open_bluetooth)
    void openBluetooth() {
        if (!isBleOn()) {
            openBle();
        }
    }

    @Override
    protected void openBleFail() {
        showOpenBleFailDialog();
    }

    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bracelet_find;
    }
}
