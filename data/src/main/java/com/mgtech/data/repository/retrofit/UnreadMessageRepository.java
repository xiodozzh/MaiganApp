package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.data.net.retrofit.UnreadMessageApi;
import com.mgtech.domain.entity.net.request.GetUnreadMessageRequestEntity;
import com.mgtech.domain.entity.net.request.MarkHealthKnowledgeReadRequestEntity;
import com.mgtech.domain.entity.net.request.MarkWeeklyReportReadRequestEntity;
import com.mgtech.domain.entity.net.response.GetAllUnreadMessageResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.HttpApi;

import javax.inject.Inject;

import rx.Single;

public class UnreadMessageRepository implements NetRepository.Message {
    private UnreadMessageApi messageApi;

    @Inject
    public UnreadMessageRepository(Context context) {
        this.messageApi = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(UnreadMessageApi.class);
    }

    @Override
    public Single<NetResponseEntity<GetAllUnreadMessageResponseEntity>> getAllUnreadMessage(GetUnreadMessageRequestEntity entity) {
        return messageApi.getAllUnreadMessage(entity.getId());
    }

    @Override
    public Single<NetResponseEntity> markHealthKnowledgeRead(MarkHealthKnowledgeReadRequestEntity entity) {
        return messageApi.markHealthKnowledgeRead(entity.getUserId(),entity.getItemId());
    }

    @Override
    public Single<NetResponseEntity> markWeeklyReportRead(MarkWeeklyReportReadRequestEntity entity) {
        return messageApi.markWeeklyReportRead(entity.getUserId(),entity.getItemId());
    }
}
