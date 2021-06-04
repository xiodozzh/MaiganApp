package com.mgtech.maiganapp.data.model;

import com.mgtech.blelib.utils.Utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class AlertReminderModel {
    private int index;
    private Calendar startTime;
    private int startHour;
    private int startMin;
    private int spanTime;
    private boolean[] localWeek;
    private boolean enable;
    private int cycle;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public int getSpanTime() {
        return spanTime;
    }

    public void setSpanTime(int spanTime) {
        this.spanTime = spanTime;
    }

    public boolean[] getLocalWeek() {
        return localWeek;
    }

    public void setLocalWeek(boolean[] localWeek) {
        this.localWeek = localWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "AlertReminderModel{" +
                "index=" + index +
                ", startTime=" + startTime +
                ", startHour=" + startHour +
                ", startMin=" + startMin +
                ", spanTime=" + spanTime +
                ", localWeek=" + Arrays.toString(localWeek) +
                ", enable=" + enable +
                ", cycle=" + cycle +
                '}';
    }

    /**
     * 获取周期（已经转换为本地时间）
     *
     * @param standardHour 标准时间 h
     * @param cycle        标准周期
     * @param timeZoneSet  是否被设置时区
     * @return 本地周期 [true,true,true,true,true,true,true]表示一周7天都被选中
     */
    public static boolean[] getWeek(int standardHour, int cycle, boolean timeZoneSet) {
        int newCycle;
        if (timeZoneSet) {
            newCycle = cycle;
        } else {
            int timeZone = Utils.getTimeZone();
            if (standardHour + timeZone >= 24) {
                newCycle = (cycle << 1) | ((cycle >>> 6) & 1);
            } else if (standardHour + timeZone < 0) {
                newCycle = (cycle >>> 1) | ((cycle & 1) << 6);
            } else {
                newCycle = cycle;
            }
        }
        boolean[] weeks = new boolean[7];
        for (int i = 0; i < 7; i++) {
            weeks[i] = ((newCycle >> i) & 0x1) == 0x1;
        }
        return weeks;
    }

    public static int getDisplayRemindCycle(int remindDay,int startHour,boolean timeZoneSet) {
        int newDay;
        if (timeZoneSet) {
            newDay = remindDay;
        }else{
            int timeZone = Utils.getTimeZone();
            if (startHour - timeZone < 0){
                newDay = (remindDay >>> 1) + ((remindDay & 1)<<6);
            }else if (startHour - timeZone >= 24){
                newDay = (remindDay << 1) + ((remindDay>>>6) & 1);
            }else{
                newDay = remindDay;
            }
        }
        return newDay;
    }
}
