package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/6/15.
 * 计算返回值分量
 */

public class SaveRawDataResponseItemEntity {
    @SerializedName(NetConstant.JSON_RESULT_NAME)
    private String name;
    @SerializedName(NetConstant.JSON_RESULT_VALUE)
    private float value;
    @SerializedName(NetConstant.JSON_RESULT_UNIT)
    private String unit;
    @SerializedName(NetConstant.JSON_RESULT_ERROR)
    private boolean resultError;
    @SerializedName(NetConstant.JSON_RESULT_DESCRIBE)
    private int resultDescribe;
    @SerializedName(NetConstant.JSON_RESULT_CHINESE_NAME)
    private String resultDisplayName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isResultError() {
        return resultError;
    }

    public void setResultError(boolean resultError) {
        this.resultError = resultError;
    }

    public int getResultDescribe() {
        return resultDescribe;
    }

    public void setResultDescribe(int resultDescribe) {
        this.resultDescribe = resultDescribe;
    }

    public String getResultDisplayName() {
        return resultDisplayName;
    }

    public void setResultDisplayName(String resultDisplayName) {
        this.resultDisplayName = resultDisplayName;
    }

    @Override
    public String toString() {
        return "SaveRawDataResponseItemEntity{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", resultError=" + resultError +
                ", resultDescribe='" + resultDescribe + '\'' +
                ", resultDisplayName='" + resultDisplayName + '\'' +
                '}';
    }
}
