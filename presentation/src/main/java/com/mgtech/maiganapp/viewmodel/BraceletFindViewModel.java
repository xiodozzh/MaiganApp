package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.LookBandData;
import com.mgtech.blelib.entity.LookForBraceletEvent;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

public class BraceletFindViewModel extends BaseBleViewModel {
    public static final long SEARCHING_TIME = 4000;
    public final ObservableBoolean bleClosed = new ObservableBoolean(false);
    public final ObservableBoolean finding = new ObservableBoolean(false);
    public final ObservableBoolean enableFind = new ObservableBoolean(true);
    public final ObservableField<String> btnText = new ObservableField<>("");
    private IBraceletInfoManager manager;
    private CountDownTimer timer;

    public BraceletFindViewModel(@NonNull Application application) {
        super(application);
        this.manager = new BraceletInfoManagerBuilder(application).create();
        this.btnText.set(application.getString(R.string.bracelet_find_start));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null){
            timer.cancel();
        }
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

    public void startFind() {
        bleRequest.bluetoothFindBracelet(new LookBandData(LookBandData.SCREEN_ON, LookBandData.VIBRATE_ON));
    }

    private void launchTimeOut(){
        enableFind.set(false);
        btnText.set(getApplication().getString(R.string.bracelet_find_finding));
        if (timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(SEARCHING_TIME,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                enableFind.set(true);
                btnText.set(getApplication().getString(R.string.bracelet_find_start));
            }
        };
        timer.start();
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (!isConnected) {
            linkIfAllowed();
        }
    }

    @Override
    protected void onStateChange(int state) {
        if (state == BleStateEvent.STATE_OFF) {
            bleClosed.set(!bleClosed.get());
            stateViewModel.bluetoothClose();
        } else if (state == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                stateViewModel.scanning();
                checkBleState();
            } else {
                stateViewModel.unbind();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new FindResponseCallback();
    }

    class FindResponseCallback extends DefaultResponseCallback{

        @Override
        public void findResult(int code) {
            if (code == LookForBraceletEvent.NONE) {
                finding.set(!finding.get());
                launchTimeOut();
            }
        }
    }
}
