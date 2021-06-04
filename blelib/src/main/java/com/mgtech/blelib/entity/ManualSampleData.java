package com.mgtech.blelib.entity;


import com.mgtech.blelib.biz.BluetoothConfig;
import com.mgtech.blelib.utils.Utils;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2017/7/25.
 * 主动测量应答
 */

public class ManualSampleData {
    /**
     * 正常
     */
    public static final int ERROR_TYPE_NONE = 0;
    /**
     * 指令参数错误
     */
    public static final int ERROR_TYPE_PARAM = BluetoothConfig.ERROR_PARAMS;
    /**
     * 流程错误
     */
    public static final int ERROR_TYPE_PROCESS = BluetoothConfig.ERROR_FAILED;

    /**
     * 硬件错误
     */
    public static final int ERROR_HARDWARE = BluetoothConfig.ERROR_HARDWARE;
    /**
     * 运动
     */
    public static final int ERROR_TYPE_MOTIVATION = BluetoothConfig.ERROR_MOTION;
    /**
     * 导联脱落
     */
    public static final int ERROR_TYPE_LEAD_OFF = BluetoothConfig.ERROR_ECG_LEAD_OFF;
    /**
     * 未佩戴
     */
    public static final int ERROR_TYPE_NO_WEAR = BluetoothConfig.ERROR_WEAR;
    /**
     * 低电量
     */
    public static final int ERROR_TYPE_LOW_POWER = BluetoothConfig.ERROR_LOW_BATTERY;
    /**
     * 充电中
     */
    public static final int ERROR_TYPE_CHARGING = BluetoothConfig.ERROR_CHARGING;
    /**
     * 设备忙
     */
    public static final int ERROR_TYPE_BUSY = BluetoothConfig.ERROR_BUSY;

    public static final int ERROR_CRC_CHECK = BluetoothConfig.ERROR_CRC_CHECK;

    public static final int ERROR_NO_DATA = BluetoothConfig.ERROR_NO_DATA;

    public static final int ERROR_MTU_SIZE = BluetoothConfig.ERROR_MTU_SIZE;

    public static final int ERROR_END_OF_STREAM = BluetoothConfig.ERROR_END_OF_STREAM;

    public static final int ERROR_UNSPECIFIED = BluetoothConfig.ERROR_UNSPECIFIED;

    public static final int ERROR_SIGNAL_AMP_LOW = BluetoothConfig.ERROR_SIGNAL_AMP_LOW;

    public static final int ERROR_SIGNAL_AMP_HIGH = BluetoothConfig.ERROR_SIGNAL_AMP_HIGH;
    public static final int ERROR_SIGNAL_WEAR_TIGHT = BluetoothConfig.ERROR_SIGNAL_WEAR_TIGHT;
    public static final int ERROR_SIGNAL_WEAR_LOOSE = BluetoothConfig.ERROR_SIGNAL_WEAR_LOOSE;
    public static final int ERROR_SIGNAL_WRONG = BluetoothConfig.ERROR_SIGNAL_WRONG;
    public static final int ERROR_SIGNAL_HR_LOW = BluetoothConfig.ERROR_SIGNAL_HR_LOW;
    public static final int ERROR_SIGNAL_HR_HIGH = BluetoothConfig.ERROR_SIGNAL_HR_HIGH;

    private short[] unzippedData;
    private static final int RATE_CONSTANT = 32768;
    private static final int SAMPLE_STAGE = 1 << 7;
    private int taq;
    private int errorCode;
    private int pointNumber;
    private int order;
    private float sampleRate;
    private byte[] zippedData;
    private boolean haveData;
    private boolean realData;
    private int percent;

    public int getErrorCode() {
        return errorCode;
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public short[] getUnZippedData() {
        return Utils.unzipData(zippedData, 0);
    }

    public boolean isHaveData() {
        return haveData;
    }

    public int getTaq() {
        return taq;
    }

    public void setTaq(int taq) {
        this.taq = taq;
    }

    public boolean isRealData() {
        return realData;
    }

    public void setRealData(boolean realData) {
        this.realData = realData;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isAuto() {
        return taq == ManualMeasureNewOrder.TAG_AUTO;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public short[] getUnzippedData() {
        return unzippedData;
    }

    public void setUnzippedData(short[] unzippedData) {
        this.unzippedData = unzippedData;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getOrder() {
        return order;
    }

    public ManualPwData getManualPwData() {
        return new ManualPwData(getUnZippedData(), percent, sampleRate, realData, errorCode);
    }

    public static class Builder {

        public ManualSampleData setInProfile400(byte[] rawData) {
            ManualSampleData manualSampleData = new ManualSampleData();
            if (rawData.length < 4) {
                return null;
            }
            manualSampleData.taq = rawData[1] & 0xFF;
            manualSampleData.errorCode = rawData[2];
            manualSampleData.pointNumber = rawData[3] & 0xFF;
            if (rawData.length == 4) {
                return manualSampleData;
            }
            manualSampleData.realData = (rawData[4] & SAMPLE_STAGE) == SAMPLE_STAGE;
            manualSampleData.percent = rawData[4] & 0x7F;
            manualSampleData.haveData = (rawData.length == manualSampleData.pointNumber * 3 + 8);
            if (manualSampleData.haveData) {
                int sampleType = (rawData[5] & 0x7);
//                manualSampleData.isOrderError = ((manualSampleData.order + 1) & 0xF) != ((rawData[5] >> 4) & 0xF);
                manualSampleData.order = (rawData[5] >> 4) & 0xF;
                switch (sampleType) {
                    case 0:
                        manualSampleData.sampleRate = (rawData[6] & 0xFF) + ((rawData[7] & 0xFF) << 8);
                        break;
                    case 1:
                        manualSampleData.sampleRate = RATE_CONSTANT / (float) (rawData[6] & 0xFF) + ((rawData[7] & 0xFF) << 8);
                        break;
                    case 2:
                        manualSampleData.sampleRate = 1000 / 1.024f / (rawData[6] & 0xFF) + ((rawData[7] & 0xFF) << 8);
                        break;
                    default:
                        manualSampleData.sampleRate = (rawData[6] & 0xFF) + ((rawData[7] & 0xFF) << 8);
                }
                manualSampleData.zippedData = new byte[rawData.length - 8];
                System.arraycopy(rawData, 8, manualSampleData.zippedData, 0, manualSampleData.zippedData.length);
                manualSampleData.unzippedData = Utils.unzipData(manualSampleData.zippedData, 0);
            }
            return manualSampleData;
        }
    }

//        private static int profile400transErrorCode(int bleErrorCode) {
//            int info = 0;
//            switch (bleErrorCode) {
//                case BluetoothConfig.ERROR_PARAMS:
//                    info = ERROR_TYPE_PARAM;
//                    break;
//                case BluetoothConfig.ERROR_FAILED:
//                    info = ERROR_TYPE_PROCESS;
//                    break;
//                case BluetoothConfig.ERROR_CHARGING:
//                    info = ERROR_TYPE_CHARGING;
//                    break;
//                case BluetoothConfig.ERROR_MOTION:
//                    info = ERROR_TYPE_MOTIVATION;
//                    break;
//                case BluetoothConfig.ERROR_WEAR:
//                    info = ERROR_TYPE_NO_WEAR;
//                    break;
//                case BluetoothConfig.ERROR_ECG_LEAD_OFF:
//                    info = ERROR_TYPE_LEAD_OFF;
//                    break;
//                case BluetoothConfig.ERROR_BUSY:
//                    info = ERROR_TYPE_BUSY;
//            }
//            return info;
//        }
//    }

    @Override
    public String toString() {
        return "ResponseManualData{" +
                "taq=" + taq +
                ", errorCode=" + errorCode +
                ", pointNumber=" + pointNumber +
                ", order=" + order +
                ", sampleRate=" + sampleRate +
                ", zippedData=" + Arrays.toString(zippedData) +
                ", haveData=" + haveData +
                ", realData=" + realData +
                ", percent=" + percent +
                '}';
    }
}
