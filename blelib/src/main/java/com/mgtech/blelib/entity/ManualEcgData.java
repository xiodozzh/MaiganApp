package com.mgtech.blelib.entity;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2018/3/8.
 * 测量ECG
 */

public class ManualEcgData {
    private double[] data;
    private float sampleRate;
    private int index;

    public ManualEcgData(double[] data, float sampleRate, int index) {
        this.data = data;
        this.sampleRate = sampleRate;
        this.index = index;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ManualEcgData{" +
                "data=" + Arrays.toString(data) +
                ", sampleRate=" + sampleRate +
                ", index=" + index +
                '}';
    }
}
