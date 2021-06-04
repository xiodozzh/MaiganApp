package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取验证码
 */

public class GetVerificationCodeEntity implements RequestEntity {
    public static final int REGISTER = 0;
    public static final int FORGET_PASSWORD = 1;
    public static final int FIND_PASSWORD = 2;


    private String phone;
    private String zone;
    private int type;

    public GetVerificationCodeEntity(String phone,String zone,int type) {
        this.phone = phone;
        this.zone = zone;
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_VERIFICATION_CODE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
