package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.domain.entity.net.request.GetCustomMedicineRequestEntity;
import com.mgtech.domain.entity.net.request.MedicineAddEntity;
import com.mgtech.domain.entity.net.request.MedicineDeleteRequestEntity;
import com.mgtech.domain.entity.net.request.MedicineEditRequestEntity;
import com.mgtech.domain.entity.net.request.SetMedicationPlanRequestEntity;
import com.mgtech.domain.entity.net.request.SetMedicationRemindRequestEntity;
import com.mgtech.domain.entity.net.response.CustomMedicineEntity;
import com.mgtech.domain.entity.net.response.MedicationRemindResponseEntity;
import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * 用药
 */

public class MedicineUseCase {
    private NetRepository.Medicine repository;
    private Subscription getMedicineListSubscription;
    private Subscription getPlanListSubscription;
    private Subscription getCompletedPlanListSubscription;
    private Subscription addPlanSubscription;
    private Subscription updatePlanSubscription;
    private Subscription stopPlanSubscription;
    private Subscription deletePlanSubscription;
    private Subscription getRemindSubscription;
    private Subscription setRemindSubscription;
    private Subscription addCustomSubscription;
    private Subscription editCustomSubscription;
    private Subscription deleteCustomSubscription;
    private Subscription getCustomSubscription;

    public MedicineUseCase(NetRepository.Medicine medicineRepository) {
        this.repository = medicineRepository;
    }

    /**
     * 获取用户全部用药信息
     *
     * @param subscriber 订阅者
     */
    public void getLibMedicine(String userId, Subscriber<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>> subscriber) {
        unSubscribeGetMedicineList();
        this.getMedicineListSubscription = repository.getMedicineList(userId, NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Observable<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>> getLibMedicineObs(String userId){
        return repository.getMedicineList(userId, NetConstant.NO_CACHE);
    }

    public Observable<NetResponseEntity<List<CustomMedicineEntity>>>getCustomMedicineListObs(String userId){
        return repository.getCustomMedicineList(new GetCustomMedicineRequestEntity(userId,0,100));
    }


    public void getMedicationPlanList(String userId, Subscriber<NetResponseEntity<List<MedicationPlanEntity>>> subscriber) {
        unSubscribeGetMedicinePlanList();
        this.getPlanListSubscription = repository.getMedicationPlanList(userId, NetConstant.CACHE_IF_NET_ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCompletedMedicationPlanList(String userId, Subscriber<NetResponseEntity<List<MedicationPlanEntity>>> subscriber) {
        unSubscribeGetCompletedMedicinePlanList();
        this.getCompletedPlanListSubscription = repository.getCompletedMedicationPlanList(userId, NetConstant.CACHE_IF_NET_ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void addMedicationPlan(MedicationPlanEntity entity, Subscriber<NetResponseEntity<MedicationPlanEntity>> subscriber) {
        unSubscribeAddPlanList();
        this.addPlanSubscription = repository.addMedicationPlan(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void updateMedicationPlan(MedicationPlanEntity entity, Subscriber<NetResponseEntity<MedicationPlanEntity>> subscriber) {
        unSubscribeUpdatePlanList();
        this.updatePlanSubscription = repository.updateMedicationPlan(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void stopMedicationPlan(String userId, String planId, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeStopPlanList();
        this.stopPlanSubscription = repository.stopMedicationPlan(new SetMedicationPlanRequestEntity(planId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteMedicationPlan(String userId, String planId, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeDeletePlanList();
        this.deletePlanSubscription = repository.deleteMedicationPlan(new SetMedicationPlanRequestEntity(planId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getMedicationRemindList(String userId, int cacheType, Subscriber<NetResponseEntity<MedicationRemindResponseEntity>> subscriber) {
        unSubscribeGetRemindList();
        this.getRemindSubscription = repository.getMedicationRemindList(userId, cacheType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void setMedicationRemind(SetMedicationRemindRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeSetRemind();
        this.setRemindSubscription = repository.setMedicationRemind(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCustomMedicine(GetCustomMedicineRequestEntity entity, Subscriber<NetResponseEntity<List<CustomMedicineEntity>>> subscriber) {
        unSubscribeGetCustomMedicine();
        this.getCustomSubscription = repository.getCustomMedicineList(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void addCustomMedicine(MedicineAddEntity entity, Subscriber<NetResponseEntity<CustomMedicineEntity>> subscriber) {
        unSubscribeAddMedicine();
        this.addCustomSubscription = repository.addMedicine(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void editCustomMedicine(MedicineEditRequestEntity entity, Subscriber<NetResponseEntity<CustomMedicineEntity>> subscriber) {
        unSubscribeEditMedicine();
        this.editCustomSubscription = repository.editMedicine(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteCustomMedicine(MedicineDeleteRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeDeleteMedicine();
        this.deleteCustomSubscription = repository.deleteMedicine(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消订阅
     */
    public void unSubscribe() {
        unSubscribeGetMedicineList();
        unSubscribeGetMedicinePlanList();
        unSubscribeGetCompletedMedicinePlanList();
        unSubscribeAddPlanList();
        unSubscribeUpdatePlanList();
        unSubscribeStopPlanList();
        unSubscribeDeletePlanList();
        unSubscribeGetRemindList();
        unSubscribeSetRemind();
    }

    private void unSubscribeGetMedicineList() {
        if (getMedicineListSubscription != null && !getMedicineListSubscription.isUnsubscribed()) {
            getMedicineListSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetMedicinePlanList() {
        if (getPlanListSubscription != null && !getPlanListSubscription.isUnsubscribed()) {
            getPlanListSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetCompletedMedicinePlanList() {
        if (getCompletedPlanListSubscription != null && !getCompletedPlanListSubscription.isUnsubscribed()) {
            getCompletedPlanListSubscription.unsubscribe();
        }
    }

    private void unSubscribeAddPlanList() {
        if (addPlanSubscription != null && !addPlanSubscription.isUnsubscribed()) {
            addPlanSubscription.unsubscribe();
        }
    }

    private void unSubscribeUpdatePlanList() {
        if (updatePlanSubscription != null && !updatePlanSubscription.isUnsubscribed()) {
            updatePlanSubscription.unsubscribe();
        }
    }

    private void unSubscribeStopPlanList() {
        if (stopPlanSubscription != null && !stopPlanSubscription.isUnsubscribed()) {
            stopPlanSubscription.unsubscribe();
        }
    }

    private void unSubscribeDeletePlanList() {
        if (deletePlanSubscription != null && !deletePlanSubscription.isUnsubscribed()) {
            deletePlanSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetRemindList() {
        if (getRemindSubscription != null && !getRemindSubscription.isUnsubscribed()) {
            getRemindSubscription.unsubscribe();
        }
    }

    private void unSubscribeSetRemind() {
        if (setRemindSubscription != null && !setRemindSubscription.isUnsubscribed()) {
            setRemindSubscription.unsubscribe();
        }
    }

    private void unSubscribeAddMedicine() {
        if (addCustomSubscription != null && !addCustomSubscription.isUnsubscribed()) {
            addCustomSubscription.unsubscribe();
        }
    }

    private void unSubscribeDeleteMedicine() {
        if (deleteCustomSubscription != null && !deleteCustomSubscription.isUnsubscribed()) {
            deleteCustomSubscription.unsubscribe();
        }
    }

    private void unSubscribeEditMedicine() {
        if (editCustomSubscription != null && !editCustomSubscription.isUnsubscribed()) {
            editCustomSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetCustomMedicine() {
        if (getCustomSubscription != null && !getCustomSubscription.isUnsubscribed()) {
            getCustomSubscription.unsubscribe();
        }
    }
}
