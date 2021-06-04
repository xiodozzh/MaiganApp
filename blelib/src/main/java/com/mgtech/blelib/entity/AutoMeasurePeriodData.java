package com.mgtech.blelib.entity;

public class AutoMeasurePeriodData {
    public static final int MAX_NUMBER = 5;

    private boolean enable;
    private int cycle;
    private int startHour;
    private int startMin;
    private int span;
    private int interval;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
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

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "Item{" +
                "enable=" + enable +
                ", cycle=" + cycle +
                ", startHour=" + startHour +
                ", startMin=" + startMin +
                ", span=" + span +
                ", interval=" + interval +
                '}';
    }
}
