package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 *
 * @author zhaixiang
 * 处理关注请求
 */

public class DealRelationRequestEntity implements RequestEntity{


    /**
     * accountGuid :
     * requestGuid :
     * result : 1
     */

    private String requestGuid;
    private int result;

    public DealRelationRequestEntity(String requestGuid, int result) {
        this.requestGuid = requestGuid;
        this.result = result;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SET_RELATION;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public String getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(String requestGuid) {
        this.requestGuid = requestGuid;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DealRelationRequestEntity{" +
                "requestGuid='" + requestGuid + '\'' +
                ", result=" + result +
                '}';
    }
}
