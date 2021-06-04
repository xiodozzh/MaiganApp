package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * @date 2018/3/7
 */

public class CheckPhoneInfoRequestEntity implements RequestEntity {
    private String phoneId;
    private String id;

    public CheckPhoneInfoRequestEntity( String phoneId,String id) {
        this.phoneId = phoneId;
        this.id = id;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_CHECK_PHONE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
