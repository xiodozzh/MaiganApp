package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

import java.util.List;

/**
 *
 * @author zhaixiang
 * 检查手机号是否注册
 */

public class CheckPhoneRequestEntity implements RequestEntity{
    @SerializedName("phones")
    private List<PhoneItem> phones;

    public CheckPhoneRequestEntity(List<PhoneItem> phones) {
        this.phones = phones;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_CHECK_USER_BY_PHONE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public static class PhoneItem{
        @SerializedName("phone")
        private String phone;
        @SerializedName("zone")
        private String zone;
        @SerializedName("contractName")
        private String name;

        public PhoneItem(String phone, String zone,String name) {
            this.phone = phone;
            this.zone = zone;
            this.name = name;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "PhoneItem{" +
                    "phone='" + phone + '\'' +
                    ", zone='" + zone + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
