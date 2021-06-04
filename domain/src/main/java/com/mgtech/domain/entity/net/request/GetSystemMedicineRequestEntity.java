package com.mgtech.domain.entity.net.request;

import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2018/3/17.
 * 获取
 */

public class GetSystemMedicineRequestEntity implements RequestEntity {
    @Override
    public String getUrl() {
        return HttpApi.API_GET_ALL_MEDICINE;
    }

    @Override
    public String getBody() {
        return null;
    }
}
