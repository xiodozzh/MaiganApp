package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaixiang on 2017/12/15.
 * 步数
 */

public class StepDetailItemEntity {

    /**
     * startTime : 1545027719807
     * endTime : 12214354657
     * activeDuration : 1
     * distance : 9.226666
     * heat : 716.912
     * stepCount : 16
     */

    private long startTime;
    private long endTime;
    @SerializedName("activeDuration")
    private int duration;
    private float distance;
    private float heat;
    private float stepCount;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getHeat() {
        return heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public float getStepCount() {
        return stepCount;
    }

    public void setStepCount(float stepCount) {
        this.stepCount = stepCount;
    }
}
