package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;

import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.data.wrapper.MedicationPlanModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class MedicationPlanEditViewModel extends BaseViewModel {
    public SparseArray<List<MedicationPlanModel.DosageItem>> list = new SparseArray<>();
    public MutableLiveData<Boolean> headerUpdate = new MutableLiveData<>();
    public MutableLiveData<Boolean> listUpdate = new MutableLiveData<>();
    public boolean isCreate;
    public MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> stopSuccess = new MutableLiveData<>();
    public ObservableBoolean submitting = new ObservableBoolean(false);
    public ObservableField<String>title = new ObservableField<>("");
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private static final String DATE_FORMAT = "yyyy.MM.dd";
    private static final String DATE_FORMAT_NET = "yyyy-MM-dd";
    private String medicineName;
    private String medicineGuid;
    private boolean custom;
    private String medicineTimeRange;
    private String medicineCycleDays;
    private String planId;
    private MedicineUseCase useCase;
    public boolean isStopped;
    private MedicationPlanModel oldModel;

    public MedicationPlanEditViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getMedicineUseCase();
    }

    public void setCreateNew(boolean isCreateNew){
        isCreate = isCreateNew;
        title.set(isCreateNew?getApplication().getString(R.string.activity_medication_plan_add_plan):
                getApplication().getString(R.string.activity_medication_plan_edit_plan));
    }

    public void initEdit(MedicationPlanModel model) {
        oldModel = new MedicationPlanModel(model);
        startTime.setTimeInMillis(model.startTime);
        endTime.setTimeInMillis(model.endTime);
        medicineGuid = model.medicineId;
        medicineName = model.name;
        planId = model.planId;
        isStopped = model.stopped;
        custom = model.customMedicine;
        list.clear();
        for (int i = 0; i < model.dosageMap.size(); i++) {
            list.put(i, model.dosageMap.get(i));
        }
        medicineCycleDays = String.format(Locale.getDefault(),
                getApplication().getString(R.string.activity_medication_plan_cycle_days), list.size());
        setTimeRange(startTime, endTime);
        listUpdate.setValue(true);
    }

    public void initCreate() {
        endTime.add(Calendar.MONTH, 1);
        setTimeRange(startTime, endTime);
        initDosageList();
        listUpdate.setValue(true);
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicine(String medicineGuid, String medicineName,boolean custom) {
        this.medicineName = medicineName;
        this.medicineGuid = medicineGuid;
        this.custom = custom;
        headerUpdate.setValue(true);
    }

    public String getMedicineTimeRange() {
        return medicineTimeRange;
    }


    public String getMedicineCycleDays() {
        return medicineCycleDays;
    }


    private void initDosageList() {
        ArrayList<MedicationPlanModel.DosageItem> items = new ArrayList<>();
        list.put(0, items);
        medicineCycleDays = String.format(Locale.getDefault(),
                getApplication().getString(R.string.activity_medication_plan_cycle_days), list.size());
    }

    public void setList(SparseArray<List<MedicationPlanModel.DosageItem>> array) {
        this.list.clear();
        for (int i = 0; i < array.size(); i++) {
            list.put(i, array.get(i));
        }
        medicineCycleDays = String.format(Locale.getDefault(),
                getApplication().getString(R.string.activity_medication_plan_cycle_days), list.size());
        listUpdate.setValue(true);
    }

    public void setTimeRange(Calendar start, Calendar end) {
        startTime.setTimeInMillis(start.getTimeInMillis());
        endTime.setTimeInMillis(end.getTimeInMillis());
        medicineTimeRange = DateFormat.format(DATE_FORMAT, start) + "-" + DateFormat.format(DATE_FORMAT, end);
        headerUpdate.setValue(true);
    }

    public long getStartTimeInMillis() {
        return startTime.getTimeInMillis();
    }

    public long getEndTimeInMillis() {
        return endTime.getTimeInMillis();
    }

    public void submit() {
        if (TextUtils.isEmpty(medicineName) || TextUtils.isEmpty(medicineGuid)) {
            showToast(getApplication().getString(R.string.medication_name_cannot_empty));
            return;
        }
        int dosageNumber = 0;
        for (int i = 0; i < list.size(); i++) {
            List items = list.valueAt(i);
            dosageNumber += items.size();
        }
        if (dosageNumber == 0) {
            showToast(getApplication().getString(R.string.activity_medication_plan_set_dosage));
            return;
        }
        submitting.set(true);
        MedicationPlanEntity entity = new MedicationPlanEntity();
        entity.setStartDate(DateFormat.format(DATE_FORMAT_NET, startTime).toString());
        entity.setEndDate(DateFormat.format(DATE_FORMAT_NET, endTime).toString());
        entity.setDrugName(medicineName);
        entity.setRepeatPeriod(list.size());
        entity.setDrugGuid(medicineGuid);
        entity.setPlanGuid(planId);
        entity.setCustom(custom?1:0);
        Map<String, List<MedicationPlanEntity.DosageBean>> map = new HashMap<>(5);
        for (int i = 0; i < list.size(); i++) {
            List<MedicationPlanModel.DosageItem> dosageItems = list.valueAt(i);
            List<MedicationPlanEntity.DosageBean> beans = new ArrayList<>();
            for (MedicationPlanModel.DosageItem item : dosageItems) {
                MedicationPlanEntity.DosageBean bean = new MedicationPlanEntity.DosageBean();
                bean.setDosage(item.dosage);
                bean.setDosageUnitType(item.dosageUnitType);
                bean.setTime(item.time);
                beans.add(bean);
            }
            map.put(String.valueOf(i + 1), beans);
        }
        entity.setDosage(map);
        if (isCreate) {
            addPlan(entity);
        } else {
            updatePlan(entity);
        }
    }

    private void addPlan(MedicationPlanEntity entity) {
        submitting.set(true);
        useCase.addMedicationPlan(entity, new Subscriber<NetResponseEntity<MedicationPlanEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                submitting.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<MedicationPlanEntity> medicationPlanEntityNetResponseEntity) {
                Logger.i("onNext: " + medicationPlanEntityNetResponseEntity);
                if (medicationPlanEntityNetResponseEntity.getCode() == 0) {
                    saveSuccess.setValue(true);
                } else {
                    showToast(medicationPlanEntityNetResponseEntity.getMessage());
                }
                submitting.set(false);
            }
        });
    }

    private void updatePlan(MedicationPlanEntity entity) {
        MedicationPlanModel model = MedicationPlanModelWrapper.getModelFromMedicationPlanEntity(entity,isStopped);
        model.totalNumber = oldModel.totalNumber;
        model.forgetNumber = oldModel.forgetNumber;
        model.ignoreNumber = oldModel.ignoreNumber;
        model.completeNumber = oldModel.completeNumber;
        if (Objects.equals(model,oldModel)){
            // 没有改变直接返回
            submitting.set(false);
            saveSuccess.setValue(true);
            return;
        }
        submitting.set(true);
        useCase.updateMedicationPlan(entity, new Subscriber<NetResponseEntity<MedicationPlanEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                submitting.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<MedicationPlanEntity> medicationPlanEntityNetResponseEntity) {
                Logger.i( "onNext: " + medicationPlanEntityNetResponseEntity);
                if (medicationPlanEntityNetResponseEntity.getCode() == 0) {
                    saveSuccess.setValue(true);
                } else {
                    showToast(medicationPlanEntityNetResponseEntity.getMessage());
                }
                submitting.set(false);
            }
        });
    }

    public void stopPlan() {
        useCase.stopMedicationPlan(SaveUtils.getUserId(getApplication()), planId, new Subscriber<NetResponseEntity>() {
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
                    stopSuccess.setValue(true);
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
