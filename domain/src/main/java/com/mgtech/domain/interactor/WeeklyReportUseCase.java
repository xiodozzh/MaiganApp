package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.GetWeeklyReportRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeeklyReportUseCase {
    private NetRepository.WeeklyReport repository;
    private Subscription getListSubscription;

    public WeeklyReportUseCase(NetRepository.WeeklyReport repository) {
        this.repository = repository;
    }

    public void getWeeklyReportList(String id, Subscriber<NetResponseEntity<List<WeeklyReportResponseEntity>>>subscriber){
        unSubscribeGet();
        getListSubscription = repository.getWeeklyReportList(new GetWeeklyReportRequestEntity(id),NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void unSubscribeGet(){
        if (getListSubscription != null && !getListSubscription.isUnsubscribed()){
            getListSubscription.unsubscribe();
        }
    }

    public void unSubscriber(){
        unSubscribeGet();
    }

}
