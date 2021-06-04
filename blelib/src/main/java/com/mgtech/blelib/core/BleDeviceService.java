package com.mgtech.blelib.core;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhaixiang on 2017/7/28.
 * Service discovery result containing list of services and characteristics withing the services.
 */

public class BleDeviceService {
    private final List<BluetoothGattService> bluetoothGattServices;

    public BleDeviceService(List<BluetoothGattService> bluetoothGattServices) {
        this.bluetoothGattServices = bluetoothGattServices;

        if (!bluetoothGattServices.isEmpty()) {
            for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
                for (BluetoothGattCharacteristic characteristic : bluetoothGattService.getCharacteristics()) {
                    Log.i("BluetoothGattService", characteristic.getUuid().toString());
                }
            }
        } else {
            Log.e("BluetoothGattService", "null");
        }
    }

    /**
     * 获取全部 BluetoothGattService
     */
    public List<BluetoothGattService> getBluetoothGattServices() {
        return bluetoothGattServices;
    }

    /**
     * 获取特定 BluetoothGattService
     *
     * @param serviceUuid UUID service的标识
     * @return BluetoothGattService 查找不到可能返回null
     */
    public BluetoothGattService getService(@NonNull final UUID serviceUuid) {
        for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
            if (bluetoothGattService.getUuid().equals(serviceUuid)) {
                return bluetoothGattService;
            }
        }
        return null;
    }

    /**
     * 获取特定特征值
     *
     * @param characteristicUuid 特征值UUID
     * @return BluetoothGattCharacteristic 特征值，查找不到可能返回null
     */
    public BluetoothGattCharacteristic getCharacteristic(@NonNull UUID serviceUuid, @NonNull UUID characteristicUuid) {
        BluetoothGattService bluetoothGattService = getService(serviceUuid);
        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(characteristicUuid);
            if (characteristic != null) {
                return characteristic;
            }
        }
        return null;
    }
}
