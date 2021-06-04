package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.MedicationApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.domain.entity.net.request.DefaultRequestEntity;
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
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 *
 * @author zhaixiang
 * 用药相关
 */

public class MedicineRepository implements NetRepository.Medicine {
    private Context context;
    private MedicationApi api;


    public MedicineRepository(Context context) {
        this.context = context;
        this.api = new RetrofitFactory()
                .setUseToken(true)
                .setBaseUrl(HttpApi.API_BASE_URL)
                .build(context)
                .create(MedicationApi.class);
    }


    @Override
    public Observable<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>> getMedicineList(String userId,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, api.getMedicineList(userId).doOnError(new DoOnTokenErrorAction())
                , new DefaultRequestEntity(HttpApi.API_GET_ALL_MEDICINE),
                cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity<List<MedicationPlanEntity>>> getMedicationPlanList(String userId,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<MedicationPlanEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, api.getMedicationPlanList(userId).doOnError(new DoOnTokenErrorAction()),
                new DefaultRequestEntity(HttpApi.API_GET_MEDICATION_PLAN_LIST),
                cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity<List<MedicationPlanEntity>>> getCompletedMedicationPlanList(String userId,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<MedicationPlanEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, api.getCompletedMedicationPlanList(userId).doOnError(new DoOnTokenErrorAction()),
                new DefaultRequestEntity(HttpApi.API_GET_COMPLETED_MEDICATION_PLAN_LIST),
                cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity<MedicationPlanEntity>> addMedicationPlan(MedicationPlanEntity entity) {
        return api.addMedicationPlan(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<MedicationPlanEntity>> updateMedicationPlan(MedicationPlanEntity entity) {
        return api.updateMedicationPlan(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> stopMedicationPlan(SetMedicationPlanRequestEntity entity) {
        return api.stopMedicationPlan(entity.getUserId(),entity.getPlanId()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> deleteMedicationPlan(SetMedicationPlanRequestEntity entity) {
        return api.deleteMedicationPlan(entity.getUserId(),entity.getPlanId()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<MedicationRemindResponseEntity>> getMedicationRemindList(String userId,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<MedicationRemindResponseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, api.getMedicationRemindList(userId).doOnError(new DoOnTokenErrorAction()),
                new DefaultRequestEntity(HttpApi.API_GET_MEDICATION_REMIND_LIST),
                cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity> setMedicationRemind(SetMedicationRemindRequestEntity entity) {
        return api.setMedicationRemind(entity.getUserId(),entity.getPlanGuid(),entity.getRemindGuid(),entity.getState())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<CustomMedicineEntity>> addMedicine(MedicineAddEntity entity) {
        return api.addMedicine(entity);
    }

    @Override
    public Observable<NetResponseEntity> deleteMedicine(MedicineDeleteRequestEntity entity) {
        return api.deleteMedicine(entity.getGuid(),entity.getUserId());
    }

    @Override
    public Observable<NetResponseEntity<CustomMedicineEntity>> editMedicine(MedicineEditRequestEntity entity) {
        return api.editMedicine(entity);
    }

    @Override
    public Observable<NetResponseEntity<List<CustomMedicineEntity>>> getCustomMedicineList(GetCustomMedicineRequestEntity entity) {
        return api.getCustomMedicineList(entity.getUserId(),entity.getPage(),entity.getPageSize());
    }
}
