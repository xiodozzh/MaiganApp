package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.mgtech.blelib.biz.AbstractBleResponseCallback;
import com.mgtech.blelib.biz.BleRequest;
import com.mgtech.blelib.biz.BleResponse;
import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.BleApplication;
import com.mgtech.maiganapp.data.event.AppForegroundEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Subscription;

/**
 * @author zhaixiang
 */
public abstract class BaseBleViewModel extends BaseViewModel {
    public BluetoothStateViewModel stateViewModel;
    public final ObservableBoolean openBle = new ObservableBoolean(false);
    public final ObservableBoolean isLink = new ObservableBoolean(false);
    //    public final ObservableBoolean firmwareUpgrade = new ObservableBoolean(false);
    protected boolean background;

    /**
     * 是否工作无法退出
     */
    public final ObservableBoolean isWorking = new ObservableBoolean(false);

    protected BleRequest bleRequest;
    protected BleResponse bleResponse;
    protected IBraceletInfoManager manager;
    protected Subscription subscription;

    BaseBleViewModel(@NonNull Application application) {
        super(application);
        stateViewModel = new BluetoothStateViewModel();
        bleRequest = ((BleApplication) application).getRequest();
        bleResponse = ((BleApplication) application).getBleResponse();
        manager = new BraceletInfoManagerBuilder(getApplication()).create();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void registerBleCallback(){
        subscription = this.bleResponse.setCallback(getResponseCallback());
        Logger.i("registerBleCallback " + subscription);
    }

    public void unRegisterBleCallback(){
        Logger.e("registerBleCallback un " + subscription);
        this.bleResponse.removeCallback(subscription);
    }

    @Override
    protected void onCleared() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onCleared();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void bleState(BleStateEvent data) {
        onStateChange(data.getState());
    }


    protected abstract void onBluetoothStatus(int statusType);

    protected abstract void onLinkStatusReceived(boolean isConnected);

    protected abstract void onStateChange(int bleState);

    protected abstract DefaultResponseCallback getResponseCallback();

    /**
     * 设置是否在测量
     *
     * @param isWorking true测量，false不再测量
     */
    void setWorking(boolean isWorking) {
        this.isWorking.set(isWorking);
    }

    /**
     * 手环未绑定
     */
    private void deviceNotBond() {
        stateViewModel.showBondLayout.set(true);
        stateViewModel.showMask.set(true);
        stateViewModel.showScanLayout.set(false);
    }


    /**
     * 是否绑定手环
     *
     * @return true绑定
     */
    @Override
    public boolean isPaired() {
        return manager.isPaired();
    }

    /**
     * 正在扫描
     */
    private void scan() {
        stateViewModel.scanning();
    }

    /**
     * 已经连接
     */
    private void link() {
        Log.i(TAG, "已经连接");
        stateViewModel.prepare();
    }

    private void disLink() {
//        stateViewModel.showBondLayout.set(false);
//        stateViewModel.showMask.set(true);
//        stateViewModel.showScanLayout.set(true);
    }

    public void linkIfAllowed() {
        if (Utils.isBluetoothOpen() && !background) {
            Log.i(TAG, "linkIfAllowed: background " + background);
            bleRequest.bluetoothLink();
            scan();
        } else {
            openBle.set(!openBle.get());
        }
    }

    public boolean isBleOn() {
        return Utils.isBluetoothOpen();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getAppForegroundEvent(AppForegroundEvent event) {
        background = !event.isForeground();
        Log.i(TAG, "getAppForegroundEvent: " + background);
    }


    /**
     * 检查是否已经连接
     */
    public void checkLinkState() {
        boolean isPaired = manager.isPaired();
        this.stateViewModel.showBondLayout.set(!isPaired);
        if (isPaired) {
            if (isBleOn()) {
                bleRequest.bluetoothGetLinkStatus();
            } else {
                stateViewModel.bluetoothClose();
            }
        } else {
            stateViewModel.unbind();
        }
    }

    /**
     * 检查蓝牙状态
     */
    public void checkBleState() {
        bleRequest.bluetoothGetLinkStatus();
    }


    public class DefaultResponseCallback extends AbstractBleResponseCallback {
        @Override
        public void onBluetoothStatusReceived(int statusType) {
            Logger.e("onBluetoothStatus "+ statusType);
            switch (statusType) {
                case BleStatusConstant.STATUS_DISCONNECTED:
                    isLink.set(false);
                    setWorking(false);
                    disLink();
                    break;
                case BleStatusConstant.STATUS_SLEEP:
                    isLink.set(true);
                    link();
                    setWorking(false);
                    break;
                case BleStatusConstant.STATUS_PAIR_CODE_DIFFERENT:
                    deviceNotBond();
                    break;
                case BleStatusConstant.STATUS_CONNECTED:
                    stateViewModel.connecting();
                    break;
                default:
            }
            onBluetoothStatus(statusType);
        }

        @Override
        public void onLinkStatusReceived(boolean isConnected) {
            Log.i(TAG, "onLinkStatusReceived: " + isConnected);
            if (isConnected) {
                link();
            } else {
                setWorking(false);
                disLink();
            }
            isLink.set(isConnected);
            BaseBleViewModel.this.onLinkStatusReceived(isConnected);
        }
    }
}
