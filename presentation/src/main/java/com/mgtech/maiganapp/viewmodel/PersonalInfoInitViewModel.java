package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import android.text.format.DateFormat;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.DiseaseModel;
import com.mgtech.maiganapp.data.model.PersonalInfoModel;
import com.mgtech.maiganapp.data.wrapper.DiseaseModelWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 * 设置基本信息 view-model
 */

public class PersonalInfoInitViewModel extends BaseFragmentViewModel {
    private PersonalInfoUseCase useCase;
//    public ObservableInt page = new ObservableInt(0);
    public MutableLiveData<Integer> page = new MutableLiveData<>();
    public ObservableBoolean saveSuccess = new ObservableBoolean(false);
    public ObservableBoolean updatePage0 = new ObservableBoolean(false);
    public ObservableBoolean enableStep0 = new ObservableBoolean(false);
    public ObservableBoolean enableStep1 = new ObservableBoolean(false);
    public ObservableBoolean loadingDiseases = new ObservableBoolean(false);
    public ObservableBoolean loadDiseasesSuccess = new ObservableBoolean(false);

    private Calendar birthCalendar;
    public ObservableField<String> birthString = new ObservableField<>("");
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> heightString = new ObservableField<>("");
    public ObservableField<String> weightString = new ObservableField<>("");
    public List<String> heightList = new ArrayList<>();
    public List<String> weightList = new ArrayList<>();

    public List<DiseaseModel> highBpType = new ArrayList<>();
    public List<DiseaseModel> diseaseType = new ArrayList<>();
    public List<DiseaseModel> organType = new ArrayList<>();
    public List<DiseaseModel> otherType = new ArrayList<>();

    public List<DiseaseModel> diseaseHistoryList = new ArrayList<>();
    public List<DiseaseModel> organDamageList = new ArrayList<>();
    public List<DiseaseModel> highBloodPressureList = new ArrayList<>();
    public List<DiseaseModel> otherRiskFactorsList = new ArrayList<>();

    public PersonalInfoModel model;
    private String userId;

    public PersonalInfoInitViewModel(Application application) {
        super(application);
        this.birthCalendar = Calendar.getInstance();
        this.birthCalendar.set(1980,6,16);
        this.useCase = ((MyApplication) application).getPersonInfoUseCase();
        this.model = new PersonalInfoModel();
        this.page.setValue(0);
        initHeightWeightList();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private void initHeightWeightList() {
        heightList.clear();
        weightList.clear();
        for (int i = MyConstant.HEIGHT_MIN; i <= MyConstant.HEIGHT_MAX; i++) {
            heightList.add(i + getApplication().getString(R.string.unit_height));
        }
        for (int i = MyConstant.WEIGHT_MIN; i <= MyConstant.WEIGHT_MAX; i++) {
            weightList.add(i + getApplication().getString(R.string.unit_weight));
        }
    }

    public void setCalendar(Date date) {
        birthCalendar.setTime(date);
        birthString.set(DateFormat.format(MyConstant.DATE_FORMAT_BIRTHDAY, birthCalendar).toString());
    }

    public Calendar getCalendar() {
        return birthCalendar;
    }


    public boolean isMale() {
        return model.gender == MyConstant.MAN;
    }

    public void setMale(boolean male) {
        this.model.gender = male? MyConstant.MAN:MyConstant.WOMEN;
    }

    public void setHeight(float h) {
        this.model.height = h;
        this.heightString.set(String.valueOf((int)h));
    }

    public void setWeight(float w) {
        this.model.weight = w;
        this.weightString.set(String.format(Locale.getDefault(), "%.1f",
                model.weight));
    }

    public int getHeight() {
        return model.height == 0 ? MyConstant.HEIGHT_DEFAULT : (int) model.height;
    }

    public int getWeight() {
        return model.weight == 0 ? MyConstant.WEIGHT_DEFAULT : (int) model.weight;
    }

    public void savePersonalInfo() {
        PersonalInfoEntity entity = new PersonalInfoEntity();
        entity.setAccountGuid(SaveUtils.getTempUserId(getApplication()));
        entity.setDisplayName(name.get());
        entity.setSex(model.gender);
        entity.setBirthday(birthString.get());
        entity.setHeight(model.height);
        entity.setWeight(model.weight);

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
                if (personalInfoEntityNetResponseEntity.getCode() == 0) {
                    saveSuccess.set(!saveSuccess.get());
                } else {
                    showToast(personalInfoEntityNetResponseEntity.getMessage());
                }
            }
        });
    }

    public void getDiseases() {
        loadingDiseases.set(true);
        useCase.getDiseaseList(new Subscriber<NetResponseEntity<AllDiseaseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loadingDiseases.set(true);

            }

            @Override
            public void onNext(NetResponseEntity<AllDiseaseEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    AllDiseaseEntity entity = netResponseEntity.getData();
                    if (entity != null) {
                        diseaseHistoryList.clear();
                        diseaseHistoryList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getDiseaseHistory()));
                        organDamageList.clear();
                        organDamageList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getOrganDamage()));
                        highBloodPressureList.clear();
                        highBloodPressureList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getHighBloodPressure()));
                        otherRiskFactorsList.clear();
                        otherRiskFactorsList.addAll(DiseaseModelWrapper.getListFromDiseaseBeans(entity.getOtherRiskFactors()));
                        loadDiseasesSuccess.set(true);
                    }
                }
                loadingDiseases.set(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.useCase.unSubscribe();
    }

}
