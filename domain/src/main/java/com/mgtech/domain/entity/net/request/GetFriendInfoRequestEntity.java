package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

import okhttp3.RequestBody;

public class GetFriendInfoRequestEntity implements RequestEntity {
    private String accountId;
    private String targetId;
    private int friendType;

    public GetFriendInfoRequestEntity(String accountId, String targetId, int friendType) {
        this.accountId = accountId;
        this.targetId = targetId;
        this.friendType = friendType;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getTargetId() {
        return targetId;
    }

    public int getFriendType() {
        return friendType;
    }

    @Override
    public String toString() {
        return "GetFriendInfoRequestEntity{" +
                "accountId='" + accountId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", friendType=" + friendType +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_FRIEND_INFO;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
