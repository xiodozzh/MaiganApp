package com.mgtech.blelib.biz;

import android.util.Log;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmwareUpgradeData;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ISystemParam;
import com.mgtech.blelib.entity.LookBandData;
import com.mgtech.blelib.entity.ManualMeasureNewOrder;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.entity.SystemParamData;
import com.mgtech.blelib.utils.AESUtils;
import com.mgtech.blelib.utils.UpgradeFirmwareManager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.CRC32;

/**
 * @author zhaixiang
 */
public class BluetoothOrderBytes implements IOrderBytes {
    private static final String TAG = "BluetoothOrderBytes";
    protected byte[] key;
    protected byte[] vector;
    protected UpgradeFirmwareManager upgradeFileManager;
    protected IBraceletInfoManager manager;
    protected RandomCodeGenerator generator;

    public BluetoothOrderBytes(IBraceletInfoManager manager) {
        this.manager = manager;
        this.generator = RandomCodeGenerator.getInstance();
    }

    /**
     * 校验
     */
    @Override
    public byte[] verify(byte[] address, int pairCode) {
        // 4位crc结果
        byte[] plainBuf = new byte[15];
        plainBuf[0] = BluetoothConfig.CODE_AUTH_LINK;
        plainBuf[1] = generator.generate();
        System.arraycopy(address, 0, plainBuf, 4, address.length);
        plainBuf[10] = (byte) (pairCode & 0xFF);
        plainBuf[11] = (byte) ((pairCode >> 8) & 0xFF);
        plainBuf[12] = BleConstant.PROTOCOL_VERSION[0];
        plainBuf[13] = BleConstant.PROTOCOL_VERSION[1];
        plainBuf[14] = BleConstant.PROTOCOL_VERSION[2];
        byte[] beforeCrc = new byte[8];
        System.arraycopy(plainBuf, 4, beforeCrc, 0, beforeCrc.length);
        byte[] crcResult = intToByteArray(getCRC32(beforeCrc));
        plainBuf[2] = (byte) (crcResult[0] ^ crcResult[3]);
        plainBuf[3] = (byte) (crcResult[1] ^ crcResult[2]);
        Log.i(TAG, "verify: " + Arrays.toString(plainBuf));
        // 共12字节明文
        return AESUtils.encrypt(getNormalEncryptKey(address), plainBuf, getNormalEncryptVector(address, pairCode));
    }

    @Override
    public byte[] deadCode() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        Log.e(TAG, "deadCode: ");
        byte[] buf = new byte[]{BluetoothConfig.CODE_DISCONNECT_LINK, 0};
        return AESUtils.encrypt(key, buf, vector);
    }

    /**
     * 绑定请求指令
     *
     * @param address  地址
     * @param pairCode 配对码
     * @return 指令
     */
    @Override
    public byte[] requestPairOrder(byte[] address, int pairCode) {
        byte[] plainBuf = new byte[13];
        plainBuf[0] = BluetoothConfig.CODE_REQUEST_PAIR;
        plainBuf[1] = generator.generate();
        System.arraycopy(address, 0, plainBuf, 6, address.length);
        plainBuf[12] = (byte) (pairCode & 0xFF);
        // 认证数据
        plainBuf[2] = (byte) (plainBuf[1] ^ plainBuf[6]);
        plainBuf[3] = (byte) (plainBuf[7] ^ plainBuf[10]);
        plainBuf[4] = (byte) (plainBuf[8] ^ plainBuf[11]);
        plainBuf[5] = (byte) (plainBuf[9] ^ plainBuf[12]);
        // 共12位明文
        return AESUtils.encrypt(getNormalEncryptKey(address), plainBuf, getNormalEncryptVector(address, pairCode));
    }

    @Override
    public byte[] commitPairCode(byte[] key, byte[] vector) {
        byte[] plainBuf = new byte[]{BluetoothConfig.CODE_SAVE_PAIR_INFO,
                generator.generate(),
                (byte) (key[1] ^ vector[15]),
                (byte) (key[8] ^ vector[3])
        };
        Log.e(TAG, "commitPair" + Arrays.toString(plainBuf));
        return AESUtils.encrypt(key, plainBuf, vector);
    }

    @Override
    public byte[] unPairCode(int pairCode) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_UNBIND,
                generator.generate(),
                (byte) (key[1] ^ vector[11]),
                (byte) (key[8] ^ pairCode),
                BleConstant.CLOSE_AFTER_UNPAIR ? 0x01 : 0x00
        };
        Log.i(TAG, "unPair: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    /**
     * 升级信息
     *
     * @param data 信息
     * @return 加密信息
     */
    @Override
    public byte[] upgradeInfoOrder(UpgradeFirmwareManager data) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[16];
        upgradeFileManager = data;
        byte[] crcResult = upgradeFileManager.getCrc();
        byte[] version = upgradeFileManager.getVersion();
        int total = upgradeFileManager.getTotalPkgNumber();

        buf[0] = BluetoothConfig.CODE_FOTA_INFO;
        buf[1] = generator.generate();
        buf[2] = (byte) ((total & 0xFF) ^ crcResult[0]);
        buf[3] = (byte) (((total >> 8) & 0xFF) ^ crcResult[1]);
        buf[4] = version[0];
        buf[5] = version[1];
        buf[6] = version[2];
        buf[7] = (byte) data.getMirror();
        buf[8] = (byte) (UpgradeFirmwareManager.FILE_FRAGMENT_LENGTH & 0xFF);
        buf[9] = (byte) ((UpgradeFirmwareManager.FILE_FRAGMENT_LENGTH >> 8) & 0xFF);
        buf[10] = (byte) (total & 0xFF);
        buf[11] = (byte) ((total >> 8) & 0xFF);
        buf[12] = crcResult[0];
        buf[13] = crcResult[1];
        buf[14] = crcResult[2];
        buf[15] = crcResult[3];
        Log.e(TAG, "upgrade info : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] upgradeInfoOrder(FirmwareUpgradeData data) {
        throw new RuntimeException("指令不存在");
    }

    /**
     * 升级包下一包
     *
     * @return 加密指令
     */
    @Override
    public byte[] upgradeBodyOrder() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        if (upgradeFileManager != null) {
            byte[] indexAndBody = upgradeFileManager.getNextBody();
            byte[] buf = new byte[indexAndBody.length + 2];
            buf[0] = BluetoothConfig.CODE_FOTA_DATA;
            buf[1] = generator.get();
            System.arraycopy(indexAndBody, 0, buf, 2, indexAndBody.length);
            Log.e(TAG, "upgrade body : " + Arrays.toString(buf));
            return AESUtils.encrypt(key, buf, vector);
        } else {
            return null;
        }
    }

    /**
     * 升级结束指令
     *
     * @return 加密指令
     */
    @Override
    public byte[] upgradeCompleteOrder() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_FOTA_END,
                generator.generate()
        };
        Log.e(TAG, "upgrade complete : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] getManualDataOrder(ManualMeasureNewOrder manualMeasureOrder) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[15];
        buf[0] = BluetoothConfig.CODE_START_MANUAL_SAMPLE;
        buf[1] = (byte) (manualMeasureOrder.getTag() & 0xFF);
        buf[3] = (byte) (manualMeasureOrder.getType() & 0xFF);
        buf[4] = (byte) (manualMeasureOrder.getEnable() & 0xFF);
        buf[5] = (byte) ((manualMeasureOrder.getRateType()) & 0xFF);
        buf[6] = (byte) (manualMeasureOrder.getRateValue() & 0xFF);
        buf[7] = (byte) ((manualMeasureOrder.getRateValue() >> 8) & 0xFF);
        buf[8] = (byte) (manualMeasureOrder.isRegionAvailable() ? 1 : 0);
        buf[9] = (byte) ((manualMeasureOrder.getRegionMin()) & 0xFF);
        buf[10] = (byte) ((manualMeasureOrder.getRegionMin() >> 8) & 0xFF);
        buf[11] = (byte) (manualMeasureOrder.getRegionMax() & 0xFF);
        buf[12] = (byte) ((manualMeasureOrder.getRegionMax() >> 8) & 0xFF);
        buf[13] = (byte) (manualMeasureOrder.getRealTimeDoublePointNumber());
        buf[14] = (byte) (manualMeasureOrder.getResultDoublePointNumber());

        buf[2] = (byte) (buf[4] ^ buf[11]);
        Log.i(TAG, "getManualData: start" + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] stopManualDataOrder() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = {
                BluetoothConfig.CODE_STOP_MANUAL_SAMPLE,
                generator.generate()
        };
        Log.i(TAG, "stopManualDataOrder: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] autoInfoOrder(byte[] mac) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = {
                BluetoothConfig.CODE_GET_AUTO_SAMPLE_INFO,
                generator.generate(),
                (byte) (mac[1] ^ key[1]),
                (byte) (mac[0] ^ vector[0]),
        };
        Log.i(TAG, "getAutoInfo: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] getStoredDataOrder(byte[] mac, int pairCode, boolean isAuto, int autoSampleDoublePointSize) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] pair = longToByteArray(pairCode);
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_GET_STORED_SAMPLE_RET,
                generator.generate(),
                (byte) (mac[3] ^ mac[1]),
                (byte) (mac[2] ^ pair[0]),
                (byte) (isAuto ? 0x01 : 0x00),
                (byte) (autoSampleDoublePointSize & 0xFF)
        };
        Log.i(TAG, "getStoredDataOrder: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] deleteSampleData(byte[] crc, boolean isAuto) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_DEL_STORED_SAMPLE_RET,
                generator.generate(),
                (byte) (crc[0] ^ crc[3]),
                (byte) (crc[1] ^ crc[2]),
                (byte) (isAuto ? 1 : 0)

        };
        Log.i(TAG, "delete Auto Sample: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] exeAutoSample(byte[] macAddress, int pairCode) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_REEXE_AUTO_SAMPLE,
                generator.generate(),
                (byte) (macAddress[0] ^ macAddress[2]),
                (byte) (macAddress[1] ^ (pairCode & 0xFF))
        };
        Log.e(TAG, "exe auto sample : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] resetAutoSampleTime(byte[] mac, int pairCode) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_RESET_AUTO_SAMPLE,
                generator.generate(),
                (byte) (mac[1] ^ mac[2]),
                (byte) (mac[0] ^ (pairCode & 0xFF))
        };
        Log.e(TAG, "reset sample time : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    /**
     * 更新血压
     *
     * @param bloodPressure 血压值
     */
    @Override
    public byte[] updateBloodPressureCode(SyncBloodPressure bloodPressure) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] time = longToByteArray(bloodPressure.getTime());
        int ps = bloodPressure.getPs();
        int pd = bloodPressure.getPd();
        int v0 = Math.round(bloodPressure.getV0() * 100);
        byte[] buf = new byte[25];
        buf[0] = BluetoothConfig.CODE_SYNC_BLOOD_PRESSURE;
        buf[1] = generator.generate();

        buf[4] = time[0];
        buf[5] = time[1];
        buf[6] = time[2];
        buf[7] = time[3];
        buf[8] = time[4];
        buf[9] = time[5];
        buf[10] = time[6];
        buf[11] = time[7];
        // 参数数量
        buf[12] = 3;
        // 收缩压
        buf[13] = 1;
        buf[14] = (byte) (ps & 0xFF);
        buf[15] = (byte) ((ps >> 8) & 0xFF);
        buf[16] = (byte) ((bloodPressure.getPsLevel()) & 0xFF);
        // 舒张压
        buf[17] = 2;
        buf[18] = (byte) (pd & 0xFF);
        buf[19] = (byte) ((pd >> 8) & 0xFF);
        buf[20] = (byte) ((bloodPressure.getPdLevel()) & 0xFF);
        // 血粘度
        buf[21] = 13;
        buf[22] = (byte) (v0 & 0xFF);
        buf[23] = (byte) ((v0 >> 8) & 0xFF);
        buf[24] = (byte) ((bloodPressure.getV0Level()) & 0xFF);

        buf[2] = (byte) (buf[4] ^ buf[5]);
        buf[3] = (byte) (buf[6] ^ buf[12]);

        Log.i(TAG, "update blood pressure: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] getSystemParam(byte[] macAddress, byte randomCode, int pairCode, SystemParamData data) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        List<Integer> itemIndex = data.getIndex();
        byte[] buf = new byte[itemIndex.size() + 5];
        buf[0] = BluetoothConfig.CODE_GET_SYS_PARA;
        buf[1] = randomCode;
        buf[4] = (byte) itemIndex.size();
        for (int i = 0; i < itemIndex.size(); i++) {
            buf[5 + i] = (byte) (itemIndex.get(i) & 0xFF);
        }
        buf[2] = (byte) (macAddress[0] ^ macAddress[2]);
        buf[3] = (byte) ((pairCode & 0xFF) ^ buf[4]);
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] setSystemParam(byte[] mac, byte randomCode, int pairCode, SystemParamData data) {
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
                    List<AlertReminder> reminders = reminderData.getReminders();
                    Log.i(TAG, "PARAM_REMINDERS: " + reminderData);
                    int size = reminders.size();
                    code = new byte[2 + size * 6];
                    code[0] = SystemParamData.PARAM_REMINDERS;
                    code[1] = (byte) size;
                    for (int i = 0; i < size; i++) {
                        AlertReminder item = reminders.get(i);
                        code[2 + i * 6] = (byte) (item.isReminderEnable() ? 0x01 : 0x00);
                        code[3 + i * 6] = (byte) item.getReminderWeek();
                        code[4 + i * 6] = (byte) item.getReminderStartHour();
                        code[5 + i * 6] = (byte) item.getReminderStartMinute();
                        code[6 + i * 6] = (byte) (item.getReminderSpanTime() & 0xFF);
                        code[7 + i * 6] = (byte) ((item.getReminderSpanTime() >> 8) & 0xFF);
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

    @Override
    public byte[] getSystemParam(byte[] macAddress, int pairCode, int index) {
        throw new RuntimeException("此版本不支持该指令");
    }

    @Override
    public byte[] setSystemParam(byte[] mac, int pairCode, int paramIndex, ISystemParam paramData) {
        throw new RuntimeException("此版本不支持该指令");
    }

    @Override
    public byte[] getDisplayPage(byte[] mac, int pairCode) {
        return new byte[0];
    }

    @Override
    public byte[] getAlertReminders(byte[] mac, int pairCode) {
        return new byte[0];
    }


    /**
     * 获取电量指令
     *
     * @return 指令
     */
    @Override
    public byte[] getBatteryPower() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[2];
        buf[0] = BluetoothConfig.CODE_BATTERY_INFO;
        buf[1] = generator.generate();
        Log.i(TAG, "get power " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf,
                vector);
    }

    @Override
    public byte[] getFirmwareInfo() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[2];
        buf[0] = BluetoothConfig.CODE_GET_FW_INFO;
        buf[1] = generator.generate();
        Log.i(TAG, "getFirmwareInfo: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }


    @Override
    public byte[] updateTimeCode() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        byte[] timeByte = longToByteArray(time);
        byte[] buf = new byte[13];
        buf[0] = BluetoothConfig.CODE_UPDATE_TIME;
        buf[1] = generator.generate();
        buf[2] = (byte) (timeByte[2] ^ timeByte[3]);
        buf[3] = (byte) (timeByte[4] ^ timeByte[5]);

        buf[4] = timeByte[0];
        buf[5] = timeByte[1];
        buf[6] = timeByte[2];
        buf[7] = timeByte[3];
        buf[8] = timeByte[4];
        buf[9] = timeByte[5];
        buf[10] = timeByte[6];
        buf[11] = timeByte[7];
        buf[12] = (byte) (getTimeZone());
        Log.i(TAG, "updateTime: " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] getHistoryStepDataOrder(int hour) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] cal = intToByteArray(hour);
        byte dataNumber = 6;
        byte[] buf = new byte[9];
        buf[0] = BluetoothConfig.CODE_SYNC_STEP_HISTORY_DATA;
        buf[1] = generator.generate();

        buf[4] = cal[0];
        buf[5] = cal[1];
        buf[6] = cal[2];
        buf[7] = cal[3];
        buf[8] = dataNumber;

        buf[2] = (byte) (buf[4] ^ buf[8]);
        buf[3] = (byte) (buf[5] ^ buf[6]);

        Log.e(TAG, "getHistoryStepData : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] getStepDataOrder() {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_SYNC_STEP_DATA,
                generator.generate(),
                (byte) (key[0] ^ vector[3]),
                (byte) (key[4] ^ vector[7]),
                (byte) (key[6] ^ vector[1]),
                (byte) (key[2] ^ vector[5])
        };
        Log.e(TAG, "getStepData : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] syncStepHistoryDataOrder(CurrentStepData data) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        long hour = data.getHour();
        int quarterOffset = data.getQuarterOffset();
        int quarterStep = data.getQuarterStep();
        int dayStep = data.getDayStep();
        int dayMinute = data.getDayMinute();

        byte[] buf = new byte[19];
        buf[0] = BluetoothConfig.CODE_CALIBRATE_STEP_DATA;
        buf[1] = generator.generate();

        buf[4] = (byte) (hour & 0xFF);
        buf[5] = (byte) ((hour >> 8) & 0xFF);
        buf[6] = (byte) ((hour >> 16) & 0xFF);
        buf[7] = (byte) ((hour >> 24) & 0xFF);
        buf[8] = (byte) quarterOffset;
        buf[9] = (byte) (quarterStep & 0xFF);
        buf[10] = (byte) ((quarterStep >> 8) & 0xFF);
        buf[11] = (byte) ((quarterStep >> 16) & 0xFF);
        buf[12] = (byte) (((quarterStep >> 24) & 0xF) + (quarterStep << 4));
        buf[13] = (byte) (dayStep & 0xFF);
        buf[14] = (byte) ((dayStep >> 8) & 0xFF);
        buf[15] = (byte) ((dayStep >> 16) & 0xFF);
        buf[16] = (byte) ((dayStep >> 24) & 0xFF);
        buf[17] = (byte) (dayMinute & 0xFF);
        buf[18] = (byte) ((dayMinute >> 8) & 0xFF);

        buf[2] = (byte) (buf[6] ^ buf[7]);
        buf[3] = (byte) (buf[9] ^ buf[11]);
        Log.e(TAG, "syncStepHistoryData : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] enableStepDataUpdateOrder(boolean enable) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_SYNC_STEP_DATA_CTRL,
                generator.generate(),
                (byte) (key[6] ^ vector[5]),
                (byte) (key[4] ^ vector[1]),
                (byte) (key[0] ^ vector[7]),
                (byte) (key[2] ^ vector[3]),
                (byte) (enable ? 0x01 : 0x00)
        };
        Log.e(TAG, "enable step data update : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    @Override
    public byte[] lookForBand(LookBandData data) {
        if (checkIfKeyAndVectorNull()) {
            return null;
        }
        byte[] buf = new byte[]{
                BluetoothConfig.CODE_LOOK_FOR_BAND,
                generator.generate(),
                (byte) (key[3] ^ vector[4]),
                (byte) (key[1] ^ vector[2]),
                (byte) (data.getScreen()),
                (byte) (data.getVibrate())
        };
        Log.e(TAG, "lookForBand : " + Arrays.toString(buf));
        return AESUtils.encrypt(key, buf, vector);
    }

    /**
     * 本地时区
     *
     * @return 时区
     */
    private int getTimeZone() {
        return TimeZone.getDefault().getRawOffset() / 1000 / 3600;
    }

    /**
     * int 转换为byte
     *
     * @param i long
     * @return 0 低位 4 高位
     */
    private byte[] intToByteArray(long i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * long 转换为byte
     *
     * @param i long
     * @return 0 低位 4 高位
     */
    private byte[] longToByteArray(long i) {
        byte[] result = new byte[8];
        result[7] = (byte) ((i >> 56) & 0xFF);
        result[6] = (byte) ((i >> 48) & 0xFF);
        result[5] = (byte) ((i >> 40) & 0xFF);
        result[4] = (byte) ((i >> 32) & 0xFF);
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 获得crc校验结果
     *
     * @param data 数据
     * @return 校验值
     */
    private long getCRC32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

    /**
     * 获取普通密钥
     *
     * @return 密钥
     */
    private byte[] getNormalEncryptKey(byte[] address) {
        byte[] key = new byte[16];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, key, 0, 7);
        key[7] = address[0];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, key, 8, 7);
        key[15] = address[2];
        Log.e(TAG, "普通密钥: " + Arrays.toString(key));
        return key;
    }

    /**
     * 获取普通加密向量
     *
     * @return 向量
     */
    private byte[] getNormalEncryptVector(byte[] address, int pairCode) {
        byte[] vector = new byte[16];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, vector, 0, 7);
        vector[7] = address[1];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, vector, 8, 7);
        vector[15] = (byte) (pairCode & 0xFF);
        Log.e(TAG, "普通向量: " + Arrays.toString(vector));
        return vector;
    }

    /**
     * 检查是否key或vector为空
     *
     * @return true：空
     */
    private boolean checkIfKeyAndVectorNull() {
//        if (key == null || vector == null){
        key = manager.getEncryptKey();
        vector = manager.getEncryptVector();
//        }
        return key == null || vector == null;
    }


}
