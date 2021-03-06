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
     * ??????????????????
     *
     * @param isConnected true ????????????????????????
     */
    protected abstract void onLinkStatusReceived(boolean isConnected);

    /**
     * ??????????????????
     *
     * @param statusType ??????
     */
    protected void onBluetoothStatusReceived(int statusType) {

    }


    /**
     * ???????????????id???mac??????
     *
     * @param data ??????
     */
    protected void onIdAndMacReceived(CheckDeviceData data) {

    }

    /**
     * ?????????????????????
     *
     * @param broadcastData ??????
     */
    protected void onBroadcastReceived(BroadcastData broadcastData) {
    }

    /**
     * ????????????????????????
     *
     * @param data ?????????????????????
     */
    protected void onFirmwareUpgrade(FirmStateData data) {
    }

    protected void onFirmwareInfo(FirmwareInfoData data) {
    }

    /**
     * ?????????????????????????????????
     *
     * @param data ????????????
     */
    protected void onManualPWDataReceived(ManualPwData data) {
    }

    /**
     * ??????
     *
     * @param errorType ?????????
     */
    protected void onManualPWErrorDataReceived(ManualPwError errorType) {
    }

    /**
     * ??????????????????????????????
     */
    protected void onManualPWCompleteDataReceived(SampleCompleteData result) {
    }

    /**
     * ??????????????????????????????
     */
    protected void onAutoPWCompleteDataReceived(SampleCompleteData result) {
    }
    /**
     * ????????????????????????
     */
    protected void onManualStop() {
    }

    /**
     * ?????????????????????????????????
     *
     * @param data ????????????
     */
    protected void onManualECGDataReceived(ManualEcgData data) {
    }

    /**
     * ??????
     *
     * @param error ?????????
     */
    protected void onManualECGErrorDataReceived(ManualEcgError error) {
    }


    /**
     * ???????????????
     *
     * @param reminders ??????
     */
    protected void onAlertReminders(ReminderData reminders) {
    }

    /**
     * ?????????????????????
     *
     * @param displayPage ????????????
     */
    protected void onDisplayPage(DisplayPage displayPage) {
    }

    protected void onHeightAndWeight(HeightWeightData heightWeightData) {
    }

//    /**
//     * ??????????????????
//     *
//     * @param state ??????
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
