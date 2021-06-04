package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.maiganapp.utils.ViewUtils;

public class BaseOtherBleViewModel extends BaseBleViewModel {

    public BaseOtherBleViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_DISCONNECTED:
                if (ViewUtils.isScreenOn(getApplication()) && manager.isPaired()) {
                    linkIfAllowed();
                }
                break;
            default:
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        Log.i("BaseOtherBleViewModel", "onLinkStatusReceived: ");
        if (!isConnected) {
            linkIfAllowed();
        }
    }

    @Override
    protected void onStateChange(int state) {
        if (state == BleStateEvent.STATE_OFF) {
            stateViewModel.bluetoothClose();
        } else if (state == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                stateViewModel.scanning();
                checkBleState();
            }else{
                stateViewModel.unbind();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new DefaultResponseCallback();
    }

}
