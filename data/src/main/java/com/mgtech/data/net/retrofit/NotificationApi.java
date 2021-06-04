package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.SavePushIdRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Single;

public interface NotificationApi {
    @POST(HttpApi.API_SAVE_PUSH_ID)
    Single<NetResponseEntity> savePushId(@Body SavePushIdRequestEntity entity);
}
