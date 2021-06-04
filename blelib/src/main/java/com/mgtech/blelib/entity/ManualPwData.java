package com.mgtech.blelib.entity;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2018/3/8.
 * 手动脉图数据
 */

public class ManualPwData {
    private short[] data;
    private int percent;
    private float sampleRate;
    private boolean realData;
    private int errorCode;

    public ManualPwData(short[] data, int percent, float sampleRate, boolean realData, int errorCode) {
        this.data = data;
        this.percent = percent;
        this.sampleRate = sampleRate;
        this.realData = realData;
        this.errorCode = errorCode;
    }

    public short[] getData() {
        return data;
    }

    public void setData(short[] data) {
        this.data = data;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public boolean isRealData() {
        return realData;
    }

    public void setRealData(boolean realData) {
        this.realData = realData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ManualPwData{" +
                "data=" + Arrays.toString(data) +
                ", percent=" + percent +
                ", sampleRate=" + sampleRate +
                ", realData=" + realData +
                ", errorCode=" + errorCode +
                '}';
    }
}
