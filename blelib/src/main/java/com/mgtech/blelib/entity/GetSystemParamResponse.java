package com.mgtech.blelib.entity;

import android.util.Log;

import com.mgtech.blelib.biz.BluetoothConfig;
import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GetSystemParamResponse {
    private SystemParamData systemParamData;

    public void set(byte[] data,int[] protocolVersion) {
        if (data.length < 4) {
            return;
        }
        if (data[2] == BluetoothConfig.ERROR_NONE) {
            systemParamData = new SystemParamData();
            DisplayPage pageDisplayData = new DisplayPage();
            ReminderData reminderData = new ReminderData();
            int i = 4;
            while (i < data.length) {
                int type = data[i];
                switch (type) {
                    case SystemParamData.PARAM_REMINDERS:
                        int length;
                        if (Utils.doesReminderHaveIntervalFunc(protocolVersion)){
                            length = getReminderDataAfterVersion421(reminderData,data,i);
                        }else if (Utils.doesReminderHaveZoneAndIntervalSetFunc(protocolVersion)){
                            length = getReminderDataAfterVersion420(reminderData,data,i);
                        }else{
                            length = getReminderDataBeforeVersion420(reminderData,data,i);
                        }
                        i += length;
                        break;
                    case SystemParamData.PARAM_LANGUAGE:
                        systemParamData.setLang(data[i + 1]);
                        i += 2;
                        break;
                    case SystemParamData.PARAM_HEIGHT:
                        systemParamData.setHeight(data[i + 1]);
                        i += 2;
                        break;
                    case SystemParamData.PARAM_WEIGHT:
                        systemParamData.setWeight(data[i + 1]);
                        i += 2;
                        break;
                    case SystemParamData.PARAM_DATE:
                        pageDisplayData.setDatePageDisplay(data[i + 1]);
                        systemParamData.setPageDisplayData(pageDisplayData);
                        i += 2;
                        break;
                    case SystemParamData.PARAM_POWER:
                        pageDisplayData.setPowerPageDisplay(data[i + 1]);
                        systemParamData.setPageDisplayData(pageDisplayData);
                        i += 2;
                        break;
                    case SystemParamData.PARAM_CALCULATE_RESULT_DISPLAY:
                        int resultNumber = data[i + 1];
                        for (int j = 0; j < resultNumber; j++) {
                            int t = data[i + 2 + j * 2];
                            switch (t) {
                                case BleConstant.INDEX_PS:
                                    pageDisplayData.setBpPageDisplay(data[i + 3 + j * 2]);
                                    break;
                                case BleConstant.INDEX_PD:
                                    pageDisplayData.setBpPageDisplay(data[i + 3 + j * 2]);
                                    break;
                                case BleConstant.INDEX_V0:
                                    pageDisplayData.setV0PageDisplay(data[i + 3 + j * 2]);
                                    break;
                                default:
                            }
                        }
                        systemParamData.setPageDisplayData(pageDisplayData);
                        i += 2 + resultNumber * 2;
                        break;
                    case SystemParamData.PARAM_SPORT:
                        int sportNum = data[i + 1];
                        for (int j = 0; j < sportNum; j++) {
                            int t = data[i + 2 + j * 2];
                            switch (t) {
                                case BleConstant.PAGE_STEP:
                                    pageDisplayData.setStepPageDisplay(data[i + 3 + j * 2]);
                                    break;
                                case BleConstant.PAGE_DISTANCE:
                                    pageDisplayData.setDistancePageDisplay(data[i + 3 + j * 2]);
                                    break;
                                case BleConstant.PAGE_HEAT:
                                    pageDisplayData.setHeatPageDisplay(data[i + 3 + j * 2]);
                                    break;
                                default:
                            }
                        }
                        systemParamData.setPageDisplayData(pageDisplayData);
                        i += 2 + sportNum * 2;
                        break;
                    case SystemParamData.PARAM_INTERVAL:
                        List<AutoMeasurePeriodData> autoMeasurePeriodDataItems= new ArrayList<>();
                        int autoSize = data[i+1];
                        for (int j = 0; j < autoSize; j++) {
                            AutoMeasurePeriodData item = new AutoMeasurePeriodData();
                            item.setEnable(data[i + 2 + 8 * j]== 1);
                            item.setCycle(data[i + 3 + 8 * j] & 0xFF);
                            item.setStartHour(data[i + 4 + 8 * j] & 0xFF);
                            item.setStartMin(data[i + 5 + 8 * j] & 0xFF);
                            item.setSpan((data[i + 6 + 8 * j] & 0xFF) + ((data[i + 7 + 8 * j] & 0xFF) << 8));
                            item.setInterval((data[i + 8 + 8 * j] & 0xFF) + ((data[i + 9 + 8 * j] & 0xFF) << 8));
                            autoMeasurePeriodDataItems.add(item);
                        }
                        reminderData.setPeriods(autoMeasurePeriodDataItems);
                        systemParamData.setReminderData(reminderData,true);
                        i += 2 + 8 * autoSize;
                        break;
                    default:
                }
            }
        }
    }


    /**
     * 4.2.0之前的获取ReminderData
     * @param data 数据
     * @param i 起始位置
     * @return 消耗长度
     */
    private int getReminderDataBeforeVersion420(ReminderData reminderData,byte[] data, int i){
        int reminderNum = data[i + 1];
        List<AlertReminder> items = new ArrayList<>();
        for (int j = 0; j < reminderNum; j++) {
            AlertReminder item = new AlertReminder();
            item.setReminderEnable(data[i + 2 + 6 * j] == 0x1);
            item.setReminderWeek(data[i + 3 + 6 * j]);
            item.setReminderStartHour(data[i + 4 + 6 * j]);
            item.setReminderStartMinute(data[i + 5 + 6 * j]);
            item.setReminderSpanTime((data[i + 6 + 6 * j] & 0xFF) + ((data[i + 7 + 6 * j] & 0xFF) << 8));
            items.add(item);
        }
        reminderData.setTimeZoneSet(false);
        reminderData.setReminders(items);
        systemParamData.setReminderData(reminderData,false);
        return reminderNum * 6 + 2;
    }


    /**
     * 4.2.0和之后的获取ReminderData
     * @param data 数据
     * @param i 起始位置
     * @return 消耗长度
     */
    private int getReminderDataAfterVersion420(ReminderData reminderData,byte[] data, int i){
        boolean reminderSet = data[i+1] == 1;
        int reminderNum = data[i + 2];
        List<AlertReminder> items = new ArrayList<>();
        for (int j = 0; j < reminderNum; j++) {
            AlertReminder item = new AlertReminder();
            item.setReminderEnable(data[i + 3 + 6 * j] == 0x1);
            item.setReminderWeek(data[i + 4 + 6 * j]);
            item.setReminderStartHour(data[i + 5 + 6 * j]);
            item.setReminderStartMinute(data[i + 6 + 6 * j]);
            item.setReminderSpanTime((data[i + 7 + 6 * j] & 0xFF) + ((data[i + 7 + 6 * j] & 0xFF) << 8));
            items.add(item);
        }
        reminderData.setReminders(items);
        reminderData.setTimeZoneSet(reminderSet);
        systemParamData.setReminderData(reminderData,true);
        return reminderNum * 6 + 3;
    }

    private int getReminderDataAfterVersion421(ReminderData reminderData,byte[] data, int i){
        boolean reminderSet = data[i+1] == 1;
        int reminderNum = data[i + 2];
        List<AlertReminder> items = new ArrayList<>();
        for (int j = 0; j < reminderNum; j++) {
            AlertReminder item = new AlertReminder();
            item.setReminderEnable(data[i + 3 + 8 * j] == 0x1);
            item.setReminderWeek(data[i + 4 + 8 * j]);
            item.setReminderStartHour(data[i + 5 + 8 * j]);
            item.setReminderStartMinute(data[i + 6 + 8 * j]);
            item.setReminderSpanTime((data[i + 7 + 8 * j] & 0xFF) + ((data[i + 8 + 8 * j] & 0xFF) << 8));
            item.setReminderInterval((data[i + 9 + 8 * j] & 0xFF) + ((data[i + 10 + 8 * j] & 0xFF) << 8));
            items.add(item);
        }
        reminderData.setReminders(items);
        reminderData.setTimeZoneSet(reminderSet);
        systemParamData.setReminderData(reminderData,true);
        return reminderNum * 8 + 3;
    }

    public SystemParamData getSystemParamData() {
        return systemParamData;
    }

}
