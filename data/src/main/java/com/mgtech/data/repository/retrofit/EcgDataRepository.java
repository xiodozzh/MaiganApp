package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.EcgDataApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.DeleteEcgRequestEntity;
import com.mgtech.domain.entity.net.request.GetEcgListRequestEntity;
import com.mgtech.domain.entity.net.request.GetEcgRequestEntity;
import com.mgtech.domain.entity.net.request.SaveEcgRequestEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;

/**
 * @author zhaixiang
 * ecg repository
 */

public class EcgDataRepository implements NetRepository.Ecg {
    private Context context;
    private EcgDataApi netApi;

    public EcgDataRepository(Context context) {
        this.context = context;
        this.netApi = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context)
                .create(EcgDataApi.class);
    }

    @Override
    public Observable<NetResponseEntity<EcgResponseEntity>> saveEcg(SaveEcgRequestEntity entity) {
        Type type = new TypeToken<NetResponseEntity<EcgResponseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.saveEcg(entity), entity, NetConstant.NO_CACHE, type);
    }

    @Override
    public Observable<NetResponseEntity<List<EcgResponseEntity>>> getEcgList(GetEcgListRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<EcgResponseEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context,
                netApi.getEcgList(entity.getStartTime(),entity.getEndTime(),entity.getUserId(),entity.getDataLength()).doOnError(new DoOnTokenErrorAction()),
                entity, cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity<EcgResponseEntity>> getEcgById(GetEcgRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<EcgResponseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.getEcgById(entity.getMeasureGuid(),entity.getUserId()).doOnError(new DoOnTokenErrorAction()), entity, cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity> deleteEcg(DeleteEcgRequestEntity entity) {
        Type type = new TypeToken<NetResponseEntity>() {
        }.getType();
        return DataStoreFactory.strategy(context, netApi.deleteEcg(entity.getMeasureGuid()).doOnError(new DoOnTokenErrorAction()), entity, NetConstant.NO_CACHE, type);
    }
}
