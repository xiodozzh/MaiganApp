package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.utils.MyConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class BraceletConfigWrapper {
    public static DisplayPage getDisplayPageFromNet(BraceletConfigEntity entity) {
        DisplayPage data = new DisplayPage();
        BraceletConfigEntity.DisplayBean bean = entity.getDisplayInfo();
        if (bean != null) {
            data.setBpPageDisplay(bean.getDisplayBP());
            data.setDatePageDisplay(bean.getDateFormatType());
            data.setPowerPageDisplay(bean.getDisplayPower());
            data.setDistancePageDisplay(bean.getDisplayDistance());
            data.setHeatPageDisplay(bean.getDisplayHeat());
            data.setStepPageDisplay(bean.getDisplayStep());
            data.setV0PageDisplay(bean.getDisplayV0());
        }
        return data;
    }

    public static BraceletConfigEntity paramDataToNetEntity(ReminderData reminderData, DisplayPage displayData) {
        List<AlertReminder> reminderDataList = reminderData.getReminders();
        List<AutoMeasurePeriodData> measureDataList = reminderData.getPeriods();

        BraceletConfigEntity entity = new BraceletConfigEntity();
        entity.setType(MyConstant.BRACELET_TYPE);
        List<BraceletConfigEntity.RemindersBean> reminderList = new ArrayList<>();
        List<BraceletConfigEntity.MeasureBean> measureList = new ArrayList<>();
        if (reminderDataList != null) {
            for (int i = 0; i < reminderDataList.size(); i++) {
                AlertReminder reminder = reminderDataList.get(i);
                BraceletConfigEntity.RemindersBean remindersBean = new BraceletConfigEntity.RemindersBean();
                remindersBean.setEnable(reminder.isReminderEnable()?1:0);
                remindersBean.setRepeat(reminder.getReminderWeek());
                remindersBean.setStartHour(reminder.getReminderStartHour());
                remindersBean.setStartMinute(reminder.getReminderStartMinute());
                remindersBean.setPeriod(reminder.getReminderSpanTime());
                remindersBean.setIsActualTime(reminderData.isTimeZoneSet() ? 1:0);
                remindersBean.setFrequency(reminder.getReminderInterval());
                reminderList.add(remindersBean);
            }
        }
        entity.setReminders(reminderList);

        if (measureDataList != null){
            for (AutoMeasurePeriodData measureData : measureDataList) {
                BraceletConfigEntity.MeasureBean measureBean = new BraceletConfigEntity.MeasureBean();
                measureBean.setEnable(measureData.isEnable()?1:0);
                measureBean.setRepeat(measureData.getCycle());
                measureBean.setStartHour(measureData.getStartHour());
                measureBean.setStartMinute(measureData.getStartMin());
                measureBean.setPeriod(measureData.getSpan());
                measureBean.setFrequency(measureData.getInterval());
                measureList.add(measureBean);
            }
        }
        entity.setMeasureFrequency(measureList);
        BraceletConfigEntity.DisplayBean displayBean = new BraceletConfigEntity.DisplayBean();
        if (displayData != null) {
            displayBean.setDisplayBP(displayData.getBpPageDisplay());
            displayBean.setDateFormatType(displayData.getDatePageDisplay());
            displayBean.setDisplayDistance(displayData.getDistancePageDisplay());
            displayBean.setDisplayHeat(displayData.getHeatPageDisplay());
            displayBean.setDisplayPower(1);
            displayBean.setDisplayHR(1);
            displayBean.setDisplayBP(displayData.getBpPageDisplay());
            displayBean.setDisplayV0(displayData.getV0PageDisplay());
            displayBean.setDisplayStep(displayData.getStepPageDisplay());
        }
        entity.setDisplayInfo(displayBean);
        return entity;
    }

    public static ReminderData getRemindersFromEntity(BraceletConfigEntity entity) {
        List<AlertReminder> reminderList = new ArrayList<>();
        List<AutoMeasurePeriodData> measureList = new ArrayList<>();
        ReminderData reminderData = new ReminderData();
        List<BraceletConfigEntity.RemindersBean> remindersBeans = entity.getReminders();
        List<BraceletConfigEntity.MeasureBean> measureBeans = entity.getMeasureFrequency();
        if (remindersBeans != null) {
            int size = remindersBeans.size();
            for (int i = 0; i < size; i++) {
                BraceletConfigEntity.RemindersBean bean = remindersBeans.get(i);
                AlertReminder alertReminder = new AlertReminder();
                alertReminder.setReminderWeek(bean.getRepeat());
                alertReminder.setReminderStartMinute(bean.getStartMinute());
                alertReminder.setReminderStartHour(bean.getStartHour());
                alertReminder.setReminderSpanTime(bean.getPeriod());
                alertReminder.setReminderEnable(bean.getEnable() == 1);
                alertReminder.setReminderInterval(bean.getFrequency());
                reminderData.setTimeZoneSet(bean.getIsActualTime() == 1);
                reminderList.add(alertReminder);
            }
        }
        reminderData.setReminders(reminderList);

        if (measureBeans != null) {
            int size = measureBeans.size();
            for (int i = 0; i < size; i++) {
                BraceletConfigEntity.MeasureBean bean = measureBeans.get(i);
                AutoMeasurePeriodData measurePeriodData = new AutoMeasurePeriodData();
                measurePeriodData.setCycle(bean.getRepeat());
                measurePeriodData.setStartMin(bean.getStartMinute());
                measurePeriodData.setStartHour(bean.getStartHour());
                measurePeriodData.setSpan(bean.getPeriod());
                measurePeriodData.setEnable(bean.getEnable() == 1);
                measurePeriodData.setInterval(bean.getFrequency());
                measureList.add(measurePeriodData);
            }
        }
        reminderData.setPeriods(measureList);
        return reminderData;
    }
}
