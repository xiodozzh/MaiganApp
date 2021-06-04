package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * @author zhaixiang
 */
public interface WeeklyReportApi {
    @GET(HttpApi.API_GET_WEEKLY_REPORT)
    Single<NetResponseEntity<List<WeeklyReportResponseEntity>>> getWeeklyReportList(@Query("accountGuid")String id,
                                                                                    @Query("pwDataType")int dataType);
}
