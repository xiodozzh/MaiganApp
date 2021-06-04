package com.mgtech.domain.interactor;

import android.util.Log;

import com.mgtech.domain.entity.net.request.SavePushIdRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.SaveUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class NotificationUseCase {
    private NetRepository.Notification repository;
    private Subscription subscriptionSet;

    public NotificationUseCase(NetRepository.Notification repository) {
        this.repository = repository;
    }

    public void savePushIdInService(SavePushIdRequestEntity entity){
        unSubscribe();
        subscriptionSet = repository.savePushId(entity)
                .subscribe(new Subscriber<NetResponseEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        SaveUtils.setNeedPushId(true);
                    }

                    @Override
                    public void onNext(NetResponseEntity netResponseEntity) {
                        Log.i("push", "onNext: " + netResponseEntity);
                        SaveUtils.setNeedPushId(netResponseEntity.getCode() != 0);
                    }
                });
    }

//    public void

    public void unSubscribe(){
        if (subscriptionSet != null && !subscriptionSet.isUnsubscribed()){
            subscriptionSet.unsubscribe();
        }
    }
}
