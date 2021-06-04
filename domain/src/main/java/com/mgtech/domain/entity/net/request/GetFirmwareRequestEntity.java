package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/3/28.
 * 获取固件版本
 */

public class GetFirmwareRequestEntity implements RequestEntity{
    @SerializedName(NetConstant.JSON_VERSION)
    private String version;
    @SerializedName(NetConstant.JSON_DEVICE)
    private String device;

    public GetFirmwareRequestEntity(String version,String device) {
        this.version = version;
        this.device = device;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "GetFirmwareRequestEntity{" +
                "version='" + version + '\'' +
                ", device='" + device + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_FIRMWARE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
