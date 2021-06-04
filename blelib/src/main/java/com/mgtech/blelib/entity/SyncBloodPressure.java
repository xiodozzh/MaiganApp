package com.mgtech.blelib.entity;

import com.orhanobut.logger.Logger;

/**
 *
 * @author zhaixiang
 * @date 2018/1/8
 * 同步血压
 */

public class SyncBloodPressure{
    private long time;
    private int ps;
    private int pd;
    private float v0;
    private int psLevel;
    private int pdLevel;
    private int v0Level;

    public SyncBloodPressure(long time, int ps,int psLevel, int pd, int pdLevel, float v0, int v0Level) {
        this.time = time;
        this.ps = ps;
        this.pd = pd;
        this.v0 = v0;
        this.psLevel = psLevel;
        this.pdLevel = pdLevel;
        this.v0Level = v0Level;
        Logger.i("v0 " + v0);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPd() {
        return pd;
    }

    public void setPd(int pd) {
        this.pd = pd;
    }

    public float getV0() {
        return v0;
    }

    public void setV0(float v0) {
        this.v0 = v0;
    }

    public int getPsLevel() {
        return psLevel;
    }

    public void setPsLevel(int psLevel) {
        this.psLevel = psLevel;
    }

    public int getPdLevel() {
        return pdLevel;
    }

    public void setPdLevel(int pdLevel) {
        this.pdLevel = pdLevel;
    }

    public int getV0Level() {
        return v0Level;
    }

    public void setV0Level(int v0Level) {
        this.v0Level = v0Level;
    }


    @Override
    public String toString() {
        return "SyncBloodPressure{" +
                "time=" + time +
                ", ps=" + ps +
                ", pd=" + pd +
                ", v0=" + v0 +
                ", psLevel=" + psLevel +
                ", pdLevel=" + pdLevel +
                ", v0Level=" + v0Level +
                '}';
    }
}
