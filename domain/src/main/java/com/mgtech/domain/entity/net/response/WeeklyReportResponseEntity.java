package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

public class WeeklyReportResponseEntity {
    public static final int UNREAD = 0;

    private String weekReportGuid;
    private String weekReportDetailUrl;
    private String shareTitle;
    private String shareContent;
    private long startTime;
    private long endTime;
    private PwDataBean pwData;
    private int isRead = 1;

    public String getWeekReportGuid() {
        return weekReportGuid;
    }

    public void setWeekReportGuid(String weekReportGuid) {
        this.weekReportGuid = weekReportGuid;
    }

    public String getWeekReportDetailUrl() {
        return weekReportDetailUrl;
    }

    public void setWeekReportDetailUrl(String weekReportDetailUrl) {
        this.weekReportDetailUrl = weekReportDetailUrl;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

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

    public PwDataBean getPwData() {
        return pwData;
    }

    public void setPwData(PwDataBean pwData) {
        this.pwData = pwData;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "WeeklyReportResponseEntity{" +
                "weekReportGuid='" + weekReportGuid + '\'' +
                ", weekReportDetailUrl='" + weekReportDetailUrl + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareContent='" + shareContent + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pwData=" + pwData +
                ", isRead=" + isRead +
                '}';
    }

    public static class PwDataBean {
        @SerializedName(NetConstant.PS +"")
        private List<DataBean> ps;
        @SerializedName(NetConstant.PD +"")
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
         * isAchieveGoal : 0
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
                    ", value=" + value +
                    ", isAchieveGoal=" + isAchieveGoal +
                    '}';
        }
    }
}
