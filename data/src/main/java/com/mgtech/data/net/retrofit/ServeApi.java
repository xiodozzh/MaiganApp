package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.SetCustomerContactPermissionRequestEntity;
import com.mgtech.domain.entity.net.response.BoughtServiceResponseEntity;
import com.mgtech.domain.entity.net.response.CheckServiceAuthorityResponseEntity;
import com.mgtech.domain.entity.net.response.GetAuthorizedCompanyResponseEntity;
import com.mgtech.domain.entity.net.response.GetCustomerContactPermissionResponseEntity;
import com.mgtech.domain.entity.net.response.GetFirstAidPhoneResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceAuthCodeResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ServiceCompanyResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Single;

/**
 * @author Jesse
 */
public interface ServeApi {
    @GET(HttpApi.API_GET_SERVICE_LIST)
    Single<NetResponseEntity<List<GetServiceResponseEntity>>> getService(@Query("accountGuid") String id);

    @GET(HttpApi.API_CHECK_SERVICE_AUTHOR)
    Single<NetResponseEntity<CheckServiceAuthorityResponseEntity>> checkCompanyAuthor(@Query("accountGuid") String id,
                                                                                      @Query("companyGuid") String companyId);

    @GET(HttpApi.API_GET_SERVICE_AUTHOR_CODE)
    Single<NetResponseEntity<GetServiceAuthCodeResponseEntity>> getCompanyAuthorCode(@Query("accountGuid") String id,
                                                                                     @Query("companyGuid") String companyId);
    @GET(HttpApi.API_GET_COMPANY_LIST)
    Single<NetResponseEntity<List<ServiceCompanyResponseEntity>>> getCompanies(@Query("accountGuid") String id);

    @GET(HttpApi.API_GET_BOUGHT_SERVICE_LIST)
    Single<NetResponseEntity<List<BoughtServiceResponseEntity>>> getBoughtServices(@Query("accountGuid") String id);

    @GET(HttpApi.API_GET_FIRST_AID_PHONE)
    Single<NetResponseEntity<GetFirstAidPhoneResponseEntity>> getFirstAidPhoneNumber(@Query("accountGuid") String id);

    @POST(HttpApi.API_SET_CUSTOMER_CONTACT_PERMISSION)
    Single<NetResponseEntity> setCustomerContactPermission(@Body SetCustomerContactPermissionRequestEntity entity);

    @GET(HttpApi.API_GET_CUSTOMER_CONTACT_PERMISSION)
    Single<NetResponseEntity<GetCustomerContactPermissionResponseEntity>> getCustomerContactPermission(@Query("accountGuid") String id);

    @GET(HttpApi.API_CANCEL_SERVICE_AUTH)
    Single<NetResponseEntity> cancelServiceAuth(@Query("accountGuid") String id,@Query("companyGuid")String companyId);

    @GET(HttpApi.API_GET_AUTHORIZED_COMPANIES)
    Single<NetResponseEntity<List<GetAuthorizedCompanyResponseEntity>>> getAuthorizedCompanies(@Query("accountGuid") String id);
}
