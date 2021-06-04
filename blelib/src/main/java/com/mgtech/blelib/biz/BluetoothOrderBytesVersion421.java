package com.mgtech.blelib.biz;

import android.util.Log;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SystemParamData;
import com.mgtech.blelib.utils.AESUtils;
import com.mgtech.blelib.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BluetoothOrderBytesVersion421 extends BluetoothOrderBytesVersion420 {
    private static final String TAG = "OrderBytes421";

    public BluetoothOrderBytesVersion421(IBraceletInfoManager manager) {
        super(manager);
    }

    @Override
    public byte[] setSystemParam(byte[] mac, byte randomCode, int pairCode, SystemParamData data) {
        if (!Utils.doesReminderHaveIntervalFunc(manager.getDeviceProtocolVersion())) {
            return super.setSystemParam(mac, randomCode, pairCode, data);
        } else {
            List<Integer> list = data.getIndex();
            List<byte[]> codeList = new ArrayList<>();
            int totalCodeNumber = 0;
            DisplayPage pageDisplayData = data.getPageDisplayData();
            HeightWeightData heightWeightData = data.getHeightWeightData();
            ReminderData reminderData = data.getReminderData();
            for (int index : list) {
                byte[] code = new byte[0];
                switch (index) {
                    case SystemParamData.PARAM_REMINDERS:
                        if (reminderData == null || reminderData.getReminders() == null){
                            continue;
                        }
                        List<AlertReminder> reminders = reminderData.getReminders();
                        int size = reminders.size();
                        code = new byte[3 + size * 8];
                        code[0] = SystemParamData.PARAM_REMINDERS;
                        code[1] = 1;
                        code[2] = (byte) size;
                        for (int i = 0; i < size; i++) {
                            AlertReminder item = reminders.get(i);
                            code[3 + i * 8] = (byte) (item.isReminderEnable() ? 0x01 : 0x00);
                            code[4 + i * 8] = (byte) item.getReminderWeek();
                            code[5 + i * 8] = (byte) item.getReminderStartHour();
                            code[6 + i * 8] = (byte) item.getReminderStartMinute();
                            code[7 + i * 8] = (byte) (item.getReminderSpanTime() & 0xFF);
                            code[8 + i * 8] = (byte) ((item.getReminderSpanTime() >> 8) & 0xFF);
                            code[9 + i * 8] = (byte) (item.getReminderInterval() & 0xFF);
                            code[10 + i * 8] = (byte) ((item.getReminderInterval() >> 8) & 0xFF);
                        }
                        break;
                    case SystemParamData.PARAM_LANGUAGE:
                        code = new byte[2];
                        code[0] = SystemParamData.PARAM_LANGUAGE;
                        code[1] = (byte) data.getLang();
                        break;
                    case SystemParamData.PARAM_HEIGHT:
                        code = new byte[3];
                        code[0] = SystemParamData.PARAM_HEIGHT;
                        if (heightWeightData != null) {
                            code[1] = (byte) (heightWeightData.getHeight() & 0xFF);
                            code[2] = (byte) ((heightWeightData.getHeight() >> 8) & 0xFF);
                        }
                        break;
                    case SystemParamData.PARAM_WEIGHT:
                        code = new byte[3];
                        code[0] = SystemParamData.PARAM_WEIGHT;
                        if (heightWeightData != null) {
                            code[1] = (byte) (heightWeightData.getWeight() & 0xFF);
                            code[2] = (byte) ((heightWeightData.getWeight() >> 8) & 0xFF);
                        }
                        break;
                    case SystemParamData.PARAM_DATE:
                        if (pageDisplayData == null) {
                            continue;
                        }
                        code = new byte[2];
                        code[0] = SystemParamData.PARAM_DATE;
                        code[1] = (byte) (pageDisplayData.getDatePageDisplay() & 0xFF);
                        break;
                    case SystemParamData.PARAM_POWER:
                        if (pageDisplayData == null) {
                            continue;
                        }
                        code = new byte[2];
                        code[0] = SystemParamData.PARAM_POWER;
                        code[1] = (byte) (pageDisplayData.getPowerPageDisplay() & 0xFF);
                        break;
                    case SystemParamData.PARAM_CALCULATE_RESULT_DISPLAY:
                        if (pageDisplayData == null) {
                            continue;
                        }
                        code = new byte[2 + pageDisplayData.getBpPageDisplay() * 2 + pageDisplayData.getV0PageDisplay()];
                        code[0] = SystemParamData.PARAM_CALCULATE_RESULT_DISPLAY;
                        code[1] = (byte) (pageDisplayData.getBpPageDisplay() * 2 + pageDisplayData.getV0PageDisplay());
                        if (pageDisplayData.getBpPageDisplay() == DisplayPage.ON) {
                            code[2] = BleConstant.INDEX_PS;
                            code[3] = BleConstant.INDEX_PD;
                            if (pageDisplayData.getV0PageDisplay() == DisplayPage.ON) {
                                code[4] = BleConstant.INDEX_V0;
                            }
                        } else {
                            if (pageDisplayData.getV0PageDisplay() == DisplayPage.ON) {
                                code[2] = BleConstant.INDEX_V0;
                            }
                        }
                        break;
                    case SystemParamData.PARAM_SPORT:
                        if (pageDisplayData == null) {
                            continue;
                        }
                        List<Byte> displayList = new ArrayList<>();
                        if (pageDisplayData.getDistancePageDisplay() == 1) {
                            displayList.add((byte) BleConstant.PAGE_DISTANCE);
                        }
                        if (pageDisplayData.getStepPageDisplay() == 1) {
                            displayList.add((byte) BleConstant.PAGE_STEP);
                        }
                        if (pageDisplayData.getHeatPageDisplay() == 1) {
                            displayList.add((byte) BleConstant.PAGE_HEAT);
                        }
                        code = new byte[displayList.size() + 2];
                        code[0] = SystemParamData.PARAM_SPORT;
                        code[1] = (byte) displayList.size();
                        for (int i = 0; i < displayList.size(); i++) {
                            code[2 + i] = displayList.get(i);
                        }
                        break;
                    case SystemParamData.PARAM_INTERVAL:
                        if (reminderData == null || reminderData.getPeriods() == null) {
                            continue;
                        }
                        List<AutoMeasurePeriodData> autoList = reminderData.getPeriods();
                        int autoSize = autoList.size();
                        code = new byte[2 + autoSize * 8];
                        code[0] = SystemParamData.PARAM_INTERVAL;
                        code[1] = (byte) autoSize;
                        for (int i = 0; i < autoSize; i++) {
                            AutoMeasurePeriodData item = autoList.get(i);
                            code[2 + i * 8] = (byte) (item.isEnable() ? 0x01 : 0x00);
                            code[3 + i * 8] = (byte) item.getCycle();
                            code[4 + i * 8] = (byte) item.getStartHour();
                            code[5 + i * 8] = (byte) item.getStartMin();
                            code[6 + i * 8] = (byte) (item.getSpan() & 0xFF);
                            code[7 + i * 8] = (byte) ((item.getSpan() >> 8) & 0xFF);
                            code[8 + i * 8] = (byte) (item.getInterval() & 0xFF);
                            code[9 + i * 8] = (byte) ((item.getInterval() >> 8) & 0xFF);
                        }
                        break;
                    default:
                }
                totalCodeNumber += code.length;
                codeList.add(code);
            }
            byte[] buf = new byte[totalCodeNumber + 5];
            buf[0] = BluetoothConfig.CODE_SET_SYS_PARA;
            buf[1] = randomCode;
            buf[2] = (byte) (mac[0] ^ mac[2]);
            buf[3] = (byte) (mac[1] ^ (pairCode & 0xFF));
            buf[4] = (byte) codeList.size();
            int start = 5;
            for (byte[] code : codeList) {
                System.arraycopy(code, 0, buf, start, code.length);
                start += code.length;
            }
            Log.i(TAG, "set system param " + Arrays.toString(buf));
            return AESUtils.encrypt(key, buf, vector);
        }

    }
}
