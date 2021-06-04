package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author zhaixiang
 * 检查手机是否当前登录
 */

public class CheckPhoneInfoResponseEntity {
    @SerializedName("isLoginOtherDevice")
    private int check;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "CheckPhoneInfoResponseEntity{" +
                "check=" + check +
                '}';
    }
}
