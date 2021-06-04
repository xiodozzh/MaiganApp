//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.Gson;
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.entity.net.RequestEntity;
//
///**
// *
// * @author zhaixiang
// * 被动采样(异步计算)结果查询
// */
//
//public class QueryAsyncDataEntity implements RequestEntity {
//    @SerializedName(NetConstant.JSON_USER_ID)
//    private String id;
//    @SerializedName(NetConstant.JSON_REQUEST_DATE)
//    private String date;
//
//    public QueryAsyncDataEntity(String id, String date) {
//        this.id = id;
//        this.date = date;
//    }
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_QUERY_ASYNC_DATA;
//    }
//
//    @Override
//    public String getBody() {
//        return new Gson().toJson(this);
//    }
//}
