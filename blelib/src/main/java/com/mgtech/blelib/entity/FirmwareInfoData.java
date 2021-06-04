package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2016/12/22.
 * 自动采谱数据
 */

public class FirmwareInfoData implements Parcelable{
    public byte mirror;
    public int[] version;

    public FirmwareInfoData(){}

    protected FirmwareInfoData(Parcel in) {
        mirror = in.readByte();
        version = in.createIntArray();
    }

    public static final Creator<FirmwareInfoData> CREATOR = new Creator<FirmwareInfoData>() {
        @Override
        public FirmwareInfoData createFromParcel(Parcel in) {
            return new FirmwareInfoData(in);
        }

        @Override
        public FirmwareInfoData[] newArray(int size) {
            return new FirmwareInfoData[size];
        }
    };

    @Override
    public String toString() {
        return "FirmwareInfoData{" +
                "mirror=" + mirror +
                ", version=" + Arrays.toString(version) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(mirror);
        dest.writeIntArray(version);
    }
}
