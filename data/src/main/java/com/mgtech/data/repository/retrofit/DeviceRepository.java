package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.DeviceApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.BindDeviceRequestEntity;
import com.mgtech.domain.entity.net.request.CheckBraceletRequestEntity;
import com.mgtech.domain.entity.net.request.GetBraceletConfigRequestEntity;
import com.mgtech.domain.entity.net.request.GetDeviceBindInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SetBraceletInfoRequestEntity;
import com.mgtech.domain.entity.net.request.UnbindBraceletRequestEntity;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.entity.net.response.CheckBraceletResponseEntity;
import com.mgtech.domain.entity.net.response.GetBindInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;

/**
 *
 * @author zhaixiang
 * 网络请求
 */

public class DeviceRepository implements NetRepository.Device {
    private Context context;
    private DeviceApi deviceApi;

    public DeviceRepository(Context context) {
        this.context = context;
        this.deviceApi = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(DeviceApi.class);
    }

    @Override
    public Observable<NetResponseEntity<CheckBraceletResponseEntity>> checkBraceletCopyright(CheckBraceletRequestEntity entity) {
        return deviceApi.checkBraceletCopyright(entity.getDeviceId(),entity.getMac()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<BraceletConfigEntity>> bindDevice(BindDeviceRequestEntity entity) {
        return deviceApi.bindDevice(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> unbindDevice(UnbindBraceletRequestEntity entity) {
        return deviceApi.unbindDevice(entity.getMac(),entity.getId()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<GetBindInfoResponseEntity>> getBracelet(GetDeviceBindInfoRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<GetBindInfoResponseEntity>>>(){}.getType();
        return DataStoreFactory.strategy(context,deviceApi.getBracelet(entity.getType(),entity.getId()).doOnError(new DoOnTokenErrorAction()),entity,cacheType,type);
    }

    @Override
    public Observable<NetResponseEntity<BraceletConfigEntity>> getConfig(GetBraceletConfigRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<BraceletConfigEntity>>(){}.getType();
        return DataStoreFactory.strategy(context,deviceApi.getConfig(entity.getType(),entity.getId()).doOnError(new DoOnTokenErrorAction()),entity,cacheType,type);
    }

    @Override
    public Observable<NetResponseEntity> setConfig(BraceletConfigEntity entity) {
        return deviceApi.setConfig(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> setInfo(SetBraceletInfoRequestEntity entity) {
        return deviceApi.setInfo(entity).doOnError(new DoOnTokenErrorAction());
    }

}
