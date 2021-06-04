package com.mgtech.domain.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zhaixiang on 2017/12/5.
 * 网络
 */

public class NetUtils {
    private static final String NET_DATE = "yyyy-MM-ddHH:mm:ss";

    private static DateFormat dateFormat = new SimpleDateFormat(NET_DATE, Locale.getDefault());

    public static Calendar getCalendarFromNet(String s) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(s.replaceAll(" ", ""));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * 获取网络的UTC时间
     *
     * @param s 　网络UTC时间
     * @return    本地时间calendar
     */
    public static Calendar getUtcCalendarFromNet(String s) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(s.replaceAll(" ", ""));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR_OF_DAY, Utils.getTimeZone());
        return calendar;
    }

    public static String byteToHexString(byte[] array){
        StringBuilder sb = new StringBuilder();
        for (byte b :array) {
            String string = Integer.toHexString(b & 0xFF);
            if (string.length() < 2) {
                sb.append("0");
            }
            sb.append(string);
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String string){
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        String upper = string.toUpperCase();
        int length = upper.length() / 2;
        char[] hexChars = upper.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4
                    | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
