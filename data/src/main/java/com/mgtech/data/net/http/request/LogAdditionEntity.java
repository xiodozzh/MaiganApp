package com.mgtech.data.net.http.request;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;

/**
 * Created by zhaixiang on 2017/5/16.
 * Addition
 */

public class LogAdditionEntity {
    @SerializedName(NetConstant.JSON_PHONE)
    private String phone;
    @SerializedName(NetConstant.JSON_USER_ID)
    private String userId;

    public LogAdditionEntity(Context context) {
        userId = SaveUtils.getUserId(context);
        phone = SaveUtils.getPhone(context);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LogAdditionEntity{" +
                "phone='" + phone + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
