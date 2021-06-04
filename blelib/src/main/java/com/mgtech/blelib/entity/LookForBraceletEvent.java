package com.mgtech.blelib.entity;

import com.mgtech.blelib.biz.BluetoothConfig;

public class LookForBraceletEvent {
    public static final int NONE = BluetoothConfig.ERROR_NONE;
    public static final int ERROR_PARANS = BluetoothConfig.ERROR_PARAMS;
    public static final int ERROR_HARDWARE = BluetoothConfig.ERROR_HARDWARE;
    public static final int ERROR_LOW_BATTERY = BluetoothConfig.ERROR_LOW_BATTERY;
    public static final int ERROR_BUSY = BluetoothConfig.ERROR_BUSY;
    public static final int ERROR_FAILED = BluetoothConfig.ERROR_FAILED;

    private int errorCode;

    public LookForBraceletEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "LookForBraceletEvent{" +
                "errorCode=" + errorCode +
                '}';
    }
}
