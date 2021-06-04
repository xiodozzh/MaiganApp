package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.blelib.utils.Utils;
import com.mgtech.maiganapp.data.model.AlertReminderModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhaixiang
 * @date 2017/8/2
 * 手环自动采样提醒
 */

public class BraceletReminderViewModel extends BaseBleViewModel {
    private static final int STYLE_CONTINUOUS = 0;
    private static final int STYLE_DAILY = 1;
    public List<AlertReminderModel> data;
    public ArrayList<AlertReminder> reminders;
    public final ObservableBoolean dataLoadSuccess = new ObservableBoolean(false);
    public final ObservableBoolean actionFail = new ObservableBoolean(false);
    public final ObservableBoolean isEmpty = new ObservableBoolean(true);
    public int updateIndex;
    public int periodStyle;
    private IBraceletInfoManager manager;
    public boolean timeZoneSet;

    public BraceletReminderViewModel(Application context) {
        super(context);
        this.manager = new BraceletInfoManagerBuilder(context).create();
        this.data = new ArrayList<>();
    }

    public void loadReminders() {
        ReminderData reminderData = manager.getReminderData();
        reminders = new ArrayList<>(reminderData.getReminders());
        List<AutoMeasurePeriodData> periods = reminderData.getPeriods();
        if (periods != null && !periods.isEmpty()) {
            periodStyle = STYLE_DAILY;
        } else {
            periodStyle = STYLE_CONTINUOUS;
        }
        timeZoneSet = reminderData.isTimeZoneSet();
        setData(reminders, timeZoneSet);
        dataLoadSuccess.set(!dataLoadSuccess.get());
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_SET_ALERT_REMINDERS_SUCCESS:
                Logger.e("STATUS_SET_ALERT_REMINDERS_SUCCESS");
                ReminderData reminderData = manager.getReminderData();
                List<AutoMeasurePeriodData> periods = reminderData.getPeriods();
                if (periods != null && !periods.isEmpty()) {
                    periodStyle = STYLE_DAILY;
                } else {
                    periodStyle = STYLE_CONTINUOUS;
                }
                timeZoneSet = reminderData.isTimeZoneSet();
                setData(reminders, timeZoneSet);
                dataLoadSuccess.set(!dataLoadSuccess.get());
                break;
            case BleStatusConstant.STATUS_DISCONNECTED:
                if ( manager.isPaired()) {
                    linkIfAllowed();
                }
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
            } else {
                stateViewModel.unbind();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new ReminderResponseCallback();
    }

    private class ReminderResponseCallback extends DefaultResponseCallback{
        @Override
        protected void onAlertReminders(ReminderData reminders) {
            super.onAlertReminders(reminders);
            Log.i(TAG, "onAlertReminders: "+ reminders);
            loadReminders();
        }
    }

    /**
     * 检查是否已经连接
     */
    public void checkLink() {
        boolean isPaired = manager.isPaired();
        if (isPaired) {
            if (isBleOn()) {
                bleRequest.bluetoothGetLinkStatus();
            } else {
                stateViewModel.bluetoothClose();
            }
        } else {
            stateViewModel.unbind();
        }
    }


    public void refresh(int index) {
        this.reminders.clear();
        ReminderData reminderData = manager.getReminderData();
        this.reminders.addAll(reminderData.getReminders());
        setData(reminders, timeZoneSet);
        if (index == -1) {
            dataLoadSuccess.set(!dataLoadSuccess.get());
        } else {
            updateIndex = index;
            dataLoadSuccess.set(!dataLoadSuccess.get());
        }
    }

    public void deleteReminder(int position) {
        if (position < 0 || position >= reminders.size()){
            return;
        }
        reminders.remove(position);
        updateIndex = position;
        ReminderData reminderData = new ReminderData();
        reminderData.setTimeZoneSet(timeZoneSet);
        reminderData.setReminders(reminders);
        bleRequest.bluetoothSetAutoReminder(reminderData);
    }

    /**
     * 是否开启
     *
     * @param position 位置
     * @param enable   是否开启
     */
    public void enableChange(int position, boolean enable) {
        if (position < 0) {
            return;
        }
        AlertReminder reminder = reminders.get(position);
        reminder.setReminderEnable(enable);
        updateIndex = position;
        ReminderData reminderData = new ReminderData();
        reminderData.setTimeZoneSet(timeZoneSet);
        reminderData.setReminders(reminders);
        bleRequest.bluetoothSetAutoReminder(reminderData);
    }

    public void changeAutoMeasure(int style) {
        ReminderData reminderData = new ReminderData();
        reminderData.setTimeZoneSet(timeZoneSet);
        reminderData.setReminders(reminders);
        if (style == STYLE_CONTINUOUS) {
            reminderData.setPeriods(new ArrayList<>());
        } else {
            List<AutoMeasurePeriodData> list = new ArrayList<>();
            AutoMeasurePeriodData data = new AutoMeasurePeriodData();
            data.setInterval(180);
            data.setCycle(127);
            data.setEnable(true);
            data.setStartHour(6);
            data.setStartMin(0);
            data.setSpan(60 * 15);
            list.add(data);
            reminderData.setPeriods(list);
        }
        bleRequest.bluetoothSetAutoReminderAndInteval(reminderData);
    }


    private void setData(List<AlertReminder> paramData, boolean timeZoneSet) {
        data.clear();
        for (int i = 0; i < paramData.size(); i++) {
            AlertReminder reminder = paramData.get(i);
            AlertReminderModel item = new AlertReminderModel();
            Calendar startTime = getLocalCalendar(reminder.getReminderStartHour(), reminder.getReminderStartMinute());
            item.setCycle(AlertReminderModel.getDisplayRemindCycle(reminder.getReminderWeek(), startTime.get(Calendar.HOUR_OF_DAY), timeZoneSet));
            item.setLocalWeek(AlertReminderModel.getWeek(reminder.getReminderStartHour(), reminder.getReminderWeek(), timeZoneSet));
            item.setIndex(i);
            item.setSpanTime(reminder.getReminderSpanTime());
            item.setStartTime(startTime);
            item.setStartHour(reminder.getReminderStartHour());
            item.setStartMin(reminder.getReminderStartMinute());
            item.setEnable(reminder.isReminderEnable());
            data.add(item);
        }
        dataLoadSuccess.set(!dataLoadSuccess.get());
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
        if (!Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion())) {
            c.add(Calendar.HOUR_OF_DAY, Utils.getTimeZone());
        }
        return c;
    }



}
