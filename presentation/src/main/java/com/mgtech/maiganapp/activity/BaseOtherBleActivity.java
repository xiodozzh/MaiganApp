package com.mgtech.maiganapp.activity;

import android.util.Log;

import com.mgtech.maiganapp.viewmodel.BaseOtherBleViewModel;

public abstract class BaseOtherBleActivity<T extends BaseOtherBleViewModel> extends BleActivity<T> {

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLinkState();
    }


    @Override
    protected void openBleFail() {
    }

    @Override
    protected void openBleSuccess() {
    }

}
