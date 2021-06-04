package com.mgtech.blelib.entity;

import androidx.annotation.NonNull;

/**
 * @author zhaixiang
 */
public class AlertReminder {
    public static final int MAX_NUMBER = 5;
    public static final int DAILY_INTERVAL = 3600 * 3;
    public static final int CONTINUOUS_INTERVAL = 3600;
    public static final int EVERYDAY = 127;
    public static final int WORKDAY = 62;
    public static final int WEEKEND = 65;

    private int reminderWeek;
    private int reminderStartHour;
    private int reminderStartMinute;
    private int reminderSpanTime;
    private boolean reminderEnable;
    private int reminderInterval;

    public AlertReminder() {
    }

    public int getReminderWeek() {
        return reminderWeek;
    }

    public void setReminderWeek(int reminderWeek) {
        this.reminderWeek = reminderWeek;
    }

    public int getReminderStartHour() {
        return reminderStartHour;
    }

    public void setReminderStartHour(int reminderStartHour) {
        this.reminderStartHour = reminderStartHour;
    }

    public int getReminderStartMinute() {
        return reminderStartMinute;
    }

    public void setReminderStartMinute(int reminderStartMinute) {
        this.reminderStartMinute = reminderStartMinute;
    }

    public int getReminderSpanTime() {
        return reminderSpanTime;
    }

    public void setReminderSpanTime(int reminderSpanTime) {
        this.reminderSpanTime = reminderSpanTime;
    }

    public boolean isReminderEnable() {
        return reminderEnable;
    }

    public void setReminderEnable(boolean reminderEnable) {
        this.reminderEnable = reminderEnable;
    }

    public int getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(int reminderInterval) {
        if (reminderInterval <= 0){
            this.reminderInterval = CONTINUOUS_INTERVAL;
        }else {
            this.reminderInterval = reminderInterval;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "AlertReminder{" +
                "reminderWeek=" + reminderWeek +
                ", reminderStartHour=" + reminderStartHour +
                ", reminderStartMinute=" + reminderStartMinute +
                ", reminderSpanTime=" + reminderSpanTime +
                ", reminderEnable=" + reminderEnable +
                ", reminderInterval=" + reminderInterval +
                '}';
    }
}
