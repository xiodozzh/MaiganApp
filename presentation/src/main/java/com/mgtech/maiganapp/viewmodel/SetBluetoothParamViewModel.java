package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;

import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.maiganapp.R;

/**
 * Created by zhaixiang on 2017/3/25.
 * 修改蓝牙参数
 */

public class SetBluetoothParamViewModel extends BaseBleViewModel {
    private Application context;
    public final ObservableBoolean setSuccess = new ObservableBoolean(false);

    public SetBluetoothParamViewModel(Application context) {
        super(context);
        this.context = context;
    }

    /**
     * 设置是否开启自动采样
     *
     * @param enable true 开启，false 关闭
     */
    public void setEnable(boolean enable) {
        this.bleRequest.enableBluetoothAutoMeasure(enable);
    }


    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType){
//            case BleStatusConstant.STATUS_PARAM_SET_0:
//                setSuccess.set(!setSuccess.get());
//                break;
            case BleStatusConstant.STATUS_HAVE_AUTO_SAMPLE_DATA:
                showDialog(context.getString(R.string.receiving_data),context.getString(R.string.please_wait));
                break;
            case BleStatusConstant.STATUS_HAVE_NO_AUTO_SAMPLE_DATA:
                cancelDialog();
                break;
            case BleStatusConstant.STATUS_DISCONNECTED:
                cancelDialog();
                break;
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {

    }

    @Override
    protected void onStateChange(int bleState) {

    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new DefaultResponseCallback();
    }
}
