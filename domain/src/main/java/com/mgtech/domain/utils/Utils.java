package com.mgtech.domain.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zhaixiang on 2017/12/13.
 * 工具
 */

public class Utils {
    /**
     * 一星期毫秒
     */
    private static final long WEEK_IN_MILLISECOND = 1000 * 3600 * 24 * 7;

    /**
     * 解压数据
     *
     * @param rawData 原始数据
     * @return 解压后数据
     */
    public static short[] unzipData(byte[] rawData) {
        int length = rawData.length;
        short[] data = new short[length / 3 * 2];
        int[] temple = new int[length / 3];
        for (int i = 2; i < rawData.length; i++) {
            int row = (i + 1) / 3 - 1;
            int column = (i + 1) % 3;
            temple[row] += (rawData[i] & 0x0000FF) << (8 * column);
        }
        for (int i = 0; i < temple.length; i++) {
            data[i * 2] = (short) (temple[i] & 0xFFF);
            data[i * 2 + 1] = (short) (temple[i] >>> 12);
        }
        return data;
    }

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

    public static byte[] zipData(List<Short> data) {
        int length = data.size();
        if (length % 2 != 0) {
            data.add((short) 0xFFF);
        }
        length = data.size();
        byte[] zipData = new byte[length / 2 * 3];
        for (int i = 0; i < length / 2; i++) {
            short num1 = data.get(i * 2);
            short num2 = data.get(i * 2 + 1);
            byte[] temp = zipData(num1, num2);
            zipData[i * 3] = temp[0];
            zipData[i * 3 + 1] = temp[1];
            zipData[i * 3 + 2] = temp[2];
        }
        return zipData;
    }

    public static byte[] zipData(short number1, short number2) {
        byte[] data = new byte[3];
        data[0] = (byte) (number1 & 0xFF);
        data[2] = (byte) ((number2 >> 4) & 0xFF);
        data[1] = (byte) (((number2 & 0xF) << 4) + ((number1 >> 8) & 0xF));
        return data;
    }


    public static String Base64_Encode(String str) {
        int c1, c2, c3;
        String base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        int i = 0;
        int len = str.length();
        String string = "";

        while (i < len) {
            c1 = str.charAt(i++) & 0xff;
            if (i == len) {
                string += base64EncodeChars.charAt(c1 >> 2);
                string += base64EncodeChars.charAt((c1 & 0x3) << 4);
                string += "==";
                break;
            }
            c2 = str.charAt(i++);
            if (i == len) {
                string += base64EncodeChars.charAt(c1 >> 2);
                string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
                string += base64EncodeChars.charAt((c2 & 0xF) << 2);
                string += "=";
                break;
            }
            c3 = str.charAt(i++);
            string += base64EncodeChars.charAt(c1 >> 2);
            string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
            string += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
            string += base64EncodeChars.charAt(c3 & 0x3F);
        }
        return string;
    }

    /**
     * 获取年
     *
     * @param calendar 日期
     * @return 年
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @param calendar 日期
     * @return 月
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取日
     *
     * @param calendar 日期
     * @return 日
     */
    public static int getDayOfMonth(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取星期
     *
     * @param calendar 日期 周日=1 周一=2 ...
     * @return 星期
     */
    public static int getDayOfWeek(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取时间
     *
     * @param calendar 日期
     * @return 时
     */
    public static float getTimeLineX(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / (float) 60;
    }

    /**
     * 获取当前月的最大天数
     *
     * @param calendar 日期
     * @return 天数
     */
    public static int getMonthDay(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        c.set(Calendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        return c.get(Calendar.DATE);
    }

    /**
     * 获取一周开始日期
     *
     * @param calendar 日期
     * @return 开始日期
     */
    public static Calendar getFirstDayOfWeek(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        int dayOfWeek = getDayOfWeek(c);
        c.add(Calendar.DATE, getCalendarFirstDayOfWeek(c) - dayOfWeek);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    private static int getCalendarFirstDayOfWeek(Calendar c){
        return Calendar.SUNDAY;
    }

    /**
     * 是否是同一天
     *
     * @param calendar1 日期1
     * @param calendar2 日期2
     * @return true 同一天
     */
    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return getYear(calendar1) == getYear(calendar2) && getMonth(calendar1) == getMonth(calendar2)
                && calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE);
    }

    /**
     * 将日期设置为加上市区的时间（用于网络请求）
     *
     * @param calendar 需要修饰的时间
     * @return 修饰后的时间
     */
    public static Calendar addTimeZoneToCalendar(Calendar calendar) {
        int timeZone = getTimeZone();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.HOUR_OF_DAY, timeZone);
        return c;
    }

    /**
     * 获取一周结束日期
     *
     * @param calendar 日期
     * @return 结束日期
     */
    public static Calendar getLastDayOfWeek(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        int dayOfWeek = getDayOfWeek(c);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, getCalendarFirstDayOfWeek(c) - dayOfWeek + 7);
//        c.add(Calendar.SECOND, -1);
        return c;
    }

    /**
     * 获取一个月结束日期
     *
     * @param calendar 日期
     * @return 结束日期
     */
    public static Calendar getLastDayOfMonth(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        int dayOfWeek = getDayOfWeek(c);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, getCalendarFirstDayOfWeek(c) - dayOfWeek + 7);
        return c;
    }

    /**
     * 获取一年最后一天结束日期
     *
     * @param calendar 日期
     * @return 结束日期
     */
    public static Calendar getLastYear(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.YEAR, 1);
        c.add(Calendar.SECOND, -1);
        return c;
    }

    /**
     * 日期之间相差周数
     *
     * @param firstDayOfWeek 一周的开始
     * @param targetDay      目标日期
     * @return 周数 =0，本周内，<0 targetDay 在firstDayOfWeek前几周
     */
    public static int weeksBetween(Calendar firstDayOfWeek, Calendar targetDay) {
        Log.e("goToDay", "day: " + targetDay.getTimeInMillis() + " " + firstDayOfWeek.getTimeInMillis());
        long dif = targetDay.getTimeInMillis() - firstDayOfWeek.getTimeInMillis();
        int week = (int) (dif / WEEK_IN_MILLISECOND);
        Log.e("goToDay", "weeksBetween: " + week + " " + dif);
        if (dif < 0) {
            week--;
        }
        return week;
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
     * 手机品牌型号
     */
    public static String getBand(){
        return android.os.Build.BRAND + " " + android.os.Build.DEVICE;
    }

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context 上下文
     * @return versionCode
     */
    public static long getVersionCode(Context context) {
        long versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                versionCode = packInfo.getLongVersionCode();
            }else {
                versionCode = packInfo.versionCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getPhoneMac(Context context) {
//        try {
//            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface nif : all) {
//                if (!nif.getName().equalsIgnoreCase("wlan0")) {
//                    continue;
//                }
//                byte[] macBytes = nif.getHardwareAddress();
//                if (macBytes == null) {
//                    return "";
//                }
//                StringBuilder res1 = new StringBuilder();
//                for (byte b : macBytes) {
//                    String byteString = Integer.toHexString(b & 0xFF);
//                    if (byteString.length() <= 1) {
//                        res1.append("0" + Integer.toHexString(b & 0xFF) + ":");
//                    } else {
//                        res1.append(Integer.toHexString(b & 0xFF) + ":");
//                    }
//                }
//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length() - 1);
//                }
//                return res1.toString();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return "";
        return PhoneMacAddressUtil.getPhoneMac(context);
    }


    /**
     * 标准数据转换为本地时间
     *
     * @param utcCalendar 标准时间
     * @return 本地时间
     */
    public static Calendar UTCCalendarToLocal(Calendar utcCalendar) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(utcCalendar.getTimeInMillis());
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar;
    }

    /**
     * format yyyy-MM-dd
     */
    private static DateFormat format1 = new SimpleDateFormat(MyConstant.DATE_FORMAT_BIRTHDAY, Locale.getDefault());
    /**
     * format yyyy-MM-dd HH:mm:ss
     */
    private static DateFormat format2 = new SimpleDateFormat(MyConstant.FULL_DATETIME_FORMAT, Locale.getDefault());

    static {
        format1.setTimeZone(TimeZone.getTimeZone("UTC"));
        format2.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * 标准数据转换为本地时间
     *
     * @param utcCalendarText 标准时间
     * @return 本地时间
     */
    public static Calendar UTCCalendarToLocal(@NonNull String utcCalendarText) {
        DateFormat format;
        if (utcCalendarText.length() <= 10) {
            format = format1;
        } else {
            format = format2;
        }
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(utcCalendarText.replaceAll("T", " "));
            calendar.setTime(date);
            calendar.setTimeZone(TimeZone.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    public static Calendar localToCalendar(@NonNull String local) {
        DateFormat format;
        if (local.length() <= 10) {
            format = new SimpleDateFormat(MyConstant.DATE_FORMAT_BIRTHDAY, Locale.getDefault());
        } else {
            format = new SimpleDateFormat(MyConstant.FULL_DATETIME_FORMAT, Locale.getDefault());
        }
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(local.replaceAll("T", " "));
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    public static Calendar utcCalendarToLocal(@NonNull String local) {
        DateFormat format;
        if (local.length() <= 10) {
            format = new SimpleDateFormat(MyConstant.DATE_FORMAT_BIRTHDAY, Locale.getDefault());
        } else {
            format = new SimpleDateFormat(MyConstant.DISPLAY_DATE_WITH_NO_SPACE, Locale.getDefault());
        }
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(local.replaceAll("T", ""));
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, getTimeZone());
            calendar.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    /**
     * 获取时区
     *
     * @return 东8区为+8
     */
    public static int getTimeZone() {
        return TimeZone.getDefault().getRawOffset() / 1000 / 3600;
    }

    public static int calculateNumberOfDaysBetweenDays(Calendar c1, Calendar c2) {
        long time = c2.getTimeInMillis() - c1.getTimeInMillis();
        float d = time / (24 * 3600 * 1000 * 1f);
        return (int) Math.floor(d);
    }

    public static int calculateNumberOfDaysBetweenDays(long c1, long c2) {
        long time = c2 - c1;
        float d = time / (24 * 3600 * 1000 * 1f);
        return (int) Math.floor(d);
    }

    public static int calculateNumberOfMonthsBetweenDays(Calendar c1, Calendar c2) {
        return (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }


    /**
     * 本地时间转换为标准时间
     *
     * @param localCalendar 本地时间
     * @return 标准时间
     */
    public static Calendar localCalendarToUTC(Calendar localCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localCalendar.getTimeInMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar;
    }

    public static String localCalendarToUTCString(long localTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTime);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat format = new SimpleDateFormat(MyConstant.DATE_FORMAT_BIRTHDAY, Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(localTime));

//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        calendar.setTimeInMillis(localTime);
//        DateFormat format = new SimpleDateFormat(BleConstant.DISPLAY_DATE_5,Locale.getDefault());
//        return format.format(new Date(localTime));
    }

    public static String localCalendarToFullUTCString(long localTime) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(localTime);
//        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//        DateFormat format = new SimpleDateFormat(BleConstant.DATE_FORMAT_BIRTHDAY,Locale.getDefault());
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return format.format(new Date(localTime));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(localTime);
//        DateFormat format = new SimpleDateFormat(BleConstant.DISPLAY_DATE_5,Locale.getDefault());
        return format2.format(new Date(localTime));
    }

    public static boolean isLanguageChinese() {
        return TextUtils.equals(Locale.getDefault().getLanguage(), Locale.SIMPLIFIED_CHINESE.getLanguage()) ||
                TextUtils.equals(Locale.getDefault().getLanguage(), Locale.TRADITIONAL_CHINESE.getLanguage());
    }

    public static boolean isThereInternetConnection(Context context) {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        } catch (NullPointerException e) {
            return false;
        }
        return isConnected;
    }

    public static final float[] FONT_SIZE = {0.9f, 1f, 1.066f, 1.133f, 1.2f};

    /**
     * 获取字体大小倍数
     *
     * @param index 字体大小指针
     * @return 字体大小倍数
     */
    public static float getFontSizeScale(int index) {
        if (index < 0 || index >= FONT_SIZE.length) {
            return 1;
        }
        return FONT_SIZE[index];
    }
}
