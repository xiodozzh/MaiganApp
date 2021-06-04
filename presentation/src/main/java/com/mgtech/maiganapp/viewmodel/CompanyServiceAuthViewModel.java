package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.response.GetServiceAuthCodeResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.ServeUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.CompanyModel;

import rx.Subscriber;

/**
 * @author Jesse
 */
public class CompanyServiceAuthViewModel extends BaseViewModel {
    private ServeUseCase serveUseCase;
    public MutableLiveData<String> pageUrl = new MutableLiveData<>();


    public CompanyServiceAuthViewModel(@NonNull Application application) {
        super(application);
        serveUseCase = ((MyApplication)application).getServeUseCase();
    }

    public void getAuthorCode(CompanyModel company) {
        serveUseCase.getCompanyAuthorCode(SaveUtils.getUserId(), company.id,
                new Subscriber<NetResponseEntity<GetServiceAuthCodeResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<GetServiceAuthCodeResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            GetServiceAuthCodeResponseEntity getServiceAuthCodeResponseEntity = netResponseEntity.getData();
                            if (getServiceAuthCodeResponseEntity != null) {
                                SaveUtils.saveServiceAuthCode(getServiceAuthCodeResponseEntity.getAuthCode());
                                jump(company, getServiceAuthCodeResponseEntity.getAuthCode());
                            } else {
                                showToast(getApplication().getString(R.string.network_error));
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }

    private void jump(CompanyModel company, String code) {
        String param;
        if (company.associatedServiceModel == null) {
            param = "?phone=" + SaveUtils.getPhone(getApplication()) + "&zone=" + SaveUtils.getZone(getApplication()) + "&code=" + code;
        } else {
            param = "?phone=" + SaveUtils.getPhone(getApplication()) + "&zone=" + SaveUtils.getZone(getApplication()) + "&code=" + code + "&serviceCode=" + company.associatedServiceModel.serviceCode;
        }
        pageUrl.setValue(company.pageUrl + param);
    }
}
