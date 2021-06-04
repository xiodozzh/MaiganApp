package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.response.GetAllUnreadMessageResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public interface UnreadMessageApi {
    @GET(HttpApi.API_GET_ALL_UNREAD_MESSAGE)
    Single<NetResponseEntity<GetAllUnreadMessageResponseEntity>> getAllUnreadMessage(@Query("accountGuid")String userId);

    @GET(HttpApi.API_MARK_HEALTH_KNOWLEDGE_READ)
    Single<NetResponseEntity> markHealthKnowledgeRead(@Query("accountGuid")String userId,@Query("healthKnowledgeGuid")String itemId);

    @GET(HttpApi.API_MARK_WEEKLY_REPORT_READ)
    Single<NetResponseEntity> markWeeklyReportRead(@Query("accountGuid")String userId,@Query("weekReportGuid")String itemId);
}
