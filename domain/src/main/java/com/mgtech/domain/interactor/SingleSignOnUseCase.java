package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.CheckPhoneInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SaveMobileInfoRequestEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * 单点登陆
 */

public class SingleSignOnUseCase {
    private NetRepository.SingleSignOn repository;
    private Subscription saveInfoSubscription;
    private Subscription checkInfoSubscription;

    public SingleSignOnUseCase(NetRepository.SingleSignOn repository) {
        this.repository = repository;
    }

    public void savePhoneInfoInService(SaveMobileInfoRequestEntity entity, Subscriber<NetResponseEntity> subscriber){
        saveInfoSubscription = repository.savePhoneInfo(entity)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);
    }

    public void checkPhoneInfo(CheckPhoneInfoRequestEntity entity, Subscriber<NetResponseEntity<CheckPhoneInfoResponseEntity>> subscriber){
        unSubscribeCheck();
        checkInfoSubscription = repository.checkPhoneInfo(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void unSubscribe(){
        if (saveInfoSubscription != null && !saveInfoSubscription.isUnsubscribed()){
            saveInfoSubscription.unsubscribe();
        }
        unSubscribeCheck();
    }

    private void unSubscribeCheck(){
        if (checkInfoSubscription != null && !checkInfoSubscription.isUnsubscribed()){
            checkInfoSubscription.unsubscribe();
        }
    }
}
