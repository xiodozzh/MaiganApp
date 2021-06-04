package com.mgtech.data.entity;

import com.mgtech.domain.utils.BluetoothConfig;

/**
 * Created by zhaixiang on 2016/12/30.
 * 固件升级信息包应答
 */

public class UpgradeInfoResponseData {
    private byte errorCode;

    public UpgradeInfoResponseData(byte[] data){
        if (data.length < 2 || data[0] != BluetoothConfig.CODE_FOTA_INFO){
            throw new RuntimeException("this is not upgrade info data");
        }
        errorCode = data[1];
    }

    public byte getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return getFirmwareErrorMessage(errorCode);
    }

    /**
     * 固件升级错误码
     *
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static String getFirmwareErrorMessage(byte errorCode) {
        String info;
        switch (errorCode) {
            case 0x0:
                info = "正确";
                break;
            case 0x1:
                info = "新固件文件类型错误";
                break;
            case 0x2:
                info = "每包固件数据长度错误";
                break;
            case 0x3:
                info = "新固件文件大小超出Flash范围";
                break;
            case 0x4:
                info = "腕带剩余电量低于20%";
                break;
            case 0x5:
                info = "腕带Flash损坏，无法固件升级";
                break;
            case 0x6:
                info = "腕带有未发送的自动采样数据，禁止固件升级";
                break;
            case 0x7:
                info = "硬件错误";
                break;
            default:
                info = "";
                break;
        }
        return info;
    }
}
