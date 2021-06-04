package com.mgtech.maiganapp;

import android.app.Application;

import com.mgtech.blelib.biz.BleCenter;
import com.mgtech.blelib.biz.BleRequest;
import com.mgtech.blelib.biz.BleResponse;
import com.mgtech.blelib.biz.BleState;
import com.mgtech.blelib.biz.IBizController;
import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;

/**
 * @author zhaixiang
 */
public class BleApplication extends Application {
    private static final String TAG = "BleApplication";
//    private IBizController bizController;
//    private IBraceletInfoManager manager;
    private BleRequest bleRequest;
    private BleResponse bleResponse;
    private BleState bleState;


    public void initBle(){
        IBizController bizController = new BleCenter(this);
//        manager = new BraceletInfoManagerBuilder(this).create();
//        bleRequest = new BleRequest(bizController,manager);
        bleRequest = bizController.getRequest();
        bleResponse = bizController.getResponse();
        bleState = bizController.getState();
    }

    public BleRequest getRequest() {
        return bleRequest;
    }

    public BleResponse getBleResponse() {
        return bleResponse;
    }

    public BleState getBleState() {
        return bleState;
    }
}