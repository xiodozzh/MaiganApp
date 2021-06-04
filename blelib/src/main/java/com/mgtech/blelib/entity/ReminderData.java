package com.mgtech.blelib.entity;

import java.util.List;

public class ReminderData {
    private List<AlertReminder> reminders;
    private List<AutoMeasurePeriodData> periods;
    private boolean timeZoneSet;

    public List<AlertReminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<AlertReminder> reminders) {
        this.reminders = reminders;
    }

    public boolean isTimeZoneSet() {
        return timeZoneSet;
    }

    public void setTimeZoneSet(boolean timeZoneSet) {
        this.timeZoneSet = timeZoneSet;
    }

    public List<AutoMeasurePeriodData> getPeriods() {
        return periods;
    }

    public void setPeriods(List<AutoMeasurePeriodData> periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "ReminderData{" +
                "reminders=" + reminders +
                ", periods=" + periods +
                ", timeZoneSet=" + timeZoneSet +
                '}';
    }
}
