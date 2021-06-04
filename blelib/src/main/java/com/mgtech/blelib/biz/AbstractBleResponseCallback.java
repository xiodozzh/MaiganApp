package com.mgtech.blelib.biz;

import androidx.core.util.Pair;

import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareInfoData;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ManualEcgData;
import com.mgtech.blelib.entity.ManualEcgError;
import com.mgtech.blelib.entity.ManualPwData;
import com.mgtech.blelib.entity.ManualPwError;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.StepHistory;

import rx.Subscriber;

public abstract class AbstractBleResponseCallback extends Subscriber<Pair<Integer, Object>> {

    @Override
    public void onNext(Pair<Integer, Object> pair) {
        if (pair == null || pair.first == null || pair.second == null) {
            return;
        }
        switch (pair.first) {
            case BleResponse.STATUS:
                onBluetoothStatusReceived((int) pair.second);
                break;
            case BleResponse.BROADCAST:
                onBroadcastReceived((BroadcastData) pair.second);
                break;
            case BleResponse.PW_DATA:
                onManualPWDataReceived((ManualPwData) pair.second);
                break;
            case BleResponse.PW_MANUAL_COMPLETE:
                onManualPWCompleteDataReceived((SampleCompleteData) pair.second);
                break;
            case BleResponse.PW_AUTO_COMPLETE:
                onAutoPWCompleteDataReceived((SampleCompleteData) pair.second);
                break;
            case BleResponse.PW_ERROR:
                onManualPWErrorDataReceived((ManualPwError) pair.second);
                break;
            case BleResponse.ECG_DATA:
                onManualECGDataReceived((ManualEcgData) pair.second);
                break;
            case BleResponse.ECG_ERROR:
                onManualECGErrorDataReceived((ManualEcgError) pair.second);
                break;
            case BleResponse.MEASURE_STOP:
                onManualStop();
                break;
            case BleResponse.DISPLAY:
                onDisplayPage((DisplayPage) pair.second);
                break;
            case BleResponse.REMIND:
                onAlertReminders((ReminderData) pair.second);
                break;
            case BleResponse.HEIGHT_WEIGHT:
                onHeightAndWeight((HeightWeightData) pair.second);
                break;
            case BleResponse.FIRMWARE_DATA:
                onFirmwareUpgrade((FirmStateData) pair.second);
                break;
            case BleResponse.FIRMWARE_INFO:
                onFirmwareInfo((FirmwareInfoData) pair.second);
                break;
            case BleResponse.CHECK_DEVICE:
                onIdAndMacReceived((CheckDeviceData) pair.second);
                break;
            case BleResponse.LINK_STATUS:
                onLinkStatusReceived((boolean) pair.second);
                break;
            case BleResponse.FIND:
                findResult((Integer) pair.second);
                break;
            case BleResponse.STEP_CURRENT:
                onCurrentStepReceived((CurrentStepData) pair.second);
                break;
            case BleResponse.STEP_HISTORY:
                onStep((StepHistory) pair.second);
                break;
            default:
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    /**
     * 蓝牙连接状态
     *
     * @param isConnected true 蓝牙正在连接状态
     */
    protected abstract void onLinkStatusReceived(boolean isConnected);

    /**
     * 处理状态信息
     *
     * @param statusType 状态
     */
    protected void onBluetoothStatusReceived(int statusType) {

    }


    /**
     * 接收到手环id和mac信息
     *
     * @param data 数据
     */
    protected void onIdAndMacReceived(CheckDeviceData data) {

    }

    /**
     * 接收到手环广播
     *
     * @param broadcastData 广播
     */
    protected void onBroadcastReceived(BroadcastData broadcastData) {
    }

    /**
     * 处理固件升级状态
     *
     * @param data 升级所处的状态
     */
    protected void onFirmwareUpgrade(FirmStateData data) {
    }

    protected void onFirmwareInfo(FirmwareInfoData data) {
    }

    /**
     * 处理主动采样数据返回值
     *
     * @param data 返回数据
     */
    protected void onManualPWDataReceived(ManualPwData data) {
    }

    /**
     * 错误
     *
     * @param errorType 错误码
     */
    protected void onManualPWErrorDataReceived(ManualPwError errorType) {
    }

    /**
     * 处理主动采样数据停止
     */
    protected void onManualPWCompleteDataReceived(SampleCompleteData result) {
    }

    /**
     * 处理自动采样数据停止
     */
    protected void onAutoPWCompleteDataReceived(SampleCompleteData result) {
    }
    /**
     * 用户主动结束采样
     */
    protected void onManualStop() {
    }

    /**
     * 处理主动采样数据返回值
     *
     * @param data 返回数据
     */
    protected void onManualECGDataReceived(ManualEcgData data) {
    }

    /**
     * 错误
     *
     * @param error 错误码
     */
    protected void onManualECGErrorDataReceived(ManualEcgError error) {
    }


    /**
     * 手环中提醒
     *
     * @param reminders 提醒
     */
    protected void onAlertReminders(ReminderData reminders) {
    }

    /**
     * 手环中显示页面
     *
     * @param displayPage 显示页面
     */
    protected void onDisplayPage(DisplayPage displayPage) {
    }

    protected void onHeightAndWeight(HeightWeightData heightWeightData) {
    }

//    /**
//     * 蓝牙状态变化
//     *
//     * @param state 状态
//     */
//    protected void onStateChange(int state) {
//    }

    protected void findResult(int code) {
    }

    protected void onStep(StepHistory stepHistory) {
    }

    protected void onCurrentStepReceived(CurrentStepData data) {
    }

}
