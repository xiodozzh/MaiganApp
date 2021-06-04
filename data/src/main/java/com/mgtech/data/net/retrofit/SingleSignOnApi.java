package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.SaveMobileInfoRequestEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zhaixiang
 */
public interface SingleSignOnApi {
    @POST(HttpApi.API_SAVE_PHONE_INFO)
    Observable<NetResponseEntity> setPhoneInfo(@Body SaveMobileInfoRequestEntity entity);

    @GET(HttpApi.API_CHECK_PHONE)
    Observable<NetResponseEntity<CheckPhoneInfoResponseEntity>>checkPhone(@Query("deviceId") String mac,@Query("accountGuid")String id);
}
