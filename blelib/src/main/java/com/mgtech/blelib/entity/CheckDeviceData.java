package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2018/3/8.
 * 检查手环设备正版与否
 */

public class CheckDeviceData implements Parcelable{
    private String id;
    private String mac;
    private byte[] key;
    private byte[] vector;

    public CheckDeviceData(String id, String mac, byte[] key, byte[] vector) {
        this.id = id;
        this.mac = mac;
        this.key = key;
        this.vector = vector;
    }

    protected CheckDeviceData(Parcel in) {
        id = in.readString();
        mac = in.readString();
        key = in.createByteArray();
        vector = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mac);
        dest.writeByteArray(key);
        dest.writeByteArray(vector);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckDeviceData> CREATOR = new Creator<CheckDeviceData>() {
        @Override
        public CheckDeviceData createFromParcel(Parcel in) {
            return new CheckDeviceData(in);
        }

        @Override
        public CheckDeviceData[] newArray(int size) {
            return new CheckDeviceData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getVector() {
        return vector;
    }

    public void setVector(byte[] vector) {
        this.vector = vector;
    }


    @Override
    public String toString() {
        return "CheckDeviceData{" +
                "id='" + id + '\'' +
                ", mac='" + mac + '\'' +
                ", key=" + Arrays.toString(key) +
                ", vector=" + Arrays.toString(vector) +
                '}';
    }
}
