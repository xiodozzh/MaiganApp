package com.mgtech.maiganapp.utils;

import android.content.Context;

import com.mgtech.maiganapp.R;


/**
 * Created by zhaixiang on 2017/1/11.
 * 判断生理数据范围
 */

public class IndicatorDescriptionUtil {
    private static final int BPH_SECTION_1 = 120;
    private static final int BPL_SECTION_1 = 80;
    private static final int BPH_SECTION_2 = 140;
    private static final int BPL_SECTION_2 = 90;

    private static final int HR_SECTION_1 = 40;
    private static final int HR_SECTION_2 = 60;
    private static final int HR_SECTION_3 = 100;
    private static final int HR_SECTION_4 = 160;

    /**
     * 获取血压等级
     *
     * @param bph 收缩压
     * @param bpl 舒张压
     * @return 0:正常，1:正常高压，2:高血压
     */
    public static int judgeBloodPressure(float bph, float bpl) {
        int value1 = judgeSystolicBloodPressure(bph);
        int value2 = judgeDiastolicBloodPressure(bpl);
        return Math.max(value1, value2);
    }

    /**
     * 获取收缩压血压等级
     *
     * @param bph 收缩压
     * @return 0:正常，1:正常高压，2:高血压
     */
    public static int judgeSystolicBloodPressure(float bph) {
        int value1;
        if (bph < BPH_SECTION_1) {
            value1 = 0;
        } else if (bph < BPH_SECTION_2) {
            value1 = 1;
        } else {
            value1 = 2;
        }
        return value1;
    }

    /**
     * 获取收缩压血压等级
     *
     * @param bpl 舒张压
     * @return 0:正常，1:正常高压，2:高血压
     */
    public static int judgeDiastolicBloodPressure(float bpl) {
        int value2;
        if (bpl < BPL_SECTION_1) {
            value2 = 0;
        } else if (bpl < BPL_SECTION_2) {
            value2 = 1;
        } else {
            value2 = 2;
        }
        return value2;
    }

    /**
     * 判断心率等级
     *
     * @param hr 心率
     * @return 0：正常，-1、1：异常1，-2、2：异常2
     */
    public static int judgeHeartRate(float hr) {
        if (hr < HR_SECTION_1) {
            return -2;
        } else if (hr < HR_SECTION_2) {
            return -1;
        } else if (hr < HR_SECTION_3) {
            return 0;
        } else if (hr < HR_SECTION_4) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 获取文字判断
     *
     * @param value 等级
     * @return 用于显示的文字
     */
    public static String getDescription(Context context,int value) {
        if (value < 0) {
            return context.getString(R.string.lower);
        } else if (value > 0) {
            return context.getString(R.string.higher);
        } else {
            return context.getString(R.string.normal);
        }
    }

}
