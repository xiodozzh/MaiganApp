package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

public class GetFriendDataRequestEntity implements RequestEntity {
    private String friendId;

    public GetFriendDataRequestEntity(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    @Override
    public String getUrl() {
        return new Gson().toJson(this);
    }

    @Override
    public String getBody() {
        return HttpApi.API_GET_FRIEND_DATA;
    }
}
