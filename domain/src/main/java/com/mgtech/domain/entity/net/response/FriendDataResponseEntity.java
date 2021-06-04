package com.mgtech.domain.entity.net.response;

public class FriendDataResponseEntity {

    private long lastMeasureTime;
    private long lastRemindTime;
    private int pwDataUnread;
    private int pwAbnormalDataUnread;
    private int hasDataPermission = 1;
    private int hasNotificationPermission = 1;
    private ExceptionResponseEntity lastTimeAbnormalRecord;
    private GetHomeDataResponseEntity.LastDayPWDataBean lastDayPWData;
    private GetHomeDataResponseEntity.LastTimeECGDataBean lastTimeECGData;

    public long getLastMeasureTime() {
        return lastMeasureTime;
    }

    public void setLastMeasureTime(long lastMeasureTime) {
        this.lastMeasureTime = lastMeasureTime;
    }

    public long getLastRemindTime() {
        return lastRemindTime;
    }

    public void setLastRemindTime(long lastRemindTime) {
        this.lastRemindTime = lastRemindTime;
    }

    public ExceptionResponseEntity getLastTimeAbnormalRecord() {
        return lastTimeAbnormalRecord;
    }

    public void setLastTimeAbnormalRecord(ExceptionResponseEntity lastTimeAbnormalRecord) {
        this.lastTimeAbnormalRecord = lastTimeAbnormalRecord;
    }

    public GetHomeDataResponseEntity.LastDayPWDataBean getLastDayPWData() {
        return lastDayPWData;
    }

    public void setLastDayPWData(GetHomeDataResponseEntity.LastDayPWDataBean lastDayPWData) {
        this.lastDayPWData = lastDayPWData;
    }

    public GetHomeDataResponseEntity.LastTimeECGDataBean getLastTimeECGData() {
        return lastTimeECGData;
    }

    public void setLastTimeECGData(GetHomeDataResponseEntity.LastTimeECGDataBean lastTimeECGData) {
        this.lastTimeECGData = lastTimeECGData;
    }

    public int getPwDataUnread() {
        return pwDataUnread;
    }

    public void setPwDataUnread(int pwDataUnread) {
        this.pwDataUnread = pwDataUnread;
    }

    public int getPwAbnormalDataUnread() {
        return pwAbnormalDataUnread;
    }

    public void setPwAbnormalDataUnread(int pwAbnormalDataUnread) {
        this.pwAbnormalDataUnread = pwAbnormalDataUnread;
    }

    public int getHasDataPermission() {
        return hasDataPermission;
    }

    public void setHasDataPermission(int hasDataPermission) {
        this.hasDataPermission = hasDataPermission;
    }

    public int getHasNotificationPermission() {
        return hasNotificationPermission;
    }

    public void setHasNotificationPermission(int hasNotificationPermission) {
        this.hasNotificationPermission = hasNotificationPermission;
    }

    @Override
    public String toString() {
        return "FriendDataResponseEntity{" +
                "lastMeasureTime=" + lastMeasureTime +
                ", lastRemindTime=" + lastRemindTime +
                ", pwDataUnread=" + pwDataUnread +
                ", pwAbnormalDataUnread=" + pwAbnormalDataUnread +
                ", hasDataPermission=" + hasDataPermission +
                ", hasNotificationPermission=" + hasNotificationPermission +
                ", lastTimeAbnormalRecord=" + lastTimeAbnormalRecord +
                ", lastDayPWData=" + lastDayPWData +
                ", lastTimeECGData=" + lastTimeECGData +
                '}';
    }
}
