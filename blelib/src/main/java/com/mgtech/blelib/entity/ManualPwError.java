package com.mgtech.blelib.entity;

import com.mgtech.blelib.biz.BluetoothConfig;

/**
 * Created by zhaixiang on 2018/3/8.
 * 错误码
 */

public class ManualPwError {
    /**
     * 正常
     */
    public static final int ERROR_TYPE_NONE = ManualSampleData.ERROR_TYPE_NONE;
    /**
     * 指令参数错误
     */
    public static final int ERROR_TYPE_PARAM = ManualSampleData.ERROR_TYPE_PARAM;
    /**
     * 流程错误
     */
    public static final int ERROR_TYPE_PROCESS = ManualSampleData.ERROR_TYPE_PROCESS;
    /**
     * 运动
     */
    public static final int ERROR_TYPE_MOTIVATION = ManualSampleData.ERROR_TYPE_MOTIVATION;
    /**
     * 导联脱落
     */
    public static final int ERROR_TYPE_LEAD_OFF = ManualSampleData.ERROR_TYPE_LEAD_OFF;
    /**
     * 未佩戴
     */
    public static final int ERROR_TYPE_NO_WEAR = ManualSampleData.ERROR_TYPE_NO_WEAR;
    /**
     * 低电量
     */
    public static final int ERROR_TYPE_LOW_POWER = ManualSampleData.ERROR_TYPE_LOW_POWER;
    /**
     * 充电中
     */
    public static final int ERROR_TYPE_CHARGING = ManualSampleData.ERROR_TYPE_CHARGING;

    /**
     * 设备忙
     */
    public static final int ERROR_TYPE_BUSY = ManualSampleData.ERROR_TYPE_BUSY;

    public static final int ERROR_SIGNAL_HR_LOW = BluetoothConfig.ERROR_SIGNAL_HR_LOW;

    public static final int ERROR_SIGNAL_HR_HIGH = BluetoothConfig.ERROR_SIGNAL_HR_HIGH;

    private int errorCode;

    public ManualPwError(int errorCode) {
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
        return "ManualPwError{" +
                "errorCode=" + errorCode +
                '}';
    }
}
