package com.mgtech.domain.repository;

import androidx.annotation.NonNull;

import com.mgtech.domain.entity.ContactEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by zhaixiang on 2016/5/20.
 * 全部repository 接口
 * 获取数据或存储数据
 */
public interface Repository {

    interface Contact{
        Observable<List<ContactEntity>> getContact();
    }

}
