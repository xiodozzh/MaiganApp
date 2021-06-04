package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedicationRemindResponseEntity {

    private List<RemindBean> todayRemind;
    private List<RemindBean> futureRemind;

    public List<RemindBean> getTodayRemind() {
        return todayRemind;
    }

    public void setTodayRemind(List<RemindBean> todayRemind) {
        this.todayRemind = todayRemind;
    }

    public List<RemindBean> getFutureRemind() {
        return futureRemind;
    }

    public void setFutureRemind(List<RemindBean> futureRemind) {
        this.futureRemind = futureRemind;
    }

    public static class RemindBean {
        /**
         * planGuid :
         * drugName :
         * remindGuid :
         * dosage : {"time":"8:30","dosage":2,"dosageUnitType":1}
         * isTaken : 1
         * state : 0
         */

        private String planGuid;
        private String drugName;
        private String remindGuid;
        private DosageBean dosage;
        private int isTaken;
        private int state;

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

        public String getRemindGuid() {
            return remindGuid;
        }

        public void setRemindGuid(String remindGuid) {
            this.remindGuid = remindGuid;
        }

        public DosageBean getDosage() {
            return dosage;
        }

        public void setDosage(DosageBean dosage) {
            this.dosage = dosage;
        }

        public int getIsTaken() {
            return isTaken;
        }

        public void setIsTaken(int isTaken) {
            this.isTaken = isTaken;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "RemindBean{" +
                    "planGuid='" + planGuid + '\'' +
                    ", drugName='" + drugName + '\'' +
                    ", remindGuid='" + remindGuid + '\'' +
                    ", dosage=" + dosage +
                    ", isTaken=" + isTaken +
                    ", state=" + state +
                    '}';
        }
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

    @Override
    public String toString() {
        return "MedicationRemindResponseEntity{" +
                "todayRemind=" + todayRemind +
                ", futureRemind=" + futureRemind +
                '}';
    }
}
