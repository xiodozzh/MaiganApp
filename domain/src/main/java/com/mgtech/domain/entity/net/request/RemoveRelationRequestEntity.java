package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 取消关注
 */

public class RemoveRelationRequestEntity implements RequestEntity{
    private String relationGuid;
    private String userId;

    public RemoveRelationRequestEntity(String relationGuid, String userId) {
        this.relationGuid = relationGuid;
        this.userId = userId;
    }

    public String getRelationGuid() {
        return relationGuid;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_REMOVE_RELATION;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
