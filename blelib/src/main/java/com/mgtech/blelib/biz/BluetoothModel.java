//package com.mgtech.blelib.biz;
//
//import android.app.Application;
//import android.bluetooth.BluetoothAdapter;
//
//import org.greenrobot.eventbus.EventBus;
//
///**
// * Created by zhaixiang on 2017/3/25.
// * 网络业务逻辑父类
// */
//
//public class BluetoothModel {
//    public Application context;
//
//
//    public BluetoothModel(Application context, int index) {
//        this.context = context;
//    }
//
//    public BluetoothModel(Application context) {
//        this.context = context;
//    }
//
//
//    public void register() {
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    public void unRegister() {
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//    }
//
//
//
//
//
//    /**
//     * 蓝牙是否打开
//     *
//     * @return true 蓝牙开，false 蓝牙关
//     */
//    private boolean isBleOn() {
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
//    }
//
////    /**
////     * 发送连接指令
////     */
////    public final boolean bluetoothLink() {
////        if (isBleOn()) {
////            Logger.i("bluetoothLink");
////            EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_LINK));
////            return true;
////        }
////        return false;
////    }
////
////    public final void bluetoothRequestPair(BroadcastData data) {
////        if (isBleOn()) {
////            EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_REQUEST_PAIR, data));
////        }
////    }
////
////    /**
////     * 发送绑定指令
////     */
////    public final void bluetoothPair() {
////        if (isBleOn()) {
////            EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_PAIR));
////        }
////    }
////
////    public final void bluetoothPairMac(BroadcastData data) {
////        if (isBleOn()) {
////            EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_PAIR_MAC, data));
////        }
////    }
////
////    /**
////     * 发送连接指令
////     */
////    public final void bluetoothGetLinkStatus() {
////        if (isBleOn()) {
////            EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_GET_LINK_STATUS));
////        }
////    }
////
////
////    /**
////     * 发送断开指令
////     */
////    public final void bluetoothDisconnect() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_DISCONNECT));
////    }
////
////    /**
////     * 发送停止蓝牙扫描指令
////     */
////    public final void bluetoothStopScan() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_STOP_SCAN));
////    }
////
////    /**
////     * 发送测量指令
////     */
////    public final void bluetoothAdjust() {
////        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
////        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
////        manualMeasureOrder.setRateType(0);
////        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
////        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
////        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_AUTO);
////        manualMeasureOrder.setEnable(ManualMeasureNewOrder.recognizeEnable |
////                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
////        manualMeasureOrder.setRegionAvailable(false);
////        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_MEASURE_PW_AUTO, manualMeasureOrder));
////
////    }
////
////    /**
////     * 发送测量ECG指令
////     */
////    public final void bluetoothMeasureECG() {
////        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
////        manualMeasureOrder.setType(ManualMeasureNewOrder.ECG);
////        manualMeasureOrder.setRateType(0);
////        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.ECG_SAMPLE_RATE);
////        manualMeasureOrder.setEnable(ManualMeasureNewOrder.accEnable);
////        manualMeasureOrder.setRegionAvailable(false);
////        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.ECG_POINTS);
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_MEASURE_ECG, manualMeasureOrder));
////    }
////
////    /**
////     * 手环脉图识别采样
////     */
////    public final void bluetoothMeasureAutoPw() {
////        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
////        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
////        manualMeasureOrder.setRateType(0);
////        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
////        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
////        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_AUTO);
////        manualMeasureOrder.setEnable(ManualMeasureNewOrder.accEnable | ManualMeasureNewOrder.recognizeEnable |
////                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
////        manualMeasureOrder.setRegionAvailable(false);
////        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_MEASURE_PW_AUTO, manualMeasureOrder));
////    }
////
////    public final void bluetoothMeasurePwWithoutRecognize(boolean accEnable) {
////        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
////        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
////        manualMeasureOrder.setRateType(0);
////        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
////        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
////        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_PW_RAW_DATA);
////        manualMeasureOrder.setEnable((accEnable ? ManualMeasureNewOrder.accEnable : 0) |
////                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
////        manualMeasureOrder.setRegionAvailable(false);
////        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_MEASURE_PW_WITHOUT_RECOGNIZE, manualMeasureOrder));
////    }
////
////    public final void bluetoothMeasurePw(boolean accEnable) {
////        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
////        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
////        manualMeasureOrder.setRateType(0);
////        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
////        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
////        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_PW_RAW_DATA);
////        manualMeasureOrder.setEnable((accEnable ? ManualMeasureNewOrder.accEnable : 0) |
////                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
////        manualMeasureOrder.setRegionAvailable(false);
////        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_MEASURE_PW, manualMeasureOrder));
////    }
////
////    /**
////     * 发送停止采样指令
////     */
////    public final void bluetoothStopMeasure() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_STOP_MEASURE));
////    }
////
////    /**
////     * 设置自动采样是否开启
////     *
////     * @param enable 是否开启
////     */
////    public final void enableBluetoothAutoMeasure(boolean enable) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_ENABLE_AUTO_MEASURE, enable));
////    }
////
////    /**
////     * 开始固件升级
////     *
////     * @param data 固件信息
////     */
////    public final void bluetoothUpgradeFirmware(FirmwareUpgradeData data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_UPGRADE, data));
////    }
////
////    /**
////     * 取消固件升级
////     */
////    public final void bluetoothCancelUpgrade() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_CANCEL_UPGRADE));
////    }
////
////    /**
////     * 重置自动采样时间
////     */
////    public final void bluetoothResetAutoSampleTimeAndSyncBloodPressure() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_RESET_AUTO_SAMPLE_TIME_AND_SYCN_BP));
////
////    }
////
////    /**
////     * 解除绑定
////     */
////    public final void bluetoothUnPair() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_UN_PAIR));
////    }
////
////    /**
////     * 获取自动采样提醒时间
////     */
////    public final void bluetoothGetAutoReminder() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_GET_ALERT_REMINDERS));
////
////    }
////
////    /**
////     * 获取手环显示信息
////     */
////    public final void bluetoothGetBraceletDisplayConfig() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_GET_DISPLAY_PAGE));
////    }
////
////    /**
////     * 设置自动采样提醒时间
////     *
////     * @param data 提醒内容
////     */
////    public final void bluetoothSetAutoReminder(ReminderData data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SET_ALERT_REMINDER, data));
////    }
////
////    /**
////     * 设置自动采样提醒时间
////     *
////     * @param data 提醒内容
////     */
////    public final void bluetoothSetAutoReminderAndInteval(ReminderData data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SET_AUTO_MEASURE_INTERVAL, data));
////    }
////
////    /**
////     * 设置手环显示信息
////     *
////     * @param data 提醒内容
////     */
////    public final void bluetoothSetDisplayConfig(DisplayPage data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SET_DISPLAY_PAGE, data));
////    }
////
////    /**
////     * 设置手环身高体重
////     *
////     * @param data 提醒内容
////     */
////    public final void bluetoothSetHeightWeight(HeightWeightData data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SET_HEIGHT_WEIGHT, data));
////    }
////
////    /**
////     * 获取计步历史数据
////     */
////    public final void bluetoothGetStepHistoryData() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SYNC_STEP_HISTORY_DATA));
////    }
////
////    /**
////     * 获取计步数据(15min)
////     */
////    public final void bluetoothGetStepData() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SYNC_STEP_DATA));
////    }
////
////    /**
////     * 同步历史计步数据至手环
////     *
////     * @param data 历史计步数据
////     */
////    public final void bluetoothSetStepHistoryData(CurrentStepData data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_CALIBRATE_STEP_DATA, data));
////    }
////
////
////    /**
////     * 发送血压数据至手环
////     *
////     * @param syncBloodPressure 血压数据
////     */
////    public final void bluetoothSetBloodPressure(SyncBloodPressure syncBloodPressure) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_SET_BLOOD_PRESSURE, syncBloodPressure));
////    }
////
////    /**
////     * 发送设备校验结果
////     *
////     * @param checkDeviceData 校验成功后发
////     */
////    public final void bluetoothSendVerifyDeviceResult(CheckDeviceData checkDeviceData) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_VERIFY, checkDeviceData));
////    }
////
////    public final void bluetoothGetStoredManualData() {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_GET_STORED_MANUAL_DATA));
////    }
////
////    public final void bluetoothFindBracelet(LookBandData data) {
////        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_LOOK_FOR_BAND,data));
////    }
////    /**
////     * 蓝牙是否开着
////     *
////     * @return
////     */
////    public final boolean isBleOpen() {
////        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
////        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
////    }
//
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void status(StatusDataEvent statusDataEvent) {
////        if (callback != null) {
////            callback.onBluetoothStatusReceived(statusDataEvent.getStatus());
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void broadcast(BroadcastData broadcastData) {
////        if (pairCallback != null) {
////            pairCallback.onBroadcastReceived(broadcastData);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void manualPwData(ManualPwData data) {
//////        Logger.e("manualPwData: " + data );
////        if (measurePwCallback != null) {
////            measurePwCallback.onManualSampleDataReceived(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void measurePwComplete(SampleCompleteData data) {
////        if (data.isAuto()) {
////            return;
////        }
////        if (measurePwCallback != null) {
////            measurePwCallback.onManualSampleCompleteDataReceived(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void manualPwError(ManualPwError data) {
////        if (measurePwCallback != null) {
////            measurePwCallback.onManualErrorDataReceived(data);
////        }
////    }
////
//////    @Subscribe(threadMode = ThreadMode.MAIN)
//////    public void manualPwAuto(ManualPwAutoData data){
//////        if (measurePwCallback != null) {
//////            measurePwCallback.onManualErrorDataReceived(data.getErrorCode());
//////        }
//////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void manualStop(StopMeasureData data) {
////        if (measurePwCallback != null && data != null) {
////            measurePwCallback.onManualSampleStop();
////        }
////        if (measureEcgCallback != null && data != null) {
////            measureEcgCallback.onManualSampleStop();
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void manualEcgData(ManualEcgData data) {
////        if (measureEcgCallback != null) {
////            measureEcgCallback.onManualSampleDataReceived(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void manualEcgError(ManualEcgError data) {
////        if (measureEcgCallback != null) {
////            measureEcgCallback.onManualErrorDataReceived(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void warning(WarningData data) {
////        if (callback != null) {
////            callback.onWarningReceived(data.getCode());
////        }
////    }
////
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void displayPage(DisplayPage displayPage) {
////        if (displayPageCallback != null){
////            displayPageCallback.onDisplayPage(displayPage);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void alertReminder(ReminderData reminderData) {
////        if (remindersCallback != null){
////            remindersCallback.onAlertReminders(reminderData);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void firm(FirmStateData data) {
////        if (firmwareUpgradeCallback != null) {
////            firmwareUpgradeCallback.onFirmwareUpgrade(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void linkStatus(LinkStatusData data) {
////        if (callback != null) {
////            callback.onLinkStatusReceived(data.getStatus() == BleStatusConstant.LINK_CONNECTED);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void device(CheckDeviceData data) {
////        if (pairCallback != null) {
////            pairCallback.onIdAndMacReceived(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void phone(PhoneEvent data) {
////        if (phoneStatusCallback != null) {
////            phoneStatusCallback.onEventReceive(data);
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void blueState(BleStateEvent data) {
////        if (bleStateCallback != null) {
////            bleStateCallback.onStateChange(data.getState());
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void blueLinkStatus(LinkStatusEvent event) {
////        if (callback != null) {
////            callback.onLinkStatusReceived(event.isLink());
////        }
////    }
////
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void blueFindBracelet(LookForBraceletEvent event) {
////        if (callback != null) {
////            findForBracelet.findResult(event.getErrorCode());
////        }
////    }
//}
