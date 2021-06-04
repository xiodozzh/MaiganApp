package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.data.net.retrofit.StepApi;
import com.mgtech.domain.entity.net.request.GetStepRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepStatisticsDataByTimeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepStatisticsDataRequestEntity;
import com.mgtech.domain.entity.net.request.SaveStepRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.domain.entity.net.response.StepStatisticsItemEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import rx.Observable;

/**
 * Created by zhaixiang on 2017/12/15.
 * 计步
 */

public class StepRepository implements NetRepository.Step {
    private Context context;
    private StepApi stepApi;

    public StepRepository(Context context) {
        this.context = context;
        this.stepApi = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(StepApi.class);
    }

    @Override
    public Observable<NetResponseEntity> saveStep(SaveStepRequestEntity entity) {
        return stepApi.saveStepData(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<List<StepDetailItemEntity>>> getStepDetail(int cacheType,
                                                                                   GetStepRequestEntity entity) {
        return stepApi.getStepDetail(entity.getId(), entity.getStartTime(), entity.getEndTime(), entity.getType())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStepStatisticsInfo(
            int cacheType,
            GetStepStatisticsDataRequestEntity entity) {
        return stepApi.getStatisticsStep(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStepStatisticsInfoByTime(
            int cacheType,
            GetStepStatisticsDataByTimeRequestEntity entity) {
        return stepApi.getStatisticsStep(entity).doOnError(new DoOnTokenErrorAction());
    }
}
