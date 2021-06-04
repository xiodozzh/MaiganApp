//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.Gson;
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.entity.net.RequestEntity;
//
///**
// * Created by zhaixiang on 2017/9/27.
// * 获取全部药物信息
// */
//
//public class GetAllMedicineRequestEntity implements RequestEntity{
//    @SerializedName(NetConstant.JSON_USER_ID)
//    private String id;
//
//    public GetAllMedicineRequestEntity(String id) {
//        this.id = id;
//    }
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_GET_MEDICINE_TIMER;
//    }
//
//    @Override
//    public String getBody() {
//        return new Gson().toJson(this);
//    }
//}
