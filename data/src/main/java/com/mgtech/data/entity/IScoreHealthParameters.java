package com.mgtech.data.entity;

import java.util.List;

/**
 * @author zhaixiang
 */
public interface IScoreHealthParameters {
    int INDEX_HR = 0;
    int INDEX_PD = 1;
    int INDEX_PS = 2;
    int INDEX_PM = 3;
    int INDEX_SV = 4;
    int INDEX_SI = 5;
    int INDEX_CO = 6;
    int INDEX_TPR = 7;
    int INDEX_AC = 8;
    int INDEX_ALK = 9;
    int INDEX_ALT = 10;
    int INDEX_TM = 11;
    int INDEX_BV = 12;
    int INDEX_V0 = 13;
    int INDEX_K = 14;

    /**
     * 获取数据打分
     * @param index 参数类型
     * @param values 一组需要打分的数据值
     * @return 打分
     */
    int score(int index,List<Float> values);
}
