package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.DiseaseModel;
import com.mgtech.maiganapp.data.model.PersonalInfoModel;
import com.mgtech.maiganapp.data.wrapper.DiseaseModelWrapper;
import com.mgtech.maiganapp.data.wrapper.PersonalInfoModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class PersonalInfoDiseaseViewModel extends BaseViewModel {
    private PersonalInfoUseCase useCase;

    public List<DiseaseModel> highBpType = new ArrayList<>();
    public List<DiseaseModel> diseaseType = new ArrayList<>();
    public List<DiseaseModel> organType = new ArrayList<>();
    public List<DiseaseModel> otherType = new ArrayList<>();

    public List<DiseaseModel> diseaseHistoryList = new ArrayList<>();
    public List<DiseaseModel> organDamageList = new ArrayList<>();
    public List<DiseaseModel> highBloodPressureList = new ArrayList<>();
    public List<DiseaseModel> otherRiskFactorsList = new ArrayList<>();

    public MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadSuccess = new MutableLiveData<>();
    public PersonalInfoModel model;
    private Subscription getSubscription;


    public PersonalInfoDiseaseViewModel(@NonNull Application application) {
        super(application);
        this.useCase = ((MyApplication) application).getPersonInfoUseCase();
    }

    public void getInfo() {
        loading.setValue(true);

        unSubscribeGetInfo();
        getSubscription = Observable.zip(useCase.getPersonalInfoObservable(SaveUtils.getUserId(getApplication())),
                useCase.getAllDiseaseObservable(), new Func2<NetResponseEntity<PersonalInfoEntity>,
                        NetResponseEntity<AllDiseaseEntity>, Boolean>() {
                    @Override
                    public Boolean call(NetResponseEntity<PersonalInfoEntity> netResponseEntity,
                                        NetResponseEntity<AllDiseaseEntity> netResponseEntity2) {
                        boolean success = true;
                        if (netResponseEntity.getCode() == 0) {
                            AllDiseaseEntity entity = netResponseEntity2.getData();
                            if (entity != null) {
                                diseaseHistoryList.clear();
                                diseaseHistoryList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getDiseaseHistory()));
                                organDamageList.clear();
                                organDamageList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getOrganDamage()));
                                highBloodPressureList.clear();
                                highBloodPressureList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getHighBloodPressure()));
                                otherRiskFactorsList.clear();
                                otherRiskFactorsList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getOtherRiskFactors()));
                            } else {
                                success = false;
                            }
                        } else {
                            success = false;
                        }
                        if (netResponseEntity.getCode() == 0) {
                            PersonalInfoEntity entity = netResponseEntity.getData();
                            if (entity != null) {
                                model = PersonalInfoModelWrapper.getModelFromNet(entity);
                                highBpType.clear();
                                highBpType.addAll(model.highBloodPressure);
                                organType.clear();
                                organType.addAll(model.organDamage);
                                diseaseType.clear();
                                diseaseType.addAll(model.diseaseHistory);
                                otherType.clear();
                                otherType.addAll(model.otherRiskFactors);
                            } else {
                                success = false;
                            }
                        } else {
                            success = false;
                        }
                        return success;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loadSuccess.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            loadSuccess.setValue(true);
                        } else {
                            loadSuccess.setValue(false);
                        }
                        loading.setValue(false);
                    }
                });
    }

    public void savePersonalInfo() {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setOrganDamage(DiseaseModelWrapper.transferListToBeans(organType));
        entity.setHighBloodPressure(DiseaseModelWrapper.transferListToBeans(highBpType));
        entity.setDiseaseHistory(DiseaseModelWrapper.transferListToBeans(diseaseType));
        entity.setOther(DiseaseModelWrapper.transferListToBeans(otherType));
        useCase.setInfo(entity, new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<PersonalInfoEntity> personalInfoEntityNetResponseEntity) {
                Logger.i("onNext: " + personalInfoEntityNetResponseEntity);
                if (personalInfoEntityNetResponseEntity.getCode() == 0) {
                    saveSuccess.setValue(true);
                } else {
                    showToast(personalInfoEntityNetResponseEntity.getMessage());
                }
            }
        });
    }

    public boolean isDirtyData() {
        return isDirty(model.highBloodPressure, highBpType) || isDirty(model.diseaseHistory, diseaseType) ||
                isDirty(model.organDamage, organType) || isDirty(model.otherRiskFactors, otherType);
    }

    private boolean isDirty(List<DiseaseModel> one, List<DiseaseModel> other) {
        int oneSize = 0;
        int otherSize = 0;
        if (one != null) {
            oneSize = one.size();
        }
        if (other != null) {
            otherSize = other.size();
        }
        if (oneSize != otherSize) {
            return true;
        }
        if (oneSize == 0) {
            return false;
        }
        for (DiseaseModel diseaseModel : one) {
            if (!other.contains(diseaseModel)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        unSubscribeGetInfo();
    }

    private void unSubscribeGetInfo() {
        if (getSubscription != null && !getSubscription.isUnsubscribed()) {
            getSubscription.unsubscribe();
        }
    }
}
