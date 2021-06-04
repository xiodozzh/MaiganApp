package com.mgtech.maiganapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.service.BluetoothService;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.viewmodel.BaseBleViewModel;

import java.util.Arrays;

/**
 * @author zhaixiang
 */
public abstract class BleActivity<T extends BaseBleViewModel> extends BaseActivity<T> {
    private Dialog openBleFailDialog;
    private AlertDialog goToOpenLocationPermissionDialog;
    private AlertDialog localDialog;

    @Override
    protected void init(Bundle savedInstanceState) {
        viewModel.openBle.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                checkBle();
            }
        });
        if (this instanceof BraceletPairActivity){
            askForLocationPermission();
        }else{
            askForLocationPermissionIfPaired();
        }
    }

    private void askForLocationPermission(){
        if (PermissionUtils.isLocationPermissionOpen(this)) {
            if (isLocationOpen()){
                if (!isBleOn()){
                    openBle();
                }
            }else{
                openLocation();
            }
        } else {
            PermissionUtils.requestLocationPermission(this, REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * 获取蓝牙权限
     */
    public void askForLocationPermissionIfPaired() {
        if (viewModel.isPaired()) {
            askForLocationPermission();
        }
    }

    /**
     * 如果未打开位置权限，则请求权限；如果打开，则检查位置是否开启并相应
     */
    private void askForLocationPermissionAndResponse(){
        if (PermissionUtils.isLocationPermissionOpen(this)) {
            openLocationAndResponse();
        } else {
            PermissionUtils.requestLocationPermission(this, REQUEST_LOCATION_PERMISSION);
        }
    }



    public void openLocation() {
        if (Build.VERSION.SDK_INT >= 23 && !isLocationOpen()) {
            if (localDialog != null && localDialog.isShowing()) {
                return;
            }
            localDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.caution)
                    .setMessage(R.string.ble_need_location_information)
                    .setNegativeButton(R.string.do_not_open, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(BleActivity.this, R.string.ble_cannot_work_normally,
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton(R.string.go_to_open, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent enableLocate = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(enableLocate, REQUEST_ENABLE_LOCATION);
                        }
                    }).create();
            localDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel.isPaired() && !BluetoothService.isServiceOn(this)){
            BluetoothService.startBluetoothService(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (doesAutoSetRegisterBleCallback()) {
            viewModel.registerBleCallback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (doesAutoSetRegisterBleCallback()) {
            viewModel.unRegisterBleCallback();
        }
    }

    protected boolean doesAutoSetRegisterBleCallback(){
        return true;
    }

    /**
     * 检查蓝牙，如果关闭则打开
     */
    private void checkBle() {
        if (!isBleOn()) {
            openBle();
        }
    }

    /**
     * 开启蓝牙
     */
    public void openBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.phone_do_not_support_bluetooth, Toast.LENGTH_SHORT).show();
        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLE_REQUEST);
        }
    }

    public boolean isBleOn(){
        return Utils.isBluetoothOpen();
    }

    /**
     * 显示打开蓝牙失败对话框
     */
    protected void showOpenBleFailDialog(){
        if (openBleFailDialog != null && openBleFailDialog.isShowing()){
            return;
        }
        openBleFailDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.activity_ble_please_open_ble)
                .setPositiveButton(R.string.activity_ble_open_ble, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openBle();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userNotOpenBle();
                    }
                }).create();
        openBleFailDialog.show();
    }

    /**
     * 用户确定不打开蓝牙
     */
    private void userNotOpenBle(){
        finish();
    }

    private void showGoToOpenLocationPermissionDialog(){
        if (goToOpenLocationPermissionDialog != null && goToOpenLocationPermissionDialog.isShowing()){
            return;
        }
        goToOpenLocationPermissionDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.ble_bluetooth_need_location_permission))
                .setNegativeButton(R.string.do_not_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(BleActivity.this, R.string.ble_cannot_work_normally,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.go_to_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(SettingSystemPermissionActivity.getCallingIntent(BleActivity.this),REQUEST_PERMISSION_ACTIVITY);
                    }
                }).create();
        goToOpenLocationPermissionDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        // 开启蓝牙
        if (requestCode == ENABLE_BLE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                openBleSuccess();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                openBleFail();
            }
        }
        // 获取位置权限
        if (requestCode == REQUEST_PERMISSION_ACTIVITY) {
            askForLocationPermissionAndResponse();
        }
        // 开启位置
        if (requestCode == REQUEST_ENABLE_LOCATION){
            if (resultCode == RESULT_OK){
                openLocationAndResponse();
            }else{
                Toast.makeText(BleActivity.this, R.string.ble_cannot_work_normally,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 如果位置功能未开，则打开位置。如果位置已打开，则检查蓝牙。
     */
    private void openLocationAndResponse(){
        if (isLocationOpen()){
            openBleAndResponse();
        }else{
            openLocation();
        }
    }

    /**
     * 如果蓝牙未开，则打开蓝牙。如果蓝牙已打开，则触发回调方法
     */
    private void openBleAndResponse(){
        if (isBleOn()){
            openBleSuccess();
        }else{
            openBle();
        }
    }

    /**
     * 打开蓝牙失败
     */
    protected abstract void openBleFail();

    /**
     * 打开蓝牙成功
     */
    protected abstract void openBleSuccess();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: " + Arrays.toString(grantResults));
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length >= 1 ){
            if (grantResults[0]== PackageManager.PERMISSION_DENIED) {
                showGoToOpenLocationPermissionDialog();
            }else if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                openLocationAndResponse();
            }
        }
    }
}
