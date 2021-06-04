package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.DeleteExceptionEntity;
import com.mgtech.domain.entity.net.request.GetAllExceptionRequestEntity;
import com.mgtech.domain.entity.net.request.SetExceptionReadRequestEntity;
import com.mgtech.domain.entity.net.request.UserIdRequestEntity;
import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * 异常提醒
 */

public class ExceptionUseCase {
    private NetRepository.Abnormal abnormalRepository;
    private Subscription getAllExceptionSubscription;
    private Subscription getExceptionSubscription;
    private Subscription markReadSubscription;
    private Subscription deleteExceptionSubscription;
    private Subscription setThresholdSubscription;
    private Subscription getThresholdSubscription;

    public ExceptionUseCase(NetRepository.Abnormal abnormalRepository) {
        this.abnormalRepository = abnormalRepository;
    }

    /**
     * 获取异常提醒
     *
     * @param subscriber 订阅
     */
    public void getAllException(String id, int page, int pageSize, Subscriber<NetResponseEntity<List<ExceptionResponseEntity>>> subscriber) {
        unSubscribeGetAllException();
        GetAllExceptionRequestEntity entity = new GetAllExceptionRequestEntity(id, page, pageSize);
        this.getAllExceptionSubscription = this.abnormalRepository.getAllException(entity, NetConstant.CACHE_IF_NET_ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getException(String id, int page, int pageSize, Subscriber<NetResponseEntity<List<ExceptionResponseEntity>>> subscriber) {
        unSubscribeGetException();
        GetAllExceptionRequestEntity entity = new GetAllExceptionRequestEntity(id, page, pageSize);
        this.getExceptionSubscription = this.abnormalRepository.getException(entity, NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Single<NetResponseEntity<List<ExceptionResponseEntity>>> getExceptionObservable(String id, int page, int pageSize){
        GetAllExceptionRequestEntity entity = new GetAllExceptionRequestEntity(id, page, pageSize);
        return this.abnormalRepository.getException(entity);
    }

    public void markRead(String id, String targetId, String noticeId, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeMarkRead();
        SetExceptionReadRequestEntity entity = new SetExceptionReadRequestEntity(id, targetId, noticeId);
        this.markReadSubscription = this.abnormalRepository.markRead(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getThreshold(String id, Subscriber<NetResponseEntity<ThresholdRequestEntity>> subscriber) {
        unSubscribeGetThreshold();
        this.getThresholdSubscription = this.abnormalRepository.getThreshold(new UserIdRequestEntity(id), NetConstant.CACHE_IF_NET_ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void setThreshold(ThresholdRequestEntity entity, Subscriber<NetResponseEntity<ThresholdRequestEntity>> subscriber) {
        unSubscribeSetThreshold();
        this.getThresholdSubscription = this.abnormalRepository.setThreshold(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 删除异常提醒
     *
     * @param subscriber 订阅
     */
    public void deleteException(String userId, String noticeId, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeDelete();
        DeleteExceptionEntity entity = new DeleteExceptionEntity(userId, noticeId);
        this.deleteExceptionSubscription = this.abnormalRepository.deleteException(entity)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 取消订阅
     */
    public void unSubscribe() {
        unSubscribeGetAllException();
        unSubscribeGetException();
        unSubscribeMarkRead();
        unSubscribeDelete();
        unSubscribeGetThreshold();
        unSubscribeSetThreshold();
    }

    private void unSubscribeGetAllException() {
        if (getAllExceptionSubscription != null && !getAllExceptionSubscription.isUnsubscribed()) {
            getAllExceptionSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetException() {
        if (getExceptionSubscription != null && !getExceptionSubscription.isUnsubscribed()) {
            getExceptionSubscription.unsubscribe();
        }
    }

    private void unSubscribeMarkRead() {
        if (markReadSubscription != null && !markReadSubscription.isUnsubscribed()) {
            markReadSubscription.unsubscribe();
        }
    }

    private void unSubscribeDelete() {
        if (deleteExceptionSubscription != null && !deleteExceptionSubscription.isUnsubscribed()) {
            deleteExceptionSubscription.unsubscribe();
        }
    }

    private void unSubscribeSetThreshold() {
        if (setThresholdSubscription != null && !setThresholdSubscription.isUnsubscribed()) {
            setThresholdSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetThreshold() {
        if (getThresholdSubscription != null && !getThresholdSubscription.isUnsubscribed()) {
            getThresholdSubscription.unsubscribe();
        }
    }

}
