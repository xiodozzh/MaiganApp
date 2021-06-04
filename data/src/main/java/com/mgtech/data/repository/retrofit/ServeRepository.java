package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.data.net.retrofit.ServeApi;
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
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceResponseEntity;
import com.mgtech.domain.entity.net.response.ServiceCompanyResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import rx.Single;

/**
 * @author xiang
 */
public class ServeRepository implements NetRepository.Serve {
    private ServeApi api;

    public ServeRepository(Context context) {
        this.api = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(ServeApi.class);
    }

    @Override
    public Single<NetResponseEntity<List<GetServiceResponseEntity>>> getAllServices(UserIdRequestEntity entity) {
        return api.getService(entity.getId());
    }

    @Override
    public Single<NetResponseEntity<CheckServiceAuthorityResponseEntity>> checkCompanyAuthor(CheckServiceAuthorRequestEntity entity) {
        return api.checkCompanyAuthor(entity.getUserId(),entity.getCompanyId());
    }

    @Override
    public Single<NetResponseEntity<GetServiceAuthCodeResponseEntity>> getCompanyAuthorCode(GetCompanyAuthorCodeRequestEntity entity) {
        return api.getCompanyAuthorCode(entity.getUserId(),entity.getCompanyId());
    }

    @Override
    public Single<NetResponseEntity<List<ServiceCompanyResponseEntity>>> getCompanies(String userId) {
        return api.getCompanies(userId);
    }

    @Override
    public Single<NetResponseEntity<List<BoughtServiceResponseEntity>>> getBoughtServices(String userId) {
        return api.getBoughtServices(userId);
    }

    @Override
    public Single<NetResponseEntity<GetFirstAidPhoneResponseEntity>> getFirstAidPhone(String userId) {
        return api.getFirstAidPhoneNumber(userId);
    }

    @Override
    public Single<NetResponseEntity> setCustomerContactPermission(SetCustomerContactPermissionRequestEntity entity) {
        return api.setCustomerContactPermission(entity);
    }

    @Override
    public Single<NetResponseEntity<GetCustomerContactPermissionResponseEntity>> getCustomerContactPermission(String userId) {
        return api.getCustomerContactPermission(userId);
    }

    @Override
    public Single<NetResponseEntity> cancelServiceAuth(String userId, String companyId) {
        return api.cancelServiceAuth(userId,companyId);
    }

    @Override
    public Single<NetResponseEntity<List<GetAuthorizedCompanyResponseEntity>>> getAuthorizedCompanies(String userId) {
        return api.getAuthorizedCompanies(userId);
    }
}
