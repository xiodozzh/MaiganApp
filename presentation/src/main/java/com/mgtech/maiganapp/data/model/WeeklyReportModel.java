package com.mgtech.maiganapp.data.model;

import java.util.List;
import java.util.Objects;

public class WeeklyReportModel {
    public String weekReportGuid;
    public String weekReportDetailUrl;
    public String shareTitle = "";
    public String shareContent= "";
    public boolean read;
    public long startTime;
    public long endTime;
    public List<IndicatorDataModel> indicators;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklyReportModel that = (WeeklyReportModel) o;
        return startTime == that.startTime &&
                endTime == that.endTime &&
                Objects.equals(weekReportGuid, that.weekReportGuid) &&
                Objects.equals(weekReportDetailUrl, that.weekReportDetailUrl) &&
                Objects.equals(shareTitle, that.shareTitle) &&
                Objects.equals(shareContent, that.shareContent) &&
                Objects.equals(indicators, that.indicators) &&
                read == that.read;
    }

    @Override
    public int hashCode() {

        return Objects.hash(weekReportGuid, weekReportDetailUrl, shareTitle, shareContent, startTime, endTime, indicators,read);
    }

    @Override
    public String toString() {
        return "WeeklyReportModel{" +
                "weekReportGuid='" + weekReportGuid + '\'' +
                ", weekReportDetailUrl='" + weekReportDetailUrl + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareContent='" + shareContent + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", indicators=" + indicators +
                ", read=" + read +
                '}';
    }
}
