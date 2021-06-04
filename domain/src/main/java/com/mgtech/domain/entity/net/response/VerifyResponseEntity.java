package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/1/6.
 * 注册
 */

public class VerifyResponseEntity extends ResponseEntity{
    @Expose
    @SerializedName(NetConstant.JSON_PHONE)
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "RegisterResponseEntity{" +
                ", phone='" + phone + '\'' +
                "," + super.toString() + "}";
    }
}
