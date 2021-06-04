package com.mgtech.data.entity;

import android.util.Log;

import com.mgtech.domain.utils.BluetoothConfig;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2016/12/29.
 * 主动采样信息包
 */

public class ManualSampleInfoData {
    public static final  int RATE_CONSTANT = 32768;
    private byte[] data;
    private boolean reverse;
    private int rateType;
    private int rateValue;

    public ManualSampleInfoData(byte[] data) {
        if (data.length != 6){
            return;
        }
        this.data = data;
        this.reverse = data[2] == 0x01;
        this.rateType = data[3] & 0xFF;
        this.rateValue = (data[4] & 0xFF) + ((data[5] & 0xFF) << 8);
        Log.e("result", "ManualSampleInfoData: " + Arrays.toString(data)  );

    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public float getSampleRate(){
        float rate;
        switch (rateType){
            case BluetoothConfig.SAMPLE_RATE_TYPE_0:
                rate = rateValue;
                break;
            case BluetoothConfig.SAMPLE_RATE_TYPE_1:
                rate = RATE_CONSTANT / (float) rateValue;
                break;
            case BluetoothConfig.SAMPLE_RATE_TYPE_2:
                rate = (float) (1000 / 1.024 / rateValue);
                break;
            default:
                rate = rateValue;
        }
        return rate;
    }

    @Override
    public String toString() {
        return "信息包：[" +
                "是否反向：" + reverse + "\t" +
                "采样率：" + getSampleRate()  + "\n" +
//                "采样心率：" + heartRate + "\n" +
//                "每个应答包中双采样点数量：" + doublePointNumber + "\n" +
                ']';
    }
}
