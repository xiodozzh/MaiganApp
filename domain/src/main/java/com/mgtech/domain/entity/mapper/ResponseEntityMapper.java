package com.mgtech.domain.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgtech.domain.entity.net.response.IndicatorItemResponseEntity;
import com.mgtech.domain.entity.net.response.ResponseEntity;
import com.mgtech.domain.entity.net.response.ResultEntity;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhaixiang on 2017/1/9.
 * ResponseEntity 类型转换
 */

public class ResponseEntityMapper {

    public static <K extends ResponseEntity> K mapper(List<ResultEntity<K>> list, K entity) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ResultEntity<K> resultEntity = list.get(0);
        if (resultEntity.getData() != null && !resultEntity.getData().isEmpty()) {
            entity = resultEntity.getData().get(0);
            entity.setFlag(resultEntity.getResult());
            entity.setMessage(resultEntity.getMessage());
        } else {
            entity.setFlag(resultEntity.getResult());
            entity.setMessage(resultEntity.getMessage());
        }
        return entity;
    }

    public static <K extends ResponseEntity> List<K> mapperToList(List<ResultEntity<K>> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ResultEntity<K> resultEntity = list.get(0);
        if (resultEntity.getData() == null) {
            return null;
        }
        List<K> dataList = resultEntity.getData();
        for (K data : dataList) {
            data.setMessage(resultEntity.getMessage());
            data.setFlag(resultEntity.getResult());
        }
        return dataList;
    }

    public static <K> List<K> mapperToNormalList(List<ResultEntity<K>> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ResultEntity<K> resultEntity = list.get(0);
        if (resultEntity.getData() == null) {
            return null;
        }
        return resultEntity.getData();
    }

    /**
     * 转化为 List <ResultEntity <IndicatorItemResponseEntity>>
     * 获取最近的数据
     *
     * @param string 字符串
     * @return List <ResultEntity <IndicatorItemResponseEntity>>
     */
//    public static List<ResultEntity<IndicatorItemResponseEntity>> mapperToIndicator(String string) {
//        Type type = new TypeToken<List<ResultEntity<IndicatorItemResponseEntity>>>() {
//        }.getType();
//        return new Gson().fromJson(string, type);
//    }
//
//    /**
//     * 转化为 List<ResultEntity<GetCalculateDataResponseEntity>>
//     * 用于按时间获取数据
//     *
//     * @param string 字符串
//     * @return List <ResultEntity<GetCalculateDataResponseEntity>>
//     */
//    public static List<ResultEntity<GetCalculateDataResponseEntity>> mapperToCalculateDataResultList(String string) {
//        Type type = new TypeToken<List<ResultEntity<GetCalculateDataResponseEntity>>>() {
//        }.getType();
//        return new Gson().fromJson(string, type);
//    }

    /**
     * 转化为 List<IndicatorItemResponseEntity>
     * 按时获取数据中，不规范的字符串的转化
     *
     * @param string 字符串
     * @return List <IndicatorItemResponseEntity>
     */
    public static List<IndicatorItemResponseEntity> mapperToIndicatorEntities(String string) {
        Type type = new TypeToken<List<IndicatorItemResponseEntity>>() {
        }.getType();
        return new Gson().fromJson(string, type);
    }
}
