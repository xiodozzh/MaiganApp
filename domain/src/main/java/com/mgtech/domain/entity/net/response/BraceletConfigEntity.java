package com.mgtech.domain.entity.net.response;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

/**
 *
 * @author zhaixiang
 * 获取手环参数
 */

public class BraceletConfigEntity implements RequestEntity {

    private int type;
    private DisplayBean displayInfo;
    private List<RemindersBean> reminders;
    private List<MeasureBean> measureFrequency;
    private long bindTime;
    private String accountGuid;

    @Override
    public String getUrl() {
        return HttpApi.API_SET_BRACELET_CONFIG;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public DisplayBean getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(DisplayBean displayInfo) {
        this.displayInfo = displayInfo;
    }

    public long getBindTime() {
        return bindTime;
    }

    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }

    public List<MeasureBean> getMeasureFrequency() {
        return measureFrequency;
    }

    public void setMeasureFrequency(List<MeasureBean> measureFrequency) {
        this.measureFrequency = measureFrequency;
    }

    @Override
    public String toString() {
        return "BraceletConfigEntity{" +
                "type=" + type +
                ", displayInfo=" + displayInfo +
                ", reminders=" + reminders +
                ", measureFrequency=" + measureFrequency +
                ", bindTime=" + bindTime +
                ", accountGuid='" + accountGuid + '\'' +
                '}';
    }

    public List<RemindersBean> getReminders() {
        return reminders;
    }

    public void setReminders(List<RemindersBean> reminders) {
        this.reminders = reminders;
    }

    public static class RemindersBean {
        /**
         * enable : 90
         * repeat : 45
         * startHour : 67
         * startMinute : 89
         * period : 123
         */

        private int enable;
        private int repeat;
        private int startHour;
        private int startMinute;
        private int period;
        private int isActualTime;
        private int frequency;

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public int getRepeat() {
            return repeat;
        }

        public void setRepeat(int repeat) {
            this.repeat = repeat;
        }

        public int getStartHour() {
            return startHour;
        }

        public void setStartHour(int startHour) {
            this.startHour = startHour;
        }

        public int getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(int startMinute) {
            this.startMinute = startMinute;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getIsActualTime() {
            return isActualTime;
        }

        public void setIsActualTime(int isActualTime) {
            this.isActualTime = isActualTime;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return "RemindersBean{" +
                    "enable=" + enable +
                    ", repeat=" + repeat +
                    ", startHour=" + startHour +
                    ", startMinute=" + startMinute +
                    ", period=" + period +
                    ", isActualTime=" + isActualTime +
                    ", frequency=" + frequency +
                    '}';
        }
    }

    public static class DisplayBean {
        private int displayBP;
        private int displayHR;
        private int displayV0;
        private int displayStep;
        private int displayDistance;
        private int displayHeat;
        private int displayPower;
        private int dateFormatType;

        public int getDisplayBP() {
            return displayBP;
        }

        public void setDisplayBP(int displayBP) {
            this.displayBP = displayBP;
        }

        public int getDisplayHR() {
            return displayHR;
        }

        public void setDisplayHR(int displayHR) {
            this.displayHR = displayHR;
        }

        public int getDisplayV0() {
            return displayV0;
        }

        public void setDisplayV0(int displayV0) {
            this.displayV0 = displayV0;
        }

        public int getDisplayStep() {
            return displayStep;
        }

        public void setDisplayStep(int displayStep) {
            this.displayStep = displayStep;
        }

        public int getDisplayDistance() {
            return displayDistance;
        }

        public void setDisplayDistance(int displayDistance) {
            this.displayDistance = displayDistance;
        }

        public int getDisplayHeat() {
            return displayHeat;
        }

        public void setDisplayHeat(int displayHeat) {
            this.displayHeat = displayHeat;
        }

        public int getDisplayPower() {
            return displayPower;
        }

        public void setDisplayPower(int displayPower) {
            this.displayPower = displayPower;
        }

        public int getDateFormatType() {
            return dateFormatType;
        }

        public void setDateFormatType(int dateFormatType) {
            this.dateFormatType = dateFormatType;
        }

        @Override
        public String toString() {
            return "DisplayBean{" +
                    "displayBP=" + displayBP +
                    ", displayHR=" + displayHR +
                    ", displayV0=" + displayV0 +
                    ", displayStep=" + displayStep +
                    ", displayDistance=" + displayDistance +
                    ", displayHeat=" + displayHeat +
                    ", displayPower=" + displayPower +
                    ", dateFormatType=" + dateFormatType +
                    '}';
        }
    }
    public static class MeasureBean{
        private int enable;
        private int repeat;
        private int startHour;
        private int startMinute;
        private int period;
        private int frequency;

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public int getRepeat() {
            return repeat;
        }

        public void setRepeat(int repeat) {
            this.repeat = repeat;
        }

        public int getStartHour() {
            return startHour;
        }

        public void setStartHour(int startHour) {
            this.startHour = startHour;
        }

        public int getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(int startMinute) {
            this.startMinute = startMinute;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return "MeasureBean{" +
                    "enable=" + enable +
                    ", repeat=" + repeat +
                    ", startHour=" + startHour +
                    ", startMinute=" + startMinute +
                    ", period=" + period +
                    ", frequency=" + frequency +
                    '}';
        }
    }
}
