package com.mgtech.domain.entity.mapper;

/**
 * Created by zhaixiang on 2017/5/5.
 */

public interface MapperEntity<T> {
    String TAG = "mapper";
    T mapToEntity(String text);
}
