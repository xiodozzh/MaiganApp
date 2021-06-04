package com.mgtech.blelib.core;

import android.bluetooth.BluetoothDevice;

/**
 * Created by zhaixiang on 2017/7/28.
 * BleClientCallback 回调
 */

public interface BleClientCallback2 {

    /**
     * 连接建立
     */
    void onConnected();

    /**
     * 发现设备已经连接，并重新发起连接
     */
    void connectExistingConnection(boolean exist);

    /**
     * 连接断开
     */
    void onDisConnected();

    /**
     * 发现服务完成
     */
    void onServiceDiscovered();

    /**
     * 写 characteristic 成功应答
     *
     * @param status status of the operation, true if success, false if failed
     * @param s      UUID of the written characteristic
     */
    void onWrite(boolean status, String s);

    /**
     * 读 characteristic 成功应答
     *
     * @param status status of the operation, true if success, false if failed
     * @param s      UUID of the characteristic
     * @param value  Value of the characteristic
     */
    void onRead(boolean status, String s, byte[] value);

    /**
     * 使能 notification
     */
    void onDataTransferEnabled(String uuid);

    /**
     * 获取 notification characteristic 数据
     *
     * @param uuid UUID
     * @param receivedData 收到的数据
     */
    void onDataReceived(String uuid, byte[] receivedData);

    /**
     * RSSI改变
     *
     * @param rssi the changed rssi
     */
    void onRRSIChanged(int rssi);

    /**
     * 发生错误
     */
    void onError();

    /**
     * MTU改变
     *
     * @param mtu    mtu
     * @param status status
     */
    void onMtuChanged(int mtu, int status);
}
