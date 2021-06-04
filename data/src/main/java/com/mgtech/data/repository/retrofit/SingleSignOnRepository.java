package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.data.net.retrofit.SingleSignOnApi;
import com.mgtech.domain.entity.net.request.CheckPhoneInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SaveMobileInfoRequestEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import rx.Observable;

/**
 *
 * @author zhaixiang
 * 单点登陆
 */

public class SingleSignOnRepository implements NetRepository.SingleSignOn {
    private SingleSignOnApi api;

    public SingleSignOnRepository(Context context) {
        this.api = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(SingleSignOnApi.class);
    }

    @Override
    public Observable<NetResponseEntity> savePhoneInfo(SaveMobileInfoRequestEntity entity) {
        return api.setPhoneInfo(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<CheckPhoneInfoResponseEntity>> checkPhoneInfo(CheckPhoneInfoRequestEntity entity) {
        return api.checkPhone(entity.getPhoneId(),entity.getId()).doOnError(new DoOnTokenErrorAction());
    }
}
