package com.mgtech.domain.entity.net.request;

import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * @date 2018/3/29
 * 获取手环参数
 */

public class GetBraceletConfigRequestEntity implements RequestEntity{
    private int type = 0;
    private String id;


    public GetBraceletConfigRequestEntity(int type, String id) {
        this.type = type;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_BRACELET_CONFIG + "?" +"type="+type ;
    }

    @Override
    public String getBody() {
        return null;
    }
}
