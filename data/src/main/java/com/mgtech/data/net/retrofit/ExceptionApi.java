package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface ExceptionApi {
    @GET(HttpApi.API_GET_ALL_EXCEPTIONS)
    Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getAllException(@Query("accountGuid") String id
            , @Query("page") int page, @Query("pageSize") int pageSize);


    @GET(HttpApi.API_GET_EXCEPTIONS_BY_ID)
    Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getExceptionById(@Query("accountGuid") String id
            , @Query("page") int page, @Query("pageSize") int pageSize);

    @GET(HttpApi.API_GET_EXCEPTIONS_BY_ID)
    Single<NetResponseEntity<List<ExceptionResponseEntity>>> getAlertRecordsById(@Query("accountGuid") String id
            , @Query("page") int page, @Query("pageSize") int pageSize);

    @GET(HttpApi.API_MARK_EXCEPTION_READ)
    Observable<NetResponseEntity> markRead(@Query("accountGuid") String id, @Query("targetAccountGuid") String targetId
            , @Query("abnormalNoticeGuid") String noticeId);

    @GET(HttpApi.API_DELETE_EXCEPTION)
    Observable<NetResponseEntity> deleteException(@Query("accountGuid") String id
            , @Query("abnormalNoticeGuid") String noticeId);

    @POST(HttpApi.API_SET_EXCEPTION_THRESHOLD)
    Observable<NetResponseEntity<ThresholdRequestEntity>> setThreshold(@Body ThresholdRequestEntity entity);

    @GET(HttpApi.API_GET_EXCEPTION_THRESHOLD)
    Observable<NetResponseEntity<ThresholdRequestEntity>> getThreshold(@Query("accountGuid") String id);
}
