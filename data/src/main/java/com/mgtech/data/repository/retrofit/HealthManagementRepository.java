package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.HealthManagementApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.response.HealthKnowledgeResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import rx.Single;

public class HealthManagementRepository implements NetRepository.HealthManagement {
    private HealthManagementApi api;

    public HealthManagementRepository(Context context) {
        this.api = new RetrofitFactory()
                .setUseToken(true)
                .setBaseUrl(HttpApi.API_BASE_URL)
                .build(context)
                .create(HealthManagementApi.class);
    }

    @Override
    public Single<NetResponseEntity<List<HealthKnowledgeResponseEntity>>> getWeeklyReportList(int cacheType) {
        return api.getKnowledgeList().doOnError(new DoOnTokenErrorAction());
    }
}
