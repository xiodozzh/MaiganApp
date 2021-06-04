package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 */

public class WarningData {
    private int code;

    public WarningData(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "WarningData{" +
                "code=" + code +
                '}';
    }
}
