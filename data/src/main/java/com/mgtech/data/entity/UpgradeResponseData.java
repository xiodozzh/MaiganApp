package com.mgtech.data.entity;

import com.mgtech.domain.utils.BluetoothConfig;

/**
 * Created by zhaixiang on 2016/12/30.
 * 固件升级信息包应答
 */

public class UpgradeResponseData {
    private byte errorCode;

    public UpgradeResponseData(byte[] data){
        if (data.length < 2 || data[0] != BluetoothConfig.CODE_FOTA_DATA){
            throw new RuntimeException("this is not upgrade body data");
        }
        errorCode = data[1];
    }

    public byte getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return getFirmwareBodyErrorMessage(errorCode);
    }

    /**
     * 固件升级内容包错误码
     *
     * @param errorCode 错误码
     * @return 错误信息
     */
    private String getFirmwareBodyErrorMessage(byte errorCode) {
        String info;
        switch (errorCode) {
            case 0x0:
                info = "正确";
                break;
            case 0x1:
                info = "流程错误，还未发送固件信息";
                break;
            case 0x2:
                info = "数据包序号错误，超出存储范围";
                break;
            case 0x3:
                info = "flash损坏，永久无法固件升级";
                break;
            case 0x4:
                info = "其它硬件错误，无法继续升级";
                break;
            default:
                info = "";
                break;
        }
        return info;
    }
}
