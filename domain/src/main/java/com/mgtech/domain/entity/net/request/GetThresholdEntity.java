//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.entity.net.RequestEntity;
//
///**
// * Created by zhaixiang on 2017/8/28.
// * 获取推送阈值
// */
//
//public class GetThresholdEntity implements RequestEntity {
//    @SerializedName(NetConstant.JSON_USER_ID)
//    private String id;
//
//    public GetThresholdEntity(String id) {
//        this.id = id;
//    }
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_GET_THRESHOLD + "/" + id;
//    }
//
//    @Override
//    public String getBody() {
//        return null;
//    }
//}
