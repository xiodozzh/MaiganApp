package com.mgtech.domain.entity;

/**
 * Created by zhaixiang on 2017/4/11.
 * 参数列表
 */

public class IndicatorDetailItem {
    private int type;
    private String title;
    private float value;
    private String unit;
    private int level;
    private String valueString;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    @Override
    public String toString() {
        return "IndicatorDetailItem{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", level=" + level +
                '}';
    }
}
