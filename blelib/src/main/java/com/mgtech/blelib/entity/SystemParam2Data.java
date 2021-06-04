package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by zhaixiang on 2017/2/18.
 * 获取系统参数
 */

public class SystemParam2Data implements Parcelable ,ISystemParam{

    /**
     * 采样率自适应使能
     */
    private boolean sampleSelfFitMode;
    /**
     * 采样率自适应基准心率
     */
    private int sampleSelfFitHeartRate;
    /**
     * 采样率自适应基准类型
     */
    private int sampleSelfFitType;
    /**
     * 采样率自适应基准采样率
     */
    private int sampleSelfFitValue;
    /**
     * 测量心率时脉图数量
     */
    private int samplePwNumber;


    public SystemParam2Data() {
    }

    protected SystemParam2Data(Parcel in) {
        sampleSelfFitMode = in.readByte() != 0;
        sampleSelfFitHeartRate = in.readInt();
        sampleSelfFitType = in.readInt();
        sampleSelfFitValue = in.readInt();
        samplePwNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (sampleSelfFitMode ? 1 : 0));
        dest.writeInt(sampleSelfFitHeartRate);
        dest.writeInt(sampleSelfFitType);
        dest.writeInt(sampleSelfFitValue);
        dest.writeInt(samplePwNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SystemParam2Data> CREATOR = new Creator<SystemParam2Data>() {
        @Override
        public SystemParam2Data createFromParcel(Parcel in) {
            return new SystemParam2Data(in);
        }

        @Override
        public SystemParam2Data[] newArray(int size) {
            return new SystemParam2Data[size];
        }
    };

    public boolean isSampleSelfFitMode() {
        return sampleSelfFitMode;
    }

    public void setSampleSelfFitMode(boolean sampleSelfFitMode) {
        this.sampleSelfFitMode = sampleSelfFitMode;
    }

    public int getSampleSelfFitHeartRate() {
        return sampleSelfFitHeartRate;
    }

    public void setSampleSelfFitHeartRate(int sampleSelfFitHeartRate) {
        this.sampleSelfFitHeartRate = sampleSelfFitHeartRate;
    }

    public int getSampleSelfFitType() {
        return sampleSelfFitType;
    }

    public void setSampleSelfFitType(int sampleSelfFitType) {
        this.sampleSelfFitType = sampleSelfFitType;
    }

    public int getSampleSelfFitValue() {
        return sampleSelfFitValue;
    }

    public void setSampleSelfFitValue(int sampleSelfFitValue) {
        this.sampleSelfFitValue = sampleSelfFitValue;
    }

    public int getSamplePwNumber() {
        return samplePwNumber;
    }

    public void setSamplePwNumber(int samplePwNumber) {
        this.samplePwNumber = samplePwNumber;
    }

    @Override
    public void setData(byte[] data) {
        if (data.length == 7) {
            this.sampleSelfFitMode = data[1] == 0x01;
            this.sampleSelfFitHeartRate = data[2] & 0xFF;
            this.sampleSelfFitType = data[3] & 0xFF;
            this.sampleSelfFitValue = (data[4] & 0xFF) + ((data[5] & 0xFF) << 8);
            this.samplePwNumber = data[6] & 0xFF;
        }
    }

    @Override
    public byte[] getSetParamCode(byte[] mac, int pairCode) {
        byte[] buf = new byte[9];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_2;
//        buf[1] = (byte) (this.isSampleSelfFitMode() ? 0x01 : 0x00);
//        buf[2] = (byte) (this.getSampleSelfFitHeartRate() & 0xFF);
//        buf[3] = (byte) (this.getSampleSelfFitType() & 0xFF);
//        buf[4] = (byte) (this.getSampleSelfFitValue() & 0xFF);
//        buf[5] = (byte) ((this.getSampleSelfFitValue() >> 8) & 0xFF);
//        buf[6] = (byte) (this.getSamplePwNumber() & 0xFF);
//        buf[7] = (byte) (mac[0] ^ buf[2]);
//        buf[8] = (byte) (mac[1] ^ buf[3]);
        return buf;
    }

    @Override
    public String getSaveString() {
//        byte[] buf = new byte[7];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_2;
//        buf[1] = (byte) (this.isSampleSelfFitMode() ? 0x01 : 0x00);
//        buf[2] = (byte) (this.getSampleSelfFitHeartRate() & 0xFF);
//        buf[3] = (byte) (this.getSampleSelfFitType() & 0xFF);
//        buf[4] = (byte) (this.getSampleSelfFitValue() & 0xFF);
//        buf[5] = (byte) ((this.getSampleSelfFitValue() >> 8) & 0xFF);
//        buf[6] = (byte) (this.getSamplePwNumber() & 0xFF);
//        return buf;
        return new Gson().toJson(this);
    }



    @Override
    public String toString() {
        return "SystemParam2Data{" +
                "sampleSelfFitMode=" + sampleSelfFitMode +
                ", sampleSelfFitHeartRate=" + sampleSelfFitHeartRate +
                ", sampleSelfFitType=" + sampleSelfFitType +
                ", sampleSelfFitValue=" + sampleSelfFitValue +
                ", samplePwNumber=" + samplePwNumber +
                '}';
    }
}
