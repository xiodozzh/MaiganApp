package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaixiang on 2018/1/30.
 * 检查手环是否正版
 */

public class CheckBraceletResponseEntity {
    @SerializedName("legal")
    private int isLegal;

    public int getIsLegal() {
        return isLegal;
    }

    public void setIsLegal(int isLegal) {
        this.isLegal = isLegal;
    }

    @Override
    public String toString() {
        return "CheckBraceletResponseEntity{" +
                "isLegal=" + isLegal +
                '}';
    }
}
