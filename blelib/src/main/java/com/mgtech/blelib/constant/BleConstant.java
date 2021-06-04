package com.mgtech.blelib.constant;


/**
 * @author zhaixiang
 * 常量
 */
public class BleConstant {
    public static final String TAG = "mgApp";

    public static final byte[] PROTOCOL_VERSION = {4, 2, 1}; //选择版本
    public static final String[] DEVICE_NAMES = {"MS-BDHH01", "MS-YL1001-00", "MS-YL1001-01", "MS-YL1001-02", "MS-BDHH01-0", "MS-BDHH01-1",
            "MS-BDHH01-2", "MS-BDHH01-3", "MS-BDHH01-4", "MS-JK1001-0", "MS-JK1001-1", "MS-JK1001-2",
            "MS-JK1001-3", "MS-JK1001-4", "MS-JK1001-00", "MS-JK1001-01", "MS-JK1001-02", "MS-JK1001-03"};
    public static final long PAIR_TIME_OUT_TIME = 10000;
    public static final long CONNECT_TIME_OUT_TIME = 5000;
    public static final boolean CLOSE_AFTER_UNPAIR = false;

    public static final String DISPLAY_DATE_1 = "yyyy-MM-dd";
    public static final String DISPLAY_DATE_2 = "M/d";
    public static final String DISPLAY_DATE_3 = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_4 = "yyyy,MMM";
    public static final String DISPLAY_DATE_5 = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_TIME = "HH:mm";

    public static final String ACTIVITY_ACTION = "com.mg.app.activity";
    public static final int CODE_BROADCAST = 0;
    public static final int CODE_LINK = 1;
    public static final int CODE_MEASURE_PW = 2;
    public static final int CODE_STOP_MEASURE = 3;
    public static final int CODE_DISCONNECT = 4;
    public static final int CODE_GET_LINK_STATUS = 5;
    public static final int CODE_STOP_SCAN = 6;
    public static final int CODE_ENABLE_AUTO_MEASURE = 7;
    public static final int CODE_UPGRADE = 9;
    public static final int CODE_CANCEL_UPGRADE = 10;
    public static final int CODE_RESET_AUTO_SAMPLE_TIME_AND_SYCN_BP = 11;
    public static final int CODE_UN_PAIR = 12;
    public static final int CODE_GET_ALERT_REMINDERS = 13;
    public static final int CODE_SET_ALERT_REMINDER = 14;
    public static final int CODE_GET_DISPLAY_PAGE = 15;
    public static final int CODE_SET_DISPLAY_PAGE = 16;
    public static final int CODE_SYNC_STEP_HISTORY_DATA = 17;
    public static final int CODE_SYNC_STEP_DATA = 18;
    public static final int CODE_CALIBRATE_STEP_DATA = 19;
    public static final int CODE_SET_BLOOD_PRESSURE = 21;
    public static final int CODE_MEASURE_ECG = 22;
    public static final int CODE_ADJUST = 23;
    public static final int CODE_VERIFY = 24;
    public static final int CODE_PAIR = 25;
    public static final int CODE_PAIR_MAC = 26;
    public static final int CODE_MEASURE_PW_AUTO = 27;
    public static final int CODE_GET_STORED_MANUAL_DATA = 28;
    public static final int CODE_GET_FIRMWARE_INFO = 29;
    public static final int CODE_GET_POWER = 30;
    public static final int CODE_GET_PARAM_0 = 31;
    public static final int CODE_RESET_AUTO_SAMPLE_TIME = 32;
    public static final int CODE_GET_AUTO_MEASURE_INFO = 33;
    public static final int CODE_GET_HISTORY_STEP = 34;
    public static final int CODE_SYNC_TIME = 35;
    public static final int CODE_MEASURE_PW_WITHOUT_RECOGNIZE = 36;
    public static final int CODE_REQUEST_PAIR = 37;
    public static final int CODE_SET_HEIGHT_WEIGHT = 38;
    public static final int CODE_LOOK_FOR_BAND = 39;
    public static final int CODE_SET_AUTO_MEASURE_INTERVAL = 40;

    public static final byte[] PREFE_KEY = {'M', 'Y', 'S', 'T', 'R', 'A', 'C', 'E', 'G', 'R', 'E', 'A', 'T', 0x01, 0x02, 0x03};
    public static final byte[] PREFE_VECTOR = {0x03, 0x02, 0x01, 'm', 'y', 's', 't', 'r', 'a', 'c', 'e', 'g', 'r', 'e', 'a', 't'};

    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;

    public static final int INDEX_HR = 0;
    public static final int INDEX_PS = 1;
    public static final int INDEX_PD = 2;
    public static final int INDEX_PM = 3;
    public static final int INDEX_SV = 4;
    public static final int INDEX_SI = 5;
    public static final int INDEX_CO = 6;
    public static final int INDEX_TPR = 7;
    public static final int INDEX_AC = 8;
    public static final int INDEX_ALK = 9;
    public static final int INDEX_ALT = 10;
    public static final int INDEX_TM = 11;
    public static final int INDEX_BV = 12;
    public static final int INDEX_V0 = 13;
    public static final int INDEX_K = 14;

    public static final int PAGE_STEP = 0;
    public static final int PAGE_DISTANCE = 1;
    public static final int PAGE_HEAT = 2;

    public static final int DEFAULT_HEIGHT = 160;
    public static final int DEFAULT_WEIGHT = 100;

    public static final int MEASURE_PRE_PW_COMPLETE_NUMBER = 3;
    public static final int MEASURE_PW_COMPLETE_NUMBER_MIN = 10;
    public static final int MEASURE_PW_COMPLETE_NUMBER_MAX = 15;
}
