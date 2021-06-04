package com.mgtech.domain.rx;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.exception.RefreshTokenErrorException;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;

public class NetSubscriber<T> extends Subscriber<NetResponseEntity<T>> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof RefreshTokenErrorException){
            EventBus.getDefault().postSticky(new GoToLoginEvent());
        }
    }

    @Override
    public void onNext(NetResponseEntity<T> tNetResponseEntity) {

    }
}
