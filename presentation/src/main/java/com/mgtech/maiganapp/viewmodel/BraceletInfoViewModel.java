package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.os.Handler;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.blelib.utils.Utils;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.net.request.UnbindBraceletRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.DeviceUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.service.NetJobService;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

/**
 *
 * @author zhaixiang
 * @date 2018/1/3
 * 手环信息
 */

public class BraceletInfoViewModel extends BaseBleViewModel {
    private static final long WAIT_TIME = 500;
    private Application context;
    public final ObservableField<String> power = new ObservableField<>("");
    public final ObservableField<String> mac = new ObservableField<>("");
    public final ObservableField<String> version = new ObservableField<>("");
    public final ObservableField<String> reminderNumber = new ObservableField<>("");
    public final ObservableBoolean braceletHasNewVersion = new ObservableBoolean(false);
    public final ObservableBoolean braceletHasNewVersionDebug = new ObservableBoolean(false);
    public final ObservableBoolean braceletNotPair = new ObservableBoolean(false);
    public final ObservableBoolean showDebug = new ObservableBoolean(false);
    public final ObservableBoolean bleClosed = new ObservableBoolean(false);
    public final ObservableBoolean showFind = new ObservableBoolean(false);
    public final ObservableBoolean unPairFail = new ObservableBoolean(false);
    public final ObservableBoolean unPairSuccess = new ObservableBoolean(false);

    private static Handler handler = new Handler();

    private IBraceletInfoManager manager;
    private DisplayPage saveConfig;
    private DeviceUseCase deviceUseCase;
    private boolean clickUnPair = false;

    public BraceletInfoViewModel(Application context) {
        super(context);
        this.context = context;
        this.deviceUseCase = ((MyApplication)context).getDeviceUseCase();
        this.manager = new BraceletInfoManagerBuilder(context).create();
        this.showDebug.set(((MyApplication)context).isDebug());
        Logger.i(Arrays.toString(manager.getDeviceProtocolVersion()));
        this.showFind.set(Utils.doesFirmwareHaveFindBraceletFunc(manager.getDeviceProtocolVersion()));
    }

    public void setReminderNumber() {
        List<AlertReminder> reminders =  manager.getReminderData().getReminders();
        int n = 0;
        for (AlertReminder reminder : reminders) {
            if (reminder.isReminderEnable()){
                n++;
            }
        }
        if (n == 0) {
            reminderNumber.set(context.getString(R.string.bracelet_info_alert_not_open));
        } else {
            reminderNumber.set(String.format(Locale.getDefault(), context.getString(R.string.bracelet_info_alert_open_n_reminders), n));
        }
    }

    @Override
    protected void onCleared() {
        deviceUseCase.unSubscribe();
        handler.removeCallbacks(unPairRunnable);
        super.onCleared();
    }

    /**
     * 获取当前状态
     */
    public void getBraceletInfo() {
        boolean pairState = manager.isPaired();
        if (pairState) {
            braceletHasNewVersion.set(AppConfigNew.isBraceletHasNewVersion(context, manager.getDeviceFirmwareVersionString(),
                    manager.getDeviceName(),false));
            this.mac.set(manager.getAddress());
            if (manager.getPowerStatus() == IBraceletInfoManager.POWER_CHARGING){
                this.power.set(context.getString(R.string.bracelet_info_charging));
            }else {
                String power = manager.getPower() + "% ";
                this.power.set(power);
            }
            this.version.set(manager.getDeviceFirmwareVersionString());
            braceletHasNewVersionDebug.set(true);
        }
    }

    public void setConfig(DisplayPage config) {
        this.saveConfig = config;
        checkLink();
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_SLEEP:
                link();
                getBraceletInfo();
                if (saveConfig != null){
                    bleRequest.bluetoothSetDisplayConfig(saveConfig);
                }
                break;
            case BleStatusConstant.STATUS_DISCONNECTED:
                cancelDialog();
                scan();
                if (ViewUtils.isScreenOn(context) && manager.isPaired()) {
                    linkIfAllowed();
                }
                if (saveConfig != null) {
                    saveConfig = null;
                    showToast(context.getString(R.string.set_fail));
                }
                if (clickUnPair) {
                    unPairWithoutLink();
                }
                break;
            case BleStatusConstant.STATUS_HAVE_AUTO_SAMPLE_DATA:
                showDialog(context.getString(R.string.receiving_data), context.getString(R.string.please_wait));
                break;
            case BleStatusConstant.STATUS_HAVE_NO_AUTO_SAMPLE_DATA:
                cancelDialog();
                break;
            case BleStatusConstant.STATUS_PAIR_CODE_DIFFERENT:
                deviceNotBond();
                break;
//            case BleStatusConstant.STATUS_PARAM_SET_FAIL:
//                saveConfig = null;
//                showToast(context.getString(R.string.set_fail));
//                break;
            case BleStatusConstant.STATUS_SET_DISPLAY_PAGE_SUCCESS:
                if (saveConfig != null) {
                    SaveUtils.setParamDisplayUpdate(context, true);
                    showToast(getApplication().getString(R.string.set_success));
//                    context.startService(NetService.getCallingIntent(context, NetService.TYPE_SAVE_REMINDERS));
                    NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SAVE_REMINDERS));
                    saveConfig = null;
                }
                break;
            case BleStatusConstant.STATUS_UN_PAIR_SUCCESS:
                unPairWithoutLink();
                break;
            default:
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (!isConnected) {
            if (saveConfig != null) {
                showToast(context.getString(R.string.set_fail));
                saveConfig = null;
            }
            linkIfAllowed();
        } else {
            link();
            if (saveConfig != null) {
                bleRequest.bluetoothSetDisplayConfig(saveConfig);
            }
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
            }else{
                stateViewModel.unbind();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new DefaultResponseCallback();
    }

    /**
     * 已经连接
     */
    private void link() {
        stateViewModel.prepare();
    }

    /**
     * 手环未绑定
     */
    private void deviceNotBond() {
        this.braceletNotPair.set(!braceletNotPair.get());
    }

    /**
     * 正在扫描
     */
    private void scan() {
        stateViewModel.scanning();
    }

    /**
     * 检查是否已经连接
     */
    public void checkLink() {
        boolean isPaired = manager.isPaired();
        this.stateViewModel.showBondLayout.set(!isPaired);
        if (isPaired) {
            if (isBleOn()) {
                Logger.i("checkLink");
                bleRequest.bluetoothGetLinkStatus();
            }else{
                stateViewModel.bluetoothClose();
            }
        }else{
            stateViewModel.unbind();
        }
    }

    public void unbind() {
        UnbindBraceletRequestEntity entity = new UnbindBraceletRequestEntity(manager.getAddress(),
                SaveUtils.getUserId(getApplication()));
        this.deviceUseCase.unbind(entity, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                unPairFail.set(!unPairFail.get());
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity != null) {
                    if ((netResponseEntity.getCode() == 0 || netResponseEntity.getCode() == -2)) {
                        if (isLink.get()) {
                            unPair();
                        } else {
                            unPairWithoutLink();
                        }
                    } else {
                        unPairFail.set(!unPairFail.get());
                    }
                } else {
                    unPairFail.set(!unPairFail.get());
                }
            }
        });
    }

    /**
     * 不连接删除绑定信息
     */
    private void unPairWithoutLink() {
        handler.removeCallbacks(unPairRunnable);
        manager.deletePairInformation();
        this.bleRequest.bluetoothDisconnect();
        this.unPairSuccess.set(!unPairSuccess.get());
    }

    /**
     * 连接解绑
     */
    private void unPair() {
        this.bleRequest.bluetoothUnPair();
        handler.postDelayed(unPairRunnable, WAIT_TIME);
    }

    private Runnable unPairRunnable = new Runnable() {
        @Override
        public void run() {
            unPairWithoutLink();
        }
    };
}
