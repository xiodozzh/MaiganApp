package com.mgtech.blelib.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.ReminderData;
import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zhaixiang on 2017/12/13.
 * 工具
 */

public class Utils {

    private static final int[] FIND_BRACELET_SUPPORT_VERSION = {4,1,0};
    private static final int[] REMINDER_TIME_ZONE_SUPPORT_VERSION = {4,2,0};
    private static final int[] REMINDER_INTERVAL_SUPPORT_VERSION = {4,2,1};

    public static short[] unzipData(byte[] rawData, int offset) {
        int length = rawData.length;
        short[] data = new short[(length - offset) / 3 * 2];
        int[] temple = new int[(length - offset) / 3];
        for (int i = offset; i < length; i++) {
            int row = (i - offset) / 3;
            int column = (i - offset) % 3;
            temple[row] += (rawData[i] & 0x0000FF) << (8 * column);
        }
        for (int i = 0; i < temple.length; i++) {
            data[i * 2] = (short) (temple[i] & 0xFFF);
            data[i * 2 + 1] = (short) (temple[i] >>> 12);
        }
        return data;
    }

    /**
     * 蓝牙是否打开
     *
     * @return true蓝牙打开，false反之
     */
    public static boolean isBluetoothOpen() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    /**
     * 是否支持查找手环功能
     * @param currentVersion 当前通讯协议
     * @return true支持
     */
    public static boolean doesFirmwareHaveFindBraceletFunc(int[] currentVersion){
        return doseCurrentVersionFitSupportVersion(currentVersion,FIND_BRACELET_SUPPORT_VERSION);
    }

    /**
     * 震动提醒是否支持时区和调整测量间隔
     * @param currentVersion 当前通讯协议
     * @return true支持
     */
    public static boolean doesReminderHaveZoneAndIntervalSetFunc(int[] currentVersion){
        return doseCurrentVersionFitSupportVersion(currentVersion,REMINDER_TIME_ZONE_SUPPORT_VERSION);
    }

    /**
     * 震动提醒是否支持时区和调整测量间隔
     * @param currentVersion 当前通讯协议
     * @return true支持
     */
    public static boolean doesReminderHaveIntervalFunc(int[] currentVersion){
        return doseCurrentVersionFitSupportVersion(currentVersion,REMINDER_INTERVAL_SUPPORT_VERSION);
    }

    public static void updateReminder(ReminderData reminderData){
        if (reminderData == null){
            return;
        }
        if (reminderData.isTimeZoneSet()){
            return;
        }
        Logger.e("updateReminder");
        List<AlertReminder>reminders = reminderData.getReminders();
        int timeZone = getTimeZone();
        for (AlertReminder reminder :reminders) {
            int repeat = reminder.getReminderWeek();
            int hour = reminder.getReminderStartHour() + timeZone;
            if (hour >= 24){
                hour -= 24;
                repeat = repeat << 1;
                if (repeat > 127){
                    repeat = repeat - 126;
                }
            }else if (hour < 0){
                hour += 24;
                boolean hasLast = (repeat & 1)== 1;
                repeat = repeat >>> 1;
                if (hasLast){
                    repeat = repeat | (1<<6);
                }
            }
            reminder.setReminderWeek(repeat);
            reminder.setReminderStartHour(hour);
        }
        reminderData.setTimeZoneSet(true);
    }

    public static void backReminder(ReminderData reminderData){
        if (!reminderData.isTimeZoneSet()){
            return;
        }
        Logger.e("backReminder");
        List<AlertReminder>reminders = reminderData.getReminders();
        int timeZone = getTimeZone();
        for (AlertReminder reminder :reminders) {
            int repeat = reminder.getReminderWeek();
            int hour = reminder.getReminderStartHour() - timeZone;
            if (hour >= 24){
                hour -= 24;
                repeat = repeat << 1;
                if (repeat > 127){
                    repeat = repeat - 126;
                }
            }else if (hour < 0){
                hour += 24;
                boolean hasLast = (repeat & 1)== 1;
                repeat = repeat >>> 1;
                if (hasLast){
                    repeat = repeat | (1<<6);
                }
            }
            reminder.setReminderWeek(repeat);
            reminder.setReminderStartHour(hour);
        }
        reminderData.setTimeZoneSet(false);
    }


    /**
     * 当前版本是否大于等于所支持的版本
     * @param currentVersion 当前
     * @param supportVersion 支持
     * @return true支持
     */
    private static boolean doseCurrentVersionFitSupportVersion(int[] currentVersion,int[] supportVersion){
        if (currentVersion == null || currentVersion.length != 3){
            return false;
        }
        if (currentVersion[0] > supportVersion[0]){
            return true;
        }else if (currentVersion[0] == supportVersion[0]){
            if (currentVersion[1] > supportVersion[1]){
                return true;
            }else if(currentVersion[1] == supportVersion[1]){
                return currentVersion[2] >= supportVersion[2];
            }
        }
        return false;
    }
    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context 上下文
     * @return versionName
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context 上下文
     * @return versionCode
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * format yyyy-MM-dd
     */
    private static DateFormat format1 = new SimpleDateFormat(BleConstant.DISPLAY_DATE_1, Locale.getDefault());
    /**
     * format yyyy-MM-dd HH:mm:ss
     */
    private static DateFormat format2 = new SimpleDateFormat(BleConstant.DISPLAY_DATE_3, Locale.getDefault());

    static {
        format1.setTimeZone(TimeZone.getTimeZone("UTC"));
        format2.setTimeZone(TimeZone.getTimeZone("UTC"));
    }



    /**
     * 获取时区
     *
     * @return 东8区为+8
     */
    public static int getTimeZone() {
        return TimeZone.getDefault().getRawOffset() / 1000 / 3600;
    }


    public static String localCalendarToFullUTCString(long localTime) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(localTime);
//        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//        DateFormat format = new SimpleDateFormat(BleConstant.DISPLAY_DATE_1,Locale.getDefault());
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return format.format(new Date(localTime));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(localTime);
//        DateFormat format = new SimpleDateFormat(BleConstant.DISPLAY_DATE_5,Locale.getDefault());
        return format2.format(new Date(localTime));
    }

    public static boolean isEnglish(){
        return !Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage());
    }

}
