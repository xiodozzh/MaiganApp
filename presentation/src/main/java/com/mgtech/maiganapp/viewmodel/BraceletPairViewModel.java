package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.databinding.ObservableBoolean;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.blelib.utils.Utils;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.net.request.BindDeviceRequestEntity;
import com.mgtech.domain.entity.net.request.CheckBraceletRequestEntity;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.entity.net.response.CheckBraceletResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.domain.interactor.DeviceUseCase;
import com.mgtech.domain.utils.NetUtils;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.wrapper.BraceletConfigWrapper;
import com.mgtech.maiganapp.service.NetJobService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class BraceletPairViewModel extends BaseBleViewModel {
    public final ObservableBoolean pairError = new ObservableBoolean(false);
    public final ObservableBoolean checkFail = new ObservableBoolean(false);
    public final ObservableBoolean connecting = new ObservableBoolean(false);
    public final ObservableBoolean broadcastUpdate = new ObservableBoolean(false);
    /**
     * 显示升级对话框
     */
    public final ObservableBoolean goToUpgradeActivity = new ObservableBoolean(false);
    /**
     * 显示同步参数对话框
     */
    public final ObservableBoolean syncSuccess = new ObservableBoolean(false);

    /**
     * 显示连接页面
     */
    public final ObservableBoolean showConnectGuideGif = new ObservableBoolean(false);

    /**
     * 蓝牙绑定成功
     */
    public final ObservableBoolean blePairSuccess = new ObservableBoolean(false);

    /**
     * 网络绑定成功
     */
    public final ObservableBoolean netPairSuccess = new ObservableBoolean(false);
    public final ObservableBoolean netPairFail = new ObservableBoolean(false);
    public final ObservableBoolean bleClosed = new ObservableBoolean(false);

    private boolean isPairSuccess = false;
    private static final long SCAN_TIME = 10000;
    private static Handler mHandler = new Handler();
    private boolean isLeaving = false;
    private AppConfigUseCase appConfigUseCase;
    private IBraceletInfoManager manager;
    private DeviceUseCase useCase;
    public String errorText = "";
    private boolean isVerifyFail = false;
    private ReminderData netReminder;
    private DisplayPage updateDisplay;
    private boolean needUpdateReminder;
    private boolean needUpdateDisplay;
    public boolean hadReminders;
    private boolean bindError;

    private BroadcastData pairData;
    public List<BroadcastData> broadcastData;

    private boolean firstUpgrade = false;
    /**
     * 固件升级后无论升级是否成功都不再重复升级
     */
    private boolean upgradeDone;

    public BraceletPairViewModel(Application application) {
        super(application);
        this.manager = new BraceletInfoManagerBuilder(application).create();
        this.appConfigUseCase = ((MyApplication) application).getAppConfigUseCase();
        this.useCase = ((MyApplication) application).getDeviceUseCase();
        this.broadcastData = new ArrayList<>();
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        Log.i("PairViewModel", "bluetoothPairStatus: " + statusType);
        switch (statusType) {
            case BleStatusConstant.STATUS_LINK:
                connecting.set(false);
                if (!manager.isPaired()) {
                    showConnectGuideGif.set(!showConnectGuideGif.get());
                    bleRequest.bluetoothRequestPair(pairData);
                }
                mHandler.removeCallbacks(connectTimeOutRunnable);
                break;
            case BleStatusConstant.STATUS_SLEEP:
                Log.i(TAG, "请求连接: " + !manager.isPaired());
                if (bindError){
                    return;
                }
                connecting.set(false);
                if (manager.isPaired()) {
                    askUpgrade();
                }
                mHandler.removeCallbacks(connectTimeOutRunnable);
                break;
            case BleStatusConstant.STATUS_CONNECTED:
                if (manager.isPaired()) {
                    mHandler.postDelayed(connectTimeOutRunnable, 5000);
                }
                break;
            case BleStatusConstant.STATUS_UN_PAIR_SUCCESS:
                // TODO 解绑时间晚于STATUS_SLEEP情况未处理
                Log.i(TAG, "onBluetoothStatus: 解绑" );
                manager.deletePairInformation();
                showCheckFailDialog(getApplication().getString(R.string.bind_fail));
                break;
            case BleStatusConstant.STATUS_PAIR_SUCCESS:
                mHandler.removeCallbacks(stopPairRunnable);
                blePairSuccess.set(true);
                saveBindStatus(manager.getAddress(), manager.getEncryptKey(), manager.getEncryptVector(),
                        manager.getCodePair(), manager.getDeviceName(), manager.getDeviceFirmwareVersionString());
                break;
            case BleStatusConstant.STATUS_DISCONNECTED:
                Log.e(TAG, "disconnect " + isPairSuccess);
                mHandler.removeCallbacks(connectTimeOutRunnable);
                mHandler.removeCallbacks(stopPairRunnable);
                if (netPairFail.get()) {
                    manager.deletePairInformation();
                }
                if (netPairSuccess.get()) {
                    startScan();
                } else {
                    if (isVerifyFail) {
                        checkFail.set(!checkFail.get());
                    } else if (!isLeaving) {
                        if (isBleOn()) {
                            pairError.set(!pairError.get());
                        }
                    }
                }
                cancelDialog();
                break;
            case BleStatusConstant.STATUS_SET_HEIGHT_WEIGHT_SUCCESS:
                break;
            case BleStatusConstant.STATUS_SET_ALERT_REMINDERS_SUCCESS:
                needUpdateReminder = false;
                if (!needUpdateDisplay) {
                    syncSuccess.set(!syncSuccess.get());
                } else {
                    bleRequest.bluetoothSetDisplayConfig(updateDisplay);
                }
                break;
            case BleStatusConstant.STATUS_SET_DISPLAY_PAGE_SUCCESS:
                needUpdateDisplay = false;
                if (!needUpdateReminder) {
                    syncSuccess.set(!syncSuccess.get());
                }
                SaveUtils.setParamDisplayUpdate(getApplication(), true);
//                getApplication().startService(NetService.getCallingIntent(getApplication(), NetService.TYPE_SAVE_DISPLAY));
                NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SAVE_DISPLAY));

                break;
            default:
                break;
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        Log.i("Pair", "onLinkStatusReceived: " + isConnected);
        if (isConnected) {
            if (manager.isPaired()) {
                askUpgrade();
            } else {
                bleRequest.bluetoothDisconnect();
            }
        } else {
            if (manager.isPaired()) {
                connecting.set(true);
                bleRequest.linkIfAvailable();
            } else {
                bleRequest.pair();
            }
        }
    }

    @Override
    protected void onStateChange(int state) {
        if (state == BleStateEvent.STATE_OFF) {
            bleClosed.set(!bleClosed.get());
        } else if (state == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                startScan();
            } else {
                startPairScan();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new PairResponseCallback();
    }

    public void getLinkedStatus() {
//        bluetoothModel.bluetoothGetLinkStatus();
        bleRequest.bluetoothGetLinkStatus();
    }


    public void setUpgradeDone() {
        this.upgradeDone = true;
    }

    /**
     * 添加广播组件
     *
     * @param data 广播包
     */
    private void addBroadcastItem(BroadcastData data) {
        String address = data.getMacAddress();
        if (address == null) {
            return;
        }
        for (int i = 0; i < broadcastData.size(); i++) {
            BroadcastData item = broadcastData.get(i);
            if (address.equals(item.getMacAddress())) {
                item.setInitTime(data.getInitTime());
                if (data.getRandomNumber() != item.getRandomNumber() || data.getSignalLevel() != item.getSignalLevel()) {
                    Logger.e("重复的手环 " + item.getRandomNumber());
                    broadcastData.set(i, data);
                    broadcastUpdate.set(!broadcastUpdate.get());
                }
                return;
            }
        }
        broadcastData.add(data);
        broadcastUpdate.set(!broadcastUpdate.get());
        Log.e(TAG, "addBroadcastItem: " + data);
    }

    private Runnable connectTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            bleRequest.bluetoothDisconnect();
        }
    };

    /**
     * 绑定此设备
     *
     * @param broadcastData 广播
     */
    public void pair(BroadcastData broadcastData) {
        bleRequest.bluetoothPairMac(broadcastData);
        mHandler.removeCallbacks(stopPairRunnable);
        mHandler.postDelayed(stopPairRunnable, SCAN_TIME);
        pairData = broadcastData;
    }

    private void check(final CheckDeviceData data) {
        useCase.check(new CheckBraceletRequestEntity(data.getId(), data.getMac()),
                new Subscriber<NetResponseEntity<CheckBraceletResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showCheckFailDialog(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<CheckBraceletResponseEntity> netResponseEntity) {
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() != 0) {
                                showCheckFailDialog(getApplication().getString(R.string.network_error));
                            } else {
                                CheckBraceletResponseEntity entity = netResponseEntity.getData();
                                if (entity != null && entity.getIsLegal() == 1) {
                                    bleRequest.bluetoothSendVerifyDeviceResult(data);
                                } else {
                                    bleRequest.bluetoothDisconnect();
                                    showCheckFailDialog(getApplication().getString(R.string.activity_bracelet_pair_please_buy_the_correct_device));
                                }
                            }
                        } else {
                            showCheckFailDialog(getApplication().getString(R.string.network_error));
                        }
                    }
                });
    }

    /**
     * 校验错误
     *
     * @param errorText 错误原因提醒
     */
    private void showCheckFailDialog(String errorText) {
        this.errorText = errorText;
        isVerifyFail = true;
        mHandler.removeCallbacks(stopPairRunnable);
        bleRequest.bluetoothDisconnect();
    }

    /**
     * 开始绑定
     */
    public void startPairScan() {
        isVerifyFail = false;
        bleRequest.bluetoothPair();
    }

    public void startScan() {
        Log.i(TAG, "startScan: pair page scan ");
        broadcastData.clear();
        broadcastUpdate.set(!broadcastUpdate.get());
        bleRequest.bluetoothLink();
        connecting.set(true);
    }

    /**
     * 离开页面
     */
    public void leave() {
        isLeaving = true;
        stopScan();
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        if (isPairSuccess) {
            bleRequest.bluetoothStopScan();
        } else {
            bleRequest.bluetoothDisconnect();
        }
        mHandler.removeCallbacks(stopPairRunnable);
    }

    private Runnable stopPairRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: stopPairRunnable");
            bleRequest.bluetoothDisconnect();
            pairError.set(!pairError.get());
        }
    };

    private void askUpgrade() {
        Log.i(TAG, "检查是否升级");
        if (firstUpgrade || !upgradeDone && AppConfigNew.shouldAutoUpgradeBracelet(getApplication(), manager.getDeviceFirmwareVersionString(),
                manager.getDeviceName(), manager.getPower(), manager.getPowerStatus() == IBraceletInfoManager.POWER_CHARGING, false)) {
            firstUpgrade = false;
            goToUpgradeActivity.set(!goToUpgradeActivity.get());
        } else {
            if (Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion())) {
                if (!manager.getReminderData().isTimeZoneSet()) {
                    Logger.e("新版本");
                    Utils.updateReminder(netReminder);
                }
            } else {
                if (manager.getReminderData().isTimeZoneSet()) {
                    Logger.e("旧版本手环 新版本本地");
                    Utils.backReminder(netReminder);
                }
            }
            Logger.e(netReminder.toString());
            if (needUpdateReminder) {
                bleRequest.bluetoothSetAutoReminder(netReminder);
            } else {
                manager.setReminder(netReminder);
                manager.setDisplayPage(updateDisplay);
                syncSuccess.set(!syncSuccess.get());
            }
        }
    }

    /**
     * 将信息保存至服务器
     */
    private void saveBindStatus(String mac, byte[] key, byte[] vector, final int pairCode, String braceletName,
                                String firmwareVersion) {
        BindDeviceRequestEntity entity = new BindDeviceRequestEntity(SaveUtils.getUserId(getApplication()),
                pairCode, NetUtils.byteToHexString(key),
                NetUtils.byteToHexString(vector), mac, braceletName, firmwareVersion);
        useCase.bind(entity, new Subscriber<NetResponseEntity<BraceletConfigEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.bind_fail_please_check));
                netPairFail.set(true);
                bleRequest.bluetoothUnPair();
            }

            @Override
            public void onNext(NetResponseEntity<BraceletConfigEntity> netResponseEntity) {
                if (netResponseEntity.getCode() != 0) {
                    showToast(netResponseEntity.getMessage());
                    bindError = true;
                    bleRequest.bluetoothUnPair();
                } else {
                    bindError = false;
                    BraceletConfigEntity parameterEntity = netResponseEntity.getData();
                    if (parameterEntity != null) {
                        // 参数不为空
                        DisplayPage netParam5 = BraceletConfigWrapper
                                .getDisplayPageFromNet(parameterEntity);
                        if (netParam5.isSet()) {
                            updateDisplay = netParam5;
                            needUpdateDisplay = true;
                        } else {
                            updateDisplay = new DisplayPage();
                            needUpdateDisplay = false;
                        }
                        netReminder = BraceletConfigWrapper.getRemindersFromEntity(parameterEntity);
                        needUpdateReminder = true;
                        hadReminders = !netReminder.getReminders().isEmpty();
                        if (!hadReminders) {
                            netReminder.getReminders().clear();
                            netReminder.getReminders().add(getDefaultReminder());
                        }
                        Log.e(TAG, "need update: " + needUpdateReminder + needUpdateDisplay);
                    }
                    isPairSuccess = true;
                    netPairSuccess.set(true);
                    showToast(getApplication().getString(R.string.bind_success));
//                    getApplication().startService(NetService.getCallingIntent(getApplication(), NetService.TYPE_SET_BRACELET_INFO));
                    NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SET_BRACELET_INFO));
//                    askUpgrade();
                    bleRequest.bluetoothPairInit();
                }
            }
        });
    }

    private AlertReminder getDefaultReminder() {
        AlertReminder reminder = new AlertReminder();
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 0);
        Calendar calendar;
        if (manager.getReminderData().isTimeZoneSet()) {
            calendar = Calendar.getInstance();
        } else {
            calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        }
        calendar.setTimeInMillis(startTime.getTimeInMillis());
        reminder.setReminderEnable(true);
        reminder.setReminderStartHour(calendar.get(Calendar.HOUR_OF_DAY));
        reminder.setReminderStartMinute(calendar.get(Calendar.MINUTE));
        reminder.setReminderSpanTime(60 * 12);
        reminder.setReminderInterval(AlertReminder.DAILY_INTERVAL);
        reminder.setReminderWeek(AlertReminder.EVERYDAY);
        return reminder;
    }

    @Override
    protected void onCleared() {
        mHandler.removeCallbacks(connectTimeOutRunnable);
        mHandler.removeCallbacks(stopPairRunnable);
        appConfigUseCase.unSubscribe();
        super.onCleared();
    }

    class PairResponseCallback extends DefaultResponseCallback{
        @Override
        public void onIdAndMacReceived(CheckDeviceData data) {
            check(data);
        }

        @Override
        public void onBroadcastReceived(BroadcastData broadcastData) {
            addBroadcastItem(broadcastData);
        }

    }

}
