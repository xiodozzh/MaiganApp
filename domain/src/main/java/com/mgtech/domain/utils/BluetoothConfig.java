package com.mgtech.domain.utils;

public class BluetoothConfig {
    // service and parameters in it
    public static final String MG_PROFILE_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static final String CHAR_VERIFICATION_ORDER = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String CHAR_VERIFICATION_RESPONSE = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String CHAR_ORDER = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static final String CHAR_RESPONSE = "0000fff4-0000-1000-8000-00805f9b34fb";

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
    public static final byte CODE_SYNC_BLOOD_PRESSURE = 0x22;

    public static final byte CODE_GET_AUTO_SAMPLE_INFO = 0x30;
    public static final byte CODE_GET_AUTO_SAMPLE_RET = 0x31;
    public static final byte CODE_DEL_AUTO_SAMPLE_RET = 0x32;
    public static final byte CODE_REEXE_AUTO_SAMPLE = 0x33;
    public static final byte CODE_RESET_AUTO_SAMPLE = 0x34;

    public static final byte CODE_GET_SYS_PARA_0 = 0x40;
    public static final byte CODE_GET_SYS_PARA_1 = 0x41;
    public static final byte CODE_GET_SYS_PARA_2 = 0x42;
    public static final byte CODE_GET_SYS_PARA_3 = 0x43;
    public static final byte CODE_GET_SYS_PARA_4 = 0x44;
    public static final byte CODE_GET_SYS_PARA_5 = 0x45;

    public static final byte CODE_SET_SYS_PARA_0 = 0x50;
    public static final byte CODE_SET_SYS_PARA_1 = 0x51;
    public static final byte CODE_SET_SYS_PARA_2 = 0x52;
    public static final byte CODE_SET_SYS_PARA_3 = 0x53;
    public static final byte CODE_SET_SYS_PARA_4 = 0x54;
    public static final byte CODE_SET_SYS_PARA_5 = 0x55;

    public static final byte CODE_BATTERY_INFO = 0x60;
    public static final byte CODE_GET_FW_INFO = 0x61;
    public static final byte CODE_UPDATE_TIME = 0x62;
    public static final byte CODE_STATUS_UPDATE = 0x70;

    public static final byte CODE_SYNC_STEP_HISTORY_DATA = (byte) 0x80;
    public static final byte CODE_SYNC_STEP_DATA = (byte) 0x81;
    public static final byte CODE_CALIBRATE_STEP_DATA = (byte) 0x82;
    public static final byte CODE_SYNC_STEP_DATA_CTRL = (byte) 0x83;


    public static final byte CODE_FACTORY_RESET = (byte) 0xF0;

//    public static final byte NORMAL = 0x00;
//    public static final byte CHARGING = 0x01;
//    public static final byte CHARGED = 0x02;
    public static final long POWER_AVAILABLE_TIME = 1000 * 3600 * 24;
//    public static final long POWER_AVAILABLE_TIME = 1000 * 60;

    public static final byte SAMPLE_RATE_TYPE_0 = 0x00;
    public static final byte SAMPLE_RATE_TYPE_1 = 0x01;
    public static final byte SAMPLE_RATE_TYPE_2 = 0x02;

    public static final int DEFAULT_INTV_MIN = 6;
    public static final int DEFAULT_INTV_MAX = 60;
    public static final int DEFAULT_INTV_LATENCY = 0;
    public static final int DEFAULT_OVER_TIME = 100;
    public static final int DEFAULT_INTV_CE_LENGTH_MIN = 1;
    public static final int DEFAULT_INTV_CE_LENGTH_MAX = 120;

//    public static final int SLOW_INTV_MIN = 240;
    public static final int SLOW_INTV_MIN = 304;
    public static final int SLOW_INTV_MAX = 320;
    public static final int SLOW_INTV_LATENCY = 4;
    public static final int SLOW_OVER_TIME = 600;

    public static final int BROADCAST_NOT_ALLOW_BOND = 0x00;
    public static final int BROADCAST_ALLOW_BOND = 0x01;

    public static final int ERROR_CODE_CHARGE_WHILE_MEASURE = 0xB;

    public static String getSampleErrorMessage(byte errorCode) {
        String info;
        switch (errorCode) {
            case 0x0:
                info = "??????????????????";
                break;
            case 0x1:
                info = "????????????MTU";
                break;
            case 0x2:
                info = "???????????????????????????";
                break;
            case 0x3:
                info = "????????????????????????";
                break;
            case 0x4:
                info = "?????????????????????PW";
                break;
            case 0x5:
                info = "?????????????????????ECG";
                break;
            case 0x6:
                info = "??????????????????????????????";
                break;
            case 0x7:
//                info = "???????????????????????????????????????";
                info = "??????????????????";
                break;
            case 0x8:
                info = "???????????????????????????PW??????";
                break;
            case 0x9:
                info = "????????????ECG????????????";
                break;
            case 0xA:
                info = "ECG???????????????????????????ECG??????";
                break;
            case 0xB:
//                info = "???????????????????????????????????????????????????";
                info = "???????????????";
                break;
            default:
                info = "";
                break;
        }
        return info;
    }





    /**
     * ???????????????????????????
     *
     * @param errorCode ?????????
     * @return ????????????
     */
    public static String getFirmwareEndErrorMessage(byte errorCode) {
        String info;
        switch (errorCode) {
            case 0x0:
                info = "??????";
                break;
            case 0x1:
                info = "???????????????????????????????????????";
                break;
            case 0x2:
                info = "?????????????????????CRC????????????";
                break;
            case 0x3:
                info = "?????????????????????flash?????????????????????????????????";
                break;
            case 0x4:
                info = "?????????????????????????????????";
                break;
            default:
                info = "";
                break;
        }
        return info;
    }
}
