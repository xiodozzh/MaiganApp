package com.mgtech.blelib.biz;

/**
 * @author zhaixiang
 */
public class BluetoothConfig {
    public static final byte[] ENCRYPT_FIXED_CODE = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

    public static final int SAMPLE_RATE_CONSTANT = 32768;
    public static final byte CODE_AUTH_LINK = 0x01;
    public static final byte CODE_DISCONNECT_LINK = 0x02;
    public static final byte CODE_REQUEST_PAIR = 0x03;
    public static final byte CODE_SAVE_PAIR_INFO = 0x04;
    public static final byte CODE_SET_CONNECT_PARA = 0x05;
    public static final byte CODE_UNBIND = 0x06;

    public static final byte CODE_FOTA_INFO = 0x10;
    public static final byte CODE_FOTA_DATA = 0x11;
    public static final byte CODE_FOTA_END = 0x12;

    public static final byte CODE_START_MANUAL_SAMPLE = 0x20;
    public static final byte CODE_STOP_MANUAL_SAMPLE = 0x21;

    public static final byte CODE_GET_AUTO_SAMPLE_INFO = 0x22;
    public static final byte CODE_GET_STORED_SAMPLE_RET = 0x23;
    public static final byte CODE_DEL_STORED_SAMPLE_RET = 0x25;
    public static final byte CODE_REEXE_AUTO_SAMPLE = 0x26;
    public static final byte CODE_RESET_AUTO_SAMPLE = 0x27;

    public static final byte CODE_SYNC_BLOOD_PRESSURE = 0x28;

    public static final byte CODE_GET_SYS_PARA = 0x30;
    public static final byte CODE_SET_SYS_PARA = 0x31;

    public static final byte CODE_BATTERY_INFO = 0x32;
    public static final byte CODE_GET_FW_INFO = 0x33;
    public static final byte CODE_UPDATE_TIME = 0x34;
    public static final byte CODE_STATUS_UPDATE = (byte) 0xE0;

    public static final byte CODE_SYNC_STEP_HISTORY_DATA = (byte) 0x40;
    public static final byte CODE_SYNC_STEP_DATA = (byte) 0x41;
    public static final byte CODE_CALIBRATE_STEP_DATA = (byte) 0x42;
    public static final byte CODE_SYNC_STEP_DATA_CTRL = (byte) 0x43;
    public static final byte CODE_LOOK_FOR_BAND = (byte) 0x50;


    public static final byte CODE_FACTORY_RESET = (byte) 0xF0;

    public static final int BROADCAST_ALLOW_BOND = 0x01;

    public static final byte PARAM_HEIGHT_WEIGHT = 10;
    public static final byte PARAM_DISPLAY_PAGE = 20;
    public static final byte PARAM_ALERT_REMINDERS = 30;
//    public static final byte PARAM_AUTO_MEASURE_INTERVAL = 40;

    public static final byte ERROR_NONE = 0x00;
    public static final byte ERROR_PARAMS = 0x01;
    public static final byte ERROR_FAILED = 0x02;
    public static final byte ERROR_HARDWARE = 0x03;
    public static final byte ERROR_LOW_BATTERY = 0x04;
    public static final byte ERROR_CHARGING = 0x05;
    public static final byte ERROR_BUSY = 0x06;
    public static final byte ERROR_CRC_CHECK = 0x07;
    public static final byte ERROR_MOTION = 0x08;
    public static final byte ERROR_WEAR = 0x09;
    public static final byte ERROR_ECG_LEAD_OFF = 0x0A;
    public static final byte ERROR_NO_DATA = 0x0B;
    public static final byte ERROR_MTU_SIZE = 0x0C;
    public static final byte ERROR_END_OF_STREAM = 0x0D;
    public static final byte ERROR_UNSPECIFIED = 0x0E;
    public static final byte ERROR_SIGNAL_AMP_LOW = 0x0F;
    public static final byte ERROR_SIGNAL_AMP_HIGH= 0x10;
    public static final byte ERROR_SIGNAL_WEAR_TIGHT= 0x11;
    public static final byte ERROR_SIGNAL_WEAR_LOOSE= 0x12;
    public static final byte ERROR_SIGNAL_WRONG= 0x13;
    public static final byte ERROR_SIGNAL_HR_LOW= 0x14;
    public static final byte ERROR_SIGNAL_HR_HIGH= 0x15;

    /**
     * 固件升级结束错误码
     *
     * @param errorCode 错误码
     * @return 错误信息
     */
    public static String getErrorMessage(byte errorCode) {
        String info;
        switch (errorCode) {
            case ERROR_NONE:
                info = "正确";
                break;
            case ERROR_PARAMS:
                info = "参数错误";
                break;
            case ERROR_FAILED:
                info = "执行失败";
                break;
            case ERROR_HARDWARE:
                info = "硬件错误";
                break;
            case ERROR_LOW_BATTERY:
                info = "电量过低";
                break;
            case ERROR_CHARGING:
                info = "正在充电";
                break;
            case ERROR_BUSY:
                info = "被占用/流程错误（有未发送数据、未更新时间）";
                break;
            case ERROR_CRC_CHECK:
                info = "CRC校验错误";
                break;
            case ERROR_MOTION:
                info = "手腕状态错误";
                break;
            case ERROR_WEAR:
                info = "未佩戴手环";
                break;
            case ERROR_ECG_LEAD_OFF:
                info = "心电导联脱落";
                break;
            case ERROR_NO_DATA:
                info = "无采样数据";
                break;
            case ERROR_MTU_SIZE:
                info = "MTU大小错误";
                break;
            case ERROR_END_OF_STREAM:
                info = "数据结尾";
                break;
            case ERROR_UNSPECIFIED:
                info = "未知错误";
                break;
            default:
                info = "未知错误";
                break;
        }
        return info;
    }
}
