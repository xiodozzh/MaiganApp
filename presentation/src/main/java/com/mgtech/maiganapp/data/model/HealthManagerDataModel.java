package com.mgtech.maiganapp.data.model;

import java.util.List;

public class HealthManagerDataModel {
    public float psGoal;
    public float pdGoal;
    public String suggest;
    public int normalDays;
    public int abnormalDays;
    public List<IndicatorDataModel> list;

    public float getMinPs(){
        return getMinValue(list,true);
    }

    public float getMinPd(){
        return getMinValue(list,false);
    }

    public float getAvgPs(){
        return getAvgValue(list,true);
    }

    public float getAvgPd(){
        return getAvgValue(list,false);
    }

    public float getMaxPs(){
        return getMaxValue(list,true);
    }

    public float getMaxPd(){
        return getMaxValue(list,false);
    }


    private float getMinValue(List<IndicatorDataModel> list,boolean isPs ){
        if (list == null || list.isEmpty()){
            return Float.NaN;
        }
        float min = Float.MAX_VALUE;
        for (IndicatorDataModel model :list) {
            min = Math.min(min,isPs ?model.ps : model.pd);
        }
        return min;
    }

    private float getMaxValue(List<IndicatorDataModel> list,boolean isPs){
        if (list == null || list.isEmpty()){
            return Float.NaN;
        }
        float max = Float.MIN_VALUE;
        for (IndicatorDataModel model :list) {
            max = Math.max(max,isPs ?model.ps : model.pd);
        }
        return max;
    }

    private float getAvgValue(List<IndicatorDataModel> list,boolean isPs){
        if (list == null || list.isEmpty()){
            return Float.NaN;
        }
        float sum = 0;
        for (IndicatorDataModel model :list) {
            sum += isPs ?model.ps : model.pd;
        }
        return sum/list.size();
    }

    @Override
    public String toString() {
        return "HealthManagerDataModel{" +
                "psGoal=" + psGoal +
                ", pdGoal=" + pdGoal +
                ", suggest='" + suggest + '\'' +
                ", normalDays=" + normalDays +
                ", abnormalDays=" + abnormalDays +
                ", list=" + list +
                '}';
    }
}
