package com.mgtech.maiganapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mgtech.maiganapp.receiver.MyAlarmReceiver;

public class AlarmUtil {
    public static Intent getAlarmIntent(Context context) {
        return new Intent(context, MyAlarmReceiver.class);
    }

    public static void startAlarm(Context context, int id, long start, Intent intent) {
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //一次性闹钟，第一个参数表示闹钟类型，第二个参数表示闹钟执行时间，第三个参数表示闹钟响应动作。
        if (manager != null) {
            manager.set(AlarmManager.RTC_WAKEUP, start, sender);
        }
    }

    public static void startAlarm(Context context, int requestCode, long start, long interval, Intent intent) {
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //重复闹钟，第一个参数表示闹钟类型，第二个参数表示闹钟首次执行时间，第三个参数表示闹钟两次执行的间隔时间，第三个参数表示闹钟响应动作。
        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, start, interval, sender);
        }
    }

    public static void stopAlarm(Context context, int requestCode, Intent intent) {
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.cancel(sender);
        }
    }
}
