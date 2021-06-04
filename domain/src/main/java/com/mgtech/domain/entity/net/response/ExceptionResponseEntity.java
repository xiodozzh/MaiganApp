package com.mgtech.domain.entity.net.response;

import java.util.List;

/**
 * Created by zhaixiang on 2017/3/9.
 * 获取异常提醒
 */

public class ExceptionResponseEntity extends ResponseEntity {
    private String noticeGuid;
    private long noticeTime;
    private String desc;
    private int isRead;
    private String measureGuid;
    private String remark;
    private long measureTime;
    private List<DataBean> data;

    public String getNoticeGuid() {
        return noticeGuid;
    }

    public void setNoticeGuid(String noticeGuid) {
        this.noticeGuid = noticeGuid;
    }

    public long getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(long noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ExceptionResponseEntity{" +
                "noticeGuid='" + noticeGuid + '\'' +
                ", noticeTime=" + noticeTime +
                ", desc='" + desc + '\'' +
                ", isRead=" + isRead +
                ", measureGuid='" + measureGuid + '\'' +
                ", remark='" + remark + '\'' +
                ", measureTime=" + measureTime +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * pwDataItemType : 0
         * itemName : 心率
         * abbreviation : HR
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
