package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.data.wrapper.MedicationPlanModelWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class MedicationPlanViewModel extends BaseViewModel {
    public List<MedicationPlanModel> dataList;
    public ObservableBoolean getDataSuccess = new ObservableBoolean(false);
    public ObservableBoolean deleteSuccess = new ObservableBoolean(false);
    public ObservableBoolean loading = new ObservableBoolean(false);
    public ObservableBoolean isEmpty = new ObservableBoolean(true);
    private MedicineUseCase useCase;

    public MedicationPlanViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getMedicineUseCase();
        dataList = new ArrayList<>();
    }

    public void getMedicationPlan() {
        loading.set(true);
        useCase.getMedicationPlanList(SaveUtils.getUserId(getApplication()), new Subscriber<NetResponseEntity<List<MedicationPlanEntity>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                isEmpty.set(dataList.isEmpty());
                loading.set(false);
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<List<MedicationPlanEntity>> listNetResponseEntity) {
                if (listNetResponseEntity.getCode() == 0) {
                    List<MedicationPlanEntity> entities = listNetResponseEntity.getData();
                    if (entities != null ) {
                        dataList.clear();
                        for (MedicationPlanEntity entity : entities) {
                            dataList.add(MedicationPlanModelWrapper.getModelFromMedicationPlanEntity(entity, false));
                        }
                    }
                    getDataSuccess.set(!getDataSuccess.get());
                } else {
                    showToast(listNetResponseEntity.getMessage());
                }
                isEmpty.set(dataList.isEmpty());
                loading.set(false);
            }
        });
    }

    public void delete(final int index) {
        useCase.deleteMedicationPlan(SaveUtils.getUserId(getApplication()), dataList.get(index).planId,
                new Subscriber<NetResponseEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            dataList.remove(index);
                            deleteSuccess.set(!deleteSuccess.get());
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
    }
}
