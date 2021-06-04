package com.mgtech.blelib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 * @date 2018/1/15
 * 手环信息
 */

public class BraceletInfoMMKVManager implements IBraceletInfoManager {
    private static final String PREF_ENCRYPT_KEY = "encryptKey";
    private static final String PREF_ENCRYPT_VECTOR = "encryptVector";
    private static final String PREF_PAIRED_STATUS = "isPaired";
    private static final String PREF_PAIRED_TIME = "pairedTime";
    private static final String PREF_POWER = "power";
    private static final String PREF_POWER_STATUS = "powerStatus";
    private static final String PREF_FIRMWARE_VERSION = "firmwareVersion";
    private static final String PREF_PROTOCOL_VERSION = "protocolVersion";
    private static final String PREF_HARDWARE_VERSION = "hardwareVersion";
    private static final String PREF_PAIR_CODE = "pairCode";
    private static final String PREF_PAIRED_DEVICE_ADDRESS = "pairedDevice";
    private static final String PREF_PAIRED_DEVICE_NAME = "pairedDeviceName";

    private static final String PREF_HIDE_ADJUST_REMIND = "hideAdjustRemind";

    private static final String PREF_STEP_SYNC_TIME = "stepSyncTime";
    private static final String PREF_BRACELET_HAS_NEW_VERSION = "hasNewVersion";
    private static final String PREF_IS_STEP_HISTORY_SYNC = "isStepHistorySync";

    private static final String IS_STEP_HISTORY_SYNC = "isStepHistorySync";

    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_TIME = "preferenceSyncBloodPressureTime";
    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PS = "preferenceSyncBloodPressurePS";
    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PD = "preferenceSyncBloodPressurePD";
    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_V0 = "preferenceSyncBloodPressureV0";
    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL = "preferenceSyncBloodPressurePSLevel";
    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL = "preferenceSyncBloodPressurePDLevel";
    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL = "preferenceSyncBloodPressureV0Level";
    private static final String PREFERENCE_TIME_ZONE = "preferenceTimeZone";

//    private static final String PREFERENCE_SYSTEM_DATA0 = "preferenceSystemData0";
//    private static final String PREFERENCE_SYSTEM_DATA1 = "preferenceSystemData1";
//    private static final String PREFERENCE_SYSTEM_DATA2 = "preferenceSystemData2";
//    private static final String PREFERENCE_SYSTEM_DATA3 = "preferenceSystemData3";
//    private static final String PREFERENCE_SYSTEM_DATA4 = "preferenceSystemData4";
//    private static final String PREFERENCE_SYSTEM_DATA5 = "preferenceSystemData5";

    private static final String PREF_SYNC_TIME = "syncTime";

    private static final String ALERT_NUMBER = "alertNumber";
    private static final String ALERT_WEEKDAY = "alertWeekday";
    private static final String ALERT_START_HOUR = "alertStartHour";
    private static final String ALERT_START_MINUTE = "alertStartMinute";
    private static final String ALERT_SPAN_TIME = "alertSpanTime";
    private static final String ALERT_ENABLE = "alertEnable";
    private static final String ALERT_TIME_ZONE_SET = "alertTimeZoneSet";
    private static final String ALERT_INTERVAL = "alertInterval";

    private static final String AUTO_MEASURE_NUMBER = "autoMeasureNumber";
    private static final String AUTO_MEASURE_WEEKDAY = "autoMeasureWeekday";
    private static final String AUTO_MEASURE_START_HOUR = "autoMeasureStartHour";
    private static final String AUTO_MEASURE_START_MINUTE = "autoMeasureStartMinute";
    private static final String AUTO_MEASURE_SPAN_TIME = "autoMeasureSpanTime";
    private static final String AUTO_MEASURE_ENABLE = "autoMeasureEnable";
    private static final String AUTO_MEASURE_INTERVAL = "autoMeasureInterval";

    private static final String DISPLAY_DATE_PAGE = "datePageDisplay";
    private static final String DISPLAY_BP_PAGE = "bpPageDisplay";
    private static final String DISPLAY_V0_PAGE = "v0PageDisplay";
    private static final String DISPLAY_STEP_PAGE = "stepPageDisplay";
    private static final String DISPLAY_DISTANCE_PAGE = "distancePageDisplay";
    private static final String DISPLAY_HEAT_PAGE = "heatPageDisplay";
    private static final String DISPLAY_POWER_PAGE = "powerPageDisplay";
    private static final String DISPLAY_LANGUAGE_PAGE = "languagePageDisplay";
    /**
     * 解绑的mac，用于同步网络
     */
    private static final String DELETE_MAC = "deleteMac";

    private static final String HEIGHT = "height";
    private static final String WEIGHT = "weight";

    private MMKV mmkv;

    public BraceletInfoMMKVManager(Context context) {
        mmkv = MMKV.defaultMMKV();
    }

    @Override
    public String getDeviceName() {
        return mmkv.decodeString(PREF_PAIRED_DEVICE_NAME);
    }

    @Override
    public void saveDeviceName(String braceletName) {
        if (TextUtils.isEmpty(braceletName)) {
            return;
        }
        mmkv.encode(PREF_PAIRED_DEVICE_NAME, braceletName);
    }

    @Override
    public byte[] getEncryptKey() {
        return mmkv.decodeBytes(PREF_ENCRYPT_KEY);
    }

    @Override
    public byte[] getEncryptVector() {
        return mmkv.decodeBytes(PREF_ENCRYPT_VECTOR);
    }

    @Override
    public int getCodePair() {
        return mmkv.decodeInt(PREF_PAIR_CODE);
    }

    @Override
    public String getAddress() {
        return mmkv.decodeString(PREF_PAIRED_DEVICE_ADDRESS);
    }


    @Override
    public boolean savePairInformation(String deviceName, String address, int pairCode, byte[] key,
                                       byte[] vector, String id, long bindTime, int[] protocolVersion,
                                       int[] firmwareVersion, int[] hardwareVersion) {
        mmkv.encode(PREF_PAIRED_DEVICE_NAME, deviceName);
        mmkv.encode(PREF_PAIRED_STATUS, true);
        mmkv.encode(PREF_PAIRED_DEVICE_ADDRESS, address);
        mmkv.encode(PREF_ENCRYPT_KEY, key);
        mmkv.encode(PREF_ENCRYPT_VECTOR, vector);
        mmkv.encode(PREF_PAIR_CODE, pairCode);
        mmkv.encode(PREF_PAIRED_TIME, bindTime);
        mmkv.encode(PREF_PROTOCOL_VERSION, arrayToString(protocolVersion));
        mmkv.encode(PREF_FIRMWARE_VERSION, arrayToString(firmwareVersion));
        mmkv.encode(PREF_HARDWARE_VERSION, arrayToString(hardwareVersion));
        return true;
    }

    @Override
    public boolean savePairInformation(String deviceName, String address, int pairCode, byte[] key,
                                       byte[] vector, String id, long bindTime, String protocolVersion,
                                       String softwareVersion) {
        return savePairInformation(deviceName, address, pairCode, key, vector, id, bindTime,
                stringToIntArray(protocolVersion), stringToIntArray(softwareVersion), new int[]{});
    }

    @Override
    public boolean isPaired() {
        boolean isPair = mmkv.decodeBool(PREF_PAIRED_STATUS);
        String mac = getAddress();
        byte[] key = getEncryptKey();
        byte[] vector = getEncryptVector();
        return isPair && key != null && vector != null && key.length != 0 && vector.length != 0 && mac != null && !mac.isEmpty();
    }

    @Override
    public void setPairStatus(boolean isPaired) {
        mmkv.encode(PREF_PAIRED_STATUS, isPaired);
    }

    @Override
    public void deletePairInformation() {
        mmkv.removeValuesForKeys(new String[]{
                PREF_PAIRED_DEVICE_NAME,
                PREF_PAIRED_DEVICE_ADDRESS,
                PREF_PAIR_CODE,
                PREF_PAIRED_STATUS,

                PREF_ENCRYPT_KEY,
                PREF_ENCRYPT_VECTOR,
                PREF_PAIRED_TIME,
                PREF_BRACELET_HAS_NEW_VERSION,
                PREF_HIDE_ADJUST_REMIND,
                PREF_SYNC_TIME,
                PREF_STEP_SYNC_TIME,
                PREF_IS_STEP_HISTORY_SYNC,
                PREF_FIRMWARE_VERSION,
                PREF_PROTOCOL_VERSION,
                PREF_HARDWARE_VERSION,
//                PREFERENCE_SYSTEM_DATA0,
//                PREFERENCE_SYSTEM_DATA1,
//                PREFERENCE_SYSTEM_DATA2,
//                PREFERENCE_SYSTEM_DATA3,
//                PREFERENCE_SYSTEM_DATA4,
//                PREFERENCE_SYSTEM_DATA5,
                IS_STEP_HISTORY_SYNC,
                PREFERENCE_SYNC_BLOOD_PRESSURE_TIME,
                PREFERENCE_SYNC_BLOOD_PRESSURE_PS,
                PREFERENCE_SYNC_BLOOD_PRESSURE_PD,
                PREFERENCE_SYNC_BLOOD_PRESSURE_V0,
                PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL,
                PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL,
                PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL,
        });
        removeReminders();
        removeAutoMeasurePeriod();
        removeDisplayPage();
    }

    @Override
    public void saveLatestSyncTime(long time) {
        mmkv.encode(PREF_SYNC_TIME, time);
    }

    @Override
    public long getLatestSyncTime() {
        return mmkv.decodeLong(PREF_SYNC_TIME);
    }

    @Override
    public void saveStepSyncTime(long time) {
        mmkv.encode(PREF_STEP_SYNC_TIME, time);
    }

    @Override
    public long getStepSyncTime() {
        return mmkv.decodeLong(PREF_STEP_SYNC_TIME);
    }

    @Override
    public int[] getDeviceProtocolVersion() {
        return stringToIntArray(getDeviceProtocolVersionString());
    }

    @Override
    public String getDeviceProtocolVersionString() {
        return mmkv.decodeString(PREF_PROTOCOL_VERSION);
    }

    @Override
    public void saveDeviceProtocolVersion(int[] version) {
        mmkv.encode(PREF_PROTOCOL_VERSION, arrayToString(version));
    }

    @Override
    public int[] getDeviceFirmwareVersion() {
        return stringToIntArray(getDeviceFirmwareVersionString());
    }

    @Override
    public String getDeviceFirmwareVersionString() {
        return mmkv.decodeString(PREF_FIRMWARE_VERSION);
    }

    @Override
    public void saveDeviceFirmwareVersion(int[] version) {
        mmkv.encode(PREF_FIRMWARE_VERSION, arrayToString(version));
    }

    @Override
    public int getPower() {
        return mmkv.decodeInt(PREF_POWER);
    }

    @Override
    public int getPowerStatus() {
        return mmkv.decodeInt(PREF_POWER_STATUS);
    }

    @Override
    public void savePower(int power, int powerStatus) {
        mmkv.encode(PREF_POWER, power);
        mmkv.encode(PREF_POWER_STATUS, powerStatus);
    }

    @Override
    public SyncBloodPressure getSyncBloodPressure() {
        long time = mmkv.decodeLong(PREFERENCE_SYNC_BLOOD_PRESSURE_TIME);
        if (time == 0) {
            return null;
        }
        int ps = mmkv.decodeInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PS);
        int pd = mmkv.decodeInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PD);
        float v0 = mmkv.decodeFloat(PREFERENCE_SYNC_BLOOD_PRESSURE_V0);

        int psLevel = mmkv.decodeInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL);
        int pdLevel = mmkv.decodeInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL);
        int v0Level = mmkv.decodeInt(PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL);
        return new SyncBloodPressure(time, ps, psLevel, pd, pdLevel, v0, v0Level);
    }

    @Override
    public void saveSyncBloodPressure(SyncBloodPressure syncBloodPressure) {
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_TIME, syncBloodPressure.getTime());
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_PS, syncBloodPressure.getPs());
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_PD, syncBloodPressure.getPd());
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_V0, syncBloodPressure.getV0());
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL, syncBloodPressure.getPsLevel());
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL, syncBloodPressure.getPdLevel());
        mmkv.encode(PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL, syncBloodPressure.getV0Level());
    }

    @Override
    public long getPairedTime() {
        return mmkv.decodeInt(PREF_PAIRED_TIME);
    }

    @Override
    public void setIsStepHistorySyncToBracelet(boolean needSync) {
        mmkv.encode(IS_STEP_HISTORY_SYNC, needSync);
    }

    @Override
    public boolean isStepHistorySyncToBracelet() {
        return mmkv.decodeBool(IS_STEP_HISTORY_SYNC);
    }

    @Override
    public void setTimeZone(int timeZone) {
        mmkv.encode(PREFERENCE_TIME_ZONE, timeZone);
    }

    @Override
    public int getTimeZone() {
        return mmkv.decodeInt(PREFERENCE_TIME_ZONE);
    }

    @Override
    public void setReminder(ReminderData reminderData) {
        Log.i("save", "setReminder: "+ reminderData);
        List<AlertReminder> alertReminders = reminderData.getReminders();
        removeReminders();
        if (alertReminders != null) {
            int size = alertReminders.size();
            mmkv.putInt(ALERT_NUMBER, size);
            for (int i = 0; i < size; i++) {
                AlertReminder alertReminder = alertReminders.get(i);
                mmkv.putInt(i + ALERT_WEEKDAY, alertReminder.getReminderWeek());
                mmkv.putInt(i + ALERT_START_HOUR, alertReminder.getReminderStartHour());
                mmkv.putInt(i + ALERT_START_MINUTE, alertReminder.getReminderStartMinute());
                mmkv.putInt(i + ALERT_SPAN_TIME, alertReminder.getReminderSpanTime());
                mmkv.putBoolean(i + ALERT_ENABLE, alertReminder.isReminderEnable());
                mmkv.putInt(i + ALERT_INTERVAL, alertReminder.getReminderInterval());
            }
            mmkv.putBoolean(ALERT_TIME_ZONE_SET, reminderData.isTimeZoneSet());
        }
        List<AutoMeasurePeriodData> periods = reminderData.getPeriods();
        if (periods != null) {
            removeAutoMeasurePeriod();
            int periodsSize = periods.size();
            mmkv.putInt(AUTO_MEASURE_NUMBER, periodsSize);
            for (int i = 0; i < periodsSize; i++) {
                AutoMeasurePeriodData item = periods.get(i);
                mmkv.putInt(i + AUTO_MEASURE_WEEKDAY, item.getCycle());
                mmkv.putInt(i + AUTO_MEASURE_START_HOUR, item.getStartHour());
                mmkv.putInt(i + AUTO_MEASURE_START_MINUTE, item.getStartMin());
                mmkv.putInt(i + AUTO_MEASURE_SPAN_TIME, item.getSpan());
                mmkv.putInt(i + AUTO_MEASURE_INTERVAL, item.getInterval());
                mmkv.putBoolean(i + AUTO_MEASURE_ENABLE, item.isEnable());
            }
        }
    }

    @Override
    public ReminderData getReminderData() {
        ReminderData data = new ReminderData();
        int number = mmkv.getInt(ALERT_NUMBER, 0);
        List<AlertReminder> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            AlertReminder alertReminder = new AlertReminder();
            alertReminder.setReminderWeek(mmkv.getInt(i + ALERT_WEEKDAY, 0));
            alertReminder.setReminderStartHour(mmkv.getInt(i + ALERT_START_HOUR, 0));
            alertReminder.setReminderStartMinute(mmkv.getInt(i + ALERT_START_MINUTE, 0));
            alertReminder.setReminderSpanTime(mmkv.getInt(i + ALERT_SPAN_TIME, 0));
            alertReminder.setReminderEnable(mmkv.getBoolean(i + ALERT_ENABLE, false));
            alertReminder.setReminderInterval(mmkv.getInt(i + ALERT_INTERVAL, AlertReminder.CONTINUOUS_INTERVAL));
            list.add(alertReminder);
        }
        data.setReminders(list);
        data.setTimeZoneSet(mmkv.getBoolean(ALERT_TIME_ZONE_SET, false));

        List<AutoMeasurePeriodData> periodDataList = new ArrayList<>();
        int periodDataListSize = mmkv.getInt(AUTO_MEASURE_NUMBER, 0);
        for (int i = 0; i < periodDataListSize; i++) {
            AutoMeasurePeriodData item = new AutoMeasurePeriodData();
            item.setCycle(mmkv.getInt(i + AUTO_MEASURE_WEEKDAY, 0));
            item.setStartHour(mmkv.getInt(i + AUTO_MEASURE_START_HOUR, 0));
            item.setStartMin(mmkv.getInt(i + AUTO_MEASURE_START_MINUTE, 0));
            item.setSpan(mmkv.getInt(i + AUTO_MEASURE_SPAN_TIME, 0));
            item.setInterval(mmkv.getInt(i + AUTO_MEASURE_INTERVAL, 0));
            item.setEnable(mmkv.getBoolean(i + AUTO_MEASURE_ENABLE, false));
            periodDataList.add(item);
        }
        data.setPeriods(periodDataList);
        return data;
    }

    @Override
    public void clearReminder() {
        removeReminders();
    }

    @Override
    public void clearDisplay() {
        removeDisplayPage();
    }

    @Override
    public void setDisplayPage(DisplayPage displayPage) {
        mmkv.putInt(DISPLAY_BP_PAGE, displayPage.getBpPageDisplay());
        mmkv.putInt(DISPLAY_DATE_PAGE, displayPage.getDatePageDisplay());
        mmkv.putInt(DISPLAY_DISTANCE_PAGE, displayPage.getDistancePageDisplay());
        mmkv.putInt(DISPLAY_V0_PAGE, displayPage.getV0PageDisplay());
        mmkv.putInt(DISPLAY_HEAT_PAGE, displayPage.getHeatPageDisplay());
        mmkv.putInt(DISPLAY_STEP_PAGE, displayPage.getStepPageDisplay());
        mmkv.putInt(DISPLAY_POWER_PAGE, displayPage.getPowerPageDisplay());
        mmkv.putInt(DISPLAY_LANGUAGE_PAGE, displayPage.getLang());
    }

    @Override
    public DisplayPage getDisplayPage() {
        DisplayPage displayPage = new DisplayPage();
        displayPage.setBpPageDisplay(mmkv.getInt(DISPLAY_BP_PAGE, DisplayPage.OFF));
        displayPage.setDatePageDisplay(mmkv.getInt(DISPLAY_DATE_PAGE, DisplayPage.DATE_COMPLEX));
        displayPage.setV0PageDisplay(mmkv.getInt(DISPLAY_V0_PAGE, DisplayPage.OFF));
        displayPage.setStepPageDisplay(mmkv.getInt(DISPLAY_STEP_PAGE, DisplayPage.OFF));
        displayPage.setDistancePageDisplay(mmkv.getInt(DISPLAY_DISTANCE_PAGE, DisplayPage.OFF));
        displayPage.setLang(mmkv.getInt(DISPLAY_LANGUAGE_PAGE, DisplayPage.CHINESE));
        displayPage.setHeatPageDisplay(mmkv.getInt(DISPLAY_HEAT_PAGE, DisplayPage.OFF));
        return displayPage;
    }

    @Override
    public void setHeightWeight(int height, int weight) {
        mmkv.putInt(HEIGHT, height);
        mmkv.putInt(WEIGHT, weight);
    }

    @Override
    public HeightWeightData getHeightWeight() {
        HeightWeightData heightWeightData = new HeightWeightData();
        heightWeightData.setWeight(mmkv.getInt(WEIGHT, BleConstant.DEFAULT_WEIGHT));
        heightWeightData.setHeight(mmkv.getInt(HEIGHT, BleConstant.DEFAULT_HEIGHT));
        return heightWeightData;
    }

    @Override
    public void setDeleteMac(String mac) {
        mmkv.putString(DELETE_MAC, mac);
    }

    @Override
    public String getDeleteMac() {
        return mmkv.getString(DELETE_MAC, "");
    }


    private void removeDisplayPage() {
        mmkv.removeValuesForKeys(new String[]{
                DISPLAY_BP_PAGE,
                DISPLAY_DATE_PAGE,
                DISPLAY_V0_PAGE,
                DISPLAY_STEP_PAGE,
                DISPLAY_DISTANCE_PAGE,
                DISPLAY_LANGUAGE_PAGE,
                DISPLAY_HEAT_PAGE
        });
    }

    private void removeReminders() {
        mmkv.remove(ALERT_NUMBER);
        mmkv.remove(ALERT_TIME_ZONE_SET);
        for (int i = 0; i < AlertReminder.MAX_NUMBER; i++) {
            mmkv.removeValuesForKeys(new String[]{
                    i + ALERT_WEEKDAY,
                    i + ALERT_START_HOUR,
                    i + ALERT_START_MINUTE,
                    i + ALERT_SPAN_TIME,
                    i + ALERT_ENABLE,
                    i + ALERT_INTERVAL
            });
        }
    }

    private void removeAutoMeasurePeriod() {
        mmkv.remove(AUTO_MEASURE_NUMBER);
        for (int i = 0; i < AutoMeasurePeriodData.MAX_NUMBER; i++) {
            mmkv.removeValuesForKeys(new String[]{
                    i + AUTO_MEASURE_WEEKDAY,
                    i + AUTO_MEASURE_START_HOUR,
                    i + AUTO_MEASURE_START_MINUTE,
                    i + AUTO_MEASURE_SPAN_TIME,
                    i + AUTO_MEASURE_ENABLE,
                    i + AUTO_MEASURE_INTERVAL,
            });
        }
    }

    private static String arrayToString(int[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i : array) {
            builder.append(i);
            builder.append(".");
        }
        if (builder.length() != 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private static int[] stringToIntArray(String string) {
        if (TextUtils.isEmpty(string)) {
            return new int[0];
        }
        String[] arrayString = string.split("\\.");
        int[] version = new int[arrayString.length];
        try {
            for (int i = 0; i < version.length; i++) {
                version[i] = Integer.parseInt(arrayString[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new int[0];
        }
        return version;
    }
}
