//package com.mgtech.blelib.net;
//
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.blelib.constant.NetConstant;
//
///**
// * Created by zhaixiang on 2017/12/15.
// * 步数
// */
//
//public class StepDetailItemEntity {
//    @SerializedName(NetConstant.JSON_START_TIME)
//    private String startTime;
//    @SerializedName(NetConstant.JSON_END_TIME)
//    private String endTime;
//    @SerializedName(NetConstant.JSON_DURATION)
//    private float duration;
//    @SerializedName(NetConstant.JSON_DISTANCE)
//    private float distance;
//    @SerializedName(NetConstant.JSON_HEAT)
//    private float heat;
//    @SerializedName(NetConstant.JSON_STEP_NUM)
//    private float stepNum;
//
//
//    public String getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
//
//    public float getDuration() {
//        return duration;
//    }
//
//    public void setDuration(float duration) {
//        this.duration = duration;
//    }
//
//    public float getDistance() {
//        return distance;
//    }
//
//    public void setDistance(float distance) {
//        this.distance = distance;
//    }
//
//    public float getHeat() {
//        return heat;
//    }
//
//    public void setHeat(float heat) {
//        this.heat = heat;
//    }
//
//    public float getStepNum() {
//        return stepNum;
//    }
//
//    public void setStepNum(float stepNum) {
//        this.stepNum = stepNum;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }
//
//    @Override
//    public String toString() {
//        return "StepDetailItemEntity{" +
//                "startTime='" + startTime + '\'' +
//                ", duration=" + duration +
//                ", distance=" + distance +
//                ", heat=" + heat +
//                ", stepNum=" + stepNum +
//                '}';
//    }
//}
