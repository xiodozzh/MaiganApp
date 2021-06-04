package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.GetStepRequestEntity;
import com.mgtech.domain.entity.net.request.SaveStepRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.domain.repository.NetRepository;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhaixiang on 2017/12/15.
 * 计步
 */

public class StepUseCase {
    private NetRepository.Step repository;
    private Subscription saveStepSubscription;
    private Subscription getStepDetailSubscription;
    private Subscription getStepStatisticsSubscription;
    private Subscription getStepStatisticsByDateSubscription;

    public StepUseCase(NetRepository.Step repository) {
        this.repository = repository;
    }

    public void saveStep(SaveStepRequestEntity entity, Subscriber<NetResponseEntity> subscriber){
        unSubscribeSave();
        saveStepSubscription = repository.saveStep(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void getStepDetail(int cacheType,GetStepRequestEntity entity,
                              Subscriber<NetResponseEntity<List<StepDetailItemEntity>>> subscriber){
        unSubscribeDetail();
        getStepDetailSubscription = repository.getStepDetail(cacheType,entity)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//    public void getStepStatistics(int cacheType,GetStepStatisticsDataRequestEntity entity,
//                              Subscriber<NetResponseEntity<List<StepStatisticsItemEntity>>> subscriber){
//        unSubscribeGetStatistics();
//        getStepStatisticsSubscription = repository.getStepStatisticsInfo(cacheType,entity)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
//
//    public void getStepStatisticsByDate(int cacheType,GetStepStatisticsDataByTimeRequestEntity entity,
//                                  Subscriber<NetResponseEntity<List<StepStatisticsItemEntity>>> subscriber){
//        unSubscribeGetStatisticsByDate();
//        getStepStatisticsByDateSubscription = repository.getStepStatisticsInfoByTime(cacheType,entity)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }


    public void unSubscribe(){
        unSubscribeGetStatisticsByDate();
        unSubscribeGetStatistics();
        unSubscribeDetail();
        unSubscribeSave();
    }

    public void unSubscribeDetail(){
        if (getStepDetailSubscription != null && !getStepDetailSubscription.isUnsubscribed()) {
            getStepDetailSubscription.unsubscribe();
        }
    }

    private void unSubscribeSave(){
        if (saveStepSubscription != null && !saveStepSubscription.isUnsubscribed()) {
            saveStepSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetStatistics(){
        if (getStepStatisticsSubscription != null && !getStepStatisticsSubscription.isUnsubscribed()) {
            getStepStatisticsSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetStatisticsByDate(){
        if (getStepStatisticsByDateSubscription != null && !getStepStatisticsByDateSubscription.isUnsubscribed()) {
            getStepStatisticsByDateSubscription.unsubscribe();
        }
    }

}
