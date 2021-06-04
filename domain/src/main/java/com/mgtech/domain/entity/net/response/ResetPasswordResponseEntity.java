package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/1/6.
 * 重置密码
 */

public class ResetPasswordResponseEntity extends ResponseEntity{
    @SerializedName(value = NetConstant.JSON_PASSWORD, alternate = {NetConstant.Password})
    private String password;
    @SerializedName(NetConstant.JSON_PHONE)
    private String phone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ResetPasswordResponseEntity{" +
                "password='" + password + '\'' +
                "," + super.toString() + "}" ;
    }
}
