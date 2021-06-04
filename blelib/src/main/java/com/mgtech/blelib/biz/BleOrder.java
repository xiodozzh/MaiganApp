package com.mgtech.blelib.biz;

import android.text.TextUtils;
import android.util.Log;

import com.mgtech.blelib.core.BleClient2;
import com.mgtech.blelib.core.BleCoreConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
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
import com.mgtech.blelib.entity.SystemParam0Data;
import com.mgtech.blelib.entity.SystemParamData;
import com.mgtech.blelib.utils.UpgradeFirmwareManager;
import com.mgtech.blelib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class BleOrder {
    private static final String TAG = "BleOrder";
    private static final int AUTO_SAMPLE_DOUBLE_POINT_SIZE = ManualMeasureNewOrder.COMPLETE_POINTS;

    private BleCore bleCore;
    private BleClient2 bleClient;
    private IOrderBytes order;
    private IBraceletInfoManager manager;
    private BleState bleState;
//    private static final int RANDOM_REMINDER = 4;
//    private static final int RANDOM_DISPLAY = 5;

    public void setBleCore(BleCore bleCore) {
        this.bleCore = bleCore;
    }

    public void setBleClient(BleClient2 bleClient) {
        this.bleClient = bleClient;
    }

    public void setOrder(IOrderBytes order) {
        this.order = order;
    }

    public void setManager(IBraceletInfoManager manager) {
        this.manager = manager;
    }

    public void setBleState(BleState bleState) {
        this.bleState = bleState;
    }

    /**
     * 请求校验
     */
    public void verifyWork(final byte[] address, final int pairCode) {
        Log.e(TAG, "run: verify 2");
        bleClient.writeWithResponse(BleCoreConstant.MG_PROFILE_SERVICE,
                BleCoreConstant.CHAR_VERIFICATION_ORDER, order.verify(address, pairCode));

    }

    /**
     * 请求绑定
     */
    public void requestPair(BroadcastData linkedBroadcastData) {
        Log.e(TAG, "requestPair ");
        bleClient.writeWithoutResponse(BleCoreConstant.MG_PROFILE_SERVICE,
                BleCoreConstant.CHAR_VERIFICATION_ORDER,
                order.requestPairOrder(linkedBroadcastData.getMacAddressBytes(), linkedBroadcastData.getPairCode()));
        bleCore.launchPairTimeOutClock();
    }

    /**
     * 校验成功，确认绑定信息
     */
    public void verifySuccessAndPair(CheckDeviceData checkDeviceData) {
        bleState.setUnconfirmedDeviceData(checkDeviceData);
        write(order.commitPairCode(checkDeviceData.getKey(), checkDeviceData.getVector()));
    }


    /**
     * 查询是否有自动采样数据
     */
    public void getAutoSampleInfo() {
        Log.i(TAG, "获取自动采样信息 ");
        write(order.autoInfoOrder(getMacBytes()));
    }

    public void sendUpgradeInfo(FirmwareUpgradeData data) {
        bleState.setCancelUpgrading(false);
        UpgradeFirmwareManager upgradeFileManager = UpgradeFirmwareManager.getInstance(data);
        bleState.setUpgradeFileManager(upgradeFileManager);
        write(order.upgradeInfoOrder(upgradeFileManager));
    }

    public void cancelUpgrade() {
        bleState.setCancelUpgrading(true);
    }

    /**
     * 获取数据
     */
    public void getStoredAutoPwData() {
        bleState.setAutoDataState(true);
        write(order.getStoredDataOrder(getMacBytes(), manager.getCodePair(), true, AUTO_SAMPLE_DOUBLE_POINT_SIZE));
    }

    /**
     * 获取电量
     */
    public void getPower() {
        Log.i(TAG, "getPower: ");
        write(order.getBatteryPower());
    }

    /**
     * 解绑
     */
    public void unPair() {
        write(order.unPairCode(manager.getCodePair()));
    }

    public void getParameter0() {
        write(order.getSystemParam(getMacBytes(), manager.getCodePair(), 0));
    }

    public void getAlertReminders() {
        Log.i(TAG, "获取提醒 ");
        SystemParamData data = new SystemParamData();
        ReminderData reminderData = new ReminderData();
        data.setReminderData(reminderData, Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion()));
        write(order.getSystemParam(getMacBytes(), BluetoothConfig.PARAM_ALERT_REMINDERS, manager.getCodePair(), data));
    }

    public void getDisplayPage() {
        Log.i(TAG, "获取显示页面: ");
        SystemParamData data = new SystemParamData();
        DisplayPage pageDisplayData = new DisplayPage();
        data.setPageDisplayData(pageDisplayData);
        write(order.getSystemParam(getMacBytes(), BluetoothConfig.PARAM_DISPLAY_PAGE, manager.getCodePair(), data));
    }

    public void getHeightWeight() {
        Log.i(TAG, "获取身高体重: ");
        SystemParamData data = new SystemParamData();
        data.setHeightAndWeight(new HeightWeightData());
        write(order.getSystemParam(getMacBytes(), BluetoothConfig.PARAM_HEIGHT_WEIGHT, manager.getCodePair(), data));
    }

    public void getFirmwareInfo() {
        Log.i(TAG, "获取固件信息 ");
        write(order.getFirmwareInfo());
    }

    public void upgradeBody() {
        byte[] encryptedBuf = order.upgradeBodyOrder();
        if (encryptedBuf != null) {
            write(encryptedBuf);
        } else {
            bleClient.disconnect();
        }
    }

    public void upgradeComplete() {
        write(order.upgradeCompleteOrder());
    }

    public void measurePw(ManualMeasureNewOrder measureNewOrder) {
        Log.i(TAG, "采集脉图，并用手环脉图识别");
        write(order.getManualDataOrder(measureNewOrder));
    }

    public void measureEcg(ManualMeasureNewOrder measureNewOrder) {
        Log.i(TAG, "采集ECG");
        write(order.getManualDataOrder(measureNewOrder));
    }

    public void stopMeasure() {
        Log.i(TAG, "停止采样 ");
        write(order.stopManualDataOrder());
    }

    public void getStoredMeasureData() {
        Log.i(TAG, "获取储存的手动采集数据 ");
        bleState.setAutoDataState(false);
        write(order.getStoredDataOrder(getMacBytes(), manager.getCodePair(), false, AUTO_SAMPLE_DOUBLE_POINT_SIZE));
    }

    public void resetAutoSampleTime() {
        Log.i(TAG, "重置自动采样时间 ");
        write(order.resetAutoSampleTime(getMacBytes(), manager.getCodePair()));
    }

    public void setBloodPressure(SyncBloodPressure data) {
        Log.i(TAG, "设置显示血压 ");
        write(order.updateBloodPressureCode(data));
    }

    public void updateTime() {
        Log.i(TAG, "更新时间 ");
        write(order.updateTimeCode());
    }

    public void calibrateStep(CurrentStepData data) {
        Log.i(TAG, "同步步数 ");
        write(order.syncStepHistoryDataOrder(data));
    }

    public void getStepHistory(int hour) {
        Log.i(TAG, "获取x小时至今的计步数据 ");
        write(order.getHistoryStepDataOrder(hour));
    }

    public void setParameter0(SystemParam0Data systemParam0Data) {
    }

    public void setAlertReminders(ReminderData reminders) {
        SystemParamData data = new SystemParamData();
        data.setReminderData(reminders, false);
        bleState.setReminders(reminders);
        write(order.setSystemParam(getMacBytes(), BluetoothConfig.PARAM_ALERT_REMINDERS, manager.getCodePair(), data));
    }

    public void setAlertRemindersAndInteval(ReminderData reminders) {
        SystemParamData data = new SystemParamData();
        data.setReminderData(reminders, Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion()));
        bleState.setReminders(reminders);
        write(order.setSystemParam(getMacBytes(), BluetoothConfig.PARAM_ALERT_REMINDERS, manager.getCodePair(), data));
    }

    public void setDisplayPage(DisplayPage dataDisplay) {
        SystemParamData data = new SystemParamData();
        data.setPageDisplayData(dataDisplay);
        bleState.setDisplayPage(dataDisplay);
        write(order.setSystemParam(getMacBytes(), BluetoothConfig.PARAM_DISPLAY_PAGE, manager.getCodePair(), data));
    }

//    public void setAutoMeasureInterval(AutoMeasurePeriodData autoMeasureInterval){
//        SystemParamData data = new SystemParamData();
//        data.setAutoMeasurePeriodData(autoMeasureInterval);
//        bleState.setAutoMeasurePeriodData(autoMeasureInterval);
//        write(order.setSystemParam(getMacBytes(), BluetoothConfig.PARAM_AUTO_MEASURE_INTERVAL, manager.getCodePair(), data));
//    }

    public void setHeightAndWeight(HeightWeightData heightWeightData) {
        SystemParamData data = new SystemParamData();
        data.setHeightAndWeight(heightWeightData);
        bleState.setHeightWeightData(heightWeightData);
        write(order.setSystemParam(getMacBytes(), BluetoothConfig.PARAM_HEIGHT_WEIGHT, manager.getCodePair(), data));
    }

    /**
     * 删除
     */
    public void deleteStoredData(boolean isAutoData) {
        write(order.deleteSampleData(bleState.getStoredSampleDataBuilder().getCrc(), isAutoData));
    }

    /**
     * 查找手环
     *
     * @param data 振动屏幕
     */
    public void lookForBand(LookBandData data) {
        write(order.lookForBand(data));
    }

    /**
     * 写入特征值
     *
     * @param data 数据
     */
    private void write(byte[] data) {
//        bleState.setWorking(true);
        bleClient.writeWithoutResponse(BleCoreConstant.MG_PROFILE_SERVICE,
                BleCoreConstant.CHAR_ORDER, data);
    }

    public byte[] getMacBytes() {
        return hexStringToBytes(manager.getAddress());
    }


    /**
     * Convert hex string to byte[]
     *
     * @param s the hex string
     * @return byte[]
     */
    private byte[] hexStringToBytes(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        String hexString = s.replaceAll(":", "");
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4
                    | charToByte(hexChars[pos + 1]));
        }
        byte[] address = new byte[length];
        for (int i = 0; i < length; i++) {
            address[i] = d[length - i - 1];
        }
        return address;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
