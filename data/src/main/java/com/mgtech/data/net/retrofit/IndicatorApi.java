package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.SaveRawDataRequestEntity;
import com.mgtech.domain.entity.net.request.SetPwMarkRequestEntity;
import com.mgtech.domain.entity.net.response.GetHealthManagementResponseEntity;
import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.entity.net.response.PwStatisticDataResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zhaixiang
 */
public interface IndicatorApi {
    /**
     * 获取参数说明
     *
     * @return 说明列表
     */
    @GET(HttpApi.API_GET_INDICATOR_PARAMETER_DESCRIPTION)
    Observable<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>> getIndicatorDescription();

    @POST(HttpApi.API_SAVE_RAW_DATA)
    Observable<NetResponseEntity<PwDataResponseEntity>> calculateData(@Body SaveRawDataRequestEntity entity);

//    @POST(HttpApi.API_SAVE_RAW_DATA)
//    Observable<NetResponseEntity<PwDataResponseEntity>> calculateAutoData(@Body SaveRawDataRequestEntity entity);

    @GET(HttpApi.API_GET_PW_DATA_BY_ID)
    Observable<NetResponseEntity<PwDataResponseEntity>> getDataById(@Query("measureGuid") String guid,@Query("accountGuid")String userId);

    @GET(HttpApi.API_GET_LAST_PW_DATA)
    Observable<NetResponseEntity<PwDataResponseEntity>> getLastData(@Query("accountGuid") String userId);

    @GET(HttpApi.API_GET_STATISTIC_PW_BY_DATE)
    Observable<NetResponseEntity<PwStatisticDataResponseEntity>> getStatisticPwByDate(@Query("accountGuid") String userId,
                                                                                      @Query("date") long date,
                                                                                      @Query("items") int[] items,
                                                                                      @Query("pwDataType") int dataType);

    @GET(HttpApi.API_GET_PW_BY_DATE_RANGE)
    Observable<NetResponseEntity<List<PwDataResponseEntity>>> getPwByDateRange(@Query("accountGuid") String userId,
                                                                               @Query("startDate") long start,
                                                                               @Query("endDate") long end,
                                                                               @Query("pwDataType") int dataType);

    @GET(HttpApi.API_GET_STATISTIC_PW_BY_DATE_RANGE)
    Observable<NetResponseEntity<PwStatisticDataResponseEntity>> getStatisticPwByDateRange(@Query("accountGuid") String userId,
                                                                                           @Query("startDate") long start,
                                                                                           @Query("endDate") long end,
                                                                                           @Query("items") int[] items,
                                                                                           @Query("pwDataType") int dataType);

    @GET(HttpApi.API_DELETE_PW)
    Observable<NetResponseEntity> deletePw(@Query("measureGuid") String id,@Query("accountGuid") String userId);

    @GET(HttpApi.API_GET_HEALTH_MANAGEMENT_DATA)
    Observable<NetResponseEntity<GetHealthManagementResponseEntity>> getHealthManagementData(@Query("accountGuid") String id,
                                                                                             @Query("startDate") long startDate,
                                                                                             @Query("endDate") long endDate, @Query("items") int[] items,
                                                                                             @Query("pwDataType") int dataType);

    @GET(HttpApi.API_GET_HOME_PAGE_DATA)
    Observable<NetResponseEntity<GetHomeDataResponseEntity>> getHomePageData(@Query("accountGuid") String id,
                                                                             @Query("pwDataType") int pwDataType);

    @POST(HttpApi.API_PW_REMARK)
    Observable<NetResponseEntity> setPwMark(@Body SetPwMarkRequestEntity entity);
}
