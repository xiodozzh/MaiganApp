package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.response.CountryCodeEntity;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ProvinceCodeEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

/**
 * @author zhaixiang
 */
public interface AppConfigApi {
    /**
     * 获取APP 配置信息
     *
     * @return 配置信息
     */
    @GET(HttpApi.API_GET_APP_CONFIG)
    Observable<NetResponseEntity<GetAppConfigResponseEntity>> getAppConfig(@Query("key") String key);



    @GET(HttpApi.API_GET_COUNTRY_LIST)
    Observable<NetResponseEntity<Map<String,List<CountryCodeEntity>>>> getCountryList();

    @GET(HttpApi.API_GET_PROVINCE_LIST)
    Observable<NetResponseEntity<Map<String,List<ProvinceCodeEntity>>>> getProvinceList(@Query("countryGuid") String id);

    @GET(HttpApi.API_DOWNLOAD_FILE)
    Single<ResponseBody> downloadFirmwareFile(@Query("fileId") String path);
//    Single<ResponseBody> downloadFirmwareFile(@Path("path") String path,);
}
