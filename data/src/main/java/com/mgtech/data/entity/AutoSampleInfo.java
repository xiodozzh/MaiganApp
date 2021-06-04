package com.mgtech.data.entity;

import com.mgtech.domain.utils.BluetoothConfig;

/**
 * Created by zhaixiang on 2016/12/29.
 * 主动采样信息
 */

public class AutoSampleInfo {
    private byte[] data;
    private boolean error;
    private int lastSampleStatus;
    private int number;

    public AutoSampleInfo(byte[] data) {
        this.data = data;
        if (data[0] != BluetoothConfig.CODE_GET_AUTO_SAMPLE_INFO){
            throw new RuntimeException("data is not auto sample info");
        }
        this.error = data[1] != 0x00;
        this.lastSampleStatus = data[2] & 0xFF;
        this.number = data[3] & 0xFF;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public int getLastSampleStatus() {
        return lastSampleStatus;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        if (error){
            return "硬件错误";
        }
        switch (lastSampleStatus){
            case 0x00:
                return "采样成功";
            case 0x01:
                return "采样失败";
            case 0x02:
                return "未佩戴腕带";
            case 0x03:
                return "用户按按键中止采样";
            case 0x04:
                return "硬件错误";
        }
        return "其他错误";
    }
}
