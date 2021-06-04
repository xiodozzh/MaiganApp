package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zhaixiang on 2018/3/7.
 * 保存手机信息
 */

public class SaveMobileInfoRequestEntity implements RequestEntity {
    @SerializedName("macAddress")
    private String phoneMacAddress;
    @SerializedName("extra")
    private String extra;

    public SaveMobileInfoRequestEntity(String phoneMacAddress, String extra) {
        this.phoneMacAddress = phoneMacAddress;
        this.extra = extra;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SAVE_PHONE_INFO;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
