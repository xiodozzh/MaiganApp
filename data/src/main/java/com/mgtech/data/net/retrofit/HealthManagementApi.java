package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.response.HealthKnowledgeResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.GET;
import rx.Single;

public interface HealthManagementApi {
    @GET(HttpApi.API_GET_KNOWLEDGE_LIST)
    Single<NetResponseEntity<List<HealthKnowledgeResponseEntity>>> getKnowledgeList();
}
