package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
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
public interface PersonalInformationApi {

    /**
     * 修改用户个人信息
     *
     * @param entity 个人信息
     * @return 结果
     */
    @POST(HttpApi.API_SET_PERSONAL_INFO_URL)
    Observable<NetResponseEntity<PersonalInfoEntity>> setInfo(@Body PersonalInfoEntity entity);

    /**
     * 获取用户个人信息
     * @param id 获取人的id
     * @return 用户个人信息
     */
    @GET(HttpApi.API_GET_PERSONAL_INFO_URL)
    Observable<NetResponseEntity<PersonalInfoEntity>> getInfo(@Query("targetAccountGuid") String id);


    @GET(HttpApi.API_GET_ALL_DISEASE_INFO)
    Observable<NetResponseEntity<AllDiseaseEntity>> getDiseaseList();
}
