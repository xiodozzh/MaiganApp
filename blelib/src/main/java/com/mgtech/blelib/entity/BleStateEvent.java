package com.mgtech.blelib.entity;

import android.bluetooth.BluetoothAdapter;

public class BleStateEvent {
    public static final int STATE_ON = BluetoothAdapter.STATE_ON;
    public static final int STATE_OFF = BluetoothAdapter.STATE_OFF;
    public static final int STATE_TURNING_ON = BluetoothAdapter.STATE_TURNING_ON;
    public static final int STATE_TURNING_OFF = BluetoothAdapter.STATE_TURNING_OFF;
    private int state;

    public BleStateEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
