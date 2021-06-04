package com.mgtech.maiganapp.data.model;

import java.util.List;

public class FriendDataModel {
    public long lastMeasureTime;
    public long lastRemindTime;
    public int pwUnreadNumber;
    public int abnormalUnreadNumber;
    public ExceptionRecordModel lastException;

    public long pwTime;
    public List<Data> psList;
    public List<Data> pdList;

    public long ecgTime;
    public float[] ecgData;

    public boolean haveDataPermission;
    public boolean haveNotificationPermission;

    public static class Data{
        public long time;
        public float value;

        @Override
        public String toString() {
            return "Data{" +
                    "time=" + time +
                    ", value=" + value +
                    '}';
        }
    }
}
