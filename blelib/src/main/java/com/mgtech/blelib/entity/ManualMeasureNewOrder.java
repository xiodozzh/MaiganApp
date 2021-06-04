package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.mgtech.blelib.biz.BluetoothConfig;

/**
 * Created by zhaixiang on 2017/3/31.
 * 主动采样命令
 */

public class ManualMeasureNewOrder implements Parcelable{
    public static final int PW = 0;
    public static final int ECG = 1;
    public static final int POINTS = 6;
    public static final int COMPLETE_POINTS = 32;
    public static final int PW_SAMPLE_RATE = 128;
    public static final int ECG_SAMPLE_RATE = 512;
//    public static final int ECG_POINTS = 64;
    public static final int ECG_POINTS = 32;
    public static final int MEASURE_STATE = 1;
    public static final int TAG_PRE = 0;
    public static final int TAG_REAL_DATA = 1;
    public static final int TAG_ECG_PRE = 2;
    public static final int TAG_ECG_REAL_DATA = 3;
    public static final int TAG_AUTO = 4;
    public static final int TAG_PW_RAW_DATA = 5;

    public static final int accEnable = 1;
    public static final int incrementEnable = 1<<1;
    public static final int phaseChangeEnable = 1<<2;
    public static final int recognizeEnable = 1<<3;

    private int type;
    private int tag;
    private int enable;
    private int rateType;
    private int rateValue;
    private boolean regionAvailable;
    private int regionMax;
    private int regionMin;
    private int realTimeDoublePointNumber;
    private int resultDoublePointNumber = 12;

    public ManualMeasureNewOrder(){}

    protected ManualMeasureNewOrder(Parcel in) {
        type = in.readInt();
        tag = in.readInt();
        enable = in.readInt();
        rateType = in.readInt();
        rateValue = in.readInt();
        regionAvailable = in.readByte() != 0;
        regionMax = in.readInt();
        regionMin = in.readInt();
        realTimeDoublePointNumber = in.readInt();
        resultDoublePointNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(tag);
        dest.writeInt(enable);
        dest.writeInt(rateType);
        dest.writeInt(rateValue);
        dest.writeByte((byte) (regionAvailable ? 1 : 0));
        dest.writeInt(regionMax);
        dest.writeInt(regionMin);
        dest.writeInt(realTimeDoublePointNumber);
        dest.writeInt(resultDoublePointNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ManualMeasureNewOrder> CREATOR = new Creator<ManualMeasureNewOrder>() {
        @Override
        public ManualMeasureNewOrder createFromParcel(Parcel in) {
            return new ManualMeasureNewOrder(in);
        }

        @Override
        public ManualMeasureNewOrder[] newArray(int size) {
            return new ManualMeasureNewOrder[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
        if ((enable & recognizeEnable)== recognizeEnable){
            this.tag = TAG_AUTO;
        }
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
    }

    public boolean isRegionAvailable() {
        return regionAvailable;
    }

    public void setRegionAvailable(boolean regionAvailable) {
        this.regionAvailable = regionAvailable;
    }

    public int getRegionMax() {
        return regionMax;
    }

    public void setRegionMax(int regionMax) {
        this.regionMax = regionMax;
    }

    public int getRegionMin() {
        return regionMin;
    }

    public void setRegionMin(int regionMin) {
        this.regionMin = regionMin;
    }

    public int getRealTimeDoublePointNumber() {
        return realTimeDoublePointNumber;
    }

    public void setRealTimeDoublePointNumber(int realTimeDoublePointNumber) {
        this.realTimeDoublePointNumber = realTimeDoublePointNumber;
    }

    public int getResultDoublePointNumber() {
        return resultDoublePointNumber;
    }

    public void setResultDoublePointNumber(int resultDoublePointNumber) {
        this.resultDoublePointNumber = resultDoublePointNumber;
    }

//    public void setMeasure(boolean isMeasuring){
//        isMeasuring ?tag = MEASURE_STATE:tag = 0;
//    }
//
//    public boolean isMeasure(){
//        return tag == MEASURE_STATE;
//    }

    public boolean isAuto(){
        return (enable & recognizeEnable) == recognizeEnable;
    }

    @Override
    public String toString() {
        return "ManualMeasureNewOrder{" +
                "type=" + type +
                ", tag=" + tag +
                ", enable=" + enable +
                ", rateType=" + rateType +
                ", rateValue=" + rateValue +
                ", regionAvailable=" + regionAvailable +
                ", regionMax=" + regionMax +
                ", regionMin=" + regionMin +
                ", realTimeDoublePointNumber=" + realTimeDoublePointNumber +
                ", resultDoublePointNumber=" + resultDoublePointNumber +
                '}';
    }

    public static byte[] getManualMeasureCode(ManualMeasureNewOrder manualMeasureOrder){
        byte[] buf = new byte[15];
        buf[0] = BluetoothConfig.CODE_START_MANUAL_SAMPLE;
        buf[1] = (byte) (manualMeasureOrder.getTag() & 0xFF);
        buf[2] = (byte) (manualMeasureOrder.getType() & 0xFF);
        buf[3] = (byte) (manualMeasureOrder.getEnable() & 0xFF);
        buf[4] = (byte) ((manualMeasureOrder.getRateType() ) & 0xFF);
        buf[5] = (byte) (manualMeasureOrder.getRateValue()& 0xFF);
        buf[6] = (byte) ((manualMeasureOrder.getRateValue() >>8) & 0xFF);
        buf[7] = (byte) (manualMeasureOrder.isRegionAvailable() ? 1 : 0);
        buf[8] = (byte) ((manualMeasureOrder.getRegionMin()) & 0xFF);
        buf[9] = (byte) ((manualMeasureOrder.getRegionMin() >> 8) & 0xFF);
        buf[10] = (byte) (manualMeasureOrder.getRegionMax() & 0xFF);
        buf[11] = (byte) ((manualMeasureOrder.getRegionMax() >> 8) & 0xFF);
        buf[12] = (byte) (manualMeasureOrder.getRealTimeDoublePointNumber());
        buf[13] = (byte) (manualMeasureOrder.getResultDoublePointNumber());
        buf[14] = (byte) (buf[3]^buf[10]);
        return buf;
    }
}
