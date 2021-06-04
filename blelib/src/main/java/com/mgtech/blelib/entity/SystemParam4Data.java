package com.mgtech.blelib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by zhaixiang on 2017/2/18.
 * 获取系统参数
 */

public class SystemParam4Data implements Parcelable,ISystemParam {
    /**
     * 自动采样提醒
     */
    private int reminderNumber;
    private int[] reminderWeek;
    private int[] reminderStartHour;
    private int[] reminderStartMinute;
    private int[] reminderSpanTime;
    private boolean[] reminderEnable;
//    private String[] id;

    public SystemParam4Data() {
    }

    protected SystemParam4Data(Parcel in) {
        reminderNumber = in.readInt();
        reminderWeek = in.createIntArray();
        reminderStartHour = in.createIntArray();
        reminderStartMinute = in.createIntArray();
        reminderSpanTime = in.createIntArray();
        reminderEnable = in.createBooleanArray();
//        id = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reminderNumber);
        dest.writeIntArray(reminderWeek);
        dest.writeIntArray(reminderStartHour);
        dest.writeIntArray(reminderStartMinute);
        dest.writeIntArray(reminderSpanTime);
        dest.writeBooleanArray(reminderEnable);
//        dest.writeStringArray(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SystemParam4Data> CREATOR = new Creator<SystemParam4Data>() {
        @Override
        public SystemParam4Data createFromParcel(Parcel in) {
            return new SystemParam4Data(in);
        }

        @Override
        public SystemParam4Data[] newArray(int size) {
            return new SystemParam4Data[size];
        }
    };
    @Override
    public void setData(byte[] data) {
        if (data.length >= 2) {
            reminderNumber = data[1];
            if (reminderNumber * 6 + 2 != data.length) {
                return;
            }
            reminderSpanTime = new int[reminderNumber];
            reminderWeek = new int[reminderNumber];
            reminderStartHour = new int[reminderNumber];
            reminderStartMinute = new int[reminderNumber];
            reminderEnable = new boolean[reminderNumber];
            for (int i = 0; i < reminderNumber; i++) {
                reminderEnable[i] = data[6 * i + 2] == 0x01;
                reminderWeek[i] = data[6 * i + 3] & 0xFF;
                reminderStartHour[i] = data[6 * i + 4] & 0xFF;
                reminderStartMinute[i] = data[6 * i + 5] & 0xFF;
                reminderSpanTime[i] = (data[6 * i + 6] & 0xFF) +
                        ((data[6 * i + 7] & 0xFF) << 8);
            }
        }
    }

    @Override
    public byte[] getSetParamCode(byte[] mac, int pairCode) {
        int number = this.getReminderNumber();
        int[] hour = this.getReminderStartHour();
        int[] min = this.getReminderStartMinute();
        int[] week = this.getReminderWeek();
        int[] span = this.getReminderSpanTime();
        boolean[] enable = this.getReminderEnable();
        if (number != 0 &&(hour == null || min == null || week == null || span == null || enable == null
                || hour.length != number || min.length != number
                || week.length != number || span.length != number || enable.length != number)) {
            throw new RuntimeException("time format error!!");
        }
        byte[] buf = new byte[number * 6 + 4];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_4;
        buf[1] = (byte) (number & 0xFF);
        for (int i = 0; i < number; i++) {
            buf[i * 6 + 2] = (byte) (enable[i] ? 0x01 : 0x00);
            buf[i * 6 + 3] = (byte) (week[i] & 0xFF);
            buf[i * 6 + 4] = (byte) (hour[i] & 0xFF);
            buf[i * 6 + 5] = (byte) (min[i] & 0xFF);
            buf[i * 6 + 6] = (byte) (span[i] & 0xFF);
            buf[i * 6 + 7] = (byte) ((span[i] >> 8) & 0xFF);
        }
        buf[6 * number + 2] = (byte) (mac[0] ^ buf[1]);
        buf[6 * number + 3] = (byte) (mac[2] ^ pairCode);
        return buf;
    }

    @Override
    public String getSaveString() {
//        int number = this.getReminderNumber();
//        int[] hour = this.getReminderStartHour();
//        int[] min = this.getReminderStartMinute();
//        int[] week = this.getReminderWeek();
//        int[] span = this.getReminderSpanTime();
//        boolean[] enable = this.getReminderEnable();
//        if (number != 0 && (hour == null || min == null || week == null || span == null || enable == null
//                || hour.length != number || min.length != number
//                || week.length != number || span.length != number || enable.length != number)) {
//            throw new RuntimeException("time format error!!");
//        }
//        byte[] buf = new byte[number * 6 + 2];
//        buf[0] = BluetoothConfig.CODE_SET_SYS_PARA_4;
//        buf[1] = (byte) (number & 0xFF);
//        for (int i = 0; i < number; i++) {
//            buf[i * 6 + 2] = (byte) (enable[i] ? 0x01 : 0x00);
//            buf[i * 6 + 3] = (byte) (week[i] & 0xFF);
//            buf[i * 6 + 4] = (byte) (hour[i] & 0xFF);
//            buf[i * 6 + 5] = (byte) (min[i] & 0xFF);
//            buf[i * 6 + 6] = (byte) (span[i] & 0xFF);
//            buf[i * 6 + 7] = (byte) ((span[i] >> 8) & 0xFF);
//        }
//        return buf;
        return new Gson().toJson(this);
    }

    public int getReminderNumber() {
        return reminderNumber;
    }

    public void setReminderNumber(int reminderNumber) {
        this.reminderNumber = reminderNumber;
    }

    public int[] getReminderWeek() {
        return reminderWeek;
    }

    public void setReminderWeek(int[] reminderWeek) {
        this.reminderWeek = reminderWeek;
    }

    public int[] getReminderStartHour() {
        return reminderStartHour;
    }

    public void setReminderStartHour(int[] reminderStartHour) {
        this.reminderStartHour = reminderStartHour;
    }

    public int[] getReminderStartMinute() {
        return reminderStartMinute;
    }

    public void setReminderStartMinute(int[] reminderStartMinute) {
        this.reminderStartMinute = reminderStartMinute;
    }

    public int[] getReminderSpanTime() {
        return reminderSpanTime;
    }

    public void setReminderSpanTime(int[] reminderSpanTime) {
        this.reminderSpanTime = reminderSpanTime;
    }

    public boolean[] getReminderEnable() {
        return reminderEnable;
    }

    public void setReminderEnable(boolean[] reminderEnable) {
        this.reminderEnable = reminderEnable;
    }

//    /**
//     * 获取周期（已经转换为本地时间）
//     *
//     * @param standardHour 标准时间 h
//     * @param standardMin  标准时间 min
//     * @param cycle        标准周期
//     * @return 本地周期 [true,true,true,true,true,true,true]表示一周7天都被选中
//     */
//    public static boolean[] getWeek(int standardHour, int standardMin, int cycle) {
//        Calendar standardCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        standardCalendar.set(Calendar.HOUR_OF_DAY, standardHour);
//        standardCalendar.set(Calendar.MINUTE, standardMin);
//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//        calendar.setTimeInMillis(standardCalendar.getTimeInMillis());
//        boolean isSwift = calendar.get(Calendar.DATE) != standardCalendar.get(Calendar.DATE);
//        long diff = calendar.getTimeInMillis() - standardCalendar.getTimeInMillis();
//        int newCycle = cycle;
//        if (isSwift) {
//            if (diff > 0) {
//                newCycle = ((cycle >>> 6) & 0x1) + ((cycle & 0x3F) << 1);
//            } else if (diff < 0) {
//                newCycle = ((cycle & 0x1) << 6) + (cycle >> 1);
//            }
//        }
//        boolean[] weeks = new boolean[7];
//        for (int i = 0; i < 7; i++) {
//            weeks[i] = ((newCycle >> i) & 0x1) == 0x1;
//        }
//        return weeks;
//    }

    public boolean isDifferent(SystemParam4Data other){
        if (other == null){
            return reminderNumber != 0;
        }
        return !(reminderNumber== other.getReminderNumber() && Arrays.equals(reminderEnable,other.getReminderEnable())
                && Arrays.equals(reminderSpanTime,other.getReminderSpanTime())
                && Arrays.equals(reminderStartHour,other.getReminderStartHour())
                && Arrays.equals(reminderStartMinute,other.getReminderStartMinute())
                && Arrays.equals(reminderWeek,other.getReminderWeek()));
    }

    /**
     * 获取本地时间
     *
     * @param standardHour 标准时间 h
     * @param standardMin  标准时间 min
     * @return 本地时间
     */
    public static Calendar getLocalCalendar(int standardHour, int standardMin) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.HOUR_OF_DAY, standardHour);
        calendar.set(Calendar.MINUTE, standardMin);
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTimeInMillis(calendar.getTimeInMillis());
        return c;
    }

    @Override
    public String toString() {
        return "SystemParam4Data{" +
                "reminderNumber=" + reminderNumber +
                ", reminderWeek=" + Arrays.toString(reminderWeek) +
                ", reminderStartHour=" + Arrays.toString(reminderStartHour) +
                ", reminderStartMinute=" + Arrays.toString(reminderStartMinute) +
                ", reminderSpanTime=" + Arrays.toString(reminderSpanTime) +
                ", reminderEnable=" + Arrays.toString(reminderEnable) +
                '}';
    }
}
