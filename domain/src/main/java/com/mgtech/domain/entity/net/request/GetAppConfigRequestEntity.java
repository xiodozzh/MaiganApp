package com.mgtech.domain.entity.net.request;

import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取App配置信息
 */

public class GetAppConfigRequestEntity implements RequestEntity {
    private String key;

    public GetAppConfigRequestEntity(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "GetAppConfigRequestEntity{" +
                "key='" + key + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_APP_CONFIG;
    }

    @Override
    public String getBody() {
        return null;
    }
}
