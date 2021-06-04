package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.GetStepStatisticsDataByTimeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepStatisticsDataRequestEntity;
import com.mgtech.domain.entity.net.request.SaveStepRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.domain.entity.net.response.StepStatisticsItemEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface StepApi {
    @POST(HttpApi.API_SAVE_STEP_DATA)
    Observable<NetResponseEntity> saveStepData(@Body SaveStepRequestEntity entity);

    @GET(HttpApi.API_GET_STEP_DETAIL_BY_DATE)
    Observable<NetResponseEntity<List<StepDetailItemEntity>>> getStepDetail(@Query("accountGuid") String id,@Query("startTime")long startTime,
                                                                            @Query("endTime")long endTime,@Query("type")int type);

    @POST(HttpApi.API_GET_STATISTICS_STEP_INFO)
    Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStatisticsStep(@Body GetStepStatisticsDataRequestEntity entity);

    @POST(HttpApi.API_GET_STATISTICS_STEP_BY_TIME)
    Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStatisticsStep(@Body GetStepStatisticsDataByTimeRequestEntity entity);
}
