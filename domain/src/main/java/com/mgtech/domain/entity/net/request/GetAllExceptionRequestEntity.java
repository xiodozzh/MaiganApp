package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/3/9.
 * 获取异常信息
 */

public class GetAllExceptionRequestEntity implements RequestEntity{
    private String id;
    private int page;
    private int pageSize;

    public GetAllExceptionRequestEntity(String id, int page, int pageSize) {
        this.id = id;
        this.page = page;
        this.pageSize = pageSize;
    }

    public String getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "GetAllExceptionRequestEntity{" +
                "id='" + id + '\'' +
                ", page='" + page + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_ALL_EXCEPTIONS;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
