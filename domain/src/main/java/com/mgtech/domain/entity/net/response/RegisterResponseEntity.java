package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author zhaixiang
 * 注册
 */

public class RegisterResponseEntity extends ResponseEntity {
    @SerializedName("accountGuid")
    private String id;
    @SerializedName("phone")
    private String phone;
    @SerializedName("zone")
    private String zone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    @Override
    public String toString() {
        return "RegisterResponseEntity{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
