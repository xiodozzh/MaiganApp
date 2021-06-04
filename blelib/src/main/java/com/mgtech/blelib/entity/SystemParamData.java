package com.mgtech.blelib.entity;

import java.util.ArrayList;
import java.util.List;

public class SystemParamData {
    public static final int ON = 1;
    public static final int OFF = 0;

    public static final int DATE_SIMPLE = 2;
    public static final int DATE_COMPLEX = 1;

    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;

    public static final int DEFAULT_HEIGHT = 160;
    public static final int DEFAULT_WEIGHT = 65;

    public static final int NONE = -1;

    private List<Integer> index;
    /**
     * 省电模式使能控制
     */
    public static final int PARAM_POWER_SAVE_MODE = 0;

    /**
     * 自动测量模式使能控制
     */
    public static final int PARAM_AUTO_MEASURE_ENABLE = 1;
    /**
     * 自动测量间隔时间
     */
    public static final int PARAM_AUTO_MEASURE_INTERVAL = 2;
    /**
     * 自动测量超时时间
     */
    public static final int PARAM_AUTO_MEASURE_TIMEOUT = 3;
    /**
     * 脉图识别:连续脉图最小数量
     */
    public static final int PARAM_MIN_PW_NUMBER_IN_SERIES = 4;
    /**
     * 识别成功条件：采集脉图最小数量
     */
    public static final int PARAM_MIN_PW_NUMBER = 5;
    /**
     * 识别成功条件：采集脉图最大数量
     */
    public static final int PARAM_MAX_PW_NUMBER = 6;
    /**
     * 识别成功条件：采集脉图最大时间
     */
    public static final int PARAM_MAX_COMPLETE_TIME = 7;
    /**
     * 采样率自适应使能控制
     */
    public static final int PARAM_SAMPLE_RATE_SELF_ADAPTATION_ENABLE= 8;
    /**
     * 采样率自适应基准心率
     */
    public static final int PARAM_SAMPLE_RATE_SELF_ADAPTATION_BASE_HEART_RATE= 9;
    /**
     * 采样率自适应基准采样率
     */
    public static final int PARAM_SAMPLE_RATE_SELF_ADAPTATION_BASE_SAMPLE_RATE= 10;
    /**
     * 测量心率需要的脉图数量
     */
    public static final int PARAM_HEART_RATE_NEED_PW_NUMBER= 11;
    /**
     * 增益自适应使能控制
     */
    public static final int PARAM_SELF_AMPLIFICATION_ENABLE= 12;
    /**
     * 测量脉图极值时需要识别的脉图数量
     */
    public static final int PARAM_PW_NUMBER_TO_MEASURE_PEAK= 13;
    /**
     * 数字电位器基准值
     */
    public static final int PARAM_BASE_OF_DIGITAL= 14;
    /**
     * 自动采样提醒
     */
    public static final int PARAM_REMINDERS= 15;
    /**
     * 语言
     */
    public static final int PARAM_LANGUAGE= 16;
    /**
     * 身高
     */
    public static final int PARAM_HEIGHT= 17;
    /**
     * 体重 斤
     */
    public static final int PARAM_WEIGHT= 18;
    /**
     * 日期显示
     */
    public static final int PARAM_DATE= 19;

    /**
     * 电量显示
     */
    public static final int PARAM_POWER= 20;

    /**
     * 计算结果显示
     */
    public static final int PARAM_CALCULATE_RESULT_DISPLAY= 21;

    /**
     * 运动显示
     */
    public static final int PARAM_SPORT= 22;
    /**
     * 分时段自动测量间隔
     */
    public static final int PARAM_INTERVAL= 23;


    /**
     * 省电模式使能控制
     */
    private boolean powerSaveMode = false;

    /**
     * 自动测量模式使能控制
     */
    private boolean autoMeasureEnable = false;

    /**
     * 自动测量间隔时间
     */
    private int autoMeasureInterval = 3600;
    /**
     * 自动测量超时时间
     */
    private int autoSampleTimeout = 240;

    /**
     * 脉图识别:连续脉图最小数量
     */
    private int samplePwSectionNumber = 2;
    /**
     * 识别成功条件：采集脉图最小数量
     */
    private int sampleMinPwNumber = 10;
    /**
     * 识别成功条件：采集脉图最大数量
     */
    private int sampleMaxPwNumber = 15;
    /**
     * 识别成功条件：采集脉图最大时间
     */
    private int sampleMaxCompleteTime = 20;

    /**
     * 采样率自适应使能控制
     */
    private boolean sampleRateSelfAdaptationEnable = false;
    /**
     * 采样率自适应基准心率
     */
    private int sampleRateSelfAdaptationBaseHeartRate = 60;

    /**
     * 采样率自适应基准采样率
     */
    private int sampleRateSelfAdaptationBaseSampleRate = 128;
    /**
     * 测量心率需要的脉图数量
     */
    private int heartRateNeedPwNumber = 10;

    /**
     * 增益自适应使能控制
     */
    private boolean selfAmplificationEnable = true;

    /**
     * 测量脉图极值时需要识别的脉图数量
     */
    private int pwNumberToMeasurePeak = 2;

    /**
     * 数字电位器基准值
     */
    private int baseOfDigital = 32;

    /**
     * 自动采样提醒
     */
    private ReminderData reminderData;

    /**
     * 语言
     */
    private int lang = CHINESE;


    private DisplayPage pageDisplayData;

    private HeightWeightData heightWeightData = new HeightWeightData();

//    private AutoMeasurePeriodData autoMeasurePeriodData;


    public SystemParamData() {
        index = new ArrayList<>();
    }

    public List<Integer> getIndex() {
        return index;
    }

    public ReminderData getReminderData(){
        return reminderData;
    }

    public void setReminderData(ReminderData reminders, boolean haveInterval){
        index.add(PARAM_REMINDERS);
        if (haveInterval) {
            index.add(PARAM_INTERVAL);
        }
        this.reminderData = reminders;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        index.add(PARAM_LANGUAGE);
        this.lang = lang;
    }

    public void setHeight(int height){
        index.add(PARAM_HEIGHT);
        heightWeightData.setHeight(height);
    }

    public void setWeight(int weight){
        index.add(PARAM_WEIGHT);
        heightWeightData.setWeight(weight);
    }


    public void setHeightAndWeight(HeightWeightData data){
        index.add(PARAM_HEIGHT);
        index.add(PARAM_WEIGHT);
        heightWeightData = data;
    }

    public void setHeightAndWeight(int height,int weight){
        index.add(PARAM_HEIGHT);
        index.add(PARAM_WEIGHT);
        heightWeightData = new HeightWeightData();
        heightWeightData.setHeight(height);
        heightWeightData.setWeight(weight);
    }

    public HeightWeightData getHeightWeightData(){
        return heightWeightData;
    }

    public DisplayPage getPageDisplayData() {
        return pageDisplayData;
    }

    public void setPageDisplayData(DisplayPage pageDisplayData) {
        index.add(PARAM_POWER);
        index.add(PARAM_CALCULATE_RESULT_DISPLAY);
        index.add(PARAM_SPORT);
        index.add(PARAM_DATE);
        this.pageDisplayData = pageDisplayData;
    }

    public void clearIndex(){
        index.clear();
    }

    @Override
    public String toString() {
        return "SystemParamData{" +
                "reminders=" + reminderData +
                ", lang=" + lang +
                ", pageDisplayData=" + pageDisplayData +
                ", heightWeightData=" + heightWeightData +
                '}';
    }
}
