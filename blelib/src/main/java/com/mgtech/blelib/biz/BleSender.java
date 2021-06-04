package com.mgtech.blelib.biz;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BluetoothOrder;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareInfoData;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.LinkStatusEvent;
import com.mgtech.blelib.entity.LookForBraceletEvent;
import com.mgtech.blelib.entity.ManualEcgData;
import com.mgtech.blelib.entity.ManualEcgError;
import com.mgtech.blelib.entity.ManualPwData;
import com.mgtech.blelib.entity.ManualPwError;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.StatusDataEvent;
import com.mgtech.blelib.entity.StepHistory;
import com.mgtech.blelib.entity.StopMeasureData;

/**
 * @author zhaixiang
 */
public class BleSender {
    private BleCenter bleCenter;
    private BleResponse bleResponse;

    BleSender(BleCenter bleCenter, BleResponse bleResponse) {
        this.bleCenter = bleCenter;
        this.bleResponse = bleResponse;
    }

    void sendBroadcast(BroadcastData broadcastData) {
        bleResponse.broadcast(broadcastData);
    }

    void sendPairCodeDifferent() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_PAIR_CODE_DIFFERENT));
    }

    void sendFindDevice() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_FIND_DEVICE));
    }

    void braceletNeedCheck(CheckDeviceData data) {
        bleResponse.device(data);
    }

    void startVerify() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_VERIFING));
    }

    void sleep() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_SLEEP));
    }

    void connected() {
        bleCenter.clearTask();
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_CONNECTED));
    }

    public void link() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_LINK));
        bleCenter.initLinkTask();
    }

    void pairSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_PAIR_SUCCESS));
//        bleCenter.initPairTask();
    }

    void pairFail() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_PAIR_FAIL));
    }

    public void disconnect() {
        bleCenter.clearTask();
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_DISCONNECTED));
    }


    public void power() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_GET_POWER_SUCCESS));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void manualPwData(ManualPwData data){
        bleResponse.manualPwData(data);
    }

    void manualEcgData(ManualEcgData data){
        bleResponse.manualEcgData(data);
    }

    void samplePwError(ManualPwError error) {
        bleResponse.manualPwError(error);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void sampleEcgError(ManualEcgError error) {
        bleResponse.manualEcgError(error);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void stopSample() {
        bleResponse.manualStop(new StopMeasureData());
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void getSampleCompleteManualData(SampleCompleteData data) {
        bleResponse.manualPwComplete(data);
    }

    void getSampleCompleteAutoData(SampleCompleteData data) {
        bleResponse.autoPwComplete(data);
    }

    void getLinkStatus(LinkStatusEvent event){
        bleResponse.blueLinkStatus(event);
    }

    void stopReceivingAutoPwData() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setBloodPressureComplete() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void unPairSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_UN_PAIR_SUCCESS));
    }

    void setTimeSuccess() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void resetAutoSampleTimeSuccess() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setSampleAgainSuccess() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    public void upgrade(FirmStateData data) {
        bleResponse.firm(data);
    }


    void upgradeCancel() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void getFirmwareInfo(FirmwareInfoData data) {
        if (bleCenter != null) {
//            EventBus.getDefault().post(data);
            bleResponse.firmwareVersion(data);
            bleCenter.runNextTask();
        }
    }

    void startReceivingAutoPwData() {
    }


    void deleteStoredManualSampleDataSuccess() {
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }


    public void getDisplayPage(DisplayPage page) {
        bleResponse.displayPage(page);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void getAlertReminders(ReminderData data) {
        bleResponse.alertReminder(data);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void getHeightAndWeight(HeightWeightData data) {
        bleResponse.heightAndWeight(data);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setParamAlertRemindersSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_SET_ALERT_REMINDERS_SUCCESS));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setDisplayPageSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_SET_DISPLAY_PAGE_SUCCESS));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setHeightAndWeightSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_SET_HEIGHT_WEIGHT_SUCCESS));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }


    void onPowerChange() {
        if (bleCenter != null) {
            bleCenter.putOrderInQueue(new BluetoothOrder(BleConstant.CODE_GET_POWER));
            bleCenter.runNextTaskWhenNotWorking();
        }
    }

    void onAutoDataChange() {
        if (bleCenter != null) {
            bleCenter.putOrderInQueue(new BluetoothOrder(BleConstant.CODE_GET_AUTO_MEASURE_INFO));
            bleCenter.runNextTaskWhenNotWorking();
        }
    }

    void stepHistory(StepHistory stepHistory) {
        bleResponse.stepHistory(stepHistory);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void currentStep(CurrentStepData currentStepData) {
        bleResponse.currentStepData(currentStepData);
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setStepUpdateSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_ENABLE_STEP_UPDATE));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setFindBraceletSuccess(int code) {
        bleResponse.blueFindBracelet(new LookForBraceletEvent(code));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void setHistoryStepSuccess() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_SET_HISTORY_STEP_SUCCESS));
        if (bleCenter != null) {
            bleCenter.runNextTask();
        }
    }

    void noAutoSampleData() {
        if (bleCenter != null) {
            bleCenter.runNextTaskWhenNotWorking();
        }
    }

     void ecgStartFirstStatus() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_MEASURE_ECG_1));
    }

    void ecgStartSecondStatus() {
        bleResponse.status(new StatusDataEvent(BleStatusConstant.STATUS_MEASURE_ECG_2));
    }
}
