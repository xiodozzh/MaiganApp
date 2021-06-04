package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/5/20.
 * 标记是否运动
 */

public class MarkDataRequestEntity {
    @SerializedName(NetConstant.JSON_IS_SPORT)
    private String isSport;
    @SerializedName(NetConstant.JSON_ROW_GUID)
    private String rowGuid;

    public MarkDataRequestEntity(String isSport, String rowGuid) {
        this.isSport = isSport;
        this.rowGuid = rowGuid;
    }

    public String getIsSport() {
        return isSport;
    }

    public void setIsSport(String isSport) {
        this.isSport = isSport;
    }

    public String getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(String rowGuid) {
        this.rowGuid = rowGuid;
    }
}
