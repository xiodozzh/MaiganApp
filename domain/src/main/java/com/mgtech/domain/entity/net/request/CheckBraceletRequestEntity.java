package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 校验 Mac 地址和手环的 GUID
 */

public class CheckBraceletRequestEntity implements RequestEntity {
    @SerializedName(NetConstant.JSON_GUID)
    private String deviceId;
    @SerializedName(NetConstant.JSON_MAC)
    private String mac;

    public CheckBraceletRequestEntity(String deviceId, String mac) {
        this.deviceId = deviceId;
        this.mac = mac;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_CHECK_BRACELET_COPYRIGHT;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
