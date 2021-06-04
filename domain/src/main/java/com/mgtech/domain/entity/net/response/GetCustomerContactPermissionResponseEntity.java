package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

public class GetCustomerContactPermissionResponseEntity {
    public static final int ALLOW = 1;
    public static final int DENY = 0;

    /**
     * allowByWechat : 1
     * allowByPhone : 1
     */
    @SerializedName("allowByWechat")
    private int allowByWechat;
    @SerializedName("allowByPhone")
    private int allowByPhone;


    public int getAllowByWechat() {
        return allowByWechat;
    }

    public void setAllowByWechat(int allowByWechat) {
        this.allowByWechat = allowByWechat;
    }

    public int getAllowByPhone() {
        return allowByPhone;
    }

    public void setAllowByPhone(int allowByPhone) {
        this.allowByPhone = allowByPhone;
    }

    @Override
    public String toString() {
        return "SetCustomerContactPermissionRequestEntity{" +
                ", allowByWechat=" + allowByWechat +
                ", allowByPhone=" + allowByPhone +
                '}';
    }
}
