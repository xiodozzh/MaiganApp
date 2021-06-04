package com.mgtech.data.cache;

import com.mgtech.domain.entity.net.RequestEntity;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * @author zhaixiang
 */
public interface IDiskCache<T> {
    /**
     * 从磁盘中获取数据
     *
     * @param entity 请求
     * @param type   返回的类型
     * @return 数据
     */
    Observable<T> getFromDisk(RequestEntity entity, Type type);

    /**
     * 保存数据至磁盘
     *
     * @param entity 请求
     * @param value  网络body
     */
    void saveToDisk(RequestEntity entity, T value);
}
