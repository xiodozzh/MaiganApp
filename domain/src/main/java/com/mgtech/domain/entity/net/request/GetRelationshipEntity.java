package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取我关注的及关注我的
 */

public class GetRelationshipEntity implements RequestEntity {
    /**
     * 我监护的
     */
    public static final int TYPE_MONITOR = 0;
    /**
     * 监护我的
     */
    public static final int TYPE_MONITORED = 1;
    public static final int TYPE_BOTH = 2;

    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;
    @SerializedName(NetConstant.JSON_CURRENT_PAGE)
    private int currentPage;
    @SerializedName(NetConstant.JSON_PAGE_SIZE)
    private int pageSize;
    @SerializedName(NetConstant.JSON_TYPE)
    private int type;

    public GetRelationshipEntity(String id, int currentPage, int pageSize, int type) {
        this.id = id;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_RELATIONSHIP;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
