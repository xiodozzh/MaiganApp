package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.GetUnreadMessageRequestEntity;
import com.mgtech.domain.entity.net.request.MarkHealthKnowledgeReadRequestEntity;
import com.mgtech.domain.entity.net.request.MarkWeeklyReportReadRequestEntity;
import com.mgtech.domain.entity.net.response.GetAllUnreadMessageResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.SaveUtils;


import javax.inject.Inject;

import dagger.Provides;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UnreadMessageUseCase {
    @Inject
    NetRepository.Message repository;
    private Subscription getAllSubscription;
    private Subscription markKnowledgeSubscription;
    private Subscription markWeeklyReportSubscription;

    @Inject
    public UnreadMessageUseCase(NetRepository.Message repository) {
        this.repository = repository;
    }

    public void getAll(Subscriber<NetResponseEntity<GetAllUnreadMessageResponseEntity>> subscriber) {
        getAllUnSubscribe();
        getAllSubscription = repository.getAllUnreadMessage(new GetUnreadMessageRequestEntity(SaveUtils.getUserId()))
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);
    }

    public void markHealthKnowledgeRead(String knowledgeId, Subscriber<NetResponseEntity> subscriber) {
        markKnowledgeSubscription = repository.markHealthKnowledgeRead(new MarkHealthKnowledgeReadRequestEntity(SaveUtils.getUserId(), knowledgeId))
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);
    }

    public void markWeeklyReportRead(String weeklyReportId, Subscriber<NetResponseEntity> subscriber) {
        markWeeklyReportSubscription = repository.markWeeklyReportRead(new MarkWeeklyReportReadRequestEntity(SaveUtils.getUserId(), weeklyReportId))
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(subscriber);
    }

    private void getAllUnSubscribe() {
        if (getAllSubscription != null && !getAllSubscription.isUnsubscribed()) {
            getAllSubscription.unsubscribe();
        }
    }

    private void markKnowledgeUnSubscribe() {
        if (markKnowledgeSubscription != null && !markKnowledgeSubscription.isUnsubscribed()) {
            markKnowledgeSubscription.unsubscribe();
        }
    }

    private void markWeeklyReportUnSubscribe() {
        if (markWeeklyReportSubscription != null && !markWeeklyReportSubscription.isUnsubscribed()) {
            markWeeklyReportSubscription.unsubscribe();
        }
    }

    public void unSubscribe() {
        getAllUnSubscribe();
        markKnowledgeUnSubscribe();
        markWeeklyReportUnSubscribe();
    }
}
