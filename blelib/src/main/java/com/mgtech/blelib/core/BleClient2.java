package com.mgtech.blelib.core;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhaixiang on 2017/7/28.
 * 蓝牙使用核心类
 */

public class BleClient2 {
    private static final String TAG = "BleClient2";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BleClientCallback2 callback;
    private boolean mScanning;
    private BleDeviceService mBleDeviceService;

    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    private BluetoothManager bluetoothManager;

    private static volatile BleClient2 bleClient;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, "Connect state change: old:" + status + " new:" + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED;
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:");
                if (callback != null) {
                    callback.onConnected();
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                if (callback != null) {
                    callback.onDisConnected();
                }
                close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i(TAG, "onServicesDiscovered: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mBleDeviceService = new BleDeviceService(gatt.getServices());
                if (mBleDeviceService.getBluetoothGattServices().isEmpty()) {
                    if (callback != null) {
                        Log.i(TAG, "找不到service");
                        callback.onDisConnected();
                    }
                } else {
                    if (callback != null) {
                        callback.onServiceDiscovered();
                    }
                }

            } else {
                Log.i(TAG, "onServicesDiscovered received: " + status);
                error();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "read characteristic success");
                if (callback != null) {
                    callback.onRead(BleCoreConstant.BLE_SUCCESS, characteristic.getUuid().toString(), characteristic.getValue());
                }
            } else {
                Log.i(TAG, "read characteristic fail " + status);
                if (callback != null) {
                    callback.onRead(BleCoreConstant.BLE_FAILED, characteristic.getUuid().toString(), null);
                }
            }

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "characteristic write success ");
                if (callback != null) {
                    callback.onWrite(BleCoreConstant.BLE_SUCCESS, characteristic.getUuid().toString());
                }

            } else {
                Log.i(TAG, "characteristic write fail " + status);
                if (callback != null) {
                    callback.onWrite(BleCoreConstant.BLE_FAILED, characteristic.getUuid().toString());
                }

            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, "onCharacteristicChanged: " + Arrays.toString(characteristic.getValue()) + " " +
                    characteristic.getUuid().toString());
            if (callback != null) {
                callback.onDataReceived(characteristic.getUuid().toString(), characteristic.getValue());
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "Notification/indication  enabled successfully" + descriptor.getCharacteristic().getUuid().toString());
                if (callback != null) {
                    callback.onDataTransferEnabled(descriptor.getCharacteristic().getUuid().toString());
                }
            } else {
                Log.i(TAG, "Notification/indication enabled failed");
                disconnect();
//                error();
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (callback != null) {
                callback.onRRSIChanged(rssi);
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            if (callback != null) {
                callback.onMtuChanged(mtu, status);
            }
        }
    };

    public static BleClient2 getInstance(Context context) {
        if (bleClient == null) {
            synchronized (BleClient2.class) {
                if (bleClient == null) {
                    bleClient = new BleClient2(context);
                }
            }
        }
        return bleClient;
    }

    private static boolean isLocationPermissionOpen(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private BleClient2(Context context) {
        Log.i(TAG, "BleClient2: <init>");
        if (isLocationPermissionOpen(context)) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                mBluetoothAdapter = bluetoothManager.getAdapter();
                Log.i(TAG, "BleClient2: adapter " + mBluetoothAdapter);
            } else {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
            }
        }else{
            Log.e(TAG, "位置权限未开启.");
        }
        mConnectionState = STATE_DISCONNECTED;
    }

    public void setCallback(BleClientCallback2 callback) {
        this.callback = callback;
    }

    public BleClientCallback2 getCallback() {
        return callback;
    }

    /**
     * 开始扫描，扫描结果在 BluetoothAdapter.LeScanCallback 中返回
     */
    public synchronized void startScan(Context context, String[] deviceNames, ScanCallback mScanCallback) {
        try {
            reSet(context);
            if (deviceNames == null || deviceNames.length == 0) {
                Log.i(TAG, "deviceNames is empty ");
                return;
            }
            if (mBluetoothAdapter == null) {
                error();
                Log.i(TAG, "error with null adapter ");
                return;
            } else if (mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_NONE) {
                error();
                return;
            }
            Log.i(TAG, "start scan success");
            this.mScanning = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
                List<ScanFilter> filters = new ArrayList<>();
//            for (String name : deviceNames) {
//                ScanFilter filter = new ScanFilter.Builder()
//                        .setDeviceName(name)
//                        .setServiceUuid(new ParcelUuid(UUID.fromString(BleCoreConstant.MG_PROFILE_SERVICE)))
//                        .build();
//                filters.add(filter);
//            }
                ScanFilter filter = new ScanFilter.Builder()
                        .setServiceUuid(new ParcelUuid(UUID.fromString(BleCoreConstant.MG_PROFILE_SERVICE)))
                        .build();
                filters.add(filter);
                scanner.startScan(filters, new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build(), mScanCallback);
            } else {
                Log.e(TAG, "startScan: 扫描版本低");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检验设备是否连接
     *
     * @param address 设备名
     */
    public synchronized void startScan(Context context, String[] names, String address, ScanCallback mScanCallback) {
        try {
            if (TextUtils.isEmpty(address)) {
                return;
            }
            reSet(context);
            if (mBluetoothAdapter == null) {
                return;
            }
            final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            if (bluetoothManager.getConnectionState(device, BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED) {
                // 发现手环已经连上
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.close();
                }
                startScan(context, names, mScanCallback);
            } else {
                startScan(context, names, mScanCallback);
                if (callback != null) {
                    callback.connectExistingConnection(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopScan(Context context, ScanCallback mScanCallback) {
        this.mScanning = false;
        try {
            reSet(context);
            if (mBluetoothAdapter == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
                if (scanner != null) {
                    scanner.stopScan(mScanCallback);
                } else {
                    close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isScanning() {
        return mScanning;
    }

    public int getConnectionState() {
        return mConnectionState;
    }

//    private ScanCallback mScanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            if (callback != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                BluetoothDevice device = result.getDevice();
//                int rssi = result.getRssi();
//                if (result.getScanRecord() != null) {
//                    byte[] data = result.getScanRecord().getBytes();
//                    callback.onScanResults(device, rssi, data);
//                }
//            }
//        }
//    };


    public synchronized boolean connect(Context context, String address) {
        reSet(context);
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            error();
            return false;
        }
        Log.i(TAG, "stopScan ");
        // Previously connected device.  Try to reconnect.

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.i(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        Log.i(TAG, "Trying to create a new connection." + mBluetoothGatt);
        mConnectionState = STATE_CONNECTING;
        return true;
    }


    /**
     * Call GATT function: discoverService()
     */
    public synchronized void discoverServices() {
        if (mConnectionState != STATE_CONNECTED || mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.i(TAG, "wrong operation, please connect first 1");
            error();
            return;
        }
        Log.i(TAG, "discovering service");
        mBluetoothGatt.discoverServices();
    }

    public synchronized void getRssi() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.i(TAG, "BluetoothAdapter not initialized when get rssi");
            return;
        }
        mBluetoothGatt.readRemoteRssi();
    }

    public synchronized void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Log.i(TAG, "do disconnect: ");
        mConnectionState = STATE_DISCONNECTED;
        mBluetoothGatt.disconnect();
    }


    private synchronized void close() {
        if (mBluetoothGatt == null) {
            Log.e(TAG, "close: mBluetoothGatt == null");
            return;
        }
        Log.i(TAG, "close gatt");
        mConnectionState = STATE_DISCONNECTED;
        mBluetoothGatt.close();
        mBluetoothAdapter = null;
        bluetoothManager = null;
    }

    private void reSet(Context context) {
        if (isLocationPermissionOpen(context)) {
            if (mBluetoothAdapter == null || bluetoothManager == null) {
                bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                if (bluetoothManager != null) {
                    mBluetoothAdapter = bluetoothManager.getAdapter();
                } else {
                    Log.e(TAG, "Unable to initialize BluetoothManager.");
                    error();
                }
            } else {
                Log.i(TAG, "reSet: 2");
            }
        }else{
            Log.i(TAG, "reSet: 位置权限未打卡");
        }
    }

    /**
     * 读特征值
     *
     * @param serviceUUID        服务 UUID
     * @param characteristicUUID 特征值 UUID
     */
    public synchronized void readCharacteristic(String serviceUUID, String characteristicUUID) {
        if (mConnectionState != STATE_CONNECTED || mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            error();
            return;
        }
        boolean isError = false;
        if (mBleDeviceService != null) {
            BluetoothGattCharacteristic characteristic = mBleDeviceService.getCharacteristic(UUID.fromString(serviceUUID),
                    UUID.fromString(characteristicUUID));
            if (characteristic != null) {
//                int permission = characteristic.getProperties();
//                if ((permission & BluetoothGattCharacteristic.PROPERTY_READ)
//                        == BleCoreConstant.AT_BLE_PERMISSION_CHAR_READ) {
//                    Log.i(TAG, "reading characteristic");
//                    mBluetoothGatt.readCharacteristic(characteristic);
//                } else {
//                    Log.i(TAG, "read permission denied");
//                    isError = true;
//                }
                mBluetoothGatt.readCharacteristic(characteristic);
            } else {
                Log.i(TAG, "characteristic is null");
                isError = true;
            }
        } else {
            Log.i(TAG, "mBleDeviceService is null");
            isError = true;
        }
        if (isError) {
            error();
        }
    }

    private void write(String serviceUUID, String characteristicUUID, byte[] data, boolean needResponse) {
        if (mConnectionState != STATE_CONNECTED || mBluetoothAdapter == null || mBluetoothGatt == null) {
            error();
            return;
        }
        if (mBleDeviceService != null) {
            if (mBleDeviceService.getBluetoothGattServices().isEmpty()) {
                mBleDeviceService = new BleDeviceService(mBluetoothGatt.getServices());
            }
            BluetoothGattCharacteristic characteristic = mBleDeviceService.getCharacteristic(UUID.fromString(serviceUUID),
                    UUID.fromString(characteristicUUID));

            if (characteristic != null) {
                if (!needResponse) {
                    characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                }
                characteristic.setValue(data);
                mBluetoothGatt.writeCharacteristic(characteristic);
            } else {
                Log.i(TAG, "null characteristic");
                error();
            }
        }
    }

    /**
     * Call GATT function: writeCharacteristic with response(BluetoothGattCharacteristic)
     *
     * @param serviceUUID        UUID of the service
     * @param characteristicUUID UUID of the target characteristic
     */
    public void writeWithResponse(String serviceUUID, String characteristicUUID, byte[] data) {
        write(serviceUUID, characteristicUUID, data, true);
    }

    /**
     * Call GATT function: writeCharacteristic without response(BluetoothGattCharacteristic)
     *
     * @param serviceUUID        UUID of the service
     * @param characteristicUUID UUID of the target characteristic
     */
    public void writeWithoutResponse(String serviceUUID, String characteristicUUID, byte[] data) {
        write(serviceUUID, characteristicUUID, data, false);
    }

    /**
     * 使能通知
     *
     * @param serviceUUID        服务UUID
     * @param characteristicUUID 特征值UUID
     * @param notificationFlag   是否开启
     */
    public void toggleNotification(String serviceUUID, String characteristicUUID, boolean notificationFlag) {
        if (mConnectionState != STATE_CONNECTED || mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            error();
            return;
        }
        if (mBleDeviceService != null) {
            BluetoothGattCharacteristic characteristic = mBleDeviceService.getCharacteristic(UUID.fromString(serviceUUID),
                    UUID.fromString(characteristicUUID));
            if (characteristic != null) {
                int permission = (byte) characteristic.getProperties();
                if ((permission & BluetoothGattCharacteristic.PROPERTY_NOTIFY) == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    UUID CCC = UUID.fromString(BleCoreConstant.CONFIG_DESCRIPTOR);
                    mBluetoothGatt.setCharacteristicNotification(characteristic, notificationFlag); //Enabled locally
                    BluetoothGattDescriptor config = characteristic.getDescriptor(CCC);
                    config.setValue(notificationFlag ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
                            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(config); //Enabled remotely
                } else {
                    Log.i(TAG, "characteristic has no notification property");
                    error();
                }
            } else {
                Log.i(TAG, "null characteristic");
                error();
            }
        }
    }

    public void toggleIndication(String serviceUUID, String characteristicUUID, boolean notificationFlag) {
        if (mConnectionState != STATE_CONNECTED || mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            error();
            return;
        }
        if (mBleDeviceService != null) {
            BluetoothGattCharacteristic characteristic = mBleDeviceService.getCharacteristic(UUID.fromString(serviceUUID),
                    UUID.fromString(characteristicUUID));
            if (characteristic != null) {
                int permission = (byte) characteristic.getProperties();
                if ((permission & BluetoothGattCharacteristic.PROPERTY_INDICATE) == BluetoothGattCharacteristic.PROPERTY_INDICATE) {
                    UUID CCC = UUID.fromString(BleCoreConstant.CONFIG_DESCRIPTOR);
                    mBluetoothGatt.setCharacteristicNotification(characteristic, notificationFlag); //Enabled locally
                    BluetoothGattDescriptor config = characteristic.getDescriptor(CCC);
                    config.setValue(notificationFlag ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE :
                            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(config); //Enabled remotely
                } else {
                    Log.i(TAG, "characteristic has no indication property");
                    error();
                }
            } else {
                Log.i(TAG, "null characteristic");
                error();
            }
        }
    }

    /**
     * 设置mtu
     *
     * @param mtu mtu的大小
     * @return 如果设置成功返回true，否则返回false
     */
    public boolean requestMtu(int mtu) {
        Log.i(TAG, "requestMtu: " + mtu);
        return mBluetoothGatt != null && mBluetoothGatt.requestMtu(mtu);
    }

    private void error() {
        if (callback != null) {
            callback.onError();
        }
    }
}
