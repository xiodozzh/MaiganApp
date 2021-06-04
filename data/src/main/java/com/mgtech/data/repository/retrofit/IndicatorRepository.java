package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.IndicatorApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.DefaultRequestEntity;
import com.mgtech.domain.entity.net.request.DeletePwRequestEntity;
import com.mgtech.domain.entity.net.request.GetHealthManagementDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetHomeDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetPwByDateRangeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStatisticPwByDateRangeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStatisticPwByDateRequestEntity;
import com.mgtech.domain.entity.net.request.SaveRawDataRequestEntity;
import com.mgtech.domain.entity.net.request.SetPwMarkRequestEntity;
import com.mgtech.domain.entity.net.response.GetHealthManagementResponseEntity;
import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.entity.net.response.PwStatisticDataResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;

/**
 * 获取计算数据
 *
 * @author zhaixiang
 */

public class IndicatorRepository implements NetRepository.Data {
    private Context context;
    private IndicatorApi netApi;

    public IndicatorRepository(Context context) {
        this.context = context;
        this.netApi = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(IndicatorApi.class);
    }

    @Override
    public Observable<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>> getIndicatorDescription(int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.getIndicatorDescription().doOnError(new DoOnTokenErrorAction()),
                new DefaultRequestEntity(HttpApi.API_GET_INDICATOR_PARAMETER_DESCRIPTION), cacheType,type);

    }

    @Override
    public Observable<NetResponseEntity<PwDataResponseEntity>> calculatePwData(SaveRawDataRequestEntity entity) {
        return netApi.calculateData(entity);
    }

    @Override
    public Observable<NetResponseEntity<PwDataResponseEntity>> calculateAutoData(SaveRawDataRequestEntity entity) {
        return netApi.calculateData(entity);
    }

    @Override
    public Observable<NetResponseEntity<PwDataResponseEntity>> getLastData(String userId,int cacheType) {
        return netApi.getLastData(userId);
    }

    @Override
    public Observable<NetResponseEntity<PwStatisticDataResponseEntity>> getStatisticPwByDate(GetStatisticPwByDateRequestEntity entity,
                                                                                             int cacheType) {
        return netApi.getStatisticPwByDate(entity.getUserId(),entity.getDate(),entity.getItems(),entity.getDataType())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<List<PwDataResponseEntity>>> getPwByDateRange(GetPwByDateRangeRequestEntity entity,
                                                                                      int cacheType) {
        return netApi.getPwByDateRange(entity.getUserId(),entity.getStartDate(),entity.getEndDate(),entity.getType())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<PwStatisticDataResponseEntity>> getStatisticPwByDateRange(
            GetStatisticPwByDateRangeRequestEntity entity, int cacheType) {
        return netApi.getStatisticPwByDateRange(entity.getUserId(),entity.getStartDate(),entity.getEndDate(),
                entity.getItems(),entity.getType())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> deletePw(DeletePwRequestEntity entity) {
        return netApi.deletePw(entity.getMeasureGuid(),entity.getUserId()).doOnError(new DoOnTokenErrorAction());
    }


    @Override
    public Observable<NetResponseEntity<GetHomeDataResponseEntity>> getHomePageData(int cacheType, GetHomeDataRequestEntity entity) {
        Type type = new TypeToken<NetResponseEntity<GetHomeDataResponseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.getHomePageData(entity.getId(),entity.getPwDataType())
                .doOnError(new DoOnTokenErrorAction()), entity, cacheType,type);
    }

    @Override
    public Observable<NetResponseEntity<GetHealthManagementResponseEntity>> getHealthManagementData(int cache
            , GetHealthManagementDataRequestEntity entity) {
        return netApi.getHealthManagementData(entity.getId(),entity.getStartDate(),entity.getEndDate(),
                entity.getItems(),entity.getDataType())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> setPwRemark(SetPwMarkRequestEntity entity) {
        return netApi.setPwMark(entity);
    }

    @Override
    public Observable<NetResponseEntity<PwDataResponseEntity>> getDataById(String measureId,String userId) {
        return netApi.getDataById(measureId,userId);
    }
}
