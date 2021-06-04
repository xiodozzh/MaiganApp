package com.mgtech.blelib.biz;

import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ISystemParam;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.entity.SystemParamData;

import java.util.List;

/**
 *
 * @author zhaixiang
 * 手环绑定信息管理
 */

public interface IBraceletInfoManager {
    /**
     * 电量正常
     */
    int POWER_NORMAL = 0x00;
    /**
     * 正在充电
     */
    int POWER_CHARGING = 0x01;
    /**
     * 充电完成
     */
    int POWER_CHARGED = 0x02;

    int POWER_CAN_UPGRADE = 10;

    /**
     * 获取手环名字
     *
     * @return 绑定的手环名
     */
    String getDeviceName();


    /**
     * 保存手环名字
     * @param braceletName 手环名
     */
    void saveDeviceName(String braceletName);

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    byte[] getEncryptKey();

    /**
     * 获取向量
     *
     * @return 向量
     */
    byte[] getEncryptVector();

    /**
     * 获取手环配对码
     *
     * @return 配对码
     */
    int getCodePair();

    /**
     * 获取手环Mac地址
     *
     * @return Mac地址
     */
    String getAddress();


    /**
     * 保存配对信息
     *
     * @param deviceName 设备名
     * @param address    mac地址
     * @param pairCode   配对码
     * @param key        密钥
     * @param vector     向量
     * @param id         设备id
     * @param bindTime   绑定时间
     * @return 是否保存成功
     */
    boolean savePairInformation(String deviceName, String address, int pairCode, byte[] key, byte[] vector, String id, long bindTime,
                                int[] protocolVersion,int[]softwareVersion,int[]hardwareVersion);


    boolean savePairInformation(String deviceName, String address, int pairCode, byte[] key, byte[] vector, String id, long bindTime,
                                String protocolVersion,String softwareVersion);
    /**
     * 是否配对
     *
     * @return true配对
     */
    boolean isPaired();

    /**
     * 设置配对状态
     *
     * @param isPaired true 配对
     */
    void setPairStatus(boolean isPaired);

    /**
     * 删除配对信息
     */
    void deletePairInformation();

    /**
     * 保存最近一次同步时间
     *
     * @param time UTC时间
     */
    void saveLatestSyncTime(long time);

    /**
     * 获取最近一次同步时间
     *
     * @return UTC时间
     */
    long getLatestSyncTime();

    /**
     * 保存计步同步时间
     *
     * @param time UTC时间
     */
    void saveStepSyncTime(long time);

    /**
     * 获取计步同步时间
     *
     * @return UTC时间
     */
    long getStepSyncTime();

    /**
     * 获取通讯协议版本
     *
     * @return int[3]
     */
    int[] getDeviceProtocolVersion();

    String getDeviceProtocolVersionString();

    /**
     * 获取固件版本
     *
     * @return int[3]
     */
    int[] getDeviceFirmwareVersion();

    /**
     * 获取固件版本
     *
     * @return int[3]
     */
    String getDeviceFirmwareVersionString();

    /**
     * 保存通讯协议版本
     *
     * @param version int[3]
     */
    void saveDeviceProtocolVersion(int[] version);

    /**
     * 保存固件版本
     *
     * @param version int[3]
     */
    void saveDeviceFirmwareVersion(int[] version);

    /**
     * 获取电量
     *
     * @return 电量
     */
    int getPower();

    /**
     * 获取电池状态 0 正常，1 充电中，2 充电完成
     *
     * @return 0
     */
    int getPowerStatus();

    /**
     * 保存电量信息
     *
     * @param power       电量
     * @param powerStatus 充电状态
     */
    void savePower(int power, int powerStatus);

    /**
     * 获取需要同步的血压信息
     *
     * @return 血压信息
     */
    SyncBloodPressure getSyncBloodPressure();

    /**
     * 保存需要同步的血压信息
     * 获取完最近一次血压信息后调用此方法，再次和手环连接后将同步数据
     *
     * @param syncBloodPressure 血压信息
     */
    void saveSyncBloodPressure(SyncBloodPressure syncBloodPressure);

    /**
     * 获取绑定时间
     *
     * @return 绑定时间
     */
    long getPairedTime();

    /**
     * 设置是否需要同步计步数据至手环
     *
     * @param needSync false 需要同步
     */
    void setIsStepHistorySyncToBracelet(boolean needSync);

    /**
     * 是否需要同步计步数据至手环
     *
     * @return false 需要同步
     */
    boolean isStepHistorySyncToBracelet();

    /**
     * 保存时区
     *
     * @param timeZone 时区
     */
    void setTimeZone(int timeZone);

    /**
     * 获取时区
     *
     * @return timeZone
     */
    int getTimeZone();

    void setReminder(ReminderData alertReminders);

    ReminderData getReminderData();

    void clearReminder();

    void clearDisplay();

    void setDisplayPage(DisplayPage displayPage);

    DisplayPage getDisplayPage();

    void setHeightWeight(int height, int weight);

    HeightWeightData getHeightWeight();

    void setDeleteMac(String mac);

    String getDeleteMac();
}
