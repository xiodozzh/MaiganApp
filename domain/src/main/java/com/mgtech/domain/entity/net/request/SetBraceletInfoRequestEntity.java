package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * @author zhaixiang
 */
public class SetBraceletInfoRequestEntity implements RequestEntity {


    /**
     * macAddress : de:vd:ev:00:00:yy
     * firmwareVersion : 8.8.8
     */

    private String macAddress;
    private String firmwareVersion;
    private String braceletName;

    public SetBraceletInfoRequestEntity(String macAddress, String firmwareVersion, String braceletName) {
        this.macAddress = macAddress;
        this.firmwareVersion = firmwareVersion;
        this.braceletName = braceletName;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SET_BRACELET_INFORMATION;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "SetBraceletInfoRequestEntity{" +
                "macAddress='" + macAddress + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", braceletName='" + braceletName + '\'' +
                '}';
    }
}
