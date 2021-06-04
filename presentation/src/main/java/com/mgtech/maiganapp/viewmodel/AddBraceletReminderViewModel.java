package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.blelib.utils.Utils;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.AlertReminderModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhaixiang
 * @date 2017/8/7
 * 添加提醒ViewModel
 */

public class AddBraceletReminderViewModel extends BaseBleViewModel {
    private Calendar startTime;
    private Calendar endTime;
    public final ObservableField<String> startTimeString = new ObservableField<>("");
    public final ObservableField<String> endTimeString = new ObservableField<>("");
    public final ObservableBoolean success = new ObservableBoolean(false);
    public final ObservableBoolean isDaily = new ObservableBoolean(true);
    private int remindDays;
    private boolean timeZoneSet;
    private List<AlertReminder> data;
    private int index;
    private boolean[] selectedWeeks = {true, true, true, true, true, true, true};
    private IBraceletInfoManager manager;

    public AddBraceletReminderViewModel(Application context) {
        super(context);
        this.startTime = Calendar.getInstance();
        this.endTime = Calendar.getInstance();
        this.manager = new BraceletInfoManagerBuilder(context).create();
        ReminderData reminderData = manager.getReminderData();
        timeZoneSet = reminderData.isTimeZoneSet();
        data = reminderData.getReminders();
    }


    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_DISCONNECTED:
                if (ViewUtils.isScreenOn(getApplication()) && !background) {
                    checkLink();
                }
                break;
            case BleStatusConstant.STATUS_SET_ALERT_REMINDERS_SUCCESS:
                Logger.i("设置成功");
                success.set(!success.get());
                break;
            default:
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (!isConnected) {
            linkIfAllowed();
        }
    }

    @Override
    protected void onStateChange(int state) {
        if (state == BleStateEvent.STATE_OFF) {
            stateViewModel.bluetoothClose();
        } else if (state == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                stateViewModel.scanning();
                bleRequest.bluetoothLink();
            }else{
                stateViewModel.unbind();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new DefaultResponseCallback();
    }


    public void checkLink() {
        boolean isPaired = manager.isPaired();
        if (isPaired) {
            if (isBleOn()) {
                bleRequest.bluetoothGetLinkStatus();
            }else{
                stateViewModel.bluetoothClose();
            }
        }else{
            stateViewModel.unbind();
        }
    }

    public void setStartTime(Date date) {
        startTime.setTime(date);
        displayStartTime();
    }

    public void setEndTime(Date date) {
        endTime.setTime(date);
        displayEndTime();
    }

    private void displayStartTime() {
        startTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, startTime).toString());
        if (!endTime.after(startTime)) {
            endTimeString.set(getApplication().getString(R.string.bracelet_reminder_next_day) + DateFormat.format(MyConstant.DISPLAY_TIME, endTime));
        } else {
            endTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, endTime).toString());
        }
    }

    private void displayEndTime() {
        if (!endTime.after(startTime)) {
            endTimeString.set(getApplication().getString(R.string.bracelet_reminder_next_day) + DateFormat.format(MyConstant.DISPLAY_TIME, endTime));
        } else {
            endTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, endTime).toString());
        }
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }


    public void setRemindDays(int days) {
        this.remindDays = days;
        Log.e(TAG, "setRemindDays: " + days);
    }

    public int getRemindDays() {
        return remindDays;
    }

    public void save() {
        if (TextUtils.isEmpty(startTimeString.get()) || TextUtils.isEmpty(endTimeString.get())) {
            showToast(getApplication().getString(R.string.bracelet_reminder_please_select_reminder_time));
        } else if (remindDays == 0) {
            showToast(getApplication().getString(R.string.bracelet_reminder_please_choose_cycle));
        } else {
            bleRequest.bluetoothSetAutoReminder(setSystemPara());
        }
    }


    /**
     * 设置初始
     *
     * @param index 处理的序号
     */
    public void initData(int index) {
        this.index = index;
        if (index != -1 && index < data.size()) {
            AlertReminder reminder = data.get(index);
            isDaily.set(reminder.getReminderInterval() == AlertReminder.DAILY_INTERVAL);
            Calendar c = getLocalCalendar(reminder.getReminderStartHour(),
                    reminder.getReminderStartMinute());
            startTime.setTimeInMillis(c.getTimeInMillis());
            c.add(Calendar.MINUTE, reminder.getReminderSpanTime());
            endTime.setTimeInMillis(c.getTimeInMillis());
            startTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, startTime).toString());
            if (endTime.get(Calendar.DATE) != startTime.get(Calendar.DATE)) {
                // 如果日期不一样则调整为同一天，并且结束日期前加“次日”
                endTime.add(Calendar.DATE, -1);
                endTimeString.set(getApplication().getString(R.string.bracelet_reminder_next_day) + DateFormat.format(MyConstant.DISPLAY_TIME, endTime));
            } else {
                endTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, endTime).toString());
            }
            remindDays = reminder.getReminderWeek();
            displayReminderDays(reminder.getReminderStartHour(), remindDays);
        }else{
            startTime.set(Calendar.HOUR_OF_DAY, 8);
            startTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.HOUR_OF_DAY, 20);
            endTime.set(Calendar.MINUTE, 0);
            startTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, startTime).toString());
            endTimeString.set(DateFormat.format(MyConstant.DISPLAY_TIME, endTime).toString());
            isDaily.set(true);
        }
    }

    /**
     * 获取本地时间
     *
     * @param standardHour 标准时间 h
     * @param standardMin  标准时间 min
     * @return 本地时间
     */
    private Calendar getLocalCalendar(int standardHour, int standardMin) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, standardHour);
        c.set(Calendar.MINUTE, standardMin);
        if (!timeZoneSet) {
            c.add(Calendar.HOUR_OF_DAY,Utils.getTimeZone());
        }
        return c;
    }

    /**
     * 显示选择的星期
     *
     * @param startHour 起始标准时间
     * @param weekDay   标准时间重复星期
     */
    private void displayReminderDays(int startHour, int weekDay) {
        setSelectedWeeks(AlertReminderModel.getWeek(startHour, weekDay,timeZoneSet));
    }


    private ReminderData setSystemPara() {
        if (index == -1) {
            return addSystemParaData();
        } else {
            return updateSystemParaData();
        }
    }

    /**
     * 添加 SystemParamData
     * @return 添加后的data
     */
    private ReminderData addSystemParaData() {
        ReminderData reminderData = new ReminderData();
        int[] hourAndMin = getStartHourAndMinute();
        AlertReminder reminder = new AlertReminder();
        reminder.setReminderEnable(true);
        reminder.setReminderStartHour(hourAndMin[0]);
        reminder.setReminderStartMinute(hourAndMin[1]);
        reminder.setReminderSpanTime(getSpanMinute());
        reminder.setReminderWeek(AlertReminderModel.getDisplayRemindCycle(remindDays,startTime.get(Calendar.HOUR_OF_DAY),timeZoneSet));
        reminder.setReminderInterval(isDaily.get()?AlertReminder.DAILY_INTERVAL:AlertReminder.CONTINUOUS_INTERVAL);
        data.add(reminder);
        reminderData.setReminders(data);
        reminderData.setTimeZoneSet(Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion()));
        return reminderData;
    }

    /**
     * 修改 SystemParamData
     * @return 修改后的 SystemParamData
     */
    private ReminderData updateSystemParaData() {
        ReminderData reminderData = new ReminderData();
        AlertReminder reminder = data.get(index);
        int[] hourAndMin = getStartHourAndMinute();
        reminder.setReminderEnable(true);
        reminder.setReminderStartHour(hourAndMin[0]);
        reminder.setReminderStartMinute(hourAndMin[1]);
        reminder.setReminderSpanTime(getSpanMinute());
        reminder.setReminderWeek(AlertReminderModel.getDisplayRemindCycle(remindDays,startTime.get(Calendar.HOUR_OF_DAY),timeZoneSet));
        reminder.setReminderInterval(isDaily.get()?AlertReminder.DAILY_INTERVAL:AlertReminder.CONTINUOUS_INTERVAL);
        reminderData.setReminders(data);
        reminderData.setTimeZoneSet(Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion()));
        return reminderData;
    }



    /**
     * 获取时间跨度
     *
     * @return 分钟
     */
    private int getSpanMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime.getTimeInMillis());
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMin = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(endTime.getTimeInMillis());
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMin = calendar.get(Calendar.MINUTE);
        int min = endHour * 60 + endMin - startHour * 60 - startMin;
        if (min <= 0) {
            min += 24 * 60;
        }
        return min;
    }

    private int[] getStartHourAndMinute() {
        int[] time = new int[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime.getTimeInMillis());
        time[0] = calendar.get(Calendar.HOUR_OF_DAY);
        time[1] = calendar.get(Calendar.MINUTE);
        if (!Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion())) {
            time[0] =(time[0] - Utils.getTimeZone() + 24) % 24;
        }
        Log.e(TAG, "getStartHourAndMinute: " + Arrays.toString(time));
        return time;
    }



    public boolean[] getSelectedWeeks() {
        return selectedWeeks;
    }

    private void setSelectedWeeks(boolean[] selectedWeeks) {
        this.selectedWeeks = selectedWeeks;
    }

    public int getIndex() {
        return index;
    }
}
