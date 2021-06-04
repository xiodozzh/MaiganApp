package com.mgtech.blelib.biz;


import android.util.Log;

import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.StepHistory;
import com.mgtech.blelib.entity.StoredSampleData;
import com.mgtech.blelib.utils.UpgradeFirmwareManager;

/**
 * @author zhaixiang
 */
public class BleState {
    private static final String TAG = "BleState";
    public static final int STATUS_DISCONNECT = 556;
    public static final int STATUS_SAMPLING = 925;
    public static final int STATUS_UPGRADING_START = 869;
    public static final int STATUS_UPGRADING_CONTINUE = 70;
    public static final int STATUS_LAUNCH_WORK = 873;
    public static final int STATUS_READY = 330;

    private BroadcastData linkedBroadcastData;
    private RandomCodeGenerator generator;
    private CheckDeviceData unconfirmedDeviceData;
    /**
     * 计步历史数据
     */
    private StepHistory.Builder stepHistoryBuilder;
    private StoredSampleData.Builder storedSampleDataBuilder;
    private UpgradeFirmwareManager upgradeFileManager;
    private boolean cancelUpgrading;
    private boolean upgradeSuccess;
    private boolean autoDataState = false;

    private int status = STATUS_DISCONNECT;

    private ReminderData reminders;
    private DisplayPage displayPage;
    private HeightWeightData heightWeightData;

    BleState() {
        this.generator = RandomCodeGenerator.getInstance();
    }

    public void clear() {
        stepHistoryBuilder = null;
        storedSampleDataBuilder = null;
        upgradeFileManager = null;
        reminders = null;
        displayPage = null;
        heightWeightData = null;
    }

    BroadcastData getLinkedBroadcastData() {
        return linkedBroadcastData;
    }

    void setLinkedBroadcastData(BroadcastData linkedBroadcastData) {
        this.linkedBroadcastData = linkedBroadcastData;
    }

    boolean randomCodeSuccess(byte code) {
        return code == generator.get();
    }

    CheckDeviceData getUnconfirmedDeviceData() {
        return unconfirmedDeviceData;
    }

    void setUnconfirmedDeviceData(CheckDeviceData unconfirmedDeviceData) {
        this.unconfirmedDeviceData = unconfirmedDeviceData;
    }

    StepHistory.Builder getStepHistoryBuilder() {
        return stepHistoryBuilder;
    }

    void setStepHistoryBuilder(StepHistory.Builder stepHistoryBuilder) {
        this.stepHistoryBuilder = stepHistoryBuilder;
    }

    StoredSampleData.Builder getStoredSampleDataBuilder() {
        return storedSampleDataBuilder;
    }

    void setStoredSampleDataBuilder(StoredSampleData.Builder storedSampleDataBuilder) {
        this.storedSampleDataBuilder = storedSampleDataBuilder;
    }

    UpgradeFirmwareManager getUpgradeFileManager() {
        return upgradeFileManager;
    }

    void setUpgradeFileManager(UpgradeFirmwareManager upgradeFileManager) {
        this.upgradeFileManager = upgradeFileManager;
    }

    boolean isCancelUpgrading() {
        return cancelUpgrading;
    }

    void setCancelUpgrading(boolean cancelUpgrading) {
        this.cancelUpgrading = cancelUpgrading;
    }

    boolean isAutoDataState() {
        return autoDataState;
    }

    void setAutoDataState(boolean autoDataState) {
        this.autoDataState = autoDataState;
    }

    public int getStatus() {
        return status;
    }

    void setStatus(int status, String tag) {
        Log.i("bleState", "setWorking: " + tag + " " + status);
        this.status = status;
    }

    public boolean isUpgrading(){
        return status == BleState.STATUS_UPGRADING_CONTINUE || status == BleState.STATUS_UPGRADING_START;
    }

    boolean isReady() {
        Log.i(TAG, "isReady: " + status);
        return status != STATUS_DISCONNECT;
    }

    public boolean isWorking() {
        return status != STATUS_READY;
    }


    public ReminderData getReminders() {
        return reminders;
    }

    public void setReminders(ReminderData reminders) {
        this.reminders = reminders;
    }

    public DisplayPage getDisplayPage() {
        return displayPage;
    }

    public void setDisplayPage(DisplayPage displayPage) {
        this.displayPage = displayPage;
    }

    public HeightWeightData getHeightWeightData() {
        return heightWeightData;
    }

    public void setHeightWeightData(HeightWeightData heightWeightData) {
        this.heightWeightData = heightWeightData;
    }

    public boolean isUpgradeSuccess() {
        return upgradeSuccess;
    }

    public void setUpgradeSuccess(boolean upgradeSuccess) {
        this.upgradeSuccess = upgradeSuccess;
    }
}
