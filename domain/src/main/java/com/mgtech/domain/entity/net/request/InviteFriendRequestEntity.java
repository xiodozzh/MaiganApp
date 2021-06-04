package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * @author zhaixiang
 */
public class InviteFriendRequestEntity implements RequestEntity {
    private String phone;
    private String zone;

    public InviteFriendRequestEntity(String phone, String zone) {
        this.phone = phone;
        this.zone = zone;
    }

    public String getPhone() {
        return phone;
    }

    public String getZone() {
        return zone;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_INVITE_FRIEND;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
