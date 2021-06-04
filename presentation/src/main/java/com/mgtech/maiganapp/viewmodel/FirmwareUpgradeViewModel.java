package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.blelib.biz.BleState;
import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareUpgradeData;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.maiganapp.BleApplication;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;

import rx.Subscriber;

/**
 * @author zhaixiang
 * @date 2017/3/29
 * 固件升级
 */

public class FirmwareUpgradeViewModel extends BaseBleViewModel {
    /**
     * 升级成功
     */
    public final ObservableBoolean upgradeFinish = new ObservableBoolean(false);
    public final ObservableBoolean upgrading = new ObservableBoolean(false);

    /**
     * 升级失败
     */
    public final ObservableBoolean upgradeFail = new ObservableBoolean(false);
    /**
     * 电量不足，重新尝试
     */
    public final ObservableBoolean powerLow = new ObservableBoolean(false);
    public final ObservableField<String> log = new ObservableField<>("");

    public MutableLiveData<FirmStateData> firmProgressLiveData = new MutableLiveData<>();
    private AppConfigUseCase appConfigUseCase;
    private byte[] firmwareContent;
    private String deviceName;
    public String failReason = "";
    private boolean isDebug = false;
    private static Handler mHandler = new Handler();
    private BleState bleState;
    private int[] firmwareVersion;
    private boolean upgradeSuccess;
    public boolean canGoBack = false;
    // TODO 使用liveData

    public FirmwareUpgradeViewModel(Application application) {
        super(application);
        bleState = ((BleApplication)getApplication()).getBleState();
    }

    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;

        this.upgradeFinish.set(false);
        this.appConfigUseCase = ((MyApplication)getApplication()).getAppConfigUseCase();
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(getApplication()).create();
        this.deviceName = manager.getDeviceName();

        this.log.set(AppConfigNew.getBraceletLog(getApplication(), deviceName,isDebug));
    }

    /**
     * 手环是否需要升级
     *
     * @return 升级条件：绑定、有新版本、配置信息为强制升级、电量20以上、已经连接、网络正常
     */
    private boolean doesDeviceNeedUpgrade() {
        return  isDebug || AppConfigNew.shouldAutoUpgradeBracelet(
                getApplication(),
                manager.getDeviceFirmwareVersionString(),
                manager.getDeviceName(), manager.getPower(),
                manager.getPowerStatus() == IBraceletInfoManager.POWER_CHARGING,isDebug);
    }

    private void getFirmwareUpgradeDirectly(FirmStateData data) {
        firmProgressLiveData.setValue(data);
        int totalPackage = data.getTotal();
//        Log.i(TAG, "getFirmwareUpgradeDirectly: "+ data.getProgress());
        switch (data.getStatus()) {
            case FirmStateData.START:
                upgradeSuccess = false;
                upgrading.set(true);
                canGoBack = false;
                break;
            case FirmStateData.CONTINUE:
                upgradeSuccess = totalPackage == data.getProgress();
                canGoBack = false;
                break;
            case FirmStateData.END:
                upgradeSuccess = true;
                canGoBack = true;
                upgrading.set(false);
                upgradeFinish.set(true);
                break;
            default:
                break;
        }
    }

    private int getPercent(int progress, int total){
        if (total == 0){
            return 0;
        }
        return Math.min(100,Math.round((float)progress/total * 100));
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_DISCONNECTED:
                upgrading.set(false);
                canGoBack = true;
                if (upgradeSuccess) {
                    bleRequest.bluetoothLink();
                    upgradeFinish.set(true);
                    if (firmwareVersion != null) {
                        IBraceletInfoManager manager =
                                new BraceletInfoManagerBuilder(getApplication()).create();
                        manager.saveDeviceFirmwareVersion(firmwareVersion);
                    }
                } else {
                    failReason = getApplication().getString(R.string.firmware_upgrade_fail_error_try_later);
                    upgradeFail.set(!upgradeFail.get());
                }
                break;
            case BleStatusConstant.STATUS_SLEEP:
                upgrading.set(false);
                if (!upgradeSuccess && doesDeviceNeedUpgrade()) {
                    upgrade(getFirmwareUpgrade());
                    canGoBack = false;
                }else{
                    canGoBack = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (isConnected) {
            if (bleState.isUpgrading()){
                return;
            }
            if (doesDeviceNeedUpgrade()) {
                upgrade(getFirmwareUpgrade());
            }else{
                upgradeSuccess = true;
                upgradeFinish.set(true);
            }
        } else {
            bleRequest.bluetoothLink();
        }
    }

    @Override
    protected void onStateChange(int bleState) {
        if (bleState == BleStateEvent.STATE_OFF) {
            bleRequest.bluetoothDisconnect();
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new FirmwareResponseCallback();
    }


    class FirmwareResponseCallback extends DefaultResponseCallback{
        @Override
        protected void onFirmwareUpgrade(FirmStateData data) {
            getFirmwareUpgradeDirectly(data);
        }
    }

    /**
     * 检查蓝牙状态，如果已经升级成功，则直接
     */
    public void checkState(){
//        if (bleState.isUpgradeSuccess()){
//            upgradeFinish.set(true);
//        }else{
//            checkLink();
//        }
        checkLink();
    }

    private void checkLink() {
        bleRequest.bluetoothGetLinkStatus();
    }

    private Runnable getFirmwareTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            cancelDialog();
            failReason = getApplication().getString(R.string.network_error);
            upgradeFail.set(!upgradeFail.get());
        }
    };

    /**
     * 获取固件文件
     */
    public void getFirmwareFile() {
        String url = AppConfigNew.getBraceletUrl(getApplication(), deviceName,isDebug);
        Log.i(TAG, "getFirmwareFile: "+ url);
        mHandler.postDelayed(getFirmwareTimeOutRunnable,5000);
        if (TextUtils.isEmpty(url)) {
            failReason = getApplication().getString(R.string.network_error);
            upgradeFail.set(!upgradeFail.get());
            return;
        }
        canGoBack = false;
        showDialog(getApplication().getString(R.string.firmware_upgrade),getApplication().getString(R.string.please_wait));
        this.appConfigUseCase.getFirmwareFile(url, new Subscriber<byte[]>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mHandler.removeCallbacks(getFirmwareTimeOutRunnable);
                failReason = getApplication().getString(R.string.network_error_try_later);
                upgradeFail.set(!upgradeFail.get());
                canGoBack = true;
                cancelDialog();
            }

            @Override
            public void onNext(byte[] content) {
                mHandler.removeCallbacks(getFirmwareTimeOutRunnable);
                cancelDialog();
                if (content != null && content.length != 0) {
                    firmwareContent = content;
                    checkLink();
                } else {
                    failReason = getApplication().getString(R.string.network_error_try_later);
                    upgradeFail.set(!upgradeFail.get());
                }
            }
        });
    }

    /**
     * 获取固件信息
     *
     * @return 固件数据
     */
    private FirmwareUpgradeData getFirmwareUpgrade() {
        if (firmwareContent== null || firmwareContent.length == 0) {
            getFirmwareFile();
            return null;
        }
        FirmwareUpgradeData data = new FirmwareUpgradeData();
        data.setContent(firmwareContent);
        data.setVersion(AppConfigNew.getBraceletVersionBytes(getApplication(), deviceName,isDebug));
        data.setMirror(0);
        return data;
    }

    private void upgrade(FirmwareUpgradeData data) {
        if (data == null) {
            upgradeFail.set(!upgradeFail.get());
            return;
        }
        Log.e(TAG, "upgrade: " + data.toString());
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(getApplication()).create();
        if (manager.getPowerStatus() != IBraceletInfoManager.POWER_CHARGING && manager.getPower() < IBraceletInfoManager.POWER_CAN_UPGRADE) {
            this.powerLow.set(!powerLow.get());
            return;
        }
        bleRequest.bluetoothUpgradeFirmware(data);
        this.upgradeSuccess = false;
        this.firmwareVersion = data.getVersion();
    }

    public void cancelUpgrade() {
        bleRequest.bluetoothCancelUpgrade();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        appConfigUseCase.unSubscribe();
        mHandler.removeCallbacks(getFirmwareTimeOutRunnable);
    }
}
