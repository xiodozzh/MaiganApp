package com.mgtech.domain.entity;

/**
 * Created by zhaixiang on 2017/5/9.
 * 数据转换为entity
 */

public interface Mapper<T> {
    T mapToEntity(String string);
}
