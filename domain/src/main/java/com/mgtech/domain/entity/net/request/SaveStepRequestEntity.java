package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

/**
 * Created by zhaixiang on 2017/12/15.
 * 保存计步数据
 */

public class SaveStepRequestEntity implements RequestEntity{
    @SerializedName("data")
    private List<StepBean> items;
    @SerializedName("accountGuid")
    private String id;

    public SaveStepRequestEntity(List<StepBean> items, String id) {
        this.items = items;
        this.id = id;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SAVE_STEP_DATA;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public List<StepBean> getItems() {
        return items;
    }

    public void setItems(List<StepBean> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class StepBean {
        /**
         * requestDate : 1545027719807
         * duration : 1.0
         * distance : 9.226666
         * heat : 716.912
         * stepCount : 16.0
         */

        private long startTime;
        private long endTime;
        @SerializedName("activeDuration")
        private int duration;
        private float distance;
        private float heat;
        private int stepCount;


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

        public int getStepCount() {
            return stepCount;
        }

        public void setStepCount(int stepCount) {
            this.stepCount = stepCount;
        }

        @Override
        public String toString() {
            return "StepBean{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", duration=" + duration +
                    ", distance=" + distance +
                    ", heat=" + heat +
                    ", stepCount=" + stepCount +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SaveStepRequestEntity{" +
                "items=" + items +
                ", id='" + id + '\'' +
                '}';
    }
}
