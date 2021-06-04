package com.mgtech.domain.entity.net.request;

import com.mgtech.domain.entity.net.RequestEntity;

public class DefaultRequestEntity implements RequestEntity {
    private String url;

    public DefaultRequestEntity(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getBody() {
        return null;
    }
}
