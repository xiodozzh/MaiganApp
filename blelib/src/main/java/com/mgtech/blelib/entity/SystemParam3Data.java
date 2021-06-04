package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by zhaixiang on 2017/2/18.
 * 获取系统参数
 */

public class SystemParam3Data implements Parcelable,ISystemParam {
    /**
     * 增益自适应使能
     */
    private boolean selfAmplificationEnable;

    /**
     * 测量脉图极值时需要识别的脉图数量
     */
    private int pwNumberToMeasurePeak;

    /**
     * 数字电位器基准值
     */
    private int baseOfDigital;


    public SystemParam3Data() {
    }


    protected SystemParam3Data(Parcel in) {
        selfAmplificationEnable = in.readByte() != 0;
        pwNumberToMeasurePeak = in.readInt();
        baseOfDigital = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (selfAmplificationEnable ? 1 : 0));
        dest.writeInt(pwNumberToMeasurePeak);
        dest.writeInt(baseOfDigital);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SystemParam3Data> CREATOR = new Creator<SystemParam3Data>() {
        @Override
        public SystemParam3Data createFromParcel(Parcel in) {
            return new SystemParam3Data(in);
        }

        @Override
        public SystemParam3Data[] newArray(int size) {
            return new SystemParam3Data[size];
        }
    };

    @Override
    public void setData(byte[] data) {
        if (data.length == 5) {
            this.selfAmplificationEnable = data[1] == 0x01;
            this.pwNumberToMeasurePeak = data[2] & 0xFF;
            this.baseOfDigital = (data[3] & 0xFF) + ((data[4] & 0xFF) << 8);
        }
    }

    @Override
    public byte[] getSetParamCode(byte[] mac, int pairCode) {
        byte[] buf = new byte[7];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_3;
        buf[1] = (byte) (this.isSelfAmplificationEnable() ? 0x01 : 0x00);
        buf[2] = (byte) (this.getPwNumberToMeasurePeak() & 0xFF);
        buf[3] = (byte) (this.getBaseOfDigital() & 0xFF);
        buf[4] = (byte) ((this.getBaseOfDigital() >> 8) & 0xFF);
        buf[5] = (byte) (mac[0] ^ buf[2]);
        buf[6] = (byte) (mac[1] ^ buf[4]);
        return buf;
    }

    @Override
    public String getSaveString() {
//        byte[] buf = new byte[5];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_3;
//        buf[1] = (byte) (this.isSelfAmplificationEnable() ? 0x01 : 0x00);
//        buf[2] = (byte) (this.getPwNumberToMeasurePeak() & 0xFF);
//        buf[3] = (byte) (this.getBaseOfDigital() & 0xFF);
//        buf[4] = (byte) ((this.getBaseOfDigital() >> 8) & 0xFF);
//        return buf;
        return new Gson().toJson(this);
    }

    public boolean isSelfAmplificationEnable() {
        return selfAmplificationEnable;
    }

    public void setSelfAmplificationEnable(boolean selfAmplificationEnable) {
        this.selfAmplificationEnable = selfAmplificationEnable;
    }

    public int getPwNumberToMeasurePeak() {
        return pwNumberToMeasurePeak;
    }

    public void setPwNumberToMeasurePeak(int pwNumberToMeasurePeak) {
        this.pwNumberToMeasurePeak = pwNumberToMeasurePeak;
    }

    public int getBaseOfDigital() {
        return baseOfDigital;
    }

    public void setBaseOfDigital(int baseOfDigital) {
        this.baseOfDigital = baseOfDigital;
    }


//    public static byte[] getSetParamCode(SystemParam3Data paramData, byte[] mac) {
//        byte[] buf = new byte[6];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_3;
//        buf[1] = (byte) (paramData.isSelfAmplificationEnable() ? 0x01 : 0x00);
//        buf[2] = (byte) (paramData.getPwNumberToMeasurePeak() & 0xFF);
//        buf[3] = (byte) (paramData.getBaseOfDigital() & 0xFF);
//        buf[4] = (byte) ((paramData.getBaseOfDigital() >> 8) & 0xFF);
//        buf[5] = (byte) (mac[0] ^ buf[2]);
//        buf[6] = (byte) (mac[1] ^ buf[4]);
//        return buf;
//    }

    @Override
    public String toString() {
        return "SystemParam3Data{" +
                "selfAmplificationEnable=" + selfAmplificationEnable +
                ", pwNumberToMeasurePeak=" + pwNumberToMeasurePeak +
                ", baseOfDigital=" + baseOfDigital +
                '}';
    }

}
