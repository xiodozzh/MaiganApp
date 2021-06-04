package com.mgtech.domain.entity.net.response;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhaixiang on 2017/8/23.
 * 保存ECG
 */

public class EcgResponseEntity {

    /**
     * measureGuid :
     * measureTime : 0
     * result : {"hrValue":78,"resultOfHr":0,"resultOfEcg":0}
     * rawData : [90,1234,3214,1232]
     */

    private String measureGuid;
    private long measureTime;
    private float sampleRate;
    private ResultBean result;
    private float[] rawData;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public float[] getRawData() {
        return rawData;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setRawData(float[] rawData) {
        this.rawData = rawData;
    }

    public static class ResultBean {
        /**
         * hrValue : 78
         * resultOfHr : 0
         * resultOfEcg : 0
         */

        private float hrValue;
        private int resultOfHr;
        private int resultOfEcg;

        public float getHrValue() {
            return hrValue;
        }

        public void setHrValue(float hrValue) {
            this.hrValue = hrValue;
        }

        public int getResultOfHr() {
            return resultOfHr;
        }

        public void setResultOfHr(int resultOfHr) {
            this.resultOfHr = resultOfHr;
        }

        public int getResultOfEcg() {
            return resultOfEcg;
        }

        public void setResultOfEcg(int resultOfEcg) {
            this.resultOfEcg = resultOfEcg;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "hrValue=" + hrValue +
                    ", resultOfHr=" + resultOfHr +
                    ", resultOfEcg=" + resultOfEcg +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EcgResponseEntity{" +
                "measureGuid='" + measureGuid + '\'' +
                ", measureTime=" + measureTime +
                ", result=" + result +
                ", rawData=" + Arrays.toString(rawData) +
                '}';
    }
}
