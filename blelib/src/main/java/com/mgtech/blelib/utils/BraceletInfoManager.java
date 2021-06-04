//package com.mgtech.blelib.utils;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.Log;
//
//import com.mgtech.blelib.biz.IBraceletInfoManager;
//import com.mgtech.blelib.constant.BleConstant;
//import com.mgtech.blelib.entity.AlertReminder;
//import com.mgtech.blelib.entity.DisplayPage;
//import com.mgtech.blelib.entity.HeightWeightData;
//import com.mgtech.blelib.entity.SyncBloodPressure;
//
//import java.util.List;
//
///**
// * Created by zhaixiang on 2018/1/15.
// * 手环信息
// */
//
//public class BraceletInfoManager implements IBraceletInfoManager {
//    private static final String TAG = "BraceletInfoManager";
//    public static final boolean ENCRYPT = true;
//    public static final String name = "com.mgtech.www";
//    public static final String CHARSET = "ISO-8859-1";
//    private static final String CHARSET2 = "gb2312";
//
//    private static final String PREF_ENCRYPT_KEY = "encryptKey";
//    private static final String PREF_ENCRYPT_VECTOR = "encryptVector";
//    private static final String PREF_PAIRED_STATUS = "isPaired";
//    private static final String PREF_PAIRED_TIME = "pairedTime";
//    private static final String PREF_POWER = "power";
//    private static final String PREF_POWER_STATUS = "powerStatus";
//    private static final String PREF_FIRMWARE_VERSION = "firmwareVersion";
//    private static final String PREF_PROTOCOL_VERSION = "protocolVersion";
//    private static final String PREF_HARDWARE_VERSION = "hardwareVersion";
//    private static final String PREF_PAIR_CODE = "pairCode";
//    private static final String PREF_PAIRED_DEVICE_ADDRESS = "pairedDevice";
//    private static final String PREF_PAIRED_DEVICE_NAME = "pairedDeviceName";
//
//    private static final String PREF_HIDE_ADJUST_REMIND = "hideAdjustRemind";
//
//    private static final String PREF_STEP_SYNC_TIME = "stepSyncTime";
//    private static final String PREF_BRACELET_HAS_NEW_VERSION = "hasNewVersion";
//    private static final String PREF_IS_STEP_HISTORY_SYNC = "isStepHistorySync";
//
//    private static final String IS_STEP_HISTORY_SYNC = "isStepHistorySync";
//
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_TIME = "preferenceSyncBloodPressureTime";
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PS = "preferenceSyncBloodPressurePS";
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PD = "preferenceSyncBloodPressurePD";
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_V0 = "preferenceSyncBloodPressureV0";
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL = "preferenceSyncBloodPressurePSLevel";
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL = "preferenceSyncBloodPressurePDLevel";
//    private static final String PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL = "preferenceSyncBloodPressureV0Level";
//    private static final String PREFERENCE_TIME_ZONE = "preferenceTimeZone";
//
//    private static final String PREFERENCE_SYSTEM_DATA0 = "preferenceSystemData0";
//    private static final String PREFERENCE_SYSTEM_DATA1 = "preferenceSystemData1";
//    private static final String PREFERENCE_SYSTEM_DATA2 = "preferenceSystemData2";
//    private static final String PREFERENCE_SYSTEM_DATA3 = "preferenceSystemData3";
//    private static final String PREFERENCE_SYSTEM_DATA4 = "preferenceSystemData4";
//    private static final String PREFERENCE_SYSTEM_DATA5 = "preferenceSystemData5";
//
//    private static final String PREF_SYNC_TIME = "syncTime";
//
//
//    private Context context;
//
//    public BraceletInfoManager(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public String getDeviceName() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        String name = sharedPreferences.getString(PREF_PAIRED_DEVICE_NAME, "");
//        if (TextUtils.isEmpty(name)) {
//            return "";
//        }
//        return decrypt(name);
//    }
//
//    @Override
//    public void saveDeviceName(String braceletName) {
//        if (TextUtils.isEmpty(braceletName)) {
//            return;
//        }
//        Log.i(TAG, "saveDeviceName: " + braceletName);
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        String nameE = encrypt(braceletName);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(PREF_PAIRED_DEVICE_NAME, nameE);
//        editor.apply();
//    }
//
//    @Override
//    public byte[] getEncryptKey() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return decryptBytes(sharedPreferences.getString(PREF_ENCRYPT_KEY, ""));
//    }
//
//    @Override
//    public byte[] getEncryptVector() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return decryptBytes(sharedPreferences.getString(PREF_ENCRYPT_VECTOR, ""));
//    }
//
//    @Override
//    public int getCodePair() {
//        return PreferenceUtil.getIntFromPreferences(context, PREF_PAIR_CODE);
//    }
//
//    @Override
//    public String getAddress() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return decrypt(sharedPreferences.getString(PREF_PAIRED_DEVICE_ADDRESS, ""));
//    }
//
//
//    @Override
//    public boolean savePairInformation(String deviceName, String address, int pairCode, byte[] key,
//                                       byte[] vector, String id, long bindTime, int[] protocolVersion,
//                                       int[] firmwareVersion, int[] hardwareVersion) {
//        String nameE = encrypt(deviceName);
//        String addressE = encrypt(address);
//        String keyE = encryptBytes(key);
//        String vectorE = encryptBytes(vector);
//        if (TextUtils.isEmpty(addressE) || TextUtils.isEmpty(keyE) || TextUtils.isEmpty(vectorE) || TextUtils.isEmpty(nameE)) {
//            Log.e("BleSaveUtils", "savePairInformation: fail");
//            return false;
//        }
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString(PREF_PAIRED_DEVICE_NAME, nameE);
//        editor.putString(PREF_PAIRED_DEVICE_ADDRESS, addressE);
//        editor.putString(PREF_ENCRYPT_KEY, keyE);
//        editor.putString(PREF_ENCRYPT_VECTOR, vectorE);
//        editor.putInt(PREF_PAIR_CODE, pairCode);
//        editor.putLong(PREF_PAIRED_TIME, bindTime);
//        editor.putString(PREF_PROTOCOL_VERSION, arrayToString(protocolVersion));
//        editor.putString(PREF_FIRMWARE_VERSION, arrayToString(firmwareVersion));
//        editor.putString(PREF_HARDWARE_VERSION, arrayToString(hardwareVersion));
//        return editor.commit();
//    }
//
//    @Override
//    public boolean savePairInformation(String deviceName, String address, int pairCode, byte[] key,
//                                       byte[] vector, String id, long bindTime, String protocolVersion,
//                                       String softwareVersion) {
//        return savePairInformation(deviceName, address, pairCode, key, vector, id, bindTime,
//                stringToIntArray(protocolVersion), stringToIntArray(softwareVersion), new int[]{});
//    }
//
//    @Override
//    public boolean isPaired() {
//        boolean isPair = PreferenceUtil.getBooleanFromPreferences(context, PREF_PAIRED_STATUS);
//        String mac = getAddress();
//        byte[] key = getEncryptKey();
//        byte[] vector = getEncryptVector();
//        return isPair && key != null && vector != null && key.length != 0 && vector.length != 0 && mac != null && !mac.isEmpty();
//    }
//
//    @Override
//    public void setPairStatus(boolean isPaired) {
//        PreferenceUtil.safeWriteBooleanToPreferences(context, PREF_PAIRED_STATUS, isPaired);
//    }
//
//    @Override
//    public void deletePairInformation() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove(PREF_PAIRED_STATUS);
//        editor.remove(PREF_ENCRYPT_KEY);
//        editor.remove(PREF_ENCRYPT_VECTOR);
//        editor.remove(PREF_PAIRED_TIME);
//        editor.remove(PREF_BRACELET_HAS_NEW_VERSION);
//        editor.remove(PREF_HIDE_ADJUST_REMIND);
//        editor.remove(PREF_SYNC_TIME);
//        editor.remove(PREF_STEP_SYNC_TIME);
//        editor.remove(PREF_IS_STEP_HISTORY_SYNC);
//        editor.remove(PREF_FIRMWARE_VERSION);
//        editor.remove(PREF_PROTOCOL_VERSION);
//        editor.remove(PREF_HARDWARE_VERSION);
//        editor.remove(PREFERENCE_SYSTEM_DATA0);
//        editor.remove(PREFERENCE_SYSTEM_DATA1);
//        editor.remove(PREFERENCE_SYSTEM_DATA2);
//        editor.remove(PREFERENCE_SYSTEM_DATA3);
//        editor.remove(PREFERENCE_SYSTEM_DATA4);
//        editor.remove(PREFERENCE_SYSTEM_DATA5);
//        editor.remove(IS_STEP_HISTORY_SYNC);
//
//        editor.remove(PREFERENCE_SYNC_BLOOD_PRESSURE_TIME);
//        editor.remove(PREFERENCE_SYNC_BLOOD_PRESSURE_PS);
//        editor.remove(PREFERENCE_SYNC_BLOOD_PRESSURE_PD);
//        editor.apply();
//    }
//
//    @Override
//    public void saveLatestSyncTime(long time) {
//        PreferenceUtil.writeLongToPreferences(context, PREF_SYNC_TIME, time);
//    }
//
//    @Override
//    public long getLatestSyncTime() {
//        return PreferenceUtil.getLongToPreferences(context, PREF_SYNC_TIME);
//    }
//
//    @Override
//    public void saveStepSyncTime(long time) {
//        PreferenceUtil.writeLongToPreferences(context, PREF_STEP_SYNC_TIME, time);
//    }
//
//    @Override
//    public long getStepSyncTime() {
//        return PreferenceUtil.getLongToPreferences(context, PREF_STEP_SYNC_TIME);
//    }
//
//    @Override
//    public int[] getDeviceProtocolVersion() {
//        return stringToIntArray(getDeviceProtocolVersionString());
//    }
//
//    @Override
//    public String getDeviceProtocolVersionString() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getString(PREF_PROTOCOL_VERSION, "");
//    }
//
//    @Override
//    public void saveDeviceProtocolVersion(int[] version) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(PREF_PROTOCOL_VERSION, arrayToString(version));
//        editor.apply();
//    }
//
//    @Override
//    public int[] getDeviceFirmwareVersion() {
//        return stringToIntArray(getDeviceFirmwareVersionString());
//    }
//
//    @Override
//    public String getDeviceFirmwareVersionString() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getString(PREF_FIRMWARE_VERSION, "");
//    }
//
//    @Override
//    public void saveDeviceFirmwareVersion(int[] version) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(PREF_FIRMWARE_VERSION, arrayToString(version));
//        editor.apply();
//    }
//
//    @Override
//    public int getPower() {
//        Log.i(TAG, "getPower: " + PreferenceUtil.getIntFromPreferences(context, PREF_POWER));
//        return PreferenceUtil.getIntFromPreferences(context, PREF_POWER);
//    }
//
//    @Override
//    public int getPowerStatus() {
//        Log.i(TAG, "getPowerStatus: " + PreferenceUtil.getIntFromPreferences(context, PREF_POWER_STATUS));
//        return PreferenceUtil.getIntFromPreferences(context, PREF_POWER_STATUS);
//    }
//
//    @Override
//    public void savePower(int power, int powerStatus) {
//        PreferenceUtil.writeIntToPreferences(context, PREF_POWER, power);
//        PreferenceUtil.writeIntToPreferences(context, PREF_POWER_STATUS, powerStatus);
//    }
//
//    @Override
//    public SyncBloodPressure getSyncBloodPressure() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceUtil.name,
//                Context.MODE_PRIVATE);
//        long time = sharedPreferences.getLong(PREFERENCE_SYNC_BLOOD_PRESSURE_TIME, 0);
//        if (time == 0) {
//            return null;
//        }
//        int ps = sharedPreferences.getInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PS, 0);
//        int pd = sharedPreferences.getInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PD, 0);
//        float v0 = sharedPreferences.getFloat(PREFERENCE_SYNC_BLOOD_PRESSURE_V0, 0);
//
//        int psLevel = sharedPreferences.getInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL, 0);
//        int pdLevel = sharedPreferences.getInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL, 0);
//        int v0Level = sharedPreferences.getInt(PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL, 0);
//        return new SyncBloodPressure(time, ps, psLevel, pd, pdLevel, v0, v0Level);
//    }
//
//    @Override
//    public void saveSyncBloodPressure(SyncBloodPressure syncBloodPressure) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceUtil.name,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putLong(PREFERENCE_SYNC_BLOOD_PRESSURE_TIME, syncBloodPressure.getTime());
//        editor.putInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PS, syncBloodPressure.getPs());
//        editor.putInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PD, syncBloodPressure.getPd());
//        editor.putFloat(PREFERENCE_SYNC_BLOOD_PRESSURE_V0, syncBloodPressure.getV0());
//
//        editor.putInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PS_LEVEL, syncBloodPressure.getPsLevel());
//        editor.putInt(PREFERENCE_SYNC_BLOOD_PRESSURE_PD_LEVEL, syncBloodPressure.getPdLevel());
//        editor.putInt(PREFERENCE_SYNC_BLOOD_PRESSURE_V0_LEVEL, syncBloodPressure.getV0Level());
//        editor.apply();
//    }
//
//    @Override
//    public long getPairedTime() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceUtil.name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getLong(PREF_PAIRED_TIME, 0);
//    }
//
//    @Override
//    public void setIsStepHistorySyncToBracelet(boolean needSync) {
//        PreferenceUtil.writeBooleanToPreferences(context, IS_STEP_HISTORY_SYNC, needSync);
//    }
//
//    @Override
//    public boolean isStepHistorySyncToBracelet() {
//        return PreferenceUtil.getBooleanFromPreferences(context, IS_STEP_HISTORY_SYNC);
//    }
//
//    @Override
//    public void setTimeZone(int timeZone) {
//        PreferenceUtil.writeIntToPreferences(context, PREFERENCE_TIME_ZONE, timeZone);
//    }
//
//    @Override
//    public int getTimeZone() {
//        return PreferenceUtil.getIntFromPreferences(context, PREFERENCE_TIME_ZONE, 8);
//    }
//
//
//    @Override
//    public void setReminder(List<AlertReminder> alertReminders) {
//
//    }
//
//    @Override
//    public List<AlertReminder> getReminderData() {
//        return null;
//    }
//
//    @Override
//    public void clearReminder() {
//
//    }
//
//    @Override
//    public void clearDisplay() {
//
//    }
//
//    @Override
//    public void setDisplayPage(DisplayPage displayPage) {
//
//    }
//
//    @Override
//    public DisplayPage getDisplayPage() {
//        return null;
//    }
//
//    @Override
//    public void setHeightWeight(int height, int weight) {
//
//    }
//
//    @Override
//    public HeightWeightData getHeightWeight() {
//        return null;
//    }
//
//    @Override
//    public void setDeleteMac(String mac) {
//
//    }
//
//    @Override
//    public String getDeleteMac() {
//        return null;
//    }
//
//    private static byte[] getBytes(String string) {
//        return Base64.decode(string, Base64.DEFAULT);
//    }
//
//    private static String byteToString(byte[] array) {
//        return Base64.encodeToString(array, Base64.DEFAULT);
//    }
//
//
//    private static String encrypt(String plainText) {
//        try {
//            byte[] result = AESUtils.encrypt(BleConstant.PREFE_KEY, plainText.getBytes(CHARSET2), BleConstant.PREFE_VECTOR);
//            if (result == null) {
//                return "";
//            }
//            return byteToString(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private static String encryptBytes(byte[] bytes) {
//        try {
//            byte[] result = AESUtils.encrypt(BleConstant.PREFE_KEY, bytes, BleConstant.PREFE_VECTOR);
//            if (result == null) {
//                return "";
//            }
//            return byteToString(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private static String decrypt(String encryptText) {
//        try {
//            byte[] result = AESUtils.decryptWithException(BleConstant.PREFE_KEY, getBytes(encryptText),
//                    BleConstant.PREFE_VECTOR);
//            return new String(result, CHARSET2);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private static byte[] decryptBytes(String encryptText) {
//        try {
//            return AESUtils.decryptWithException(BleConstant.PREFE_KEY, getBytes(encryptText),
//                    BleConstant.PREFE_VECTOR);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static String arrayToString(int[] array) {
//        StringBuilder builder = new StringBuilder();
//        for (int i : array) {
//            builder.append(i);
//            builder.append(".");
//        }
//        if (builder.length() != 0) {
//            builder.deleteCharAt(builder.length() - 1);
//        }
//        return builder.toString();
//    }
//
//    private static int[] stringToIntArray(String string) {
//        if (TextUtils.isEmpty(string)) {
//            return new int[0];
//        }
//        String[] arrayString = string.split("\\.");
//        int[] version = new int[arrayString.length];
//        try {
//            for (int i = 0; i < version.length; i++) {
//                version[i] = Integer.parseInt(arrayString[i]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new int[0];
//        }
//        return version;
//    }
//}
