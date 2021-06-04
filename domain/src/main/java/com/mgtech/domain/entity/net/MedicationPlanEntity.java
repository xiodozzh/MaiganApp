package com.mgtech.domain.entity.net;

import com.google.gson.Gson;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;
import java.util.Map;

/**
 * @author zhaixiang
 */
public class MedicationPlanEntity implements RequestEntity {

    /**
     * planGuid :
     * drugName : 感冒胶囊
     * drugGuid :
     * startDate : 2019-09-01
     * endDate : 2019-09-07
     * repeatType : 0
     * repeatPeriod : 2
     * dosage : {"1":[{"time":"8:30","dosage":2,"dosageUnitType":1}],"2":[{"time":"8:30","dosage":2,"dosageUnitType":1}]}
     * completedNumber : 12
     * forgetNumber : 12
     * totalNumber : 24
     */
    private String userId;
    private String planGuid;
    private String drugName;
    private String drugGuid;
    private String startDate;
    private String endDate;
    private int repeatType = 0;
    private int repeatPeriod;
    private Map<String,List<DosageBean>> dosage;
    private int completedNumber;
    private int forgetNumber;
    private int totalNumber;
    private int ignoreNumber;
    private int isCustom;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanGuid() {
        return planGuid;
    }

    public void setPlanGuid(String planGuid) {
        this.planGuid = planGuid;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugGuid() {
        return drugGuid;
    }

    public void setDrugGuid(String drugGuid) {
        this.drugGuid = drugGuid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public int getRepeatPeriod() {
        return repeatPeriod;
    }

    public void setRepeatPeriod(int repeatPeriod) {
        this.repeatPeriod = repeatPeriod;
    }

    public Map<String, List<DosageBean>> getDosage() {
        return dosage;
    }

    public void setDosage(Map<String, List<DosageBean>> dosage) {
        this.dosage = dosage;
    }

    public int getCompletedNumber() {
        return completedNumber;
    }

    public void setCompletedNumber(int completedNumber) {
        this.completedNumber = completedNumber;
    }

    public int getForgetNumber() {
        return forgetNumber;
    }

    public void setForgetNumber(int forgetNumber) {
        this.forgetNumber = forgetNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getIgnoreNumber() {
        return ignoreNumber;
    }

    public void setIgnoreNumber(int ignoreNumber) {
        this.ignoreNumber = ignoreNumber;
    }

    public int getCustom() {
        return isCustom;
    }

    public void setCustom(int custom) {
        this.isCustom = custom;
    }

    @Override
    public String toString() {
        return "MedicationPlanEntity{" +
                "userId='" + userId + '\'' +
                ", planGuid='" + planGuid + '\'' +
                ", drugName='" + drugName + '\'' +
                ", drugGuid='" + drugGuid + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", repeatType=" + repeatType +
                ", repeatPeriod=" + repeatPeriod +
                ", dosage=" + dosage +
                ", completedNumber=" + completedNumber +
                ", forgetNumber=" + forgetNumber +
                ", totalNumber=" + totalNumber +
                ", ignoreNumber=" + ignoreNumber +
                ", isCustom=" + isCustom +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_ADD_MEDICATION_PLAN;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public static class DosageBean {
        /**
         * time : 8:30
         * dosage : 2.0
         * dosageUnitType : 1
         */

        private String time;
        private float dosage;
        private int dosageUnitType;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getDosage() {
            return dosage;
        }

        public void setDosage(float dosage) {
            this.dosage = dosage;
        }

        public int getDosageUnitType() {
            return dosageUnitType;
        }

        public void setDosageUnitType(int dosageUnitType) {
            this.dosageUnitType = dosageUnitType;
        }

        @Override
        public String toString() {
            return "DosageBean{" +
                    "time='" + time + '\'' +
                    ", dosage=" + dosage +
                    ", dosageUnitType=" + dosageUnitType +
                    '}';
        }
    }
}
