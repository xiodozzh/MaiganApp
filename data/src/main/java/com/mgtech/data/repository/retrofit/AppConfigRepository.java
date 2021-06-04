package com.mgtech.data.repository.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.AppConfigApi;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.DefaultRequestEntity;
import com.mgtech.domain.entity.net.request.GetAppConfigRequestEntity;
import com.mgtech.domain.entity.net.response.CountryCodeEntity;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ProvinceCodeEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Single;

/**
 * @author zhaixiang
 * App Config
 */
public class AppConfigRepository implements NetRepository.AppConfig {
    private Context context;
    private AppConfigApi api;

    public AppConfigRepository(Context context) {
        this.context = context.getApplicationContext();
        this.api = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .build(context)
                .create(AppConfigApi.class);
    }

    @Override
    public Observable<NetResponseEntity<GetAppConfigResponseEntity>> getAppConfig(GetAppConfigRequestEntity entity) {
        return api.getAppConfig(entity.getKey()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<Map<String,List<CountryCodeEntity>>>> getCountryList() {
        Type type = new TypeToken<NetResponseEntity<Map<String,List<CountryCodeEntity>>>>() {
        }.getType();
        return DataStoreFactory.strategy(context, api.getCountryList().doOnError(new DoOnTokenErrorAction()), new DefaultRequestEntity(HttpApi.API_GET_COUNTRY_LIST),
                NetConstant.CACHE_IF_NET_ERROR, type);
    }

    @Override
    public Observable<NetResponseEntity<Map<String,List<ProvinceCodeEntity>>>> getProvinceList(String code) {
//        Type type = new TypeToken<NetResponseEntity<List<ProvinceCodeEntity>>>() {
//        }.getType();
//        return DataStoreFactory.strategy(context, api.getProvinceList(code), new DefaultRequestEntity(HttpApi.API_GET_PROVINCE_LIST+code),
//                NetConstant.CACHE_IF_NET_ERROR, type);
        return api.getProvinceList(code).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<ResponseBody> downloadFirmwareFile(String url) {
        return api.downloadFirmwareFile(url).doOnError(new DoOnTokenErrorAction());
    }


}
