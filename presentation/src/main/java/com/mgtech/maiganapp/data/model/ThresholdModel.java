package com.mgtech.maiganapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ThresholdModel implements Parcelable {
    public boolean open;
    public boolean openPs;
    public boolean openPd;
    public boolean openHr;
    public boolean openV0;

    public int psHigh;
    public int psLow;

    public int pdHigh;
    public int pdLow;

    public int hrHigh;
    public int hrLow;

    public int v0High;
    public int v0Low;

    public ThresholdModel() {
    }

    protected ThresholdModel(Parcel in) {
        open = in.readByte() != 0;
        openPs = in.readByte() != 0;
        openPd = in.readByte() != 0;
        openHr = in.readByte() != 0;
        openV0 = in.readByte() != 0;
        psHigh = in.readInt();
        psLow = in.readInt();
        pdHigh = in.readInt();
        pdLow = in.readInt();
        hrHigh = in.readInt();
        hrLow = in.readInt();
        v0High = in.readInt();
        v0Low = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (open ? 1 : 0));
        dest.writeByte((byte) (openPs ? 1 : 0));
        dest.writeByte((byte) (openPd ? 1 : 0));
        dest.writeByte((byte) (openHr ? 1 : 0));
        dest.writeByte((byte) (openV0 ? 1 : 0));
        dest.writeInt(psHigh);
        dest.writeInt(psLow);
        dest.writeInt(pdHigh);
        dest.writeInt(pdLow);
        dest.writeInt(hrHigh);
        dest.writeInt(hrLow);
        dest.writeInt(v0High);
        dest.writeInt(v0Low);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThresholdModel> CREATOR = new Creator<ThresholdModel>() {
        @Override
        public ThresholdModel createFromParcel(Parcel in) {
            return new ThresholdModel(in);
        }

        @Override
        public ThresholdModel[] newArray(int size) {
            return new ThresholdModel[size];
        }
    };
}
