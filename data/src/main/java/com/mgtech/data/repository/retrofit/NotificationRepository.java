package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.NotificationApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.SavePushIdRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import rx.Single;

/**
 * @author zhaixiang
 */
public class NotificationRepository implements NetRepository.Notification {
    private NotificationApi api;

    public NotificationRepository(Context context) {
        this.api = new RetrofitFactory()
                .setUseToken(true)
                .setBaseUrl(HttpApi.API_BASE_URL)
                .build(context)
                .create(NotificationApi.class);
    }


    @Override
    public Single<NetResponseEntity> savePushId(SavePushIdRequestEntity entity) {
        return api.savePushId(entity).doOnError(new DoOnTokenErrorAction());
    }
}
