package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

import java.util.Arrays;
import java.util.List;

public class GetHomeDataResponseEntity {

    private LastTimePWDataBean lastTimePWData;
    private LastDayPWDataBean lastDayPWData;
    private LastTimeECGDataBean lastTimeECGData;
    private MedicationReminder todayMedicationRemind;

    public LastTimePWDataBean getLastTimePWData() {
        return lastTimePWData;
    }

    public void setLastTimePWData(LastTimePWDataBean lastTimePWData) {
        this.lastTimePWData = lastTimePWData;
    }

    public LastDayPWDataBean getLastDayPWData() {
        return lastDayPWData;
    }

    public void setLastDayPWData(LastDayPWDataBean lastDayPWData) {
        this.lastDayPWData = lastDayPWData;
    }

    public LastTimeECGDataBean getLastTimeECGData() {
        return lastTimeECGData;
    }

    public void setLastTimeECGData(LastTimeECGDataBean lastTimeECGData) {
        this.lastTimeECGData = lastTimeECGData;
    }

    public MedicationReminder getMedicationRemind() {
        return todayMedicationRemind;
    }

    public void setMedicationRemind(MedicationReminder medicationRemind) {
        this.todayMedicationRemind = medicationRemind;
    }

    @Override
    public String toString() {
        return "GetHomeDataResponseEntity{" +
                "lastTimePWData=" + lastTimePWData +
                ", lastDayPWData=" + lastDayPWData +
                ", lastTimeECGData=" + lastTimeECGData +
                ", medicationRemind=" + todayMedicationRemind +
                '}';
    }

    public static class LastTimePWDataBean {
        private String measureGuid;
        private long measureTime;
        private List<LastDataBean> data;

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

        public List<LastDataBean> getData() {
            return data;
        }

        public void setData(List<LastDataBean> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "LastTimePWDataBean{" +
                    "measureGuid='" + measureGuid + '\'' +
                    ", measureTime=" + measureTime +
                    ", data=" + data +
                    '}';
        }
    }

    public static class LastDataBean {
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
            return "LastDataBean{" +
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

    public static class LastDayPWDataBean {
        private long date;
        private ParamInfoBean paramInfos;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public ParamInfoBean getParamInfos() {
            return paramInfos;
        }

        public void setParamInfos(ParamInfoBean paramInfos) {
            this.paramInfos = paramInfos;
        }

        @Override
        public String toString() {
            return "LastDayPWDataBean{" +
                    "date=" + date +
                    ", paramInfos=" + paramInfos +
                    '}';
        }
    }

    public static class ParamInfoBean {
        @SerializedName(NetConstant.PS + "")
        private List<DataBean> ps;
        @SerializedName(NetConstant.PD + "")
        private List<DataBean> pd;

        public List<DataBean> getPs() {
            return ps;
        }

        public void setPs(List<DataBean> ps) {
            this.ps = ps;
        }

        public List<DataBean> getPd() {
            return pd;
        }

        public void setPd(List<DataBean> pd) {
            this.pd = pd;
        }

        @Override
        public String toString() {
            return "ParamInfoBean{" +
                    "ps=" + ps +
                    ", pd=" + pd +
                    '}';
        }
    }

    public static class LastTimeECGDataBean {
        /**
         * measureTime : 12131434345
         * rawData : [113214,132435,14234]
         */

        private long measureTime;
        private float[] rawData;

        public long getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public float[] getRawData() {
            return rawData;
        }

        public void setRawData(float[] rawData) {
            this.rawData = rawData;
        }

        @Override
        public String toString() {
            return "LastTimeECGDataBean{" +
                    "measureTime=" + measureTime +
                    ", rawData=" + Arrays.toString(rawData) +
                    '}';
        }
    }

    public static class MedicationReminder{

        /**
         * nextTime : 9:15
         * nextTimeDrugVariety : 5
         * futureRemindCount : 10
         * forgetRemindCount : 2
         */

        private String nextTime;
        private int nextTimeDrugVariety;
        private int futureRemindCount;
        private int forgetRemindCount;

        public String getNextTime() {
            return nextTime;
        }

        public void setNextTime(String nextTime) {
            this.nextTime = nextTime;
        }

        public int getNextTimeDrugVariety() {
            return nextTimeDrugVariety;
        }

        public void setNextTimeDrugVariety(int nextTimeDrugVariety) {
            this.nextTimeDrugVariety = nextTimeDrugVariety;
        }

        public int getFutureRemindCount() {
            return futureRemindCount;
        }

        public void setFutureRemindCount(int futureRemindCount) {
            this.futureRemindCount = futureRemindCount;
        }

        public int getForgetRemindCount() {
            return forgetRemindCount;
        }

        public void setForgetRemindCount(int forgetRemindCount) {
            this.forgetRemindCount = forgetRemindCount;
        }

        @Override
        public String toString() {
            return "MedicationReminder{" +
                    "nextTime='" + nextTime + '\'' +
                    ", nextTimeDrugVariety=" + nextTimeDrugVariety +
                    ", futureRemindCount=" + futureRemindCount +
                    ", forgetRemindCount=" + forgetRemindCount +
                    '}';
        }
    }


    public static class DataBean {
        /**
         * value : 71.1612
         * date : 121312424234
         */

        private float value;
        private long date;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "value=" + value +
                    ", date=" + date +
                    '}';
        }
    }
}
