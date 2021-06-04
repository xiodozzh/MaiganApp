package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.BindDeviceRequestEntity;
import com.mgtech.domain.entity.net.request.CheckBraceletRequestEntity;
import com.mgtech.domain.entity.net.request.GetBraceletConfigRequestEntity;
import com.mgtech.domain.entity.net.request.GetDeviceBindInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SetBraceletInfoRequestEntity;
import com.mgtech.domain.entity.net.request.UnbindBraceletRequestEntity;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.entity.net.response.CheckBraceletResponseEntity;
import com.mgtech.domain.entity.net.response.GetBindInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.NetConstant;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * DeviceUseCase
 */

public class DeviceUseCase {
    private NetRepository.Device repository;
    private Subscription checkSubscription;
    private Subscription bindSubscription;
    private Subscription unbindSubscription;
    private Subscription getBraceletSubscription;
    private Subscription getConfigSubscription;
    private Subscription setConfigSubscription;
    private Subscription setInfoSubscription;

    public DeviceUseCase(NetRepository.Device repository) {
        this.repository = repository;
    }

    public void check(CheckBraceletRequestEntity entity, Subscriber<NetResponseEntity<CheckBraceletResponseEntity>> subscriber) {
        unSubscribeCheck();
        checkSubscription = repository.checkBraceletCopyright(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void bind(BindDeviceRequestEntity entity, Subscriber<NetResponseEntity<BraceletConfigEntity>> subscriber) {
        unSubscribeBind();
        bindSubscription = repository.bindDevice(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unbind(UnbindBraceletRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeUnBind();
        unbindSubscription = repository.unbindDevice(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unbindInOtherThread(UnbindBraceletRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeUnBind();
        unbindSubscription = repository.unbindDevice(entity)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);
    }

    public void getBracelet(GetDeviceBindInfoRequestEntity entity, boolean inService,
                            Subscriber<NetResponseEntity<GetBindInfoResponseEntity>> subscriber) {
        if (inService) {
            getBraceletSubscription= repository.getBracelet(entity, NetConstant.NO_CACHE)
                    .subscribe(subscriber);
        }else {
            getBraceletSubscription = repository.getBracelet(entity, NetConstant.NO_CACHE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void getConfig(GetBraceletConfigRequestEntity entity, boolean inService,
                          Subscriber<NetResponseEntity<BraceletConfigEntity>> subscriber) {
        unSubscribeGetParameters();
        if (inService) {
            getConfigSubscription = repository.getConfig(entity, NetConstant.NO_CACHE)
                    .subscribeOn(Schedulers.immediate())
                    .observeOn(Schedulers.immediate())
                    .subscribe(subscriber);
        } else {
            getConfigSubscription = repository.getConfig(entity, NetConstant.NO_CACHE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void setConfig(BraceletConfigEntity entity, boolean inService, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeSetParameters();
        if (inService) {
            setConfigSubscription = repository.setConfig(entity)
                    .subscribeOn(Schedulers.immediate())
                    .observeOn(Schedulers.immediate())
                    .subscribe(subscriber);
        } else {
            setConfigSubscription = repository.setConfig(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void setInfo(SetBraceletInfoRequestEntity entity,boolean inService,Subscriber<NetResponseEntity>subscriber){
        unSubscribeSetInfo();
        if (inService) {
            setInfoSubscription = repository.setInfo(entity)
                    .subscribe(subscriber);
        }else{
            setInfoSubscription = repository.setInfo(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void unSubscribe() {
        unSubscribeCheck();
        unSubscribeBind();
        unSubscribeUnBind();
        unSubscribeGetBracelet();
        unSubscribeGetParameters();
        unSubscribeSetParameters();
    }

    private void unSubscribeCheck(){
        if (checkSubscription != null && !checkSubscription.isUnsubscribed()) {
            checkSubscription.unsubscribe();
        }
    }

    private void unSubscribeBind(){
        if (bindSubscription != null && !bindSubscription.isUnsubscribed()) {
            bindSubscription.unsubscribe();
        }
    }

    private void unSubscribeUnBind(){
        if (unbindSubscription != null && !unbindSubscription.isUnsubscribed()) {
            unbindSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetBracelet(){
        if (getBraceletSubscription != null && !getBraceletSubscription.isUnsubscribed()) {
            getBraceletSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetParameters(){
        if (getConfigSubscription != null && !getConfigSubscription.isUnsubscribed()) {
            getConfigSubscription.unsubscribe();
        }
    }

    private void unSubscribeSetParameters(){
        if (setConfigSubscription != null && !setConfigSubscription.isUnsubscribed()) {
            setConfigSubscription.unsubscribe();
        }
    }

    private void unSubscribeSetInfo(){
        if (setInfoSubscription != null && !setInfoSubscription.isUnsubscribed()) {
            setInfoSubscription.unsubscribe();
        }
    }
}
