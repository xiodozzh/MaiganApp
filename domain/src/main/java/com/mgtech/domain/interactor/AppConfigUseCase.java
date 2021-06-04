package com.mgtech.domain.interactor;

import android.util.Log;

import com.mgtech.domain.entity.net.request.GetAppConfigRequestEntity;
import com.mgtech.domain.entity.net.response.CountryCodeEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ProvinceCodeEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.MyConstant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import rx.Scheduler;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * App Config UseCase
 */

public class AppConfigUseCase {
    private NetRepository.AppConfig repository;
    private Subscription getAppConfigSubscription;
    private Subscription getFirmwareFileSubscription;
    private Subscription getCountryCodeSubscription;
    private Subscription getProvinceCodeSubscription;

    public AppConfigUseCase(NetRepository.AppConfig repository) {
        this.repository = repository;
    }

    public void getAppConfig(Subscriber<NetResponseEntity<GetAppConfigResponseEntity>> subscriber) {
        unSubscribeGetAppConfig();
        this.getAppConfigSubscription = this.repository.getAppConfig(new GetAppConfigRequestEntity(MyConstant.APP_KEY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAppConfigInService(Subscriber<NetResponseEntity<GetAppConfigResponseEntity>> subscriber){
        unSubscribeGetAppConfig();
        this.getAppConfigSubscription = this.repository.getAppConfig(new GetAppConfigRequestEntity(MyConstant.APP_KEY))
                .subscribe(subscriber);
    }

    public void getCountryCode(Subscriber<NetResponseEntity<Map<String, List<CountryCodeEntity>>>> subscriber) {
        unSubscribeCountryCode();
        getCountryCodeSubscription = repository.getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProvinceCode(String countryCode, Subscriber<NetResponseEntity<Map<String,List<ProvinceCodeEntity>>>> subscriber) {
        unSubscribeProvinceCode();
        getProvinceCodeSubscription = repository.getProvinceList(countryCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFirmwareFile(String url, Subscriber<byte[]> subscriber) {
        unSubscibeFirmwareFile();
        getFirmwareFileSubscription = repository.downloadFirmwareFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, byte[]>() {
                    @Override
                    public byte[] call(ResponseBody responseBody) {
                        byte[] firmwareContent = null;
                        if (responseBody != null) {
                            BufferedSource source = responseBody.source();
                            try {
                                firmwareContent = source.readByteArray();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return firmwareContent;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 解除subscriber
     */
    public void unSubscribe() {
        unSubscribeCountryCode();
        unSubscribeProvinceCode();
        unSubscribeGetAppConfig();
        unSubscibeFirmwareFile();
    }

    private void unSubscribeCountryCode() {
        if (getCountryCodeSubscription != null && !getCountryCodeSubscription.isUnsubscribed()) {
            getCountryCodeSubscription.unsubscribe();
        }
    }

    private void unSubscribeProvinceCode() {
        if (getProvinceCodeSubscription != null && !getProvinceCodeSubscription.isUnsubscribed()) {
            getProvinceCodeSubscription.unsubscribe();
        }
    }


    private void unSubscribeGetAppConfig() {
        if (getAppConfigSubscription != null && !getAppConfigSubscription.isUnsubscribed()) {
            getAppConfigSubscription.unsubscribe();
        }
    }

    private void unSubscibeFirmwareFile() {
        if (getFirmwareFileSubscription != null && !getFirmwareFileSubscription.isUnsubscribed()) {
            getFirmwareFileSubscription.unsubscribe();
        }
    }

}
