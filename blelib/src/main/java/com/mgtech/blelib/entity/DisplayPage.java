package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author zhaixiang
 * 获取系统参数
 */

public class DisplayPage implements Parcelable {

    public static final int ON = 1;
    public static final int OFF = 0;

    public static final int DATE_SIMPLE = 2;
    public static final int DATE_COMPLEX = 1;

    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;

    /**
     * 语言
     */
    private int lang = CHINESE;

    /**
     * 单位 斤
     */
    private int datePageDisplay = DATE_SIMPLE;
    private int bpPageDisplay = OFF;
    private int v0PageDisplay = OFF;
    private int stepPageDisplay = OFF;
    private int distancePageDisplay = OFF;
    private int heatPageDisplay = OFF;
    private int powerPageDisplay = OFF;

    public DisplayPage() {
    }

    protected DisplayPage(Parcel in) {
        lang = in.readInt();
        datePageDisplay = in.readInt();
        bpPageDisplay = in.readInt();
        v0PageDisplay = in.readInt();
        stepPageDisplay = in.readInt();
        distancePageDisplay = in.readInt();
        heatPageDisplay = in.readInt();
        powerPageDisplay = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lang);
        dest.writeInt(datePageDisplay);
        dest.writeInt(bpPageDisplay);
        dest.writeInt(v0PageDisplay);
        dest.writeInt(stepPageDisplay);
        dest.writeInt(distancePageDisplay);
        dest.writeInt(heatPageDisplay);
        dest.writeInt(powerPageDisplay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DisplayPage> CREATOR = new Creator<DisplayPage>() {
        @Override
        public DisplayPage createFromParcel(Parcel in) {
            return new DisplayPage(in);
        }

        @Override
        public DisplayPage[] newArray(int size) {
            return new DisplayPage[size];
        }
    };

    /**
     * 清除信息，只保留身高体重
     */
    public void clear() {
        datePageDisplay = DATE_SIMPLE;
        bpPageDisplay = ON;
        stepPageDisplay = ON;
        distancePageDisplay = ON;
        heatPageDisplay = ON;
        powerPageDisplay = ON;
        v0PageDisplay = ON;
    }




    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }


    public int getDatePageDisplay() {
        return datePageDisplay;
    }

    public void setDatePageDisplay(int datePageDisplay) {
        this.datePageDisplay = datePageDisplay;
    }

    public int getBpPageDisplay() {
        return bpPageDisplay;
    }

    public void setBpPageDisplay(int bpPageDisplay) {
        this.bpPageDisplay = bpPageDisplay;
    }

    public int getStepPageDisplay() {
        return stepPageDisplay;
    }

    public void setStepPageDisplay(int stepPageDisplay) {
        this.stepPageDisplay = stepPageDisplay;
    }

    public int getDistancePageDisplay() {
        return distancePageDisplay;
    }

    public void setDistancePageDisplay(int distancePageDisplay) {
        this.distancePageDisplay = distancePageDisplay;
    }

    public int getHeatPageDisplay() {
        return heatPageDisplay;
    }

    public void setHeatPageDisplay(int heatPageDisplay) {
        this.heatPageDisplay = heatPageDisplay;
    }

    public int getPowerPageDisplay() {
        return powerPageDisplay;
    }

    public void setPowerPageDisplay(int powerPageDisplay) {
        this.powerPageDisplay = powerPageDisplay;
    }

    public int getV0PageDisplay() {
        return v0PageDisplay;
    }

    public void setV0PageDisplay(int v0PageDisplay) {
        this.v0PageDisplay = v0PageDisplay;
    }


    public boolean isDifferentIgnorePersonalInfo(DisplayPage other){
        if (other == null){
            return true;
        }
        return !(powerPageDisplay == other.getPowerPageDisplay() &&
                heatPageDisplay == other.getHeatPageDisplay() &&
                distancePageDisplay == other.getDistancePageDisplay() &&
                stepPageDisplay == other.getStepPageDisplay() &&
                lang == other.getLang() &&
                v0PageDisplay == other.getV0PageDisplay() &&
                bpPageDisplay == other.getBpPageDisplay() &&
                datePageDisplay == other.getDatePageDisplay());
    }

    public boolean isSet(){
        return !(powerPageDisplay == ON &&
                heatPageDisplay == OFF &&
                distancePageDisplay == OFF &&
                stepPageDisplay == OFF &&
                v0PageDisplay == OFF &&
                bpPageDisplay == OFF &&
                datePageDisplay == DATE_SIMPLE
        );
    }


    @Override
    public String toString() {
        return "DisplayPage{" +
                "lang=" + lang +
                ", datePageDisplay=" + datePageDisplay +
                ", bpPageDisplay=" + bpPageDisplay +
                ", v0PageDisplay=" + v0PageDisplay +
                ", stepPageDisplay=" + stepPageDisplay +
                ", distancePageDisplay=" + distancePageDisplay +
                ", heatPageDisplay=" + heatPageDisplay +
                ", powerPageDisplay=" + powerPageDisplay +
                '}';
    }

}
