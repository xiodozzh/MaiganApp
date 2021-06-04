package com.mgtech.blelib.biz;

import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmwareUpgradeData;
import com.mgtech.blelib.entity.ISystemParam;
import com.mgtech.blelib.entity.LookBandData;
import com.mgtech.blelib.entity.ManualMeasureNewOrder;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.entity.SystemParamData;
import com.mgtech.blelib.utils.UpgradeFirmwareManager;

import java.util.List;

/**
 * @author zhaixiang
 */
public interface IOrderBytes {
    /**
     * 校验指令
     * @param address mac地址
     * @param pairCode 配对码
     * @return 指令
     */
    byte[] verify(byte[] address, int pairCode);

    /**
     * 断开连接指令
     * @return 指令
     */
    byte[] deadCode();

    /**
     * 请求绑定指令
     * @param address mac地址
     * @param pairCode 配对码
     * @return 指令
     */
    byte[] requestPairOrder(byte[] address, int pairCode);

    /**
     * 确认绑定指令
     * @param key 密钥
     * @param vector 向量
     * @return 指令
     */
    byte[] commitPairCode(byte[] key, byte[] vector);

    /**
     * 解绑指令
     * @param pairCode 配对码
     * @return 指令
     */
    byte[] unPairCode(int pairCode);

    /**
     * 升级信息包指令
     * @param data 固件信息
     * @return 指令
     */
    byte[] upgradeInfoOrder(UpgradeFirmwareManager data);

    byte[] upgradeInfoOrder(FirmwareUpgradeData data);


    /**
     * 升级包指令
     * @return 指令
     */
    byte[] upgradeBodyOrder();

    /**
     * 升级结束指令
     * @return 指令
     */
    byte[] upgradeCompleteOrder();

    /**
     * 主动采样指令
     * @param manualMeasureOrder 采样信息
     * @return 指令
     */
    byte[] getManualDataOrder(ManualMeasureNewOrder manualMeasureOrder);

    /**
     * 停止采样指令
     * @return 指令
     */
    byte[] stopManualDataOrder();
    /**
     * 获取自动采样信息指令
     * @param mac mac地址
     * @return 指令
     */
    byte[] autoInfoOrder(byte[] mac);

    /**
     * 获取储存的数据指令
     * @param mac mac地址
     * @param pairCode 配对码
     * @param isAuto 是否是自动采样
     * @param doublePointNumber 每包发送双点个数
     * @return 指令
     */
    byte[] getStoredDataOrder(byte[] mac, int pairCode, boolean isAuto, int doublePointNumber);

    /**
     * 删除采样数据指令
     * @param crc crc验证码
     * @param isAuto 是否是自动采样数据
     * @return 指令
     */
    byte[] deleteSampleData(byte[] crc, boolean isAuto);

    /**
     * 查看电量指令
     * @return 指令
     */
    byte[] getBatteryPower();

    /**
     * 获取固件信息
     * @return 指令
     */
    byte[] getFirmwareInfo();

    /**
     * 执行一次自动采样指令
     * @param macAddress mac地址
     * @param pairCode 配对码
     * @return 指令
     */
    byte[] exeAutoSample(byte[] macAddress, int pairCode);
    /**
     * 重置自动采样时间指令
     * @param mac mac地址
     * @param pairCode 配对码
     * @return 指令
     */
    byte[] resetAutoSampleTime(byte[] mac, int pairCode);

    /**
     * 设置血压指令
     * @param bloodPressure 血压
     * @return 指令
     */
    byte[] updateBloodPressureCode(SyncBloodPressure bloodPressure);

    /**
     * 获取系统参数指令
     * @param macAddress mac地址
     * @param pairCode 配对码
     * @param array 参数编号列表
     * @return 指令
     */
    byte[] getSystemParam(byte[] macAddress,byte randomCode, int pairCode, SystemParamData array);

    byte[] setSystemParam(byte[] mac,byte randomCode, int pairCode, SystemParamData paramData);
    /**
     * 获取系统参数指令
     * @param macAddress mac地址
     * @param pairCode 配对码
     * @param index 系统参数编号
     * @return 指令
     */
    byte[] getSystemParam(byte[] macAddress, int pairCode, int index);

    /**
     * 设置系统参数指令
     * @param mac  mac地址
     * @param pairCode 配对码
     * @param paramIndex 系统参数编号
     * @param paramData 系统参数
     * @return 指令
     */
    byte[] setSystemParam(byte[] mac, int pairCode, int paramIndex, ISystemParam paramData);


    byte[] getDisplayPage(byte[] mac, int pairCode);


    byte[] getAlertReminders(byte[] mac, int pairCode);

    /**
     * 更新时间指令
     * @return 指令
     */
    byte[] updateTimeCode();

    /**
     * 获取历史计步信息指令
     * @param hour 获取的起始小时（1970年起）
     * @return 指令
     */
    byte[] getHistoryStepDataOrder(int hour);

    /**
     * 获取当前日期计步数据指令
     * @return 指令
     */
    byte[] getStepDataOrder();

    /**
     * 同步计步信息至手环
     * @param data 步数信息
     * @return 指令
     */
    byte[] syncStepHistoryDataOrder(CurrentStepData data);

    /**
     * 使能计步可以更新指令
     * @param enable 使能或者禁能
     * @return 指令
     */
    byte[] enableStepDataUpdateOrder(boolean enable);


    byte[] lookForBand(LookBandData data);
}
