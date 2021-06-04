package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 * 错误码
 */

public class ManualEcgError {
    /**
     * 指令参数错误
     */
    public static final int ERROR_TYPE_PARAM = ManualSampleData.ERROR_TYPE_PARAM;
    /**
     * 流程错误
     */
    public static final int ERROR_TYPE_PROCESS = ManualSampleData.ERROR_TYPE_PROCESS;
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

    private int errorCode;

    public ManualEcgError(int errorCode) {
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
        return "ManualEcgError{" +
                "errorCode=" + errorCode +
                '}';
    }
}
