package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.response.HealthKnowledgeResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HealthManagementUseCase {
    private NetRepository.HealthManagement repository;
    private Subscription getListSubscription;

    public HealthManagementUseCase(NetRepository.HealthManagement repository) {
        this.repository = repository;
    }

    public void getKnowledgeList(Subscriber<NetResponseEntity<List<HealthKnowledgeResponseEntity>>>subscriber){
        getListSubscription = repository.getWeeklyReportList(NetConstant.CACHE_IF_NET_ERROR)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unSubscriber(){
        if (getListSubscription != null && !getListSubscription.isUnsubscribed()){
            getListSubscription.unsubscribe();
        }
    }
}
