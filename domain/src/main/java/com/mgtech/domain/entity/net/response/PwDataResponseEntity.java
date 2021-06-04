package com.mgtech.domain.entity.net.response;

import java.util.List;

/**
 * @author zhaixiang
 */
public class PwDataResponseEntity {

    private String measureGuid;
    private String remark;
    private String rawDataFileId;
    private long measureTime;
    private int isAutoSampling;
    private List<PwDataBean> data;

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

    public List<PwDataBean> getData() {
        return data;
    }

    public void setData(List<PwDataBean> data) {
        this.data = data;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRawDataFileId() {
        return rawDataFileId;
    }

    public int getIsAutoSampling() {
        return isAutoSampling;
    }

    public void setIsAutoSampling(int isAutoSampling) {
        this.isAutoSampling = isAutoSampling;
    }

    public void setRawDataFileId(String rawDataFileId) {
        this.rawDataFileId = rawDataFileId;
    }

    @Override
    public String toString() {
        return "PwDataResponseEntity{" +
                "measureGuid='" + measureGuid + '\'' +
                ", remark='" + remark + '\'' +
                ", rawDataFileId='" + rawDataFileId + '\'' +
                ", measureTime=" + measureTime +
                ", isAutoSampling=" + isAutoSampling +
                ", data=" + data +
                '}';
    }

    public static class PwDataBean {
        /**
         * pwDataItemType : 0
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
            return "PwDataBean{" +
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
