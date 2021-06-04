package com.mgtech.maiganapp.data.model;

import java.util.Objects;

/**
 * @author zhaixiang
 */
public class ExceptionRecordModel {
    public long noticeTime;
    public String noticeId;
    public String description;
    public boolean isRead;
    public String targetId;

    public IndicatorDataModel indicator;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionRecordModel that = (ExceptionRecordModel) o;
        return noticeTime == that.noticeTime &&
                isRead == that.isRead &&
                Objects.equals(noticeId, that.noticeId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(indicator, that.indicator);
    }

    @Override
    public int hashCode() {

        return Objects.hash(noticeTime, noticeId, description, isRead, indicator);
    }
}
