package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/8/31.
 * 删除药物
 */

public class DeleteMedicineEntity implements RequestEntity {
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;
    @SerializedName(NetConstant.JSON_ROW_GUID)
    private String rowGuid;

    public DeleteMedicineEntity(String id, String rowGuid) {
        this.id = id;
        this.rowGuid = rowGuid;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_DELETE_MEDICINE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
