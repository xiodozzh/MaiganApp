package com.mgtech.blelib.biz;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.entity.BluetoothOrder;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmwareUpgradeData;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.LookBandData;
import com.mgtech.blelib.entity.ManualMeasureNewOrder;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.utils.Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

public class BleRequest {
    private IBizController bizController;
    private IBraceletInfoManager manager;

    public BleRequest(IBizController bizController, IBraceletInfoManager manager) {
        this.bizController = bizController;
        this.manager = manager;
    }

    /**
     * 发送连接指令
     */
    public void link() {
        if (!manager.isPaired() || !isBleOn()) {
            return;
        }
        bizController.link();
    }

    public void pair() {
        if (manager.isPaired() || !isBleOn()) {
            return;
        }
        bizController.pair();
    }

    /**
     * 发送断开指令
     */
    public void disconnect() {
        bizController.disconnect();
    }

    public void disconnectIfNotWorking(){
        bizController.disconnectIfNotWorking();
    }

    public void getOrder(BluetoothOrder order) {
        if (!Utils.isBluetoothOpen()) {
            // 蓝牙关闭，断开并停止扫描
            disconnect();
            return;
        }
        bizController.addOrder(order);
    }

    public void linkIfAvailable(){
        bizController.linkIfAvailable();
    }

    public void stopScan(){
        bizController.stopScan();
    }

    /**
     * 蓝牙是否打开
     *
     * @return true 蓝牙开，false 蓝牙关
     */
    public boolean isBleOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
    }

    /**
     * 发送连接指令
     */
    public final boolean bluetoothLink() {
        if (isBleOn()) {
            Logger.i("bluetoothLink");
            getOrder(new BluetoothOrder(BleConstant.CODE_LINK));
            return true;
        }
        return false;
    }

    public final void bluetoothRequestPair(BroadcastData data) {
        if (isBleOn()) {
            getOrder(new BluetoothOrder(BleConstant.CODE_REQUEST_PAIR, data));
        }
    }

    /**
     * 发送绑定指令
     */
    public final void bluetoothPair() {
        if (isBleOn()) {
            getOrder(new BluetoothOrder(BleConstant.CODE_PAIR));
        }
    }

    public final void bluetoothPairMac(BroadcastData data) {
        if (isBleOn()) {
            getOrder(new BluetoothOrder(BleConstant.CODE_PAIR_MAC, data));
        }
    }

    /**
     * 发送连接指令
     */
    public final void bluetoothGetLinkStatus() {
        if (isBleOn()) {
            getOrder(new BluetoothOrder(BleConstant.CODE_GET_LINK_STATUS));
        }
    }


    /**
     * 发送断开指令
     */
    public final void bluetoothDisconnect() {
        getOrder(new BluetoothOrder(BleConstant.CODE_DISCONNECT));
    }

    /**
     * 发送停止蓝牙扫描指令
     */
    public final void bluetoothStopScan() {
        getOrder(new BluetoothOrder(BleConstant.CODE_STOP_SCAN));
    }

    /**
     * 发送测量指令
     */
    public final void bluetoothAdjust() {
        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
        manualMeasureOrder.setRateType(0);
        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_AUTO);
        manualMeasureOrder.setEnable(ManualMeasureNewOrder.recognizeEnable |
                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
        manualMeasureOrder.setRegionAvailable(false);
        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
        getOrder(new BluetoothOrder(BleConstant.CODE_MEASURE_PW_AUTO, manualMeasureOrder));

    }

    /**
     * 发送测量ECG指令
     */
    public final void bluetoothMeasureECG() {
        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
        manualMeasureOrder.setType(ManualMeasureNewOrder.ECG);
        manualMeasureOrder.setRateType(0);
        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.ECG_SAMPLE_RATE);
        manualMeasureOrder.setEnable(ManualMeasureNewOrder.accEnable);
        manualMeasureOrder.setRegionAvailable(false);
        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.ECG_POINTS);
        getOrder(new BluetoothOrder(BleConstant.CODE_MEASURE_ECG, manualMeasureOrder));
    }

    /**
     * 手环脉图识别采样
     */
    public final void bluetoothMeasureAutoPw() {
        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
        manualMeasureOrder.setRateType(0);
        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_AUTO);
        manualMeasureOrder.setEnable(ManualMeasureNewOrder.accEnable | ManualMeasureNewOrder.recognizeEnable |
                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
        manualMeasureOrder.setRegionAvailable(false);
        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
        getOrder(new BluetoothOrder(BleConstant.CODE_MEASURE_PW_AUTO, manualMeasureOrder));
    }

    public final void bluetoothMeasurePwWithoutRecognize(boolean accEnable) {
        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
        manualMeasureOrder.setRateType(0);
        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_PW_RAW_DATA);
        manualMeasureOrder.setEnable((accEnable ? ManualMeasureNewOrder.accEnable : 0) |
                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
        manualMeasureOrder.setRegionAvailable(false);
        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
        EventBus.getDefault().post(new BluetoothOrder(BleConstant.CODE_MEASURE_PW_WITHOUT_RECOGNIZE, manualMeasureOrder));
    }

    public final void bluetoothMeasurePw(boolean accEnable) {
        ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
        manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
        manualMeasureOrder.setRateType(0);
        manualMeasureOrder.setRateValue(ManualMeasureNewOrder.PW_SAMPLE_RATE);
        manualMeasureOrder.setResultDoublePointNumber(ManualMeasureNewOrder.COMPLETE_POINTS);
        manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_PW_RAW_DATA);
        manualMeasureOrder.setEnable((accEnable ? ManualMeasureNewOrder.accEnable : 0) |
                ManualMeasureNewOrder.incrementEnable | ManualMeasureNewOrder.phaseChangeEnable);
        manualMeasureOrder.setRegionAvailable(false);
        manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
        getOrder(new BluetoothOrder(BleConstant.CODE_MEASURE_PW, manualMeasureOrder));
    }

    /**
     * 发送停止采样指令
     */
    public final void bluetoothStopMeasure() {
        getOrder(new BluetoothOrder(BleConstant.CODE_STOP_MEASURE));
    }

    /**
     * 设置自动采样是否开启
     *
     * @param enable 是否开启
     */
    public final void enableBluetoothAutoMeasure(boolean enable) {
        getOrder(new BluetoothOrder(BleConstant.CODE_ENABLE_AUTO_MEASURE, enable));
    }

    /**
     * 开始固件升级
     *
     * @param data 固件信息
     */
    public final void bluetoothUpgradeFirmware(FirmwareUpgradeData data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_UPGRADE, data));
    }

    /**
     * 取消固件升级
     */
    public final void bluetoothCancelUpgrade() {
        getOrder(new BluetoothOrder(BleConstant.CODE_CANCEL_UPGRADE));
    }

    /**
     * 重置自动采样时间
     */
    public final void bluetoothResetAutoSampleTimeAndSyncBloodPressure() {
        getOrder(new BluetoothOrder(BleConstant.CODE_RESET_AUTO_SAMPLE_TIME_AND_SYCN_BP));

    }

    /**
     * 解除绑定
     */
    public final void bluetoothUnPair() {
        getOrder(new BluetoothOrder(BleConstant.CODE_UN_PAIR));
    }

    /**
     * 获取自动采样提醒时间
     */
    public final void bluetoothGetAutoReminder() {
        getOrder(new BluetoothOrder(BleConstant.CODE_GET_ALERT_REMINDERS));

    }

    /**
     * 获取手环显示信息
     */
    public final void bluetoothGetBraceletDisplayConfig() {
        getOrder(new BluetoothOrder(BleConstant.CODE_GET_DISPLAY_PAGE));
    }

    /**
     * 设置自动采样提醒时间
     *
     * @param data 提醒内容
     */
    public final void bluetoothSetAutoReminder(ReminderData data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_SET_ALERT_REMINDER, data));
    }

    /**
     * 设置自动采样提醒时间
     *
     * @param data 提醒内容
     */
    public final void bluetoothSetAutoReminderAndInteval(ReminderData data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_SET_AUTO_MEASURE_INTERVAL, data));
    }

    /**
     * 设置手环显示信息
     *
     * @param data 提醒内容
     */
    public final void bluetoothSetDisplayConfig(DisplayPage data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_SET_DISPLAY_PAGE, data));
    }

    /**
     * 设置手环身高体重
     *
     * @param data 提醒内容
     */
    public final void bluetoothSetHeightWeight(HeightWeightData data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_SET_HEIGHT_WEIGHT, data));
    }

    /**
     * 获取计步历史数据
     */
    public final void bluetoothGetStepHistoryData() {
        getOrder(new BluetoothOrder(BleConstant.CODE_SYNC_STEP_HISTORY_DATA));
    }

    /**
     * 获取计步数据(15min)
     */
    public final void bluetoothGetStepData() {
        getOrder(new BluetoothOrder(BleConstant.CODE_SYNC_STEP_DATA));
    }

    /**
     * 同步历史计步数据至手环
     *
     * @param data 历史计步数据
     */
    public final void bluetoothSetStepHistoryData(CurrentStepData data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_CALIBRATE_STEP_DATA, data));
    }


    /**
     * 发送血压数据至手环
     *
     * @param syncBloodPressure 血压数据
     */
    public final void bluetoothSetBloodPressure(SyncBloodPressure syncBloodPressure) {
        getOrder(new BluetoothOrder(BleConstant.CODE_SET_BLOOD_PRESSURE, syncBloodPressure));
    }

    /**
     * 发送设备校验结果
     *
     * @param checkDeviceData 校验成功后发
     */
    public final void bluetoothSendVerifyDeviceResult(CheckDeviceData checkDeviceData) {
        getOrder(new BluetoothOrder(BleConstant.CODE_VERIFY, checkDeviceData));
    }

    public final void bluetoothGetStoredManualData() {
        getOrder(new BluetoothOrder(BleConstant.CODE_GET_STORED_MANUAL_DATA));
    }

    public final void bluetoothFindBracelet(LookBandData data) {
        getOrder(new BluetoothOrder(BleConstant.CODE_LOOK_FOR_BAND,data));
    }

    public final void bluetoothPairInit() {
        bizController.pairInit();
    }
}
