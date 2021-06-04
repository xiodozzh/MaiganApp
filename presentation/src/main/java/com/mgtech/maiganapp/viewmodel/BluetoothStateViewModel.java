package com.mgtech.maiganapp.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

/**
 *
 * @author zhaixiang
 * @date 2017/8/9
 * 蓝牙状态
 */

public class BluetoothStateViewModel {
    public final ObservableBoolean showScanLayout = new ObservableBoolean(true);
    public final ObservableBoolean showMask = new ObservableBoolean(true);
    public final ObservableBoolean showBondLayout = new ObservableBoolean(false);
    public final ObservableBoolean showOpenBleLayout = new ObservableBoolean(false);
    public final ObservableBoolean showConnectLayout = new ObservableBoolean(false);

    public void unbind(){
        showScanLayout.set(false);
        showMask.set(true);
        showBondLayout.set(true);
        showOpenBleLayout.set(false);
        showConnectLayout.set(false);
    }

    public void bluetoothClose(){
        showScanLayout.set(false);
        showMask.set(true);
        showBondLayout.set(false);
        showOpenBleLayout.set(true);
        showConnectLayout.set(false);
    }

    public void scanning(){
        showScanLayout.set(true);
        showMask.set(true);
        showBondLayout.set(false);
        showOpenBleLayout.set(false);
        showConnectLayout.set(false);
    }

    public void connecting(){
        showScanLayout.set(false);
        showMask.set(true);
        showBondLayout.set(false);
        showOpenBleLayout.set(false);
        showConnectLayout.set(true);
    }

    public void prepare(){
        showScanLayout.set(false);
        showMask.set(false);
        showBondLayout.set(false);
        showOpenBleLayout.set(false);
        showConnectLayout.set(false);
    }
}
