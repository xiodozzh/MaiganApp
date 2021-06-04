package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.CheckServiceAuthorRequestEntity;
import com.mgtech.domain.entity.net.request.GetCompanyAuthorCodeRequestEntity;
import com.mgtech.domain.entity.net.request.SetCustomerContactPermissionRequestEntity;
import com.mgtech.domain.entity.net.request.UserIdRequestEntity;
import com.mgtech.domain.entity.net.response.BoughtServiceResponseEntity;
import com.mgtech.domain.entity.net.response.CheckServiceAuthorityResponseEntity;
import com.mgtech.domain.entity.net.response.GetAuthorizedCompanyResponseEntity;
import com.mgtech.domain.entity.net.response.GetCustomerContactPermissionResponseEntity;
import com.mgtech.domain.entity.net.response.GetFirstAidPhoneResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceAuthCodeResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ServiceCompanyResponseEntity;
import com.mgtech.domain.repository.NetRepository;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ServeUseCase {
    private NetRepository.Serve repository;

    public ServeUseCase(NetRepository.Serve repository) {
        this.repository = repository;
    }

    public void getService(String userId, Subscriber<NetResponseEntity<List<GetServiceResponseEntity>>> subscriber) {
        repository.getAllServices(new UserIdRequestEntity(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void checkServiceAuthor(String userId, String companyId,
                                   Subscriber<NetResponseEntity<CheckServiceAuthorityResponseEntity>> subscriber) {
        CheckServiceAuthorRequestEntity entity = new CheckServiceAuthorRequestEntity(userId, companyId);
        repository.checkCompanyAuthor(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCompanyAuthorCode(String userId, String companyId,
                                     Subscriber<NetResponseEntity<GetServiceAuthCodeResponseEntity>> subscriber) {
        GetCompanyAuthorCodeRequestEntity entity = new GetCompanyAuthorCodeRequestEntity(userId, companyId);
        repository.getCompanyAuthorCode(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCompanies(String userId, Subscriber<NetResponseEntity<List<ServiceCompanyResponseEntity>>> subscriber) {
        repository.getCompanies(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getBoughtServices(String userId, Subscriber<NetResponseEntity<List<BoughtServiceResponseEntity>>> subscriber) {
        repository.getBoughtServices(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFirstAidPhone(String userId, Subscriber<NetResponseEntity<GetFirstAidPhoneResponseEntity>> subscriber) {
        repository.getFirstAidPhone(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void setCustomerServiceContactPermission(SetCustomerContactPermissionRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        repository.setCustomerContactPermission(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCustomerServiceContactPermission(String userId, Subscriber<NetResponseEntity<GetCustomerContactPermissionResponseEntity>> subscriber) {
        repository.getCustomerContactPermission(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void cancelServiceAuthority(String userId, String companyId, Subscriber<NetResponseEntity> subscriber) {
        repository.cancelServiceAuth(userId, companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getAuthorizedCompanies(String userId, Subscriber<NetResponseEntity<List<GetAuthorizedCompanyResponseEntity>>> subscriber){
        repository.getAuthorizedCompanies(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
