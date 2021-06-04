package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.DeleteEcgRequestEntity;
import com.mgtech.domain.entity.net.request.GetEcgListRequestEntity;
import com.mgtech.domain.entity.net.request.GetEcgRequestEntity;
import com.mgtech.domain.entity.net.request.SaveEcgRequestEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * 处理ECG数据
 */

public class EcgUseCase {
    private NetRepository.Ecg ecgRepository;
    private Subscription saveSubscription;
    private Subscription getListSubscription;
    private Subscription getEcgSubscription;
    private Subscription deleteEcgSubscription;

    public EcgUseCase(NetRepository.Ecg ecgRepository) {
        this.ecgRepository = ecgRepository;
    }

    public void saveEcg(SaveEcgRequestEntity entity, Subscriber<NetResponseEntity<EcgResponseEntity>> subscriber) {
        unSubscribeSave();
        this.saveSubscription = this.ecgRepository.saveEcg(entity)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getEcgList(GetEcgListRequestEntity entity, Subscriber<NetResponseEntity<List<EcgResponseEntity>>> subscriber) {
        unSubscribeGetList();
        this.getListSubscription = this.ecgRepository.getEcgList(entity,NetConstant.CACHE_IF_NET_ERROR)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getEcg(GetEcgRequestEntity entity, int cacheType,Subscriber<NetResponseEntity<EcgResponseEntity>> subscriber) {
        unSubscribeGet();
        this.getEcgSubscription = this.ecgRepository.getEcgById(entity,cacheType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteEcg(DeleteEcgRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeDelete();
        this.deleteEcgSubscription = this.ecgRepository.deleteEcg(entity)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unSubscribe() {
        unSubscribeSave();
        unSubscribeGetList();
        unSubscribeGet();
        unSubscribeDelete();
    }

    private void unSubscribeSave(){
        if (saveSubscription != null && !saveSubscription.isUnsubscribed()) {
            saveSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetList(){
        if (getListSubscription != null && !getListSubscription.isUnsubscribed()) {
            getListSubscription.unsubscribe();
        }
    }

    private void unSubscribeGet(){
        if (getEcgSubscription != null && !getEcgSubscription.isUnsubscribed()) {
            getEcgSubscription.unsubscribe();
        }
    }

    private void unSubscribeDelete(){
        if (deleteEcgSubscription != null && !deleteEcgSubscription.isUnsubscribed()) {
            deleteEcgSubscription.unsubscribe();
        }
    }
}
