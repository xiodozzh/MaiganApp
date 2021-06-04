package com.mgtech.domain.utils;

import androidx.annotation.IntDef;

/**
 *
 * @author zhaixiang
 * 常量
 */
public class MyConstant {
    /**
     * true 生产环境， false 测试环境
     */
    public static final boolean IS_PRODUCE_NET = false;
    public static final boolean FORCE_OPEN_ECG = true;
//    public static final String APP_KEY = "releaseKey";
    public static final String APP_KEY = "health";
    public static final String WEBSITE = "www.mystrace.com";
    public static final String APP_DOWNLOAD_SITE = "http://a.app.qq.com/o/simple.jsp?pkgname=com.mgtech.maiganapp";
    public static final String USER_PROTOCOL_SITE = "https://api.maigantech.com/html/UserProtocol.htm";
    public static final String PRIVACY_PROTOCOL_SITE = "https://api.maigantech.com/html/PrivacyPolicy.htm";
    public static final String SERVICE_PROTOCOL_SITE = "https://api.maigantech.com/html/UserAuthProtocol.htm";
    public static final String EMAIL = "feedback@maigantech.com";

    /**
     * 手环类型，健康版 0，医疗版 1
     */
    public static final int BRACELET_TYPE = 0;
    public static final String API_VERSION = "3.1.1";

    public static final String TAG = "mgApp";
    public static final String DEVICE_SUB_FIRMWARE_RELEASE = "release";
    public static final String DEVICE_SUB_FIRMWARE_DEBUG = "debug";
    public static final int[] DISPLAY_DATA_TYPE = {NetConstant.PS, NetConstant.PD, NetConstant.HR,
            NetConstant.CO,NetConstant.TPR,NetConstant.V0,NetConstant.PM};

    public static final String WX_LOGIN_APP_ID = "wx02b209b8031e9292";
    public static final String WX_LOGIN_SECRET = "47dd9e70892e99e183edbba22a45d353";


    public static final String QQ_LOGIN_APP_ID = "1106226172";
    /**
     * 短信间隔时间
     */
    public static final long MSM_TIME_INTERVAL = 60500;

    /**
     * 默认采样率
     */
    public static final int DEFAULT_SAMPLE_RATE = 128;
    /**
     * ecg默认采样率
     */
    public static final int DEFAULT_ECG_SAMPLE_RATE = 512;
    /**
     * ecg测量时间
     */
    public static final int ECG_SAVING_TIME = 30;

    /**
     * 返回
     * 0 都返回
     * 1 自动
     * 2 手动
     */
    public static final int GET_PW_DATA_TYPE = 0;

    public static final float[] POWER_LEVELS = {87.5f, 62.5f,37.5f,12.5f};

    public static final int SEX_UNKNOW = 0;
    public static final int MAN = 1;
    public static final int WOMEN = 2;

    public static final int HEIGHT_MAX = 230;
    public static final int HEIGHT_MIN = 50;
    public static final int HEIGHT_DEFAULT = 160;

    public static final int WEIGHT_MAX = 200;
    public static final int WEIGHT_MIN = 20;
    public static final int WEIGHT_DEFAULT = 50;

    public static final String MAN_STRING = "男";

    public static final String MEDICINE_NAME = "MedicinalName";
    public static final String MEDICINE_DOSAGE = "Dosage";
    public static final String MEDICINE_ENTITY = "MedicinalEntity";

    public static final String DATE_FORMAT_BIRTHDAY = "yyyy-MM-dd";
    public static final String DISPLAY_DATE_2 = "M/d";
    public static final String FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT2 = "yyyy.MM.dd";
    public static final String DISPLAY_DATE_4 = "yyyy,MMM";
    public static final String DISPLAY_DATE_5 = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_WITH_NO_SPACE = "yyyy-MM-ddHH:mm:ss";
    public static final String DISPLAY_DATE_6 = "MM-dd";
    public static final String DISPLAY_DATE_7 = "MMM";
    public static final String DISPLAY_TIME = "HH:mm";
    public static final String MEDICINE_TIME = "HH:mm";

    public static final String NAME_HR = "HR";
    public static final String NAME_PD = "Pd";
    public static final String NAME_PS = "Ps";
    public static final String NAME_PM = "PM";
    public static final String NAME_SV = "SV";
    public static final String NAME_SI = "SI";
    public static final String NAME_CO = "CO";
    public static final String NAME_TPR = "TPR";
    public static final String NAME_AC = "AC";
    public static final String NAME_ALK = "ALK";
    public static final String NAME_ALT = "ALT";
    public static final String NAME_TM = "TM";
    public static final String NAME_BV = "BV";
    public static final String NAME_V0 = "V0";
    public static final String NAME_K = "K";

    // 显示
    public static final int RANK_HR = 0;
    public static final int RANK_PS = 1;
    public static final int RANK_PD = 2;
    public static final int RANK_PM = 3;
    public static final int RANK_CO = 4;
    public static final int RANK_TPR = 5;
    public static final int RANK_V0 = 6;

    // 不显示
    public static final int RANK_SV = 7;
    public static final int RANK_SI = 8;
    public static final int RANK_AC = 9;
    public static final int RANK_ALK = 10;
    public static final int RANK_ALT = 11;
    public static final int RANK_TM = 12;
    public static final int RANK_BV = 13;
    public static final int RANK_K = 14;

    public static final int NOTIFICATION_MEDICINE_REMINDER_ID = 200;

    public static final int ACTION_INDEX_MEASURE = 100;
    public static final int ACTION_INDEX_ADJUST = 200;
    public static final int ACTION_INDEX_PAIR = 300;
    public static final int ACTION_INDEX_UPGRADE = 400;
    public static final int ACTION_INDEX_LINK = 500;
    public static final int ACTION_INDEX_SET_PARA = 600;
    public static final int ACTION_INDEX_MEASURE_ECG = 700;
    public static final int ACTION_INDEX_STEP = 800;
    public static final int ACTION_GET_REMINDER = 900;
    public static final int ACTION_ADD_REMINDER = 1000;

    public static final byte[] PREFE_KEY = {'M', 'Y', 'S', 'T', 'R', 'A', 'C', 'E', 'G', 'R', 'E', 'A', 'T', 0x01, 0x02, 0x03};
    public static final byte[] PREFE_VECTOR = {0x03, 0x02, 0x01, 'm', 'y', 's', 't', 'r', 'a', 'c', 'e', 'g', 'r', 'e', 'a', 't'};

    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;

    @IntDef({RANK_PS, RANK_PD, RANK_PM, RANK_CO, RANK_K, RANK_TM, RANK_TPR, RANK_BV, RANK_HR, RANK_SI, RANK_SV, RANK_V0, RANK_AC, RANK_ALK, RANK_ALT})
    public @interface IndicatorType{}

    @IntDef({DAY,WEEK,MONTH})
    public @interface TimeSpan{}

    public static final String SETTING_ACTIVITY = "android.settings.APPLICATION_DETAILS_SETTINGS";
    public static final String PACKAGE = "package";
}
