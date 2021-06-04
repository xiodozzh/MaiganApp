package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.PersonalInformationApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.DefaultRequestEntity;
import com.mgtech.domain.entity.net.request.GetUserInfoRequestEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * @author zhaixiang
 * 个人信息
 */

public class PersonalInfoRepository implements NetRepository.PersonalInformation {
    private Context context;
    private PersonalInformationApi apiToken;

    public PersonalInfoRepository(Context context) {
        this.context = context;
        this.apiToken = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context)
                .create(PersonalInformationApi.class);
    }

    @Override
    public Observable<NetResponseEntity<PersonalInfoEntity>> setInfo(PersonalInfoEntity entity) {
        return apiToken.setInfo(entity).doOnError(new DoOnTokenErrorAction());
    }


    @Override
    public Observable<NetResponseEntity<PersonalInfoEntity>> getInfo(GetUserInfoRequestEntity entity, int useCache) {
        Type type = new TypeToken<NetResponseEntity<PersonalInfoEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, apiToken.getInfo(entity.getId()).doOnError(new DoOnTokenErrorAction()), entity, useCache, type);
    }

    @Override
    public Observable<NetResponseEntity<AllDiseaseEntity>> getDiseaseList() {
        Type type = new TypeToken<NetResponseEntity<AllDiseaseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, apiToken.getDiseaseList().doOnError(new DoOnTokenErrorAction()), new DefaultRequestEntity(HttpApi.API_GET_ALL_DISEASE_INFO),
                NetConstant.CACHE_IF_NET_ERROR, type);
    }

}
