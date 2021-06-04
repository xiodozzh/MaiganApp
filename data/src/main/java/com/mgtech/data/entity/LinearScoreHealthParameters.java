package com.mgtech.data.entity;

import java.util.List;
import java.util.Random;

public class LinearScoreHealthParameters implements IScoreHealthParameters {
    private static final int MAX_SCORE = 10;
    private static final int MIN_SCORE = 1;
    private static final float[] HR_SECTION = {57, 94};

    private static final float[] PS_SECTION = {84, 140};

    private static final float[] PD_SECTION = {56, 90};

    private static final float[] PM_SECTION = {63, 100};

    private static final float[] SV_SECTION = {60.03f, 103.49f};


    private static final float[] TPR_SECTION = {14.1f, 20.86f};

    private static final float[] CO_SECTION = {3.87f, 7.21f};


    @Override
    public int score(int index, List<Float> values) {
        int size = values.size();
        if (size == 0) {
            return 0;
        }
        float value = 0;
        for (float v : values) {
            value += v;
        }
        value = value / size;
        int score;
        switch (index) {
            case INDEX_HR:
                score = scoreByRange(value, HR_SECTION[1], HR_SECTION[0], 150, 30);
                break;
            case INDEX_PD:
                score = scoreByRange(value, PD_SECTION[1], PD_SECTION[0], 150, 40);
                break;
            case INDEX_PS:
                score = scoreByRange(value, PS_SECTION[1], PS_SECTION[0], 200, 70);
                break;
            case INDEX_PM:
                score = scoreByRange(value, PM_SECTION[1], PM_SECTION[0], 150, 20);
                break;
            case INDEX_SV:
                score = scoreByRange(value, SV_SECTION[1], SV_SECTION[0], SV_SECTION[1] * 10, 0);
                break;
            case INDEX_CO:
                score = scoreByRange(value, CO_SECTION[1], CO_SECTION[0], CO_SECTION[1] * 10, 0);
                break;
            case INDEX_TPR:
                score = scoreByRange(value, TPR_SECTION[1], TPR_SECTION[0], TPR_SECTION[1] * 10, 0);
                break;
            default:
                score = MAX_SCORE;
        }
        return score;
    }

    /**
     * 打分算法
     *
     * @param value    数据平均值
     * @param goodHigh 正常范围上限
     * @param goodLow  正常范围下限
     * @param max      数值可能的最大值
     * @param min      数值可能的最小值
     * @return 数据
     */
    private int scoreByRange(float value, float goodHigh, float goodLow, float max, float min) {
        if (value > max || value < min) {
            return MIN_SCORE;
        }
        if (value >= goodLow && value <= goodHigh) {
            return MAX_SCORE;
        }
        if (value <= max && value > goodHigh) {
            return (int) (MIN_SCORE + (MAX_SCORE - MIN_SCORE) * (value - goodHigh) / (max - goodHigh));
        }
        if (value >= min && value < goodLow) {
            return (int) (MIN_SCORE + (MAX_SCORE - MIN_SCORE) * (goodLow - value) / (goodLow - min));
        }
        return MAX_SCORE;
    }

}
