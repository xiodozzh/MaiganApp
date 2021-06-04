package com.mgtech.maiganapp.data.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.mgtech.domain.entity.database.ReminderEntity;
import com.mgtech.domain.entity.database.ReminderManager;
import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.utils.AlarmUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * @date 2017/3/8
 * 设置提醒 model
 */

public class SetAlarmModel {
    private Context context;
    private SimpleDateFormat format;
    private SimpleDateFormat formatDate;

    public SetAlarmModel(Context context) {
        this.format = new SimpleDateFormat(MyConstant.MEDICINE_TIME, Locale.getDefault());
        this.formatDate = new SimpleDateFormat(MyConstant.DATE_FORMAT_BIRTHDAY, Locale.getDefault());
        this.context = context;
    }

    /**
     * 设置闹钟
     *
     * @param dataList 数据
     */
    public synchronized void setAlarm(final List<MedicineResponseEntity> dataList) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                List<ReminderEntity> reminders = transform(dataList);
                ReminderManager reminderManager = ReminderManager.getInstance(context);
                List<ReminderEntity> diskReminders = reminderManager.getReminders();

                // 如果本地有多余提醒，则关闭该提醒
                for (ReminderEntity reminderFromLocal : diskReminders) {
                    if (!contains(reminderFromLocal, reminders)) {
                        stopAlarm(reminderFromLocal);
                    }
                }
                // 如果本地不包含新设置的提醒，则打开一个提醒
                for (ReminderEntity reminderFromNet : reminders) {
                    if (!contains(reminderFromNet, diskReminders)) {
                        startAlarm(reminderFromNet);
                    }
                }
                // 删除旧数据，保存新数据
                reminderManager.updateAllReminder(reminders);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });

    }

    public synchronized void addReminders(final MedicineResponseEntity entity) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                List<ReminderEntity> reminders = transform(entity);
                ReminderManager reminderManager = ReminderManager.getInstance(context);
                List<ReminderEntity> diskReminders = reminderManager.getReminders();
                // 如果本地不包含提醒，则打开
                for (ReminderEntity reminderFromNet : reminders) {
                    if (!contains(reminderFromNet, diskReminders)) {
                        startAlarm(reminderFromNet);
                    }
                }
                // 保存新数据
                reminderManager.addReminders(reminders);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    public synchronized void removeReminders(final MedicineResponseEntity entity) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                List<ReminderEntity> reminders = transform(entity);
                ReminderManager reminderManager = ReminderManager.getInstance(context);
                List<ReminderEntity> diskReminders = reminderManager.getReminders();
                // 如果本地包含需要删除的提醒，则关闭
                for (ReminderEntity reminderFromNet : reminders) {
                    if (contains(reminderFromNet, diskReminders)) {
                        stopAlarm(reminderFromNet);
                    }
                }
                // 删除旧数据，保存新数据
                reminderManager.removeReminders(reminders);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    public synchronized void updateReminders(final MedicineResponseEntity oldEntity, final MedicineResponseEntity newEntity) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                List<ReminderEntity> oldReminders = transform(oldEntity);
                List<ReminderEntity> newReminders = transform(newEntity);
                ReminderManager reminderManager = ReminderManager.getInstance(context);
                // 关闭旧的提醒
                for (ReminderEntity reminderFromNet : oldReminders) {
                    stopAlarm(reminderFromNet);
                }
                // 开启新的提醒
                for (ReminderEntity reminderFromNet : newReminders) {
                    startAlarm(reminderFromNet);
                }
                // 删除旧数据，保存新数据
                reminderManager.removeReminders(oldReminders);
                reminderManager.addReminders(newReminders);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    public synchronized void stopAndRemoveAllReminders() {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ReminderManager reminderManager = ReminderManager.getInstance(context);
                List<ReminderEntity> diskReminders = reminderManager.getReminders();
                for (ReminderEntity entity : diskReminders) {
                    stopAlarm(entity);
                }
                reminderManager.removeAllReminder();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 每次打开app都确保数据库中的Reminder都被设置到alarmManager中
     */
    public void wakeUp(){
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ReminderManager reminderManager = ReminderManager.getInstance(context);
                List<ReminderEntity> diskReminders = reminderManager.getReminders();
                for (ReminderEntity entity : diskReminders) {
                    startAlarm(entity);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 不包含
     *
     * @param entity 元素
     * @param list   集合
     * @return 集合包括元素返回true
     */
    private boolean contains(ReminderEntity entity, List<ReminderEntity> list) {
        for (ReminderEntity reminder : list) {
            if (reminder.getHashCodeId() == entity.getHashCodeId()) {
                return true;
            }
        }
        return false;
    }

    private void startAlarm(ReminderEntity entity) {
        Log.i("alarm", "startAlarm: " + entity.toString());
        Intent intent = AlarmUtil.getAlarmIntent(context);
        intent.putExtra("name", entity.getName());
        intent.putExtra("dosage", entity.getDosage());
        AlarmUtil.startAlarm(context, entity.getHashCodeId(), entity.getRemindTime(), 24 * 60 * 60 * 1000, intent);
    }

    private void stopAlarm(ReminderEntity entity) {
        Log.i("alarm", "stopAlarm: " + entity.toString());
        Intent intent = AlarmUtil.getAlarmIntent(context);
        intent.putExtra("name", entity.getName());
        intent.putExtra("dosage", entity.getDosage());
        AlarmUtil.stopAlarm(context, entity.getHashCodeId(), intent);
    }

    /**
     * 将服务器数据转换为本地数据库格式,并且过滤掉过期内容
     *
     * @param dataList 服务器数据
     * @return 本地数据库格式
     */
    private List<ReminderEntity> transform(List<MedicineResponseEntity> dataList) {
        List<ReminderEntity> result = new ArrayList<>();
        for (MedicineResponseEntity entity : dataList) {
            result.addAll(transform(entity));
        }
        return result;
    }

    private List<ReminderEntity> transform(MedicineResponseEntity entity) {
        List<ReminderEntity> result = new ArrayList<>();
//        String startString = entity.getStartTime();
//        String endString = entity.getEndTime();
//        Calendar start = parseStartDateFormString(startString);
//        Calendar end = parseEndDateFormString(endString);
//
//        String reminderTimes = entity.getReminderTime();
//        if (reminderTimes == null || reminderTimes.isEmpty()) {
//            return result;
//        }
//        String[] timeList = reminderTimes.split(",");
//
//        for (String timeString : timeList) {
//            Intent intent = AlarmUtil.getAlarmIntent(context);
//            intent.putExtra("name", entity.getMedicineName());
//            intent.putExtra("dosage", entity.getDosage());
//            long time = getNextReminderTimeInMillis(timeString);
//            int reminderId = generateReminderId(entity, timeString);
//            if ((start != null && time < start.getTimeInMillis()) || (end != null && time > end.getTimeInMillis())) {
//                continue;
//            }
//            ReminderEntity reminderEntity = new ReminderEntity();
//            reminderEntity.setDosage(entity.getDosage());
//            reminderEntity.setName(entity.getMedicineName());
//            reminderEntity.setRemindTime(time);
//            reminderEntity.setHashCodeId(reminderId);
//            result.add(reminderEntity);
//        }
        return result;
    }


    private Calendar parseStartDateFormString(String dateString) {
        Calendar calendar = null;
        if (!TextUtils.isEmpty(dateString)) {
            try {
                Date date = formatDate.parse(dateString.substring(0, 10));
                calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendar;
    }

    private Calendar parseEndDateFormString(String dateString) {
        Calendar calendar = null;
        if (!TextUtils.isEmpty(dateString)) {
            try {
                Date date = formatDate.parse(dateString.substring(0, 10));
                calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendar;
    }


    private long getNextReminderTimeInMillis(String text) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            Date date = format.parse(text);
            calendar.setTime(date);
            calendar.set(year, month, day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = calendar.getTimeInMillis();
        if (time <= Calendar.getInstance().getTimeInMillis() - 60 * 1000) {
            time += 24 * 3600 * 1000;
        }
        return time;
    }

//    /**
//     * 生成唯一ID
//     *
//     * @param entity 类
//     * @param time   时间
//     * @return planId
//     */
//    private int generateReminderId(MedicineResponseEntity entity, String time) {
//        String start = entity.getStartTime();
//        String end = entity.getEndTime();
//        String name = entity.getMedicineName();
//        String dosage = entity.getDosage();
//        return (start + end + name + dosage + time).hashCode();
//    }
}
