package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/3/2.
 * 获取用药
 */

public class GetMedicineDetailRequestEntity {
    @SerializedName(NetConstant.JSON_ROW_GUID)
    private String rowId;

    public GetMedicineDetailRequestEntity(String rowId) {
        this.rowId = rowId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    @Override
    public String toString() {
        return "GetMedicineTimerRequestEntity{" +
                "rowId='" + rowId + '\'' +
                '}';
    }
}
