package com.mgtech.domain.entity.net.response;

/**
 * Created by zhaixiang on 2017/12/15.
 * 分页查询
 */

public class StepStatisticsItemEntity {

    /**
     * startTime : 2018-02-01 01:00:00
     * endTime : 2018-02-01 02:00:00
     * duration : 1.0
     * distance : 9.226666
     * heat : 716.912
     * stepCount : 16.0
     * count : 10
     */

    private long startTime;
    private long endTime;
    private int duration;
    private double distance;
    private double heat;
    private int stepCount;
    private int count;

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    @Override
    public String toString() {
        return "StepStatisticsItemEntity{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration=" + duration +
                ", distance=" + distance +
                ", heat=" + heat +
                ", stepCount=" + stepCount +
                ", count=" + count +
                '}';
    }

    public void setEntity(StepStatisticsItemEntity entity) {
        startTime = entity.startTime;
        endTime = entity.endTime;
        duration = entity.duration;
        distance = entity.distance;
        heat = entity.heat;
        stepCount = entity.stepCount;
        count = entity.count;

    }
}
