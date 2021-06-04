package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by zhaixiang on 2017/2/18.
 * 获取系统参数
 */

public class SystemParam1Data implements Parcelable , ISystemParam{

    /**
     * 自动模式使能控制
     */
    private boolean autoModelEnable;

    /**
     * 自动采样间隔
     */
    private int autoSampleInterval;
    /**
     * 自动采样超时时间
     */
    private int autoSampleTimeout;


    /**
     * 识别成功条件：最小脉图数量
     */
    private int sampleMinPwNumber;
    /**
     * 识别成功条件：最大脉图数量
     */
    private int sampleMaxPwNumber;
    /**
     * 识别成功条件：最大脉图时间
     */
    private int sampleMaxCompleteTime;
    /**
     * 脉图识别连续脉图最小数量
     */
    private int samplePwSectionNumber;

    public SystemParam1Data() {
    }

    protected SystemParam1Data(Parcel in) {
        autoModelEnable = in.readByte() != 0;
        autoSampleInterval = in.readInt();
        autoSampleTimeout = in.readInt();
        sampleMinPwNumber = in.readInt();
        sampleMaxPwNumber = in.readInt();
        sampleMaxCompleteTime = in.readInt();
        samplePwSectionNumber = in.readInt();
    }

    public static final Creator<SystemParam1Data> CREATOR = new Creator<SystemParam1Data>() {
        @Override
        public SystemParam1Data createFromParcel(Parcel in) {
            return new SystemParam1Data(in);
        }

        @Override
        public SystemParam1Data[] newArray(int size) {
            return new SystemParam1Data[size];
        }
    };

    @Override
    public void setData(byte[] data) {
        if (data.length == 10) {
            this.autoModelEnable = data[1] == 0x01;
            this.autoSampleInterval = (data[2] & 0xFF) + ((data[3] & 0xFF) << 8);
            this.autoSampleTimeout = (data[4] & 0xFF) + ((data[5] & 0xFF) << 8);
            this.samplePwSectionNumber = data[6] & 0xFF;
            this.sampleMinPwNumber = data[7] & 0xFF;
            this.sampleMaxPwNumber = data[8] & 0xFF;
            this.sampleMaxCompleteTime = data[9] & 0xFF;
        }
    }

    @Override
    public byte[] getSetParamCode(byte[] mac, int pairCode) {
        byte[] buf = new byte[12];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_1;
        buf[1] = (byte) (this.isAutoModelEnable() ? 0x01 : 0x00);
        buf[2] = (byte) (this.getAutoSampleInterval() & 0xFF);
        buf[3] = (byte) ((this.getAutoSampleInterval() >> 8) & 0xFF);
        buf[4] = (byte) (this.getAutoSampleTimeout() & 0xFF);
        buf[5] = (byte) ((this.getAutoSampleTimeout() >> 8) & 0xFF);
        buf[6] = (byte) (this.getSamplePwSectionNumber() & 0xFF);
        buf[7] = (byte) (this.getSampleMinPwNumber() & 0xFF);
        buf[8] = (byte) (this.getSampleMaxPwNumber() & 0xFF);
        buf[9] = (byte) (this.getSampleMaxCompleteTime() & 0xFF);
        buf[10] = (byte) (mac[0] ^ buf[7]);
        buf[11] = (byte) (mac[1] ^ buf[8]);
        return buf;
    }

    @Override
    public String getSaveString() {
//        byte[] buf = new byte[10];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_1;
//        buf[1] = (byte) (this.isAutoModelEnable() ? 0x01 : 0x00);
//        buf[2] = (byte) (this.getAutoSampleInterval() & 0xFF);
//        buf[3] = (byte) ((this.getAutoSampleInterval() >> 8) & 0xFF);
//        buf[4] = (byte) (this.getAutoSampleTimeout() & 0xFF);
//        buf[5] = (byte) ((this.getAutoSampleTimeout() >> 8) & 0xFF);
//        buf[6] = (byte) (this.getSamplePwSectionNumber() & 0xFF);
//        buf[7] = (byte) (this.getSampleMinPwNumber() & 0xFF);
//        buf[8] = (byte) (this.getSampleMaxPwNumber() & 0xFF);
//        buf[9] = (byte) (this.getSampleMaxCompleteTime() & 0xFF);
//        return buf;
        return new Gson().toJson(this);
    }

    public boolean isAutoModelEnable() {
        return autoModelEnable;
    }

    public void setAutoModelEnable(boolean autoModelEnable) {
        this.autoModelEnable = autoModelEnable;
    }

    public int getAutoSampleInterval() {
        return autoSampleInterval;
    }

    public void setAutoSampleInterval(int autoSampleInterval) {
        this.autoSampleInterval = autoSampleInterval;
    }

    public int getAutoSampleTimeout() {
        return autoSampleTimeout;
    }

    public void setAutoSampleTimeout(int autoSampleTimeout) {
        this.autoSampleTimeout = autoSampleTimeout;
    }

    public int getSampleMinPwNumber() {
        return sampleMinPwNumber;
    }

    public void setSampleMinPwNumber(int sampleMinPwNumber) {
        this.sampleMinPwNumber = sampleMinPwNumber;
    }

    public int getSampleMaxPwNumber() {
        return sampleMaxPwNumber;
    }

    public void setSampleMaxPwNumber(int sampleMaxPwNumber) {
        this.sampleMaxPwNumber = sampleMaxPwNumber;
    }

    public int getSampleMaxCompleteTime() {
        return sampleMaxCompleteTime;
    }

    public void setSampleMaxCompleteTime(int sampleMaxCompleteTime) {
        this.sampleMaxCompleteTime = sampleMaxCompleteTime;
    }

    public int getSamplePwSectionNumber() {
        return samplePwSectionNumber;
    }

    public void setSamplePwSectionNumber(int samplePwSectionNumber) {
        this.samplePwSectionNumber = samplePwSectionNumber;
    }


//    public static byte[] getSetParamCode(SystemParam1Data paramData, byte[] mac) {
//        byte[] buf = new byte[12];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_1;
//        buf[1] = (byte) (paramData.isAutoModelEnable() ? 0x01 : 0x00);
//        buf[2] = (byte) (paramData.getAutoSampleInterval() & 0xFF);
//        buf[3] = (byte) ((paramData.getAutoSampleInterval() >> 8) & 0xFF);
//        buf[4] = (byte) (paramData.getAutoSampleTimeout() & 0xFF);
//        buf[5] = (byte) ((paramData.getAutoSampleTimeout() >> 8) & 0xFF);
//        buf[6] = (byte) (paramData.getSamplePwSectionNumber() & 0xFF);
//        buf[7] = (byte) (paramData.getSampleMinPwNumber() & 0xFF);
//        buf[8] = (byte) (paramData.getSampleMaxPwNumber() & 0xFF);
//        buf[9] = (byte) (paramData.getSampleMaxCompleteTime() & 0xFF);
//        buf[10] = (byte) (mac[0] ^ buf[7]);
//        buf[11] = (byte) (mac[1] ^ buf[8]);
//        return buf;
//    }

    @Override
    public String toString() {
        return "SystemParam1Data{" +
                "autoModelEnable=" + autoModelEnable +
                ", autoSampleInterval=" + autoSampleInterval +
                ", autoSampleTimeout=" + autoSampleTimeout +
                ", sampleMinPwNumber=" + sampleMinPwNumber +
                ", sampleMaxPwNumber=" + sampleMaxPwNumber +
                ", sampleMaxCompleteTime=" + sampleMaxCompleteTime +
                ", samplePwSectionNumber=" + samplePwSectionNumber +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (autoModelEnable ? 1 : 0));
        dest.writeInt(autoSampleInterval);
        dest.writeInt(autoSampleTimeout);
        dest.writeInt(sampleMinPwNumber);
        dest.writeInt(sampleMaxPwNumber);
        dest.writeInt(sampleMaxCompleteTime);
        dest.writeInt(samplePwSectionNumber);
    }
}
