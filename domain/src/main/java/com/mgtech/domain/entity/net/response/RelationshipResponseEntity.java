package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * @author zhaixiang
 * 获取好友列表
 */

public class RelationshipResponseEntity {
    private String relationGuid;
    private String targetAccountGuid;
    private int type;
    private String targetName;
    private String targetNoteName;
    private String targetAvatarUrl;
    private String phone;
    @SerializedName("allowOtherView")
    private int allowTargetRead;
    @SerializedName("allowPushToOther")
    private int allowPushToTarget;
    @SerializedName("whetherToViewData")
    private int readTarget;
    @SerializedName("whetherToReceivePushData")
    private int getTargetPush;
    @SerializedName("gender")
    private int sex;

    private LastResultBean lastResult;
    private int pwDataUnread;
    private int pwAbnormalDataUnread;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelationGuid() {
        return relationGuid;
    }

    public void setRelationGuid(String relationGuid) {
        this.relationGuid = relationGuid;
    }

    public String getTargetAccountGuid() {
        return targetAccountGuid;
    }

    public void setTargetAccountGuid(String targetAccountGuid) {
        this.targetAccountGuid = targetAccountGuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetNoteName() {
        return targetNoteName;
    }

    public void setTargetNoteName(String targetNoteName) {
        this.targetNoteName = targetNoteName;
    }

    public String getTargetAvatarUrl() {
        return targetAvatarUrl;
    }

    public void setTargetAvatarUrl(String targetAvatarUrl) {
        this.targetAvatarUrl = targetAvatarUrl;
    }

    public int getAllowTargetRead() {
        return allowTargetRead;
    }

    public void setAllowTargetRead(int allowTargetRead) {
        this.allowTargetRead = allowTargetRead;
    }

    public int getAllowPushToTarget() {
        return allowPushToTarget;
    }

    public void setAllowPushToTarget(int allowPushToTarget) {
        this.allowPushToTarget = allowPushToTarget;
    }

    public int getReadTarget() {
        return readTarget;
    }

    public void setReadTarget(int readTarget) {
        this.readTarget = readTarget;
    }

    public int getGetTargetPush() {
        return getTargetPush;
    }

    public void setGetTargetPush(int getTargetPush) {
        this.getTargetPush = getTargetPush;
    }

    public LastResultBean getLastResult() {
        return lastResult;
    }

    public void setLastResult(LastResultBean lastResult) {
        this.lastResult = lastResult;
    }

    public int getPwDataUnread() {
        return pwDataUnread;
    }

    public void setPwDataUnread(int pwDataUnread) {
        this.pwDataUnread = pwDataUnread;
    }

    public int getPwAbnormalDataUnread() {
        return pwAbnormalDataUnread;
    }

    public void setPwAbnormalDataUnread(int pwAbnormalDataUnread) {
        this.pwAbnormalDataUnread = pwAbnormalDataUnread;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "RelationshipResponseEntity{" +
                "relationGuid='" + relationGuid + '\'' +
                ", targetAccountGuid='" + targetAccountGuid + '\'' +
                ", type=" + type +
                ", targetName='" + targetName + '\'' +
                ", targetNoteName='" + targetNoteName + '\'' +
                ", targetAvatarUrl='" + targetAvatarUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", allowTargetRead=" + allowTargetRead +
                ", allowPushToTarget=" + allowPushToTarget +
                ", readTarget=" + readTarget +
                ", getTargetPush=" + getTargetPush +
                ", sex=" + sex +
                ", lastResult=" + lastResult +
                ", pwDataUnread=" + pwDataUnread +
                ", pwAbnormalDataUnread=" + pwAbnormalDataUnread +
                '}';
    }

    public static class LastResultBean {


        private String measureGuid;
        private long measureTime;
        private List<DataBean> data;

        public String getMeasureGuid() {
            return measureGuid;
        }

        public void setMeasureGuid(String measureGuid) {
            this.measureGuid = measureGuid;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "LastResultBean{" +
                    "measureGuid='" + measureGuid + '\'' +
                    ", measureTime=" + measureTime +
                    ", data=" + data +
                    '}';
        }

    }

    public static class DataBean {
        /**
         * pwDataItemType : 1
         * itemName : 心率
         * abbreviation : RANK_HR
         * value : 70.4252
         * unit : beat/min
         * resultOfAnalysis : 0
         * score : 86
         */

        private int pwDataItemType;
        private String itemName;
        private String abbreviation;
        private float value;
        private String unit;
        private int resultOfAnalysis;
        private int score;

        public int getPwDataItemType() {
            return pwDataItemType;
        }

        public void setPwDataItemType(int pwDataItemType) {
            this.pwDataItemType = pwDataItemType;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getResultOfAnalysis() {
            return resultOfAnalysis;
        }

        public void setResultOfAnalysis(int resultOfAnalysis) {
            this.resultOfAnalysis = resultOfAnalysis;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "pwDataItemType=" + pwDataItemType +
                    ", itemName='" + itemName + '\'' +
                    ", abbreviation='" + abbreviation + '\'' +
                    ", value=" + value +
                    ", unit='" + unit + '\'' +
                    ", resultOfAnalysis=" + resultOfAnalysis +
                    ", score=" + score +
                    '}';
        }
    }
}
