package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import com.mgtech.domain.entity.net.response.CountryCodeEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ProvinceCodeEntity;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.data.wrapper.LocationModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class SetCountryAndProvinceViewModel extends BaseFragmentViewModel {
    private AppConfigUseCase useCase;
    public List<Object> locationModels = new ArrayList<>();
    public List<String> letters = new ArrayList<>();
    public ObservableBoolean reloadData = new ObservableBoolean(false);
    public ObservableBoolean loading = new ObservableBoolean(false);
    public ObservableBoolean netError = new ObservableBoolean(false);

    public SetCountryAndProvinceViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication)application).getAppConfigUseCase();
    }

    public void getCountries(){
        loading.set(true);
        useCase.getCountryCode(new Subscriber<NetResponseEntity<Map<String,List<CountryCodeEntity>>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                netError.set(true);
                letters.clear();
                loading.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<Map<String,List<CountryCodeEntity>>> netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    locationModels.clear();
                    letters.clear();
                    Map<String,List<CountryCodeEntity>> map = netResponseEntity.getData();
                    if (map != null) {
                        for (char i = 'A'; i <= 'Z'; i++) {
                            List<CountryCodeEntity> list = map.get(String.valueOf(i));
                            if (list != null && !list.isEmpty()){
                                locationModels.add(String.valueOf(i));
                                letters.add(String.valueOf(i));
                                locationModels.addAll(LocationModelWrapper.getModelListFromCountryEntity(list));
                            }
                        }
                    }
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                Logger.i("onNext: "+ locationModels);
                reloadData.set(!reloadData.get());
                netError.set(false);
                loading.set(false);
            }
        });
    }


    public void getProvinces(String code){
        loading.set(true);
        useCase.getProvinceCode(code,new Subscriber<NetResponseEntity<Map<String,List<ProvinceCodeEntity>>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                letters.clear();
                loading.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<Map<String,List<ProvinceCodeEntity>>> netResponseEntity) {
                Logger.i( "onNext: "+netResponseEntity);
                if (netResponseEntity.getCode() == 0){
                    locationModels.clear();
                    letters.clear();
                    Map<String,List<ProvinceCodeEntity>> map = netResponseEntity.getData();
                    if (map != null) {
                        for (char i = 'A'; i <= 'Z'; i++) {
                            List<ProvinceCodeEntity> list = map.get(String.valueOf(i));
                            if (list != null && !list.isEmpty()){
                                locationModels.add(String.valueOf(i));
                                letters.add(String.valueOf(i));
                                locationModels.addAll(LocationModelWrapper.getModelListFromProvinceEntity(list));
                            }
                        }
                    }
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                reloadData.set(!reloadData.get());
                netError.set(false);
                loading.set(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
    }
}
