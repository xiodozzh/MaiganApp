package com.mgtech.maiganapp.data.model;

import java.util.Objects;

public class StepModel {
    public long startTime;
    public long endTime;
    public int duration;
    public double distance;
    public double heat;
    public int stepCount;
    public int count;
    public String dateString;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModel that = (StepModel) o;
        return startTime == that.startTime &&
                endTime == that.endTime &&
                duration == that.duration &&
                Double.compare(that.distance, distance) == 0 &&
                Double.compare(that.heat, heat) == 0 &&
                stepCount == that.stepCount &&
                count == that.count;
    }

    @Override
    public int hashCode() {

        return Objects.hash(startTime, endTime, duration, distance, heat, stepCount, count);
    }

    @Override
    public String toString() {
        return "StepModel{" +
                ", dateString='" + dateString + '\'' +
                '}';
    }
}
