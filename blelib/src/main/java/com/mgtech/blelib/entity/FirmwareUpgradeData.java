package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 *
 * @author zhaixiang
 * 固件升级
 */

public class FirmwareUpgradeData implements Parcelable {
    private int[] version;
    private int mirror;
    private byte[] content;
    private String fileName;


    public FirmwareUpgradeData() {
    }

    protected FirmwareUpgradeData(Parcel in) {
        version = in.createIntArray();
        mirror = in.readInt();
        content = in.createByteArray();
        fileName = in.readString();
    }

    public int[] getVersion() {
        return version;
    }

    public void setVersion(int[] version) {
        this.version = version;
    }

    public int getMirror() {
        return mirror;
    }

    public void setMirror(int mirror) {
        this.mirror = mirror;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(version);
        dest.writeInt(mirror);
        dest.writeByteArray(content);
        dest.writeString(fileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FirmwareUpgradeData> CREATOR = new Creator<FirmwareUpgradeData>() {
        @Override
        public FirmwareUpgradeData createFromParcel(Parcel in) {
            return new FirmwareUpgradeData(in);
        }

        @Override
        public FirmwareUpgradeData[] newArray(int size) {
            return new FirmwareUpgradeData[size];
        }
    };

    @Override
    public String toString() {
        return "FirmwareUpgradeData{" +
                "version=" + Arrays.toString(version) +
                ", mirror=" + mirror +
                ", content=" + Arrays.toString(content) +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
