//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.Gson;
//import com.mgtech.domain.entity.net.RequestEntity;
//import com.mgtech.domain.utils.HttpApi;
//
///**
// * Created by zhaixiang on 2018/4/13.
// * 获取参数说明
// */
//
//public class GetIndicatorDescriptionRequestEntity implements RequestEntity{
//
//    public GetIndicatorDescriptionRequestEntity() {}
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_GET_INDICATOR_PARAMETER_DESCRIPTION;
//    }
//
//    @Override
//    public String getBody() {
//        return new Gson().toJson(this);
//    }
//}
