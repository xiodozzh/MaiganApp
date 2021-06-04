package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

/**
 * @author zhaixiang
 */
public class GetHealthManagementResponseEntity {
    private int psGoal;
    private int pdGoal;
    private String suggest;
    private int normalDayCount;
    private int abnormalDayCount;
    private PwDataBean pwData;

    public int getPsGoal() {
        return psGoal;
    }

    public void setPsGoal(int psGoal) {
        this.psGoal = psGoal;
    }

    public int getPdGoal() {
        return pdGoal;
    }

    public void setPdGoal(int pdGoal) {
        this.pdGoal = pdGoal;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public int getNormalDayCount() {
        return normalDayCount;
    }

    public void setNormalDayCount(int normalDayCount) {
        this.normalDayCount = normalDayCount;
    }

    public int getAbnormalDayCount() {
        return abnormalDayCount;
    }

    public void setAbnormalDayCount(int abnormalDayCount) {
        this.abnormalDayCount = abnormalDayCount;
    }

    public PwDataBean getPwData() {
        return pwData;
    }

    public void setPwData(PwDataBean pwData) {
        this.pwData = pwData;
    }

    @Override
    public String toString() {
        return "GetHealthManagementResponseEntity{" +
                "psGoal=" + psGoal +
                ", pdGoal=" + pdGoal +
                ", suggest='" + suggest + '\'' +
                ", normalDayCount=" + normalDayCount +
                ", abnormalDayCount=" + abnormalDayCount +
                ", pwData=" + pwData +
                '}';
    }

    public static class PwDataBean {
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
            return "PwDataBean{" +
                    "ps=" + ps +
                    ", pd=" + pd +
                    '}';
        }
    }

    public static class DataBean {
        /**
         * date : 12143142434
         * value : 132.1522
         * resultOfAnalysis : 0
         */

        private long date;
        private float value;
        private int isAchieveGoal;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public int getIsAchieveGoal() {
            return isAchieveGoal;
        }

        public void setIsAchieveGoal(int isAchieveGoal) {
            this.isAchieveGoal = isAchieveGoal;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "date=" + date +
                    ", value='" + value + '\'' +
                    ", isAchieveGoal=" + isAchieveGoal +
                    '}';
        }
    }
}
