package com.mgtech.maiganapp.utils;

import android.content.Context;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.R;

import java.text.DecimalFormat;

/**
 * Created by zhaixiang on 2017/2/8.
 * 生理指数工具
 */

public class IndicatorUtils {
//    private static final float[] NORMAL_RANGE_HR = {60, 100};
//
//    private static final float[] NORMAL_RANGE_PS = {97, 140};
//
//    private static final float[] NORMAL_RANGE_PD = {78, 90};
//
//    private static final float[] NORMAL_RANGE_PM = {84, 91};
//
//    private static final float[] NORMAL_RANGE_SV = {60, 70};
//
//    private static final float[] TM_SECTION = {29, 31};
//
//    private static final float[] NORMAL_RANGE_TPR = {1255, 1468};
//
//    private static final float[] NORMAL_RANGE_CO = {4, 6};
//
////    private static final float[] ALT_SECTION = {19};
//
//    private static final float[] SI_SECTION = {60, 90};

    public static final int[] RANGE_HR = {30, 150};
    public static final int[] RANGE_PS = {40, 190};
    public static final int[] RANGE_PD = {40, 190};
    public static final int[] RANGE_V0 = {0, 9};
    public static final int[] RANGE_TPR = {0, 48};
    public static final int[] RANGE_PM = {30, 150};
    public static final int[] RANGE_CO = {0, 18};

    public static final float[] NORMAL_RANGE_HR = {55, 100};

    public static final float[] NORMAL_RANGE_PS = {90, 140};

    public static final float[] NORMAL_RANGE_PD = {56, 90};

    public static final float[] NORMAL_RANGE_PM ={70, 105};

    public static final float[] NORMAL_RANGE_V0 = {3.01f, 4.29f};

    public static final float[] NORMAL_RANGE_TPR = {11.5f, 20.9f};

    public static final float[] NORMAL_RANGE_CO = {3, 8f};

    private static final float[] NORMAL_RANGE_SV = {60.03f, 103.49f};

    private static final float[] NORMAL_RANGE_BV = {3.83f, 6.36f};


    /**
     * 获取名字
     *
     * @param context Context
     * @param type    类型
     * @return String
     */
    public static String getTitle(Context context,int type) {
        switch (type) {
            case NetConstant.HR:
                return context.getString(R.string.HR);
            case NetConstant.AC:
                return context.getString(R.string.AC);
            case NetConstant.PD:
                return context.getString(R.string.PD);
            case NetConstant.PS:
                return context.getString(R.string.PS);
            case NetConstant.PM:
                return context.getString(R.string.PM);
            case NetConstant.TM:
                return context.getString(R.string.TM);
            case NetConstant.TPR:
                return context.getString(R.string.TPR);
            case NetConstant.CO:
                return context.getString(R.string.CO);
            case NetConstant.V0:
                return context.getString(R.string.V0);
            case NetConstant.SI:
                return context.getString(R.string.SI);
            case NetConstant.SV:
                return context.getString(R.string.SV);
            case NetConstant.ALK:
                return context.getString(R.string.ALK);
            case NetConstant.ALT:
                return context.getString(R.string.ALT);
            case NetConstant.K:
                return context.getString(R.string.K);
            case NetConstant.BV:
                return context.getString(R.string.BV);
            default:
        }
        return "";
    }


    public static String getTitle(Context context, String type) {
        switch (type) {
            case "RANK_HR":
                return context.getString(R.string.HR);
            case "RANK_AC":
                return context.getString(R.string.AC);
            case "RANK_PD":
                return context.getString(R.string.PD);
            case "RANK_PS":
                return context.getString(R.string.PS);
            case "RANK_PM":
                return context.getString(R.string.PM);
            case "RANK_TM":
                return context.getString(R.string.TM);
            case "RANK_TPR":
                return context.getString(R.string.TPR);
            case "RANK_CO":
                return context.getString(R.string.CO);
            case "RANK_V0":
                return context.getString(R.string.V0);
            case "RANK_SI":
                return context.getString(R.string.SI);
            case "RANK_SV":
                return context.getString(R.string.SV);
            case "RANK_ALK":
                return context.getString(R.string.ALK);
            case "RANK_ALT":
                return context.getString(R.string.ALT);
            case "RANK_K":
                return context.getString(R.string.K);
            case "RANK_BV":
                return context.getString(R.string.BV);
                default:
        }
        return "";
    }

    /**
     * 获取单位
     *
     * @param context Context
     * @param type    类型
     * @return String
     */
    public static String getUnit(Context context,@MyConstant.IndicatorType int type) {
        switch (type) {
            case MyConstant.RANK_HR:
                return context.getString(R.string.HR_unit);
            case MyConstant.RANK_AC:
                return context.getString(R.string.AC_unit);
            case MyConstant.RANK_PD:
                return context.getString(R.string.PD_unit);
            case MyConstant.RANK_PS:
                return context.getString(R.string.PS_unit);
            case MyConstant.RANK_PM:
                return context.getString(R.string.PM_unit);
            case MyConstant.RANK_TM:
                return context.getString(R.string.TM_unit);
            case MyConstant.RANK_TPR:
                return context.getString(R.string.TPR_unit);
            case MyConstant.RANK_CO:
                return context.getString(R.string.CO_unit);
            case MyConstant.RANK_V0:
                return context.getString(R.string.V0_unit);
            case MyConstant.RANK_SI:
                return context.getString(R.string.SI_unit);
            case MyConstant.RANK_SV:
                return context.getString(R.string.SV_unit);
            case MyConstant.RANK_ALK:
                return context.getString(R.string.ALK_unit);
            case MyConstant.RANK_ALT:
                return context.getString(R.string.ALT_unit);
            case MyConstant.RANK_K:
                return context.getString(R.string.K_unit);
            case MyConstant.RANK_BV:
                return context.getString(R.string.BV_unit);
            default:
        }
        return "";
    }

    /**
     * 获取名字简写
     *
     * @param context Context
     * @param type    类型
     * @return String
     */
    public static String getShortName(Context context,@MyConstant.IndicatorType int type) {
        switch (type) {
            case MyConstant.RANK_HR:
                return context.getString(R.string.HR_short);
            case MyConstant.RANK_AC:
                return context.getString(R.string.AC_short);
            case MyConstant.RANK_PD:
                return context.getString(R.string.PD_short);
            case MyConstant.RANK_PS:
                return context.getString(R.string.PS_short);
            case MyConstant.RANK_PM:
                return context.getString(R.string.PM_short);
            case MyConstant.RANK_TM:
                return context.getString(R.string.TM_short);
            case MyConstant.RANK_TPR:
                return context.getString(R.string.TPR_short);
            case MyConstant.RANK_CO:
                return context.getString(R.string.CO_short);
            case MyConstant.RANK_V0:
                return context.getString(R.string.V0_short);
            case MyConstant.RANK_SI:
                return context.getString(R.string.SI_short);
            case MyConstant.RANK_SV:
                return context.getString(R.string.SV_short);
            case MyConstant.RANK_ALK:
                return context.getString(R.string.ALK_short);
            case MyConstant.RANK_ALT:
                return context.getString(R.string.ALT_short);
            case MyConstant.RANK_K:
                return context.getString(R.string.K_short);
            case MyConstant.RANK_BV:
                return context.getString(R.string.BV_short);
            default:
        }
        return "";
    }

    /**
     * 获取参数等级
     *
     * @param type  参数类型
     * @param value 参数值
     * @return 0正常，-1偏低，1偏高
     */
    public static int getLevel(int type, float value) {
        int level;
        float[] section = null;
        switch (type) {
            case NetConstant.HR:
                section = NORMAL_RANGE_HR;
                break;
            case NetConstant.AC:
                section = null;
                break;
            case NetConstant.PD:
                section = NORMAL_RANGE_PD;
                break;
            case NetConstant.PS:
                section = NORMAL_RANGE_PS;
                break;
            case NetConstant.PM:
                section = NORMAL_RANGE_PM;
                break;
            case NetConstant.TM:
                section = null;
                break;
            case NetConstant.TPR:
                section = NORMAL_RANGE_TPR;
                break;
            case NetConstant.CO:
                section = NORMAL_RANGE_CO;
                break;
            case NetConstant.V0:
                section = null;
                break;
            case NetConstant.SI:
                section = null;
                break;
            case NetConstant.SV:
                section = NORMAL_RANGE_SV;
                break;
            case NetConstant.ALK:
                section = null;
                break;
            case NetConstant.ALT:
                section = null;
                break;
            case NetConstant.K:
                section = null;
                break;
            case NetConstant.BV:
                section = NORMAL_RANGE_BV;
                break;
            default:
        }
        if (section != null && section.length == 2) {
            if (value < section[0]) {
                level = -1;
            } else if (value > section[1]) {
                level = 1;
            } else {
                level = 0;
            }
        } else {
            level = 0;
        }
        return level;
    }

    /**
     * 获取等级说明文字
     *
     * @param level 参数等级
     * @return String 等级说明
     */
    public static String getLevelString(int level) {
        if (level < 0) {
            return "偏低";
        } else if (level > 0) {
            return "偏高";
        } else {
            return "正常";
        }
    }

    public static String getDescriptionString(Context context,@MyConstant.IndicatorType int type) {
        switch (type) {
            case MyConstant.RANK_HR:
                return context.getString(R.string.HR_desc);
            case MyConstant.RANK_AC:
                return context.getString(R.string.AC_desc);
            case MyConstant.RANK_PD:
                return context.getString(R.string.PD_desc);
            case MyConstant.RANK_PS:
                return context.getString(R.string.PS_desc);
            case MyConstant.RANK_PM:
                return context.getString(R.string.PM_desc);
            case MyConstant.RANK_TM:
                return "";
            case MyConstant.RANK_TPR:
                return context.getString(R.string.TPR_desc);
            case MyConstant.RANK_CO:
                return context.getString(R.string.CO_desc);
            case MyConstant.RANK_V0:
                return context.getString(R.string.V0_desc);
            case MyConstant.RANK_SI:
                return context.getString(R.string.SI_desc);
            case MyConstant.RANK_SV:
                return context.getString(R.string.SV_desc);
            case MyConstant.RANK_ALK:
                return "";
            case MyConstant.RANK_ALT:
                return "";
            case MyConstant.RANK_K:
                return context.getString(R.string.K_desc);
            case MyConstant.RANK_BV:
                return context.getString(R.string.BV_desc);
            default:
        }
        return "";
    }

    private static DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
    private static DecimalFormat decimalFormat2 = new DecimalFormat("0.00");

    /**
     * 将参数数值转换为字符，同时四舍五入至3位“有效”数字
     *
     * @param value 参数数字
     * @return 四舍五入后的字符串
     */
    public static String indicatorValueToString(float value) {
        int digitNumber = 0;
        if (value > 100) {
            digitNumber = 3;
        } else if (value > 10) {
            digitNumber = 2;
        } else if (value > 1) {
            digitNumber = 1;
        }
        if (digitNumber >= 3) {
            return String.valueOf(Math.round(value));
        } else if (digitNumber == 2) {
            return decimalFormat1.format(Math.round(value * 10) / (float) 10);
        } else if (digitNumber == 1) {
            return decimalFormat2.format(Math.round(value * 100) / (float) 100);
        } else {
            float v = Math.round(value * 1000) / (float) 1000;
            if (v < 0.01) {
                return "-";
            } else {
                return decimalFormat2.format(v);
            }
        }
    }

}
