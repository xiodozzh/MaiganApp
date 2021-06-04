package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.mgtech.blelib.biz.BluetoothConfig;


/**
 * Created by zhaixiang on 2017/12/7.
 * 同步的历史数据
 */
public class CurrentStepData implements Parcelable {
    private long hour;
    private int quarterOffset;
    private int quarterStep;
    private int quarterMinute;
    private int dayStep;
    private int dayMinute;

    public CurrentStepData() {
    }

    public byte[] getData() {
        byte[] buf = new byte[18];
        buf[0] = BluetoothConfig.CODE_CALIBRATE_STEP_DATA;
        buf[1] = (byte) (hour & 0xFF);
        buf[2] = (byte) ((hour >> 8) & 0xFF);
        buf[3] = (byte) ((hour >> 16) & 0xFF);
        buf[4] = (byte) ((hour >> 24) & 0xFF);
        buf[5] = (byte) quarterOffset;
        buf[6] = (byte) (quarterStep & 0xFF);
        buf[7] = (byte) ((quarterStep >> 8) & 0xFF);
        buf[8] = (byte) ((quarterStep >> 16) & 0xFF);
        buf[9] = (byte) (((quarterStep >> 24) & 0xF) + (quarterStep<<4));
        buf[10] = (byte) (dayStep & 0xFF);
        buf[11] = (byte) ((dayStep >> 8) & 0xFF);
        buf[12] = (byte) ((dayStep >> 16) & 0xFF);
        buf[13] = (byte) ((dayStep >> 24) & 0xFF);
        buf[14] = (byte) (dayMinute & 0xFF);
        buf[15] = (byte) ((dayMinute>>8) & 0xFF);
        buf[16] = (byte) (buf[3] ^ buf[4]);
        buf[17] = (byte) (buf[6] ^ buf[8]);
        return buf;
    }

    protected CurrentStepData(Parcel in) {
        hour = in.readLong();
        quarterOffset = in.readInt();
        quarterStep = in.readInt();
        quarterMinute = in.readInt();
        dayStep = in.readInt();
        dayMinute = in.readInt();
    }

    public static final Creator<CurrentStepData> CREATOR = new Creator<CurrentStepData>() {
        @Override
        public CurrentStepData createFromParcel(Parcel in) {
            return new CurrentStepData(in);
        }

        @Override
        public CurrentStepData[] newArray(int size) {
            return new CurrentStepData[size];
        }
    };

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public int getQuarterOffset() {
        return quarterOffset;
    }

    public void setQuarterOffset(int quarterOffset) {
        this.quarterOffset = quarterOffset;
    }

    public int getQuarterStep() {
        return quarterStep;
    }

    public void setQuarterStep(int quarterStep) {
        this.quarterStep = quarterStep;
    }


    public int getQuarterMinute() {
        return quarterMinute;
    }

    public void setQuarterMinute(int quarterMinute) {
        this.quarterMinute = quarterMinute;
    }

    public int getDayStep() {
        return dayStep;
    }

    public void setDayStep(int dayStep) {
        this.dayStep = dayStep;
    }

    public int getDayMinute() {
        return dayMinute;
    }

    public void setDayMinute(int dayMinute) {
        this.dayMinute = dayMinute;
    }

    @Override
    public String toString() {
        return "CurrentStepData{" +
                "hour=" + hour +
                ", quarterOffset=" + quarterOffset +
                ", quarterStep=" + quarterStep +
                ", quarterMinute=" + quarterMinute +
                ", dayStep=" + dayStep +
                ", dayMinute=" + dayMinute +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(hour);
        dest.writeInt(quarterOffset);
        dest.writeInt(quarterStep);
        dest.writeInt(quarterMinute);
        dest.writeInt(dayStep);
        dest.writeInt(dayMinute);
    }
}
