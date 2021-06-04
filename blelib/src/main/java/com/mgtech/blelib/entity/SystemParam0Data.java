package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by zhaixiang on 2017/2/18.
 * 获取系统参数
 */

public class SystemParam0Data implements Parcelable, ISystemParam {
    /**
     * 省电模式
     */
    private boolean powerSaveMode;
    /**
     * 自动模式
     */
    private boolean autoMode;


    public SystemParam0Data() {
    }

    public SystemParam0Data(boolean powerSaveMode, boolean autoMode) {
        this.powerSaveMode = powerSaveMode;
        this.autoMode = autoMode;
    }

    protected SystemParam0Data(Parcel in) {
        powerSaveMode = in.readByte() != 0;
        autoMode = in.readByte() != 0;
    }

    public static final Creator<SystemParam0Data> CREATOR = new Creator<SystemParam0Data>() {
        @Override
        public SystemParam0Data createFromParcel(Parcel in) {
            return new SystemParam0Data(in);
        }

        @Override
        public SystemParam0Data[] newArray(int size) {
            return new SystemParam0Data[size];
        }
    };


    public boolean isPowerSaveMode() {
        return powerSaveMode;
    }

    public void setPowerSaveMode(boolean powerSaveMode) {
        this.powerSaveMode = powerSaveMode;
    }

    public boolean isAutoMode() {
        return autoMode;
    }

    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }

    @Override
    public byte[] getSetParamCode(byte[] mac, int pairCode) {
        byte[] buf = new byte[5];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_0;
        buf[1] = (byte) (this.isPowerSaveMode() ? 0x01 : 0x00);
        buf[2] = (byte) (this.isAutoMode() ? 0x01 : 0x00);
        buf[3] = (byte) (mac[0] ^ buf[1]);
        buf[4] = (byte) (mac[1] ^ buf[2]);
        return buf;
    }

    @Override
    public String getSaveString() {
//        byte[] buf = new byte[3];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_0;
//        buf[1] = (byte) (this.isPowerSaveMode() ? 0x01 : 0x00);
//        buf[2] = (byte) (this.isAutoMode() ? 0x01 : 0x00);
        return new Gson().toJson(this);
    }

    @Override
    public void setData(byte[] data) {
        if (data.length == 3) {
            this.powerSaveMode = data[1] == 0x01;
            this.autoMode = data[2] == 0x01;
        }
    }

    @Override
    public String toString() {
        return "SystemParam0Data{" +
                "powerSaveMode=" + powerSaveMode +
                ", autoMode=" + autoMode +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (powerSaveMode ? 1 : 0));
        dest.writeByte((byte) (autoMode ? 1 : 0));
    }
}
