package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取好友请求
 */

public class GetRelationRequestEntity implements RequestEntity{
    private int page;
    private int pageSize;

    public GetRelationRequestEntity(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }


    public int getPageSize() {
        return pageSize;
    }


    @Override
    public String getUrl() {
        return HttpApi.API_GET_REQUEST;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
