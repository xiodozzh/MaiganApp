package com.mgtech.domain.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.Utils;
import com.tencent.mmkv.MMKV;

import java.util.Map;
import java.util.Objects;

import static com.mgtech.domain.utils.PreferenceUtil.name;

/**
 * @author zhaixiang
 * @date 2018/11/1
 * app 配置信息
 */

public class AppConfigNew {
    private static final String PREFERENCE_SHOW_ECG = "AppConfigShowEcg";
    private static final String PREFERENCE_VERSION = "AppConfigVersion";
    private static final String PREFERENCE_NEED_TO_SHOW_UPGRADE = "AppConfigNeedToShowUpgrade";
    private static final String PREFERENCE_LOG = "AppConfigAppLog";
    private static final String PREFERENCE_APP_URL = "AppConfigAppUrl";
    private static final String PREFERENCE_DEVICE_VERSION = "AppConfigDeviceVersion";
    private static final String PREFERENCE_DEVICE_URL = "AppConfigDeviceUrl";
    private static final String PREFERENCE_DEVICE_FORCE_UPGRADE = "AppConfigDeviceForceUpgrade";
    private static final String PREFERENCE_DEVICE_LOG = "AppConfigDeviceLog";
    private static final String PREFERENCE_WEEKLY_REPORT_GUIDE = "weeklyReportShareGuide";

    private static final int OPEN_ECG = 1;
    private static final int FORCE_UPGRADE = 1;
    private static final int VERSION_NUMBER = 3;

    private static final String DEBUG = "debug";


    public static boolean isShowEcg(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getBoolean(PREFERENCE_SHOW_ECG, false) || MyConstant.FORCE_OPEN_ECG;
//        return sharedPreferences.getBoolean(PREFERENCE_SHOW_ECG, false) || BleConstant.FORCE_OPEN_ECG;
    }

    public static int getVersion(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(PREFERENCE_VERSION, 0);

        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getInt(PREFERENCE_VERSION, 0);
    }

    public static boolean isForceUpgrade(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getBoolean(PREFERENCE_NEED_TO_SHOW_UPGRADE, false);

        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getBoolean(PREFERENCE_NEED_TO_SHOW_UPGRADE, false);
    }


    public static String getAppLog(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getString(PREFERENCE_LOG, "");

        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getString(PREFERENCE_LOG, "");
    }

    public static String getAppUrl(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getString(PREFERENCE_APP_URL, "");

        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getString(PREFERENCE_APP_URL, "");
    }

    public static String getBraceletVersion(Context context, String braceletName, boolean isDebug) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getString(braceletName + (isDebug ? DEBUG : "") + PREFERENCE_DEVICE_VERSION, "");
    }

    public static String getBraceletUrl(Context context, String braceletName, boolean isDebug) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getString(braceletName + PREFERENCE_DEVICE_URL, "");
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getString(braceletName + (isDebug ? DEBUG : "") + PREFERENCE_DEVICE_URL, "");
    }

    private static boolean isBraceletForceUpgrade(Context context, String braceletName, boolean isDebug) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getBoolean(braceletName + (isDebug ? DEBUG : "") + PREFERENCE_DEVICE_FORCE_UPGRADE, false);
    }

    public static String getBraceletLog(Context context, String braceletName, boolean isDebug) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getString(braceletName + PREFERENCE_DEVICE_LOG, "");

        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getString(braceletName + (isDebug ? DEBUG : "") + PREFERENCE_DEVICE_LOG, "");
    }

    public static String getWeeklyReportShareGuideUrl() {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.getString(PREFERENCE_WEEKLY_REPORT_GUIDE, "https://mp.weixin.qq.com/s/LFhwqiGpXT5pXZqMFCoc9A");
    }

    /**
     * 将网络数据保存至本地
     *
     * @param context 上下文
     * @param entity  网络数据
     */
    public static void saveToLocal(Context context, GetAppConfigResponseEntity entity) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        GetAppConfigResponseEntity.AppConfig appConfig = entity.getAppConfig();
        MMKV mmkv = MMKV.defaultMMKV();
        if (appConfig != null) {
            GetAppConfigResponseEntity.AndroidInfo androidInfo = appConfig.getAndroidInfo();
            if (androidInfo != null) {
                mmkv.putInt(PREFERENCE_VERSION, androidInfo.getVersion());
                mmkv.putString(PREFERENCE_LOG, androidInfo.getLog());
                mmkv.putString(PREFERENCE_APP_URL, androidInfo.getUrl());
                mmkv.putBoolean(PREFERENCE_NEED_TO_SHOW_UPGRADE, androidInfo.getOpenUpdate() == FORCE_UPGRADE);
            }
            mmkv.putString(PREFERENCE_WEEKLY_REPORT_GUIDE, appConfig.getWeekReportShareGuideUrl());
            mmkv.putBoolean(PREFERENCE_SHOW_ECG, appConfig.getShowEcg() == OPEN_ECG);
        }
        Map<String, GetAppConfigResponseEntity.DeviceInfo> firmwareInfo = entity.getFirmwareVersionInfo();
        Log.i("save", "saveToLocal: " + firmwareInfo);
        if (firmwareInfo != null) {
            for (Map.Entry<String, GetAppConfigResponseEntity.DeviceInfo> entry : firmwareInfo.entrySet()) {
                String key = entry.getKey();
                GetAppConfigResponseEntity.DeviceInfo deviceInfo = entry.getValue();
                if (deviceInfo == null) {
                    return;
                }
                GetAppConfigResponseEntity.SubVersion release = deviceInfo.getRelease();
                if (release != null) {
                    mmkv.putString(key + PREFERENCE_DEVICE_VERSION, release.getVersion());
                    mmkv.putString(key + PREFERENCE_DEVICE_URL, release.getUrl());
                    mmkv.putBoolean(key + PREFERENCE_DEVICE_FORCE_UPGRADE, release.getOpenUpdate() == FORCE_UPGRADE);
                    mmkv.putString(key + PREFERENCE_DEVICE_LOG, release.getLog());
                }

                GetAppConfigResponseEntity.SubVersion debug = deviceInfo.getDebug();
                if (debug != null) {
                    mmkv.putString(key + DEBUG + PREFERENCE_DEVICE_VERSION, debug.getVersion());
                    mmkv.putString(key + DEBUG + PREFERENCE_DEVICE_URL, debug.getUrl());
                    mmkv.putBoolean(key + DEBUG + PREFERENCE_DEVICE_FORCE_UPGRADE, debug.getOpenUpdate() == FORCE_UPGRADE);
                    mmkv.putString(key + DEBUG + PREFERENCE_DEVICE_LOG, debug.getLog());
                }
            }
        }
    }


    public static boolean isBraceletHasNewVersion(Context context, String deviceVersion, String braceletName,boolean isDebug) {
        String version = AppConfigNew.getBraceletVersion(context, braceletName,isDebug);
        return !TextUtils.isEmpty(version) && versionHasNew(deviceVersion, version);
    }

    public static boolean isBraceletHasNewVersionDebug(Context context, String deviceVersion, String braceletName,boolean isDebug) {
        String version = AppConfigNew.getBraceletVersion(context, braceletName,isDebug);
        return !TextUtils.isEmpty(version) && versionHasNew(deviceVersion, version);
    }

    public static boolean shouldAutoUpgradeBracelet(Context context, String deviceVersion, String braceletName, int devicePower,boolean isCharging, boolean isDebug) {
        return isBraceletHasNewVersion(context, deviceVersion, braceletName,isDebug)
                && AppConfigNew.isBraceletForceUpgrade(context, braceletName,isDebug) && (devicePower >= IBraceletInfoManager.POWER_CAN_UPGRADE || isCharging)
                && Utils.isThereInternetConnection(context);
    }

    public static int[] getBraceletVersionBytes(Context context, String braceletName,boolean isDebug) {
        String version = getBraceletVersion(context, braceletName,isDebug);
        if (TextUtils.isEmpty(version)) {
            return new int[]{0, 0, 0};
        }
        String[] v = version.split("\\.");
        int[] versionBytes = new int[v.length];
        for (int i = 0; i < v.length; i++) {
            versionBytes[i] = Integer.parseInt(v[i]);
        }
        return versionBytes;
    }


    private static boolean versionHasNew(String localVersion, String onlineVersion) {
//            return !Objects.equals(localVersion,onlineVersion);

        if (TextUtils.isEmpty(onlineVersion)) {
            return false;
        }
        if (TextUtils.isEmpty(localVersion)) {
            return true;
        }
        Log.i("test", "versionHasNew: " + localVersion + " " + onlineVersion);
        int[] local = new int[VERSION_NUMBER];
        int[] online = new int[VERSION_NUMBER];
        String[] localVersionFragment = localVersion.split("\\.");
        String[] onlineVersionFragment = onlineVersion.split("\\.");
        try {
            if (localVersionFragment.length != VERSION_NUMBER || onlineVersionFragment.length != VERSION_NUMBER) {
                return true;
            }
            for (int i = 0; i < VERSION_NUMBER; i++) {
                local[i] = Integer.parseInt(localVersionFragment[i]);
                online[i] = Integer.parseInt(onlineVersionFragment[i]);
                if (local[i] < online[i]) {
                    return true;
                } else if (local[i] > online[i]) {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
