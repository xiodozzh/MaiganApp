//package com.mgtech.domain.entity.mapper;
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.mgtech.domain.entity.net.response.UserInfoResponseEntity;
//import com.mgtech.domain.entity.net.response.ResultEntity;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
///**
// * Created by zhaixiang on 2017/5/5.
// * 转换为 GetPersonalInfoResponseMapper
// */
//
//public class GetPersonalInfoResponseMapper implements MapperEntity<UserInfoResponseEntity> {
//    @Override
//    public UserInfoResponseEntity mapToEntity(String text) {
//        Log.e(TAG, "需要转换的数据 " + text);
////        UserInfoResponseEntity responseEntity = new UserInfoResponseEntity();
////        Type type = new TypeToken<List<ResultEntity<UserInfoResponseEntity>>>() {
////        }.getType();
////
////        List<ResultEntity<UserInfoResponseEntity>> list = new Gson().fromJson(text, type);
////        responseEntity = ResponseEntityMapper.mapper(list, responseEntity);
//        Type type = new TypeToken<UserInfoResponseEntity>() {
//        }.getType();
//        return new Gson().fromJson(text, type);
//    }
//}
