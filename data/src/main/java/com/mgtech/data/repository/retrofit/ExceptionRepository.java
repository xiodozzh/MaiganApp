package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.ExceptionApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.DefaultRequestEntity;
import com.mgtech.domain.entity.net.request.DeleteExceptionEntity;
import com.mgtech.domain.entity.net.request.GetAllExceptionRequestEntity;
import com.mgtech.domain.entity.net.request.SendExceptionRequestEntity;
import com.mgtech.domain.entity.net.request.SetExceptionReadRequestEntity;
import com.mgtech.domain.entity.net.request.UserIdRequestEntity;
import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.SimpleResponseEntity;
import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.Single;

/**
 * 异常信息
 *
 * @author zhaixiang
 */

public class ExceptionRepository implements NetRepository.Abnormal {
    private Context context;
    private ExceptionApi netApi;

    public ExceptionRepository(Context context) {
        this.context = context;
        this.netApi = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context)
                .create(ExceptionApi.class);
    }

    @Override
    public Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getAllException(GetAllExceptionRequestEntity entity,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<ExceptionResponseEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.getAllException(entity.getId(),entity.getPage(),entity.getPageSize()).doOnError(new DoOnTokenErrorAction())
                , entity, cacheType,type);
    }

    @Override
    public Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getException(GetAllExceptionRequestEntity entity,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<ExceptionResponseEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.getExceptionById(entity.getId(),entity.getPage(),entity.getPageSize()).doOnError(new DoOnTokenErrorAction())
                , entity, cacheType,type);
    }

    @Override
    public Single<NetResponseEntity<List<ExceptionResponseEntity>>> getException(GetAllExceptionRequestEntity entity) {
        return netApi.getAlertRecordsById(entity.getId(),entity.getPage(),entity.getPageSize());
    }

    @Override
    public Observable<NetResponseEntity> markRead(SetExceptionReadRequestEntity entity) {
        return netApi.markRead(entity.getId(),entity.getTargetId(),entity.getExceptionId()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> deleteException(DeleteExceptionEntity entity) {
        return netApi.deleteException(entity.getId(),entity.getExceptionId()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<ThresholdRequestEntity>> setThreshold(ThresholdRequestEntity entity) {
        return netApi.setThreshold(entity).doOnError(new DoOnTokenErrorAction());
    }


    @Override
    public Observable<NetResponseEntity<ThresholdRequestEntity>> getThreshold(UserIdRequestEntity entity,int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<ExceptionResponseEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.getThreshold(entity.getId()).doOnError(new DoOnTokenErrorAction())
                , new DefaultRequestEntity(HttpApi.API_GET_EXCEPTION_THRESHOLD), cacheType,type);
    }


}
