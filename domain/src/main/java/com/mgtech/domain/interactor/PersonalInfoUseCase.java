package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.GetUserInfoRequestEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * 用户个人信息
 */

public class PersonalInfoUseCase {
    private NetRepository.PersonalInformation repository;
    private Subscription setSubscription;
    private Subscription getSubscription;
    private Subscription getDiseaseListSubscription;


    public PersonalInfoUseCase(NetRepository.PersonalInformation repository) {
        this.repository = repository;
    }

    /**
     * 更新个人信息
     *
     * @param entity     信息
     * @param subscriber 消息返回订阅
     */
    public void setInfo(PersonalInfoEntity entity,
                        Subscriber<NetResponseEntity<PersonalInfoEntity>> subscriber) {
        unSubscribeSet();
        setSubscription = this.repository.setInfo(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取个人信息
     *
     * @param cacheType 是否使用缓存
     * @param id         用户id
     * @param subscriber 返回订阅
     */
    public void getInfo(int cacheType, String id, Subscriber<NetResponseEntity<PersonalInfoEntity>> subscriber) {
        unSubscribeGet();
        GetUserInfoRequestEntity entity = new GetUserInfoRequestEntity(id);
        getSubscription = this.repository.getInfo(entity, cacheType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDiseaseList(Subscriber<NetResponseEntity<AllDiseaseEntity>>subscriber){
        unSubscribeGetDiseaseList();
        getDiseaseListSubscription = repository.getDiseaseList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Observable<NetResponseEntity<PersonalInfoEntity>> getPersonalInfoObservable(String id){
        GetUserInfoRequestEntity entity = new GetUserInfoRequestEntity(id);
        return repository.getInfo(entity, NetConstant.NO_CACHE);
    }

    public Observable<NetResponseEntity<AllDiseaseEntity>> getAllDiseaseObservable(){
        return repository.getDiseaseList();
    }

    public void unSubscribe() {
        unSubscribeGetDiseaseList();
        unSubscribeSet();
        unSubscribeGet();
    }

    private void unSubscribeSet() {
        if (setSubscription != null && !setSubscription.isUnsubscribed()) {
            setSubscription.unsubscribe();
        }
    }

    private void unSubscribeGet() {
        if (getSubscription != null && !getSubscription.isUnsubscribed()) {
            getSubscription.unsubscribe();
        }
    }


    private void unSubscribeGetDiseaseList(){
        if (getDiseaseListSubscription != null && !getDiseaseListSubscription.isUnsubscribed()) {
            getDiseaseListSubscription.unsubscribe();
        }
    }
}
