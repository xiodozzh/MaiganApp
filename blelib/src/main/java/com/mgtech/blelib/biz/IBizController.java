package com.mgtech.blelib.biz;

import android.content.Context;

import com.mgtech.blelib.entity.BluetoothOrder;

public interface IBizController {

    boolean linkIfAvailable();

    void pairInit();

    void link();

    void pair();

    void disconnect();

    void stopScan();

    void disconnectIfNotWorking();

    void addOrder(BluetoothOrder order);

    BleResponse getResponse();

    BleRequest getRequest();

    BleState getState();

}
