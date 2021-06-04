package com.mgtech.domain.interactor;

import android.util.Log;

import com.mgtech.domain.entity.ContactEntity;
import com.mgtech.domain.repository.Repository;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * 查找手机号码
 */

public class ContactUseCase {
    private Repository.Contact repository;
    private Subscription subscription;

    public ContactUseCase(Repository.Contact repository) {
        this.repository = repository;
    }

    /**
     * 搜索手机通讯录
     *
     * @param subscriber  订阅者
     */
    public void searchContact( Subscriber<List<ContactEntity>> subscriber) {
        unSubscribe();
        this.subscription = repository.getContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unSubscribe(){
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}
